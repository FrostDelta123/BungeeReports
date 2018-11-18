package ru.frostdelta.bungeereports.rewards;

import org.bukkit.Bukkit;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.Network;

import java.util.UUID;

public class IsUUID {

    private final Network Network = new Network();

    public void getUUID(String table, String money, String playerCol, String player) {
        Loader plugin = Loader.inst();

        UUID uuid = Bukkit.getPlayer(player).getUniqueId();

        String url = plugin.getUrl();
        String username = plugin.getUsername();
        String password = plugin.getPassword();

        Network.customReward(table, money, playerCol, plugin.getCustomRewardAmount(), uuid.toString(), url,username, password);

    }

}
