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

## Original Projects

- Copycats+: [GitHub](https://github.com/copycats-plus/copycats) | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/copycats) | [Modrinth](https://modrinth.com/mod/copycats)
- Create Fly: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/create-fly) | [Modrinth](https://modrinth.com/mod/create-fly)

## Proof

The permission screenshot is included here for reference.

![Permission proof](docs/permission.png)

## What this port includes

- Fabric-only build and release setup
- Copycat blocks, slabs, stairs, panes, doors, layers, boards, and more
- CreateFly integration for the Fabric loader
- The same copycat feature set, rebuilt for this target

## Installation

### Option 1: CurseForge App

1. Open the [Create: Copycats+ CurseForge page](https://www.curseforge.com/minecraft/mc-mods/copycats).
2. Pick the Fabric file for your Minecraft version.
3. Open your CurseForge Minecraft profile.
4. Click `Add More Content` and install the matching jar and required dependencies.
5. Launch the game once so the mod can generate its config files.

### Option 2: Manual install

1. Download the Fabric jar from the repo release or build output.
2. Install [Fabric Loader](https://fabricmc.net/use/installer/) for the matching Minecraft version.
3. Install the required dependencies for the same Minecraft version, including [Create Fly](https://www.curseforge.com/minecraft/mc-mods/create-fly).
4. Put the jar into your `mods` folder.
5. Start the game and confirm the mod appears in the mod list.

## Building from source

- Install Java 25.
- Run `./gradlew build`
- The jar will be placed in `build/libs`

## Notes

- This repository is intentionally Fabric-only.
- If you want the upstream multi-loader project, use the original Copycats+ repository instead.
- Support and updates for this port are handled separately from the NeoForge branch.

## Contributing

- Bug reports and pull requests for this Fabric port are welcome here.
- Translation work should go through the linked Crowdin project.
- If you are trying to help the original projects instead of this port, use the upstream Copycats+ and Create Fly links above.

## Credits

- Original mod: Copycats+
- Create community and addon hub contributors
- Permission proof is shown above
