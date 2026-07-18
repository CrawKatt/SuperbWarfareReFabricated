package com.atsuishio.superbwarfare.client.model;

import com.atsuishio.superbwarfare.Mod;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class DragonTeethObjModelLoader {
    private static final ResourceLocation BLOCK_ID = Mod.loc("dragon_teeth");
    private static final ResourceLocation OBJ_LOCATION = Mod.loc("models/block/dragon_teeth.obj");
    private static final ResourceLocation FALLBACK_TEXTURE = Mod.loc("block/dragon_teeth");
    private static final float PLANE_EPSILON = 1.0E-5F;

    private DragonTeethObjModelLoader() {
    }

    public static void register() {
        ModelLoadingPlugin.register(pluginContext -> {
            ModelCache cache = new ModelCache();

            pluginContext.modifyModelAfterBake().register((bakedModel, context) -> {
                ResourceLocation topLevelId = context.id();
                if (bakedModel == null || topLevelId == null || !BLOCK_ID.equals(topLevelId)
                        || bakedModel instanceof DragonTeethBakedModel) {
                    return bakedModel;
                }

                try {
                    BakedObj bakedObj = cache.get().bake(context.textureGetter(), context.settings());
                    return new DragonTeethBakedModel(bakedModel, bakedObj);
                } catch (RuntimeException exception) {
                    cache.logFailure(exception);
                    return bakedModel;
                }
            });
        });
    }

    private static final class ModelCache {
        private ParsedObj model;
        private RuntimeException failure;
        private boolean failureLogged;

        private synchronized ParsedObj get() {
            if (failure != null) {
                throw failure;
            }
            if (model == null) {
                try {
                    model = ParsedObj.load(Minecraft.getInstance().getResourceManager(), OBJ_LOCATION);
                } catch (IOException | RuntimeException exception) {
                    failure = new IllegalStateException("Could not load " + OBJ_LOCATION, exception);
                    throw failure;
                }
            }
            return model;
        }

        private synchronized void logFailure(RuntimeException exception) {
            if (!failureLogged) {
                failureLogged = true;
                Mod.LOGGER.error("Failed to bake the original Dragon Teeth OBJ model", exception);
            }
        }
    }

    private record ParsedObj(
            List<Vector3f> positions,
            List<Vector2f> textureCoordinates,
            List<Vector3f> normals,
            List<ObjFace> faces,
            Map<String, ResourceLocation> materialTextures
    ) {
        private static ParsedObj load(ResourceManager resourceManager, ResourceLocation modelLocation) throws IOException {
            Resource resource = requiredResource(resourceManager, modelLocation);
            List<Vector3f> positions = new ArrayList<>();
            List<Vector2f> textureCoordinates = new ArrayList<>();
            List<Vector3f> normals = new ArrayList<>();
            List<ObjFace> faces = new ArrayList<>();
            Map<String, ResourceLocation> materialTextures = new HashMap<>();
            String currentMaterial = null;

            try (BufferedReader reader = resource.openAsReader()) {
                String rawLine;
                int lineNumber = 0;
                while ((rawLine = reader.readLine()) != null) {
                    lineNumber++;
                    String line = withoutComment(rawLine);
                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] tokens = line.split("\\s+");
                    switch (tokens[0]) {
                        case "v" -> positions.add(parsePosition(tokens, modelLocation, lineNumber));
                        case "vt" -> textureCoordinates.add(parseUv(tokens, modelLocation, lineNumber));
                        case "vn" -> normals.add(parseNormal(tokens, modelLocation, lineNumber));
                        case "mtllib" -> materialTextures.putAll(loadMaterialLibrary(
                                resourceManager,
                                relativeTo(modelLocation, arguments(line, tokens[0]))
                        ));
                        case "usemtl" -> currentMaterial = arguments(line, tokens[0]);
                        case "f" -> addFace(
                                tokens,
                                currentMaterial,
                                positions.size(),
                                textureCoordinates.size(),
                                normals.size(),
                                faces,
                                modelLocation,
                                lineNumber
                        );
                        default -> {}
                    }
                }
            }

            if (positions.isEmpty() || faces.isEmpty()) {
                throw parseError(modelLocation, 0, "OBJ has no renderable geometry", null);
            }
            return new ParsedObj(
                    List.copyOf(positions),
                    List.copyOf(textureCoordinates),
                    List.copyOf(normals),
                    List.copyOf(faces),
                    Map.copyOf(materialTextures)
            );
        }

        private BakedObj bake(Function<Material, TextureAtlasSprite> textureGetter, ModelState modelState) {
            Renderer renderer = RendererAccess.INSTANCE.getRenderer();
            if (renderer == null) {
                throw new IllegalStateException("Fabric Rendering API has no active renderer");
            }

            MeshBuilder meshBuilder = renderer.meshBuilder();
            QuadEmitter emitter = meshBuilder.getEmitter();
            Matrix4f positionTransform = centeredTransform(modelState);
            Matrix3f normalTransform = new Matrix3f(positionTransform);
            if (Math.abs(normalTransform.determinant()) > 1.0E-6F) {
                normalTransform.invert().transpose();
            }

            Map<ResourceLocation, TextureAtlasSprite> spriteCache = new HashMap<>();
            List<TextureAtlasSprite> faceSprites = new ArrayList<>(faces.size());
            for (ObjFace face : faces) {
                ResourceLocation texture = textureFor(face.materialName());
                TextureAtlasSprite sprite = spriteCache.computeIfAbsent(texture, id ->
                        textureGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, id))
                );
                emitFace(emitter, face, sprite, positionTransform, normalTransform);
                faceSprites.add(sprite);
            }
            return new BakedObj(meshBuilder.build(), List.copyOf(faceSprites));
        }

        private ResourceLocation textureFor(@Nullable String materialName) {
            if (materialName == null) {
                return FALLBACK_TEXTURE;
            }
            ResourceLocation texture = materialTextures.get(materialName);
            if (texture == null) {
                throw new IllegalStateException("OBJ references unknown material '" + materialName + "'");
            }
            return texture;
        }

        private void emitFace(
                QuadEmitter emitter,
                ObjFace face,
                TextureAtlasSprite sprite,
                Matrix4f positionTransform,
                Matrix3f normalTransform
        ) {
            Vector3f[] transformedPositions = new Vector3f[4];
            Vector3f[] transformedNormals = new Vector3f[4];
            Vector2f[] uvs = new Vector2f[4];

            for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                ObjVertex vertex = face.vertices().get(Math.min(vertexIndex, face.vertices().size() - 1));
                Vector3f position = new Vector3f(positions.get(vertex.positionIndex()));
                positionTransform.transformPosition(position);
                transformedPositions[vertexIndex] = position;
                uvs[vertexIndex] = vertex.uvIndex() >= 0
                        ? textureCoordinates.get(vertex.uvIndex())
                        : defaultUv(vertexIndex);
            }

            Vector3f geometricNormal = faceNormal(transformedPositions);
            for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                ObjVertex vertex = face.vertices().get(Math.min(vertexIndex, face.vertices().size() - 1));
                if (vertex.normalIndex() < 0) {
                    transformedNormals[vertexIndex] = new Vector3f(geometricNormal);
                    continue;
                }

                Vector3f normal = new Vector3f(normals.get(vertex.normalIndex()));
                normalTransform.transform(normal);
                transformedNormals[vertexIndex] = normal.lengthSquared() > 1.0E-8F
                        ? normal.normalize()
                        : new Vector3f(geometricNormal);
            }

            Direction nominalFace = Direction.getNearest(geometricNormal.x, geometricNormal.y, geometricNormal.z);
            emitter.cullFace(cullFace(transformedPositions, geometricNormal));
            emitter.nominalFace(nominalFace);
            emitter.colorIndex(-1);

            for (int vertexIndex = 0; vertexIndex < 4; vertexIndex++) {
                Vector3f position = transformedPositions[vertexIndex];
                Vector3f normal = transformedNormals[vertexIndex];
                Vector2f uv = uvs[vertexIndex];
                emitter.pos(vertexIndex, position.x, position.y, position.z);
                emitter.color(vertexIndex, 0xFFFFFFFF);
                // NeoForge's OBJ loader defaults flip_v to false; preserve the source UVs verbatim.
                emitter.uv(vertexIndex, sprite.getU(uv.x * 16.0F), sprite.getV(uv.y * 16.0F));
                emitter.normal(vertexIndex, normal.x, normal.y, normal.z);
            }
            emitter.emit();
        }
    }

    private record ObjVertex(int positionIndex, int uvIndex, int normalIndex) {
    }

    private record ObjFace(List<ObjVertex> vertices, @Nullable String materialName) {
    }

    private record BakedObj(Mesh mesh, List<TextureAtlasSprite> faceSprites) {
    }

    private static final class DragonTeethBakedModel implements BakedModel {
        private final BakedModel delegate;
        private final List<BakedQuad> unculledFaces;
        private final Map<Direction, List<BakedQuad>> culledFaces;

        private DragonTeethBakedModel(BakedModel delegate, BakedObj bakedObj) {
            this.delegate = delegate;

            List<BakedQuad> unculled = new ArrayList<>();
            Map<Direction, List<BakedQuad>> culled = new EnumMap<>(Direction.class);
            int[] spriteIndex = {0};
            bakedObj.mesh().forEach(quad -> {
                TextureAtlasSprite sprite = bakedObj.faceSprites().get(spriteIndex[0]++);
                BakedQuad bakedQuad = quad.toBakedQuad(sprite);
                Direction cullFace = quad.cullFace();
                if (cullFace == null) {
                    unculled.add(bakedQuad);
                } else {
                    culled.computeIfAbsent(cullFace, ignored -> new ArrayList<>()).add(bakedQuad);
                }
            });

            this.unculledFaces = List.copyOf(unculled);
            Map<Direction, List<BakedQuad>> immutableCulled = new EnumMap<>(Direction.class);
            culled.forEach((direction, quads) -> immutableCulled.put(direction, List.copyOf(quads)));
            this.culledFaces = Map.copyOf(immutableCulled);
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource random) {
            return side == null ? unculledFaces : culledFaces.getOrDefault(side, List.of());
        }

        @Override
        public boolean useAmbientOcclusion() {
            return delegate.useAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return delegate.isGui3d();
        }

        @Override
        public boolean usesBlockLight() {
            return delegate.usesBlockLight();
        }

        @Override
        public boolean isCustomRenderer() {
            return delegate.isCustomRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleIcon() {
            return delegate.getParticleIcon();
        }

        @Override
        public ItemTransforms getTransforms() {
            return delegate.getTransforms();
        }

        @Override
        public ItemOverrides getOverrides() {
            return delegate.getOverrides();
        }
    }

    private static Map<String, ResourceLocation> loadMaterialLibrary(
            ResourceManager resourceManager,
            ResourceLocation libraryLocation
    ) throws IOException {
        Resource resource = requiredResource(resourceManager, libraryLocation);
        Map<String, ResourceLocation> textures = new HashMap<>();
        String currentMaterial = null;

        try (BufferedReader reader = resource.openAsReader()) {
            String rawLine;
            while ((rawLine = reader.readLine()) != null) {
                String line = withoutComment(rawLine);
                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens = line.split("\\s+");
                if ("newmtl".equals(tokens[0])) {
                    currentMaterial = arguments(line, tokens[0]);
                } else if ("map_Kd".equals(tokens[0]) && currentMaterial != null) {
                    String texturePath = lastArgument(arguments(line, tokens[0]));
                    textures.put(currentMaterial, textureLocation(texturePath, libraryLocation));
                }
            }
        }
        return textures;
    }

    private static void addFace(
            String[] tokens,
            @Nullable String material,
            int positionCount,
            int uvCount,
            int normalCount,
            List<ObjFace> faces,
            ResourceLocation location,
            int lineNumber
    ) {
        if (tokens.length < 4) {
            throw parseError(location, lineNumber, "Face requires at least three vertices", null);
        }

        List<ObjVertex> vertices = new ArrayList<>(tokens.length - 1);
        for (int index = 1; index < tokens.length; index++) {
            String[] indices = tokens[index].split("/", -1);
            if (indices.length > 3 || indices[0].isEmpty()) {
                throw parseError(location, lineNumber, "Invalid face vertex '" + tokens[index] + "'", null);
            }

            int positionIndex = resolveIndex(indices[0], positionCount, location, lineNumber);
            int uvIndex = indices.length > 1 && !indices[1].isEmpty()
                    ? resolveIndex(indices[1], uvCount, location, lineNumber)
                    : -1;
            int normalIndex = indices.length > 2 && !indices[2].isEmpty()
                    ? resolveIndex(indices[2], normalCount, location, lineNumber)
                    : -1;
            vertices.add(new ObjVertex(positionIndex, uvIndex, normalIndex));
        }

        if (vertices.size() <= 4) {
            faces.add(new ObjFace(List.copyOf(vertices), material));
            return;
        }

        for (int index = 1; index < vertices.size() - 1; index++) {
            faces.add(new ObjFace(List.of(vertices.get(0), vertices.get(index), vertices.get(index + 1)), material));
        }
    }

    private static Vector3f parsePosition(String[] tokens, ResourceLocation location, int lineNumber) {
        requireLength(tokens, 4, location, lineNumber);
        float x = parseFloat(tokens[1], location, lineNumber);
        float y = parseFloat(tokens[2], location, lineNumber);
        float z = parseFloat(tokens[3], location, lineNumber);
        float w = tokens.length > 4 ? parseFloat(tokens[4], location, lineNumber) : 1.0F;
        if (Math.abs(w) < 1.0E-8F) {
            throw parseError(location, lineNumber, "Vertex has a zero homogeneous coordinate", null);
        }
        return new Vector3f(x / w, y / w, z / w);
    }

    private static Vector2f parseUv(String[] tokens, ResourceLocation location, int lineNumber) {
        requireLength(tokens, 2, location, lineNumber);
        return new Vector2f(
                parseFloat(tokens[1], location, lineNumber),
                tokens.length > 2 ? parseFloat(tokens[2], location, lineNumber) : 0.0F
        );
    }

    private static Vector3f parseNormal(String[] tokens, ResourceLocation location, int lineNumber) {
        requireLength(tokens, 4, location, lineNumber);
        Vector3f normal = new Vector3f(
                parseFloat(tokens[1], location, lineNumber),
                parseFloat(tokens[2], location, lineNumber),
                parseFloat(tokens[3], location, lineNumber)
        );
        return normal.lengthSquared() > 1.0E-8F ? normal.normalize() : normal;
    }

    private static int resolveIndex(String token, int count, ResourceLocation location, int lineNumber) {
        final int rawIndex;
        try {
            rawIndex = Integer.parseInt(token);
        } catch (NumberFormatException exception) {
            throw parseError(location, lineNumber, "Invalid OBJ index '" + token + "'", exception);
        }

        int resolved = rawIndex > 0 ? rawIndex - 1 : count + rawIndex;
        if (rawIndex == 0 || resolved < 0 || resolved >= count) {
            throw parseError(location, lineNumber, "OBJ index " + rawIndex + " is out of bounds", null);
        }
        return resolved;
    }

    private static Matrix4f centeredTransform(ModelState modelState) {
        return new Matrix4f()
                .translation(0.5F, 0.5F, 0.5F)
                .mul(modelState.getRotation().getMatrix())
                .translate(-0.5F, -0.5F, -0.5F);
    }

    private static Vector3f faceNormal(Vector3f[] positions) {
        Vector3f firstEdge = new Vector3f(positions[1]).sub(positions[0]);
        Vector3f secondEdge = new Vector3f(positions[2]).sub(positions[0]);
        Vector3f normal = firstEdge.cross(secondEdge);
        return normal.lengthSquared() > 1.0E-8F ? normal.normalize() : new Vector3f(0.0F, 1.0F, 0.0F);
    }

    @Nullable
    private static Direction cullFace(Vector3f[] positions, Vector3f normal) {
        if (allOnPlane(positions, 0, 0.0F) && normal.x < 0.0F) return Direction.WEST;
        if (allOnPlane(positions, 0, 1.0F) && normal.x > 0.0F) return Direction.EAST;
        if (allOnPlane(positions, 1, 0.0F) && normal.y < 0.0F) return Direction.DOWN;
        if (allOnPlane(positions, 1, 1.0F) && normal.y > 0.0F) return Direction.UP;
        if (allOnPlane(positions, 2, 0.0F) && normal.z < 0.0F) return Direction.NORTH;
        if (allOnPlane(positions, 2, 1.0F) && normal.z > 0.0F) return Direction.SOUTH;
        return null;
    }

    private static boolean allOnPlane(Vector3f[] positions, int axis, float value) {
        for (Vector3f position : positions) {
            float component = axis == 0 ? position.x : axis == 1 ? position.y : position.z;
            if (Math.abs(component - value) > PLANE_EPSILON) {
                return false;
            }
        }
        return true;
    }

    private static Vector2f defaultUv(int index) {
        return switch (index) {
            case 0 -> new Vector2f(0.0F, 0.0F);
            case 1 -> new Vector2f(0.0F, 1.0F);
            case 2 -> new Vector2f(1.0F, 1.0F);
            default -> new Vector2f(1.0F, 0.0F);
        };
    }

    private static Resource requiredResource(ResourceManager manager, ResourceLocation location) throws IOException {
        return manager.getResource(location).orElseThrow(() -> new FileNotFoundException(location.toString()));
    }

    private static ResourceLocation relativeTo(ResourceLocation base, String path) {
        if (path.indexOf(':') >= 0) {
            return new ResourceLocation(path);
        }
        int slash = base.getPath().lastIndexOf('/');
        String directory = slash >= 0 ? base.getPath().substring(0, slash + 1) : "";
        return new ResourceLocation(base.getNamespace(), directory + path);
    }

    private static ResourceLocation textureLocation(String path, ResourceLocation materialLibrary) {
        String normalized = path.replace('\\', '/');
        String namespace = materialLibrary.getNamespace();
        if (normalized.indexOf(':') >= 0) {
            ResourceLocation parsed = new ResourceLocation(normalized);
            namespace = parsed.getNamespace();
            normalized = parsed.getPath();
        }
        if (normalized.startsWith("textures/")) normalized = normalized.substring("textures/".length());
        if (normalized.endsWith(".png")) normalized = normalized.substring(0, normalized.length() - 4);
        return new ResourceLocation(namespace, normalized);
    }

    private static String withoutComment(String line) {
        int comment = line.indexOf('#');
        return (comment >= 0 ? line.substring(0, comment) : line).trim();
    }

    private static String arguments(String line, String command) {
        String arguments = line.substring(command.length()).trim();
        if (arguments.isEmpty()) {
            throw new IllegalArgumentException("Missing arguments for " + command);
        }
        return arguments;
    }

    private static String lastArgument(String arguments) {
        int lastSpace = arguments.lastIndexOf(' ');
        return lastSpace >= 0 ? arguments.substring(lastSpace + 1) : arguments;
    }

    private static void requireLength(String[] tokens, int length, ResourceLocation location, int lineNumber) {
        if (tokens.length < length) {
            throw parseError(location, lineNumber, "Not enough values for '" + tokens[0] + "'", null);
        }
    }

    private static float parseFloat(String value, ResourceLocation location, int lineNumber) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException exception) {
            throw parseError(location, lineNumber, "Invalid number '" + value + "'", exception);
        }
    }

    private static IllegalArgumentException parseError(
            ResourceLocation location,
            int lineNumber,
            String message,
            @Nullable Throwable cause
    ) {
        String source = lineNumber > 0 ? location + ":" + lineNumber : location.toString();
        return new IllegalArgumentException(source + ": " + message, cause);
    }
}
