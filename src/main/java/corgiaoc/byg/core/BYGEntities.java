package corgiaoc.byg.core;

import corgiaoc.byg.BYG;
import corgiaoc.byg.common.entity.boat.BYGBoatEntity;
import corgiaoc.byg.common.entity.manowar.ManOWar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("deprecation")
public class BYGEntities {
    public static Set<EntityType<?>> entities = new HashSet<>();

    public static final EntityType<BYGBoatEntity> BOAT = createEntity("boat", EntityType.Builder.<BYGBoatEntity>of(BYGBoatEntity::new, MobCategory.MISC).sized(1.375F, 0.5625F).build(BYG.MOD_ID + ":boat"));

    //mobs
    public static final EntityType<ManOWar> MANOWAR = createEntity("man_o_war", EntityType.Builder.of(ManOWar::new, MobCategory.AMBIENT).sized(0.6F, 1.75F).build(BYG.MOD_ID + ":man_o_war"));

    public static <E extends Entity, ET extends EntityType<E>> ET createEntity(String id, ET entityType) {
        Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(BYG.MOD_ID, id), entityType);
//        entityType.setRegistryName(new ResourceLocation(BYG.MOD_ID, id)); //Forge
        entities.add(entityType);
        return entityType;
    }

    public static void init() {
    }
}
