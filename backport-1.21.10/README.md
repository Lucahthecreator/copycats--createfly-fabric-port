# Minecraft 1.21.10 Fabric backport

This experimental target is based on the project's 1.21.11 backport and builds against:

- Minecraft 1.21.10
- Java 21
- Fabric Loader 0.19.3
- Fabric API 0.138.4+1.21.10
- Create Fly 1.21.10-6.0.8-6 or 1.21.10-6.0.9-2

The 1.21.10 build uses the Create Fly release for that Minecraft version. It is a separate project from the 1.21.11 target because the Minecraft and Create Fly APIs differ between releases.

## Beta status

The current beta is `Copycats-CreateFly-1.21.10-v1.18.jar`. One JAR is compatible with Create Fly 6.0.8-6 and 6.0.9-2. It compiles against 6.0.9-2 and uses compatibility lookups for APIs that moved between those releases. Fabric metadata allows `>=6.0.8-6 <6.0.10`. Select the runtime used for the development client with `-P createFlyRuntimeVersion=1.21.10-6.0.8-6` or `-P createFlyRuntimeVersion=1.21.10-6.0.9-2`; the default is 6.0.9-2. Steam 'n' Rails Fly requires the newer version. Both versions reached client startup in smoke tests; with 6.0.9-2, Steam 'n' Rails also initialized. Train/copycat gameplay interactions still need testing.

Build it from the repository root:

```powershell
./gradlew.bat -p backport-1.21.10 clean build
```

The JAR is written to `build/libs/` in this folder. The Windows build menu copies `Copycats-CreateFly-1.21.10-v1.18.jar` into `dist/`.

To launch the isolated development client, use `run-test-client.bat` or choose it in `Copycats Build and Test.bat`. Its world and logs are kept in `run-1.21.10/`.

The dev client starts with Copycats+ and Create Fly loaded. To test Steam 'n' Rails Fly 0.1.5j-beta, place its JAR in `run-1.21.10/mods/` before launching. Run `/copycats_test` to place the development gallery near your player. Gameplay checks for block placement, copied materials, rendering (including grass on cogwheel shafts), particles, connected textures, Create interactions, Steam 'n' Rails train/track behavior, and world save/reload are still needed before treating this as stable.
