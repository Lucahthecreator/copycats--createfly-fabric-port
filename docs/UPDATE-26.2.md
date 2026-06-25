# Create: Copycats+ CreateFly Port - Minecraft 26.2

Release date: June 19, 2026

## Compatibility

- Minecraft: `>=26.2- <26.3-`
- Tested on Minecraft `26.2`
- Fabric Loader: `>=0.19.2`
- Java: `25` or newer
- CreateFly: `>=6.0.9-1 <6.0.10`
- Built against CreateFly `6.0.9-1` for Minecraft 26.2

## Added

- A separate Minecraft 26.2 build of the CreateFly Copycats+ port.
- Connected-texture support between adjacent copycat blocks using the same copied material.
- Connected framed-glass borders across top, bottom, and side neighbors.
- Internal-face hiding for matching transparent copied materials.
- Render-path support for vanilla rendering, Fabric Indigo, and optional Sodium integration.
- Copied-material lighting and animated sliding/folding door lighting support.

## Fixed

- Ported the CreateFly cogwheel item constructor change introduced in the 26.2 build.
- Replaced the removed Minecraft `ItemTags.DOORS` reference with the 26.2 wooden-door item tag.
- Framed glass showing borders between adjacent copycat blocks.
- Glass faces remaining visible inside connected copycat structures.
- Connected textures receiving the copycat wrapper state instead of the copied material.
- Copycat lighting and shadows using incorrect values.

## Tested With

- Minecraft `26.2`
- CreateFly `6.0.9-1` for 26.2
- Fabric API `0.152.2+26.2`

The shader compatibility hooks are included. Use Sodium, Iris, and shader-pack versions made specifically for your Minecraft 26.2 installation.

## Installation

1. Install Fabric Loader for Minecraft 26.2.
2. Install Fabric API and CreateFly `6.0.9-1` for the 26.2 line.
3. Put `copycats-createfly-3.0.7-createfly+mc.26.2.jar` in the `mods` folder.
4. Do not install the 26.1.2 jar on Minecraft 26.2.
