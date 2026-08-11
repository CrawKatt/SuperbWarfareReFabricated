package com.atsuishio.superbwarfare.compat.ponder.scene

import com.atsuishio.superbwarfare.compat.ponder.GeneratedPonderSupport
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3

object PondererExamplePonderScene {

    fun register(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        helper.forComponents(ResourceLocation.fromNamespaceAndPath("minecraft", "writable_book"))
            .addStoryBoard("basic_5x5", PondererExamplePonderScene::introScene)
            .addStoryBoard("basic_5x5", PondererExamplePonderScene::interactScene)
            .addStoryBoard("basic_9x9", PondererExamplePonderScene::thirdScene)
            .addStoryBoard("basic_5x5", PondererExamplePonderScene::fourthScene)
    }

    private fun introScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("ponderer_example_page_1_structure_overlay", "Page 1/4 - Structure & Overlay")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(0, 0, 0), BlockPos(5, 4, 5))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, 1.0f, null)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "show_structure: Displays the loaded structure. Use 'structure' field to reference pool entries.", Vec3(2.5, 1.5, 2.5), 80, "green", true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "text: Floating text with color, pointer and keyframe options.", Vec3(2.5, 2.5, 2.5), 80, "input", true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.showControls(scene, Vec3(4.5, 1.8, 4.5), "down", 60, "right", "minecraft:stick", null, false, true)
        GeneratedPonderSupport.showText(scene, "show_controls: Input hint bubble with action, item and modifier keys.", Vec3(2.5, 3.5, 2.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.encapsulateBounds(scene, BlockPos(5, 4, 5))
        GeneratedPonderSupport.showText(scene, "encapsulate_bounds: Expand the scene bounding box to the given size.", Vec3(2.5, 1.5, 2.5), 60, null, true)
        scene.idle(40)
    }


    private fun interactScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("ponderer_example_page_2_world_blocks", "Page 2/4 - Block Operations")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(0, 0, 0), BlockPos(4, 1, 4))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, 1.0f, null)
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.setBlock(scene, context, "minecraft:stone", null, BlockPos(1, 1, 1), BlockPos(1, 1, 1), null, null, null, null, null, null, null, null, null)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "set_block: Places a block at a position with optional particles.", Vec3(1.5, 1.5, 1.5), 60, "green", true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.replaceBlocks(scene, context, "minecraft:glass", null, BlockPos(0, 0, 0), BlockPos(1, 1, 1), null)
        GeneratedPonderSupport.showText(scene, "replace_blocks: Replaces all blocks in a region with the given block.", Vec3(1.5, 2.5, 1.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.destroyBlock(scene, context, BlockPos(1, 1, 1), null, true)
        GeneratedPonderSupport.showText(scene, "destroy_block: Destroys a block with breaking animation.", Vec3(1.5, 3.5, 1.5), 60, null, true)
        scene.idle(40)
        GeneratedPonderSupport.setBlock(scene, context, "minecraft:lever", null, BlockPos(4, 1, 0), null, null, null, null, null, null, null, null, null, null)
        GeneratedPonderSupport.setBlock(scene, context, "minecraft:redstone_lamp", mapOf("lit" to "false"), BlockPos(4, 1, 1), null, null, null, null, null, null, null, null, null, null)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.toggleRedstonePower(scene, BlockPos(4, 1, 0), BlockPos(4, 1, 4))
        GeneratedPonderSupport.showText(scene, "toggle_redstone_power: Toggles redstone state in a region.", Vec3(4.5, 1.5, 1.5), 60, "red", true)
        scene.idle(40)
        GeneratedPonderSupport.setBlock(scene, context, "minecraft:lime_banner", mapOf("rotation" to "8"), BlockPos(0, 1, 0), null, null, null, null, null, null, null, null, null, null)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.modifyBlockEntity(scene, null, "{Patterns:[{Pattern:\"cre\",Color:13}]}", true, BlockPos(0, 1, 0), null)
        GeneratedPonderSupport.showText(scene, "modify_block_entity_nbt: Modifies a block entity's NBT data. Here a banner's pattern changed visually via reDrawBlocks.", Vec3(1.5, 1.5, 1.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.indicateRedstone(scene, BlockPos(4, 1, 1))
        scene.addKeyframe()
        GeneratedPonderSupport.indicateSuccess(scene, BlockPos(4, 1, 4))
        GeneratedPonderSupport.showText(scene, "indicate_redstone / indicate_success: Visual effect indicators at block positions.", Vec3(4.5, 1.5, 1.5), 60, null, true)
        scene.idle(40)
    }


    private fun thirdScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("ponderer_example_page_3_sections", "Page 3/4 - Section Operations")
        val context = GeneratedPonderSupport.Context()
        GeneratedPonderSupport.preScanBounds(scene, BlockPos(0, 0, 0), BlockPos(2, 3, 2))
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, 0.800000011920929f, null)
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Change Background.", Vec3(2.0, 2.0, 2.0), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.hideSection(scene, context, BlockPos(0, 0, 0), BlockPos(2, 2, 2), 20, "up")
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "hide_section: Hides blocks in a region with a directional animation.", Vec3(1.5, 1.5, 1.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.showSectionAndMerge(scene, context, BlockPos(0, 0, 0), BlockPos(2, 3, 2), "core", 20, "west", null, null, null, null)
        GeneratedPonderSupport.showText(scene, "show_section_and_merge: Shows a section as independent element with a link ID.", Vec3(1.5, 2.5, 1.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.rotateSection(scene, context, "core", null, null, 0.0, 90.0, 0.0, 40)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "rotate_section: Rotates a linked section around X/Y/Z axes over a duration.", Vec3(1.5, 3.5, 1.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.moveSection(scene, context, "core", null, null, Vec3(0.0, 1.5, 0.0), 40)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "move_section: Moves a linked section by the given offset over a duration.", Vec3(1.5, 1.5, 1.5), 60, null, true)
    }


    private fun fourthScene(scene: SceneBuilder, util: SceneBuildingUtil) {
        scene.title("ponderer_example_page_4_entities_camera_sound", "Page 4/4 - Entity, Camera & Sound")
        val context = GeneratedPonderSupport.Context()
        scene.addKeyframe()
        GeneratedPonderSupport.showStructure(scene, context, null, null, null, null)
        scene.idle(20)
        scene.addKeyframe()
        GeneratedPonderSupport.createEntity(scene, context, "minecraft:zombie", Vec3(2.5, 1.0, 2.5), null, 180.0f, 0.0f, null, null, null, null, null)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "create_entity: Spawns an entity. AI and gravity are disabled in ponder scenes.", Vec3(2.5, 2.0, 2.5), 80, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.createItemEntity(scene, context, "minecraft:apple", 3, Vec3(0.5, 2.0, 0.5), Vec3(0.0, 0.0, 0.0), null, null)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "create_item_entity: Spawns an item entity with initial velocity.", Vec3(0.5, 2.0, 0.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.rotateCameraY(scene, 60.0f, 0f, 20)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "rotate_camera_y: Rotates the camera around the Y axis.", Vec3(2.5, 3.0, 2.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.playSound(scene, "minecraft:entity.experience_orb.pickup", 1.0f, 1.0f, "master")
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "play_sound: Plays a sound with configurable source, volume and pitch.", Vec3(2.5, 1.0, 2.5), 60, null, true)
        scene.idle(40)
        scene.addKeyframe()
        GeneratedPonderSupport.clearEntities(scene, context, true, null, null, null, null, null, null, null)
        GeneratedPonderSupport.clearItemEntities(scene, context, true, null, null, null, null)
        scene.addKeyframe()
        GeneratedPonderSupport.showText(scene, "Clear entities.", Vec3(4.5, 1.0, 0.5), 60, null, true)
    }


}
