package ru.frostdelta.bungeereports;

import org.bukkit.Bukkit;
import ru.frostdelta.bungeereports.rewards.GiveCustomReward;
import ru.frostdelta.bungeereports.rewards.GiveReward;

public class CanReport {

    public void needReward(String player){
        Loader plugin = Loader.inst();
        int reportCount = Network.playerReports(player, "accept");
        int customReportsNeed = plugin.getConfig().getInt("customreward.reportsneed");
        int reportsneed = plugin.getConfig().getInt("reward.reportsneed");
            if(plugin.isRewardsEnabled() && reportCount >= reportsneed){
                GiveReward GiveReward = new GiveReward();
                GiveReward.getReward(Bukkit.getPlayer(player));
                Network.purge(player, "accept");
            }else {
                if(plugin.isCustomEnabled() && reportCount >= customReportsNeed) {
                    GiveCustomReward GiveCustomReward = new GiveCustomReward();
                    GiveCustomReward.giveCustomReward(player);
                    Network.purge(player, "accept");
                }
            }
        }


    public boolean limit(String player){
        Loader plugin = Loader.inst();
        return Network.playerReports(player, "no") >= plugin.getConfig().getInt("limit.limit");
    }

}
