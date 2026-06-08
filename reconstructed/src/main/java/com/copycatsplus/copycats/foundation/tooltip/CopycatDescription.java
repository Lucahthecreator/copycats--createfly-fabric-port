/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.catnip.data.Iterate
 *  com.zurrtum.create.catnip.data.Pair
 *  com.zurrtum.create.client.catnip.lang.FontHelper$Palette
 *  com.zurrtum.create.client.foundation.item.TooltipHelper
 *  com.zurrtum.create.client.foundation.item.TooltipModifier
 *  com.zurrtum.create.client.foundation.utility.CreateLang
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.locale.Language
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.copycatsplus.copycats.foundation.tooltip;

import com.copycatsplus.copycats.foundation.tooltip.CopycatCharacteristics;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.zurrtum.create.catnip.data.Iterate;
import com.zurrtum.create.catnip.data.Pair;
import com.zurrtum.create.client.catnip.lang.FontHelper;
import com.zurrtum.create.client.foundation.item.TooltipHelper;
import com.zurrtum.create.client.foundation.item.TooltipModifier;
import com.zurrtum.create.client.foundation.utility.CreateLang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    public static final Map<Item, List<CopycatCharacteristics>> ITEM_CHARACTERISTICS = new HashMap<Item, List<CopycatCharacteristics>>();
    protected final Item item;
    private Language cachedLanguage = null;
    private Map<String, List<Object>> cachedArgs = new HashMap<String, List<Object>>();
    private Map<CopycatCharacteristics, Pair<List<Component>, List<Component>>> descriptions;
    @Nullable
    private List<Component> shortDescription = null;
    @Nullable
    private List<Component> longDescription = null;

    public static void register(ItemLike item, CopycatCharacteristics ... characteristics) {
        ITEM_CHARACTERISTICS.put(item.asItem(), List.of(characteristics));
    }

    public static <T extends ItemLike> NonNullConsumer<? super T> register(CopycatCharacteristics ... characteristics) {
        return item -> CopycatDescription.register((ItemLike)item.asItem(), characteristics);
    }

    protected CopycatDescription(Item item) {
        this.item = item;
    }

    private void loadDescriptions(Item item) {
        List<CopycatCharacteristics> characteristics = ITEM_CHARACTERISTICS.get(item);
        if (characteristics == null) {
            this.shortDescription = new ArrayList<Component>();
            this.longDescription = new ArrayList<Component>();
            return;
        }
        this.shortDescription = new ArrayList<Component>(characteristics.size());
        this.longDescription = new ArrayList<Component>(characteristics.size() * 2);
        for (CopycatCharacteristics characteristic : characteristics) {
            Pair<List<Component>, List<Component>> pair = this.descriptions.get((Object)characteristic);
            if (pair == null) continue;
            this.shortDescription.addAll((Collection)pair.getFirst());
            this.longDescription.addAll((Collection)pair.getFirst());
            this.longDescription.addAll((Collection)pair.getSecond());
        }
        String[] holdDesc = CreateLang.translateDirect((String)"tooltip.holdForDescription", (Object[])new Object[]{"$"}).getString().split("\\$");
        MutableComponent keyShift = CreateLang.translateDirect((String)"tooltip.keyShift", (Object[])new Object[0]);
        for (boolean shift : Iterate.falseAndTrue) {
            MutableComponent tabBuilder = Component.empty();
            tabBuilder.append((Component)Component.literal((String)holdDesc[0]).withStyle(ChatFormatting.DARK_GRAY));
            tabBuilder.append((Component)keyShift.plainCopy().withStyle(shift ? ChatFormatting.WHITE : ChatFormatting.GRAY));
            if (holdDesc.length > 1) {
                tabBuilder.append((Component)Component.literal((String)holdDesc[1]).withStyle(ChatFormatting.DARK_GRAY));
            }
            (shift ? this.longDescription : this.shortDescription).add(0, (Component)tabBuilder);
            (shift ? this.longDescription : this.shortDescription).add(1, (Component)Component.empty());
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
        HashMap<String, List<Object>> newArgs = new HashMap<String, List<Object>>();
        for (CopycatCharacteristics characteristics : CopycatCharacteristics.all()) {
            newArgs.put(characteristics.getSerializedName(), Arrays.stream(characteristics.getArgs()).map(Supplier::get).toList());
        }
        if (!currentLanguage.equals(this.cachedLanguage) || !newArgs.equals(this.cachedArgs)) {
            this.cachedLanguage = currentLanguage;
            this.cachedArgs = newArgs;
            return true;
        }
        return false;
    }

    private void populateDescriptions() {
        this.descriptions = new HashMap<CopycatCharacteristics, Pair<List<Component>, List<Component>>>();
        for (CopycatCharacteristics characteristics : CopycatCharacteristics.all()) {
            String titleKey = characteristics.getTitleKey();
            String descKey = characteristics.getDescriptionKey();
            if (!this.cachedLanguage.has(titleKey) || !this.cachedLanguage.has(descKey)) continue;
            this.descriptions.put(characteristics, (Pair<List<Component>, List<Component>>)Pair.of(List.of(Component.literal((String)("- " + this.cachedLanguage.getOrDefault(titleKey))).withStyle(ChatFormatting.GRAY)), (Object)TooltipHelper.cutStringTextComponent((String)String.format(this.cachedLanguage.getOrDefault(descKey), this.cachedArgs.get(characteristics.getSerializedName()).toArray()), (FontHelper.Palette)FontHelper.Palette.STANDARD_CREATE)));
        }
    }

    @NotNull
    public static TooltipModifier create(ItemLike item) {
        CopycatDescription description = new CopycatDescription(item.asItem());
        return (tooltip, player) -> description.modify(item.asItem(), tooltip);
    }
}

