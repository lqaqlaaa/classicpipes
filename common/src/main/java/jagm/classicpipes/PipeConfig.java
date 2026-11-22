package jagm.classicpipes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Shared UTF-8 configuration. Uses Java only so the same file works on
 * NeoForge, Forge and Fabric.
 */
public final class PipeConfig {
    private static final Path FILE = Path.of("config", "classicpipes.properties");
    private static final Properties VALUES = new Properties();
    private static final Map<String, String> DEFAULTS = defaults();

    static { load(); }
    private PipeConfig() {}
    public static void initialize() {}

    private static Map<String, String> defaults() {
        Map<String, String> p = new LinkedHashMap<>();
        p.put("items.speed_limit", "1024");
        p.put("items.initial_packet_speed", "64");
        addItem(p, "basic", 64, 1);
        addItem(p, "copper", 64, 1);
        p.put("items.copper.extract_amount", "1");
        p.put("items.copper.extract_interval_ticks", "9");
        addItem(p, "advanced_copper", 512, 16);
        p.put("items.advanced_copper.extract_amount", "64");
        p.put("items.advanced_copper.extract_interval_ticks", "9");
        addItem(p, "golden", 1024, 16);
        addItem(p, "iron", 64, 1);
        addItem(p, "diamond", 64, 1);
        addItem(p, "flint", 64, 1);
        addItem(p, "lapis", 64, 1);
        addItem(p, "obsidian", 64, 1);
        addItem(p, "bone", 64, 1);
        addItem(p, "network", 1024, 64);
        p.put("items.packet_lifetime_ticks", "24000");
        p.put("items.network_request_timeout_ticks", "24000");
        p.put("items.scheduled_route_timeout_ticks", "1200");
        p.put("items.routing_overflow_check_interval_ticks", "100");
        p.put("items.recipe_crafter_poll_interval_ticks", "8");
        p.put("network.client_cache_update_interval_ticks", "40");
        p.put("network.pipe_update_scan_minimum", "100");

        p.put("fluids.capacity_mb", "1000");
        p.put("fluids.minimum_packet_mb", "20");
        p.put("fluids.speed_thin", "256");
        p.put("fluids.speed_normal", "128");
        p.put("fluids.speed_thick", "64");
        p.put("fluids.acceleration", "1");
        p.put("fluids.copper.extract_amount_mb", "125");
        p.put("fluids.copper.extract_interval_ticks", "5");
        p.put("fluids.advanced_copper.extract_amount_mb", "500");
        p.put("fluids.advanced_copper.extract_interval_ticks", "5");
        p.put("fluids.obsidian.gurgle_min_delay_ticks", "80");
        p.put("fluids.obsidian.gurgle_max_delay_ticks", "160");
        p.put("fluids.obsidian.gurgle_chance_denominator", "10");
        return p;
    }

    private static void addItem(Map<String, String> p, String id, int speed, int acceleration) {
        p.put("items." + id + ".speed", Integer.toString(speed));
        p.put("items." + id + ".acceleration", Integer.toString(acceleration));
    }

    private static void load() {
        try {
            Files.createDirectories(FILE.getParent());
            if (Files.exists(FILE)) {
                try (BufferedReader reader = Files.newBufferedReader(FILE, StandardCharsets.UTF_8)) {
                    VALUES.load(reader);
                }
            }
            boolean changed = false;
            for (Map.Entry<String, String> entry : DEFAULTS.entrySet()) {
                if (!VALUES.containsKey(entry.getKey())) {
                    VALUES.setProperty(entry.getKey(), entry.getValue());
                    changed = true;
                }
            }
            if (changed || !Files.exists(FILE)) save();
        } catch (IOException exception) {
            DEFAULTS.forEach(VALUES::setProperty);
            java.util.logging.Logger.getLogger("Classic Pipes").warning(
                "无法读取 classicpipes.properties，已使用内置默认值：" + exception.getMessage());
        }
    }

    /**
     * Writes a hand-authored UTF-8 template rather than Properties.store(),
     * because Properties.store() would discard the Chinese explanations.
     */
    private static void save() throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(FILE, StandardCharsets.UTF_8)) {
            w.write("# Classic Pipes 1.21.1 游戏数值配置");
            w.newLine();
            w.write("# 修改后需要完全退出并重新启动游戏才能生效。");
            w.newLine();
            w.write("# 数值越大通常代表速度/数量/时间越大；具体影响见每项说明。");
            w.newLine();
            section(w, "物品管道：传输速度与加速度");
            setting(w, "items.speed_limit", "1024", "所有物品管道共用的最高移动速度上限。单个管道速度不能超过此值。单位：内部速度单位/刻。提高可允许更快的管道；降低会限制所有物品管道。范围：1–32766。");
            itemSettings(w, "basic", "普通/基础物品管道");
            itemSettings(w, "copper", "铜质物品管道");
            setting(w, "items.copper.extract_amount", "1", "铜质物品管道每次从相邻容器抽取的物品数量。单位：个。提高可一次抽取更多物品。范围：1–1000000。");
            setting(w, "items.copper.extract_interval_ticks", "9", "铜质物品管道两次抽取之间的间隔。单位：游戏刻（20刻=1秒）。数值越小，抽取越频繁。范围：1–127。");
            itemSettings(w, "advanced_copper", "高级铜质物品管道");
            setting(w, "items.advanced_copper.extract_amount", "64", "高级铜质物品管道每次从相邻容器抽取的物品数量。单位：个。范围：1–1000000。");
            setting(w, "items.advanced_copper.extract_interval_ticks", "9", "高级铜质物品管道两次抽取之间的间隔。单位：游戏刻。数值越小，抽取越频繁。范围：1–127。");
            itemSettings(w, "golden", "金质物品管道");
            itemSettings(w, "iron", "铁质物品管道");
            itemSettings(w, "diamond", "钻石物品管道");
            itemSettings(w, "flint", "燧石物品管道");
            itemSettings(w, "lapis", "青金石物品管道");
            itemSettings(w, "obsidian", "黑曜石物品管道");
            itemSettings(w, "bone", "骨质物品管道");
            itemSettings(w, "network", "网络/请求/存储等网络型物品管道");
            section(w, "物品与网络请求生命周期");
            setting(w, "items.packet_lifetime_ticks", "24000", "物品在管道内部允许存在的最长时间，超时后物品包会被清除。单位：游戏刻；24000刻约20分钟。范围：1–32766。");
            setting(w, "items.network_request_timeout_ticks", "24000", "网络请求等待物品送达的最长时间，超时后请求被判定失败。单位：游戏刻。范围：1–32767。");
            setting(w, "items.scheduled_route_timeout_ticks", "1200", "网络中一条已安排路线的最长有效时间，超时后路线失效。单位：游戏刻；1200刻约1分钟。范围：1–32767。");
            setting(w, "items.routing_overflow_check_interval_ticks", "100", "路由管道检查溢出处理的间隔。单位：游戏刻。数值越小检查越频繁，但会增加检查开销。范围：1–32767。");
            setting(w, "items.recipe_crafter_poll_interval_ticks", "8", "配方管道等待合成时，轮询相邻容器是否出现目标产物的间隔。单位：游戏刻。数值越小检查越频繁，但会增加容器查询开销。范围：1–127。")
            section(w, "网络更新与缓存");
            setting(w, "network.client_cache_update_interval_ticks", "40", "网络缓存变化后，向客户端同步更新的冷却间隔。单位：游戏刻；40刻约2秒。数值越小界面数据更新越及时，但同步更频繁。范围：1–127。");
            setting(w, "network.pipe_update_scan_minimum", "100", "网络每轮随机选择待更新管道时使用的最小扫描基数，属于网络更新调度参数。数值越大，单轮更新抽样更分散。范围：1–1000000。");
            section(w, "流体管道");
            setting(w, "fluids.capacity_mb", "1000", "每一段流体管道内部可储存的流体总容量。单位：毫桶（mB）。提高可储存更多流体。范围：1–1000000000。");
            setting(w, "fluids.minimum_packet_mb", "20", "流体管道拆分/传输流体包时使用的最小包量。单位：mB。提高会减少小流体包数量，但可能延后少量流体的传输。范围：1–当前容量。");
            setting(w, "fluids.speed_thin", "256", "细流体类别的流体包目标移动速度。单位：内部速度单位/刻。范围：1–32767。");
            setting(w, "fluids.speed_normal", "128", "普通流体类别的流体包目标移动速度。单位：内部速度单位/刻。范围：1–32767。");
            setting(w, "fluids.speed_thick", "64", "浓稠流体类别的流体包目标移动速度。单位：内部速度单位/刻。范围：1–32767。");
            setting(w, "fluids.acceleration", "1", "所有流体管道中流体包每刻调整速度的幅度。数值越大，流体包越快达到目标速度。单位：内部速度单位/刻。范围：1–8192。");
            setting(w, "fluids.copper.extract_amount_mb", "125", "铜质流体管道每次从相邻容器抽取的流体量。单位：mB。范围：1–1000000000。");
            setting(w, "fluids.copper.extract_interval_ticks", "5", "铜质流体管道两次抽取之间的间隔。单位：游戏刻。数值越小，抽取越频繁。范围：1–127。");
            setting(w, "fluids.advanced_copper.extract_amount_mb", "500", "高级铜质流体管道每次从相邻容器抽取的流体量。单位：mB。范围：1–1000000000。");
            setting(w, "fluids.advanced_copper.extract_interval_ticks", "5", "高级铜质流体管道两次抽取之间的间隔。单位：游戏刻。数值越小，抽取越频繁。范围：1–127。");
            setting(w, "fluids.obsidian.gurgle_min_delay_ticks", "80", "黑曜石流体管道两次咕噜音效之间的最短间隔。单位：游戏刻。数值越大，音效越不频繁。范围：1–32767。");
            setting(w, "fluids.obsidian.gurgle_max_delay_ticks", "160", "黑曜石流体管道触发咕噜音效前允许等待的最长间隔。单位：游戏刻。达到该时间后必定播放音效；必须不小于最短间隔。范围：1–32767。");
            setting(w, "fluids.obsidian.gurgle_chance_denominator", "10", "黑曜石流体管道超过最短音效间隔后，每次检查播放音效的概率分母。默认10代表每次检查有1/10概率播放；数值越大越不容易提前播放。范围：1–1000000。")

            // Keep unrecognized user-added keys rather than silently deleting them.
            TreeMap<String, String> extra = new TreeMap<>();
            for (String key : VALUES.stringPropertyNames()) {
                if (!DEFAULTS.containsKey(key)) extra.put(key, VALUES.getProperty(key));
            }
            if (!extra.isEmpty()) {
                section(w, "未识别/自定义键（程序不会删除这些项目）");
                for (Map.Entry<String, String> entry : extra.entrySet()) {
                    w.write("# 未识别的自定义配置项，当前版本不会读取其值。");
                    w.newLine();
                    w.write(entry.getKey() + "=" + entry.getValue());
                    w.newLine();
                }
            }
        }
    }

    private static void section(BufferedWriter w, String title) throws IOException {
        w.newLine(); w.write("# ===== " + title + " ====="); w.newLine();
    }

    private static void setting(BufferedWriter w, String key, String fallback, String description) throws IOException {
        w.write("# " + description); w.newLine();
        w.write(key + "=" + VALUES.getProperty(key, fallback)); w.newLine();
    }

    private static void itemSettings(BufferedWriter w, String id, String name) throws IOException {
        setting(w, "items." + id + ".speed", id.equals("golden") || id.equals("network") ? "1024" : id.equals("advanced_copper") ? "512" : "64",
            name + "的目标移动速度。单位：内部速度单位/刻。数值越大，物品在管道中移动越快；仍受 items.speed_limit 限制。范围：1–32767。");
        String acceleration = id.equals("network") || id.equals("advanced_copper") || id.equals("golden") ? (id.equals("network") ? "64" : "16") : "1";
        setting(w, "items." + id + ".acceleration", acceleration,
            name + "的加速度，即每刻调整物品速度的幅度。数值越大，物品越快达到目标速度。单位：内部速度单位/刻。范围：1–8192。");
    }

    private static int get(String key, int fallback, int min, int max) {
        try {
            return Math.max(min, Math.min(max, Integer.parseInt(VALUES.getProperty(key, Integer.toString(fallback)).trim())));
        } catch (RuntimeException exception) { return fallback; }
    }

    private static short speed(String id, int fallback) { return (short) get("items." + id + ".speed", fallback, 1, speedLimit()); }
    private static short acceleration(String id, int fallback) { return (short) get("items." + id + ".acceleration", fallback, 1, 8192); }
    public static int speedLimit() { return get("items.speed_limit", 1024, 1, 32767); }
    public static short initialItemPacketSpeed() { return (short) get("items.initial_packet_speed", 64, 1, speedLimit()); }
    public static short basicSpeed() { return speed("basic", 64); }
    public static short basicAcceleration() { return acceleration("basic", 1); }
    public static short copperSpeed() { return speed("copper", 64); }
    public static short copperAcceleration() { return acceleration("copper", 1); }
    public static int copperExtractAmount() { return get("items.copper.extract_amount", 1, 1, 1_000_000); }
    public static int copperExtractInterval() { return get("items.copper.extract_interval_ticks", 9, 1, 127); }
    public static short advancedCopperSpeed() { return speed("advanced_copper", 512); }
    public static short advancedCopperAcceleration() { return acceleration("advanced_copper", 16); }
    public static int advancedCopperExtractAmount() { return get("items.advanced_copper.extract_amount", 64, 1, 1_000_000); }
    public static int advancedCopperExtractInterval() { return get("items.advanced_copper.extract_interval_ticks", 9, 1, 127); }
    public static short goldenSpeed() { return speed("golden", 1024); }
    public static short goldenAcceleration() { return acceleration("golden", 16); }
    public static short ironSpeed() { return speed("iron", 64); }
    public static short ironAcceleration() { return acceleration("iron", 1); }
    public static short diamondSpeed() { return speed("diamond", 64); }
    public static short diamondAcceleration() { return acceleration("diamond", 1); }
    public static short flintSpeed() { return speed("flint", 64); }
    public static short flintAcceleration() { return acceleration("flint", 1); }
    public static short lapisSpeed() { return speed("lapis", 64); }
    public static short lapisAcceleration() { return acceleration("lapis", 1); }
    public static short obsidianSpeed() { return speed("obsidian", 64); }
    public static short obsidianAcceleration() { return acceleration("obsidian", 1); }
    public static short boneSpeed() { return speed("bone", 64); }
    public static short boneAcceleration() { return acceleration("bone", 1); }
    public static short networkSpeed() { return speed("network", 1024); }
    public static short networkAcceleration() { return acceleration("network", 64); }

    public static int fluidCapacity() { return get("fluids.capacity_mb", 1000, 1, 1_000_000_000); }
    public static int fluidMinimumPacket() { return get("fluids.minimum_packet_mb", 20, 1, fluidCapacity()); }
    public static short fluidThinSpeed() { return (short) get("fluids.speed_thin", 256, 1, speedLimit()); }
    public static short fluidNormalSpeed() { return (short) get("fluids.speed_normal", 128, 1, speedLimit()); }
    public static short fluidThickSpeed() { return (short) get("fluids.speed_thick", 64, 1, speedLimit()); }
    public static short fluidAcceleration() { return (short) get("fluids.acceleration", 1, 1, 8192); }
    public static int copperFluidExtractAmount() { return get("fluids.copper.extract_amount_mb", 125, 1, 1_000_000_000); }
    public static int copperFluidExtractInterval() { return get("fluids.copper.extract_interval_ticks", 5, 1, 127); }
    public static int advancedCopperFluidExtractAmount() { return get("fluids.advanced_copper.extract_amount_mb", 500, 1, 1_000_000_000); }
    public static int advancedCopperFluidExtractInterval() { return get("fluids.advanced_copper.extract_interval_ticks", 5, 1, 127); }

    public static int itemPacketLifetimeTicks() { return get("items.packet_lifetime_ticks", 24000, 1, Short.MAX_VALUE - 1); }
    public static int networkRequestTimeoutTicks() { return get("items.network_request_timeout_ticks", 24000, 1, Short.MAX_VALUE); }
    public static int scheduledRouteTimeoutTicks() { return get("items.scheduled_route_timeout_ticks", 1200, 1, Short.MAX_VALUE); }
    public static int routingOverflowCheckIntervalTicks() { return get("items.routing_overflow_check_interval_ticks", 100, 1, Short.MAX_VALUE); }
    public static int clientCacheUpdateIntervalTicks() { return get("network.client_cache_update_interval_ticks", 40, 1, 127); }
    public static int pipeUpdateScanMinimum() { return get("network.pipe_update_scan_minimum", 100, 1, 1_000_000); }
}
