package io.github.cafeteriaguild.exdrico.client.network

import io.github.cafeteriaguild.exdrico.common.blockentities.VatBlockEntity
import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketContext
import net.minecraft.network.PacketByteBuf

object ClientPacketCompendium {

    val SYNC_MESH_DATA_S2C = ModIdentifier("sync_mesh_data")
    val CLEAR_VAT_COLOR_S2C = ModIdentifier("clear_vat_color")

    fun initPackets() {

        ClientSidePacketRegistry.INSTANCE.register(SYNC_MESH_DATA_S2C) { packetContext: PacketContext, attachedData: PacketByteBuf ->
            MeshType.TYPES.clear()
            val qnt = attachedData.readInt()
            repeat(qnt) {
                val id = attachedData.readIdentifier()
                val meshType = MeshType.readFromBuf(attachedData)
                MeshType.TYPES[id] = meshType
            }
        }

        ClientSidePacketRegistry.INSTANCE.register(CLEAR_VAT_COLOR_S2C) { packetContext: PacketContext, attachedData: PacketByteBuf ->
            val pos = attachedData.readBlockPos()
            packetContext.taskQueue.run {
                (packetContext.player.world.getBlockEntity(pos) as? VatBlockEntity)?.let {
                    it.sumr = 0
                    it.sumg = 0
                    it.sumb = 0
                    it.sumQnt = 0
                }
            }
        }

    }

}