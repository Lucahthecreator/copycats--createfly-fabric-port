
<div align="center">

<img src="reconstructed/src/main/resources/copycats_icon_bordered.png" width="104" alt="Copycats+ icon" />

# Copycats+ · Create Fly Port

**More shapes. Your materials. Fabric & NeoForge.**

An unofficial port of **Create: Copycats+ 3.0.7** for **Create Fly**.

[![Fabric](https://img.shields.io/badge/Loader-Fabric-dbd4c5?style=flat-square)](#compatibility)
[![NeoForge](https://img.shields.io/badge/NeoForge-Coming_Soon-orange?style=flat-square)](#upcoming-support)
[![Minecraft](https://img.shields.io/badge/Minecraft-26.1–26.3-62b47a?style=flat-square)](#compatibility)
[![Java](https://img.shields.io/badge/Java-25-e76f00?style=flat-square)](#development)
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

## What's new in v1.16

**Fixed the placement crash with Create Fly 26.2-6.0.9-2 beta.**

The renderer now supports both Create Fly's newer single-normal API and its older four-normal API.

Additional improvements:

- Shared renderer compiles directly from source.
- Normal adapter compatibility is checked against real Create Fly dependencies.
- Development launchers support both newer and older Create Fly versions.
- Improved compatibility checks during builds.

See the [changelog](CHANGELOG.md) for more information.

> [!IMPORTANT]
> This is the current source version. CurseForge and Modrinth may have an earlier published build.

## Compatibility

| Minecraft | Fabric | NeoForge |
| :--- | :---: | :---: |
| **26.3** | Planned | Planned |
| **26.2** | Supported (v1.16) | Planned |
| **26.1.2** | Supported (v1.16) | Planned |
| **26.1.1** | Supported (v1.16) | Planned |
| **26.1** | Supported (v1.16) | Planned |
| **1.21.11** | Under consideration | Under consideration |

### Currently supported builds

| Minecraft | File | Tested Create Fly version |
| :--- | :--- | :--- |
| 26.2 | `Copycats+-Fabric-26.2-v1.16.jar` | `26.2-rc-2-6.0.9-1` / `26.2-6.0.9-2` beta |
| 26.1–26.1.2 | `Copycats+-Fabric-26.1+-v1.16.jar` | `26.1.2-6.0.9-4` |

The 26.1+ file supports the 26.1 release line through 26.1.2, although 26.1 and 26.1.1 have not been individually retested with this patch.

> [!WARNING]
> **NeoForge builds are not officially available yet.**
>
> Fabric and NeoForge require separate builds. Do not attempt to load a Fabric JAR using NeoForge.

## Upcoming support

Work is planned to expand support across both Fabric and NeoForge.

| Version | Planned development |
| :--- | :--- |
| **26.3 Fabric** | New Minecraft version support |
| **26.3 NeoForge** | NeoForge adaptation |
| **26.2 NeoForge** | NeoForge adaptation |
| **26.1 NeoForge** | NeoForge adaptation |
| **1.21.11** | Possible future backport |

Release dates have not been confirmed. Support depends on compatible Create Fly builds, successful compilation, and gameplay testing.

### Community NeoForge development

The **Zoompack 5 team** has developed a community adaptation for Minecraft 26.3 using NeoForge.

Their work includes:

- A NeoForge 26.3 port based on this project's source.
- Compatibility work for Create Fly on NeoForge.
- Build and static compatibility checks.
- Dedicated server and client startup testing.

**Community resources:**

- [NeoForge 26.3 source branch](https://github.com/zoom-packers/copycats-createfly/tree/zp5/26.3)
- [Porting documentation](https://github.com/zoom-packers/copycats-createfly/blob/zp5/26.3/neoforge-26.3/PORTING.md)

This is a separate community adaptation and is not an official release maintained by lucahTheCreator.

The Zoompack 5 team has been advised to request permission from the original Copycats+ developers before publicly redistributing their adaptation.

## Installation

1. Install **Fabric Loader 0.19.2 or newer** and **Java 25**.
2. Install **Create Fly** and the matching **Fabric API**.
3. Download the correct Copycats+ version from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/createfly-copycats-port/files) or [Modrinth](https://modrinth.com/mod/copycats+-createfly-port/versions).
4. Place the downloaded JAR into your Minecraft instance's `mods` folder.
5. Remove any previous Copycats+ JAR before updating.
6. Launch Minecraft and test the blocks in a creative world.

For multiplayer, install matching required mods on both the client and dedicated server.

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

A **Java 25 JDK** is required.

```bash
# Build Minecraft 26.1.2
./gradlew build -Ptarget_version=26.1.2

# Build Minecraft 26.2
./gradlew build -Ptarget_version=26.2

# Verify compatibility with the older Create Fly API
./gradlew verifyNormalsCompatibility -Ptarget_version=26.2 -Pcreate_fly_version=26.2-rc-2-6.0.9-1
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

Compiled JAR files are located in `build/libs`.

The script `scripts/build-supported.ps1` builds both currently supported Minecraft targets into `dist`.

### Development clients

| Launcher | Minecraft / Create Fly |
| :--- | :--- |
| `Test Copycats.cmd` | 26.2 / Create Fly 6.0.9-2 beta |
| `Test Copycats - Older Create Fly.cmd` | 26.2 / Create Fly 6.0.9-1 |

Set `JAVA_HOME` to your Java 25 JDK.

For Minecraft 26.1.2:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/run-dev-client.ps1 -Minecraft 26.1.2
```

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

- `patches/src/main/java` — Active compatibility patches.
- `reconstructed/src/main/java` — Reconstructed source files.
- `reconstructed/working.jar` — Remaining precompiled classes.

Not all reconstructed Java files are automatically compiled into releases. Changes to unselected files may not affect the final JAR.

## Credits and permission

| Project | Credits |
| :--- | :--- |
| **Original Copycats+** | Lysine, Bennyboy1695, Redcat_XVIII, and the Copycats+ team |
| **Create Fly** | ZurrTum and contributors |
| **Fabric port** | lucahTheCreator |
| **Future NeoForge port** | lucahTheCreator (planned) |
| **Community NeoForge adaptation** | Zoompack 5 team |

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

**Fabric available now · NeoForge planned · More versions coming**

</div>
