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
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

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
        BakedQuad cropped = BakedModelHelper.cropAndMove((BakedQuad)source.quad(), (AABB)crop, (Vec3)move);
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
        BakedQuad baked = new BakedQuad((Vector3fc)quad.vertices.get((int)0).xyz.toVec3().toVector3f(), (Vector3fc)quad.vertices.get((int)1).xyz.toVec3().toVector3f(), (Vector3fc)quad.vertices.get((int)2).xyz.toVec3().toVector3f(), (Vector3fc)quad.vertices.get((int)3).xyz.toVec3().toVector3f(), UVPair.pack((float)quad.vertices.get((int)0).uv.u, (float)quad.vertices.get((int)0).uv.v), UVPair.pack((float)quad.vertices.get((int)1).uv.u, (float)quad.vertices.get((int)1).uv.v), UVPair.pack((float)quad.vertices.get((int)2).uv.u, (float)quad.vertices.get((int)2).uv.v), UVPair.pack((float)quad.vertices.get((int)3).uv.u, (float)quad.vertices.get((int)3).uv.v), cropped.tintIndex(), lightFace, cropped.sprite(), cropped.shade(), cropped.lightEmission());
        // Minecraft 1.21.11's BakedQuad stores vertices and a light face, but no
        // per-vertex normal field. Its renderer derives normals from the quad.
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
        vertices.add(BakedCopycatRenderContext.vertex(quad.position0(), quad.packedUV0()));
        vertices.add(BakedCopycatRenderContext.vertex(quad.position1(), quad.packedUV1()));
        vertices.add(BakedCopycatRenderContext.vertex(quad.position2(), quad.packedUV2()));
        vertices.add(BakedCopycatRenderContext.vertex(quad.position3(), quad.packedUV3()));
        return new MutableQuad(vertices, cullFace);
    }

    private static MutableVertex vertex(Vector3fc position, long packedUv) {
        return new MutableVertex(new MutableVec3(position.x(), position.y(), position.z()), new MutableUV(UVPair.unpackU((long)packedUv), UVPair.unpackV((long)packedUv)));
    }

    public record SourceQuad(BakedQuad quad, @Nullable Direction cullFace) {
    }
}
