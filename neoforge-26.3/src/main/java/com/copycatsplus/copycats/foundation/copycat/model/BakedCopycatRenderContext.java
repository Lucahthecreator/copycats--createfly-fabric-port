package com.copycatsplus.copycats.foundation.copycat.model;

import com.copycatsplus.copycats.compat.render.CreateFlyNormals;
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
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection.Builder;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class BakedCopycatRenderContext extends CopycatRenderContext.Base<List<BakedCopycatRenderContext.SourceQuad>, Builder> {
    public BakedCopycatRenderContext(List<BakedCopycatRenderContext.SourceQuad> source, Builder destination, String key) {
        super(source, destination, key);
    }

    @Override
    public void assemblePiece(AssemblyTransform transform, MutableVec3 offset, MutableAABB select, MutableCullFace cull) {
        this.assemblePiece(transform, offset, select, cull, new QuadTransform[0]);
    }

    @Override
    public void assemblePiece(AssemblyTransform transform, MutableVec3 offset, MutableAABB select, MutableCullFace cull, QuadTransform... transforms) {
        transform.apply(select);
        transform.apply(offset);
        transform.apply(cull);
        AABB crop = select.toAABB();
        Vec3 move = offset.toVec3().subtract(select.minX, select.minY, select.minZ);

        for (BakedCopycatRenderContext.SourceQuad source : this.source()) {
            if (!cull.isCulled(source.quad().direction())) {
                this.assembleQuad(source, crop, move, transform, transforms);
            }
        }
    }

    @Override
    public void assembleAll() {
        for (BakedCopycatRenderContext.SourceQuad source : this.source()) {
            this.add(source.quad(), source.cullFace());
        }
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move) {
        this.assembleRaw(crop, move, new QuadTransform[0]);
    }

    @Override
    public void assembleRaw(AABB crop, Vec3 move, QuadTransform... transforms) {
        for (BakedCopycatRenderContext.SourceQuad source : this.source()) {
            this.assembleQuad(source, crop, move, AssemblyTransform.IDENTITY, transforms);
        }
    }

    private void assembleQuad(
        BakedCopycatRenderContext.SourceQuad source, AABB crop, Vec3 move, AssemblyTransform assemblyTransform, QuadTransform... transforms
    ) {
        BakedQuad cropped = BakedModelHelper.cropAndMove(source.quad(), crop, move);
        MutableQuad quad = mutable(cropped, source.cullFace());
        assemblyTransform.apply(quad);
        quad.undoMutate();

        for (QuadTransform transform : transforms) {
            if (!transform.transformQuad(quad, cropped.materialInfo().sprite())) {
                return;
            }
        }

        if (quad.disableFinalAutoCull || QuadAutoCull.BLOCK.transformQuad(quad, cropped.materialInfo().sprite())) {
            quad.mutate();
            Direction lightFace = quad.computeLightFace();
            BakedQuad baked = new BakedQuad(
                quad.vertices.get(0).xyz.toVec3().toVector3f(),
                quad.vertices.get(1).xyz.toVec3().toVector3f(),
                quad.vertices.get(2).xyz.toVec3().toVector3f(),
                quad.vertices.get(3).xyz.toVec3().toVector3f(),
                UVPair.pack(quad.vertices.get(0).uv.u, quad.vertices.get(0).uv.v),
                UVPair.pack(quad.vertices.get(1).uv.u, quad.vertices.get(1).uv.v),
                UVPair.pack(quad.vertices.get(2).uv.u, quad.vertices.get(2).uv.v),
                UVPair.pack(quad.vertices.get(3).uv.u, quad.vertices.get(3).uv.v),
                lightFace,
                cropped.materialInfo()
            );
            MutableVec3 normal = quad.computeFaceNormal();
            Vector3f vector = new Vector3f((float)normal.x, (float)normal.y, (float)normal.z);
            CreateFlyNormals.setNormal(baked, vector);
            this.add(baked, quad.cullFace);
        }
    }

    private void add(BakedQuad quad, @Nullable Direction cullFace) {
        if (cullFace == null) {
            this.destination().addUnculledFace(quad);
        } else {
            this.destination().addCulledFace(cullFace, quad);
        }
    }

    private static MutableQuad mutable(BakedQuad quad, @Nullable Direction cullFace) {
        ArrayList<MutableVertex> vertices = new ArrayList<>(4);
        vertices.add(vertex(quad.position0(), quad.packedUV0()));
        vertices.add(vertex(quad.position1(), quad.packedUV1()));
        vertices.add(vertex(quad.position2(), quad.packedUV2()));
        vertices.add(vertex(quad.position3(), quad.packedUV3()));
        return new MutableQuad(vertices, cullFace);
    }

    private static MutableVertex vertex(Vector3fc position, long packedUv) {
        return new MutableVertex(new MutableVec3(position.x(), position.y(), position.z()), new MutableUV(UVPair.unpackU(packedUv), UVPair.unpackV(packedUv)));
    }

    public record SourceQuad(BakedQuad quad, @Nullable Direction cullFace) {
    }
}
