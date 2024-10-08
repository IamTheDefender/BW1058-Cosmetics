package xyz.iamthedefender.cosmetics.util;

import org.bukkit.Bukkit;

public class VersionSupportUtil {

    private static final String PACKAGE_NAME = Bukkit.getServer().getClass().getPackage().getName();
    private static final String SERVER_VERSION = PACKAGE_NAME.substring(PACKAGE_NAME.lastIndexOf('.') + 1)
            .replace("_", ".").replace("v", "")
            .replaceAll("\\.R\\d+$", "");

    public static boolean isHigherThan(String version) {
        return compareVersions(SERVER_VERSION, version) > 0;
    }

    public static boolean isLowerThan(String version) {
        return compareVersions(SERVER_VERSION, version) < 0;
    }

    public static String getVersion() {
        return SERVER_VERSION;
    }

    private static int compareVersions(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int length = Math.max(parts1.length, parts2.length);

        for (int i = 0; i < length; i++) {
            int part1 = i < parts1.length ? Integer.parseInt(parts1[i]) : 0;
            int part2 = i < parts2.length ? Integer.parseInt(parts2[i]) : 0;

            if (part1 < part2) {
                return -1;
            }
            if (part1 > part2) {
                return 1;
            }
        }

        return 0;
    }
}
