package io.github.cafeteriaguild.exdrico.compat

import com.mojang.blaze3d.systems.RenderSystem
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.widgets.Label
import me.shedaniel.rei.api.widgets.Slot
import me.shedaniel.rei.api.widgets.Widgets
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.client.MinecraftClient
import net.minecraft.client.resource.language.I18n
import net.minecraft.item.ItemStack
import uk.me.desert_island.rer.rei_stuff.LootCategory
import uk.me.desert_island.rer.rei_stuff.LootDisplay

class SieveLootCategory: LootCategory() {

    override fun getIdentifier() = REIPlugin.SIEVE

    override fun getLogo(): EntryStack = EntryStack.create(BlockCompendium.ACACIA_SIEVE)

    override fun getCategoryName() = I18n.translate("exdrico.rei.category.sieve")

    override fun setupDisplay(lootDisplay: LootDisplay, bounds: Rectangle): MutableList<Widget> {
        val widgets: MutableList<Widget> = mutableListOf()
        widgets.add(Widgets.createCategoryBase(bounds))

        widgets.add( Widgets.createDrawableWidget { _, _, _, _, _ ->
            RenderSystem.scalef(2f, 2f, 1f)
            MinecraftClient.getInstance().itemRenderer.renderInGui(ItemStack(BlockCompendium.ACACIA_SIEVE), bounds.x/2 + 5, bounds.y/2 + 9)
            RenderSystem.scalef(0.5f, 0.5f, 1f)
        })

        val superWidgets = super.setupDisplay(lootDisplay, bounds)
        widgets.addAll(superWidgets.filter { it !is Label }) //Remove the loot table id widget)
        return widgets
    }

    override fun registerWidget(display: LootDisplay?, widgets: MutableList<Widget>?, bounds: Rectangle?) { }

    override fun getOutputsArea(root: Rectangle): Rectangle {
        return Rectangle(root.x + 46, root.y + 6, root.width - 17 - 36, root.height - 12)
    }

}