# Compatibility and build guide

This file is the version-specific index for the repository. The root [README](../README.MD) contains general project, installation, and development guidance. When adding a Minecraft version, backport, loader, or Create Fly API variant, add its tested combination and build path here and update the relevant build menu or script.

## Supported and testable targets

| Minecraft | Loader | Status | Java | Loader/API requirements | Create Fly | Build source / notes |
| :--- | :--- | :--- | :---: | :--- | :--- | :--- |
| 1.21.10 | Fabric | Beta.3; client startup tested with Create Fly 6.0.8-6 and 6.0.9-2 | 21 | Fabric Loader 0.19.3; Fabric API 0.138.4+1.21.10 | 1.21.10-6.0.8-6 or 1.21.10-6.0.9-2 | [`backport-1.21.10`](../backport-1.21.10/README.md); local beta JAR is copied to `Downloads/Copycats-CreateFly-1.21.10-beta.3.jar` |
| 1.21.11 | Fabric | Beta.1 archived; beta.2 compatibility fix candidate | 21 | Fabric Loader 0.19.2+ (development client uses 0.19.3); Fabric API 0.141.6 | 1.21.11-6.0.9-5 | [`backport-1.21.11`](../backport-1.21.11/README.md); known [startup issue #10](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/10) and [#11](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/11) |
| 26.1–26.1.2 | Fabric | Supported (v1.16) | 25 | Fabric Loader 0.19.2+; Fabric API 0.152.1+26.1.2 | 6.0.9-4 tested | Root Gradle project; target 26.1.2 |
| 26.2 | Fabric | Supported (v1.16) | 25 | Fabric Loader 0.19.2+; Fabric API 0.152.2+26.2 | 26.2-6.0.9-2 beta; 26.2-rc-2-6.0.9-1 compatibility build | Root Gradle project |

Targets not listed here are not currently represented as tested builds in this checkout. Support depends on compatible Minecraft, loader, and Create Fly releases and successful build and gameplay testing. The repository may also contain experimental or unlisted modules; use their local notes when present.

## Artifacts

| Target | Build output |
| :--- | :--- |
| Fabric root project | `build/libs/` |
| 1.21.10 Fabric backport | `backport-1.21.10/build/libs/` |
| 1.21.11 Fabric backport | `backport-1.21.11/build/libs/` |

The Windows build menu copies selected Fabric JARs to `dist/`. Its displayed build names and options are authoritative for the current checkout. Development builds may not be published on CurseForge or Modrinth.

## Build commands

Use the Java version in the target row. On Windows, use `gradlew.bat` and the matching `.bat` or PowerShell helper where provided.

```bash
# Fabric root project, selecting the Minecraft target
./gradlew build -Ptarget_version=26.1.2
./gradlew build -Ptarget_version=26.2

# Fabric 1.21.11 backport
./gradlew -p backport-1.21.11 build

# Fabric 1.21.10 backport
./gradlew -p backport-1.21.10 build

```

## Archived loader references

The NeoForge 26.3 source and data are retained for reference, but this repository does not currently provide a NeoForge build. If an official NeoForge version becomes available, use its official files and instructions; these archived notes can help identify prior porting work.

| Minecraft | Loader | Reference material |
| :--- | :--- | :--- |
| 26.3 | NeoForge | [Create Fly NeoForge source](https://github.com/zoom-packers/Create-Fly/tree/neoforge-26.3); [community port branch](https://github.com/zoom-packers/copycats-createfly/tree/zp5/26.3); [archived porting notes](NEOFORGE-26.3-REFERENCE.md) |

## Distribution and attribution

The archived NeoForge links point to the community port and its Create Fly dependency as references. They do not describe a build target or release from this repository.
