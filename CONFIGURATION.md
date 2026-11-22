# Classic Pipes configuration

Classic Pipes uses one shared configuration file on Fabric, Forge and NeoForge:

`config/classicpipes.properties`

The file is generated automatically on first launch. Missing keys are added with their default values when a later version introduces new settings. Restart the game or server after editing the file.

## Item pipes

| Key | Default | Meaning |
|---|---:|---|
| `items.speed_limit` | 1024 | Global maximum item movement speed |
| `items.basic.speed` / `items.basic.acceleration` | 64 / 1 | Wooden, brick and nether-brick/basic item pipes |
| `items.copper.speed` / `items.copper.acceleration` | 64 / 1 | Copper item pipe |
| `items.copper.extract_amount` | 1 | Items extracted per operation |
| `items.copper.extract_interval_ticks` | 9 | Ticks between extraction attempts |
| `items.advanced_copper.speed` / `items.advanced_copper.acceleration` | 512 / 16 | Advanced copper item pipe |
| `items.advanced_copper.extract_amount` | 64 | Items extracted per operation |
| `items.advanced_copper.extract_interval_ticks` | 9 | Ticks between extraction attempts |
| `items.golden.speed` / `items.golden.acceleration` | 1024 / 16 | Golden item pipe |
| `items.iron.speed` / `items.iron.acceleration` | 64 / 1 | Iron item pipe |
| `items.diamond.speed` / `items.diamond.acceleration` | 64 / 1 | Diamond item pipe |
| `items.flint.speed` / `items.flint.acceleration` | 64 / 1 | Flint item pipe |
| `items.lapis.speed` / `items.lapis.acceleration` | 64 / 1 | Lapis item pipe |
| `items.obsidian.speed` / `items.obsidian.acceleration` | 64 / 1 | Obsidian item pipe |
| `items.bone.speed` / `items.bone.acceleration` | 64 / 1 | Bone item pipe |
| `items.network.speed` / `items.network.acceleration` | 1024 / 64 | Networked item pipes |

## Fluid pipes

| Key | Default | Meaning |
|---|---:|---|
| `fluids.capacity_mb` | 1000 | Maximum fluid stored in one fluid pipe |
| `fluids.minimum_packet_mb` | 20 | Minimum packet size when splitting fluid between directions |
| `fluids.basic.speed_thin` | 256 | Movement speed for fluids tagged as thin |
| `fluids.basic.speed_normal` | 128 | Movement speed for ordinary fluids |
| `fluids.basic.speed_thick` | 64 | Movement speed for fluids tagged as thick |
| `fluids.basic.acceleration` | 1 | Fluid packet acceleration |
| `fluids.copper.extract_amount_mb` | 125 | Copper fluid pipe extraction amount per operation |
| `fluids.copper.extract_interval_ticks` | 5 | Copper fluid pipe extraction interval |
| `fluids.advanced_copper.extract_amount_mb` | 500 | Advanced copper fluid pipe extraction amount |
| `fluids.advanced_copper.extract_interval_ticks` | 5 | Advanced copper fluid pipe extraction interval |

Speed and acceleration values are bounded to safe ranges; extraction intervals are 1–127 ticks. Fluid capacity and packet amounts are in millibuckets (mB).