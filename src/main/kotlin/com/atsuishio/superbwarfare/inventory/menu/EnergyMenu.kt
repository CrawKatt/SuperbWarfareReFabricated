package com.atsuishio.superbwarfare.inventory.menu

import com.atsuishio.superbwarfare.Mod
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyData
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyDataSlot
import com.atsuishio.superbwarfare.network.dataslot.ContainerEnergyDataSlot.Companion.forContainer
import com.atsuishio.superbwarfare.network.message.receive.ContainerDataMessage
import com.atsuishio.superbwarfare.tools.sendPacket
import com.google.common.collect.Lists
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.DataSlot
import net.minecraft.world.inventory.MenuType

abstract class EnergyMenu(pMenuType: MenuType<*>?, id: Int, containerData: ContainerEnergyData) :
    AbstractContainerMenu(pMenuType, id) {
    private val containerEnergyDataSlots: MutableList<ContainerEnergyDataSlot> = Lists.newArrayList()
    private val usingPlayers: MutableList<ServerPlayer> = ArrayList()

    init {
        for (i in 0..<containerData.getCount()) {
            addDataSlot(DataSlot.standalone())
            this.containerEnergyDataSlots.add(forContainer(containerData, i))
        }
    }

    open fun onOpened(player: ServerPlayer) {
        if (!this.usingPlayers.contains(player)) {
            this.usingPlayers.add(player)
        }

        val toSync: MutableList<ContainerDataMessage.Pair> = ArrayList()
        for (i in this.containerEnergyDataSlots.indices) {
            toSync.add(ContainerDataMessage.Pair(i, this.containerEnergyDataSlots[i].get()))
        }
        player.sendPacket(ContainerDataMessage(this.containerId, toSync))
    }

    override fun removed(player: Player) {
        super.removed(player)
        if (player is ServerPlayer) {
            onClosed(player)
        }
    }

    open fun onClosed(player: ServerPlayer) {
        this.usingPlayers.remove(player)
    }

    override fun broadcastChanges() {
        val pairs: MutableList<ContainerDataMessage.Pair> = ArrayList()
        for (i in this.containerEnergyDataSlots.indices) {
            val dataSlot = this.containerEnergyDataSlots[i]
            if (dataSlot.checkAndClearUpdateFlag()) pairs.add(ContainerDataMessage.Pair(i, dataSlot.get()))
        }

        if (!pairs.isEmpty()) {
            this.usingPlayers.forEach { p ->
                p.sendPacket(ContainerDataMessage(this.containerId, pairs))
            }
        }

        super.broadcastChanges()
    }

    override fun setData(id: Int, data: Int) {
        super.setData(id, data)
        if (id < 0 || id >= this.containerEnergyDataSlots.size) {
            Mod.LOGGER.error("EnergyMenu.setData id out of bounds: {}", id)
            return
        }
        this.containerEnergyDataSlots[id].set(data.toLong())
    }
}
