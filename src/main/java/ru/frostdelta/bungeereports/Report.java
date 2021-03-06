package ru.frostdelta.bungeereports;


public class Report {

    private String sender;
    private String reason;
    private String description;
    private String player;
    private String admin;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getDescription() {
        return description;
    }

    public String getPlayer() {
        return player;
    }

    public String getReason() {
        return reason;
    }

    public String getSender() {
        return sender;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
