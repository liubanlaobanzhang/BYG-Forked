package corgiaoc.byg.common.world.surfacebuilder;

import com.mojang.serialization.Codec;
import corgiaoc.byg.core.world.BYGSurfaceBuilders;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

import java.util.Random;

public class IvisFieldsSB extends SurfaceBuilder<SurfaceBuilderBaseConfiguration> {
    public IvisFieldsSB(Codec<SurfaceBuilderBaseConfiguration> config) {
        super(config);
    }

    public void apply(Random random, ChunkAccess chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, int minSurfaceLevel, long seed, SurfaceBuilderBaseConfiguration config) {
        if (noise > 0.3)
            SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, minSurfaceLevel, seed, BYGSurfaceBuilders.Configs.IVIS_CF);
        else if (noise < -0.3)
            SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, minSurfaceLevel, seed, BYGSurfaceBuilders.Configs.IVIS_CF);
        else
            SurfaceBuilder.DEFAULT.apply(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, minSurfaceLevel, seed, new SurfaceBuilderBaseConfiguration(Blocks.OBSIDIAN.defaultBlockState(), Blocks.OBSIDIAN.defaultBlockState(), Blocks.OBSIDIAN.defaultBlockState()));

    }
}