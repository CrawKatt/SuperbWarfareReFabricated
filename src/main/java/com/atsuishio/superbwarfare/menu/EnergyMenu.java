package com.atsuishio.superbwarfare.menu;

import com.atsuishio.superbwarfare.Mod;
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyData;
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyDataSlot;
import com.atsuishio.superbwarfare.network.message.receive.ContainerDataMessage;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class EnergyMenu extends AbstractContainerMenu {

    private final List<ContainerEnergyDataSlot> containerEnergyDataSlots = Lists.newArrayList();
    private final List<ServerPlayer> usingPlayers = new ArrayList<>();

    public EnergyMenu(@Nullable MenuType<?> pMenuType, int id, ContainerEnergyData containerData) {
        super(pMenuType, id);

        for (int i = 0; i < containerData.getCount(); ++i) {
            addDataSlot(DataSlot.standalone());
            this.containerEnergyDataSlots.add(ContainerEnergyDataSlot.forContainer(containerData, i));
        }
    }

    @Override
    public void addSlotListener(ContainerListener listener) {
        super.addSlotListener(listener);
        if (listener instanceof ServerPlayer serverPlayer) {
            onOpened(serverPlayer);
        }
    }

    protected void onOpened(ServerPlayer player) {
        this.usingPlayers.add(player);

        List<ContainerDataMessage.Pair> toSync = new ArrayList<>();
        for (int i = 0; i < this.containerEnergyDataSlots.size(); ++i) {
            toSync.add(new ContainerDataMessage.Pair(i, this.containerEnergyDataSlots.get(i).get()));
        }
        ServerPlayNetworking.send(player, new ContainerDataMessage(this.containerId, toSync));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (player instanceof ServerPlayer serverPlayer) {
            onClosed(serverPlayer);
        }
    }

    protected void onClosed(ServerPlayer player) {
        this.usingPlayers.remove(player);
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
            this.usingPlayers.forEach(p -> ServerPlayNetworking.send(p, new ContainerDataMessage(this.containerId, pairs)));
        }

        super.broadcastChanges();
    }

    public void setData(int id, int data) {
        super.setData(id, data);
        if (id < 0 || id >= this.containerEnergyDataSlots.size()) {
            Mod.LOGGER.error("EnergyMenu.setData id out of bounds: {}", id);
            return;
        }
        this.containerEnergyDataSlots.get(id).set(data);
    }
}
