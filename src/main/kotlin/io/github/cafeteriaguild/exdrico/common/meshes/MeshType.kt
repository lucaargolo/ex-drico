package io.github.cafeteriaguild.exdrico.common.meshes

import io.github.cafeteriaguild.exdrico.client.network.ClientPacketCompendium
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

data class MeshType(val texture: Identifier) {

    val identifier: Identifier
        get() {
            TYPES.forEach { (identifier, mesh) -> if(this == mesh) return identifier }
            return Identifier("missingno")
        }

    companion object {
        val EMPTY = MeshType(Identifier("missingno"))
        val TYPES = mutableMapOf<Identifier, MeshType>()

        fun readFromBuf(packetByteBuf: PacketByteBuf): MeshType {
            val texture = packetByteBuf.readIdentifier()
            return MeshType(texture)
        }

        fun syncTypes(playerEntity: ServerPlayerEntity) {
            val packetData = PacketByteBuf(Unpooled.buffer())
            packetData.writeInt(TYPES.size)
            TYPES.forEach { (identifier, meshType) ->
                packetData.writeIdentifier(identifier)
                meshType.writeToBuf(packetData)
            }
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerEntity, ClientPacketCompendium.SYNC_MESH_DATA_S2C, packetData)
        }
    }

    fun writeToBuf(packetByteBuf: PacketByteBuf) {
        packetByteBuf.writeIdentifier(texture)
    }
}