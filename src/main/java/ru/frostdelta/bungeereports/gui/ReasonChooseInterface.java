package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.BungeeReports;
import ru.frostdelta.bungeereports.utils.Messages;

import java.util.List;

public class ReasonChooseInterface implements InventoryHolder {

    private Inventory inv;
    public ReasonChooseInterface() {
        List<String> reasons = BungeeReports.inst().getConfig().getStringList("reasons");
        int slots = 9;
        while (reasons.size() > slots) {
            slots = slots + 9;
            if(reasons.size() < slots){
                break;
            }
        }
        inv = Bukkit.createInventory(this, slots, Messages.REASONS_INV_NAME);
        int x = 0;
        for (String reason : reasons) {

            ItemStack skull = new ItemStack(Material.SKULL_ITEM);

            ItemMeta itemMeta = skull.getItemMeta();
            itemMeta.setDisplayName(reason);
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
        return null;
    }
}
