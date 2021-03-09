package io.github.cafeteriaguild.exdrico.client.network

import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import io.github.cafeteriaguild.exdrico.utils.SievesTableCache
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.util.Identifier

object ClientPacketCompendium {

    val SYNC_MESH_DATA_S2C = ModIdentifier("sync_mesh_data")
    val SYNC_SIEVES_TABLES_S2C = ModIdentifier("sync_sieves_tables")

    fun initPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SYNC_MESH_DATA_S2C) { client, _, buf, _ ->
            MeshType.TYPES.clear()
            val qnt = buf.readInt()
            repeat(qnt) {
                val id = buf.readIdentifier()
                val meshType = MeshType.readFromBuf(buf)
                MeshType.TYPES[id] = meshType
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(SYNC_SIEVES_TABLES_S2C) { client, _, buf, _ ->
            val newCache = linkedSetOf<Identifier>()
            val size = buf.readInt()
            (0 until size).forEach {
                newCache.add(buf.readIdentifier())
            }
            SievesTableCache.setCache(newCache)
        }
    }
}