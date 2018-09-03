package ru.frostdelta.bungeereports.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.frostdelta.bungeereports.Loader;
import ru.frostdelta.bungeereports.ReasonAPI;
import ru.frostdelta.bungeereports.holders.BanReasonsHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BanReasons {

    private Loader plugin;

    public BanReasons(Loader instance){

        plugin = instance;

    }

    private static Map<String, ReasonAPI> reasonAPIMap = new HashMap<String, ReasonAPI>();

    public void openGUI(Player moder) {

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

        Inventory inv = Bukkit.createInventory(new BanReasonsHolder(), slots, "BanReasons");

        int x = 0;
        for(String name : plugin.getConfig().getStringList("ban.reasons")){
            List<String> list = new ArrayList<String>();
            for(String reason : name.split(":", 3)) {
                list.add(reason);
            }
            getAPI().put(list.get(0), new ReasonAPI(list.get(0), Integer.parseInt(list.get(1)), list.get(2)));
            itemMeta.setDisplayName(list.get(0));
            itemMeta.setLore(list);
            banButton.setItemMeta(itemMeta);
            inv.setItem(x, banButton);
            x++;
        }

        moder.openInventory(inv);

    }

    public static Map<String, ReasonAPI> getAPI(){
        return reasonAPIMap;
    }

}
