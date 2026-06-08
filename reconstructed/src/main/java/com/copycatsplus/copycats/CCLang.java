/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.client.catnip.lang.LangBuilder
 *  com.zurrtum.create.client.catnip.lang.LangNumberFormat
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.state.BlockState
 */
package com.copycatsplus.copycats;

import com.zurrtum.create.client.catnip.lang.LangBuilder;
import com.zurrtum.create.client.catnip.lang.LangNumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class CCLang {
    public static MutableComponent translateDirect(String key, Object ... args) {
        return Component.translatable((String)("copycats." + key), (Object[])CCLang.resolveBuilders(args));
    }

    public static String asId(String name) {
        return name.toLowerCase(Locale.ROOT);
    }

    public static String nonPluralId(String name) {
        String asId = CCLang.asId(name);
        return asId.endsWith("s") ? asId.substring(0, asId.length() - 1) : asId;
    }

    public static List<Component> translatedOptions(String prefix, String ... keys) {
        ArrayList<Component> result = new ArrayList<Component>(keys.length);
        for (String key : keys) {
            result.add((Component)CCLang.translate((String)(prefix != null ? prefix + "." : "") + key, new Object[0]).component());
        }
        return result;
    }

    public static LangBuilder builder() {
        return new LangBuilder("copycats");
    }

    public static LangBuilder builder(String namespace) {
        return new LangBuilder(namespace);
    }

    public static LangBuilder blockName(BlockState state) {
        return CCLang.builder().add(state.getBlock().getName());
    }

    public static LangBuilder itemName(ItemStack stack) {
        return CCLang.builder().add(stack.getHoverName().copy());
    }

    public static LangBuilder number(double d) {
        return CCLang.builder().text(LangNumberFormat.format((double)d));
    }

    public static LangBuilder translate(String langKey, Object ... args) {
        return CCLang.builder().translate(langKey, args);
    }

    public static LangBuilder text(String text) {
        return CCLang.builder().text(text);
    }

    public static Object[] resolveBuilders(Object[] args) {
        for (int i = 0; i < args.length; ++i) {
            Object object = args[i];
            if (!(object instanceof LangBuilder)) continue;
            LangBuilder cb = (LangBuilder)object;
            args[i] = cb.component();
        }
        return args;
    }
}

