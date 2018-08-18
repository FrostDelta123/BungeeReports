package ru.frostdelta.bungeereports.rewards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.modules.VaultLoader;

public class GiveReward {

    private Loader plugin;


    public GiveReward(Loader instance){

        plugin = instance;

    }

    public void getReward(Player p){



        if(plugin.isVaultEnabled()){

            VaultLoader.economy.depositPlayer(p, plugin.getRewardAmount());

            p.sendMessage(ChatColor.GREEN + "Вам была выдана награда в размере " + plugin.getRewardAmount() + " за корректное использование репорт-системы!");

        }else plugin.getLogger().info("Vault выключен, выдача наград невозможна!");

    }

}
