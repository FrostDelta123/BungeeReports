package ru.frostdelta.bungeereports.pluginMessage;

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ru.frostdelta.bungeereports.Loader;

import java.io.File;
import java.io.IOException;

public class Dump  implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {

        Loader plugin = Loader.inst();

        try {
            plugin.getLogger().info("File created in folder " + plugin.getDataFolder().getAbsolutePath()+"/dump/"+player.getUniqueId().toString()+".txt");
            FileUtils.writeByteArrayToFile(new File(plugin.getDataFolder().getAbsolutePath()+"/dump/"+player.getUniqueId().toString()+".txt"),bytes);
        } catch (IOException e) {
            plugin.getLogger().severe("ERROR!");
            e.printStackTrace();
        }

    }
}
