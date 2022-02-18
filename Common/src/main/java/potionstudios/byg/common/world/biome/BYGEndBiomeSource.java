package potionstudios.byg.common.world.biome;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.synth.SimplexNoise;
import potionstudios.byg.BYG;
import potionstudios.byg.mixin.access.WeightedListAccess;
import potionstudios.byg.util.CodecUtil;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.level.biome.TheEndBiomeSource.getHeightValue;

public abstract class BYGEndBiomeSource extends BiomeSource {
    public static final ResourceLocation LOCATION = BYG.createLocation("end");


    private final SimplexNoise islandNoise;
    private final Registry<Biome> biomeRegistry;
    private final LayersBiomeData islandLayersBiomeData;
    private final LayersBiomeData voidLayersBiomeData;
    private final BiomeResolver islandBiomeResolver;
    private final BiomeResolver voidBiomeResolver;
    private final long seed;

    protected BYGEndBiomeSource(Registry<Biome> biomeRegistry, long seed, LayersBiomeData islandLayersBiomeData, LayersBiomeData voidLayersBiomeData) {
        super(Util.make(() -> {
            EndBiomesConfig config = EndBiomesConfig.getConfig(true);
            LayersBiomeData usedIslandLayer = config.useUpdatingConfig() ? config.islandLayers() : islandLayersBiomeData;
            LayersBiomeData usedVoidLayer = config.useUpdatingConfig() ? config.voidLayers() : voidLayersBiomeData;
            List<Biome> biomesFromBiomeData = createBiomesFromBiomeData(biomeRegistry, usedIslandLayer, usedVoidLayer);
            biomesFromBiomeData.add(biomeRegistry.get(Biomes.THE_END));
            return biomesFromBiomeData;
        }));
        this.biomeRegistry = biomeRegistry;
        this.seed = seed;


        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(seed));
        worldgenrandom.consumeCount(17292);
        this.islandNoise = new SimplexNoise(worldgenrandom);
        this.islandLayersBiomeData = islandLayersBiomeData;
        this.voidLayersBiomeData = voidLayersBiomeData;
        EndBiomesConfig config = EndBiomesConfig.getConfig(true);

        LayersBiomeData usedIslandLayer = config.useUpdatingConfig() ? config.islandLayers() : islandLayersBiomeData;
        LayersBiomeData usedVoidLayer = config.useUpdatingConfig() ? config.voidLayers() : voidLayersBiomeData;

        this.islandBiomeResolver = getIslandBiomeResolver(biomeRegistry, seed, usedIslandLayer);
        this.voidBiomeResolver = getVoidBiomeResolver(biomeRegistry, seed, usedVoidLayer);
    }

    public abstract BiomeResolver getIslandBiomeResolver(Registry<Biome> biomeRegistry, long seed, LayersBiomeData islandLayersBiomeData);

    public abstract BiomeResolver getVoidBiomeResolver(Registry<Biome> biomeRegistry, long seed, LayersBiomeData voidLayersBiomeData);

    @Override
    public Biome getNoiseBiome(int x, int y, int z, Climate.Sampler sampler) {
        int chunkX = x >> 2;
        int chunkZ = y >> 2;
        if ((long) chunkX * (long) chunkX + (long) chunkZ * (long) chunkZ <= 4096L) {
            return this.biomeRegistry.get(Biomes.THE_END);
        } else {
            float heightValue = getHeightValue(this.islandNoise, chunkX * 2 + 1, chunkZ * 2 + 1);
            if (heightValue >= -20.0F) {
                return this.islandBiomeResolver.getNoiseBiome(x, y, z, sampler);
            } else {
                return this.voidBiomeResolver.getNoiseBiome(x, y, z, sampler) ;
            }
        }
    }

    public Registry<Biome> getBiomeRegistry() {
        return biomeRegistry;
    }

    public LayersBiomeData getIslandLayersBiomeData() {
        return islandLayersBiomeData;
    }

    public LayersBiomeData getVoidLayersBiomeData() {
        return voidLayersBiomeData;
    }

    public long getSeed() {
        return seed;
    }

    @SuppressWarnings("unchecked")
    public static List<Biome> createBiomesFromBiomeData(Registry<Biome> biomeRegistry, LayersBiomeData... layersBiomeDatas) {
        List<Biome> biomes = new ArrayList<>();
        for (LayersBiomeData layersBiomeData : layersBiomeDatas) {
            ((WeightedListAccess<WeightedEntry.Wrapper<ResourceKey<Biome>>>) layersBiomeData.biomeWeights).getItems().stream().map(WeightedEntry.Wrapper::getData).map(biomeRegistry::get).forEach(biomes::add);
        }
        return biomes;
    }

    public record LayersBiomeData(SimpleWeightedRandomList<ResourceKey<Biome>> biomeWeights, int biomeSize) {
        public static final Codec<LayersBiomeData> CODEC = RecordCodecBuilder.create(builder -> {
            return builder.group(
                    SimpleWeightedRandomList.wrappedCodec(CodecUtil.BIOME_CODEC).fieldOf("biomeWeights").forGetter(layersBiomeData -> layersBiomeData.biomeWeights),
                    Codec.INT.fieldOf("biomeSize").forGetter(layersBiomeData -> layersBiomeData.biomeSize)
            ).apply(builder, LayersBiomeData::new);
        });

        public static final LayersBiomeData DEFAULT_END_ISLANDS = new LayersBiomeData(SimpleWeightedRandomList.<ResourceKey<Biome>>builder().add(Biomes.END_MIDLANDS, 2).add(Biomes.END_BARRENS, 2).add(Biomes.END_HIGHLANDS, 2).build(), 3);

        public static final LayersBiomeData DEFAULT_END_VOID = new LayersBiomeData(SimpleWeightedRandomList.<ResourceKey<Biome>>builder().add(Biomes.SMALL_END_ISLANDS, 2).build(), 2);
    }
}