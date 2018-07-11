package ru.frostdelta.bungeereports;

import org.bukkit.Bukkit;
import ru.frostdelta.bungeereports.rewards.GiveCustomReward;
import ru.frostdelta.bungeereports.rewards.GiveReward;

public class CanReport {



    private Loader plugin;
    Network Network = new Network();



    public CanReport(Loader instance){

        plugin = instance;

    }

    public void needReward(String player){

        int reportCount = Network.playerReports(player, "accept");

        int customReportsNeed = plugin.getConfig().getInt("customreward.reportsneed");
        int reportsneed = plugin.getConfig().getInt("reward.reportsneed");



            if(plugin.isRewardsEnabled() && reportCount >= reportsneed){
                GiveReward GiveReward = new GiveReward(plugin);

                GiveReward.getReward(Bukkit.getPlayer(player));
                Network.purge(player, "accept");
            }else {

                if(plugin.isCustomEnabled() && reportCount >= customReportsNeed) {
                    GiveCustomReward GiveCustomReward = new GiveCustomReward(plugin);

                    GiveCustomReward.giveCustomReward(player);
                    Network.purge(player, "accept");
                }
            }

        }


    public boolean limit(String player){

        if(Network.playerReports(player, "no") >= plugin.getConfig().getInt("limit.limit")){
            return false;
        }
        return true;
    }

}
