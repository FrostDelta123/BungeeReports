package ru.frostdelta.bungeereports.spectate;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SpectateManager {


    private static Map<Entity, Player> targetMap = new HashMap<Entity, Player>();

    public static void spectateOff(Player p){
        p.setGameMode(GameMode.SURVIVAL);
        getTarget().remove(p.getSpectatorTarget());
        p.sendMessage(ChatColor.RED + "Наблюдение выключено!");

    }

    public static boolean isSpectate(Player p){
        if(p.getGameMode() == GameMode.SPECTATOR){
            return true;
        } return false;
    }

    public static void setSpectate(Player p, Player tar){

        p.setGameMode(GameMode.SPECTATOR);
        p.setSpectatorTarget(tar);
        getTarget().put(p, tar);
        p.sendMessage(ChatColor.GOLD + "Вы наблюдаете за игроком: " + tar.getName());
        p.sendMessage(ChatColor.GOLD + "Для отмены пропишите /spectateoff");
    }

    public static boolean isTarget(Player p){
        return getTarget().containsKey(p);
    }

    public static Map getTarget(){
        return targetMap;
    }

}
