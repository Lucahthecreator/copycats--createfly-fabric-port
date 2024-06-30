package com.copycatsplus.copycats.content.copycat.base.model.assembly.fabric;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.*;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.quad.QuadTransform;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.SpriteFinder;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatRenderContextFabric extends CopycatRenderContext.Base<MutableQuadView, QuadEmitter> {
    public CopycatRenderContextFabric(MutableQuadView source, QuadEmitter destination) {
        super(source, destination);
    }

    static SpriteFinder spriteFinder = SpriteFinder.get(Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS));

    @Override
    public void assemblePiece(GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        if (cull.isCulled(source().lightFace())) {
            return;
        }
        assembleQuad(source(), destination(), select.toAABB(), offset.toVec3().subtract(select.minX, select.minY, select.minZ));
    }

    @Override
    public void assemblePiece(GlobalTransform globalTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        globalTransform.apply(select);
        globalTransform.apply(offset);
        globalTransform.apply(cull);
        if (cull.isCulled(source().lightFace())) {
            return;
        }
        assembleQuad(source(), destination(), select.toAABB(), offset.toVec3().subtract(select.minX, select.minY, select.minZ), globalTransform, transforms);
    }

    @Override
    public void assembleAll() {
        assembleAll(source(), destination());
    }

    private static void assembleAll(MutableQuadView src, QuadEmitter dest) {
        dest.copyFrom(src);
        dest.emit();
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move) {
        assembleQuad(source(), destination(), crop, move);
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move, QuadTransform... transforms) {
        assembleQuad(source(), destination(), crop, move, GlobalTransform.IDENTITY, transforms);
    }


    private static void assembleQuad(MutableQuadView src, QuadEmitter dest, AABB crop, Vec3 move) {
        dest.copyFrom(src);
        BakedModelHelper.cropAndMove(dest, spriteFinder.find(src), crop, move);
        dest.emit();
    }

    private static void assembleQuad(MutableQuadView src, QuadEmitter dest, AABB crop, Vec3 move, GlobalTransform globalTransform, QuadTransform... transforms) {
        dest.copyFrom(src);
        TextureAtlasSprite sprite = spriteFinder.find(src);
        BakedModelHelper.cropAndMove(dest, sprite, crop, move);
        MutableQuad mutableQuad = getMutableQuad(dest);
        globalTransform.apply(mutableQuad);
        mutableQuad.undoMutate();
        for (QuadTransform transform : transforms) {
            transform.transformVertices(mutableQuad, sprite);
        }
        mutableQuad.mutate();
        for (int i = 0; i < 4; i++) {
            BakedQuadHelper.setXYZ(dest, i, mutableQuad.vertices.get(i).xyz.toVec3());
            dest.uv(i, mutableQuad.vertices.get(i).uv.u, mutableQuad.vertices.get(i).uv.v);
        }
        // todo: assign lightFace
        dest.emit();
    }

    public static MutableQuad getMutableQuad(MutableQuadView vertexData) {
        List<MutableVertex> vertices = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            MutableVec3 xyz = new MutableVec3(vertexData.x(i), vertexData.y(i), vertexData.z(i));
            MutableUV uv = new MutableUV(vertexData.u(i), vertexData.v(i));
            vertices.add(new MutableVertex(xyz, uv));
        }
        return new MutableQuad(vertices, vertexData.lightFace());
    }
}
