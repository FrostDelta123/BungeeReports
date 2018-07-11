package ru.frostdelta.bungeereports;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.gui.openUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class nonBungee {

    private loader plugin;

    public nonBungee(loader instance){

        plugin = instance;

    }

    openUI openUI = new openUI(plugin);

    public void getNonBungeePlayerlist(Player player){

        List<String> players = new ArrayList<String>();

        for(Player p : plugin.getServer().getOnlinePlayers()){

            players.add(p.getName());

        }

        String[] strings = players.stream().toArray(String[]::new);

        openUI.openGUI(player, players.size(), strings);

    }

}
