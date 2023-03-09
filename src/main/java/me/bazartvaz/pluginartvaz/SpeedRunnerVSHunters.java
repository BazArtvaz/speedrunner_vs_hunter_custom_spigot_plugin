package me.bazartvaz.pluginartvaz;


import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class SpeedRunnerVSHunters implements CommandExecutor {

    public static List<Player> hunters = new ArrayList<>();
    public static List<Player> speedrunnders = new ArrayList<>();
    public static List<Player> players = new ArrayList<>();
    Plugin plug = Bukkit.getPluginManager().getPlugin("PluginArtvaz");
    public static List<Player> freezePlayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!players.contains(player)) {
                players.add(player);
            }

            if (command.getName().equals("hunter")) {
                hunters.add(player);
                if (speedrunnders.contains(player)) {
                    speedrunnders.remove(player);
                }
                player.sendTitle(ChatColor.RED + "Теперь ты охотник", "");
            }

            if (command.getName().equals("speedrunner")) {
                speedrunnders.add(player);
                if (hunters.contains(player)) {
                    hunters.remove(player);
                }
                player.sendTitle(ChatColor.GREEN + "Теперь ты спидранер", "");
            }

            if (command.getName().equals("startgame")) {
                startGame();
            }
        }
        return true;
    }

    public void defultStartPosition() {
        try {
            for (Player p : players) {
                p.setHealth(20);
                p.setLevel(0);
                p.setFoodLevel(20);
                p.getInventory().clear();
                p.updateInventory();
                p.teleport(speedrunnders.get(0));
                freezePlayers.add(p);
                p.setWalkSpeed(0.2F);
                p.setFlySpeed(0.2F);
                p.getWorld().setTime(0);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void startGame() {
        try {
            defultStartPosition();
            freezePlayers.remove(speedrunnders.get(0));

            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            final int[] TimerID = {0};
            final int[] times = {30};

            TimerID[0] = scheduler.scheduleSyncRepeatingTask(plug, new Runnable() {
                @Override
                public void run() {
                    if (times[0] % 5 == 0 || times[0] < 6) {
                        speedrunnders.get(0).sendMessage("У тебя осталось " + times[0] + " секунд!");
                    }
                    for (Player p : hunters) {
                        p.sendTitle("" + times[0], "");
                    }
                    times[0]--;
                    if (times[0] == 0) {
                        speedrunnders.get(0).sendMessage("Охота началась");

                        for (Player p : hunters) {
                            getCompass(p);
                            freezePlayers.remove(p);
                            p.sendTitle("Охота началась", "");
                        }
                        setCompassLocation();
                        Bukkit.getScheduler().cancelTask(TimerID[0]);
                    }
                }
            }, 0, 20L);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getCompass(Player player) {
        Player target = speedrunnders.get(0);
        player.setItemInHand(new ItemStack(Material.COMPASS));
        player.setCompassTarget(target.getLocation());
    }

    public void setCompassLocation() {
        final Location[] lastlocation = {new Location(speedrunnders.get(0).getWorld(), 0, 0, 0)};
        new BukkitRunnable() {
            public void run() {
                Player target = speedrunnders.get(0);
                for (Player player: hunters) {
                    if (player.isOnline() && target.isOnline() && target.getWorld().getName().equals("world")) {
                        player.setCompassTarget(target.getLocation());
                        lastlocation[0] = target.getLocation();
                    } else {
                        player.setCompassTarget(lastlocation[0]);
                    }
                }
            }
        }.runTaskTimer(plug, 50L, 50L);
    }
}

