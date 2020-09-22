package io.github.cafeteriaguild.exdrico.common.recipes

import alexiil.mc.lib.attributes.fluid.FixedFluidInv
import alexiil.mc.lib.attributes.fluid.amount.FluidAmount
import alexiil.mc.lib.attributes.fluid.volume.FluidKeys
import alexiil.mc.lib.attributes.fluid.volume.FluidVolume
import alexiil.mc.lib.attributes.item.FixedItemInv
import com.google.gson.JsonObject
import io.github.cafeteriaguild.exdrico.utils.ModIdentifier
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class VatRecipe(
    val identifier: Identifier,
    val input: Map<Ingredient, Int>,
    val fluidInput: FluidVolume?,
    val out: ItemStack,
    val ticks: Int
) : Recipe<Inventory> {
    override fun getId(): Identifier = identifier

    @Deprecated("Unsupported method for Vat's recipes", replaceWith = ReplaceWith("matches(Inventory, FluidVolume?)"), DeprecationLevel.ERROR)
    override fun matches(inv: Inventory?, world: World?): Boolean = throw IllegalArgumentException("Unsupported method for Vat's recipes")

    override fun craft(inv: Inventory?): ItemStack = out.copy()

    fun matches(inv: FixedItemInv, fluidInv: FixedFluidInv?): Boolean {
        val fluidVolume = fluidInv?.getInvFluid(0)
        if (fluidInput != null && fluidVolume != null && (fluidVolume.fluidKey != fluidInput!!.fluidKey || fluidVolume.amount() < fluidInput.amount())) {
             return false
        }
        val stack = inv.getInvStack(0)
        return input.any { (first, _) -> first.test(stack) }
    }

    override fun fits(width: Int, height: Int): Boolean = true

    override fun getOutput(): ItemStack = out

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = TYPE

    companion object {

        val ID = ModIdentifier("vat")
        val TYPE = object : RecipeType<VatRecipe> {}
        val SERIALIZER = VatRecipeSerializer()

        open class VatRecipeSerializer : RecipeSerializer<VatRecipe> {
            override fun read(id: Identifier, json: JsonObject): VatRecipe {
                val ingredients = json["ingredients"].asJsonArray.associate { ingredientFromJson(it.asJsonObject) }
                val ticks = json.get("cost").asInt
                val output = itemStackFromJson(json["output"].asJsonObject)
                val fluidInputJson = json.getAsJsonObject("fluidInput")
                val fluidInput = if (fluidInputJson == null) null else getFluidFromJson(fluidInputJson)
                return VatRecipe(id, ingredients, fluidInput, output, ticks)
            }

            override fun read(id: Identifier, buf: PacketByteBuf): VatRecipe  {
                val size = buf.readInt()
                val pair = (0 until size).associate { Pair(Ingredient.fromPacket(buf), buf.readInt()) }
                val ticks = buf.readInt()
                val output = buf.readItemStack()
                val hasInputFluid = buf.readBoolean()
                val fluidInput = if (hasInputFluid) {
                    val fluidId = buf.readIdentifier()
                    val fluidAmount = FluidAmount.fromMcBuffer(buf)
                    val fluidKey = FluidKeys.get(Registry.FLUID.get(fluidId))
                    fluidKey.withAmount(fluidAmount)
                } else null
                return VatRecipe(id, pair, fluidInput, output, ticks)
            }

            override fun write(buf: PacketByteBuf, recipe: VatRecipe) {
                buf.writeInt(recipe.input.size)
                recipe.input.forEach { (ing, int) ->
                    ing.write(buf)
                    buf.writeInt(int)
                }
                buf.writeInt(recipe.ticks)
                buf.writeItemStack(recipe.output)
                buf.writeBoolean(recipe.fluidInput != null)
                if (recipe.fluidInput != null) {
                    buf.writeIdentifier(recipe.fluidInput.fluidKey.entry.id)
                    recipe.fluidInput.amount().toMcBuffer(buf)
                }
            }
        }

        fun ingredientFromJson(json: JsonObject): Pair<Ingredient, Int> {
            val ing = Ingredient.fromJson(json)
            val count = JsonHelper.getInt(json, "add", 1)
            return Pair(ing, count)
        }

        fun itemStackFromJson(json: JsonObject): ItemStack {
            val itemPath = json.get("item").asString
            val item = Registry.ITEM.get(Identifier(itemPath))
            val count = JsonHelper.getInt(json, "count", 1)
            return ItemStack(item, count)
        }
        fun getFluidFromJson(json: JsonObject): FluidVolume {
            val fluidId = json.get("fluid").asString
            val fluidKey = FluidKeys.get(Registry.FLUID.get(Identifier(fluidId)))
            val fluidAmount = FluidAmount.of(JsonHelper.getLong(json, "numerator", 1), JsonHelper.getLong(json, "denominator", 1))
            return fluidKey.withAmount(fluidAmount)
        }
    }
}