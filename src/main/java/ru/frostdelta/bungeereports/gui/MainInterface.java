package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import ru.frostdelta.bungeereports.utils.Messages;

public class MainInterface implements InventoryHolder {
    private Inventory inv;
    public MainInterface(Player p, int count, String[] playerList) {

        int slots = 9;
        while (count > slots) {
            slots = slots + 9;
            if(count < slots){
                break;
            }
        }
        inv = Bukkit.createInventory(this, slots, Messages.REPORTS_INV_NAME);
                int x = 0;
                for (String player : playerList) {
                    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3, (byte) SkullType.PLAYER.ordinal());
                    SkullMeta itemMeta = (SkullMeta) skull.getItemMeta();
                    itemMeta.setOwner(p.getName());
                    itemMeta.setDisplayName(player);
                    skull.setItemMeta(itemMeta);
                    inv.setItem(x, skull);
                    x++;
                }
    }
    public Inventory create(){
        return inv;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
