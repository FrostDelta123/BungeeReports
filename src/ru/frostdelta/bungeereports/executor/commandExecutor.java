package ru.frostdelta.bungeereports.executor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.frostdelta.bungeereports.canReport;
import ru.frostdelta.bungeereports.gui.moderUI;
import ru.frostdelta.bungeereports.loader;
import ru.frostdelta.bungeereports.network;
import ru.frostdelta.bungeereports.nonBungee;
import ru.frostdelta.bungeereports.pluginMessage.getPlayerCount;
import ru.frostdelta.bungeereports.pluginMessage.pluginMessage;

public class commandExecutor implements CommandExecutor {

    private loader plugin;


    public commandExecutor(loader instance){

        plugin = instance;

    }
    public boolean bungee;

    private boolean isBungee(){
        return bungee;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String st, String[] args) {

        if(cmd.getName().equalsIgnoreCase("getreports")){

            network network = new network();
            moderUI moderUI = new moderUI(plugin);


            //сколько запросов в 1 момент, охуеть оптимизация, каюсь, исправлю
            moderUI.openGUI((Player)s,
                    network.totalReports(),
                    network.reportList("sender"),
                    network.reportList("reason"),
                    network.reportList("player"),
                    network.reportList("comment"));


        }

        if(cmd.getName().equalsIgnoreCase("report")) {

            getPlayerCount getPlayerCount = new getPlayerCount(plugin);
            nonBungee nonBungee = new nonBungee(plugin);
            canReport canReport = new canReport(plugin);

            canReport.needReward(s.getName());

            if(plugin.isLimitEnabled() && canReport.limit(s.getName())) {

                if (isBungee()) {

                    getPlayerCount.sendMessage((Player) s);

                } else nonBungee.getNonBungeePlayerlist((Player) s);
            }

        }

        return true;
    }
}
