# Xperiment Anti-Cheat

Fabric anti-cheat framework for Minecraft 1.21.11.

## Part 3

Adds server-authoritative combat telemetry:
- Conservative 3.15-block reach threshold.
- Repeated sub-2-tick attack intervals recorded as suspicious attack-rate evidence.
- Violation levels instead of instant punishment.
- Up to 50 recent evidence events per player.
- Evidence is written to the server log.
- Admin commands: /xac vl <player>, /xac evidence <player>, /xac reset <player>.

These checks are evidence signals, not proof by themselves. Latency, entity geometry, server lag and legitimate edge cases can affect measurements.

Fabric's 1.21.11 API provides server-side play/network and combat APIs; this project keeps authoritative checks on the server rather than trusting client claims.
