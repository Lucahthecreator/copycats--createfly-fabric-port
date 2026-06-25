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

### Supported versions

| Port jar | Minecraft compatibility | CreateFly compatibility | Build dependency |
| --- | --- | --- | --- |
| `mc.26.1.2` | `>=26.1- <26.2-` | `>=6.0.9-1 <6.0.10` | CreateFly 6.0.9 build 4 |
| `mc.26.2` | `>=26.2- <26.3-` | `>=6.0.9-1 <6.0.10` | CreateFly 6.0.9 build 1 for 26.2 |

Use the jar for your Minecraft line. Do not use the `26.1.2` jar on `26.2`, or the `26.2` jar on `26.1.2`.

### Option 1: CurseForge App

1. Open the Copycats+ CreateFly Port CurseForge page https://www.curseforge.com/minecraft/mc-mods/copycats-createfly-port.
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

Install Java 25.

Build the default Minecraft 26.1.2 target:

```bash
./gradlew clean build
```

Build the Minecraft 26.2 target:

```bash
./gradlew clean build -Ptarget_version=26.2
```

Build both release jars on Windows:

```powershell
powershell -ExecutionPolicy Bypass -File scripts/build-supported.ps1
```

The built `.jar` files are placed in:

```text
build/libs
```

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

## Diagnostics

Diagnostics are off by default. After starting the game once, edit
`config/copycats-debug.properties`; changes reload while the game is running.

- Set `enabled=true` to enable every category.
- Enable individual categories with `ct`, `door`, `material`, `shape`, `filter`, or `face_hiding`.
- Set `limit` to cap the number of messages printed by each category.
- Search `latest.log` for `[Copycats Debug/` to find all diagnostic messages.
- For development, categories can also be enabled with JVM options such as
  `-Dcopycats.debug.ct=true` or `-Dcopycats.debug=true`.

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
* OpenAI Codex for help

Permission proof is shown above.

---

## License

This port follows the original Copycats+ license and redistribution requirements.
