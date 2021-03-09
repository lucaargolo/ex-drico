package io.github.cafeteriaguild.exdrico.compat

import io.github.cafeteriaguild.exdrico.common.recipes.VatRecipe
import me.shedaniel.rei.api.EntryStack
import me.shedaniel.rei.api.TransferRecipeDisplay
import me.shedaniel.rei.server.ContainerInfo
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.util.Identifier
import java.util.*

class VatRecipeDisplay(val recipe: VatRecipe): TransferRecipeDisplay {

    private val input: MutableList<MutableList<EntryStack>> = mutableListOf()
    private val output: MutableList<MutableList<EntryStack>> = mutableListOf()

    init {
        val inputItems = mutableListOf<EntryStack>()
        recipe.input.forEach { (ingredient, _) ->
            inputItems.addAll(EntryStack.ofIngredient(ingredient))
        }
        input.add(inputItems)

        val inputFluids = mutableListOf<EntryStack>()
        recipe.fluidInput?.let {
            inputFluids.add(EntryStack.create(it.rawFluid, it.amount().asInt(1)))
        }
        input.add(inputFluids)

        val inputBlock = mutableListOf<EntryStack>()
        recipe.blockBelow?.let {
            inputBlock.add(EntryStack.create(it))
        }
        input.add(inputBlock)

        val outputBlock = mutableListOf<EntryStack>()
        outputBlock.add(EntryStack.create(recipe.out))
        output.add(outputBlock)

        val outputFluid = mutableListOf<EntryStack>()
        recipe.fluidOut?.let {
            outputFluid.add(EntryStack.create(it.rawFluid, it.amount().asInt(1)))
        }
        output.add(outputFluid)
    }

    override fun getRecipeCategory(): Identifier = VatRecipe.ID

    override fun getRecipeLocation(): Optional<Identifier> = Optional.ofNullable(recipe).map { it.id }

    override fun getRequiredEntries(): MutableList<MutableList<EntryStack>> = input

    override fun getInputEntries(): MutableList<MutableList<EntryStack>> = input

    override fun getOrganisedInputEntries(p0: ContainerInfo<ScreenHandler>?, p1: ScreenHandler?): MutableList<MutableList<EntryStack>> = input

    override fun getResultingEntries(): MutableList<MutableList<EntryStack>> = output

    override fun getWidth(): Int = 1

    override fun getHeight(): Int = 1

}