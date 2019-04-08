package ru.frostdelta.bungeereports.rewards;

import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.modules.VaultLoader;
import ru.frostdelta.bungeereports.utils.Messages;

public class GiveReward {

    public void getReward(Player p){
        Loader plugin = Loader.inst();
        if(plugin.isVaultEnabled()){
            VaultLoader.economy.depositPlayer(p, plugin.getRewardAmount());
            p.sendMessage(Messages.REWARD_MESSAGE + plugin.getRewardAmount());
        }else plugin.getLogger().info("Vault выключен, выдача наград невозможна!");

    }

}
