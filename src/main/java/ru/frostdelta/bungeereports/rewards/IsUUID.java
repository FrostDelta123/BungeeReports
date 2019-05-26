package ru.frostdelta.bungeereports.rewards;

import org.bukkit.Bukkit;
import ru.frostdelta.bungeereports.BungeeReports;
import ru.frostdelta.bungeereports.Network;

import java.util.UUID;

class IsUUID {

    static void getUUID(String table, String money, String playerCol, String player) {
        BungeeReports plugin = BungeeReports.inst();
        UUID uuid = Bukkit.getPlayer(player).getUniqueId();
        String url = plugin.getUrl();
        String username = plugin.getUsername();
        String password = plugin.getPassword();
        Network.customReward(table, money, playerCol, plugin.getCustomRewardAmount(), uuid.toString(), url,username, password);

    }

}
