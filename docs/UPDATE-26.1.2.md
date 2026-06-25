# Create: Copycats+ CreateFly Port - Minecraft 26.1.2

Release date: June 19, 2026

## Compatibility

- Minecraft: `>=26.1- <26.2-`
- Fabric Loader: `>=0.19.2`
- Java: `25` or newer
- CreateFly: `>=6.0.9-1 <6.0.10`
- Built and tested against CreateFly `6.0.9-4`
- Supports CreateFly `6.0.9` builds 1, 2, 3, and 4

## Added

- Fabric-only CreateFly port of Create: Copycats+.
- Connected-texture support between adjacent copycat blocks using the same copied material.
- Connected framed-glass borders across top, bottom, and side neighbors.
- Internal-face hiding for matching transparent copied materials.
- Render-path support for vanilla rendering, Fabric Indigo, and Sodium.
- Copied-material lighting support, including light-emitting materials.
- Shader-aware lighting for copycat blocks and animated sliding/folding doors.

## Fixed

- Framed glass showing borders between vertically or horizontally adjacent copycat blocks.
- Glass faces remaining visible inside connected copycat structures.
- Sliding and folding doors rendering too dark or black while animating with shaders.
- Copycat blocks using incorrect world light or shadow values.
- Connected textures receiving the copycat wrapper state instead of the copied material.

## Tested With

- CreateFly `6.0.9-4`
- Fabric API `0.152.1+26.1.2`
- Sodium `0.8.12+mc26.1.2`
- Iris `1.10.9+mc26.1.1`
- BSL `v10.1.3`
- Complementary Reimagined `r5.8.1`

## Installation

1. Install Fabric Loader for Minecraft 26.1.2.
2. Install Fabric API and a CreateFly `6.0.9` build for the 26.1 line.
3. Put `copycats-createfly-3.0.7-createfly+mc.26.1.2.jar` in the `mods` folder.
4. Do not install the 26.2 jar on Minecraft 26.1.2.

