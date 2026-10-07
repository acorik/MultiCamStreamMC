# MultiCamStreamMC

**MultiCamStreamMC** is a Minecraft **Spigot/Paper plugin project** focused on cinematic camera control and camera movement.

This repository is plugin-only. It does **not** contain a Fabric mod.

## Plugins

### CameraOperator

CameraOperator provides tools for creating and controlling cinematic cameras on a Spigot/Paper server.

The published versions currently tracked in this repository are:

- 1.2.1
- 2.0.0
- 2.1.0
- 2.1.1

The repository also records the SHA-256 hash of each original JAR in `artifacts/VERSIONS.md`.

### CameraAnimations

CameraAnimations is a companion Spigot/Paper plugin for camera animation projects and playback.

Tracked version:

- 1.0.0

## Repository contents

- `artifacts/VERSIONS.md` — versions and SHA-256 hashes of the supplied JARs.
- `artifacts/EXTRACTION.md` — classes and structure identified from the supplied JARs.
- `LICENSE` — MIT license.

The original JARs are the authoritative plugin builds. The documentation in this repository describes the supplied plugin artifacts; it is not a claim that reconstructed code is byte-for-byte identical to the original source.

## Installation

1. Download the desired CameraOperator JAR.
2. Put it in the server's `plugins/` folder.
3. Restart the Spigot/Paper server.
4. Configure and use the plugin commands in-game.

Use the version that matches your Minecraft/Spigot/Paper server and the release's requirements.

## Camera system

CameraOperator is intended for cinematic Minecraft recording and streaming workflows, including:

- camera points
- camera sessions
- slider paths
- shots and projects
- live camera modes
- camera control for recording/streaming setups

## License

MIT.
