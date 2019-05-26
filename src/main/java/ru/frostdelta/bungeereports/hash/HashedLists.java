package ru.frostdelta.bungeereports.hash;

import ru.frostdelta.bungeereports.Network;

import java.util.ArrayList;

public class HashedLists {

    private static ArrayList<String> reportList = new ArrayList<String>();
    private static ArrayList<String> reasonList = new ArrayList<String>();
    private static ArrayList<String> senderList = new ArrayList<String>();
    private static ArrayList<String> commentList = new ArrayList<String>();

    private static int totalReports;

    public static Integer getTotalRepors(){
        return totalReports;
    }

    public static ArrayList<String> getReportList(){
        return reportList;
    }

    public static ArrayList<String> getSenderList(){
        return senderList;
    }

    public static ArrayList<String> getReasonList(){
        return reasonList;
    }

    public static ArrayList<String> getCommentList(){
        return commentList;
    }

    public static void loadReports(){

        Network network = new Network();
        totalReports = network.totalReports();
        senderList = network.reportList("sender");
        reasonList = network.reportList("reason");
        reportList = network.reportList("player");
        commentList = network.reportList("comment");
    }

    public static void addReport(String sender, String player, String reason, String comment){
        reportList.add(player);
        reasonList.add(reason);
        senderList.add(sender);
        commentList.add(comment);
        totalReports++;
    }

    public static void changeCount(int index){
        reportList.remove(index);
        reasonList.remove(index);
        senderList.remove(index);
        commentList.remove(index);
        totalReports--;
    }


}
