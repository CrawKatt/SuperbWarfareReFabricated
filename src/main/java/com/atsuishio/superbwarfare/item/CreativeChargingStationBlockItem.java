package com.atsuishio.superbwarfare.item;

import com.atsuishio.superbwarfare.init.ModBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class CreativeChargingStationBlockItem extends BlockItem {

    public CreativeChargingStationBlockItem() {
        super(ModBlocks.CREATIVE_CHARGING_STATION.get(), new Item.Properties().rarity(Rarity.EPIC).stacksTo(1));
    }
}
