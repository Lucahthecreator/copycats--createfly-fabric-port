package com.copycatsplus.copycats.content.copycat.button;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class CopycatButtonModelCore extends CopycatModelCore {
    @Override
    public void registerModels(List<CopycatModelCore.ModelEntry> entries) {
        entries.add(
            new CopycatModelCore.ModelEntry(
                "material", CopycatModelCore.ModelGetter.MATERIAL, this, updatePropertiesIfMatch(ButtonBlock.class), CopycatModelCore.EntryType.COPYCAT
            )
        );
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        if (material.getBlock() instanceof ButtonBlock) {
            context.assembleAll();
        } else {
            AttachFace face = state.getValue(ButtonBlock.FACE);
            int rot = (int)state.getValue(ButtonBlock.FACING).toYRot();
            boolean pressed = state.getValue(ButtonBlock.POWERED);
            // Glass borders sit at the outside edge of the texture. Small button
            // crops need to include that edge instead of sampling only the clear center.
            boolean glass = material.is(Blocks.GLASS)
                || material.is(Blocks.TINTED_GLASS)
                || BuiltInRegistries.BLOCK.getKey(material.getBlock()).getPath().endsWith("_stained_glass");
            double near = glass ? 0 : 1;
            double farX = glass ? 13 : 12;
            double farZ = glass ? 14 : 13;
            switch (face) {
                case WALL:
                    AssemblyTransform transformx = t -> t.rotateY(rot);
                    context.assemblePiece(
                        transformx,
                        CopycatRenderContext.vec3(5.0, 6.0, pressed ? 0 : 1),
                        CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(near, near, near),
                        CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transformx,
                        CopycatRenderContext.vec3(5.0, 8.0, pressed ? 0 : 1),
                        CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(near, glass ? 14 : 13, near),
                        CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transformx,
                        CopycatRenderContext.vec3(8.0, 8.0, pressed ? 0 : 1),
                        CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(farX, glass ? 14 : 13, near),
                        CopycatRenderContext.cull(MutableCullFace.DOWN | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transformx,
                        CopycatRenderContext.vec3(8.0, 6.0, pressed ? 0 : 1),
                        CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(farX, near, near),
                        CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.WEST)
                    );
                    if (!pressed) {
                        context.assemblePiece(
                            transformx,
                            CopycatRenderContext.vec3(5.0, 6.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 2.0, 1.0),
                            CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.EAST)
                        );
                        context.assemblePiece(
                            transformx,
                            CopycatRenderContext.vec3(5.0, 8.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(0.0, 14.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | MutableCullFace.EAST)
                        );
                        context.assemblePiece(
                            transformx,
                            CopycatRenderContext.vec3(8.0, 8.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(13.0, 14.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.DOWN | MutableCullFace.WEST)
                        );
                        context.assemblePiece(
                            transformx,
                            CopycatRenderContext.vec3(8.0, 6.0, 0.0),
                            CopycatRenderContext.aabb(3.0, 2.0, 1.0).move(13.0, 0.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.UP | MutableCullFace.WEST)
                        );
                    }
                    break;
                case CEILING:
                case FLOOR:
                    AssemblyTransform transform = t -> t.rotateY(rot).flipY(face != AttachFace.FLOOR);
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(5.0, pressed ? 0 : 1, 6.0),
                        CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(near, 0.0, near),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(5.0, pressed ? 0 : 1, 8.0),
                        CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(near, 0.0, farZ),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.EAST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(8.0, pressed ? 0 : 1, 6.0),
                        CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(farX, 0.0, near),
                        CopycatRenderContext.cull(MutableCullFace.SOUTH | MutableCullFace.WEST)
                    );
                    context.assemblePiece(
                        transform,
                        CopycatRenderContext.vec3(8.0, pressed ? 0 : 1, 8.0),
                        CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(farX, 0.0, farZ),
                        CopycatRenderContext.cull(MutableCullFace.NORTH | MutableCullFace.WEST)
                    );
                    if (!pressed) {
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(5.0, 0.0, 6.0),
                            CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(0.0, 0.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.EAST)
                        );
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(5.0, 0.0, 8.0),
                            CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(0.0, 0.0, 14.0),
                            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.EAST)
                        );
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(8.0, 0.0, 6.0),
                            CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(13.0, 0.0, 0.0),
                            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.SOUTH | MutableCullFace.WEST)
                        );
                        context.assemblePiece(
                            transform,
                            CopycatRenderContext.vec3(8.0, 0.0, 8.0),
                            CopycatRenderContext.aabb(3.0, 1.0, 2.0).move(13.0, 0.0, 14.0),
                            CopycatRenderContext.cull(MutableCullFace.UP | MutableCullFace.NORTH | MutableCullFace.WEST)
                        );
                    }
            }
        }
    }
}
