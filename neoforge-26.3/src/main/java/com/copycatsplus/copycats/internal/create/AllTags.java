package com.copycatsplus.copycats.internal.create;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class AllTags {
    public static class AllBlockTags {
        public static final AllTags.BlockTag COPYCAT_ALLOW = new AllTags.BlockTag(com.zurrtum.create.AllBlockTags.COPYCAT_ALLOW);
        public static final AllTags.BlockTag COPYCAT_DENY = new AllTags.BlockTag(com.zurrtum.create.AllBlockTags.COPYCAT_DENY);
        public static final AllTags.BlockTag FAN_TRANSPARENT = new AllTags.BlockTag(com.zurrtum.create.AllBlockTags.FAN_TRANSPARENT);
        public static final AllTags.BlockTag MOVABLE_EMPTY_COLLIDER = new AllTags.BlockTag(com.zurrtum.create.AllBlockTags.MOVABLE_EMPTY_COLLIDER);
        public static final AllTags.BlockTag NON_DOUBLE_DOOR = new AllTags.BlockTag(com.zurrtum.create.AllBlockTags.NON_DOUBLE_DOOR);
    }

    public static class AllItemTags {
        public static final AllTags.ItemTag CONTRAPTION_CONTROLLED = new AllTags.ItemTag(com.zurrtum.create.AllItemTags.CONTRAPTION_CONTROLLED);
        public static final AllTags.ItemTag WRENCH = new AllTags.ItemTag(com.zurrtum.create.AllItemTags.TOOLS_WRENCH);
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

    public static class ItemTag {
        public final TagKey<Item> tag;

        private ItemTag(TagKey<Item> tag) {
            this.tag = tag;
        }
    }
}
