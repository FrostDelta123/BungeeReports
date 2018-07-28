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
import ru.frostdelta.bungeereports.hash.HashedLists;
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

        if(s instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("getreports")) {

                GetReportsUI getReportsUI = new GetReportsUI(plugin);



                getReportsUI.openGUI((Player) s,
                        HashedLists.getTotalRepors(),
                        HashedLists.getSenderList(),
                        HashedLists.getReasonList(),
                        HashedLists.getReportList(),
                        HashedLists.getCommentList());


            }

            if (cmd.getName().equalsIgnoreCase("report")) {

                GetPlayerCount GetPlayerCount = new GetPlayerCount(plugin);
                NonBungee NonBungee = new NonBungee(plugin);
                CanReport CanReport = new CanReport(plugin);

                CanReport.needReward(s.getName());

                if (plugin.isLimitEnabled() && CanReport.limit(s.getName())) {

                    if (isBungee()) {

                        GetPlayerCount.sendMessage((Player) s);

                    } else NonBungee.getNonBungeePlayerlist((Player) s);
                }

            }
        }else plugin.getLogger().severe("For players only!");
        return true;
    }
}
