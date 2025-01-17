package corgiaoc.byg.mixin.access;

import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkBiomeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkBiomeContainer.class)
public interface BiomeContainerAccess {
    @Accessor Biome[] getBiomes();
}
