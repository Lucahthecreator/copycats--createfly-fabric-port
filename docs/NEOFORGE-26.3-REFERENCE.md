# Archived reference: Copycats+ (Create Fly port) → NeoForge 26.3

Historical notes from work written 2026-10-18, checked against MC 26.3, NeoForge 26.3.0.89, Java 25.0.1, and the NeoForge Create Fly port
`create-fly-neoforge-6.0.9+neoforge-26.3` (ZP5 `mods/create-fly-neoforge`, branch `neoforge-26.3`, `8da5b8c6`).

This repository retains the NeoForge port's source and data under `neoforge-26.3/` for possible future reference.
Its build configuration and launch tools have been removed, so this checkout cannot build or launch that port. The
community source branch is available at [Zoompack 5's NeoForge 26.3 port](https://github.com/zoom-packers/copycats-createfly/tree/zp5/26.3).

## License

All Rights Reserved. Both upstream Copycats+ ([copycats-plus/copycats](https://github.com/copycats-plus/copycats),
`LICENSE`: "All rights reserved") and the Create Fly port (same `LICENSE`, Modrinth license
`LicenseRef-All-Rights-Reserved`) reserve all rights. The Create Fly port is published with the Copycats+ team's
permission (`docs/permission.png`). The permission was given to that port's author, on condition that the source is
public on GitHub and that the port is archived once Copycats+ officially supports that version.

Permission for public distribution of this adaptation has been confirmed by the port maintainer. Keep the source public
on GitHub and archive or deprecate the port once Copycats+ officially supports the corresponding version. This
permission applies to this adaptation; other public forks or releases should obtain their own permission.

## Route: native NeoForge, not Connector

| | Archived native port source | Fabric jar under Sinytra Connector |
|---|---|---|
| Linkage | Compiles against MC 26.3, NeoForge and the Create port | The 26.2 jar resolves every `com.zurrtum.create` symbol in our Create port. It misses `PoseStack.mulPose(Quaternionfc)`, `BlockItem.updateCustomBlockEntityTag` (instance) and `Inventory.placeItemBackInInventory(ItemStack)` (16 call sites; the link check script missed this one, javac found it) |
| Fabric API surface | Replaced: entrypoints, creative tabs, server/client lifecycle, `BlockEntityRendererRegistry`, FRAPI `emitQuads`, Fabric Loader | 8 classes on FFAPI |
| Mixins | 18 checked statically, 9 server mixins applied at runtime | 2 would break on NeoForge's patches (light emission, `shouldRenderFace`), plus Indigo/Continuity/ColorLight mixins for Fabric-only mods |
| Data | 44 loot tables converted to 26.3 | Same conversion needed: a 26.2 jar's loot tables fail on 26.3 under Connector too |
| Pack cost | One jar next to the Create port | Connector, FFAPI and Launchpad, three unofficial local builds that are not in the pack, just for one addon |

Connector would have needed jar edits for the three API breaks, two retargeted mixins and the data conversion. That
is more than trivial, and it is the same work as the native port, so the native route was taken.

## Where the source comes from

The upstream repository is not a full source tree. Its build compiles about 20 hand-written classes
(`patches/src/main/java`, plus 12 files in `reconstructed/`) on top of a binary `reconstructed/working.jar`. Most of
`reconstructed/src` is CFR output that the build never compiles. So this port starts from what actually ships:

1. The released `Copycats+-Fabric-26.2-v1.16.jar` (Modrinth version `joM20Aon`, sha512 `dbd51ee9490e7f51…`) was
   decompiled with Vineflower 1.11.2 (`-dgs=1 -rsy=1 -rbr=1`, with MC 26.3, the Create port and FFAPI as libraries).
2. The 41 hand-written files in `patches/src/main/java` replaced their decompiled versions. The jar-time relocation
   was applied to them (`com.tterrag.registrate` → `com.copycatsplus.copycats.internal.registrate`,
   `com.zurrtum.create.foundation.data` and `com.zurrtum.create.AllTags` → `com.copycatsplus.copycats.internal.create`).
   The Create Fly port bundles these small compatibility copies, and they stay private.
3. Resources (assets, data, icon) come from the released jar.

The decompiled code needed four kinds of fixes: `instanceof var x` patterns (3, restored to `ICopycatBlockEntity`),
a generic cast in `AbstractRegistrate#getAll`, the shaft placement predicate (Guava `Predicates.or` array), and a
renamed loop variable in `CopycatPaneModelCore`.

## Changes for NeoForge

| Upstream (Fabric) | Here |
|---|---|
| `fabric.CopycatsImpl` (`main` entrypoint) | `neoforge.CopycatsNeoForge` (`@Mod`). Registration runs from the first `RegisterEvent` (blocks), because NeoForge rejects `Registry.register` outside the event phase. Creative tabs are registered in the `CREATIVE_MODE_TAB` event |
| `ServerLifecycleEvents.SERVER_STARTING` | `ServerAboutToStartEvent` |
| `fabric.CopycatsClientImpl` (`client` entrypoint) | `neoforge.CopycatsNeoForgeClient` (`@Mod(dist = CLIENT)`). Copycat model factories go into Create's `AllModels` from the `BLOCK_ENTITY_TYPE` `RegisterEvent`, before the first model bake |
| `BlockEntityRendererRegistry` | `EntityRenderersEvent.RegisterRenderers` |
| `ClientLifecycleEvents.CLIENT_STARTED` → `BlockColors#register` | `RegisterColorHandlersEvent.BlockTintSources` |
| `FabricLoader` (environment, config dir) | `FMLEnvironment.getDist()`, `FMLPaths.CONFIGDIR`. `Platform.CURRENT` is the new `NEOFORGE`, so convention tags use `c:` |
| `copycats.accesswidener` | `META-INF/accesstransformer.cfg` |
| `CreateFlyCopycatModel#emitQuads` (FRAPI `QuadEmitter`) | Removed. NeoForge and Sodium both render through `addPartsWithInfo`, which is how the Create port renders its own copycats |
| `FabricBlockStateModelWrapperAccessor` (FAPI `WrapperBlockStateModel`) | `DelegateBlockStateModelAccessor` (NeoForge's `DelegateBlockStateModel`) |
| `ContinuityCopycatModelMixin`, `ColorLightTintedBakedModelAccessor`, `IndigoBlockRendererMixin` | Removed. Continuity, ColorLight and Indigo are Fabric-only |
| `copycats-common.mixins.json`, `copycats-fabric.mixins.json`, `SawBlockEntityParticleMixin`, `BlockStateBaseCacheMixin` | Removed. They were unused leftovers: the configs were not listed in `fabric.mod.json`, and the common one named a plugin class that doesn't exist |
| `CopycatBlockLightEngineMixin` redirected `BlockState#getLightEmission()` | Retargeted to `getLightEmission(BlockGetter, BlockPos)`, the NeoForge overload `BlockLightEngine#getEmission` calls. Its result is the base, so Create's own copycat hook still counts |
| `ModelBlockRendererMixin` hooked `shouldRenderFace(level, state, face, neighborPos)` | Hooks NeoForge's `shouldRenderFace(level, pos, state, face, neighborPos)`, the only overload `ModelBlockRenderer` (and the Create port's overwrite) still calls |

### Minecraft 26.2 → 26.3

| Change | Fix |
|---|---|
| `BlockItem#updateCustomBlockEntityTag(pos, level, player, stack, state)` is now a static helper with no instance hook | `CopycatBoxItem` and `CopycatCatwalkItem` set the board sides in `placeBlock(context, state)` before placement, instead of re-setting the block afterwards |
| `PoseStack#mulPose(Quaternionfc)` is gone | `PoseStack#rotate(Quaternionfc)` (sliding door renderer and visual) |
| `Inventory#placeItemBackInInventory(ItemStack)` takes a `Prediction` | `Prediction.SERVER_ONLY`, as Create does (16 call sites) |
| `BlockPos(Vec3i)` constructor is gone | `BlockPos.ZERO.offset(vec)` |
| Loot table format | `create-fly-neoforge/tools/data_26_3.py` converted all 44 loot tables (conditions → `condition`, `functions` → `modifier`, `block_state_property` → `match_block`). Its lint finds 0 leftover 26.2 constructs. Recipes and models needed no changes |

## Historical verification record

- **Build.** `./gradlew build` compiles all 233 source files with no errors.
- **Static mixin check.** The Create port's `tools/check_mixins.py`, adapted for package-qualified config entries,
  reports 0 problems for all 18 mixins. It checked against the patched 26.3 jar, NeoForge, the Create port, Sodium
  0.9.3 for NeoForge, and this jar.
- **Link check.** Every Minecraft, NeoForge and Create reference in the jar resolves.
- **Game-test server** (`runGameTestServer`, `-PmixinDebug`). The world loads, all 9 common mixins apply, there are no
  data-pack, recipe or loot errors, and all 6 required tests pass:

| Test | Checks |
|---|---|
| `copycats:registry` | 46 blocks, 45 items and 8 block entity types under `copycats:`, including slab, board, stairs, shaft, cogwheel, sliding door and fluid pipe |
| `copycats:apply_material` | A survival mock player right-clicks a `copycat_block` with oak planks. The block takes oak planks as its material and one plank is used up |
| `copycats:slab_loot` | A double `copycat_slab` drops 2 slabs through its converted loot table |
| `copycats:shaft_transmits` | A creative motor drives a Create shaft through a `copycat_shaft`: both turn at 16 rpm |
| `create:water_wheel_drives_press` | The Create port's own test still passes with Copycats loaded |

## Not verified

- **Client.** Not launched (it opens a game window, so it needs the user's OK). Still untested: copycat models and
  materials in the world, connected textures, tints, the sliding/folding door and kinetic block entity renderers, the
  Flywheel cogwheel visual, the 9 client mixins at runtime, Sodium's face hiding, and creative tabs. The Create port's
  `runClientSmoke` harness is the natural place for this check.
- Contraptions carrying copycats, doors on trains, and fluid pipes.
- A full ZP5 pack boot with this jar.
