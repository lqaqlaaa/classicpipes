package jagm.classicpipes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Shared gameplay configuration for every loader.
 *
 * The same properties file and keys are used by Fabric, Forge and NeoForge:
 * config/classicpipes.properties
 */
public final class PipeConfig {
    private static final Path CONFIG_PATH = Path.of("config", "classicpipes.properties");
    private static final Properties VALUES = new Properties();

    private PipeConfig() {}

    static {
        load();
    }

    private static void load() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            if (Files.exists(CONFIG_PATH)) {
                try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
                    VALUES.load(input);
                }
            }
            boolean changed = false;
            for (String key : defaults().stringPropertyNames()) {
                if (!VALUES.containsKey(key)) {
                    VALUES.setProperty(key, defaults().getProperty(key));
                    changed = true;
                }
            }
            if (changed || !Files.exists(CONFIG_PATH)) {
                save();
            }
        } catch (IOException exception) {
            ClassicPipes.LOGGER.error("Unable to load Classic Pipes configuration; built-in defaults will be used.", exception);
        }
    }

    private static void save() throws IOException {
        Properties defaults = defaults();
        try (OutputStream output = Files.newOutputStream(CONFIG_PATH)) {
            VALUES.store(output, "Classic Pipes gameplay configuration. Values are shared by Fabric, Forge and NeoForge.");
        }
    }

    private static Properties defaults() {
        Properties p = new Properties();
        p.setProperty("items.speed_limit", "1024");
        p.setProperty("items.basic.speed", "64");
        p.setProperty("items.basic.acceleration", "1");
        p.setProperty("items.copper.speed", "64");
        p.setProperty("items.copper.acceleration", "1");
        p.setProperty("items.copper.extract_amount", "1");
        p.setProperty("items.copper.extract_interval_ticks", "9");
        p.setProperty("items.advanced_copper.speed", "512");
        p.setProperty("items.advanced_copper.acceleration", "16");
        p.setProperty("items.advanced_copper.extract_amount", "64");
        p.setProperty("items.advanced_copper.extract_interval_ticks", "9");
        p.setProperty("items.golden.speed", "1024");
        p.setProperty("items.golden.acceleration", "16");
        p.setProperty("items.iron.speed", "64");
        p.setProperty("items.iron.acceleration", "1");
        p.setProperty("items.diamond.speed", "64");
        p.setProperty("items.diamond.acceleration", "1");
        p.setProperty("items.flint.speed", "64");
        p.setProperty("items.flint.acceleration", "1");
        p.setProperty("items.lapis.speed", "64");
        p.setProperty("items.lapis.acceleration", "1");
        p.setProperty("items.obsidian.speed", "64");
        p.setProperty("items.obsidian.acceleration", "1");
        p.setProperty("items.bone.speed", "64");
        p.setProperty("items.bone.acceleration", "1");
        p.setProperty("items.network.speed", "1024");
        p.setProperty("items.network.acceleration", "64");
        p.setProperty("fluids.capacity_mb", "1000");
        p.setProperty("fluids.minimum_packet_mb", "20");
        p.setProperty("fluids.basic.speed_thin", "256");
        p.setProperty("fluids.basic.speed_normal", "128");
        p.setProperty("fluids.basic.speed_thick", "64");
        p.setProperty("fluids.basic.acceleration", "1");
        p.setProperty("fluids.copper.extract_amount_mb", "125");
        p.setProperty("fluids.copper.extract_interval_ticks", "5");
        p.setProperty("fluids.advanced_copper.extract_amount_mb", "500");
        p.setProperty("fluids.advanced_copper.extract_interval_ticks", "5");
        return p;
    }

    private static int get(String key, int fallback, int min, int max) {
        String value = VALUES.getProperty(key);
        if (value == null) return fallback;
        try {
            return Math.clamp(Integer.parseInt(value.trim()), min, max);
        } catch (NumberFormatException exception) {
            ClassicPipes.LOGGER.warn("Invalid Classic Pipes config value '{}' for {}; using {}.", value, key, fallback);
            return fallback;
        }
    }

    public static int itemSpeedLimit() { return get("items.speed_limit", 1024, 1, 4096); }
    public static int itemBasicSpeed() { return get("items.basic.speed", 64, 1, 4096); }
    public static int itemBasicAcceleration() { return get("items.basic.acceleration", 1, 1, 4096); }
    public static int itemCopperSpeed() { return get("items.copper.speed", 64, 1, 4096); }
    public static int itemCopperAcceleration() { return get("items.copper.acceleration", 1, 1, 4096); }
    public static int itemCopperExtractAmount() { return get("items.copper.extract_amount", 1, 1, 999999); }
    public static int itemCopperInterval() { return get("items.copper.extract_interval_ticks", 9, 1, 127); }
    public static int itemAdvancedCopperSpeed() { return get("items.advanced_copper.speed", 512, 1, 4096); }
    public static int itemAdvancedCopperAcceleration() { return get("items.advanced_copper.acceleration", 16, 1, 4096); }
    public static int itemAdvancedCopperExtractAmount() { return get("items.advanced_copper.extract_amount", 64, 1, 999999); }
    public static int itemAdvancedCopperInterval() { return get("items.advanced_copper.extract_interval_ticks", 9, 1, 127); }
    public static int itemGoldenSpeed() { return get("items.golden.speed", 1024, 1, 4096); }
    public static int itemGoldenAcceleration() { return get("items.golden.acceleration", 16, 1, 4096); }
    public static int itemIronSpeed() { return get("items.iron.speed", 64, 1, 4096); }
    public static int itemIronAcceleration() { return get("items.iron.acceleration", 1, 1, 4096); }
    public static int itemDiamondSpeed() { return get("items.diamond.speed", 64, 1, 4096); }
    public static int itemDiamondAcceleration() { return get("items.diamond.acceleration", 1, 1, 4096); }
    public static int itemFlintSpeed() { return get("items.flint.speed", 64, 1, 4096); }
    public static int itemFlintAcceleration() { return get("items.flint.acceleration", 1, 1, 4096); }
    public static int itemLapisSpeed() { return get("items.lapis.speed", 64, 1, 4096); }
    public static int itemLapisAcceleration() { return get("items.lapis.acceleration", 1, 1, 4096); }
    public static int itemObsidianSpeed() { return get("items.obsidian.speed", 64, 1, 4096); }
    public static int itemObsidianAcceleration() { return get("items.obsidian.acceleration", 1, 1, 4096); }
    public static int itemBoneSpeed() { return get("items.bone.speed", 64, 1, 4096); }
    public static int itemBoneAcceleration() { return get("items.bone.acceleration", 1, 1, 4096); }
    public static int itemNetworkSpeed() { return get("items.network.speed", 1024, 1, 4096); }
    public static int itemNetworkAcceleration() { return get("items.network.acceleration", 64, 1, 4096); }
    public static int fluidCapacity() { return get("fluids.capacity_mb", 1000, 1, 100000000); }
    public static int fluidMinimumPacket() { return get("fluids.minimum_packet_mb", 20, 1, 100000000); }
    public static int fluidThinSpeed() { return get("fluids.basic.speed_thin", 256, 1, 4096); }
    public static int fluidNormalSpeed() { return get("fluids.basic.speed_normal", 128, 1, 4096); }
    public static int fluidThickSpeed() { return get("fluids.basic.speed_thick", 64, 1, 4096); }
    public static int fluidAcceleration() { return get("fluids.basic.acceleration", 1, 1, 4096); }
    public static int fluidCopperExtractAmount() { return get("fluids.copper.extract_amount_mb", 125, 1, 100000000); }
    public static int fluidCopperInterval() { return get("fluids.copper.extract_interval_ticks", 5, 1, 127); }
    public static int fluidAdvancedCopperExtractAmount() { return get("fluids.advanced_copper.extract_amount_mb", 500, 1, 100000000); }
    public static int fluidAdvancedCopperInterval() { return get("fluids.advanced_copper.extract_interval_ticks", 5, 1, 127); }
}
