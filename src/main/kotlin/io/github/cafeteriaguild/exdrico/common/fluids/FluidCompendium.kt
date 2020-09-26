package io.github.cafeteriaguild.exdrico.common.fluids

import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.FluidBlock
import net.minecraft.block.Material
import net.minecraft.item.BucketItem
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry

object FluidCompendium {

    val WITCH_WATER_IDENTIFIER = ModIdentifier("witch_water")
    val WITCH_WATER_STILL: BaseFluid.Still = BaseFluid.Still(WITCH_WATER_IDENTIFIER, { WITCH_WATER }, { WITCH_WATER_BUCKET }, 0x0C2340) { WITCH_WATER_FLOWING }
    val WITCH_WATER_FLOWING = BaseFluid.Flowing(WITCH_WATER_IDENTIFIER, { WITCH_WATER }, { WITCH_WATER_BUCKET }, 0x0C2340) { WITCH_WATER_STILL }
    val WITCH_WATER_BUCKET = BucketItem(WITCH_WATER_STILL, Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1))
    val WITCH_WATER = object : FluidBlock(WITCH_WATER_STILL, FabricBlockSettings.of(Material.LAVA)) {}

    val SEA_WATER_IDENTIFIER = ModIdentifier("sea_water")
    val SEA_WATER_STILL: BaseFluid.Still = BaseFluid.Still(SEA_WATER_IDENTIFIER, { SEA_WATER }, { SEA_WATER_BUCKET }, 0x992340) { SEA_WATER_FLOWING }
    val SEA_WATER_FLOWING = BaseFluid.Flowing(SEA_WATER_IDENTIFIER, { SEA_WATER }, { SEA_WATER_BUCKET }, 0x992340) { SEA_WATER_STILL }
    val SEA_WATER_BUCKET = BucketItem(SEA_WATER_STILL, Item.Settings().recipeRemainder(Items.BUCKET).maxCount(1))
    val SEA_WATER = object : FluidBlock(SEA_WATER_STILL, FabricBlockSettings.of(Material.LAVA)) {}

    fun initFluids() {
        Registry.register(Registry.BLOCK, WITCH_WATER_IDENTIFIER, WITCH_WATER)
        Registry.register(Registry.FLUID, ModIdentifier("${WITCH_WATER_IDENTIFIER.path}_still"), WITCH_WATER_STILL)
        Registry.register(Registry.FLUID, ModIdentifier("${WITCH_WATER_IDENTIFIER.path}_flowing"), WITCH_WATER_FLOWING)
        Registry.register(Registry.ITEM, ModIdentifier("${WITCH_WATER_IDENTIFIER.path}_bucket"), WITCH_WATER_BUCKET)

        Registry.register(Registry.BLOCK, SEA_WATER_IDENTIFIER, SEA_WATER)
        Registry.register(Registry.FLUID, ModIdentifier("${SEA_WATER_IDENTIFIER.path}_still"), SEA_WATER_STILL)
        Registry.register(Registry.FLUID, ModIdentifier("${SEA_WATER_IDENTIFIER.path}_flowing"), SEA_WATER_FLOWING)
        Registry.register(Registry.ITEM, ModIdentifier("${SEA_WATER_IDENTIFIER.path}_bucket"), SEA_WATER_BUCKET)

        WITCH_WATER_STILL.registerFluidKey()
        SEA_WATER_STILL.registerFluidKey()
    }
}