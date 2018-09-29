package ru.frostdelta.bungeereports.executor;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.CanReport;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.Network;
import ru.frostdelta.bungeereports.NonBungee;
import ru.frostdelta.bungeereports.action.Action;
import ru.frostdelta.bungeereports.chat.PasteBinAPI;
import ru.frostdelta.bungeereports.gui.GetReportsUI;
import ru.frostdelta.bungeereports.hash.HashedLists;
import ru.frostdelta.bungeereports.pluginMessage.GetPlayerCount;
import ru.frostdelta.bungeereports.spectate.SpectateManager;
import ru.frostdelta.bungeereports.utils.Utils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Executor implements CommandExecutor {

    private static Map<String, Action> actionQueue = new HashMap<>();
    private static Map<String, String> requestQueue = new HashMap<>();
    private static Map<Player, Action> actionDump = new HashMap<>();

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
    public boolean onCommand(CommandSender s, Command cmd, String st, String[] args){

        Network db = new Network(plugin);
        Player player = (Player) s;

        if(cmd.getName().equalsIgnoreCase("getlogs") && args.length == 2){
            try {
                File log = new File(plugin.getDataFolder().getAbsolutePath() + "/logs/" + args[0] + "/" + args[1] + ".txt");
                FileInputStream fstream = new FileInputStream(log);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine;
                while ((strLine = br.readLine()) != null){
                    s.sendMessage(strLine);
                }
                br.close();
                log.delete();
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }else

        if(cmd.getName().equalsIgnoreCase("getdump") && args.length == 1){

            if(plugin.getConfig().getString("mod.dump-type").equalsIgnoreCase("download")) {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                File file = new File(plugin.getDataFolder().getAbsolutePath() + "/dump/" + offlinePlayer.getUniqueId().toString() + ".txt");
                if (!file.exists()) {
                    s.sendMessage(Utils.DUMP_NOT_FOUND);
                    return true;
                }
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(Action.DUMPS.getActionName());
                byte[] fileContent = new byte[0];
                try {
                    fileContent = Files.readAllBytes(file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.writeUTF(file.getName());
                out.write(fileContent);
                plugin.sendDump(player, out);
                file.delete();
            }else
                if(plugin.getConfig().getString("mod.dump-type").equalsIgnoreCase("haste")){
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                    List<String> dumpList = new ArrayList<String>();
                    try {
                        FileInputStream fstream = new FileInputStream(plugin.getDataFolder().getAbsolutePath() + "/dump/" + offlinePlayer.getUniqueId().toString() + ".txt");
                        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                        String strLine;
                        while ((strLine = br.readLine()) != null){
                           dumpList.add(strLine);
                        }
                        s.sendMessage(PasteBinAPI.post(dumpList));
                    } catch (FileNotFoundException e) {
                        s.sendMessage(Utils.DUMP_NOT_FOUND);
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            return true;
        }else

        if(cmd.getName().equalsIgnoreCase("dump")){
            if(args.length == 1 && Bukkit.getServer().getPlayer(args[0]) != null){

              Player dumped = Bukkit.getServer().getPlayer(args[0]);
              requestQueue.put(player.getName(), player.getName());
              actionQueue.put(player.getName(), Action.PROCESS);
              ByteArrayDataOutput out = ByteStreams.newDataOutput();
              out.writeUTF(Action.PROCESS.getActionName());
              plugin.sendDump(dumped, out);
              s.sendMessage(Utils.DUMP_CREATED);
              return true;
            }else s.sendMessage(Utils.DUMP_COMMAND_ERROR);
        }else

        if (cmd.getName().equalsIgnoreCase("report")) {
            senders.add((Player)s);
            GetPlayerCount getPlayerCount = new GetPlayerCount(plugin);
            NonBungee nonBungee = new NonBungee(plugin);
            CanReport canReport = new CanReport(plugin);

            canReport.needReward(s.getName());

            if (!plugin.isLimitEnabled() || plugin.isLimitEnabled() && !canReport.limit(s.getName())) {

                if (isBungee()) {
                    getPlayerCount.sendMessage((Player) s);
                } else nonBungee.getNonBungeePlayerlist((Player) s);
            }
            return true;
        }else

        if (cmd.getName().equalsIgnoreCase("screen") && Bukkit.getPlayer(args[0]) != null) {
            Player targetPlayer = plugin.getServer().getPlayer(args[0]);
            if (targetPlayer.isOnline()) {
                if (cmd.getName().equalsIgnoreCase("screen")) {
                    requestQueue.put(targetPlayer.getName(), player.getName());
                    actionQueue.put(targetPlayer.getName(), Action.SCREENSHOT);
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF(Action.SCREENSHOT.getActionName());
                    out.writeUTF(plugin.getConfig().getString("mod.clientID"));
                    plugin.sendMessage(targetPlayer, out);
                    return true;
                }
            } else {
               sendMessage(player, "&cИгрок " + targetPlayer + " оффлайн!");
            }
        }else

        if (cmd.getName().equalsIgnoreCase("getscreens")) {

            String screenshots = db.getScreenshots(args[0]);
            if (!screenshots.isEmpty()) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF(Action.SCREENSHOTS.getActionName());
                out.writeUTF(args[0]);
                out.writeUTF(db.getScreenshots(args[0]));
                plugin.sendMessage(player, out);
                return true;
            } else {
                player.sendMessage(Utils.SCREEN_CMMAND_ERROR);
            }
        }else

        if(cmd.getName().equalsIgnoreCase("br") && args.length == 1 && args[0].equals("reload")){

            plugin.loadConfig();
            plugin.getConfig().options().copyHeader(true);

            plugin.loadConfig();

            s.sendMessage(Utils.CONFIG_RELOADED);
            return true;
        }else

        if(s instanceof Player && plugin.isEnabled()) {
            SpectateManager spectateManager = new SpectateManager(plugin);
            if (cmd.getName().equalsIgnoreCase("getreports")) {

                GetReportsUI getReportsUI = new GetReportsUI(plugin);

                getReportsUI.openGUI((Player) s,
                        HashedLists.getTotalRepors(),
                        HashedLists.getSenderList(),
                        HashedLists.getReasonList(),
                        HashedLists.getReportList(),
                        HashedLists.getCommentList());
                return true;
            }else

            if(cmd.getName().equalsIgnoreCase("spectateoff")){
                if(spectateManager.isSpectate((Player)s)){
                    spectateManager.spectateOff((Player)s);
                }else s.sendMessage(Utils.SPECTATE_ERROR);
                return true;
            }else

            if(cmd.getName().equalsIgnoreCase("spectate") && args.length == 1 && plugin.isSpectateEnabled()){

                if(plugin.getServer().getPlayer(args[0]) != null){
                    spectateManager.setSpectate((Player)s, plugin.getServer().getPlayer(args[0]));
                }else s.sendMessage(Utils.PLAYER_NOT_FOUND);
                return true;
            }

        }else plugin.getLogger().severe("For players only!");
        return true;
    }

    public static Map<Player, Action> getActionDump(){
        return actionDump;
    }

    public static Map<String, Action> getActionQueue() {
        return actionQueue;
    }

    public static Map<String, String> getRequestQueue() {
        return requestQueue;
    }

    public void sendMessage(Player p, String msg) {
        p.sendMessage(("&f[&bBungeeReports&f] " + msg).replace("&", "§"));
    }


    public static List<Player> getSenders(){
        return senders;
    }
}
