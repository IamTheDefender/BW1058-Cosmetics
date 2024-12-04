package xyz.iamthedefender.cosmetics.api.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Run {

    private final static JavaPlugin plugin = Utility.getPlugin();

    public static BukkitTask delayed(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public static BukkitTask delayedAsync(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public static BukkitTask async(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static BukkitTask sync(Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public static BukkitTask every(Runnable runnable, long time){
        return Bukkit.getScheduler().runTaskTimer(plugin, runnable, time, time);
    }

    public static void every(BukkitRunnable runnable, long time){
        runnable.runTaskTimer(plugin, time, time);
    }

    public static BukkitTask every(Consumer<BukkitRunnable> runnableConsumer, long time){
        return new BukkitRunnable(){
            @Override
            public void run() {
                runnableConsumer.accept(this);
            }
        }.runTaskTimer(plugin, 0L, time);
    }

    public static BukkitTask everyAsync(Consumer<BukkitRunnable> runnableConsumer, long time){
        return new BukkitRunnable(){
            @Override
            public void run() {
                runnableConsumer.accept(this);
            }
        }.runTaskTimerAsynchronously(plugin, 0L, time);
    }
 
    public static BukkitTask everyAsync(Runnable runnable, long time){
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, time, time);
    }

    public static BukkitTask every(Runnable runnable, long time, int iterations){
        AtomicInteger counter = new AtomicInteger(0);
        return every((r) -> {
            if(counter.incrementAndGet() == iterations){
                counter.set(0);
                r.cancel();
                return;
            }

            runnable.run();
        }, time);
    }

    public static BukkitTask everyAsync(Runnable runnable, long time, int iterations){
        AtomicInteger counter = new AtomicInteger(0);
        return everyAsync((r) -> {
            if(counter.incrementAndGet() == iterations){
                counter.set(0);
                r.cancel();
                return;
            }

            runnable.run();
        }, time);
    }
}
