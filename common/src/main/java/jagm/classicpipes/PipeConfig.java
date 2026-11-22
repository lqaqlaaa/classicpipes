package jagm.classicpipes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Shared configuration for Classic Pipes. This class deliberately uses only
 * Java APIs so the same settings work on NeoForge, Forge and Fabric.
 */
public final class PipeConfig {
    private static final Path FILE = Path.of("config", "classicpipes.properties");
    private static final Properties VALUES = new Properties();

    static {
        load();
    }

    private PipeConfig() {}

    private static void load() {
        Properties defaults = defaults();
        try {
            Files.createDirectories(FILE.getParent());
            if (Files.exists(FILE)) {
                try (InputStream input = Files.newInputStream(FILE)) {
                    VALUES.load(input);
                }
            }
            boolean changed = false;
            for (String key : defaults.stringPropertyNames()) {
                if (!VALUES.containsKey(key)) {
                    VALUES.setProperty(key, defaults.getProperty(key));
                    changed = true;
                }
            }
            if (changed || !Files.exists(FILE)) {
                save();
            }
        } catch (IOException exception) {
            VALUES.putAll(defaults);
            java.util.logging.Logger.getLogger("Classic Pipes").warning(
                "Could not read config/classicpipes.properties; using built-in defaults: " + exception.getMessage()
            );
        }
    }

    private static void save() throws IOException {
        try (OutputStream output = Files.newOutputStream(FILE)) {
            VALUES.store(output, "Classic Pipes shared gameplay configuration (NeoForge / Forge / Fabric). Restart after editing.");
        }
    }

    private static Properties defaults() {
        Properties p = new Properties();
        p.setProperty("items.speed_limit", "1024");
        addItem(p, "basic", 64, 1);
        addItem(p, "copper", 64, 1);
        p.setProperty("items.copper.extract_amount", "1");
        p.setProperty("items.copper.extract_interval_ticks", "9");
        addItem(p, "advanced_copper", 512, 16);
        p.setProperty("items.advanced_copper.extract_amount", "64");
        p.setProperty("items.advanced_copper.extract_interval_ticks", "9");
        addItem(p, "golden", 1024, 16);
        addItem(p, "iron", 64, 1);
        addItem(p, "diamond", 64, 1);
        addItem(p, "flint", 64, 1);
        addItem(p, "lapis", 64, 1);
        addItem(p, "obsidian", 64, 1);
        addItem(p, "bone", 64, 1);
        addItem(p, "network", 1024, 64);

        p.setProperty("fluids.capacity_mb", "1000");
        p.setProperty("fluids.minimum_packet_mb", "20");
        p.setProperty("fluids.speed_thin", "256");
        p.setProperty("fluids.speed_normal", "128");
        p.setProperty("fluids.speed_thick", "64");
        p.setProperty("fluids.acceleration", "1");
        p.setProperty("fluids.copper.extract_amount_mb", "125");
        p.setProperty("fluids.copper.extract_interval_ticks", "5");
        p.setProperty("fluids.advanced_copper.extract_amount_mb", "500");
        p.setProperty("fluids.advanced_copper.extract_interval_ticks", "5");
        return p;
    }

    private static void addItem(Properties p, String id, int speed, int acceleration) {
        p.setProperty("items." + id + ".speed", Integer.toString(speed));
        p.setProperty("items." + id + ".acceleration", Integer.toString(acceleration));
    }

    private static int get(String key, int fallback, int min, int max) {
        try {
            return Math.max(min, Math.min(max, Integer.parseInt(VALUES.getProperty(key, Integer.toString(fallback)).trim())));
        } catch (RuntimeException exception) {
            return fallback;
        }
    }

    private static short speed(String id, int fallback) {
        return (short) get("items." + id + ".speed", fallback, 1, speedLimit());
    }

    private static short acceleration(String id, int fallback) {
        return (short) get("items." + id + ".acceleration", fallback, 1, 8192);
    }

    public static int speedLimit() { return get("items.speed_limit", 1024, 1, 8192); }
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
}
