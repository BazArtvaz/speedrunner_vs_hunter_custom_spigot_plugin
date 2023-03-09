package me.bazartvaz.pluginartvaz;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.entity.Player;

public class ChatListener implements Listener {

    public ChatListener() {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("Привет " + player.getName() + "!");
    }

    @EventHandler
    public void onPlayerGoSleep(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        player.sendTitle("НУ И ЧЕ ТЫ СПИШЬ!","ВСТАВАЙ БЛ*ТЬ");

    }

    @EventHandler
    public void onPlayerWakeUp(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        player.sendTitle("ПОРА НА ЗАВОД!","");
    }

    @EventHandler
    public void freezePlayerMove(PlayerMoveEvent event) {
        if (SpeedRunnerVSHunters.freezePlayers.contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void freezePlayerBreakBlock(BlockBreakEvent event) {
        if (SpeedRunnerVSHunters.freezePlayers.contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void playerDie(PlayerDeathEvent event) {
        if (SpeedRunnerVSHunters.speedrunnders.contains(event.getEntity())){
            for (Player p : SpeedRunnerVSHunters.hunters) {
                p.sendTitle(ChatColor.GREEN + "Вы выиграли!"  , "");
            }
            for (Player p : SpeedRunnerVSHunters.speedrunnders) {
                p.sendTitle(ChatColor.RED + "Ты проиграл!" , "Ультраплох");
            }
        }
    }

    @EventHandler
    public void playerSpawn(PlayerRespawnEvent event) {
        if (SpeedRunnerVSHunters.hunters.contains(event.getPlayer())){
            SpeedRunnerVSHunters.getCompass(event.getPlayer());
        }
    }


    @EventHandler
    public void onPlayerSwitchWorld(PlayerChangedWorldEvent event) {
//      world, world_nether, world_the_end
        Player player = event.getPlayer();
        String world = player.getWorld().getName();

        if (event.getFrom().getName().equalsIgnoreCase("world_the_end") && world.equalsIgnoreCase("world")){
            for (Player p : SpeedRunnerVSHunters.hunters) {
                p.sendTitle(ChatColor.RED + "Вы проиграли!"  , "Ультраплох");
            }
            for (Player p : SpeedRunnerVSHunters.speedrunnders) {
                p.sendTitle(ChatColor.GREEN + "Ты выиграл!" , "Мегахарош");
            }
        }else {
            switch (world) {
                case "world" -> player.sendTitle(ChatColor.GREEN + "С ВОЗВРАЩЕНИЕМ НА ЗАВОД!", "");
                case "world_nether" -> player.sendTitle(ChatColor.RED + "ДОБРО ПОЖАЛОВАТЬ В АД",
                        ChatColor.RED + "" + ChatColor.MAGIC + "ПРОСТО ТЕКСТ ДЛЯ ПРИКОЛА");
                case "world_the_end" -> player.sendTitle(ChatColor.DARK_PURPLE + "" + "КОНЕЦ?",
                        ChatColor.DARK_PURPLE + "НЕТ БЛ*ТЬ НАЧАЛО");
            }
        }
    }
}
