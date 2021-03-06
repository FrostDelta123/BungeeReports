package ru.frostdelta.bungeereports.pluginMessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ru.frostdelta.bungeereports.BungeeReports;
import ru.frostdelta.bungeereports.executor.Executor;
import ru.frostdelta.bungeereports.gui.MainInterface;

public class PluginMessage implements PluginMessageListener {

    private int players;

    public void sendMessage(Player player){
        BungeeReports plugin = BungeeReports.inst();
        if(!plugin.isEnabled()) return;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("PlayerList");
        out.writeUTF(plugin.getConfig().getString("bungee.servername"));

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());

    }

    @Override
    @SuppressWarnings("unchecked")
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        BungeeReports plugin = BungeeReports.inst();
        if (!channel.equals("BungeeCord") || !plugin.isEnabled()) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if(subchannel.equalsIgnoreCase("PlayerCount")){
            String server = in.readUTF();
            players = in.readInt();
            sendMessage(player);
            return;
        }
        if(subchannel.equalsIgnoreCase("PlayerList")) {
            String server = in.readUTF();
            String[] playerList = in.readUTF().split(", ");
            if(player.hasPermission("bungeereports.player") && Executor.getSenders().contains(player)){
                player.openInventory(new MainInterface(player, players, playerList).create());
                Executor.getSenders().remove(players);
            }
        }

    }
}
