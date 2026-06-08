# Create: Copycats+ Fabric Port

<p align="center">
  <a href="https://github.com/Lucahthecreator/copycats--createfly-fabric-port">
    <img alt="GitHub repo" src="https://img.shields.io/badge/GitHub-copycats--createfly--fabric--port-24292e?style=flat-square">
  </a>
  <a href="https://discord.gg/YxPPFHn4SS">
    <img alt="Discord" src="https://img.shields.io/badge/Discord-Create%20Addon%20Hub-5865F2?style=flat-square">
  </a>
</p>

This is a standalone Fabric-only port of Create: Copycats+ for CreateFly.
The NeoForge branch and loader-specific build files were removed so this repo only ships the Fabric version.

## Proof

The permission screenshot is included here for reference.

![Permission proof](docs/permission.png)

## What this port includes

- Fabric-only build and release setup
- Copycat blocks, slabs, stairs, panes, doors, layers, boards, and more
- CreateFly integration for the Fabric loader
- The same copycat feature set, rebuilt for this target

## Installation

1. Download the Fabric jar from the repo release or build output.
2. Install Fabric Loader for the matching Minecraft version.
3. Install the required Fabric dependencies for the same version.
4. Put the jar into your `mods` folder.

## Building from source

- Install Java 25.
- Run `./gradlew build`
- The jar will be placed in `build/libs`

## Notes

- This repository is intentionally Fabric-only.
- If you want the upstream multi-loader project, use the original Copycats repository instead.
- Support and updates for this port are handled separately from the NeoForge branch.

## Credits

- Original mod: Copycats+
- Create community and addon hub contributors
- Permission proof is shown above
