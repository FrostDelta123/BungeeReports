package ru.frostdelta.bungeereports.pluginMessage;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.frostdelta.bungeereports.loader;

public class getPlayerCount {

    private loader plugin;

    public getPlayerCount(loader instance){

        plugin = instance;

    }

    public void sendMessage(Player player){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerCount");
        out.writeUTF(plugin.getConfig().getString("bungee.servername"));

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

    }

}
