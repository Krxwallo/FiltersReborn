package com.mrcrayfish.filters.client;

import com.mrcrayfish.filters.interfaces.IFiltersRegister;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class Client {
    public static void setup(IFiltersRegister filtersRegister) {
        filtersRegister.register(ItemGroup.BUILDING_BLOCKS, new ResourceLocation("building_blocks/natural"),              new ItemStack(Blocks.GRASS_BLOCK));
        filtersRegister.register(ItemGroup.BUILDING_BLOCKS, new ResourceLocation("building_blocks/stones"),               new ItemStack(Blocks.STONE));
        filtersRegister.register(ItemGroup.BUILDING_BLOCKS, new ResourceLocation("building_blocks/woods"),                new ItemStack(Blocks.OAK_LOG));
        filtersRegister.register(ItemGroup.BUILDING_BLOCKS, new ResourceLocation("building_blocks/minerals"),             new ItemStack(Blocks.EMERALD_BLOCK));
        filtersRegister.register(ItemGroup.BUILDING_BLOCKS, new ResourceLocation("stairs"),                               new ItemStack(Blocks.OAK_STAIRS));
        filtersRegister.register(ItemGroup.BUILDING_BLOCKS, new ResourceLocation("slabs"),                                new ItemStack(Blocks.OAK_SLAB));
        filtersRegister.register(ItemGroup.BUILDING_BLOCKS, new ResourceLocation("forge", "glass"),                 new ItemStack(Blocks.GLASS));
        filtersRegister.register(ItemGroup.BUILDING_BLOCKS, new ResourceLocation("building_blocks/colored"),              new ItemStack(Blocks.RED_WOOL));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/vegetation"),         new ItemStack(Blocks.GRASS));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/functional"),         new ItemStack(Blocks.CRAFTING_TABLE));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/fences_and_walls"),   new ItemStack(Blocks.OAK_FENCE));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/interior"),           new ItemStack(Blocks.RED_BED));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/glass"),              new ItemStack(Blocks.GLASS_PANE));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/colored"),            new ItemStack(Blocks.GREEN_GLAZED_TERRACOTTA));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/special"),            new ItemStack(Blocks.DRAGON_HEAD));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/infested"),           new ItemStack(Blocks.INFESTED_CRACKED_STONE_BRICKS));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/lights"),             new ItemStack(Blocks.TORCH));
        filtersRegister.register(ItemGroup.DECORATIONS,     new ResourceLocation("decoration_blocks/bees"),               new ItemStack(Blocks.BEE_NEST));
        filtersRegister.register(ItemGroup.REDSTONE,        new ResourceLocation("redstone/core"),                        new ItemStack(Items.REDSTONE));
        filtersRegister.register(ItemGroup.REDSTONE,        new ResourceLocation("redstone/components"),                  new ItemStack(Items.STICKY_PISTON));
        filtersRegister.register(ItemGroup.REDSTONE,        new ResourceLocation("redstone/inputs"),                      new ItemStack(Items.TRIPWIRE_HOOK));
        filtersRegister.register(ItemGroup.REDSTONE,        new ResourceLocation("redstone/doors"),                       new ItemStack(Items.OAK_DOOR));
        filtersRegister.register(ItemGroup.REDSTONE,        new ResourceLocation("redstone/trapdoors"),                   new ItemStack(Items.OAK_TRAPDOOR));
        filtersRegister.register(ItemGroup.REDSTONE,        new ResourceLocation("redstone/fence_gates"),                 new ItemStack(Items.OAK_FENCE_GATE));
        filtersRegister.register(ItemGroup.TRANSPORTATION,  new ResourceLocation("transportation/minecarts"),             new ItemStack(Items.MINECART));
        filtersRegister.register(ItemGroup.TRANSPORTATION,  new ResourceLocation("transportation/boats"),                 new ItemStack(Items.OAK_BOAT));
        filtersRegister.register(ItemGroup.TRANSPORTATION,  new ResourceLocation("transportation/rails"),                 new ItemStack(Items.RAIL));
        filtersRegister.register(ItemGroup.MISC,            new ResourceLocation("miscellaneous/materials"),              new ItemStack(Items.GOLD_INGOT));
        filtersRegister.register(ItemGroup.MISC,            new ResourceLocation("miscellaneous/eggs"),                   new ItemStack(Items.TURTLE_EGG));
        filtersRegister.register(ItemGroup.MISC,            new ResourceLocation("miscellaneous/plants_and_seeds"),       new ItemStack(Items.SUGAR_CANE));
        filtersRegister.register(ItemGroup.MISC,            new ResourceLocation("miscellaneous/dyes"),                   new ItemStack(Items.RED_DYE));
        filtersRegister.register(ItemGroup.MISC,            new ResourceLocation("miscellaneous/discs"),                  new ItemStack(Items.MUSIC_DISC_MALL));
        filtersRegister.register(ItemGroup.MISC,            new ResourceLocation("miscellaneous/buckets"),                new ItemStack(Items.BUCKET));
        filtersRegister.register(ItemGroup.MISC,            new ResourceLocation("miscellaneous/horse_armor"),            new ItemStack(Items.DIAMOND_HORSE_ARMOR));
        filtersRegister.register(ItemGroup.MISC,            new ResourceLocation("miscellaneous/banner_patterns"),        new ItemStack(Items.CREEPER_BANNER_PATTERN));
        filtersRegister.register(ItemGroup.FOOD,            new ResourceLocation("foodstuffs/raw"),                       new ItemStack(Items.BEEF));
        filtersRegister.register(ItemGroup.FOOD,            new ResourceLocation("foodstuffs/cooked"),                    new ItemStack(Items.COOKED_PORKCHOP));
        filtersRegister.register(ItemGroup.FOOD,            new ResourceLocation("foodstuffs/special"),                   new ItemStack(Items.GOLDEN_APPLE));
        filtersRegister.register(ItemGroup.COMBAT,          new ResourceLocation("combat/armor"),                         new ItemStack(Items.IRON_CHESTPLATE));
        filtersRegister.register(ItemGroup.COMBAT,          new ResourceLocation("combat/weapons"),                       new ItemStack(Items.IRON_SWORD));
        filtersRegister.register(ItemGroup.COMBAT,          new ResourceLocation("combat/arrows"),                        new ItemStack(Items.ARROW));
        filtersRegister.register(ItemGroup.COMBAT,          new ResourceLocation("combat/enchanting_books"),              new ItemStack(Items.ENCHANTED_BOOK));
        filtersRegister.register(ItemGroup.TOOLS,           new ResourceLocation("tools/tools"),                          new ItemStack(Items.IRON_SHOVEL));
        filtersRegister.register(ItemGroup.TOOLS,           new ResourceLocation("tools/equipment"),                      new ItemStack(Items.COMPASS));
        filtersRegister.register(ItemGroup.TOOLS,           new ResourceLocation("tools/enchanting_books"),               new ItemStack(Items.ENCHANTED_BOOK));
        filtersRegister.register(ItemGroup.BREWING,         new ResourceLocation("brewing/potions"),                      new ItemStack(Items.DRAGON_BREATH));
        filtersRegister.register(ItemGroup.BREWING,         new ResourceLocation("brewing/ingredients"),                  new ItemStack(Items.BLAZE_POWDER));
        filtersRegister.register(ItemGroup.BREWING,         new ResourceLocation("brewing/equipment"),                    new ItemStack(Items.BREWING_STAND));
    }
}
