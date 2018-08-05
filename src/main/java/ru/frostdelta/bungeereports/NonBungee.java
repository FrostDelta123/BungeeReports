package ru.frostdelta.bungeereports;

import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.gui.UserUI;

import java.util.ArrayList;
import java.util.List;

public class NonBungee {

    private Loader plugin;

    public NonBungee(Loader instance){

        plugin = instance;

    }

    private UserUI UserUI = new UserUI(plugin);

    public void getNonBungeePlayerlist(Player player){

        List<String> players = new ArrayList<String>();

        for(Player p : plugin.getServer().getOnlinePlayers()){

            players.add(p.getName());

        }

        String[] strings = players.stream().toArray(String[]::new);

        if(player.hasPermission("bungeereports.player")){
            player.openInventory(UserUI.openGUI(player, players.size(), strings));
        }
    }

}
