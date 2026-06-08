/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.AllBlocks
 *  javax.annotation.Nullable
 *  net.minecraft.core.component.DataComponentPatch
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.storage.ValueInput
 *  net.minecraft.world.level.storage.ValueOutput
 */
package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.zurrtum.create.AllBlocks;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class MaterialItemStorage {
    private Map<String, MaterialItem> storage;

    private MaterialItemStorage(Set<String> properties) {
        this.storage = new HashMap<String, MaterialItem>(properties.size());
        for (String property : properties) {
            this.storage.put(property, new MaterialItem(AllBlocks.COPYCAT_BASE.defaultBlockState(), ItemStack.EMPTY));
        }
    }

    public static MaterialItemStorage create(Set<String> properties) {
        return new MaterialItemStorage(properties);
    }

    public void storeMaterialItem(String property, MaterialItem materialItem) {
        this.storage.put(property, materialItem);
    }

    @Nullable
    public MaterialItem getMaterialItem(String property) {
        return this.storage.get(property);
    }

    public Set<String> getAllProperties() {
        return this.storage.keySet();
    }

    public Set<BlockState> getAllMaterials() {
        return this.storage.values().stream().map(MaterialItem::material).collect(Collectors.toSet());
    }

    public Set<MaterialItem> getAllMaterialItems() {
        return new HashSet<MaterialItem>(this.storage.values());
    }

    public List<ItemStack> getAllConsumedItems() {
        return this.storage.values().stream().map(MaterialItem::consumedItem).filter(stack -> !stack.isEmpty()).collect(Collectors.toList());
    }

    public Map<String, BlockState> getMaterialMap() {
        return this.storage.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, s -> ((MaterialItem)s.getValue()).material));
    }

    public boolean hasCustomMaterial(String property) {
        return this.storage.get(property).hasCustomMaterial();
    }

    public void remapStorage(Function<String, String> keyMapper) {
        HashMap<String, MaterialItem> newStorage = new HashMap<String, MaterialItem>();
        this.storage.forEach((key, materialItem) -> newStorage.put((String)keyMapper.apply((String)key), (MaterialItem)materialItem));
        this.storage = newStorage;
    }

    public void write(ValueOutput output, boolean safe) {
        this.storage.forEach((key, materialItem) -> materialItem.write(output.child(key), safe));
    }

    public boolean read(ValueInput input) {
        AtomicBoolean anyUpdated = new AtomicBoolean(false);
        this.storage.keySet().forEach(key -> {
            MaterialItem newVersion = MaterialItem.read(input.childOrEmpty(key));
            MaterialItem oldVersion = this.storage.put((String)key, newVersion);
            if (!(oldVersion == null || newVersion.material() == oldVersion.material() && newVersion.enableCT() == oldVersion.enableCT() || anyUpdated.get())) {
                anyUpdated.set(true);
            }
        });
        return anyUpdated.get();
    }

    public static class MaterialItem {
        private BlockState material;
        private ItemStack consumedItem;
        private boolean enableCT;

        public MaterialItem(BlockState material, ItemStack consumedItem) {
            this(material, consumedItem, true);
        }

        public MaterialItem(BlockState material, ItemStack consumedItem, boolean enableCT) {
            this.material = material;
            this.consumedItem = consumedItem;
            this.enableCT = enableCT;
        }

        public void write(ValueOutput output, boolean safe) {
            ItemStack stack;
            output.store("material", BlockState.CODEC, (Object)this.material);
            ItemStack itemStack = stack = safe ? new ItemStack(this.consumedItem.typeHolder(), this.consumedItem.getCount(), DataComponentPatch.EMPTY) : this.consumedItem;
            if (!stack.isEmpty()) {
                output.store("consumedItem", ItemStack.CODEC, (Object)stack);
            }
            output.putBoolean("enableCT", this.enableCT);
        }

        public static MaterialItem read(ValueInput input) {
            return new MaterialItem(input.read("material", BlockState.CODEC).orElse(AllBlocks.COPYCAT_BASE.defaultBlockState()), input.read("consumedItem", ItemStack.CODEC).orElse(ItemStack.EMPTY), input.getBooleanOr("enableCT", true));
        }

        public BlockState material() {
            return this.material;
        }

        public ItemStack consumedItem() {
            return this.consumedItem;
        }

        public boolean enableCT() {
            return this.enableCT;
        }

        public void setMaterial(BlockState material) {
            this.material = material;
        }

        public void setConsumedItem(ItemStack stack) {
            this.consumedItem = stack.copyWithCount(1);
        }

        public void setEnableCT(boolean enableCT) {
            this.enableCT = enableCT;
        }

        public boolean hasCustomMaterial() {
            return !this.material.is((Object)AllBlocks.COPYCAT_BASE);
        }
    }
}

