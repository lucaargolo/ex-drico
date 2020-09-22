package io.github.cafeteriaguild.exdrico.common.meshes

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import blue.endless.jankson.Jankson
import blue.endless.jankson.JsonArray
import blue.endless.jankson.JsonObject
import blue.endless.jankson.JsonPrimitive
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class MeshResource : SimpleSynchronousResourceReloadListener {

    override fun apply(manager: ResourceManager?) {
        MeshType.TYPES.clear()
        val jankson = Jankson.builder().build()
        val meshes = manager?.findResources("meshes") { r -> r.endsWith(".json") || r.endsWith(".json5") }
        meshes?.forEach { mesh ->
            val json = jankson.load(manager.getResource(mesh).inputStream)
            val arr = json["meshes"] as JsonArray
            arr.forEach {
                it as JsonObject
                val id = (it["id"] as JsonPrimitive).asString()
                val texture = (it["texture"] as JsonPrimitive).asString()
                val meshType = MeshType(Identifier(texture))
                MeshType.TYPES[Identifier(id)] = meshType
            }
        }
        LOGGER.info("Loaded ${MeshType.TYPES.size} meshes!")
    }

    override fun getFabricId(): Identifier = ModIdentifier("meshes")

    companion object {
        val LOGGER: Logger = LogManager.getLogger("Meshes Resource Reloader")
    }
}