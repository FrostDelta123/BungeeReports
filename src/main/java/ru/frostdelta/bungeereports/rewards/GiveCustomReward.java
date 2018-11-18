package ru.frostdelta.bungeereports.rewards;

import org.bukkit.Bukkit;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.Network;
import ru.frostdelta.bungeereports.utils.Utils;

public class GiveCustomReward {

    public void giveCustomReward(String player){

        Loader plugin = Loader.inst();
        Network Network = new Network();

        String table = plugin.getConfig().getString("customreward.table");
        String nameCol = plugin.getConfig().getString("customreward.namecoloumn");
        String moneyCol = plugin.getConfig().getString("customreward.moneycoloumn");
        String url = plugin.getConfig().getString("url");
        String username = plugin.getConfig().getString("username");
        String password = plugin.getConfig().getString("password");

        if (!plugin.isUuid()){

            Network.customReward(table, moneyCol, nameCol, plugin.getCustomRewardAmount(), player, url,username, password);

        }else {
            IsUUID IsUUID = new IsUUID();
            IsUUID.getUUID(table, moneyCol, nameCol, player);
        }

        Bukkit.getPlayer(player).sendMessage(Utils.REWARD_MESSAGE + plugin.getRewardAmount());

    }

}
