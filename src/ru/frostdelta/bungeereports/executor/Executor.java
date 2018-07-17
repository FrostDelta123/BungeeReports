package ru.frostdelta.bungeereports.executor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.CanReport;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.gui.GetReportsUI;
import ru.frostdelta.bungeereports.Network;
import ru.frostdelta.bungeereports.NonBungee;
import ru.frostdelta.bungeereports.pluginMessage.GetPlayerCount;

public class Executor implements CommandExecutor {

    private Loader plugin;


    public Executor(Loader instance){

        plugin = instance;

    }
    public boolean bungee;

    private boolean isBungee(){
        return bungee;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String st, String[] args) {

        if(cmd.getName().equalsIgnoreCase("br") && args.length == 1 && args[0].equals("reload")){

            plugin.reloadConfig();
            plugin.getConfig().options().copyHeader(true);

            plugin.vaultEnabled = plugin.getConfig().getBoolean("vault.enabled");
            plugin.rewardsEnabled = plugin.getConfig().getBoolean("reward.enabled");
            plugin.customEnabled = plugin.getConfig().getBoolean("customreward.enabled");
            plugin.limitEnabled = plugin.getConfig().getBoolean("limit.enabled");
            plugin.rewardAmount = plugin.getConfig().getInt("reward.amount");
            plugin.customRewardAmount = plugin.getConfig().getInt("customreward.amount");
            plugin.uuid = plugin.getConfig().getBoolean("customreward.uuid");
            plugin.whitelist = plugin.getConfig().getStringList("whitelist");


            s.sendMessage(ChatColor.GREEN + "Конфиг перезагружен!");
        }

        if(cmd.getName().equalsIgnoreCase("getreports")){

            Network Network = new Network();
            GetReportsUI getReportsUI = new GetReportsUI(plugin);


            //сколько запросов в 1 момент, охуеть оптимизация, каюсь, исправлю
            getReportsUI.openGUI((Player)s,
                    Network.totalReports(),
                    Network.reportList("sender"),
                    Network.reportList("reason"),
                    Network.reportList("player"),
                    Network.reportList("comment"));


        }

        if(cmd.getName().equalsIgnoreCase("report")) {

            GetPlayerCount GetPlayerCount = new GetPlayerCount(plugin);
            NonBungee NonBungee = new NonBungee(plugin);
            CanReport CanReport = new CanReport(plugin);

            CanReport.needReward(s.getName());

            if(plugin.isLimitEnabled() && CanReport.limit(s.getName())) {

                if (isBungee()) {

                    GetPlayerCount.sendMessage((Player) s);

                } else NonBungee.getNonBungeePlayerlist((Player) s);
            }

        }

        return true;
    }
}
