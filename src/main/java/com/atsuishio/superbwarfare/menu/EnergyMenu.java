package com.atsuishio.superbwarfare.menu;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.NetworkRegistry;
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyData;
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyDataSlot;
import com.atsuishio.superbwarfare.network.message.receive.ContainerDataMessage;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class EnergyMenu extends AbstractContainerMenu {

    private final List<ContainerEnergyDataSlot> containerEnergyDataSlots = Lists.newArrayList();
    private final List<ServerPlayer> usingPlayers = new ArrayList<>();

    public EnergyMenu(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    public EnergyMenu(@Nullable MenuType<?> pMenuType, int id, ContainerEnergyData containerData) {
        super(pMenuType, id);

        for (int i = 0; i < containerData.getCount(); ++i) {
            this.containerEnergyDataSlots.add(ContainerEnergyDataSlot.forContainer(containerData, i));
        }
    }

    public void startUsing(ServerPlayer serverPlayer) {
        this.usingPlayers.add(serverPlayer);

        List<ContainerDataMessage.Pair> toSync = new ArrayList<>();
        for (int i = 0; i < this.containerEnergyDataSlots.size(); ++i) {
            toSync.add(new ContainerDataMessage.Pair(i, this.containerEnergyDataSlots.get(i).get()));
        }

        NetworkRegistry.sendToPlayer(serverPlayer, new ContainerDataMessage(this.containerId, toSync));
    }

    public void stopUsing(ServerPlayer serverPlayer) {
        this.usingPlayers.remove(serverPlayer);
    }

    @Override
    public void removed(net.minecraft.world.entity.player.Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            this.stopUsing(serverPlayer);
        }

        super.removed(player);
    }

    @Override
    public void broadcastChanges() {
        List<ContainerDataMessage.Pair> pairs = new ArrayList<>();
        for (int i = 0; i < this.containerEnergyDataSlots.size(); ++i) {
            ContainerEnergyDataSlot dataSlot = this.containerEnergyDataSlots.get(i);
            if (dataSlot.checkAndClearUpdateFlag())
                pairs.add(new ContainerDataMessage.Pair(i, dataSlot.get()));
        }

        if (!pairs.isEmpty()) {
            NetworkRegistry.sendToPlayers(this.usingPlayers, new ContainerDataMessage(this.containerId, pairs));
        }

        super.broadcastChanges();
    }

    public void setData(int id, int data) {
        if (id < 0 || id >= this.containerEnergyDataSlots.size()) {
            Mod.LOGGER.error("EnergyMenu.setData id out of bounds: {}", id);
            return;
        }
        this.containerEnergyDataSlots.get(id).set(data);
    }

    public void setData(int id, long data) {
        if (id < 0 || id >= this.containerEnergyDataSlots.size()) {
            Mod.LOGGER.error("EnergyMenu.setData id out of bounds: {}", id);
            return;
        }
        this.containerEnergyDataSlots.get(id).set(data);
    }
}
