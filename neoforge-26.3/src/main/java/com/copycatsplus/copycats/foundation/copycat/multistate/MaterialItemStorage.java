package com.copycatsplus.copycats.foundation.copycat.multistate;

import com.zurrtum.create.AllBlocks;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
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
    private Map<String, MaterialItemStorage.MaterialItem> storage;

    private MaterialItemStorage(Set<String> properties) {
        this.storage = new HashMap<>(properties.size());

        for (String property : properties) {
            this.storage.put(property, new MaterialItemStorage.MaterialItem(AllBlocks.COPYCAT_BASE.defaultBlockState(), ItemStack.EMPTY));
        }
    }

    public static MaterialItemStorage create(Set<String> properties) {
        return new MaterialItemStorage(properties);
    }

    public void storeMaterialItem(String property, MaterialItemStorage.MaterialItem materialItem) {
        this.storage.put(property, materialItem);
    }

    @Nullable
    public MaterialItemStorage.MaterialItem getMaterialItem(String property) {
        return this.storage.get(property);
    }

    public Set<String> getAllProperties() {
        return this.storage.keySet();
    }

    public Set<BlockState> getAllMaterials() {
        return this.storage.values().stream().map(MaterialItemStorage.MaterialItem::material).collect(Collectors.toSet());
    }

    public Set<MaterialItemStorage.MaterialItem> getAllMaterialItems() {
        return new HashSet<>(this.storage.values());
    }

    public List<ItemStack> getAllConsumedItems() {
        return this.storage
            .values()
            .stream()
            .map(MaterialItemStorage.MaterialItem::consumedItem)
            .filter(stack -> !stack.isEmpty())
            .collect(Collectors.toList());
    }

    public Map<String, BlockState> getMaterialMap() {
        return this.storage.entrySet().stream().collect(Collectors.toMap(Entry::getKey, s -> s.getValue().material));
    }

    public boolean hasCustomMaterial(String property) {
        return this.storage.get(property).hasCustomMaterial();
    }

    public void remapStorage(Function<String, String> keyMapper) {
        Map<String, MaterialItemStorage.MaterialItem> newStorage = new HashMap<>();
        this.storage.forEach((key, materialItem) -> newStorage.put(keyMapper.apply(key), materialItem));
        this.storage = newStorage;
    }

    public void write(ValueOutput output, boolean safe) {
        this.storage.forEach((key, materialItem) -> materialItem.write(output.child(key), safe));
    }

    public boolean read(ValueInput input) {
        AtomicBoolean anyUpdated = new AtomicBoolean(false);
        this.storage.keySet().forEach(key -> {
            MaterialItemStorage.MaterialItem newVersion = MaterialItemStorage.MaterialItem.read(input.childOrEmpty(key));
            MaterialItemStorage.MaterialItem oldVersion = this.storage.put(key, newVersion);
            if (oldVersion != null && (newVersion.material() != oldVersion.material() || newVersion.enableCT() != oldVersion.enableCT()) && !anyUpdated.get()) {
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
            output.store("material", BlockState.CODEC, this.material);
            ItemStack stack = safe ? new ItemStack(this.consumedItem.typeHolder(), this.consumedItem.getCount(), DataComponentPatch.EMPTY) : this.consumedItem;
            if (!stack.isEmpty()) {
                output.store("consumedItem", ItemStack.CODEC, stack);
            }

            output.putBoolean("enableCT", this.enableCT);
        }

        public static MaterialItemStorage.MaterialItem read(ValueInput input) {
            return new MaterialItemStorage.MaterialItem(
                input.<BlockState>read("material", BlockState.CODEC).orElse(AllBlocks.COPYCAT_BASE.defaultBlockState()),
                input.<ItemStack>read("consumedItem", ItemStack.CODEC).orElse(ItemStack.EMPTY),
                input.getBooleanOr("enableCT", true)
            );
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
            return !this.material.is(AllBlocks.COPYCAT_BASE);
        }
    }
}
