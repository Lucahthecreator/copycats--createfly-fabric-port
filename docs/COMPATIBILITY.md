# Compatibility and build guide

This file is the version-specific index for the repository. The root [README](../README.MD) contains general project, installation, and development guidance. When adding a Minecraft version, backport, loader, or Create Fly API variant, add its tested combination and build path here and update the relevant build menu or script.

## Supported and testable targets

| Minecraft | Loader | Status | Java | Loader/API requirements | Create Fly | Build source / notes |
| :--- | :--- | :--- | :---: | :--- | :--- | :--- |
| 1.21.11 | Fabric | Beta; gameplay testing | 21 | Fabric Loader 0.19.2+ (development client uses 0.19.3); Fabric API 0.141.6 | 1.21.11-6.0.9-5 | [`backport-1.21.11`](../backport-1.21.11/README.md) |
| 26.1–26.1.2 | Fabric | Supported (v1.16) | 25 | Fabric Loader 0.19.2+; Fabric API 0.152.1+26.1.2 | 6.0.9-4 tested | Root Gradle project; target 26.1.2 |
| 26.2 | Fabric | Supported (v1.16) | 25 | Fabric Loader 0.19.2+; Fabric API 0.152.2+26.2 | 26.2-6.0.9-2 beta; 26.2-rc-2-6.0.9-1 compatibility build | Root Gradle project |
| 26.3 | NeoForge | Community development port v1.17 | 25 | NeoForge 26.3.0.89 | [Create Fly NeoForge 6.0.9 source](https://github.com/zoom-packers/Create-Fly/tree/neoforge-26.3) | [Zoompack 5 community port branch](https://github.com/zoom-packers/copycats-createfly/tree/zp5/26.3); [`neoforge-26.3` porting notes](../neoforge-26.3/PORTING.md) |

Targets not listed here are not currently represented as tested builds in this checkout. Support depends on compatible Minecraft, loader, and Create Fly releases and successful build and gameplay testing. The repository may also contain experimental or unlisted modules; use their local notes when present.

## Artifacts

| Target | Build output |
| :--- | :--- |
| Fabric root project | `build/libs/` |
| 1.21.11 Fabric backport | `backport-1.21.11/build/libs/` |
| NeoForge 26.3 community port | `neoforge-26.3/build/libs/copycats-neoforge-*.jar` |

The Windows build menu copies selected Fabric JARs to `dist/`. Its displayed build names and options are authoritative for the current checkout. Development builds may not be published on CurseForge or Modrinth.

## Build commands

Use the Java version in the target row. On Windows, use `gradlew.bat` and the matching `.bat` or PowerShell helper where provided.

```bash
# Fabric root project, selecting the Minecraft target
./gradlew build -Ptarget_version=26.1.2
./gradlew build -Ptarget_version=26.2

# Fabric 1.21.11 backport
./gradlew -p backport-1.21.11 build

# NeoForge community module; provide the matching Create Fly NeoForge build
./gradlew -p neoforge-26.3 build \
  -Pcreate_jar=/path/to/create-fly-neoforge-6.0.9+neoforge-26.3.jar \
  -Pcreate_interfaces=/path/to/create-fly-neoforge/interfaces.json
```

For the NeoForge module, see [`PORTING.md`](../neoforge-26.3/PORTING.md) and [`scripts/neoforge-26.3.ps1`](../scripts/neoforge-26.3.ps1) for dependency setup, test, client, and server actions. The public Create Fly source is linked in the target row.

## Distribution and attribution

The NeoForge 26.3 adaptation has permission for public distribution under the conditions in its [porting notes](../neoforge-26.3/PORTING.md). Keep its source public and archive or deprecate it when Copycats+ officially supports the corresponding version. Credit the original Copycats+ developers, Create Fly contributors, and each port contributor listed in the module notes.
