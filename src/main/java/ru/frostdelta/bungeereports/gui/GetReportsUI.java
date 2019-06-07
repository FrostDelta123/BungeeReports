package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.EventHandler;
import ru.frostdelta.bungeereports.utils.Messages;

import java.util.ArrayList;
import java.util.HashMap;

public class GetReportsUI implements InventoryHolder {

    public HashMap<Integer, String> sender = new HashMap<Integer, String>();
    private Inventory inv;

    public GetReportsUI(Player p, int count, ArrayList<String> senderList, ArrayList<String> reasonList, ArrayList<String> playerList, ArrayList<String> comment) {

        if(!playerList.isEmpty()) {

            int slots = 9;
            while (count > slots) {
                slots = slots + 9;
                if (count < slots) {
                    break;
                }
            }
            inv = Bukkit.createInventory(this, slots, Messages.GET_REPORTS_INV_NAME);
            for (int x = 0; count > x; x++) {

                ItemStack skull = new ItemStack(Material.SKULL_ITEM);
                ItemMeta itemMeta = skull.getItemMeta();
                itemMeta.setDisplayName(playerList.get(x));

                ArrayList<String> lore = new ArrayList<String>();

                lore.add(Messages.REPORT_SENDER + senderList.get(x));
                lore.add(Messages.REPORT_REASON + reasonList.get(x));
                lore.add(Messages.REPORT_COMMENT + comment.get(x));
                itemMeta.setLore(lore);
                skull.setItemMeta(itemMeta);
                sender.put(x,senderList.get(x));

                inv.setItem(x, skull);
            }
            //Какой же это костыль, самому стыдно.
            EventHandler handler = new EventHandler(sender);
            p.openInventory(inv);
        }else p.sendMessage(Messages.NO_REPORTS);

    }

    public Inventory create(){
        return inv;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
