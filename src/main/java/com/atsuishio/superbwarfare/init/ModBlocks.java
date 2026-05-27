package com.atsuishio.superbwarfare.init;

import com.atsuishio.superbwarfare.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;

import java.util.function.Supplier;

public class ModBlocks {

    public static final Supplier<Block> SANDBAG = Registration.block("sandbag",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.SNARE).sound(SoundType.SAND).strength(10f, 20f)));
    public static final Supplier<Block> BARBED_WIRE = Registration.block("barbed_wire", BarbedWireBlock::new);
    public static final Supplier<Block> JUMP_PAD = Registration.block("jump_pad", JumpPadBlock::new);
    public static final Supplier<Block> GALENA_ORE = Registration.block("galena_ore",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3f, 5f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> DEEPSLATE_GALENA_ORE = Registration.block("deepslate_galena_ore",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3f, 8f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> SCHEELITE_ORE = Registration.block("scheelite_ore",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3f, 5f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> DEEPSLATE_SCHEELITE_ORE = Registration.block("deepslate_scheelite_ore",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3f, 8f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> SILVER_ORE = Registration.block("silver_ore",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3f, 5f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> DEEPSLATE_SILVER_ORE = Registration.block("deepslate_silver_ore",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(3f, 8f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> DRAGON_TEETH = Registration.block("dragon_teeth", DragonTeethBlock::new);
    public static final Supplier<Block> REFORGING_TABLE = Registration.block("reforging_table", ReforgingTableBlock::new);
    public static final Supplier<Block> LEAD_BLOCK = Registration.block("lead_block",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.METAL).strength(5f, 6f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> STEEL_BLOCK = Registration.block("steel_block",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.METAL).strength(5f, 6f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> TUNGSTEN_BLOCK = Registration.block("tungsten_block",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.METAL).strength(5f, 6f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> SILVER_BLOCK = Registration.block("silver_block",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.METAL).strength(5f, 6f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> CEMENTED_CARBIDE_BLOCK = Registration.block("cemented_carbide_block",
            () -> new Block(BlockBehaviour.Properties.of().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.METAL).strength(5f, 6f).requiresCorrectToolForDrops()));
    public static final Supplier<Block> CONTAINER = Registration.block("container", ContainerBlock::new);
    public static final Supplier<Block> CHARGING_STATION = Registration.block("charging_station", ChargingStationBlock::new);
    public static final Supplier<Block> CREATIVE_CHARGING_STATION = Registration.block("creative_charging_station", CreativeChargingStationBlock::new);
    public static final Supplier<Block> FUMO_25 = Registration.block("fumo_25", FuMO25Block::new);
    public static final Supplier<Block> SMALL_CONTAINER = Registration.block("small_container", SmallContainerBlock::new);
    public static final Supplier<Block> VEHICLE_DEPLOYER = Registration.block("vehicle_deployer", VehicleDeployerBlock::new);
    public static final Supplier<Block> AIRCRAFT_CATAPULT = Registration.block("aircraft_catapult", AircraftCatapultBlock::new);
    public static final Supplier<Block> SUPERB_ITEM_INTERFACE = Registration.block("superb_item_interface", SuperbItemInterfaceBlock::new);
    public static final Supplier<Block> CREATIVE_SUPERB_ITEM_INTERFACE = Registration.block("creative_superb_item_interface", CreativeSuperbItemInterfaceBlock::new);
    public static final Supplier<Block> LUCKY_CONTAINER = Registration.block("lucky_container", LuckyContainerBlock::new);
    public static final Supplier<Block> VEHICLE_ASSEMBLING_TABLE = Registration.block("vehicle_assembling_table", VehicleAssemblingTableBlock::new);

    public static void register() {

    }
}
