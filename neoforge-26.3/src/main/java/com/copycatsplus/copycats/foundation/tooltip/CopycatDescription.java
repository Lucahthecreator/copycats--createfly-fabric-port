package com.copycatsplus.copycats.foundation.tooltip;

import com.copycatsplus.copycats.internal.registrate.util.nullness.NonNullConsumer;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.catnip.data.Pair;
import com.zurrtum.create.client.catnip.lang.FontHelper.Palette;
import com.zurrtum.create.client.foundation.item.TooltipHelper;
import com.zurrtum.create.client.foundation.item.TooltipModifier;
import com.zurrtum.create.client.foundation.utility.CreateLang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class CopycatDescription {
    public static final Map<Item, List<CopycatCharacteristics>> ITEM_CHARACTERISTICS = new HashMap<>();
    protected final Item item;
    private Language cachedLanguage = null;
    private Map<String, List<Object>> cachedArgs = new HashMap<>();
    private Map<CopycatCharacteristics, Pair<List<Component>, List<Component>>> descriptions;
    @Nullable
    private List<Component> shortDescription = null;
    @Nullable
    private List<Component> longDescription = null;

    public static void register(ItemLike item, CopycatCharacteristics... characteristics) {
        ITEM_CHARACTERISTICS.put(item.asItem(), List.of(characteristics));
    }

    public static <T extends ItemLike> NonNullConsumer<? super T> register(CopycatCharacteristics... characteristics) {
        return item -> register(item.asItem(), characteristics);
    }

    protected CopycatDescription(Item item) {
        this.item = item;
    }

    private void loadDescriptions(Item item) {
        List<CopycatCharacteristics> characteristics = ITEM_CHARACTERISTICS.get(item);
        if (characteristics == null) {
            this.shortDescription = new ArrayList<>();
            this.longDescription = new ArrayList<>();
        } else {
            this.shortDescription = new ArrayList<>(characteristics.size());
            this.longDescription = new ArrayList<>(characteristics.size() * 2);

            for (CopycatCharacteristics characteristic : characteristics) {
                Pair<List<Component>, List<Component>> pair = this.descriptions.get(characteristic);
                if (pair != null) {
                    this.shortDescription.addAll(pair.getFirst());
                    this.longDescription.addAll(pair.getFirst());
                    this.longDescription.addAll(pair.getSecond());
                }
            }

            String[] holdDesc = CreateLang.translateDirect("tooltip.holdForDescription", "$").getString().split("\\$");
            MutableComponent keyShift = CreateLang.translateDirect("tooltip.keyShift");

            for (boolean shift : Iterate.falseAndTrue) {
                MutableComponent tabBuilder = Component.empty();
                tabBuilder.append(Component.literal(holdDesc[0]).withStyle(ChatFormatting.DARK_GRAY));
                tabBuilder.append(keyShift.plainCopy().withStyle(shift ? ChatFormatting.WHITE : ChatFormatting.GRAY));
                if (holdDesc.length > 1) {
                    tabBuilder.append(Component.literal(holdDesc[1]).withStyle(ChatFormatting.DARK_GRAY));
                }

                (shift ? this.longDescription : this.shortDescription).add(0, tabBuilder);
                (shift ? this.longDescription : this.shortDescription).add(1, Component.empty());
            }
        }
    }

    public void modify(Item item, List<Component> tooltip) {
        if (this.shouldInvalidateCache()) {
            this.populateDescriptions();
            this.loadDescriptions(item);
        }

        if (this.shortDescription == null || this.longDescription == null) {
            this.loadDescriptions(item);
        }

        if (Minecraft.getInstance().hasShiftDown()) {
            if (this.longDescription != null) {
                tooltip.addAll(1, this.longDescription);
            }
        } else if (this.shortDescription != null) {
            tooltip.addAll(1, this.shortDescription);
        }
    }

    private boolean shouldInvalidateCache() {
        Language currentLanguage = Language.getInstance();
        HashMap<String, List<Object>> newArgs = new HashMap<>();

        for (CopycatCharacteristics characteristics : CopycatCharacteristics.all()) {
            newArgs.put(characteristics.getSerializedName(), Arrays.stream(characteristics.getArgs()).map(Supplier::get).toList());
        }

        if (currentLanguage.equals(this.cachedLanguage) && newArgs.equals(this.cachedArgs)) {
            return false;
        } else {
            this.cachedLanguage = currentLanguage;
            this.cachedArgs = newArgs;
            return true;
        }
    }

    private void populateDescriptions() {
        this.descriptions = new HashMap<>();

        for (CopycatCharacteristics characteristics : CopycatCharacteristics.all()) {
            String titleKey = characteristics.getTitleKey();
            String descKey = characteristics.getDescriptionKey();
            if (this.cachedLanguage.has(titleKey) && this.cachedLanguage.has(descKey)) {
                List<Component> title = List.of(Component.literal("- " + this.cachedLanguage.getOrDefault(titleKey)).withStyle(ChatFormatting.GRAY));
                List<Component> description = TooltipHelper.cutStringTextComponent(
                    String.format(this.cachedLanguage.getOrDefault(descKey), this.cachedArgs.get(characteristics.getSerializedName()).toArray()),
                    Palette.STANDARD_CREATE
                );
                this.descriptions.put(characteristics, Pair.of(title, description));
            }
        }
    }

    @NotNull
    public static TooltipModifier create(ItemLike item) {
        CopycatDescription description = new CopycatDescription(item.asItem());
        return (tooltip, player) -> description.modify(item.asItem(), tooltip);
    }
}
