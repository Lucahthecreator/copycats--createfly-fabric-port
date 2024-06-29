package com.copycatsplus.copycats.forge;

import com.copycatsplus.copycats.content.copycat.base.model.SimpleCopycatPart;
import com.copycatsplus.copycats.content.copycat.fluid_pipe.forge.CopycatFluidPipeModelForge;
import com.copycatsplus.copycats.content.copycat.shaft.forge.CopycatShaftModelForge;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Block;

public class CCBlocksImpl {

    public static void getWrappedBlockState(DataGenContext<Block, ? extends Block> c, RegistrateBlockstateProvider p, String name) {
        p.simpleBlock(c.getEntry(), p.models().withExistingParent(name, "block/barrier"));
    }

    public static BakedModel getShaftModel(BakedModel original, BakedModel copycat) {
        return new CopycatShaftModelForge(original, copycat);
    }

    public static BakedModel getFluidPipeModel(BakedModel original, SimpleCopycatPart copycat) {
        return new CopycatFluidPipeModelForge(original, copycat);
    }
}
