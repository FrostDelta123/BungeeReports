package ru.frostdelta.bungeereports;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class Sender {

    private static String name;
    private static String ip;
    private static Location location;

    public static Location getLocation() {
        return location;
    }

    public static String getIp() {
        return ip;
    }
    public static String getName(){
        return name;
    }

    public static void senderOf(Player p){
        Sender.name = p.getName();
        Sender.ip = p.getAddress().getHostName();
        Sender.location = p.getLocation();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}