package io.github.cafeteriaguild.exdrico.common.blockentities

import io.github.cafeteriaguild.exdrico.common.blocks.SieveBlock
import io.github.cafeteriaguild.exdrico.utils.SyncedBlockEntity

class SieveBlockEntity(block: SieveBlock): SyncedBlockEntity(block) {

    var progress = 0

}