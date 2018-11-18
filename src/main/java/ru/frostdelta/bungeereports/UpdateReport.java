package ru.frostdelta.bungeereports;


public class UpdateReport {



    private Network Network = new Network();

    public void updateReport(String player, String sender, String solved){

        Network.updateReport(solved, player, sender);

    }

}
