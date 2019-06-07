package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.BungeeReports;
import ru.frostdelta.bungeereports.ReasonAPI;
import ru.frostdelta.bungeereports.utils.Messages;

import java.util.*;


public class BanReasons implements InventoryHolder {

    private static Map<String, ReasonAPI> reasonAPIMap = new HashMap<String, ReasonAPI>();
    private Inventory inv;
    public BanReasons(String sender) {
        BungeeReports plugin = BungeeReports.inst();
        ItemStack banButton = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta itemMeta = banButton.getItemMeta();

        int slots = 9;
        int count = plugin.getConfig().getStringList("ban.reasons").size();
        while (count > slots) {
            slots = slots + 9;
            if(count < slots){
                break;
            }
        }
        inv = Bukkit.createInventory(this, slots, sender);
        int x = 0;
        for(String name : plugin.getConfig().getStringList("ban.reasons")){
            List<String> list = new ArrayList<String>();
            list.addAll(Arrays.asList(name.split(":", 3)));
            getAPI().put(list.get(0), new ReasonAPI(list.get(0), Integer.parseInt(list.get(1)), list.get(2)));
            itemMeta.setDisplayName(list.get(0));
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(Messages.PUNISH_TIME + getAPI().get(list.get(0)).getTime());
            lore.add(Messages.PUNISH_TYPE  + getAPI().get(list.get(0)).getType());
            itemMeta.setLore(lore);
            banButton.setItemMeta(itemMeta);
            inv.setItem(x, banButton);
            x++;
        }
        ItemStack rejectButton = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = rejectButton.getItemMeta();
        meta.setDisplayName(Messages.REJECT);
        rejectButton.setItemMeta(meta);

        inv.setItem(x++, rejectButton);
    }

    public Inventory create(){
        return inv;
    }

    public static Map<String, ReasonAPI> getAPI(){
        return reasonAPIMap;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
