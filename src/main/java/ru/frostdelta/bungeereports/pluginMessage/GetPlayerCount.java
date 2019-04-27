package ru.frostdelta.bungeereports.pluginMessage;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.BungeeReports;

public class GetPlayerCount {

    public void sendMessage(Player player){

        BungeeReports plugin = BungeeReports.inst();
        if(!plugin.isEnabled()){
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerCount");
        out.writeUTF(plugin.getConfig().getString("bungee.servername"));

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

    }

}
