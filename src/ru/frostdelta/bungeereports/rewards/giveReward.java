package ru.frostdelta.bungeereports.rewards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.loader;
import ru.frostdelta.bungeereports.modules.vaultLoader;

public class giveReward {

    private loader plugin;


    public giveReward(loader instance){

        plugin = instance;

    }

    public void getReward(Player p){



        if(plugin.vaultEnabled){

            vaultLoader vaultLoader = new vaultLoader();

            vaultLoader.economy.depositPlayer(p, plugin.getRewardAmount());

            p.sendMessage(ChatColor.GREEN + "Вам была выдана награда в размере " + plugin.getRewardAmount() + " за корректное использование репорт-системы!");

        }else plugin.getLogger().info("Vault выключен, выдача наград невозможна!");

    }

}
