package ru.frostdelta.bungeereports.rewards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import ru.frostdelta.bungeereports.loader;
import ru.frostdelta.bungeereports.network;

public class giveCustomReward {

    private loader plugin;

    public giveCustomReward(loader instance){

        plugin = instance;

    }

    network network = new network();

    public void giveCustomReward(String player){

        String table = plugin.getConfig().getString("customreward.table");
        String nameCol = plugin.getConfig().getString("customreward.namecoloumn");
        String moneyCol = plugin.getConfig().getString("customreward.moneycoloumn");
        String url = plugin.getConfig().getString("url");
        String username = plugin.getConfig().getString("username");
        String password = plugin.getConfig().getString("password");

        if (!plugin.isUuid()){

            network.customReward(table, moneyCol, nameCol, plugin.getCustomRewardAmount(), player, url,username, password);

        }else {
            isUUID isUUID = new isUUID(plugin);
            isUUID.getUUID(table, moneyCol, nameCol, player);
        }

        Bukkit.getPlayer(player).sendMessage(ChatColor.GREEN + "Вам была выдана награда в размере " + plugin.getRewardAmount() + " за корректное использование репорт-системы!");

    }

}
