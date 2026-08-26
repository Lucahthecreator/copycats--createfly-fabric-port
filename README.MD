<div align="center">

# Create: Copycats+ — CreateFly Fabric Port

An unofficial, Fabric-only port of **Create: Copycats+ 3.0.7** for **CreateFly**.

[![Minecraft 26.1+](https://img.shields.io/badge/Minecraft-26.1%2B-62b47a?style=flat-square)](#supported-versions)
[![Minecraft 26.2](https://img.shields.io/badge/Minecraft-26.2-62b47a?style=flat-square)](#supported-versions)
[![Future versions](https://img.shields.io/badge/Future_versions-Planned-4c8bf5?style=flat-square)](#future-version-support)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-d8c7a3?style=flat-square)](https://fabricmc.net/)
[![Java 25](https://img.shields.io/badge/Java-25-e76f00?style=flat-square)](#requirements)
[![GitHub](https://img.shields.io/badge/GitHub-Source-24292e?style=flat-square)](https://github.com/Lucahthecreator/copycats--createfly-fabric-port)

[Modrinth](https://modrinth.com/mod/copycats+-createfly-port) ·
[CurseForge](https://www.curseforge.com/minecraft/mc-mods/copycats-createfly-port) ·
[Source](https://github.com/Lucahthecreator/copycats--createfly-fabric-port) ·
[Report an issue](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues)

</div>

> [!IMPORTANT]
> This is not an official Copycats+ release. It is not maintained or supported by the official Copycats+ team. Support for this port is handled separately by **lucahTheCreator**.

## Overview

This port rebuilds the general Copycats+ feature set for CreateFly on Fabric. It includes:

- A Fabric-only build and release setup
- The Java changes required by the supported CreateFly versions
- Copycat blocks, slabs, stairs, panes, doors, layers, boards, and more
- CreateFly compatibility for the Fabric loader
- Connected-texture compatibility with ColorLight

When ColorLight is installed, the port preserves connected textures by unwrapping its model hook for Copycat materials. ColorLight-specific tinting or lighting may therefore look slightly different on Copycat surfaces than on regular blocks.

## Supported versions

These are the newest Minecraft and CreateFly versions currently supported by the port:

| Release | JAR label | Minecraft support | Newest supported CreateFly build | Accepted CreateFly range |
| --- | --- | --- | --- | --- |
| Current 26.1 line | `mc.26.1+` | Minecraft `26.1` through `26.1.2` | CreateFly 6.0.9 build 4 for 26.1.2 | `>=6.0.9-1 <6.0.10` |
| Current newest line | `mc.26.2` | Minecraft `26.2` | CreateFly 6.0.9 build 1 for 26.2 | `>=6.0.9-1 <6.0.10` |

The `mc.26.1+` name indicates support for the compatible 26.1 release line, not only Minecraft 26.1.2. It does not include Minecraft 26.2; use the `mc.26.2` JAR for that line.

### Future version support

This port is intended to continue supporting future stable Minecraft versions and newer CreateFly releases. A future version will be listed as supported after:

- CreateFly publishes a compatible build for that Minecraft version.
- The port compiles successfully against the new versions.
- Core blocks, connected textures, contraptions, and known mod compatibility are tested.
- A separate matching JAR is published on Modrinth and CurseForge.

Newer Minecraft or CreateFly versions should not be assumed compatible until they appear in the table above. This avoids worlds being opened with an untested build while keeping the project committed to future version updates.

## Requirements

- [Fabric Loader](https://fabricmc.net/use/installer/) 0.19.2 or newer
- Java 25
- CreateFly 6.0.9 in the supported range shown above
- Any other dependencies requested by your launcher for the same Minecraft version

Always keep Minecraft, CreateFly, this port, and its dependencies on the same supported version line.

## Installation

### Modrinth App

1. Open the [Copycats+ CreateFly Port Modrinth page](https://modrinth.com/mod/copycats+-createfly-port).
2. Select **Install with Modrinth App**.
3. Choose an existing Fabric profile or create a new Fabric profile.
4. Confirm that the profile uses the correct Minecraft version for the port JAR.
5. Install the required dependencies for that version, including Create Fly.
6. Launch the game once so the mod can generate its configuration files.

### Modrinth manual installation

1. Open the [Modrinth versions page](https://modrinth.com/mod/copycats+-createfly-port/versions).
2. Download the Fabric `.jar` matching your Minecraft and CreateFly version.
3. Install [Fabric Loader](https://fabricmc.net/use/installer/) for the same Minecraft version.
4. Install the matching dependencies, including [Create Fly](https://modrinth.com/mod/create-fly).
5. Put the downloaded `.jar` in your Minecraft `mods` folder.
6. Start the game and confirm that the mod appears in the mod list.

### CurseForge App

1. Open the [Copycats+ CreateFly Port CurseForge page](https://www.curseforge.com/minecraft/mc-mods/copycats-createfly-port).
2. Select the Fabric file for your Minecraft and CreateFly version.
3. Open your CurseForge Minecraft profile.
4. Select **Add More Content**.
5. Install the matching file and all required dependencies.
6. Launch the game once so the mod can generate its configuration files.

### CurseForge manual installation

1. Open the [CurseForge files page](https://www.curseforge.com/minecraft/mc-mods/copycats-createfly-port/files).
2. Download the Fabric `.jar` matching your Minecraft and CreateFly version.
3. Install [Fabric Loader](https://fabricmc.net/use/installer/) for the same Minecraft version.
4. Install the matching dependencies, including [Create Fly](https://www.curseforge.com/minecraft/mc-mods/create-fly).
5. Put the downloaded `.jar` in your Minecraft `mods` folder.
6. Start the game and confirm that the mod appears in the mod list.

## Building from source

Install Java 25 before building.

The default target is Minecraft 26.1.2, and its output JAR uses the broader `mc.26.1+` compatibility label:

```bash
./gradlew clean build
```

Build the Minecraft 26.2 target with:

```bash
./gradlew clean build -Ptarget_version=26.2
```

Build every supported release JAR on Windows with:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/build-supported.ps1
```

Gradle places completed JARs in:

```text
build/libs
```

## Diagnostics

Diagnostics are disabled by default. After starting the game once, edit `config/copycats-debug.properties`. Changes reload while the game is running.

- Set `enabled=true` to enable every category.
- Enable individual categories with `ct`, `door`, `material`, `shape`, `filter`, or `face_hiding`.
- Set `limit` to cap the number of messages printed by each category.
- Search `latest.log` for `[Copycats Debug/` to find diagnostic messages.
- During development, categories can also be enabled with JVM options such as `-Dcopycats.debug.ct=true` or `-Dcopycats.debug=true`.

## Support and bug reports

Report issues with this unofficial port in this repository.

Do **not** report port-specific issues to the official Copycats+ issue tracker unless the same problem also occurs in an official Copycats+ release.

Include the following information in bug reports:

- Minecraft version
- Fabric Loader version
- CreateFly version
- This port's version
- Crash report or latest log
- Mod list, when possible
- Whether the issue occurs in singleplayer, multiplayer, or both

## Permission and project policy

**Bennyboy1695** from the Copycats+ team granted permission to publish this unofficial port under these conditions:

- The source code must remain visible on GitHub.
- This project should be archived or deprecated when Copycats+ officially supports this version.

![Permission proof](docs/permission.png)

This repository is intentionally Fabric-only. For the upstream multi-loader project, use the original Copycats+ repository. Support and updates for this port are handled separately from the official project.

## Original projects

- **Copycats+:** [GitHub](https://github.com/copycats-plus/copycats) · [CurseForge](https://www.curseforge.com/minecraft/mc-mods/copycats) · [Modrinth](https://modrinth.com/mod/copycats)
- **Create Fly:** [CurseForge](https://www.curseforge.com/minecraft/mc-mods/create-fly) · [Modrinth](https://modrinth.com/mod/create-fly)

All original Copycats+ code, assets, ideas, and features belong to the original Copycats+ developers.

## Contributing

Bug reports and pull requests for this Fabric port are welcome here. Contributions intended for the official project should be submitted to the upstream Copycats+ repository instead.

## Credits

- Original mod: Create: Copycats+
- Original developers: Copycats+ team
- CreateFly project and its developers
- Create community and addon contributors
- Unofficial port: lucahTheCreator
- OpenAI Codex for help

## License

This port follows the original Copycats+ license and redistribution requirements.
