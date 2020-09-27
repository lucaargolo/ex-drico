package io.github.cafeteriaguild.exdrico.common.rei

import io.github.cafeteriaguild.exdrico.common.items.ItemCompendium
import io.github.cafeteriaguild.exdrico.common.items.MeshItem
import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import me.shedaniel.rei.api.EntryRegistry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.plugins.REIPluginV0
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object REIPlugin : REIPluginV0 {
    override fun getPluginIdentifier(): Identifier = ModIdentifier("exdrico_plugin")

    override fun registerEntries(entryRegistry: EntryRegistry?) {
        entryRegistry?.removeEntryIf { s -> s.item is MeshItem }
        MeshType.TYPES.forEach { (id, _) -> entryRegistry?.registerEntries(EntryStack.create(ItemStack(ItemCompendium.MESH).also { it.orCreateTag.putString("mesh", id.toString()) })) }
    }
}