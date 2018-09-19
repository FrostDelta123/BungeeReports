package ru.frostdelta.bungeereports.rewards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.modules.VaultLoader;
import ru.frostdelta.bungeereports.utils.Utils;

public class GiveReward {

    private Loader plugin;


    public GiveReward(Loader instance){

        plugin = instance;

    }

    public void getReward(Player p){

        if(plugin.isVaultEnabled()){

            VaultLoader.economy.depositPlayer(p, plugin.getRewardAmount());

            p.sendMessage(Utils.REWARD_MESSAGE + plugin.getRewardAmount());

        }else plugin.getLogger().info("Vault выключен, выдача наград невозможна!");

    }

}
