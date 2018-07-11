package ru.frostdelta.bungeereports;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import ru.frostdelta.bungeereports.rewards.giveCustomReward;
import ru.frostdelta.bungeereports.rewards.giveReward;

public class canReport {



    private loader plugin;
    network network = new network();



    public canReport(loader instance){

        plugin = instance;

    }

    public void needReward(String player){

        int reportCount = network.playerReports(player, "accept");

        int customReportsNeed = plugin.getConfig().getInt("customreward.reportsneed");
        int reportsneed = plugin.getConfig().getInt("reward.reportsneed");



            if(plugin.isRewardsEnabled() && reportCount >= reportsneed){
                giveReward giveReward = new giveReward(plugin);

                giveReward.getReward(Bukkit.getPlayer(player));
                network.purge(player, "accept");
            }else {

                if(plugin.isCustomEnabled() && reportCount >= customReportsNeed) {
                    giveCustomReward giveCustomReward = new giveCustomReward(plugin);

                    giveCustomReward.giveCustomReward(player);
                    network.purge(player, "accept");
                }
            }

        }


    public boolean limit(String player){

        if(network.playerReports(player, "no") >= plugin.getConfig().getInt("limit.limit")){
            return false;
        }
        return true;
    }

}
