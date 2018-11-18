package ru.frostdelta.bungeereports;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.frostdelta.bungeereports.executor.Executor;
import ru.frostdelta.bungeereports.gui.BanReasons;
import ru.frostdelta.bungeereports.gui.PunishUI;
import ru.frostdelta.bungeereports.gui.ReasonsUI;
import ru.frostdelta.bungeereports.hash.HashedLists;
import ru.frostdelta.bungeereports.holders.*;
import ru.frostdelta.bungeereports.spectate.SpectateManager;
import ru.frostdelta.bungeereports.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler implements Listener {

    public EventHandler(Loader instance){

        plugin = instance;
        network = new Network();
    }

    private Loader plugin;
    private int index;
    private Network network;
    private final UpdateReport update = new UpdateReport();
    private final SpectateManager spectateManager = new SpectateManager();
    private List<Player> mutelist = new ArrayList<Player>();
    private Map<Player, Report> reports = new HashMap<Player, Report>();
    private static Map<Integer, String> send = new HashMap<>();


    public EventHandler(HashMap<Integer, String> sender) {
        EventHandler.send = sender;
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.LOW)
    public void playerDisconnect(PlayerQuitEvent e){
        if(spectateManager.isTarget(e.getPlayer())){
            Player player = (Player)spectateManager.getTarget().get(e.getPlayer());
            spectateManager.spectateOff(player);
            spectateManager.getTarget().remove(e.getPlayer());
            player.sendMessage(ChatColor.RED + "Player leave the game");
        }
        if(mutelist.contains(e.getPlayer())){
            mutelist.remove(e.getPlayer());
        }
    }


    //Сделать нормальные enum с типами банов, пока хуйня
    @org.bukkit.event.EventHandler
    public void checkBan(PlayerJoinEvent e){
        String type = network.checkBan(e.getPlayer().getName());
        if(type.equalsIgnoreCase("ban") || type.equalsIgnoreCase("tempban")){
            e.getPlayer().kickPlayer(Utils.BAN_MESSAGE);
        }
        if(type.equalsIgnoreCase("mute")){
            mutelist.add(e.getPlayer());
        }
    }

    @org.bukkit.event.EventHandler
    public void onLogout(PlayerQuitEvent e) {
        if (Executor.getRequestQueue().containsKey(e.getPlayer().getName())) {
            Executor.getActionQueue().remove(e.getPlayer().getName());
        }
    }

    @org.bukkit.event.EventHandler
    public void asyncChatEvent(AsyncPlayerChatEvent e){
        Report report = getReports().get(e.getPlayer());
        String player = e.getPlayer().getName();
        if (mutelist.contains(e.getPlayer())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(Utils.MUTE_MESSAGE);
        }
       if(report.getSender().equalsIgnoreCase(player)){
           String reason = report.getReason();
           network.addReport(player, report.getPlayer(),reason, e.getMessage());

           HashedLists.loadReports();
           e.getPlayer().sendMessage(Utils.SUCCESS_REPORT);

           e.setCancelled(true);
       }
    }

    public Map<Player, Report> getReports() {
        return reports;
    }

    @org.bukkit.event.EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        ScreenManager screenManager = new ScreenManager();
        Player p = (Player) e.getWhoClicked();
        if(e.getSlotType() != InventoryType.SlotType.OUTSIDE && e.getSlotType() == InventoryType.SlotType.CONTAINER) {

            if(e.getInventory().getHolder() instanceof BanReasonsHolder && !e.getCurrentItem().getType().equals(Material.AIR)){

                if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Utils.REJECT)){
                    Report currentReport = getReports().get((Player)e.getWhoClicked());
                    update.updateReport(currentReport.getPlayer(), e.getInventory().getName(), "reject");
                    getReports().remove((Player)e.getWhoClicked());
                    p.getOpenInventory().close();
                    p.sendMessage(Utils.REPORT_REJECT);
                    HashedLists.changeCount(index);
                    e.setCancelled(true);
                    return;
                }

                long time = BanReasons.getAPI().get(e.getCurrentItem().getItemMeta().getDisplayName()).getTime() * 60;
                String type = BanReasons.getAPI().get(e.getCurrentItem().getItemMeta().getDisplayName()).getType();
                time += System.currentTimeMillis()/1000;
                Report report = getReports().get((Player)e.getWhoClicked());
                network.addBan(report.getPlayer(), System.currentTimeMillis()/1000,time, type);
                if(Bukkit.getPlayer(report.getPlayer()) != null && type.equals("ban") || type.equals("tempban")){
                    Bukkit.getPlayer(report.getPlayer()).kickPlayer(Utils.BAN_MESSAGE);
                }
                if((Bukkit.getPlayer(report.getPlayer()) != null && type.equals("mute"))){
                    mutelist.add((Bukkit.getPlayer(report.getPlayer())));
                }
                update.updateReport(report.getPlayer(), e.getInventory().getName(), "accept");
                p.getOpenInventory().close();
                p.sendMessage(Utils.REPORT_ACCEPT);
                HashedLists.changeCount(index);
                getReports().remove(p);

                e.setCancelled(true);
            }else

            if (e.getInventory().getHolder() instanceof PunishHolder && !e.getCurrentItem().getType().equals(Material.AIR)) {
                String s = p.getOpenInventory().getItem(4).getItemMeta().getDisplayName();
                SpectateManager sp = new SpectateManager();
                Report rep = getReports().get((Player)e.getWhoClicked());
                switch(e.getSlot()){
                    case 2:
                        update.updateReport(rep.getPlayer(), s, "accept");
                        getReports().remove((Player)e.getWhoClicked());
                        p.getOpenInventory().close();
                        p.sendMessage(Utils.REPORT_ACCEPT);
                        HashedLists.changeCount(index);
                        break;
                    case 6:
                        update.updateReport(rep.getPlayer(), s, "reject");
                        getReports().remove((Player)e.getWhoClicked());
                        p.getOpenInventory().close();
                        p.sendMessage(Utils.REPORT_REJECT);
                        HashedLists.changeCount(index);
                        break;
                    case 8:
                        if(Bukkit.getServer().getPlayer(rep.getPlayer()) != null && !sp.isSpectate(p)){
                            Player target = Bukkit.getServer().getPlayer(rep.getPlayer());
                            sp.setSpectate(p,target);
                            p.getOpenInventory().close();
                        }
                        break;
                }

                e.setCancelled(true);
            }

            if (e.getInventory().getHolder() instanceof GetReportsHolder && !e.getCurrentItem().getType().equals(Material.AIR)) {

                PunishUI PunishUI = new PunishUI();
                BanReasons banReasons = new BanReasons();
                Report rp = new Report();
                String s = send.get(e.getSlot());
                if(plugin.isModUsed() && plugin.isModEnabled()) {
                    screenManager.getScreenshot(e.getCurrentItem().getItemMeta().getDisplayName(), p);
                }
                rp.setAdmin(e.getWhoClicked().getName());
                rp.setPlayer(e.getCurrentItem().getItemMeta().getDisplayName());
                getReports().put((Player)e.getWhoClicked(), rp);

                if(!plugin.isBanSystemUsed()) {
                    PunishUI.openGUI(e.getCurrentItem().getItemMeta().getDisplayName(), p, s);
                }else{
                    banReasons.openGUI(p, s);
                }
                index = e.getSlot();

                e.setCancelled(true);
            }

            if (e.getInventory().getHolder() instanceof UserHolder && !e.getCurrentItem().getType().equals(Material.AIR)) {

                if (!plugin.getWhitelist().contains(e.getCurrentItem().getItemMeta().getDisplayName())) {

                    ReasonsUI ReasonsUI = new ReasonsUI(plugin);
                    Report curentReport = new Report();
                    curentReport.setSender(e.getWhoClicked().getName());
                    curentReport.setPlayer(e.getCurrentItem().getItemMeta().getDisplayName());

                    p.getOpenInventory().close();
                    ReasonsUI.openGUI(p);
                    getReports().put((Player)e.getWhoClicked(),curentReport);
                    e.setCancelled(true);
                } else {
                    p.getOpenInventory().close();
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.RED + Utils.REPORT_UNSUCCESS);
                }
            }

            if (e.getInventory().getHolder() instanceof ReasonHolder && !e.getCurrentItem().getType().equals(Material.AIR)) {

                Report report = getReports().get((Player)e.getWhoClicked());
                if (!plugin.getConfig().getBoolean("comments")) {

                    if(plugin.isModUsed() && plugin.isModEnabled()) {
                        screenManager.addScreenshot(Bukkit.getPlayer(report.getPlayer()), p);
                    }
                    network.addReport(e.getWhoClicked().getName(), report.getPlayer(), e.getCurrentItem().getItemMeta().getDisplayName(), "");
                    HashedLists.addReport(e.getWhoClicked().getName(), report.getPlayer(), e.getCurrentItem().getItemMeta().getDisplayName(), "");
                    getReports().remove((Player)e.getWhoClicked());
                    e.getWhoClicked().getOpenInventory().close();
                    p.sendMessage(Utils.SUCCESS_REPORT);

                } else {
                    report.setReason(e.getCurrentItem().getItemMeta().getDisplayName());
                    e.getWhoClicked().getOpenInventory().close();
                    e.getWhoClicked().sendMessage(Utils.CHAT_COMMENT);
                }

                e.setCancelled(true);
            }
        }
    }
}
