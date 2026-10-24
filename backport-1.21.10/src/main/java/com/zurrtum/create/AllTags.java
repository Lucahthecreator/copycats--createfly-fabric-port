/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlockTags
 *  com.zurrtum.create.AllItemTags
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.block.Block
 */
package com.zurrtum.create;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AllTags {

    public static class ItemTag {
        public final TagKey<Item> tag;

        private ItemTag(TagKey<Item> tag) {
            this.tag = tag;
        }
    }

    public static class BlockTag {
        public final TagKey<Block> tag;

        private BlockTag(TagKey<Block> tag) {
            this.tag = tag;
        }

        public boolean matches(Block block) {
            return block.builtInRegistryHolder().is(this.tag);
        }
    }

    public static class AllItemTags {
        public static final ItemTag CONTRAPTION_CONTROLLED = new ItemTag((TagKey<Item>)com.zurrtum.create.AllItemTags.CONTRAPTION_CONTROLLED);
        public static final ItemTag WRENCH = new ItemTag((TagKey<Item>)com.zurrtum.create.AllItemTags.TOOLS_WRENCH);
    }

    public static class AllBlockTags {
        public static final BlockTag COPYCAT_ALLOW = new BlockTag((TagKey<Block>)com.zurrtum.create.AllBlockTags.COPYCAT_ALLOW);
        public static final BlockTag COPYCAT_DENY = new BlockTag((TagKey<Block>)com.zurrtum.create.AllBlockTags.COPYCAT_DENY);
        public static final BlockTag FAN_TRANSPARENT = new BlockTag((TagKey<Block>)com.zurrtum.create.AllBlockTags.FAN_TRANSPARENT);
        public static final BlockTag MOVABLE_EMPTY_COLLIDER = new BlockTag((TagKey<Block>)com.zurrtum.create.AllBlockTags.MOVABLE_EMPTY_COLLIDER);
        public static final BlockTag NON_DOUBLE_DOOR = new BlockTag((TagKey<Block>)com.zurrtum.create.AllBlockTags.NON_DOUBLE_DOOR);
    }
}

