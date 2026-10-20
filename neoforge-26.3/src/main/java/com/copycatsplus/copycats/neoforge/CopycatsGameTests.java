package com.copycatsplus.copycats.neoforge;

import com.copycatsplus.copycats.CCBlocks;
import com.copycatsplus.copycats.Copycats;
import com.copycatsplus.copycats.foundation.copycat.ICopycatBlock;
import com.zurrtum.create.AllBlocks;
import com.zurrtum.create.content.kinetics.base.DirectionalKineticBlock;
import com.zurrtum.create.content.kinetics.base.KineticBlockEntity;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.FunctionGameTestInstance;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * ZP5 checks for the NeoForge port, run by {@code runGameTestServer}. They build their setup in Create's empty test
 * structure ({@code create:gametest/empty_9x7x5}).
 */
public final class CopycatsGameTests {
    private static final Identifier STRUCTURE = Identifier.fromNamespaceAndPath("create", "gametest/empty_9x7x5");
    private static final DeferredRegister<Consumer<GameTestHelper>> FUNCTIONS = DeferredRegister.create(
        Registries.TEST_FUNCTION,
        Copycats.MODID
    );
    private static final List<String> TESTS = List.of("registry", "apply_material", "slab_loot", "shaft_transmits");

    static {
        FUNCTIONS.register("registry", () -> CopycatsGameTests::registry);
        FUNCTIONS.register("apply_material", () -> CopycatsGameTests::applyMaterial);
        FUNCTIONS.register("slab_loot", () -> CopycatsGameTests::slabLoot);
        FUNCTIONS.register("shaft_transmits", () -> CopycatsGameTests::shaftTransmits);
    }

    private CopycatsGameTests() {
    }

    public static void register(IEventBus modBus) {
        FUNCTIONS.register(modBus);
        modBus.addListener(CopycatsGameTests::registerTests);
    }

    private static void registerTests(RegisterGameTestsEvent event) {
        Holder<TestEnvironmentDefinition<?>> environment = event.registerEnvironment(Copycats.asResource("default"));
        for (String name : TESTS) {
            ResourceKey<Consumer<GameTestHelper>> function = ResourceKey.create(Registries.TEST_FUNCTION, Copycats.asResource(name));
            event.registerTest(
                Copycats.asResource(name),
                new FunctionGameTestInstance(function, new TestData<>(environment, STRUCTURE, 200, 0, true))
            );
        }
    }

    private static void floor(GameTestHelper helper) {
        for (int x = 0; x < 9; x++) {
            for (int z = 0; z < 5; z++) {
                helper.setBlock(new BlockPos(x, 0, z), Blocks.STONE);
            }
        }
    }

    /**
     * Every copycat block, its item and the block entity types are in the registries.
     */
    private static void registry(GameTestHelper helper) {
        long blocks = count(BuiltInRegistries.BLOCK);
        long items = count(BuiltInRegistries.ITEM);
        long blockEntities = count(BuiltInRegistries.BLOCK_ENTITY_TYPE);
        String counts = blocks + " blocks, " + items + " items, " + blockEntities + " block entity types";
        helper.assertTrue(blocks >= 40 && items >= 40 && blockEntities >= 5, "copycats registry: " + counts);
        for (String id : List.of("copycat_block", "copycat_slab", "copycat_board", "copycat_stairs", "copycat_shaft",
            "copycat_cogwheel", "copycat_sliding_door", "copycat_fluid_pipe")) {
            helper.assertTrue(BuiltInRegistries.BLOCK.containsKey(Copycats.asResource(id)), "missing block copycats:" + id);
        }
        Copycats.LOGGER.info("copycats:registry passed: {}", counts);
        helper.succeed();
    }

    private static long count(Registry<?> registry) {
        return registry.keySet().stream().filter(id -> id.getNamespace().equals(Copycats.MODID)).count();
    }

    /**
     * A survival player right-clicks a placed copycat block with oak planks: the block takes the planks as its
     * material and one plank is used up.
     */
    private static void applyMaterial(GameTestHelper helper) {
        floor(helper);
        BlockPos pos = new BlockPos(4, 1, 2);
        helper.setBlock(pos, CCBlocks.COPYCAT_BLOCK.get());
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.OAK_PLANKS, 4));
        BlockPos abs = helper.absolutePos(pos);
        helper.useBlock(pos, player, new BlockHitResult(Vec3.atCenterOf(abs).add(0, 0.5, 0), Direction.UP, abs, false));

        BlockState material = ICopycatBlock.getMaterial(helper.getLevel(), abs);
        int left = player.getItemInHand(InteractionHand.MAIN_HAND).getCount();
        String state = "material " + material + ", planks left " + left;
        helper.assertTrue(material.is(Blocks.OAK_PLANKS), "copycat_block did not take the planks: " + state);
        helper.assertTrue(left == 3, "survival player should use one plank: " + state);
        Copycats.LOGGER.info("copycats:apply_material passed: {}", state);
        helper.succeed();
    }

    /**
     * A double copycat slab drops two slabs through its (26.3-converted) loot table.
     */
    private static void slabLoot(GameTestHelper helper) {
        floor(helper);
        BlockPos pos = new BlockPos(4, 1, 2);
        BlockState state = CCBlocks.COPYCAT_SLAB.get().defaultBlockState();
        if (state.hasProperty(SlabBlock.TYPE)) {
            state = state.setValue(SlabBlock.TYPE, SlabType.DOUBLE);
        }
        helper.setBlock(pos, state);
        BlockPos abs = helper.absolutePos(pos);
        List<ItemStack> drops = Block.getDrops(helper.getLevel().getBlockState(abs), helper.getLevel(), abs, helper.getLevel().getBlockEntity(abs));
        int slabs = drops.stream().filter(stack -> stack.is(CCBlocks.COPYCAT_SLAB.get().asItem())).mapToInt(ItemStack::getCount).sum();
        helper.assertTrue(slabs == 2, "double copycat_slab should drop 2 slabs, got " + drops);
        Copycats.LOGGER.info("copycats:slab_loot passed: {}", drops);
        helper.succeed();
    }

    /**
     * A creative motor turns a Create shaft through a copycat shaft.
     */
    private static void shaftTransmits(GameTestHelper helper) {
        floor(helper);
        BlockPos motor = new BlockPos(2, 1, 2);
        BlockPos copycatShaft = new BlockPos(3, 1, 2);
        BlockPos shaft = new BlockPos(4, 1, 2);
        helper.setBlock(motor, AllBlocks.CREATIVE_MOTOR.defaultBlockState().setValue(DirectionalKineticBlock.FACING, Direction.EAST));
        BlockState copycat = CCBlocks.COPYCAT_SHAFT.get().defaultBlockState();
        if (copycat.hasProperty(RotatedPillarBlock.AXIS)) {
            copycat = copycat.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
        }
        helper.setBlock(copycatShaft, copycat);
        helper.setBlock(shaft, AllBlocks.SHAFT.defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X));
        helper.succeedWhen(() -> {
            float copycatSpeed = helper.getBlockEntity(copycatShaft, KineticBlockEntity.class).getSpeed();
            float shaftSpeed = helper.getBlockEntity(shaft, KineticBlockEntity.class).getSpeed();
            String state = "copycat shaft " + copycatSpeed + " rpm, shaft " + shaftSpeed + " rpm";
            helper.assertTrue(copycatSpeed != 0 && shaftSpeed != 0, state);
            Copycats.LOGGER.info("copycats:shaft_transmits passed at tick {}: {}", helper.getTick(), state);
        });
    }
}
