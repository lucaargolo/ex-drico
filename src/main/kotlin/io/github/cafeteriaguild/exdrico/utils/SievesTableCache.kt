package io.github.cafeteriaguild.exdrico.utils

import com.google.gson.Gson
import com.google.gson.JsonSerializationContext
import io.github.cafeteriaguild.exdrico.client.network.ClientPacketCompendium
import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.loot.LootManager
import net.minecraft.loot.LootTable
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

object SievesTableCache {

    val GSON = Gson()

    private var cache: LinkedHashSet<Identifier> = linkedSetOf()

    fun initCache() {
        LootTableLoadingCallback.EVENT.register { _, _, identifier, _, _ ->
            if(identifier.namespace == "exdrico" && identifier.path.startsWith("sieve")) {
                val info = identifier.path.split("/")
                if(info.size == 3) {
                    cache.add(identifier)
                }
            }
        }
    }

    fun syncCache(playerEntity: ServerPlayerEntity) {
        val packetData = PacketByteBufs.create()
        packetData.writeInt(cache.size)
        cache.forEach { identifier ->
            packetData.writeIdentifier(identifier)
        }
        ServerPlayNetworking.send(playerEntity, ClientPacketCompendium.SYNC_SIEVES_TABLES_S2C, packetData)
    }

    fun getCache() = cache

    fun setCache(cache: LinkedHashSet<Identifier>) {
        this.cache = cache
    }

}