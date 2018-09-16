package ru.frostdelta.bungeereports;

import com.avaje.ebean.EbeanServer;
import com.google.common.io.ByteArrayDataOutput;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import ru.endlesscode.inspector.bukkit.plugin.PluginLifecycle;
import ru.frostdelta.bungeereports.chat.ChatLogger;
import ru.frostdelta.bungeereports.executor.Executor;
import ru.frostdelta.bungeereports.hash.HashedLists;
import ru.frostdelta.bungeereports.modules.VaultLoader;
import ru.frostdelta.bungeereports.pluginMessage.AntiCheat;
import ru.frostdelta.bungeereports.pluginMessage.Dump;
import ru.frostdelta.bungeereports.pluginMessage.PluginMessage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FrostDelta123
 */

public class Loader extends PluginLifecycle {


    Loader plugin = this;

    Network db = new Network();
    Executor executor = new Executor(this);
    private boolean vaultEnabled;
    private boolean rewardsEnabled;
    private boolean customEnabled;
    private boolean limitEnabled;
    private boolean uuid;
    private boolean spectateEnabled;
    private boolean debugEnabled;
    private boolean bungee;
    private boolean modEnabled;
    private boolean isModUsed;
    private boolean banSystemUsed;
    private List<String> whitelist = new ArrayList<String>();

    public int rewardAmount;
    public int customRewardAmount;
    private FileConfiguration log;

    @Override
    public void onEnable(){


        this.saveDefaultConfig();
        File chatlog = new File(this.getDataFolder(), "chatlog.yml");
        if(!chatlog.exists()){
            try {
                chatlog.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log = YamlConfiguration.loadConfiguration(chatlog);

        vaultEnabled = getConfig().getBoolean("vault.enabled");
        rewardsEnabled = getConfig().getBoolean("reward.enabled");
        customEnabled = getConfig().getBoolean("customreward.enabled");
        limitEnabled = getConfig().getBoolean("limit.enabled");
        rewardAmount = getConfig().getInt("reward.amount");
        customRewardAmount = getConfig().getInt("customreward.amount");
        uuid = getConfig().getBoolean("customreward.uuid");
        whitelist = getConfig().getStringList("whitelist");
        spectateEnabled = getConfig().getBoolean("spectate");
        debugEnabled = getConfig().getBoolean("debug");
        db.url = getConfig().getString("url");
        db.username = getConfig().getString("username");
        db.password = getConfig().getString("password");
        bungee = getConfig().getBoolean("bungee.enabled");
        executor.bungee = getConfig().getBoolean("bungee.enabled");
        modEnabled = getConfig().getBoolean("mod.enabled");
        isModUsed = getConfig().getBoolean("mod.use");
        banSystemUsed = getConfig().getBoolean("ban.enabled");

        /*try {
            db.openConnection();
        } catch (SQLException e) {
            getLogger().severe("ERROR! Cant load SQL, check config!");
            getLogger().severe("PLUGIN DISABLED");
            getLogger().severe("Set debug to true in config.yml");

            if(isDebugEnabled()){
                e.printStackTrace();
            }
            plugin.setEnabled(false);
            return;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        db.createDB();
        HashedLists.loadReports();
*/
        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    db.openConnection();
                    db.createDB();
                    HashedLists.loadReports();
                    autoUnban();
                } catch (SQLException e) {
                    getLogger().severe("ERROR! Cant load SQL, check config!");
                    getLogger().severe("PLUGIN DISABLED");
                    getLogger().severe("Set debug to true in config.yml");

                    if(isDebugEnabled()){
                        e.printStackTrace();
                    }
                    plugin.setEnabled(false);
                    return;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        if(isBungee()) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new PluginMessage(this));

        }else getLogger().info("BungeeCord disabled");

        if(isModEnabled()){
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "AntiCheat");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "AntiCheat", new AntiCheat(this));

            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "Dump");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "Dump", new Dump(this));
        }else getLogger().info("Mod disabled");

        if(isVaultEnabled()){

            VaultLoader VaultLoader = new VaultLoader();
            VaultLoader.setupChat();
            VaultLoader.setupEconomy();
            VaultLoader.setupPermissions();

        }else {
            getLogger().info("Vault disabled!");
            getLogger().info("Выдача наград невозможна!");
        }

        getServer().getPluginManager().registerEvents(new EventHandler(this), this);
        getServer().getPluginManager().registerEvents(new ChatLogger(this), this);
        try {
            getCommand("report").setExecutor(executor);
            getCommand("getreports").setExecutor(executor);
            getCommand("br").setExecutor(executor);
            getCommand("spectate").setExecutor(executor);
            getCommand("spectateoff").setExecutor(executor);
            getCommand("screen").setExecutor(executor);
            getCommand("getscreens").setExecutor(executor);
            getCommand("dump").setExecutor(executor);
            getCommand("getdump").setExecutor(executor);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

    }

    public FileConfiguration getLogConfig() {
        return log;
    }

    public void autoUnban(){
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis()/1000;
                db.autoUnban(time);
            }
        }, 0L, 1200L);
    }

    @Override
    public EbeanServer getDatabase() {
        return null;
    }


    public void loadConfig(){

        vaultEnabled = getConfig().getBoolean("vault.enabled");
        rewardsEnabled = getConfig().getBoolean("reward.enabled");
        customEnabled = getConfig().getBoolean("customreward.enabled");
        limitEnabled = getConfig().getBoolean("limit.enabled");
        rewardAmount = getConfig().getInt("reward.amount");
        customRewardAmount = getConfig().getInt("customreward.amount");
        uuid = getConfig().getBoolean("customreward.uuid");
        whitelist = getConfig().getStringList("whitelist");
        spectateEnabled = getConfig().getBoolean("spectate");
        debugEnabled = getConfig().getBoolean("debug");
        db.url = getConfig().getString("url");
        db.username = getConfig().getString("username");
        db.password = getConfig().getString("password");
        bungee = getConfig().getBoolean("bungee.enabled");
        executor.bungee = getConfig().getBoolean("bungee.enabled");
        modEnabled = getConfig().getBoolean("mod.enabled");
        isModUsed = getConfig().getBoolean("mod.use");
        banSystemUsed = getConfig().getBoolean("ban.enabled");
    }


    public void sendDump(Player p, ByteArrayDataOutput buffer) {
        p.sendPluginMessage(plugin, "Dump", buffer.toByteArray());
        if(plugin.isDebugEnabled()){
            Bukkit.broadcastMessage(ChatColor.RED + "Сообщение моду отпралвено");
        }

    }

    public void sendMessage(Player p, ByteArrayDataOutput buffer) {
        p.sendPluginMessage(plugin, "AntiCheat", buffer.toByteArray());
        if(plugin.isDebugEnabled()){
            Bukkit.broadcastMessage(ChatColor.RED + "Сообщение моду отпралвено");
        }

    }

    public Boolean isBanSystemUsed(){
        return banSystemUsed;
    }

    public Boolean isModUsed(){
        return isModUsed;
    }

    public Boolean isModEnabled(){
        return modEnabled;
    }

    public Boolean isVaultEnabled(){
        return vaultEnabled;
    }

    public List<String> getWhitelist(){
        return whitelist;
    }

    public Boolean isBungee(){
        return bungee;
    }

    public Boolean isDebugEnabled(){
        return debugEnabled;
    }

    public String getPassword(){

        return getConfig().getString("password");

    }

    public String getUsername(){

        return getConfig().getString("username");

    }

    public String getUrl(){

        return getConfig().getString("url");

    }

    public boolean isUuid() {

        return uuid;

    }

    public int getCustomRewardAmount(){

        return customRewardAmount;
    }

    public int getRewardAmount(){

        return rewardAmount;
    }

    public boolean isLimitEnabled() {

        return limitEnabled;
    }

    public boolean isRewardsEnabled() {

        return rewardsEnabled;
    }

    public boolean isCustomEnabled() {

        return customEnabled;
    }

    public boolean isSpectateEnabled() {
        return spectateEnabled;
    }

    public void onDisable(){
    }

}
