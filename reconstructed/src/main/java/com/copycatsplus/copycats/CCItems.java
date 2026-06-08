/*
 * Decompiled with CFR 0.152.
 */
package com.copycatsplus.copycats;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.CCTags;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.config.FeatureToggle;
import com.copycatsplus.copycats.content.copycat.board.CopycatBoxItem;
import com.copycatsplus.copycats.content.copycat.board.CopycatCatwalkItem;
import com.copycatsplus.copycats.foundation.tooltip.CopycatCharacteristics;
import com.copycatsplus.copycats.foundation.tooltip.CopycatDescription;
import com.copycatsplus.copycats.registrate.CopycatRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.zurrtum.create.foundation.data.AssetLookup;

public class CCItems {
    private static final CopycatRegistrate REGISTRATE = Copycats.getRegistrate();
    public static final ItemEntry<CopycatBoxItem> COPYCAT_BOX = ((ItemBuilder)REGISTRATE.item("copycat_box", CopycatBoxItem::new).onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE, CopycatCharacteristics.PRE_ASSEMBLED)).model(AssetLookup.customBlockItemModel("copycat_base", "box")).transform(FeatureToggle.registerDependent(CCBlocks.COPYCAT_BOARD))).tag(CCTags.Items.COPYCAT_BOX.tag).register();
    public static final ItemEntry<CopycatCatwalkItem> COPYCAT_CATWALK = ((ItemBuilder)REGISTRATE.item("copycat_catwalk", CopycatCatwalkItem::new).onRegister(CopycatDescription.register(CopycatCharacteristics.COPYCAT, CopycatCharacteristics.CT_TOGGLE, CopycatCharacteristics.MULTI_STATE, CopycatCharacteristics.PRE_ASSEMBLED)).model(AssetLookup.customBlockItemModel("copycat_base", "catwalk")).transform(FeatureToggle.registerDependent(CCBlocks.COPYCAT_BOARD))).tag(CCTags.Items.COPYCAT_CATWALK.tag).register();

    public static void register() {
    }
}

