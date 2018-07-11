package ru.frostdelta.bungeereports.rewards;

import org.bukkit.Bukkit;
import ru.frostdelta.bungeereports.loader;
import ru.frostdelta.bungeereports.network;

import java.util.UUID;

public class isUUID {

    private loader plugin;

    public isUUID(loader instance){

        plugin = instance;

    }

    network network = new network();

    public void getUUID(String table, String money, String playerCol, String player) {

        UUID uuid = Bukkit.getPlayer(player).getUniqueId();

        String url = plugin.getUrl();
        String username = plugin.getUsername();
        String password = plugin.getPassword();

        network.customReward(table, money, playerCol, plugin.getCustomRewardAmount(), uuid.toString(), url,username, password);

    }

}
