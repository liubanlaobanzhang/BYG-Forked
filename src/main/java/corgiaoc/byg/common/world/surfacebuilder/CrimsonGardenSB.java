package corgiaoc.byg.common.world.surfacebuilder;

import com.mojang.serialization.Codec;
import corgiaoc.byg.core.BYGBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

import java.util.Random;

public class CrimsonGardenSB extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    public CrimsonGardenSB(Codec<SurfaceBuilderBaseConfiguration> p_i51312_1_) {
        super(p_i51312_1_);
    }

    public void apply(Random random, ChunkAccess chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int minSurfaceLevel, long seed, SurfaceBuilderBaseConfiguration config) {
        BlockPos.MutableBlockPos block = new BlockPos.MutableBlockPos();
        int xPos = x & 15;
        int zPos = z & 15;
        for (int yPos = 256; yPos >= seaLevel; --yPos) {
            block.set(xPos, yPos, zPos);
            BlockState currentBlockToReplace = chunkIn.getBlockState(block);
            BlockState checkAirForSB = chunkIn.getBlockState(block.above());
            if (currentBlockToReplace == Blocks.NETHERRACK.defaultBlockState() && !(checkAirForSB.getBlock() == Blocks.AIR))
                chunkIn.setBlockState(block, Blocks.BLACKSTONE.defaultBlockState(), false);
            if (currentBlockToReplace.getBlock() == Blocks.NETHERRACK && checkAirForSB.getBlock() == Blocks.AIR)
                chunkIn.setBlockState(block, BYGBlocks.OVERGROWN_CRIMSON_BLACKSTONE.defaultBlockState(), false);
        }
    }
}