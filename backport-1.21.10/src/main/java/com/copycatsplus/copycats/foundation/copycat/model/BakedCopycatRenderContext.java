/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.zurrtum.create.client.foundation.model.BakedModelHelper
 *  javax.annotation.Nullable
 *  net.minecraft.client.model.geom.builders.UVPair
 *  net.minecraft.client.renderer.block.model.BakedQuad
 *  net.minecraft.client.resources.model.QuadCollection$Builder
 *  net.minecraft.core.Direction
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.foundation.copycat.model.assembly.AssemblyTransform;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.CopycatRenderContext;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableAABB;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableCullFace;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableQuad;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableUV;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVec3;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.MutableVertex;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadAutoCull;
import com.copycatsplus.copycats.foundation.copycat.model.assembly.quad.QuadTransform;
import com.zurrtum.create.client.foundation.model.BakedModelHelper;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class BakedCopycatRenderContext
extends CopycatRenderContext.Base<List<BakedCopycatRenderContext.SourceQuad>, QuadCollection.Builder> {
    public BakedCopycatRenderContext(List<SourceQuad> source, QuadCollection.Builder destination, String key) {
        super(source, destination, key);
    }

    @Override
    public void assemblePiece(AssemblyTransform transform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        this.assemblePiece(transform, offset, select, cull, new QuadTransform[0]);
    }

    @Override
    public void assemblePiece(AssemblyTransform transform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform ... transforms) {
        transform.apply(select);
        transform.apply(offset);
        transform.apply(cull);
        AABB crop = select.toAABB();
        Vec3 move = offset.toVec3().subtract(select.minX, select.minY, select.minZ);
        for (SourceQuad source : this.source()) {
            if (cull.isCulled(source.quad().direction())) continue;
            this.assembleQuad(source, crop, move, transform, transforms);
        }
    }

    @Override
    public void assembleAll() {
        for (SourceQuad source : this.source()) {
            this.add(source.quad(), source.cullFace());
        }
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move) {
        this.assembleRaw(crop, move, new QuadTransform[0]);
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move, QuadTransform ... transforms) {
        for (SourceQuad source : this.source()) {
            this.assembleQuad(source, crop, move, AssemblyTransform.IDENTITY, transforms);
        }
    }

    private void assembleQuad(SourceQuad source, AABB crop, Vec3 move, AssemblyTransform assemblyTransform, QuadTransform ... transforms) {
        BakedQuad original = source.quad();
        int[] croppedVertices = BakedModelHelper.cropAndMove(original.vertices(), original.sprite(), crop, move);
        BakedQuad cropped = new BakedQuad(croppedVertices, original.tintIndex(), original.direction(), original.sprite(), original.shade(), original.lightEmission());
        MutableQuad quad = BakedCopycatRenderContext.mutable(cropped, source.cullFace());
        assemblyTransform.apply(quad);
        quad.undoMutate();
        for (QuadTransform transform : transforms) {
            if (transform.transformQuad(quad, cropped.sprite())) continue;
            return;
        }
        if (!quad.disableFinalAutoCull && !QuadAutoCull.BLOCK.transformQuad(quad, cropped.sprite())) {
            return;
        }
        quad.mutate();
        Direction lightFace = quad.computeLightFace();
        int[] vertices = cropped.vertices().clone();
        for (int i = 0; i < 4; i++) {
            MutableVertex vertex = quad.vertices.get(i);
            int offset = i * 8;
            vertices[offset] = Float.floatToRawIntBits((float)vertex.xyz.x);
            vertices[offset + 1] = Float.floatToRawIntBits((float)vertex.xyz.y);
            vertices[offset + 2] = Float.floatToRawIntBits((float)vertex.xyz.z);
            vertices[offset + 4] = Float.floatToRawIntBits((float)vertex.uv.u);
            vertices[offset + 5] = Float.floatToRawIntBits((float)vertex.uv.v);
        }
        BakedQuad baked = new BakedQuad(vertices, cropped.tintIndex(), lightFace, cropped.sprite(), cropped.shade(), cropped.lightEmission());
        this.add(baked, quad.cullFace);
    }

    private void add(BakedQuad quad, @Nullable Direction cullFace) {
        if (cullFace == null) {
            ((QuadCollection.Builder)this.destination()).addUnculledFace(quad);
        } else {
            ((QuadCollection.Builder)this.destination()).addCulledFace(cullFace, quad);
        }
    }

    private static MutableQuad mutable(BakedQuad quad, @Nullable Direction cullFace) {
        ArrayList<MutableVertex> vertices = new ArrayList<MutableVertex>(4);
        int[] data = quad.vertices();
        for (int i = 0; i < 4; i++) {
            int offset = i * 8;
            vertices.add(new MutableVertex(
                    new MutableVec3(Float.intBitsToFloat(data[offset]), Float.intBitsToFloat(data[offset + 1]), Float.intBitsToFloat(data[offset + 2])),
                    new MutableUV(Float.intBitsToFloat(data[offset + 4]), Float.intBitsToFloat(data[offset + 5]))));
        }
        return new MutableQuad(vertices, cullFace);
    }

    public record SourceQuad(BakedQuad quad, @Nullable Direction cullFace) {
    }
}
