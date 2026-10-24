# Minecraft 1.21.10 Fabric backport

This experimental target is based on the project's 1.21.11 backport and builds against:

- Minecraft 1.21.10
- Java 21
- Fabric Loader 0.19.3
- Fabric API 0.138.4+1.21.10
- Create Fly 1.21.10-6.0.9-2

The 1.21.10 build uses the Create Fly release for that Minecraft version. It is a separate project from the 1.21.11 target because the Minecraft and Create Fly APIs differ between releases.

## Beta status

The current local beta is `Copycats-CreateFly-1.21.10-beta.2.jar`. The build menu places a copy in `dist/`; this workspace also copies the beta JAR to the user's Downloads folder. It targets Create Fly 6.0.9-2 and Fabric API 0.138.4+1.21.10, required by the current Steam 'n' Rails Fly 1.21.10 beta. Recent fixes include cutout rendering for the animated copycat cogwheel shaft and gear, and avoiding early Create block-entity construction during startup. The smoke test loaded all four mods and entered a singleplayer world; rail and copycat gameplay interactions still need testing.

Build it from the repository root:

```powershell
./gradlew.bat -p backport-1.21.10 clean build
```

The JAR is written to `build/libs/` in this folder. The Windows build menu copies `Copycats-CreateFly-1.21.10-beta.2.jar` into `dist/`.

To launch the isolated development client, use `run-test-client.bat` or choose it in `Copycats Build and Test.bat`. Its world and logs are kept in `run-1.21.10/`.

The dev client starts with Copycats+ and Create Fly loaded. To test Steam 'n' Rails Fly 0.1.5j-beta, place its JAR in `run-1.21.10/mods/` before launching. Run `/copycats_test` to place the development gallery near your player. Gameplay checks for block placement, copied materials, rendering (including grass on cogwheel shafts), particles, connected textures, Create interactions, Steam 'n' Rails train/track behavior, and world save/reload are still needed before treating this as stable.
