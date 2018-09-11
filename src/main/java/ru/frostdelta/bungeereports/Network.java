package ru.frostdelta.bungeereports;

import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Network {

    public String url, username, password;
    private static HashMap<String, PreparedStatement> preparedStatements = new HashMap<>();
    private Connection connection;

    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url +
                    "?useUnicode=true&characterEncoding=UTF-8", this.username, this.password);

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
                    "SELECT `screenshots` FROM `screen` WHERE `player`=?;"));
            preparedStatements.put("checkBan", connection.prepareStatement(
                    "SELECT `type` FROM `banlist` WHERE player=?"));
            preparedStatements.put("autoUnban", connection.prepareStatement(
                    "DELETE FROM `banlist` WHERE unbantime<? AND unbantime<>0"));
            preparedStatements.put("addBan", connection.prepareStatement(
                    "INSERT INTO `banlist` (player, bantime, unbantime, type) VALUES (?,?,?,?) "));
        }

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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void autoUnban(long unban) {
        try {
            
            PreparedStatement autoUnban = preparedStatements.get("autoUnban");
            autoUnban.setLong(1, unban);
            autoUnban.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getScreenshots(String player) {
        try {
            
            PreparedStatement getScreenshots = preparedStatements.get("getScreenshots");
            getScreenshots.setString(1, player);
            try (ResultSet rs = getScreenshots.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("screenshots");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void customReward(String table, String money, String playerCol, double amount,
                             String player, String conUrl, String user, String pass) {
        try {
           
           Connection con = DriverManager.getConnection(conUrl + "?useUnicode=true&characterEncoding=UTF-8", user, pass);
           Statement sql = con.createStatement();
           sql.executeUpdate("UPDATE " + table +
                   "SET " + money + " = " + money + " + " + amount + " WHERE " + playerCol + " = '" + player + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void createDB() {
        try {
            
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `reports` (sender varchar(200), player varchar(200), reason varchar(200), comment varchar(200), solved varchar(200)) CHARACTER SET utf8 COLLATE utf8_general_ci";
            String sql2 = "CREATE TABLE IF NOT EXISTS `banlist` (player varchar(200), bantime bigint(200), unbantime bigint(200), type varchar(200)) CHARACTER SET utf8 COLLATE utf8_general_ci";
            
            statement.executeUpdate(sql);
            statement.executeUpdate(sql2);

            System.out.println("Database created!");
        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 1007) {
                System.out.println(sqlException.getMessage());
            } else {
                sqlException.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void purge(String sender, String solved) {
        try {
            
            PreparedStatement purge = preparedStatements.get("purge");
            purge.setString(1, sender);
            purge.setString(2, solved);
            purge.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int playerReports(String sender, String solved) {
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateReport(String solved, String player, String sender) {
        try {
           
            PreparedStatement updateReport = preparedStatements.get("updateReport");
            updateReport.setString(1, solved );
            updateReport.setString(2, player );
            updateReport.setString(3, sender);
            updateReport.setString(4, "no" );
            updateReport.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void addReport(String sender, String player, String reason, String comment) {
        try {
            
            PreparedStatement addReport = preparedStatements.get("addReport");
            addReport.setString(1, sender);
            addReport.setString(2, player);
            addReport.setString(3, reason);
            addReport.setString(4, comment);
            addReport.setString(5, "no");
            addReport.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
