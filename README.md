# Xperiment Anti-Cheat

Fabric 1.21.11 anti-cheat framework.

## Build

The GitHub Actions workflow builds the mod automatically on pushes and pull requests.

The compiled JAR is uploaded as a workflow artifact.

## Local build

Requires Java 21 and Gradle.

```bash
gradle build
```

The output is in:

```
build/libs/
```

## Current scope

This is the project foundation. The first version provides the Fabric server/client entrypoints and CI build pipeline.

Future detection should be server-authoritative. Client-reported mod information should be treated as evidence, not as proof, because modified clients can hide or falsify their local state.

## License

Add your preferred license before publishing or distributing the project.
