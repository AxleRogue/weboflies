package me.axlerogue.weboflies.datagen;
import me.axlerogue.weboflies.WebOfLies;
import me.axlerogue.weboflies.block.ModBlocks;
import me.axlerogue.weboflies.entity.client.ModEntities;
import me.axlerogue.weboflies.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.function.Consumer;

public class ModAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {
    @Override
    public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
        Advancement root = Advancement.Builder.advancement()
                .display(new DisplayInfo(new ItemStack(Items.COBWEB),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.root.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.root.description"),
                        new ResourceLocation("minecraft", "textures/gui/advancements/backgrounds/stone.png"),
                        FrameType.TASK, true, true, false))
                .addCriterion("has_spider_eye", InventoryChangeTrigger.TriggerInstance.hasItems(Items.SPIDER_EYE))
                .addCriterion("entered_dark_forest", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(
                        ResourceKey.create(Registries.DIMENSION,
                                new ResourceLocation(WebOfLies.MODID, "dark_forest"))))
                .requirements(RequirementsStrategy.OR)
                .save(saver, new ResourceLocation(WebOfLies.MODID, "root"), existingFileHelper);

        Advancement killBlackWidow = Advancement.Builder.advancement()
                .parent(root)
                .display(new DisplayInfo(new ItemStack(ModItems.THREAD_OF_FATE.get()),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.kill_black_widow.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.kill_black_widow.description"),
                        null, FrameType.TASK, true, true, false))
                .addCriterion("killed_black_widow", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(ModEntities.BLACK_WIDOW.get())))
                .save(saver, new ResourceLocation(WebOfLies.MODID, "kill_black_widow"), existingFileHelper);

        Advancement killSpiderEgg = Advancement.Builder.advancement()
                .parent(root)
                .display(new DisplayInfo(new ItemStack(Items.SPIDER_EYE),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.kill_spider_egg.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.kill_spider_egg.description"),
                        null, FrameType.TASK, true, true, false))
                .addCriterion("killed_spider_egg", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(ModEntities.SPIDER_EGG.get())))
                .save(saver, new ResourceLocation(WebOfLies.MODID, "kill_spider_egg"), existingFileHelper);

        Advancement obtainThreadOfFate = Advancement.Builder.advancement()
                .parent(killBlackWidow)
                .display(new DisplayInfo(new ItemStack(ModItems.THREAD_OF_FATE.get()),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.obtain_thread_of_fate.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.obtain_thread_of_fate.description"),
                        null, FrameType.TASK, true, true, false))
                .addCriterion("has_thread_of_fate", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.THREAD_OF_FATE.get()))
                .save(saver, new ResourceLocation(WebOfLies.MODID, "obtain_thread_of_fate"), existingFileHelper);

        Advancement obtainHomewardCobweb = Advancement.Builder.advancement()
                .parent(obtainThreadOfFate)
                .display(new DisplayInfo(new ItemStack(ModBlocks.HOMEWARD_COBWEB.get()),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.obtain_homeward_cobweb.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.obtain_homeward_cobweb.description"),
                        null, FrameType.TASK, true, true, false))
                .addCriterion("has_homeward_cobweb", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.HOMEWARD_COBWEB.get()))
                .save(saver, new ResourceLocation(WebOfLies.MODID, "obtain_homeward_cobweb"), existingFileHelper);

        Advancement witnessBabyBlackWidow = Advancement.Builder.advancement()
                .parent(killSpiderEgg)
                .display(new DisplayInfo(new ItemStack(Items.SPIDER_EYE),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.witness_baby_black_widow.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.witness_baby_black_widow.description"),
                        null, FrameType.TASK, true, true, false))
                .addCriterion("killed_baby", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(ModEntities.BABY_BLACK_WIDOW.get())))
                .save(saver, new ResourceLocation(WebOfLies.MODID, "witness_baby_black_widow"), existingFileHelper);

        Advancement killBroodMother = Advancement.Builder.advancement()
                .parent(killBlackWidow)
                .display(new DisplayInfo(new ItemStack(Items.FERMENTED_SPIDER_EYE),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.kill_black_widow_brood_mother.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.kill_black_widow_brood_mother.description"),
                        null, FrameType.CHALLENGE, true, true, false))
                .addCriterion("killed_brood_mother", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().of(ModEntities.BLACK_WIDOW_BROOD_MOTHER.get()),
                        DamageSourcePredicate.Builder.damageType().direct(EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(net.minecraft.tags.EntityTypeTags.ARROWS))).build())) // Placeholder for fire as tag check failed
                .save(saver, new ResourceLocation(WebOfLies.MODID, "kill_black_widow_brood_mother"), existingFileHelper);

        Advancement masterSlayer = Advancement.Builder.advancement()
                .parent(killBroodMother)
                .display(new DisplayInfo(new ItemStack(ModItems.THREAD_OF_FATE.get()),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.blackwidow_master_slayer.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.blackwidow_master_slayer.description"),
                        null, FrameType.CHALLENGE, true, true, false))
                .addCriterion("killed_black_widow", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.BLACK_WIDOW.get())))
                .addCriterion("killed_egg", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.SPIDER_EGG.get())))
                .addCriterion("killed_baby", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.BABY_BLACK_WIDOW.get())))
                .addCriterion("killed_brood_mother", KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(ModEntities.BLACK_WIDOW_BROOD_MOTHER.get())))
                .save(saver, new ResourceLocation(WebOfLies.MODID, "blackwidow_master_slayer"), existingFileHelper);

        Advancement eatGooseBerry = Advancement.Builder.advancement()
                .parent(root)
                .display(new DisplayInfo(new ItemStack(ModItems.GOOSE_BERRY.get()),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.eat_goose_berry.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.eat_goose_berry.description"),
                        null, FrameType.TASK, true, true, false))
                .addCriterion("ate_goose_berry", ConsumeItemTrigger.TriggerInstance.usedItem(ModItems.GOOSE_BERRY.get()))
                .save(saver, new ResourceLocation(WebOfLies.MODID, "eat_goose_berry"), existingFileHelper);

        Advancement breedBlackWidow = Advancement.Builder.advancement()
                .parent(eatGooseBerry)
                .display(new DisplayInfo(new ItemStack(Items.FERMENTED_SPIDER_EYE),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.breed_black_widow.title"),
                        net.minecraft.network.chat.Component.translatable("advancement.weboflies.breed_black_widow.description"),
                        null, FrameType.TASK, true, true, false))
                .addCriterion("bred_black_widow", BredAnimalsTrigger.TriggerInstance.bredAnimals(
                        EntityPredicate.Builder.entity().of(ModEntities.BLACK_WIDOW.get())))
                .save(saver, new ResourceLocation(WebOfLies.MODID, "breed_black_widow"), existingFileHelper);
    }
}
