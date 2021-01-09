package io.github.cafeteriaguild.exdrico.common.blocks

import io.github.cafeteriaguild.exdrico.common.blockentities.InfestedLeavesBlockEntity
import net.minecraft.block.*
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.registry.Registry
import net.minecraft.world.BlockView
import java.util.*

class InfestedLeavesBlock(val parent: LeavesBlock, settings: Settings): LeavesBlock(settings), BlockEntityProvider {

    companion object {
        val infestedLeavesMap = linkedMapOf<LeavesBlock, InfestedLeavesBlock>()

        val sprites: Collection<SpriteIdentifier>
            get() {
                val spriteList = mutableListOf<SpriteIdentifier>()
                infestedLeavesMap.forEach { (baseBlock, _) ->
                    val blockIdentifier = Registry.BLOCK.getId(baseBlock)
                    spriteList.add(SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Identifier("${blockIdentifier.namespace}:block/${blockIdentifier.path}")))
                }
                return spriteList
            }
    }

    val spriteId = infestedLeavesMap.size

    init {
        infestedLeavesMap[parent] = this
    }

    override fun randomTick(state: BlockState?, world: ServerWorld, pos: BlockPos, random: Random) {
        for (i in 0..3) {
            val blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1)
            val blockState = world.getBlockState(blockPos)
            val block = blockState.block
            if (block is LeavesBlock) {
                infestedLeavesMap[block]?.defaultState?.with(DISTANCE, blockState[DISTANCE])?.with(PERSISTENT, blockState[PERSISTENT])?.let { world.setBlockState(blockPos, it) }
            }
        }
    }

    override fun createBlockEntity(world: BlockView?) = InfestedLeavesBlockEntity(this)

}