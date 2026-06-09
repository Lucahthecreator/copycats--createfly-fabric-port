# Create: Copycats+ Fabric Port

<p align="center">
  <a href="https://github.com/Lucahthecreator/copycats--createfly-fabric-port">
    <img alt="GitHub repo" src="https://img.shields.io/badge/GitHub-copycats--createfly--fabric--port-24292e?style=flat-square">
  </a>
</p>

This is an **unofficial Fabric-only port** of **Create: Copycats+ 3.0.7** for **CreateFly**.

This project is not an official Copycats+ release and is not maintained or supported by the official Copycats+ team.

Support for this port is handled separately by **lucahTheCreator**.

---

## Permission

Permission to publish this unofficial port was granted by **Bennyboy1695** from the Copycats+ team.

The permission was granted with these conditions:

* The source code must be visible on GitHub.
* This project should be archived or deprecated when Copycats+ officially supports this version.

Proof screenshot:

![Permission proof](docs/permission.png)

---

## Original Projects

* Copycats+: [GitHub](https://github.com/copycats-plus/copycats) | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/copycats) | [Modrinth](https://modrinth.com/mod/copycats)
* Create Fly: [CurseForge](https://www.curseforge.com/minecraft/mc-mods/create-fly) | [Modrinth](https://modrinth.com/mod/create-fly)

All original Copycats+ code, assets, ideas, and features belong to the original Copycats+ developers.

---

## What This Port Includes

* Fabric-only build and release setup
* Java code changes needed for the CreateFly target version
* Copycat blocks, slabs, stairs, panes, doors, layers, boards, and more
* CreateFly compatibility for the Fabric loader
* The same general Copycats+ feature set rebuilt for this target

---

## Installation

### Option 1: CurseForge App

1. Open the Copycats+ CreateFly Port CurseForge page.
2. Pick the Fabric file for your Minecraft / CreateFly version.
3. Open your CurseForge Minecraft profile.
4. Click `Add More Content`.
5. Install the matching file and required dependencies.
6. Launch the game once so the mod can generate its config files.

### Option 2: Manual Install

1. Download the Fabric `.jar` from the Releases page or CurseForge.
2. Install [Fabric Loader](https://fabricmc.net/use/installer/) for the matching Minecraft version.
3. Install the required dependencies for the same Minecraft version, including [Create Fly](https://www.curseforge.com/minecraft/mc-mods/create-fly).
4. Put the `.jar` into your `mods` folder.
5. Start the game and confirm the mod appears in the mod list.

---

## Building From Source

Install Java 21.

Then run:

```bash
./gradlew build
```

On Windows:

```powershell
.\gradlew build
```

The built `.jar` should be placed in:

```text
build/libs
```

or a loader-specific build folder depending on the Gradle setup.

---

## Support

Report issues for this unofficial port on this repository.

Do **not** report issues from this port to the official Copycats+ issue tracker unless the same bug also happens on an official Copycats+ release.

When reporting a bug, include:

* Minecraft version
* Fabric Loader version
* CreateFly version
* This port version
* Crash report or latest log
* Mod list, if possible
* Whether the issue happens in singleplayer, multiplayer, or both

---

## Notes

* This repository is intentionally Fabric-only.
* If you want the upstream multi-loader project, use the original Copycats+ repository.
* Support and updates for this port are handled separately from the official Copycats+ project.
* This project will be archived or deprecated when official Copycats+ support for this version becomes available.

---

## Contributing

Bug reports and pull requests for this Fabric port are welcome here.

If you want to contribute to the official project, use the upstream Copycats+ repository instead.

---

## Credits

* Original mod: Create: Copycats+
* Original developers: Copycats+ team
* CreateFly project and its developers
* Create community and addon contributors
* Unofficial port: lucahTheCreator

Permission proof is shown above.

---

## License

This port follows the original Copycats+ license and redistribution requirements.

Do not remove original copyright, license, or attribution notices.
