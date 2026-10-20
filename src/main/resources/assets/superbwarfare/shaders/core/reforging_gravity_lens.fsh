#version 150

uniform sampler2D SceneColor;
uniform sampler2D SceneDepth;
uniform mat4 LensProjection;
uniform mat4 InverseProjection;
uniform int LensCount;
uniform float LensTime;
uniform vec4 Lens0;
uniform vec4 Lens1;
uniform vec4 Lens2;
uniform vec4 Lens3;
uniform vec4 Lens4;
uniform vec4 Lens5;
uniform vec4 Lens6;
uniform vec4 Lens7;
uniform vec4 LensStrengths0;
uniform vec4 LensStrengths1;

in vec2 texCoord;
out vec4 fragColor;

vec3 viewPosition(vec2 uv, float depth) {
    vec4 position = InverseProjection * vec4(uv * 2.0 - 1.0, depth * 2.0 - 1.0, 1.0);
    return position.xyz / position.w;
}

vec4 lensAt(int index) {
    if (index == 0) return Lens0;
    if (index == 1) return Lens1;
    if (index == 2) return Lens2;
    if (index == 3) return Lens3;
    if (index == 4) return Lens4;
    if (index == 5) return Lens5;
    if (index == 6) return Lens6;
    return Lens7;
}

void main() {
    vec2 halfTexel = 0.5 / vec2(textureSize(SceneColor, 0));
    vec3 nearPoint = viewPosition(texCoord, 0.0);
    vec3 ray = normalize(viewPosition(texCoord, 1.0) - nearPoint);
    vec3 surface = viewPosition(texCoord, texture(SceneDepth, texCoord).r);
    float surfaceDistance = dot(surface - nearPoint, ray);
    vec2 displacement = vec2(0.0);

    for (int index = 0; index < 8; ++index) {
        if (index >= LensCount) break;
        vec4 lens = lensAt(index);
        float strength = index < 4 ? LensStrengths0[index] : LensStrengths1[index - 4];
        float distanceAlongRay = dot(lens.xyz - nearPoint, ray);
        if (distanceAlongRay <= 0.0 || lens.w <= 0.0 || strength <= 0.0) continue;
        vec3 separation = nearPoint + ray * distanceAlongRay - lens.xyz;
        float impact = length(separation) / lens.w;
        if (impact >= 1.0) continue;

        float front = distanceAlongRay - lens.w * sqrt(max(1.0 - impact * impact, 0.0));
        // Foreground walls and entities retain their original pixels, even across the lens edge.
        float visibility = smoothstep(front, front + lens.w * 0.05, surfaceDistance);
        float envelope = smoothstep(0.30, 0.48, impact) * pow(1.0 - impact, 2.0);
        if (visibility <= 0.0 || envelope <= 0.0) continue;

        vec4 clip = LensProjection * vec4(lens.xyz, 1.0);
        vec2 radial = texCoord - (clip.xy / clip.w * 0.5 + 0.5);
        float deflection = strength * envelope / (impact * impact + 0.08);
        vec2 bend = -radial * deflection;
        // A restrained twist gives the otherwise radial lens a slowly moving edge.
        bend += vec2(-radial.y, radial.x) * deflection * 0.08 * sin(LensTime * 0.7 + index);
        vec2 sourceUv = clamp(texCoord + bend, halfTexel, vec2(1.0) - halfTexel);
        vec3 source = viewPosition(sourceUv, texture(SceneDepth, sourceUv).r);
        float sourceVisibility = smoothstep(front, front + lens.w * 0.05, dot(source - nearPoint, ray));
        // Do not pull the already-rendered black core outward into duplicate dark silhouettes.
        sourceVisibility *= smoothstep(lens.w * 0.42, lens.w * 0.50, length(source - lens.xyz));
        displacement += bend * visibility * sourceVisibility;
    }

    // All lenses read the same snapshot, so overlapping lenses need only one scene copy per frame.
    vec2 uv = clamp(texCoord + displacement, halfTexel, vec2(1.0) - halfTexel);
    fragColor = texture(SceneColor, uv);
}
