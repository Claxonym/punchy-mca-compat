Modrinth: https://modrinth.com/mod/punchy-x-mca-model-fix/moderation

# Punchy MCA Reborn Compatibility (test build)

Client-only compatibility bridge for Minecraft 1.20.1 / Forge 47.x.

## What it does

When both `punchy` and `mca` are loaded, it calls Punchy's public API:

`PunchyApi.useMinecraftHands("punchy_mca_compat", () -> true)`

This asks Punchy to keep its animation transforms but draw the animated arms through Minecraft's regular first-person arm rendering path. On Forge, that path includes `RenderArmEvent`, which is the integration point for mods that customize first-person arms.

The bridge uses reflection so Punchy remains an optional runtime dependency. It does nothing when either mod is absent.

## Install

1. Put `punchy-mca-compat-1.0.0.jar` into the instance's `mods` folder.
2. Keep your existing Punchy and MCA jars unchanged.
3. Launch the client and check `logs/latest.log` for:
   `[Punchy-MCA-Compat] Registered Punchy/MCA first-person arm bridge.`
4. Test the MCA-selected appearance in first person.

If the hand disappears, still shows the account skin, or causes any new rendering issue, remove this jar. It is client-only and does not alter world data.

## Important

This is an experimental, unverified compatibility shim. It is based on Punchy's documented public API; it has not been tested in the user's exact modpack. It assumes MCA's first-person arm appearance is available from Minecraft/Forge's normal first-person arm render path. If MCA 7.7.1-beta.5 uses another rendering path in this configuration, the bridge will not be sufficient.

## Build from source

Run `gradle build` with Java 17. The project targets Forge 1.20.1-47.4.28 and does not need a Punchy compile dependency because its API is accessed reflectively.
