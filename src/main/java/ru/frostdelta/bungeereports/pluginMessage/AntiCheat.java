package ru.frostdelta.bungeereports.pluginMessage;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.Network;
import ru.frostdelta.bungeereports.action.Action;
import ru.frostdelta.bungeereports.executor.Executor;


public class AntiCheat implements PluginMessageListener {

    private Loader plugin;

    public AntiCheat(Loader instance){

        plugin = instance;

    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {

        Network db = new Network(plugin);
        Executor exe = new Executor(plugin);
        if (channel.equals("AntiCheat")) {

            ByteArrayDataInput in = ByteStreams.newDataInput(bytes);

            Action action = Action.getAction(in.readUTF());
            if (Executor.getActionQueue().containsKey(player.getName()) && Executor.getActionQueue().get(player.getName()).equals(action)) {
                switch (action) {
                    case AUTHORIZATION:
                        Executor.getActionQueue().remove(player.getName());
                        break;
                    case SCREENSHOT:
                        String screenID = in.readUTF();
                        if (Executor.getRequestQueue().containsKey(player.getName())) {
                            Player request = plugin.getServer().getPlayer(Executor.getRequestQueue().get(player.getName()));
                            if (request.isOnline()) {
                                if (screenID.isEmpty()) {
                                    exe.sendMessage(request, "&cУ игрока " + player.getName() + " не загрузился скрин.");
                                } else {
                                    exe.sendMessage(request, "&aСкриншот экрана игрока " + player.getName() + " успешно загружен.");
                                }
                            }
                        }
                        db.addScreenshot(player.getName(), screenID);
                        break;
                }
            }
        }
    }

}
