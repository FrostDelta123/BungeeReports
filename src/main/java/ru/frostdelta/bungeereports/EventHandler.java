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
import ru.frostdelta.bungeereports.gui.*;
import ru.frostdelta.bungeereports.hash.HashedLists;
import ru.frostdelta.bungeereports.spectate.SpectateManager;
import ru.frostdelta.bungeereports.utils.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandler implements Listener {

    public EventHandler(BungeeReports instance){
        plugin = instance;
        network = new Network();
    }

    private BungeeReports plugin;
    private int index;
    private Network network;
    private List<Player> mutelist = new ArrayList<Player>();
    private Map<Player, Report> reports = new HashMap<Player, Report>();
    private static Map<Integer, String> send = new HashMap<>();


    public EventHandler(HashMap<Integer, String> sender) {
        EventHandler.send = sender;
    }

    @org.bukkit.event.EventHandler(priority = EventPriority.LOW)
    public void playerDisconnect(PlayerQuitEvent e){
        if(SpectateManager.isTarget(e.getPlayer())){
            Player player = (Player)SpectateManager.getTarget().get(e.getPlayer());
            SpectateManager.spectateOff(player);
            SpectateManager.getTarget().remove(e.getPlayer());
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
            e.getPlayer().kickPlayer(Messages.BAN_MESSAGE);
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
            e.getPlayer().sendMessage(Messages.MUTE_MESSAGE);
        }
       if(report.getSender().equalsIgnoreCase(player)){
           String reason = report.getReason();
           network.addReport(player, report.getPlayer(),reason, e.getMessage());

           HashedLists.loadReports();
           e.getPlayer().sendMessage(Messages.SUCCESS_REPORT);

           e.setCancelled(true);
       }
    }

    public Map<Player, Report> getReports() {
        return reports;
    }

    @org.bukkit.event.EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        Player p = (Player) e.getWhoClicked();
        if(e.getSlotType() != InventoryType.SlotType.OUTSIDE && e.getSlotType() == InventoryType.SlotType.CONTAINER) {

            if(e.getInventory().getHolder() instanceof BanReasons && !e.getCurrentItem().getType().equals(Material.AIR)){

                if(e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(Messages.REJECT)){
                    Report currentReport = getReports().get((Player)e.getWhoClicked());
                    Network.updateReport(currentReport.getPlayer(), e.getInventory().getName(), "reject");
                    getReports().remove((Player)e.getWhoClicked());
                    p.getOpenInventory().close();
                    p.sendMessage(Messages.REPORT_REJECT);
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
                    Bukkit.getPlayer(report.getPlayer()).kickPlayer(Messages.BAN_MESSAGE);
                }
                if((Bukkit.getPlayer(report.getPlayer()) != null && type.equals("mute"))){
                    mutelist.add((Bukkit.getPlayer(report.getPlayer())));
                }
                Network.updateReport(report.getPlayer(), e.getInventory().getName(), "accept");
                p.getOpenInventory().close();
                p.sendMessage(Messages.REPORT_ACCEPT);
                HashedLists.changeCount(index);
                getReports().remove(p);

                e.setCancelled(true);
            }else

            if (e.getInventory().getHolder() instanceof PunishUI && !e.getCurrentItem().getType().equals(Material.AIR)) {
                String s = p.getOpenInventory().getItem(4).getItemMeta().getDisplayName();

                Report rep = getReports().get(e.getWhoClicked());
                switch(e.getSlot()){
                    case 2:
                        Network.updateReport(rep.getPlayer(), s, "accept");
                        getReports().remove((Player)e.getWhoClicked());
                        p.getOpenInventory().close();
                        p.sendMessage(Messages.REPORT_ACCEPT);
                        HashedLists.changeCount(index);
                        break;
                    case 6:
                        Network.updateReport(rep.getPlayer(), s, "reject");
                        getReports().remove((Player)e.getWhoClicked());
                        p.getOpenInventory().close();
                        p.sendMessage(Messages.REPORT_REJECT);
                        HashedLists.changeCount(index);
                        break;
                    case 8:
                        if(Bukkit.getServer().getPlayer(rep.getPlayer()) != null && !SpectateManager.isSpectate(p)){
                            Player target = Bukkit.getServer().getPlayer(rep.getPlayer());
                            SpectateManager.setSpectate(p,target);
                            p.getOpenInventory().close();
                        }
                        break;
                }

                e.setCancelled(true);
            }

            if (e.getInventory().getHolder() instanceof GetReportsUI && !e.getCurrentItem().getType().equals(Material.AIR)) {

                PunishUI PunishUI = new PunishUI();
                BanReasons banReasons = new BanReasons();
                Report rp = new Report();
                String s = send.get(e.getSlot());
                if(plugin.isModUsed() && plugin.isModEnabled()) {
                    ScreenManager.getScreenshot(e.getCurrentItem().getItemMeta().getDisplayName(), p);
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

            if (e.getInventory().getHolder() instanceof UserUI && !e.getCurrentItem().getType().equals(Material.AIR)) {

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
                    p.sendMessage(ChatColor.RED + Messages.REPORT_UNSUCCESS);
                }
            }

            if (e.getInventory().getHolder() instanceof ReasonsUI && !e.getCurrentItem().getType().equals(Material.AIR)) {

                Report report = getReports().get((Player)e.getWhoClicked());
                if (!plugin.getConfig().getBoolean("comments")) {

                    if(plugin.isModUsed() && plugin.isModEnabled()) {
                        ScreenManager.addScreenshot(Bukkit.getPlayer(report.getPlayer()), p);
                    }
                    network.addReport(e.getWhoClicked().getName(), report.getPlayer(), e.getCurrentItem().getItemMeta().getDisplayName(), "");
                    HashedLists.addReport(e.getWhoClicked().getName(), report.getPlayer(), e.getCurrentItem().getItemMeta().getDisplayName(), "");
                    getReports().remove((Player)e.getWhoClicked());
                    e.getWhoClicked().getOpenInventory().close();
                    p.sendMessage(Messages.SUCCESS_REPORT);

                } else {
                    report.setReason(e.getCurrentItem().getItemMeta().getDisplayName());
                    e.getWhoClicked().getOpenInventory().close();
                    e.getWhoClicked().sendMessage(Messages.CHAT_COMMENT);
                }

                e.setCancelled(true);
            }
        }
    }
}
