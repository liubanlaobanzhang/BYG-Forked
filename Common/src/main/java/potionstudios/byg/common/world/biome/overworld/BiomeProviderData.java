package potionstudios.byg.common.world.biome.overworld;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import potionstudios.byg.common.world.biome.BYGBiomes;
import potionstudios.byg.util.codec.CodecUtil;
import potionstudios.byg.util.codec.FromFileCodec;
import potionstudios.byg.util.codec.Wrapped;

import java.util.*;

import static potionstudios.byg.common.world.biome.overworld.BYGOverworldBiomeBuilders.*;

public record BiomeProviderData(int overworldWeight, Wrapped<List<List<ResourceKey<Biome>>>> oceans,
                                Wrapped<List<List<ResourceKey<Biome>>>> middleBiomes,
                                Wrapped<List<List<ResourceKey<Biome>>>> middleBiomesVariant,
                                Wrapped<List<List<ResourceKey<Biome>>>> plateauBiomes,
                                Wrapped<List<List<ResourceKey<Biome>>>> plateauBiomesVariant,
                                Wrapped<List<List<ResourceKey<Biome>>>> extremeHills,
                                Wrapped<List<List<ResourceKey<Biome>>>> beachBiomes,
                                Wrapped<List<List<ResourceKey<Biome>>>> peakBiomes,
                                Wrapped<List<List<ResourceKey<Biome>>>> peakBiomesVariant,
                                Map<ResourceKey<Biome>, ResourceKey<Biome>> swapper) {
    public static final Codec<BiomeProviderData> CODEC = RecordCodecBuilder.create(builder -> {
        return builder.group(Codec.INT.fieldOf("weight").forGetter(biomeProviderData -> biomeProviderData.overworldWeight),
            BIOME_LAYOUT_CODEC.fieldOf("oceans").forGetter(biomeProviderData -> biomeProviderData.oceans),
            BIOME_LAYOUT_CODEC.fieldOf("middle_biomes").forGetter(biomeProviderData -> biomeProviderData.middleBiomes),
            BIOME_LAYOUT_CODEC.fieldOf("middle_biomes_variant").forGetter(biomeProviderData -> biomeProviderData.middleBiomesVariant),
            BIOME_LAYOUT_CODEC.fieldOf("plateau_biomes").forGetter(biomeProviderData -> biomeProviderData.plateauBiomes),
            BIOME_LAYOUT_CODEC.fieldOf("plateau_biomes_variant").forGetter(biomeProviderData -> biomeProviderData.plateauBiomesVariant),
            BIOME_LAYOUT_CODEC.fieldOf("shattered_biomes").forGetter(biomeProviderData -> biomeProviderData.extremeHills),
            BIOME_LAYOUT_CODEC.fieldOf("beach_biomes").orElse(BEACH_BIOMES_EMPTY).forGetter(biomeProviderData -> biomeProviderData.beachBiomes),
            BIOME_LAYOUT_CODEC.fieldOf("peak_biomes").orElse(PEAK_BIOMES_EMPTY).forGetter(biomeProviderData -> biomeProviderData.peakBiomes),
            BIOME_LAYOUT_CODEC.fieldOf("peak_biomes_variant").orElse(PEAK_BIOMES_VARIANT_EMPTY).forGetter(biomeProviderData -> biomeProviderData.peakBiomesVariant),
            Codec.unboundedMap(ResourceLocation.CODEC.comapFlatMap(resourceLocation -> {
                if (!resourceLocation.getNamespace().equals("minecraft")) {
                    throw new IllegalArgumentException("Only biomes from MC can be used as the swapper's key!!! You put: \"" + resourceLocation.toString() + "\"");
                }
                return DataResult.success(ResourceKey.create(Registry.BIOME_REGISTRY, resourceLocation));
            }, ResourceKey::location), CodecUtil.BIOME_CODEC).fieldOf("swapper").forGetter(biomeProviderData -> biomeProviderData.swapper)
        ).apply(builder, BiomeProviderData::new);
    });

    public static final FromFileCodec<BiomeProviderData> BIOME_PROVIDER_DATA_FROM_FILE_CODEC = FromFileCodec.create(CODEC, "regions");

    public static final Map<String, Pair<Map<String, String>, Wrapped<BiomeProviderData>>> BIOME_REGIONS = new HashMap<>();


    private static final int OVERWORLD_WEIGHT = 6;


    public static final Wrapped<BiomeProviderData> REGION_1 = create("region_1",
        new BiomeProviderData(OVERWORLD_WEIGHT,
            OCEANS, MIDDLE_BIOMES, MIDDLE_BIOMES_VARIANT,
            PLATEAU_BIOMES, PLATEAU_BIOMES_VARIANT, EXTREME_HILLS,
            BEACH_BIOMES_EMPTY, PEAK_BIOMES_EMPTY, PEAK_BIOMES_VARIANT_EMPTY,
            Util.make(new IdentityHashMap<>(), map -> map.put(Biomes.SWAMP, BYGBiomes.BAYOU)))
    );
    public static final Wrapped<BiomeProviderData> REGION_2 = create("region_2",
        new BiomeProviderData(OVERWORLD_WEIGHT,
            OCEANS_2, MIDDLE_BIOMES_2, MIDDLE_BIOMES_VARIANT_2,
            PLATEAU_BIOMES_2, PLATEAU_BIOMES_VARIANT_2, EXTREME_HILLS_EMPTY,
            BEACH_BIOMES_EMPTY, PEAK_BIOMES_EMPTY, PEAK_BIOMES_VARIANT_EMPTY,
            Util.make(new IdentityHashMap<>(), map -> map.put(Biomes.SWAMP, BYGBiomes.CYPRESS_SWAMPLANDS)))
    );
    public static final Wrapped<BiomeProviderData> REGION_3 = create("region_3",
        new BiomeProviderData(OVERWORLD_WEIGHT,
            OCEANS_EMPTY, MIDDLE_BIOMES_3, MIDDLE_BIOMES_VARIANT_3,
            PLATEAU_BIOMES_3, PLATEAU_BIOMES_VARIANT_EMPTY, EXTREME_HILLS_EMPTY,
            BEACH_BIOMES_EMPTY, PEAK_BIOMES_EMPTY, PEAK_BIOMES_VARIANT_EMPTY,
            new IdentityHashMap<>())
    );
    public static final Wrapped<BiomeProviderData> REGION_4 = create("region_4",
        new BiomeProviderData(OVERWORLD_WEIGHT,
            OCEANS_EMPTY, MIDDLE_BIOMES_EMPTY, MIDDLE_BIOMES_VARIANT_EMPTY,
            PLATEAU_BIOMES_4, PLATEAU_BIOMES_VARIANT_EMPTY, EXTREME_HILLS_EMPTY,
            BEACH_BIOMES_EMPTY, PEAK_BIOMES_EMPTY, PEAK_BIOMES_VARIANT_EMPTY,
            new IdentityHashMap<>())
    );
    public static final Wrapped<BiomeProviderData> REGION_5 = create("region_5",
        new BiomeProviderData(OVERWORLD_WEIGHT,
            OCEANS_EMPTY, MIDDLE_BIOMES_EMPTY, MIDDLE_BIOMES_VARIANT_EMPTY,
            PLATEAU_BIOMES_5, PLATEAU_BIOMES_VARIANT_EMPTY, EXTREME_HILLS_EMPTY,
            BEACH_BIOMES_EMPTY, PEAK_BIOMES_EMPTY, PEAK_BIOMES_VARIANT_EMPTY,
            new IdentityHashMap<>())
    );
    public static final Wrapped<BiomeProviderData> REGION_6 = create("region_6",
        new BiomeProviderData(OVERWORLD_WEIGHT,
            OCEANS_EMPTY, MIDDLE_BIOMES_EMPTY, MIDDLE_BIOMES_VARIANT_EMPTY,
            PLATEAU_BIOMES_6, PLATEAU_BIOMES_VARIANT_EMPTY, EXTREME_HILLS_EMPTY,
            BEACH_BIOMES_EMPTY, PEAK_BIOMES_EMPTY, PEAK_BIOMES_VARIANT_EMPTY,
            new IdentityHashMap<>())
    );
    public static final Wrapped<BiomeProviderData> REGION_7 = create("region_7",
        new BiomeProviderData(OVERWORLD_WEIGHT,
            OCEANS_EMPTY, MIDDLE_BIOMES_EMPTY, MIDDLE_BIOMES_VARIANT_EMPTY,
            PLATEAU_BIOMES_7, PLATEAU_BIOMES_VARIANT_EMPTY, EXTREME_HILLS_EMPTY,
            BEACH_BIOMES_EMPTY, PEAK_BIOMES_EMPTY, PEAK_BIOMES_VARIANT_EMPTY,
            new IdentityHashMap<>())
    );
    public static final List<Wrapped<BiomeProviderData>> OVERWORLD_DEFAULTS =
        ImmutableList.of(
            REGION_1, REGION_2, REGION_3, REGION_4,
            REGION_5, REGION_6, REGION_7
        );

    private static Wrapped<BiomeProviderData> create(String id, BiomeProviderData biomeProviderData) {
        return create(id, biomeProviderData, new HashMap<>());
    }

    private static Wrapped<BiomeProviderData> create(String id, BiomeProviderData biomeProviderData, Map<String, String> comments) {
        Wrapped<BiomeProviderData> result = new Wrapped<>(Optional.of(id), biomeProviderData);
        BIOME_REGIONS.put(id, Pair.of(comments, result));
        return result;
    }


}