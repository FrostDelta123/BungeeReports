package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.Loader;

public class PunishUI {

    private Loader plugin;

    public PunishUI(Loader instance){

        plugin = instance;

    }


    public void openGUI(String player, Player moder, String sender) {

        Inventory inv = Bukkit.createInventory(null, 9, "PunishMenu");


        ItemStack skull = new ItemStack(Material.SKULL_ITEM);
        ItemStack send = new ItemStack(Material.APPLE);

        ItemMeta itemMeta = skull.getItemMeta();
        ItemMeta item = send.getItemMeta();

        itemMeta.setDisplayName("Принять");
        skull.setItemMeta(itemMeta);
        inv.setItem(2, skull);

        itemMeta.setDisplayName(sender);
        skull.setItemMeta(itemMeta);
        inv.setItem(4, skull);

        itemMeta.setDisplayName("Отклонить");
        skull.setItemMeta(itemMeta);
        inv.setItem(6, skull);


        moder.openInventory(inv);

    }



}
