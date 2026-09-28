# Xperiment Anti-Cheat

Fabric anti-cheat framework for Minecraft 1.21.11.

## Installation

### 1. Get the JAR

1. Open the **Actions** tab on this GitHub repository.
2. Open the latest successful **Build Xperiment Anti-Cheat** workflow run.
3. Scroll to **Artifacts**.
4. Download **xperiment-anticheat**.
5. Extract the ZIP and keep the `.jar` file.

### 2. Prepare your Fabric server

Your server should be running **Fabric for Minecraft 1.21.11**.

Install the matching **Fabric API** in the server's `mods` folder.

### 3. Install Xperiment Anti-Cheat

Put the downloaded JAR into your server's `mods` folder:

```
your-server/
├── mods/
│   ├── xperiment-anticheat-*.jar
│   └── fabric-api-*.jar
└── ...
```

### 4. Start the server

Start the Fabric server normally.

The console should report that Xperiment Anti-Cheat has loaded.

### Important

- Install this mod on the **server** for the server-authoritative checks.
- Client-side mod detection is not treated as authoritative proof.
- A suspicious event can increase violation level, but a single event should not automatically be treated as proof of cheating.
- The project cannot guarantee detection of every ghost client.

## Commands

These commands require operator permission:

```
/xac vl <player>
/xac evidence <player>
/xac reset <player>
```

## Part 3

Adds server-authoritative combat telemetry:
- Conservative 3.15-block reach threshold.
- Repeated sub-2-tick attack intervals recorded as suspicious attack-rate evidence.
- Violation levels instead of instant punishment.
- Up to 50 recent evidence events per player.
- Evidence is written to the server log.

These checks are evidence signals, not proof by themselves. Latency, entity geometry, server lag and legitimate edge cases can affect measurements.

## Development

GitHub Actions builds the mod automatically.
