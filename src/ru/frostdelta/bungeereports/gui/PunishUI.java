package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.holders.PunishHolder;
import ru.frostdelta.bungeereports.holders.ReasonHolder;

public class PunishUI {

    private Loader plugin;

    public PunishUI(Loader instance){

        plugin = instance;

    }


    public void openGUI(String player, Player moder, String sender) {

        Inventory inv = Bukkit.createInventory(new PunishHolder(), 9, "PunishMenu");


        ItemStack accept = new ItemStack(Material.GREEN_GLAZED_TERRACOTTA);
        ItemStack deny = new ItemStack(Material.RED_GLAZED_TERRACOTTA);
        ItemStack send = new ItemStack(Material.APPLE);

        ItemMeta acceptItemMeta = accept.getItemMeta();
        ItemMeta itemMeta = accept.getItemMeta();
        ItemMeta denyMeta = deny.getItemMeta();

        acceptItemMeta.setDisplayName("Принять");
        accept.setItemMeta(acceptItemMeta);
        inv.setItem(2, accept);

        itemMeta.setDisplayName(sender);
        send.setItemMeta(itemMeta);
        inv.setItem(4, send);

        denyMeta.setDisplayName("Отклонить");
        deny.setItemMeta(denyMeta);
        inv.setItem(6, deny);


        moder.openInventory(inv);

    }



}
