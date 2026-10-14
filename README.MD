<div align="center">

<img src="reconstructed/src/main/resources/copycats_icon_bordered.png" width="104" alt="Copycats+ icon" />

# Copycats+ · Create Fly Port

**More shapes. Your materials. Built for Fabric.**

An unofficial port of **Create: Copycats+ 3.0.7** for **Create Fly**.

[![Fabric](https://img.shields.io/badge/loader-Fabric-dbd4c5?style=flat-square)](#installation)
[![Minecraft](https://img.shields.io/badge/Minecraft-26.1–26.2-62b47a?style=flat-square)](#compatibility)
[![Java](https://img.shields.io/badge/Java-25-e76f00?style=flat-square)](#development)
[![Compatibility checks](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/actions/workflows/compatibility.yml/badge.svg)](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/actions/workflows/compatibility.yml)

[**CurseForge**](https://www.curseforge.com/minecraft/mc-mods/createfly-copycats-port) ·
[**Modrinth**](https://modrinth.com/mod/copycats+-createfly-port) ·
[**Report a bug**](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=bug_report.yml) ·
[**Suggest an improvement**](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=enhancement.yml)

</div>

> [!NOTE]
> Maintained by **lucahTheCreator**, with permission from the Copycats+ team. This is an unofficial port. Please report port issues here so the original Copycats+ team can focus on their releases.

## Build with the blocks you want

Use copied materials on boards, slabs, stairs, panes, layers, slopes, doors, shafts, cogwheels, and more. Multi-material blocks let you customize individual parts; functional blocks fit into Create machines and contraptions.

- **Flexible shapes** — choose small details, partial blocks, and larger building pieces.
- **Copied materials** — give your builds the texture of the blocks you already use.
- **Create integration** — use functional copycats alongside your machines and moving builds.
- **Connected textures** — connect compatible copied materials, including framed glass.

With ColorLight installed, this port unwraps its model hook to preserve connected textures. ColorLight-specific tinting or lighting can look different on copycat surfaces.

## What's new in v1.16

**Fixed the placement crash with Create Fly 26.2-6.0.9-2 beta.** The renderer supports both Create Fly's newer single-normal API and its older four-normal API. The shared renderer now compiles from source, and builds check the normal adapter against the real Create Fly dependency.

Development launchers are included for the new beta and the earlier 26.2 build. See the [changelog](CHANGELOG.md) for the patch details.

This is the current **source build**. Check the version on CurseForge or Modrinth before downloading; those sites can still list an earlier published file.

## Compatibility

Choose the file for your **Minecraft version**, then use the matching Create Fly build.

| Minecraft | v1.16 file label | Create Fly checked with this patch |
| --- | --- | --- |
| **26.1 / 26.1.1 / 26.1.2** | `Copycats+-Fabric-26.1+-v1.16.jar` | `26.1.2-6.0.9-4` — build and normal API checks |
| **26.2** | `Copycats+-Fabric-26.2-v1.16.jar` | `26.2-rc-2-6.0.9-1` and `26.2-6.0.9-2` beta — build, normal API checks, and dev client startup |

The **26.1+** file covers the 26.1 release line through 26.1.2. It is separate from the 26.2 file. The universal file is built against 26.1.2; this patch has not been individually retested on 26.1 and 26.1.1.

The loader currently accepts Create Fly `>=6.0.9-1 <6.0.10`. That range is a loading rule; the table records the versions actually checked. Full gameplay testing should include copied materials, slopes, doors, cogwheels, and contraptions.

> [!IMPORTANT]
> **26.3 and 1.21.11 are not supported by this release.** Future version support is planned, and will be listed after a matching port is built and tested. Do not select a newer Minecraft version just because the loader accepts it.

## Installation

1. Install **Fabric Loader 0.19.2 or newer** and **Java 25** for your chosen Minecraft version.
2. Install **Create Fly** and the matching **Fabric API**.
3. Download the Copycats+ file for that same Minecraft version from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/createfly-copycats-port/files) or [Modrinth](https://modrinth.com/mod/copycats+-createfly-port/versions).
4. Put the JAR in your instance's `mods` folder. Replace the previous Copycats+ JAR when updating.
5. Start the game and try your blocks in a creative test world.

For multiplayer, install matching required mods on the client and server. Install optional rendering mods using versions made for your Minecraft release.

## Help and feedback

Use the form that matches your report:

| Report | Include |
| --- | --- |
| [Bug or crash](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=bug_report.yml) | Exact mod/Create Fly versions, reproduction steps, and crash report or log |
| [Enhancement or feature](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=enhancement.yml) | The problem you want to solve and the behavior you would like |
| [Mod compatibility](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=compatibility.yml) | Both mods' versions, affected blocks/materials, and the smallest setup that reproduces it |
| [Question or installation help](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/new?template=question.yml) | What you tried and the part you need help with |

Search [existing issues](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues) first. For visuals, a screenshot of the block and its copied material helps. Attach `latest.log` or the crash report when relevant.

## Development

Use a **Java 25 JDK**. The Gradle wrapper is included.

```bash
# Build the universal 26.1 line
./gradlew build -Ptarget_version=26.1.2

# Build the 26.2 line against the current Create Fly beta
./gradlew build -Ptarget_version=26.2

# Check the older 26.2 Create Fly API
./gradlew verifyNormalsCompatibility -Ptarget_version=26.2 -Pcreate_fly_version=26.2-rc-2-6.0.9-1
```

On Windows, use `gradlew.bat` instead of `./gradlew`. Output JARs are in `build/libs`. `scripts/build-supported.ps1` builds both Minecraft targets into `dist`.

### Play in a development client

| Windows launcher | Client |
| --- | --- |
| **`Test Copycats.cmd`** | Minecraft 26.2 + Create Fly 6.0.9-2 beta |
| **`Test Copycats - Older Create Fly.cmd`** | Minecraft 26.2 + Create Fly 6.0.9-1 |

Set `JAVA_HOME` to your Java 25 JDK. The launchers also recognize the local runtime at `%USERPROFILE%\.cache\copycats-dev\jdk-25.0.4.1+1` if it is installed. Each client has a separate `run-dev-*` directory for worlds, settings, logs, and extra mods.

For the 26.1.2 client:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/run-dev-client.ps1 -Minecraft 26.1.2
```

The `verifyNormalsCompatibility` task runs as part of `check`. It verifies API linkage; it does not replace testing block placement and appearance in the game.

### Rendering diagnostics

After the first launch, edit `config/copycats-debug.properties` in the test instance. Diagnostics reload while the game is running.

- `enabled=true` enables every category; `limit` caps output per category.
- Individual categories include `ct`, `door`, `material`, `shape`, `filter`, and `face_hiding`.
- Search the log for `[Copycats Debug/` or `Create Fly normals compatibility:`.

### Source layout

`patches/src/main/java` contains the active compatibility patches. Selected files in `reconstructed/src/main/java` are also compiled. `reconstructed/working.jar` supplies the remaining precompiled classes; changing an unselected reconstructed Java file alone will not update the release. Build overrides remove replaced classes from that overlay.

## Credits and permission

Original **Copycats+**: Lysine, Bennyboy1695, Redcat_XVIII, and the Copycats+ team. **Create Fly**: ZurrTum and contributors. Port maintenance: **lucahTheCreator**.

[Original Copycats+](https://github.com/copycats-plus/copycats) · [Create Fly](https://github.com/ZurrTum/Create-Fly) · [License](LICENSE)

<details>
<summary><strong>Permission to publish this unofficial port</strong></summary>

Bennyboy1695 granted permission on behalf of the Copycats+ team under these conditions:

- The source code must remain visible on GitHub.
- This port should be archived or deprecated when Copycats+ officially supports the corresponding version.

![Permission from the Copycats+ team](docs/permission.png)

Original Copycats+ code, assets, and features belong to their developers. This project follows the original license and redistribution requirements.

</details>
