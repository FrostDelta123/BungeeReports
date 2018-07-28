package ru.frostdelta.bungeereports.pluginMessage;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.Loader;

public class GetPlayerCount {

    private Loader plugin;

    public GetPlayerCount(Loader instance){

        plugin = instance;

    }

    public void sendMessage(Player player){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerCount");
        out.writeUTF(plugin.getConfig().getString("bungee.servername"));

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

    }

}
