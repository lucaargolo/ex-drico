package io.github.cafeteriaguild.exdrico.compat

import io.github.cafeteriaguild.exdrico.common.blockentities.SieveBlockEntity
import io.github.cafeteriaguild.exdrico.common.blocks.SieveBlock
import io.github.cafeteriaguild.exdrico.common.blocks.VatBlock
import io.github.cafeteriaguild.exdrico.common.items.ItemCompendium
import io.github.cafeteriaguild.exdrico.common.items.MeshItem
import io.github.cafeteriaguild.exdrico.common.meshes.MeshType
import io.github.cafeteriaguild.exdrico.common.recipes.VatRecipe
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import io.github.cafeteriaguild.exdrico.utils.SievesTableCache
import me.shedaniel.rei.api.EntryRegistry
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.RecipeHelper
import me.shedaniel.rei.api.plugins.REIPluginV0
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.loot.context.LootContext
import net.minecraft.util.Identifier
import net.minecraft.util.collection.WeightedList
import net.minecraft.util.registry.Registry

object REIPlugin : REIPluginV0 {

    val VAT = ModIdentifier("plugins/vat")
    val SIEVE = ModIdentifier("plugins/sieve")
    val CRUCIBLE = ModIdentifier("plugins/crucible")

    val VAT_CATEGORY = VatRecipeCategory()
    val SIEVE_CATEGORY = SieveLootCategory()

    override fun getPluginIdentifier(): Identifier = ModIdentifier("exdrico_plugin")

    @Suppress("UnstableApiUsage")
    override fun registerEntries(entryRegistry: EntryRegistry?) {
        entryRegistry?.removeEntryIf { s -> s.item is MeshItem }
        MeshType.TYPES.forEach { (id, _) -> entryRegistry?.registerEntries(EntryStack.create(ItemStack(ItemCompendium.MESH).also {
            it.orCreateTag.putString("mesh", id.toString())
        })) }
    }

    override fun registerPluginCategories(recipeHelper: RecipeHelper) {
        recipeHelper.registerCategory(VAT_CATEGORY)
        recipeHelper.registerCategory(SIEVE_CATEGORY)
    }

    override fun registerRecipeDisplays(recipeHelper: RecipeHelper) {
        recipeHelper.registerRecipes(VAT, VatRecipe::class.java) {
            VatRecipeDisplay(it)
        }

        SievesTableCache.getCache().forEach { identifier ->
            val info = identifier.path.split("/")
            if(info.size == 3) {
                val mesh = MeshType.TYPES[ModIdentifier(info[1])] ?: MeshType.EMPTY
                val block = Registry.BLOCK.get(Identifier(info[2]))
                recipeHelper.registerDisplay(SieveLootDisplay(block, mesh, identifier))
            }
        }
    }

    override fun registerOthers(recipeHelper: RecipeHelper) {
        recipeHelper.removeAutoCraftButton(VAT)
        recipeHelper.removeAutoCraftButton(SIEVE)
        VatBlock.vatMap.forEach { (_, vat) ->
            recipeHelper.registerWorkingStations(VAT, EntryStack.create(vat))
        }
        SieveBlock.sieveMap.forEach { (_, sieve) ->
            recipeHelper.registerWorkingStations(SIEVE, EntryStack.create(sieve))
        }
    }

}