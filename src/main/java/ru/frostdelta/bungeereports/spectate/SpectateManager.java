package ru.frostdelta.bungeereports.spectate;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.ScreenManager;
import ru.frostdelta.bungeereports.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class SpectateManager {

    Loader plugin;
    public SpectateManager(Loader instance){
        plugin = instance;
    }

    private Map<Entity, Player> targetMap = new HashMap<Entity, Player>();

    public void spectateOff(Player p){

        p.setGameMode(GameMode.SURVIVAL);
        getTarget().remove(p.getSpectatorTarget());
        p.sendMessage(Utils.SPECTATE_TOGGLE_OFF);

    }

    public boolean isSpectate(Player p){

        return p.getGameMode() == GameMode.SPECTATOR;

    }

    public void setSpectate(Player p, Player tar){

        p.setGameMode(GameMode.SPECTATOR);
        p.setSpectatorTarget(tar);
        getTarget().put(p, tar);
        p.sendMessage(Utils.SPECTATE_PLAYER + tar.getName());
        p.sendMessage(ChatColor.GOLD + "Для отмены пропишите /spectateoff");
    }

    public boolean isTarget(Entity p){
        return getTarget().containsKey(p);
    }

    public Map getTarget(){
        return targetMap;
    }

}
