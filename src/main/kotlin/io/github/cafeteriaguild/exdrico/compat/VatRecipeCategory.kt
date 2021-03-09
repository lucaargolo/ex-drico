package io.github.cafeteriaguild.exdrico.compat

import alexiil.mc.lib.attributes.fluid.amount.FluidAmount
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import com.mojang.blaze3d.systems.RenderSystem
import io.github.cafeteriaguild.exdrico.common.blocks.BlockCompendium
import it.unimi.dsi.fastutil.ints.IntList
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.TransferRecipeCategory
import me.shedaniel.rei.api.widgets.Widgets
import me.shedaniel.rei.gui.widget.Widget
import net.minecraft.block.Block
import net.minecraft.client.MinecraftClient
import net.minecraft.client.resource.language.I18n
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemStack

class VatRecipeCategory: TransferRecipeCategory<VatRecipeDisplay> {

    override fun getIdentifier() = REIPlugin.VAT

    override fun getCategoryName() = I18n.translate("exdrico.rei.category.vat")

    override fun getLogo(): EntryStack = EntryStack.create(BlockCompendium.ACACIA_VAT)

    override fun renderRedSlots(matrices: MatrixStack?, widgets: MutableList<Widget>?, bounds: Rectangle?, display: VatRecipeDisplay?, redSlots: IntList?) { }

    override fun setupDisplay(recipeDisplay: VatRecipeDisplay, bounds: Rectangle): MutableList<Widget> {
        val widgets: MutableList<Widget> = mutableListOf()
        widgets.add(Widgets.createCategoryBase(bounds))

        val hasFluidInput = recipeDisplay.requiredEntries[1].size > 0
        val hasBlockBellow = recipeDisplay.requiredEntries[2].size > 0

        var necessaryFluid: FluidVolume? = null
        var necessaryBlock: Block? = null
        recipeDisplay.requiredEntries.forEachIndexed { index, entries ->
            when(index) {
                0 -> {
                    //Necessary items
                    if(entries.size > 1) {
                        val inputOffset = if (hasFluidInput) 50 else 10
                        val inputSize = if (hasFluidInput) 2 else 3

                        (0..2).forEach { y ->
                            (0..inputSize).forEach { x ->
                                val inputPoint = Point(bounds.x + inputOffset + (x * 18), bounds.y + 7 + (y * 18))
                                if (entries.size >= x+(y*inputSize) && !entries[x+(y*inputSize)].isEmpty) {
                                    widgets.add(Widgets.createSlot(inputPoint).entry(entries[x+(y*inputSize)]))
                                }
                            }
                        }
                    }else if(entries.size > 0) {
                        val inputPoint = Point(bounds.x + 64, bounds.y + 7 + 18)
                        widgets.add(Widgets.createSlot(inputPoint).entry(entries[0]))
                    }

                }
                1 -> {
                    //Necessary fluid
                    if(entries.size > 0) {
                        val fluid = entries[0].fluid
                        val amount = entries[0].amount
                        necessaryFluid = FluidKeys.get(fluid).withAmount(FluidAmount.BUCKET)
                    }
                }
                2 -> {
                    //Necessary block bellow
                    if(entries.size > 0) {
                        necessaryBlock = (entries[0].item as? BlockItem)?.block
                    }
                }
            }
        }

        necessaryBlock?.let {
            widgets.add( Widgets.createDrawableWidget { _, _, _, _, _ ->
                RenderSystem.scalef(2f, 2f, 1f)
                MinecraftClient.getInstance().itemRenderer.renderInGui(ItemStack(it), bounds.x/2 + 5, bounds.y/2 + 14)
                RenderSystem.scalef(0.5f, 0.5f, 1f)
            })
        }

        necessaryFluid?.let {
            val vatOffset = if(hasBlockBellow) 5 else 9

            widgets.add( Widgets.createDrawableWidget { _, _, _, _, _ ->
                RenderSystem.scalef(2f, 2f, 1f)
                RenderSystem.translatef(0f, 0f, 400f)
                MinecraftClient.getInstance().itemRenderer.renderInGui(ItemStack(BlockCompendium.ACACIA_VAT), bounds.x/2 + 5, bounds.y/2 + vatOffset)
                RenderSystem.translatef(0f, 0f, -400f)
                RenderSystem.scalef(0.5f, 0.5f, 1f)
            })
        }

        recipeDisplay.resultingEntries.forEachIndexed { index, entries ->
            when(index) {
                0 -> {
                    //Output block
                    if(entries.isNotEmpty() && entries.find { !it.isEmpty } != null) {
                        val arrowPoint = Point(bounds.x + 89, bounds.y + 7 + 18)
                        widgets.add(Widgets.createArrow(arrowPoint))

                        val outputPoint = Point(bounds.x + 122, bounds.y + 7 + 18)
                        widgets.add(Widgets.createSlot(outputPoint).entries(entries))
                    }
                }
                1 -> {
                    //Output fluid
                    if(entries.isNotEmpty()) {
                        val arrowPoint = Point(bounds.x + 63, bounds.y + 7 + 20)
                        widgets.add(Widgets.createArrow(arrowPoint))

                        necessaryBlock?.let {
                            widgets.add( Widgets.createDrawableWidget { _, _, _, _, _ ->
                                RenderSystem.scalef(2f, 2f, 1f)
                                MinecraftClient.getInstance().itemRenderer.renderInGui(ItemStack(it), bounds.x/2 + 55, bounds.y/2 + 14)
                                RenderSystem.scalef(0.5f, 0.5f, 1f)
                            })
                        }

                        widgets.add( Widgets.createDrawableWidget { _, _, _, _, _ ->
                            RenderSystem.scalef(2f, 2f, 1f)
                            RenderSystem.translatef(0f, 0f, 400f)
                            MinecraftClient.getInstance().itemRenderer.renderInGui(ItemStack(BlockCompendium.ACACIA_VAT), bounds.x/2 + 55, bounds.y/2 + 5)
                            RenderSystem.translatef(0f, 0f, -400f)
                            RenderSystem.scalef(0.5f, 0.5f, 1f)
                        })
                    }
                }
            }

        }

        return widgets
    }

}