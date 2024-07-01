package com.copycatsplus.copycats.content.copycat.base.model.assembly.forge;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.*;
import com.copycatsplus.copycats.content.copycat.base.model.assembly.quad.QuadTransform;
import com.simibubi.create.foundation.model.BakedModelHelper;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CopycatRenderContextForge extends CopycatRenderContext.Base<List<BakedQuad>, List<BakedQuad>> {

    public CopycatRenderContextForge(List<BakedQuad> source, List<BakedQuad> destination) {
        super(source, destination);
    }

    @Override
    public void assemblePiece(AssemblyTransform assemblyTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        assemblyTransform.apply(select);
        assemblyTransform.apply(offset);
        assemblyTransform.apply(cull);
        AABB aabb = select.toAABB();
        Vec3 vec3 = offset.toVec3().subtract(select.minX, select.minY, select.minZ);
        for (BakedQuad quad : source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, destination(), aabb, vec3);
        }
    }

    @Override
    public void assemblePiece(AssemblyTransform assemblyTransform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        assemblyTransform.apply(select);
        assemblyTransform.apply(offset);
        assemblyTransform.apply(cull);
        AABB aabb = select.toAABB();
        Vec3 vec3 = offset.toVec3().subtract(select.minX, select.minY, select.minZ);
        for (BakedQuad quad : source()) {
            if (cull.isCulled(quad.getDirection())) {
                continue;
            }
            assembleQuad(quad, destination(), aabb, vec3, assemblyTransform, transforms);
        }
    }

    @Override
    public void assembleAll() {
        for (BakedQuad quad : source()) {
            assembleQuad(quad, destination());
        }
    }

    private static void assembleQuad(BakedQuad src, List<BakedQuad> dest) {
        dest.add(BakedQuadHelper.clone(src));
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move) {
        for (BakedQuad quad : source()) {
            assembleQuad(quad, destination(), crop, move);
        }
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move, QuadTransform... transforms) {
        for (BakedQuad quad : source()) {
            assembleQuad(quad, destination(), crop, move, AssemblyTransform.IDENTITY, transforms);
        }
    }

    private static void assembleQuad(BakedQuad src, List<BakedQuad> dest, AABB crop, Vec3 move) {
        dest.add(BakedQuadHelper.cloneWithCustomGeometry(src,
                BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move)));
    }

    private static void assembleQuad(BakedQuad src, List<BakedQuad> dest, AABB crop, Vec3 move, AssemblyTransform assemblyTransform, QuadTransform... transforms) {
        int[] vertices = BakedModelHelper.cropAndMove(src.getVertices(), src.getSprite(), crop, move);
        MutableQuad mutableQuad = getMutableQuad(new BakedQuad(vertices, src.getTintIndex(), src.getDirection(), src.getSprite(), src.isShade()));
        assemblyTransform.apply(mutableQuad);
        mutableQuad.undoMutate();
        for (QuadTransform transform : transforms) {
            transform.transformQuad(mutableQuad, src.getSprite());
        }
        mutableQuad.mutate();
        for (int i = 0; i < 4; i++) {
            BakedQuadHelper.setXYZ(vertices, i, mutableQuad.vertices.get(i).xyz.toVec3());
            BakedQuadHelper.setU(vertices, i, mutableQuad.vertices.get(i).uv.u);
            BakedQuadHelper.setV(vertices, i, mutableQuad.vertices.get(i).uv.v);
        }
        dest.add(new BakedQuad(vertices, src.getTintIndex(), mutableQuad.cullFace, src.getSprite(), src.isShade()));
    }

    public static MutableQuad getMutableQuad(BakedQuad quad) {
        int[] vertexData = quad.getVertices();
        List<MutableVertex> vertices = new ArrayList<>(4);
        for (int i = 0; i < 4; i++) {
            MutableVec3 xyz = new MutableVec3(BakedQuadHelper.getXYZ(vertexData, i));
            MutableUV uv = new MutableUV(BakedQuadHelper.getU(vertexData, i), BakedQuadHelper.getV(vertexData, i));
            vertices.add(new MutableVertex(xyz, uv));
        }
        return new MutableQuad(vertices, quad.getDirection());
    }
}
