package ru.frostdelta.bungeereports;

public class ReasonAPI {

   private String reason;
   private int time;
   private String type;

   public ReasonAPI(String reason, int time, String type){

       this.reason = reason;
       this.time = time;
       this.type = type;
   }

   public String getReason(){
       return this.reason;
   }

   public int getTime(){
       return this.time;
   }

   public String getType(){
       return this.type;
   }

}
