package ru.frostdelta.bungeereports;

import org.bukkit.Bukkit;
import ru.frostdelta.bungeereports.chat.ChatLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Network {

    public static String url, username, password;
    private static HashMap<String, PreparedStatement> preparedStatements = new HashMap<>();
    private static Connection connection;

    public Network() {

    }

    public static void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(url +
                "?useUnicode=true&characterEncoding=UTF-8", username, password);
        preparedStatements.put("addReport", connection.prepareStatement(
                "INSERT INTO `reports` (sender, player, reason, comment, solved) VALUES (?,?,?,?,?)"));
        preparedStatements.put("totalReports", connection.prepareStatement(
                "SELECT COUNT(*) FROM `reports` WHERE `solved`=?"));
        preparedStatements.put("playerReports", connection.prepareStatement(
                "SELECT COUNT(*) FROM `reports` WHERE `sender`=? AND `solved`=?"));
        preparedStatements.put("reportList", connection.prepareStatement(
                "SELECT * FROM `reports` WHERE `solved`=?"));
        preparedStatements.put("updateReport", connection.prepareStatement(
                "UPDATE `reports` SET `solved`=? WHERE `player`=? AND `sender`=? AND `solved`=?"));
        preparedStatements.put("purge", connection.prepareStatement(
                "DELETE FROM `reports` WHERE `sender`=? AND `solved`=?"));
        preparedStatements.put("addScreenshot",
                connection.prepareStatement(
                        "INSERT INTO `screen` (player, screenshots) VALUES (?,?)"
                                + " ON DUPLICATE KEY "
                                + "UPDATE `screenshots` = IF(player=?,CONCAT(screenshots,?),screenshots);"));
        preparedStatements.put("getScreenshots", connection.prepareStatement(
                "SELECT * FROM `screen` WHERE `player`=?;"));
        preparedStatements.put("checkBan", connection.prepareStatement(
                "SELECT `type` FROM `banlist` WHERE player=?"));
        preparedStatements.put("autoUnban", connection.prepareStatement(
                "DELETE FROM `banlist` WHERE unbantime<? AND unbantime<>0"));
        preparedStatements.put("addBan", connection.prepareStatement(
                "INSERT INTO `banlist` (player, bantime, unbantime, type) VALUES (?,?,?,?) "));


    }

    public void addBan(String player, long bantime, long unban, String type) {
        try {
            PreparedStatement addPunish = preparedStatements.get("addBan");
            addPunish.setString(1, player);
            addPunish.setLong(2, bantime);
            addPunish.setLong(3, unban);
            addPunish.setString(4, type);
            addPunish.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void autoUnban(long unban) {
        try {
            PreparedStatement autoUnban = preparedStatements.get("autoUnban");
            autoUnban.setLong(1, unban);
            autoUnban.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String checkBan(String player) {
        try {
            PreparedStatement checkBan = preparedStatements.get("checkBan");
            checkBan.setString(1, player);
            try (ResultSet rs = checkBan.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("type");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public void addScreenshot(String player, String screenID) {
        try {
            PreparedStatement addScreenshot = preparedStatements.get("addScreenshot");
            addScreenshot.setString(1, player);
            addScreenshot.setString(2, screenID);
            addScreenshot.setString(3, player);
            addScreenshot.setString(4, ";" + screenID);
            addScreenshot.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static String getScreenshots(String player) {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM `screen` WHERE `player` = '" + player + "'";
            PreparedStatement getScreenshots = preparedStatements.get("getScreenshots");
            getScreenshots.setString(1, player);
            try (ResultSet rs = getScreenshots.executeQuery()) {
                if (rs.next()) {
                    //String screenid = rs.getString("screenshots");
                    //statement.executeUpdate(sql);
                    //Bukkit.broadcastMessage(screenid);
                    return rs.getString("screenshots");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static void customReward(String table, String money, String playerCol, double amount,
                             String player, String conUrl, String user, String pass) {
        try {
           Connection con = DriverManager.getConnection(conUrl + "?useUnicode=true&characterEncoding=UTF-8", user, pass);
           Statement sql = con.createStatement();
           sql.executeUpdate("UPDATE " + table +
                   "SET " + money + " = " + money + " + " + amount + " WHERE " + playerCol + " = '" + player + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void createDB() {
        try {

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `reports` (sender varchar(200), player varchar(200), reason varchar(200), comment varchar(200), solved varchar(200)) CHARACTER SET utf8 COLLATE utf8_general_ci";
            String sql2 = "CREATE TABLE IF NOT EXISTS `banlist` (player varchar(200), bantime bigint(200), unbantime bigint(200), type varchar(200)) CHARACTER SET utf8 COLLATE utf8_general_ci";
            String sql3 = "CREATE TABLE IF NOT EXISTS `screen` (player varchar(200), screenshots varchar(200)) CHARACTER SET utf8 COLLATE utf8_general_ci";
            statement.executeUpdate(sql);
            statement.executeUpdate(sql2);
            statement.executeUpdate(sql3);
            BungeeReports.inst().getLogger().info("Database created!");
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 1007) {
                System.out.println(sqlException.getMessage());
            } else {
                sqlException.printStackTrace();
            }
        }
    }

    public static void purge(String sender, String solved) {
        try {
            PreparedStatement purge = preparedStatements.get("purge");
            purge.setString(1, sender);
            purge.setString(2, solved);
            purge.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int playerReports(String sender, String solved) {
        try {
            PreparedStatement playerReports = preparedStatements.get("playerReports");
            playerReports.setString(1, sender);
            playerReports.setString(2, solved);
            try (ResultSet rs = playerReports.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void updateReport(String solved, String player, String sender) {
        try {
            PreparedStatement updateReport = preparedStatements.get("updateReport");
            updateReport.setString(1, solved );
            updateReport.setString(2, player );
            updateReport.setString(3, sender);
            updateReport.setString(4, "no" );
            updateReport.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> reportList(String col) {
        try {
            PreparedStatement reportList = preparedStatements.get("reportList");
            ArrayList<String> list = new ArrayList<String>();
            reportList.setString(1, "no" );
            try (ResultSet rs = reportList.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString(col);
                    list.add(name);
                }
                return list;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int totalReports() {
        try {
            PreparedStatement totalReports = preparedStatements.get("totalReports");
            totalReports.setString(1, "no");
            try (ResultSet rs = totalReports.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void addReport(String sender, String player, String reason, String comment) {
        try {
            BungeeReports plugin = BungeeReports.inst();
            PreparedStatement addReport = preparedStatements.get("addReport");
            addReport.setString(1, sender);
            addReport.setString(2, player);
            addReport.setString(3, reason);
            addReport.setString(4, comment);
            addReport.setString(5, "no");
            addReport.executeUpdate();
            if(plugin.getConfig().getBoolean("chat.log")){
                File playerFolder = new File(plugin.getDataFolder().getAbsolutePath()+"/logs/" + player);
                if (!playerFolder.exists()) {
                    playerFolder.mkdirs();
                }
                File log = new File(plugin.getDataFolder().getAbsolutePath()+"/logs/" + player + "/" + sender +".txt");
                PrintWriter pw = new PrintWriter(log);
                pw.println("&4" + player + "'s messages:");
                if(!ChatLogger.getChatLog().get(Bukkit.getOfflinePlayer(player)).isEmpty()){
                    pw.println("Empty log!");
                    pw.close();
                    return;
                }
                for(String var : ChatLogger.getChatLog().get(Bukkit.getOfflinePlayer(player))){
                    pw.println(var);
                }
                pw.println("&4General chat messages:");
                for(String var : ChatLogger.getLeatestChatMessages()){
                    pw.println(var);
                }
                pw.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
