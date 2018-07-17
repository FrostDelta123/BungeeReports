package ru.frostdelta.bungeereports.hash;

import java.util.ArrayList;
import java.util.List;

public class HashedLists {

    private List<String> reportList = new ArrayList<>();
    private List<String> reasonList = new ArrayList<>();
    private List<String> senderList = new ArrayList<>();

    public List<String> getReportList(){

        return reportList;

    }

    public List<String> getSenderList(){

        return senderList;

    }

    public List<String> getReasonList(){

        return reasonList;

    }

}
