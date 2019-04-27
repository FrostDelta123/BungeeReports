package ru.frostdelta.bungeereports.spectate;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.utils.Messages;

import java.util.HashMap;
import java.util.Map;

public class SpectateManager {

    private static Map<Entity, Player> targetMap = new HashMap<Entity, Player>();

    public static void spectateOff(Player p){

        p.setGameMode(GameMode.SURVIVAL);
        getTarget().remove(p.getSpectatorTarget());
        p.sendMessage(Messages.SPECTATE_TOGGLE_OFF);
    }

    public static boolean isSpectate(Player p){

        return p.getGameMode() == GameMode.SPECTATOR;

    }

    public static void setSpectate(Player p, Player tar){

        p.setGameMode(GameMode.SPECTATOR);
        p.setSpectatorTarget(tar);
        SpectateManager.getTarget().put(p, tar);
        p.sendMessage(Messages.SPECTATE_PLAYER + tar.getName());
        p.sendMessage(ChatColor.GOLD + "Для отмены пропишите /spectateoff");
    }

    public static boolean isTarget(Entity p){
        return getTarget().containsKey(p);
    }

    public static Map<Entity, Player> getTarget(){
        return targetMap;
    }

}
