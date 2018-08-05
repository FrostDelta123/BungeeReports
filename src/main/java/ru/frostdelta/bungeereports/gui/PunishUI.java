package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.EventHandler;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.holders.PunishHolder;

import java.util.Collections;

public class PunishUI {

    private Loader plugin;

    public PunishUI(Loader instance){

        plugin = instance;

    }


    public void openGUI(String player, Player moder, String sender) {

        Inventory inv = Bukkit.createInventory(new PunishHolder(), 9, "PunishMenu");


        ItemStack accept = new ItemStack(Material.GREEN_RECORD);
        ItemStack deny = new ItemStack(Material.REDSTONE_BLOCK);
        ItemStack spectate = new ItemStack(Material.EYE_OF_ENDER);
        ItemStack send = new ItemStack(Material.APPLE);

        ItemMeta acceptItemMeta = accept.getItemMeta();
        ItemMeta itemMeta = accept.getItemMeta();
        ItemMeta denyMeta = deny.getItemMeta();
        ItemMeta spectateMeta = spectate.getItemMeta();

        acceptItemMeta.setDisplayName("Принять");
        accept.setItemMeta(acceptItemMeta);
        inv.setItem(2, accept);


        itemMeta.setDisplayName(sender);
        send.setItemMeta(itemMeta);
        inv.setItem(4, send);

        denyMeta.setDisplayName("Отклонить");
        deny.setItemMeta(denyMeta);
        inv.setItem(6, deny);

        spectateMeta.setDisplayName("Наблюдать");
        spectateMeta.setLore(Collections.singletonList("BETA"));
        spectate.setItemMeta(spectateMeta);
        EventHandler handler = new EventHandler(plugin);
        if(plugin.isSpectateEnabled() && Bukkit.getPlayer(handler.getBan().get(sender)) != null) {
            inv.setItem(8, spectate);
        }

        moder.openInventory(inv);

    }



}
