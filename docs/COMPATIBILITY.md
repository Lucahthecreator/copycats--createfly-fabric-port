# Compatibility and build guide

This file is the version-specific index for the repository. The root [README](../README.MD) contains general project, installation, and development guidance. When adding a Minecraft version, backport, loader, or Create Fly API variant, add its tested combination and build path here and update the relevant build menu or script.

## Supported and testable targets

| Minecraft | Loader | Status | Java | Loader/API requirements | Create Fly | Build source / notes |
| :--- | :--- | :--- | :---: | :--- | :--- | :--- |
| 1.21.10 | Fabric | Beta (v1.18); client startup tested with Create Fly 6.0.8-6 and 6.0.9-2 | 21 | Fabric Loader 0.19.3; Fabric API 0.138.4+1.21.10 | 1.21.10-6.0.8-6 or 1.21.10-6.0.9-2 | [`backport-1.21.10`](../backport-1.21.10/README.md) |
| 1.21.11 | Fabric | Beta (v1.18); client startup tested with Create Fly 6.0.9-5 | 21 | Fabric Loader 0.19.2+ (development client uses 0.19.3); Fabric API 0.141.6 | 1.21.11-6.0.9-5 | [`backport-1.21.11`](../backport-1.21.11/README.md); see prior [startup issue #10](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/10) and [#11](https://github.com/Lucahthecreator/copycats--createfly-fabric-port/issues/11) |
| 26.1–26.1.2 | Fabric | Supported (v1.18); client startup tested with Create Fly 6.0.9-4 | 25 | Fabric Loader 0.19.2+; Fabric API 0.152.1+26.1.2 | 6.0.9-4 tested | Root Gradle project; target 26.1.2 |
| 26.2 | Fabric | Supported (v1.18); one jar tested with both Create Fly builds | 25 | Fabric Loader 0.19.2+; Fabric API 0.152.2+26.2 | 26.2-6.0.9-2 beta and 26.2-rc-2-6.0.9-1 | Root Gradle project; one jar accepts `>=6.0.9-1 <6.0.10` |
| 26.3 | Fabric | In progress | 25 | Fabric Loader 0.20.1+; Fabric API 0.165.2+26.3 | Create Fly 26.3-6.0.9-2 | Root Gradle project; target 26.3 |

Targets not listed here are not currently represented as tested builds in this checkout. Support depends on compatible Minecraft, loader, and Create Fly releases and successful build and gameplay testing. The repository may also contain experimental or unlisted modules; use their local notes when present.

Each jar declares its intended Minecraft versions: the 26.1+ jar accepts 26.1, 26.1.1 and 26.1.2; the 1.21.10, 1.21.11, 26.2 and 26.3 jars each accept their named version only. Fabric Loader rejects other Minecraft versions.

## Create Fly version compatibility

Use the Create Fly file made for the same Minecraft version as the Copycats+ target. Current Copycats metadata accepts all 6.0.9 build revisions within the listed bounds, so new builds in that API line can load without a Copycats release. The 1.21.10 target additionally accepts the older 6.0.8-6 build and has a runtime compatibility shim for the API changes between 6.0.8 and 6.0.9. The exact builds smoke-tested are listed above; a new API line such as 6.0.10 needs a compatibility check before it is included in the accepted range.

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
./gradlew build -Ptarget_version=26.3

# Fabric 1.21.11 backport
./gradlew -p backport-1.21.11 build

# Fabric 1.21.10 backport
./gradlew -p backport-1.21.10 build

```

## Distribution and attribution
