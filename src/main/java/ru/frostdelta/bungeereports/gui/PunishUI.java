package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.utils.Utils;

import java.util.Collections;

public class PunishUI implements InventoryHolder {

    public void openGUI(String player, Player moder, String sender) {

        Inventory inv = Bukkit.createInventory(this, 9, Utils.PUNISH_INV_NAME);


        ItemStack accept = new ItemStack(Material.GREEN_RECORD);
        ItemStack deny = new ItemStack(Material.REDSTONE_BLOCK);
        ItemStack spectate = new ItemStack(Material.EYE_OF_ENDER);
        ItemStack send = new ItemStack(Material.APPLE);

        ItemMeta acceptItemMeta = accept.getItemMeta();
        ItemMeta itemMeta = accept.getItemMeta();
        ItemMeta denyMeta = deny.getItemMeta();
        ItemMeta spectateMeta = spectate.getItemMeta();

        acceptItemMeta.setDisplayName(Utils.ACCEPT);
        accept.setItemMeta(acceptItemMeta);
        inv.setItem(2, accept);


        itemMeta.setDisplayName(sender);
        send.setItemMeta(itemMeta);
        inv.setItem(4, send);

        denyMeta.setDisplayName(Utils.REJECT);
        deny.setItemMeta(denyMeta);
        inv.setItem(6, deny);

        spectateMeta.setDisplayName(Utils.SPECTATE);
        spectateMeta.setLore(Collections.singletonList("BETA"));
        spectate.setItemMeta(spectateMeta);

        inv.setItem(8, spectate);


        moder.openInventory(inv);

    }


    @Override
    public Inventory getInventory() {
        return null;
    }
}
