package io.github.cafeteriaguild.exdrico.common.items

import net.minecraft.block.BlockState
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ToolMaterial

class HammerItem(material: ToolMaterial, settings: Settings) : PickaxeItem(material, 2, 2f, settings) {

    override fun getMiningSpeedMultiplier(stack: ItemStack?, state: BlockState?): Float = miningSpeed

}