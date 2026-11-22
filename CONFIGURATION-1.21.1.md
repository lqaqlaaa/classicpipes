# Classic Pipes 1.21.1 配置说明

本分支面向 **Minecraft 1.21.1**。配置由 common 公共代码实现，不依赖 NeoForge/Fabric/Forge 专属 API。

首次启动后会生成：
`config/classicpipes.properties`

## 使用方法

1. 启动一次游戏，让模组生成配置文件。
2. 完全退出游戏或服务器。
3. 使用 UTF-8 文本编辑器打开 `config/classicpipes.properties`。
4. 修改等号右侧的数值，保存后重新启动。

配置文件中的每个参数都有中文注释，说明用途、单位和调整方向。新增参数会在下次启动时自动补齐；已有值不会被覆盖。无法识别的自定义键会被保留，但当前版本不会读取它们。

## 参数分组

### 物品管道
- `items.speed_limit`：所有物品管道的最高速度上限。
- `items.initial_packet_speed`：新生成的物品包进入管道时的初始速度。
- `items.<管道类型>.speed`：对应管道的目标移动速度。
- `items.<管道类型>.acceleration`：对应管道每刻调整物品速度的幅度。
- `items.copper.extract_amount`、`items.advanced_copper.extract_amount`：铜质/高级铜质物品管道每次抽取的物品数量。
- `items.copper.extract_interval_ticks`、`items.advanced_copper.extract_interval_ticks`：两次抽取之间的游戏刻数。

管道类型包括：`basic`（普通/基础）、`copper`、`advanced_copper`、`golden`、`iron`、`diamond`、`flint`、`lapis`、`obsidian`、`bone`、`network`（网络型）。

### 物品与网络请求生命周期
- `items.packet_lifetime_ticks`：物品包在管道内的最长存活时间。
- `items.network_request_timeout_ticks`：网络请求等待送达的超时时间。
- `items.scheduled_route_timeout_ticks`：网络计划路线的最长有效时间。
- `items.routing_overflow_check_interval_ticks`：路由管道检查溢出的间隔。
- `items.recipe_crafter_poll_interval_ticks`：配方管道等待合成时检查相邻容器产物的间隔。

### 网络更新与缓存
- `network.client_cache_update_interval_ticks`：网络缓存同步到客户端的间隔。
- `network.pipe_update_scan_minimum`：网络更新抽样调度使用的最小扫描基数。

### 流体管道
- `fluids.capacity_mb`：单段流体管道内部容量，单位 mB。
- `fluids.minimum_packet_mb`：传输流体包的最小量，单位 mB。
- `fluids.speed_thin`、`fluids.speed_normal`、`fluids.speed_thick`：三种流体类别对应的目标速度。
- `fluids.acceleration`：流体包每刻调整速度的幅度。
- `fluids.copper.extract_amount_mb`、`fluids.advanced_copper.extract_amount_mb`：铜质/高级铜质流体管道每次抽取的流体量。
- `fluids.copper.extract_interval_ticks`、`fluids.advanced_copper.extract_interval_ticks`：两次流体抽取之间的游戏刻数。

## 单位换算

- 20 游戏刻 = 1 秒。
- 1200 游戏刻 = 1 分钟。
- 24000 游戏刻 = 20 分钟。
- 流体容量和流体抽取量使用 mB（毫桶）。
- 管道速度使用模组内部速度单位，不等同于方块/秒；速度和加速度保存在 Java `short` 中，因此可配置范围为 1–32767。
- 物品数量、流体容量/包量使用 Java `int`，可配置范围为 1–2147483647；计时器使用 Java `long`，可配置范围最高为 9223372036854775807（部分采用“大于阈值”判断的计时器最大为 9223372036854775806）；实际抽取仍受相邻容器的库存、槽位及能力接口限制。

## 注意

- 配置目前在启动时读取，修改后必须重启游戏或服务器。
- 配置上限按对应源代码字段及其运算类型确定，不再额外设置任意的 127、1000000 等上限。超出实际数据类型范围的输入会限制到该类型可支持的范围。
- 物品/流体管道内部用于模型定位的几何常量、方向编号、GUI 布局坐标等属于程序实现数据，不是玩家可调的游戏平衡参数，因此不会放进配置文件。
- 本配置文件为 NeoForge 1.21.1 分支的实际实现清单；若某项源码参数尚未接入，不能仅凭文档声称它已可配置。
