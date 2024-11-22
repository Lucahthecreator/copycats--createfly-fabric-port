package com.copycatsplus.copycats.content.copycat.pane;

import com.copycatsplus.copycats.foundation.copycat.model.CopycatModelCore;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

import static com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext.*;
import static com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace.*;

public class CopycatPaneModelCore extends CopycatModelCore {

    @Override
    public void registerModels(List<ModelEntry> entries) {
        entries.add(new ModelEntry(MATERIAL_KEY, ModelGetter.MATERIAL, this, updatePropertiesIfMatch(IronBarsBlock.class), EntryType.COPYCAT));
    }

    @Override
    public void emitCopycatQuads(String key, BlockState state, CopycatRenderContext context, BlockState material) {
        List<Direction> present = new ArrayList<>();
        for (Direction direction : Iterate.horizontalDirections) {
            if (state.getValue(CopycatPaneBlock.propertyForDirection(direction)))
                present.add(direction);
        }
        AssemblyTransform transform = t -> t.rotateY(0);
        context.assemblePiece(transform,
                vec3(7, 0, 7),
                aabb(1, 16, 2).move(7, 0, 7),
                cull(EAST | (present.contains(Direction.SOUTH) ? SOUTH : 0) | (present.contains(Direction.NORTH) ? NORTH : 0) | (present.contains(Direction.EAST) ? EAST : 0) | (present.contains(Direction.WEST) ? WEST : 0)));
        context.assemblePiece(transform,
                vec3(8, 0, 7),
                aabb(1, 16, 2).move(8, 0, 7),
                cull(WEST | (present.contains(Direction.SOUTH) ? SOUTH : 0) | (present.contains(Direction.NORTH) ? NORTH : 0) | (present.contains(Direction.EAST) ? EAST : 0) | (present.contains(Direction.WEST) ? WEST : 0)));

        for (Direction direction : Iterate.horizontalDirections) {
            if (state.getValue(CopycatPaneBlock.propertyForDirection(direction))) {
                AssemblyTransform directionTransform = t -> t.rotateY((int) direction.toYRot());
                context.assemblePiece(directionTransform,
                        vec3(7, 0, 9),
                        aabb(1, 16, 7).move(0, 0, 9),
                        cull(0));
                context.assemblePiece(directionTransform,
                        vec3(8, 0, 9),
                        aabb(1, 16, 7).move(0, 0, 9),
                        cull(0));
            }
        }
    }
}
