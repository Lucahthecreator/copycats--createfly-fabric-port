package com.copycatsplus.copycats;

import com.copycatsplus.copycats.utility.Platform;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class CCTags {
    public static <T> TagKey<T> optionalTag(Registry<T> registry, Identifier id) {
        return TagKey.create(registry.key(), id);
    }

    public static <T> TagKey<T> commonTag(Registry<T> registry, String path) {
        return optionalTag(registry, Identifier.fromNamespaceAndPath(Platform.getCurrent().equals(Platform.FORGE) ? "forge" : "c", path));
    }

    public static TagKey<Block> commonBlockTag(String path) {
        return commonTag(BuiltInRegistries.BLOCK, path);
    }

    public static TagKey<Item> commonItemTag(String path) {
        return commonTag(BuiltInRegistries.ITEM, path);
    }

    public static TagKey<Fluid> commonFluidTag(String path) {
        return commonTag(BuiltInRegistries.FLUID, path);
    }

    public static void init() {
        CCTags.Items.init();
    }

    public static enum Items {
        COPYCAT_BEAM,
        COPYCAT_BLOCK,
        COPYCAT_BOARD,
        COPYCAT_BOX,
        COPYCAT_CATWALK,
        COPYCAT_FENCE,
        COPYCAT_FENCE_GATE,
        COPYCAT_SLAB,
        COPYCAT_STAIRS,
        COPYCAT_VERTICAL_STEP,
        COPYCAT_WALL;

        public final TagKey<Item> tag;
        public final boolean alwaysDatagen;

        private Items() {
            this(CCTags.NameSpace.MOD);
        }

        private Items(CCTags.NameSpace namespace) {
            this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private Items(CCTags.NameSpace namespace, String path) {
            this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
        }

        private Items(CCTags.NameSpace namespace, boolean optional, boolean alwaysDatagen) {
            this(namespace, null, optional, alwaysDatagen);
        }

        private Items(CCTags.NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
            Identifier id = Identifier.fromNamespaceAndPath(namespace.id, path == null ? CCLang.asId(this.name()) : path);
            if (optional) {
                this.tag = CCTags.optionalTag(BuiltInRegistries.ITEM, id);
            } else {
                this.tag = TagKey.create(BuiltInRegistries.ITEM.key(), id);
            }

            this.alwaysDatagen = alwaysDatagen;
        }

        public boolean matches(Item item) {
            return item.builtInRegistryHolder().is(this.tag);
        }

        public boolean matches(ItemStack stack) {
            return stack.is(this.tag);
        }

        private static void init() {
        }
    }

    public static enum NameSpace {
        MOD("copycats", false, true);

        public final String id;
        public final boolean optionalDefault;
        public final boolean alwaysDatagenDefault;

        private NameSpace(String id) {
            this(id, true, false);
        }

        private NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
            this.id = id;
            this.optionalDefault = optionalDefault;
            this.alwaysDatagenDefault = alwaysDatagenDefault;
        }
    }
}
