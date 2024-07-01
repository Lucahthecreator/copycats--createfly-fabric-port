package com.copycatsplus.copycats.content.copycat.base.model.assembly;

import com.copycatsplus.copycats.content.copycat.base.model.assembly.quad.*;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface CopycatRenderContext {

    /**
     * Copy a piece of cuboid from the source model and assemble it to the copycat.
     *
     * @param globalTransform The global transform to apply to the entire operation, changing the positions, AABBs and cull faces.
     * @param offset          In voxel space, the final position of the assembled piece.
     * @param select          In voxel space, the selection on the source model to copy from.
     * @param cull            Faces to skip rendering in the destination model.
     */
    void assemblePiece(
            @NotNull GlobalTransform globalTransform,
            MutableVec3 offset,
            MutableAABB select,
            MutableCullFace cull
    );

    /**
     * Copy a piece of cuboid from the source model, apply quad transforms, and assemble it to the copycat.
     *
     * @param globalTransform The global transform to apply to the entire operation, changing the positions, AABBs, cull faces and quad transforms.
     * @param offset          In voxel space, the final position of the assembled piece.
     * @param select          In voxel space, the selection on the source model to copy from.
     * @param cull            Faces to skip rendering in the destination model.
     * @param transforms      Quad transforms to apply to the copied quads.
     */
    void assemblePiece(
            @NotNull GlobalTransform globalTransform,
            MutableVec3 offset,
            MutableAABB select,
            MutableCullFace cull,
            QuadTransform... transforms
    );

    /**
     * Copy ALL quads from source to destination without modification.
     */
    void assembleAll();

    /**
     * Copy ALL quads from source to destination while applying the specified crop and move.
     */
    void assembleRaw(AABB crop, Vec3 move);

    /**
     * Copy ALL quads from source to destination while applying the specified transforms.
     */
    void assembleRaw(AABB crop, Vec3 move, QuadTransform... transforms);

    /**
     * Specify faces to be culled. You should import static constants from {@link MutableCullFace} and use bitwise OR to combine them.
     *
     * @param mask The faces to be culled. Specify multiple faces by bitwise OR.
     */
    static MutableCullFace cull(int mask) {
        return new MutableCullFace(mask);
    }

    /**
     * Specify a position in voxel space, where each block is 16 units.
     */
    static MutableVec3 vec3(double x, double y, double z) {
        return new MutableVec3(x / 16, y / 16, z / 16);
    }

    /**
     * Specify a position in voxel space, where each block is 16 units. This position is used as a pivot point.
     */
    static MutableVec3.AsPivot pivot(double x, double y, double z) {
        return new MutableVec3.AsPivot(x / 16, y / 16, z / 16);
    }

    /**
     * Specify axis angles in degrees. Rotations are applied in the order of X, Y, Z.
     */
    static MutableVec3.AsAngle angle(double x, double y, double z) {
        return new MutableVec3.AsAngle(x, y, z);
    }

    /**
     * Specify scaling factors in each axis. 1 is the original size.
     */
    static MutableVec3.AsScale scale(double x, double y, double z) {
        return new MutableVec3.AsScale(x, y, z);
    }

    /**
     * Specify a volume in voxel space, where each block is 16 units. This volume is used as a selection box.
     * <p>
     * By default, volumes start at (0, 0, 0) and extend to the specified size. Use {@link MutableAABB#move(double, double, double)} to move the volume.
     */
    static MutableAABB aabb(double sizeX, double sizeY, double sizeZ) {
        return new MutableAABB(sizeX / 16, sizeY / 16, sizeZ / 16);
    }

    /**
     * Freely rotate the quad around the pivot point. Rotations are performed in the order of X, Y, Z.
     * <p>
     * Rotations of any angle and of multiple axes are allowed.
     */
    static QuadRotate rotate(MutableVec3.AsPivot pivot, MutableVec3.AsAngle rot) {
        return new QuadRotate(pivot, rot);
    }

    /**
     * Scale the quad around the pivot point.
     */
    static QuadScale scale(MutableVec3.AsPivot pivot, MutableVec3.AsScale scale) {
        return new QuadScale(pivot, scale);
    }

    /**
     * Translate the quad by the specified amount.
     */
    static QuadTranslate translate(double x, double y, double z) {
        return new QuadTranslate(x / 16, y / 16, z / 16);
    }

    /**
     * Map the height of the quad in the specified direction to the specified function.
     * <p>
     * For example, if the mapping direction is UP and the function returns a scaling factor of 0.5, the height of the quad will be halved.
     * If the mapping direction is DOWN and the function returns 0.5, the bottom of the quad will be raised to 50% of its original height.
     * <p>
     * The mapping function can return values that vary with the position of the vertex to achieve slopes.
     */
    static QuadSlope slope(Direction face, QuadSlope.QuadSlopeFunction func) {
        return new QuadSlope(face, (a, b) -> func.apply(a * 16, b * 16) / 16);
    }

    /**
     * Shear the quad in the specified direction.
     *
     * @param axis      The axis to be sheared. The direction of this axis will be changed while the other two axes remain the same.
     * @param direction The direction to "pull" the positive end of the axis towards.
     * @param amount    The amount of shear in voxel space. 16 units is the width of a block.
     */
    static QuadShear shear(Direction.Axis axis, Direction direction, double amount) {
        return new QuadShear(axis, direction, amount / 16);
    }

    /**
     * Wrap quad transforms so that the textures remain visually in the same position while the vertices are being moved,
     * as opposed to the default behavior where the textures are stretched or squished along with the vertices.
     */
    static QuadUVUpdate updateUV(QuadTransform... transforms) {
        return new QuadUVUpdate(transforms);
    }

    /**
     * Modify the lighting direction of the quad.
     */
    static QuadLightDirection lightDirection(Function<Direction, Direction> directionMapper) {
        return new QuadLightDirection(directionMapper);
    }

    abstract class Base<Source, Destination> implements CopycatRenderContext {
        private final Source source;
        private final Destination destination;

        public Base(Source source, Destination destination) {
            this.source = source;
            this.destination = destination;
        }

        public Source source() {
            return source;
        }

        public Destination destination() {
            return destination;
        }
    }
}
