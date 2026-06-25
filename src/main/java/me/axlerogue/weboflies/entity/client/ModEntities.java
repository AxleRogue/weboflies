package me.axlerogue.weboflies.entity.client;
import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, WebOfLies.MODID);

    public static final RegistryObject<EntityType<SpiderEgg>> SPIDER_EGG = ENTITIES.register("spider_egg",
            () -> EntityType.Builder.of(SpiderEgg::new, MobCategory.MONSTER)
                    .sized(0.5f, 0.5f) // Adjust size as needed
                    .build("spider_egg"));

    public static final RegistryObject<EntityType<BlackWidowEntity>> BLACK_WIDOW = ENTITIES.register("black_widow",
            () -> EntityType.Builder.of(BlackWidowEntity::new, MobCategory.CREATURE)
                    .sized(1.4f, 0.9f)
                    .build("black_widow"));

    public static final RegistryObject<EntityType<BabyBlackWidowEntity>> BABY_BLACK_WIDOW = ENTITIES.register("baby_black_widow",
            () -> EntityType.Builder.of(BabyBlackWidowEntity::new, MobCategory.CREATURE)
                    .sized(0.7f, 0.45f)
                    .build("baby_black_widow"));

    public static final RegistryObject<EntityType<BlackWidowBroodMotherEntity>> BLACK_WIDOW_BROOD_MOTHER = ENTITIES.register("black_widow_brood_mother",
            () -> EntityType.Builder.of(BlackWidowBroodMotherEntity::new, MobCategory.CREATURE)
                    .sized(4.2f, 2.7f)
                    .build("black_widow_brood_mother"));

    public static final RegistryObject<EntityType<BrownWidowEntity>> BROWN_WIDOW = ENTITIES.register("brown_widow",
            () -> EntityType.Builder.of(BrownWidowEntity::new, MobCategory.CREATURE)
                    .sized(1.4f, 0.9f)
                    .build("brown_widow"));

    public static final RegistryObject<EntityType<BabyBrownWidowEntity>> BABY_BROWN_WIDOW = ENTITIES.register("baby_brown_widow",
            () -> EntityType.Builder.of(BabyBrownWidowEntity::new, MobCategory.CREATURE)
                    .sized(0.7f, 0.45f)
                    .build("baby_brown_widow"));

    public static final RegistryObject<EntityType<HauntedCobwebProjectile>> HAUNTED_COBWEB_PROJECTILE = ENTITIES.register("haunted_cobweb_projectile",
            () -> EntityType.Builder.<HauntedCobwebProjectile>of(HauntedCobwebProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build("haunted_cobweb_projectile"));


    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
