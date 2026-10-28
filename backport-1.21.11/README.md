# Minecraft 1.21.11 beta backport

This project builds Copycats+ against Minecraft 1.21.11, Fabric Loader 0.19.3,
Fabric API 0.141.6, and Create Fly 6.0.9-5. It compiles for Java 21.
The launcher selects the installed Eclipse Temurin JDK 21.

The backport is maintained alongside the 26.x Fabric targets on the same Git
branch. From the repository root, `Copycats Build and Test.bat` lets you build
or launch a client for each target version.

Double-click `run-test-client.bat` to launch the isolated development client.
The world, options, and logs are stored under `run-1.21.11/`. Close an existing
test client before starting another one.

The installable jar is produced by running `gradlew.bat -p backport-1.21.11 build`
from the repository root; find it under `build/libs/` in this folder. The current
beta fix candidate is `Copycats-CreateFly-1.21.11-v1.18.jar`.

The previous beta.1 release was archived while investigating startup crashes
reported with larger modpacks. Issues [#10](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/10)
and [#11](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/11)
show the same `CreateRegistrate.create(String)` linkage failure during Steam 'n'
Rails initialization. This candidate relocates Copycats' bundled Create and
Registrate compatibility classes so they do not shadow APIs used by other mods.
Test it with Steam 'n' Rails and other Create addons before publishing another
release.

In the development client, run `/copycats_test` to place every public Copycats
block near your position. A barrel beside the gallery contains the box and
catwalk items and a few copied-material samples. The command chooses empty
space and does not register in normal mod installations.

## In-game checks

Confirmed in the Java 21 client: the full copycat block renders copied grass
correctly, an unfilled copycat block shows the transparent base texture, and a
glass copycat button shows its glass border. Gameplay testing continues for the
remaining blocks and materials.

- Place a basic copycat block, slab, stair, board, pane, ladder, and slope.
- Apply several materials, including a tinted block and glass, then wrench them off.
- Check connected textures with two matching copycats side by side.
- Place and use a copycat door, trapdoor, and pressure plate.
- Connect a copycat shaft and both cogwheels to Create power, then observe rotation.
- Place a copycat fluid pipe with a Create fluid setup.
- Save and reopen the world to check that copycat materials persist.

If a test fails, note the block, material, action, and whether it happens again.
Crash reports are saved in `run-1.21.11/crash-reports/`; the ongoing game log is
`run-1.21.11/logs/latest.log`.

This is a beta backport. In-game checks are still in progress.
