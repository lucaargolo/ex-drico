package io.github.cafeteriaguild.exdrico.common.meshes

import io.github.cafeteriaguild.exdrico.client.network.ClientPacketCompendium
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

data class MeshType(val texture: Identifier) {

    val identifier: Identifier
        get() {
            TYPES.forEach { (identifier, mesh) -> if(this == mesh) return identifier }
            return Identifier("missingno")
        }

    val spriteIdentifier: SpriteIdentifier
        get() = SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, texture)

    val sprite: Sprite
        get() = spriteIdentifier.sprite

    companion object {
        val EMPTY = MeshType(Identifier("missingno"))
        val TYPES = mutableMapOf<Identifier, MeshType>()

        fun readFromBuf(packetByteBuf: PacketByteBuf): MeshType {
            val texture = packetByteBuf.readIdentifier()
            return MeshType(texture)
        }

        fun syncTypes(playerEntity: ServerPlayerEntity) {
            val packetData = PacketByteBufs.create()
            packetData.writeInt(TYPES.size)
            TYPES.forEach { (identifier, meshType) ->
                packetData.writeIdentifier(identifier)
                meshType.writeToBuf(packetData)
            }
            ServerPlayNetworking.send(playerEntity, ClientPacketCompendium.SYNC_MESH_DATA_S2C, packetData)
        }
    }

    fun writeToBuf(packetByteBuf: PacketByteBuf) {
        packetByteBuf.writeIdentifier(texture)
    }
}