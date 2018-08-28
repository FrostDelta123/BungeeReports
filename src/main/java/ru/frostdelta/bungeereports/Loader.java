package ru.frostdelta.bungeereports;

import com.avaje.ebean.EbeanServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.endlesscode.inspector.bukkit.plugin.PluginLifecycle;
import ru.frostdelta.bungeereports.executor.Executor;
import ru.frostdelta.bungeereports.hash.HashedLists;
import ru.frostdelta.bungeereports.modules.VaultLoader;
import ru.frostdelta.bungeereports.pluginMessage.PluginMessage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Loader extends PluginLifecycle {

    /**
     *
     * @author FrostDelta123
     */


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
    private List<String> whitelist = new ArrayList<String>();

    public int rewardAmount;
    public int customRewardAmount;


    @Override
    public void onEnable(){


        this.saveDefaultConfig();

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
        getCommand("report").setExecutor(executor);
        getCommand("getreports").setExecutor(executor);
        getCommand("br").setExecutor(executor);
        getCommand("spectate").setExecutor(executor);
        getCommand("spectateoff").setExecutor(executor);
    }


    @Override
    public EbeanServer getDatabase() {
        return null;
    }


    public void loadConfig(){
        this.vaultEnabled = plugin.getConfig().getBoolean("vault.enabled");
        this.rewardsEnabled = plugin.getConfig().getBoolean("reward.enabled");
        this.customEnabled = plugin.getConfig().getBoolean("customreward.enabled");
        this.limitEnabled = plugin.getConfig().getBoolean("limit.enabled");
        this.rewardAmount = plugin.getConfig().getInt("reward.amount");
        this.customRewardAmount = plugin.getConfig().getInt("customreward.amount");
        this.uuid = plugin.getConfig().getBoolean("customreward.uuid");
        this.whitelist = plugin.getConfig().getStringList("whitelist");
        this.spectateEnabled = plugin.getConfig().getBoolean("spectate");
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
