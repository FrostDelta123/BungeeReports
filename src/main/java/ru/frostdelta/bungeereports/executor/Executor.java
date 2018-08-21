package ru.frostdelta.bungeereports.executor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.CanReport;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.NonBungee;
import ru.frostdelta.bungeereports.gui.GetReportsUI;
import ru.frostdelta.bungeereports.hash.HashedLists;
import ru.frostdelta.bungeereports.pluginMessage.GetPlayerCount;
import ru.frostdelta.bungeereports.spectate.SpectateManager;

import java.util.ArrayList;
import java.util.List;

public class Executor extends SpectateManager implements CommandExecutor {

    private Loader plugin;


    public Executor(Loader instance){

        plugin = instance;

    }
    public boolean bungee;
    private static List<Player> senders = new ArrayList<>();
    private boolean isBungee(){
        return bungee;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String st, String[] args) {

        if(cmd.getName().equalsIgnoreCase("br") && args.length == 1 && args[0].equals("reload")){

            plugin.loadConfig();
            plugin.getConfig().options().copyHeader(true);

            plugin.loadConfig();

            s.sendMessage(ChatColor.GREEN + "Конфиг перезагружен!");
            return true;
        }

        if(s instanceof Player && plugin.isEnabled()) {
            if (cmd.getName().equalsIgnoreCase("getreports")) {

                GetReportsUI getReportsUI = new GetReportsUI(plugin);

                getReportsUI.openGUI((Player) s,
                        HashedLists.getTotalRepors(),
                        HashedLists.getSenderList(),
                        HashedLists.getReasonList(),
                        HashedLists.getReportList(),
                        HashedLists.getCommentList());
                return true;
            }

            if(cmd.getName().equalsIgnoreCase("spectateoff")){
                if(isSpectate((Player)s)){
                    spectateOff((Player)s);
                }else s.sendMessage(ChatColor.RED + "Ошибка. Вы ни за кем не наблюдаете!");
                return true;
            }

            if(cmd.getName().equalsIgnoreCase("spectate") && args.length == 1 && plugin.isSpectateEnabled()){

                if(plugin.getServer().getPlayer(args[0]) != null){
                    setSpectate((Player)s, plugin.getServer().getPlayer(args[0]));
                }else s.sendMessage(ChatColor.DARK_RED + "Игрок не найден!");
                return true;
            }

            if (cmd.getName().equalsIgnoreCase("report")) {

                senders.add((Player)s);
                GetPlayerCount GetPlayerCount = new GetPlayerCount(plugin);
                NonBungee NonBungee = new NonBungee(plugin);
                CanReport CanReport = new CanReport(plugin);

                CanReport.needReward(s.getName());

                if (plugin.isLimitEnabled() && CanReport.limit(s.getName())) {

                    if (isBungee()) {

                        GetPlayerCount.sendMessage((Player) s);

                    } else NonBungee.getNonBungeePlayerlist((Player) s);
                }
                return true;
            }
        }else plugin.getLogger().severe("For players only!");
        return true;
    }

    public static List<Player> getSenders(){
        return senders;
    }
}
