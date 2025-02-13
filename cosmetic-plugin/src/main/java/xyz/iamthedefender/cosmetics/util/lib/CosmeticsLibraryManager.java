package xyz.iamthedefender.cosmetics.util.lib;

import net.byteflux.libby.LibraryManager;
import net.byteflux.libby.classloader.URLClassLoaderHelper;
import net.byteflux.libby.logging.adapters.JDKLogAdapter;
import xyz.iamthedefender.cosmetics.CosmeticsPlugin;

import java.io.File;
import java.net.URLClassLoader;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public class CosmeticsLibraryManager extends LibraryManager {

    private final URLClassLoaderHelper classLoader;

    public CosmeticsLibraryManager(CosmeticsPlugin plugin) {
        super(new JDKLogAdapter(requireNonNull(plugin, "plugin").getLogger()), new File(plugin.getHandler().getAddonPath()).toPath(), "lib");
        classLoader = new URLClassLoaderHelper((URLClassLoader) plugin.getClass().getClassLoader(), this);
    }

    @Override
    protected void addToClasspath(Path file) {
        classLoader.addToClasspath(file);
    }

}
