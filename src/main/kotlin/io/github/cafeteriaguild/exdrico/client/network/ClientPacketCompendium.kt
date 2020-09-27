package io.github.cafeteriaguild.exdrico.client.network

import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketContext
import net.minecraft.network.PacketByteBuf

object ClientPacketCompendium {

    val SYNC_MESH_DATA_S2C = ModIdentifier("sync_mesh_data")

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
    }
}