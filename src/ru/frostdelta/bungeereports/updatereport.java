package ru.frostdelta.bungeereports;


public class updatereport {



    network network = new network();

    public void updateReport(String player, String sender, String solved){

        network.updateReport(solved, player, sender);

    }

}
