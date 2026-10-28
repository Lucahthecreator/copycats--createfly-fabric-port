
<div align="center">

<img src="reconstructed/src/main/resources/copycats_icon_bordered.png" width="104" alt="Copycats+ icon" />

# Copycats+ · Create Fly Port

**More shapes. Your materials. Multiple Minecraft versions.**

An unofficial port of **Create: Copycats+** for **Create Fly**. Supported versions, loaders, dependencies, and build paths are listed in the [compatibility guide](docs/COMPATIBILITY.md).

[![Loader](https://img.shields.io/badge/Loader-Fabric-dbd4c5?style=flat-square)](docs/COMPATIBILITY.md)
[![Minecraft](https://img.shields.io/badge/Minecraft-multiple%20targets-62b47a?style=flat-square)](docs/COMPATIBILITY.md)
[![Java](https://img.shields.io/badge/Java-see%20compatibility%20guide-e76f00?style=flat-square)](docs/COMPATIBILITY.md)
[![Compatibility checks](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/actions/workflows/compatibility.yml/badge.svg)](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/actions/workflows/compatibility.yml)

[**CurseForge**](https://www.curseforge.com/minecraft/mc-mods/createfly-copycats-port) ·
[**Modrinth**](https://modrinth.com/mod/copycats+-createfly-port) ·
[**Report a Bug**](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=bug_report.yml) ·
[**Suggest an Improvement**](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=enhancement.yml)

</div>

> [!NOTE]
> Maintained by **lucahTheCreator**, with permission from the original Copycats+ team.
> This is an unofficial port and is not maintained by the original developers.

## Build with the blocks you want

Use copied materials on boards, slabs, stairs, panes, layers, slopes, doors, shafts, cogwheels, and more.

Multi-material blocks let you customize individual parts, while functional blocks integrate with Create machines and contraptions.

- **Flexible shapes** — Build using partial blocks, detailed shapes, and larger pieces.
- **Copied materials** — Apply existing Minecraft block textures to copycat shapes.
- **Create integration** — Use functional copycats with machines and moving contraptions.
- **Connected textures** — Support compatible connected materials, including framed glass.

With ColorLight installed, this port unwraps its model hook to preserve connected textures. ColorLight-specific tinting or lighting may appear differently on copycat surfaces.

## Latest changes

See the [changelog](CHANGELOG.md) for changes by release and target.

> [!IMPORTANT]
> This is the current source version. CurseForge and Modrinth may have an earlier published build.

## Compatibility and downloads

The [compatibility guide](docs/COMPATIBILITY.md) is the single list of Minecraft targets, loaders, support status, required dependencies, artifact locations, and build instructions. Use the Windows build menu to see the targets configured for this checkout, or follow the guide to build from a terminal. Add new targets and backports to that matrix and their build tooling; the general installation and development guidance here applies across targets.

### Current targets

| Minecraft | Loader | Copycats+ status | Java |
| :--- | :--- | :--- | :---: |
| **1.21.10** | Fabric | Beta · v1.18 · Create Fly 6.0.8-6 and 6.0.9-2 startup tested | 21 |
| **1.21.11** | Fabric | Beta · v1.18 · Create Fly 6.0.9-5 startup tested | 21 |
| **26.1** (tested on 26.1.2) | Fabric | Supported · v1.18 | 25 |
| **26.2** | Fabric | Supported · v1.18 | 25 |

See the [full compatibility matrix](docs/COMPATIBILITY.md) for exact loader and Create Fly versions, build artifacts, and target-specific instructions.

Download published builds from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/createfly-copycats-port/files) or [Modrinth](https://modrinth.com/mod/copycats+-createfly-port/versions). Targets that are still in development may only be available as source builds; follow their module instructions in the compatibility guide.

> [!WARNING]
> Development and community ports may have different support and release status.
>
> Current build targets in this repository use Fabric.

## Port modules and community work

The repository contains the main Fabric project and may include backports for other Minecraft versions. Historical work on other loaders is kept as reference documentation in the [compatibility guide](docs/COMPATIBILITY.md).

The NeoForge 26.3 community port is linked in the compatibility guide as a reference; this repository no longer includes its build module.

## Installation

1. Choose a target and loader listed in the [compatibility guide](docs/COMPATIBILITY.md).
2. Install the Java version, loader, APIs, and Create Fly build listed for that target.
3. Download a published Copycats+ build from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/createfly-copycats-port/files) or [Modrinth](https://modrinth.com/mod/copycats+-createfly-port/versions). For source-only targets, build the matching module using its documented instructions.
4. Place the JAR in that Minecraft instance's `mods` folder.
5. Remove older Copycats+ JARs from the same instance before updating.
6. Launch Minecraft and test in a creative world. For multiplayer, install matching required mods on both the client and server.

## Help and feedback

| Report type | Link |
| :--- | :--- |
| Bug or crash | [Report a bug](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=bug_report.yml) |
| Feature request | [Suggest an improvement](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=enhancement.yml) |
| Mod compatibility | [Report compatibility issue](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=compatibility.yml) |
| Installation help | [Ask a question](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=question.yml) |

Before reporting an issue, search [existing issues](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues).

Include Minecraft version, loader version, Create Fly version, reproduction steps, and logs when possible.

## Development

A JDK version supported by the target is required. See the [compatibility guide](docs/COMPATIBILITY.md) for each target's Java requirement.

```bash
# Build the default target configured in gradle.properties
./gradlew build

# Select a different root-project target
./gradlew build -Ptarget_version=YOUR_TARGET

# Backports and alternate loaders use their module-specific instructions
# See docs/COMPATIBILITY.md for current module paths
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

Compiled JAR files are located in `build/libs`.

### Windows build and test menu

Double-click **`Copycats Build and Test.bat`** to choose from the build and test actions configured for this checkout. Build artifacts are saved to the location shown by the menu. For automation, use the build scripts and the target-specific commands in the [compatibility guide](docs/COMPATIBILITY.md). Each development launcher documents its required JDK and dependencies alongside its module.

Development-only utilities, including the `/copycats_test` gallery where available, are not included in normal mod installations.

### Rendering diagnostics

After the first launch, edit:

`config/copycats-debug.properties`

Available options include:

- `enabled=true` — Enable all diagnostic categories.
- `limit` — Limit log output per category.
- `ct` — Connected textures.
- `door` — Door rendering.
- `material` — Copied material handling.
- `shape` — Shape rendering.
- `filter` — Rendering filters.
- `face_hiding` — Face visibility.

Search logs for `[Copycats Debug/` or `Create Fly normals compatibility:`.

### Source layout

- `patches/src/main/java` — Active compatibility patches for the main project.
- `reconstructed/src/main/java` — Reconstructed source files for the main project.
- `backport-*` — Separate Fabric targets with their own build files and notes.
- `neoforge-26.3/src` — Archived NeoForge source and data retained for reference; no build configuration is included.
- `scripts/` — Build, test, and development-client helpers.

Not all reconstructed Java files are automatically compiled into releases. Changes to unselected files may not affect the final JAR.

## Credits and permission

| Project | Credits |
| :--- | :--- |
| **Original Copycats+** | Lysine, Bennyboy1695, Redcat_XVIII, and the Copycats+ team |
| **Create Fly** | ZurrTum and contributors |
| **Fabric port** | lucahTheCreator |

[Original Copycats+](https://github.com/copycats-plus/copycats) ·
[Create Fly](https://github.com/ZurrTum/Create-Fly) ·
[License](LICENSE)

<details>
<summary><strong>Permission to publish this unofficial port</strong></summary>

Bennyboy1695 granted permission on behalf of the Copycats+ team under these conditions:

- The source code must remain visible on GitHub.
- This port should be archived or deprecated when Copycats+ officially supports the corresponding version.

![Permission from the Copycats+ team](docs/permission.png)

Original Copycats+ code, assets, and features belong to their respective developers.

Permission to use code contributed by lucahTheCreator does not automatically grant redistribution rights to the original Copycats+ code.

Additional public forks or adaptations should obtain the necessary permission from the original Copycats+ team.

</details>

---

<div align="center">

**Maintained by [lucahTheCreator](https://github.com/Lucahthecreator)**

*Unofficial Copycats+ port for Create Fly.*

**Unofficial Fabric port · See the compatibility guide for current targets**

</div>
