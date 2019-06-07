package ru.frostdelta.bungeereports;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.gui.MainInterface;

import java.util.ArrayList;
import java.util.List;

public class NonBungee {

    public void getNonBungeePlayerlist(Player player){
        BungeeReports plugin = BungeeReports.inst();
        List<String> players = new ArrayList<String>();

        for(Player p : plugin.getServer().getOnlinePlayers()){
            players.add(p.getName());
        }
        String[] strings = players.stream().toArray(String[]::new);

        if(player.hasPermission("bungeereports.player")){
            player.openInventory(new MainInterface(player, players.size(), strings).create());
        }else player.sendMessage(ChatColor.RED + "У вас нет прав!");
    }

}
