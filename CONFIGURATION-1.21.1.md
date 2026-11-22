# Classic Pipes 1.21.1 configuration

This branch targets **Minecraft 1.21.1 / NeoForge 21.1.219**. The same shared settings class is in the common source set, so the property keys are also usable by the Fabric and Forge builds from this branch.

After the first launch, the mod creates:

`config/classicpipes.properties`

Edit the file and restart the game/server. Missing keys are added automatically on the next launch; existing values are preserved.

## Item pipes

| Property | Default | Purpose |
|---|---:|---|
| `items.speed_limit` | 1024 | Global maximum speed for item packets |
| `items.basic.speed` / `items.basic.acceleration` | 64 / 1 | Wooden, brick and nether-brick pipes |
| `items.copper.speed` / `items.copper.acceleration` | 64 / 1 | Copper item pipe |
| `items.copper.extract_amount` | 1 | Items extracted per attempt |
| `items.copper.extract_interval_ticks` | 9 | Ticks between extraction attempts |
| `items.advanced_copper.speed` / `items.advanced_copper.acceleration` | 512 / 16 | Advanced copper item pipe |
| `items.advanced_copper.extract_amount` | 64 | Items extracted per attempt |
| `items.advanced_copper.extract_interval_ticks` | 9 | Ticks between extraction attempts |
| `items.golden.speed` / `items.golden.acceleration` | 1024 / 16 | Golden item pipe |
| `items.iron.speed` / `items.iron.acceleration` | 64 / 1 | Iron item pipe |
| `items.diamond.speed` / `items.diamond.acceleration` | 64 / 1 | Diamond item pipe |
| `items.flint.speed` / `items.flint.acceleration` | 64 / 1 | Flint item pipe |
| `items.lapis.speed` / `items.lapis.acceleration` | 64 / 1 | Lapis item pipe |
| `items.obsidian.speed` / `items.obsidian.acceleration` | 64 / 1 | Obsidian item pipe |
| `items.bone.speed` / `items.bone.acceleration` | 64 / 1 | Bone item pipe |
| `items.network.speed` / `items.network.acceleration` | 1024 / 64 | Routing/network item pipes |

## Fluid pipes

| Property | Default | Purpose |
|---|---:|---|
| `fluids.capacity_mb` | 1000 | Capacity of each fluid pipe, in mB |
| `fluids.minimum_packet_mb` | 20 | Minimum amount for splitting fluid packets |
| `fluids.speed_thin` | 256 | Speed for fluids tagged as thin |
| `fluids.speed_normal` | 128 | Speed for normal fluids |
| `fluids.speed_thick` | 64 | Speed for thick fluids |
| `fluids.acceleration` | 1 | Fluid packet acceleration |
| `fluids.copper.extract_amount_mb` | 125 | Copper fluid pipe extraction amount, in mB |
| `fluids.copper.extract_interval_ticks` | 5 | Copper fluid pipe extraction interval |
| `fluids.advanced_copper.extract_amount_mb` | 500 | Advanced copper fluid pipe extraction amount, in mB |
| `fluids.advanced_copper.extract_interval_ticks` | 5 | Advanced copper fluid pipe extraction interval |

Speeds are limited by `items.speed_limit`. Extraction intervals are clamped to 1–127 ticks. Numeric values outside supported ranges are clamped; invalid values fall back to defaults.
