package ru.frostdelta.bungeereports.pluginMessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ru.frostdelta.bungeereports.loader;
import ru.frostdelta.bungeereports.gui.openUI;

import java.util.Arrays;

public class pluginMessage implements PluginMessageListener {

    private loader plugin;
    private int players;

    public pluginMessage(loader instance){

        plugin = instance;

    }


    openUI openUI = new openUI(plugin);

    public void sendMessage(Player player){
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerList");
        out.writeUTF(plugin.getConfig().getString("bungee.servername"));

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        String subchannel = in.readUTF();


        if(subchannel.equalsIgnoreCase("PlayerCount")){
            String server = in.readUTF();
            int playercount = in.readInt();

            players = playercount;
            sendMessage(player);
        }

        if(subchannel.equalsIgnoreCase("PlayerList")) {
            String server = in.readUTF();

            String[] playerList = in.readUTF().split(", ");

            openUI.openGUI(player, players, playerList);

        }

    }
}
