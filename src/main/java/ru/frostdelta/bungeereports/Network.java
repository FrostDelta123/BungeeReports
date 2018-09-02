package ru.frostdelta.bungeereports;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Network {

    public String url, username, password;
    private static HashMap<String, PreparedStatement> preparedStatements = new HashMap<String, PreparedStatement>();
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
            connection = DriverManager.getConnection(url + "?useUnicode=true&characterEncoding=UTF-8", this.username, this.password);

            preparedStatements.put("addReport", connection.prepareStatement("INSERT INTO `reports` (sender, player, reason, comment, solved) VALUES (?,?,?,?,?)"));

            preparedStatements.put("totalReports", connection.prepareStatement("SELECT COUNT(*) FROM `reports` WHERE `solved`=?"));

            preparedStatements.put("playerReports", connection.prepareStatement("SELECT COUNT(*) FROM `reports` WHERE `sender`=? AND `solved`=?"));

            preparedStatements.put("reportList", connection.prepareStatement("SELECT * FROM `reports` WHERE `solved`=?"));

            preparedStatements.put("updateReport", connection.prepareStatement("UPDATE `reports` SET `solved`=? WHERE `player`=? AND `sender`=? AND `solved`=?"));

            preparedStatements.put("purge", connection.prepareStatement("DELETE FROM `reports` WHERE `sender`=? AND `solved`=?"));

            preparedStatements.put("addScreenshot",
                    connection.prepareStatement(
                            "INSERT INTO `screen` (player, screenshots) VALUES (?,?)"
                                    + " ON DUPLICATE KEY "
                                    + "UPDATE `screenshots` = IF(player=?,CONCAT(screenshots,?),screenshots);"));

            preparedStatements.put("getScreenshots", connection.prepareStatement("SELECT `screenshots` FROM `screen` WHERE `player`=?;"));

        }

    }

    public void addScreenshot(String player, String screenID) {
        PreparedStatement addScreenshot = preparedStatements.get("addScreenshot");

        try {
            addScreenshot.setString(1, player);
            addScreenshot.setString(2, screenID);
            addScreenshot.setString(3, player);
            addScreenshot.setString(4, ";" +screenID);

            addScreenshot.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getScreenshots(String player) {
        PreparedStatement getScreenshots = preparedStatements.get("getScreenshots");

        ResultSet rs = null;
        try {
            getScreenshots.setString(1, player);

            rs = getScreenshots.executeQuery();
            while (rs.next()) {
                return rs.getString("screenshots");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return "";
    }

    public void customReward(String table, String money, String playerCol, double amount, String player, String conUrl, String user, String pass){

        try {

           Connection con = DriverManager.getConnection(conUrl + "?useUnicode=true&characterEncoding=UTF-8", user, pass);
           Statement sql = con.createStatement();

           sql.executeUpdate("UPDATE "+table+" t1 JOIN  "+table+" t2 ON t2."+playerCol+ "= '"+player+"' SET t1."+money+" = t2."+money+" + "+amount+" WHERE t1."+playerCol+" = '"+player+"'");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void createDB(){
        try {

            Statement statement;

            statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `reports` (sender varchar(200), player varchar(200), reason varchar(200), comment varchar(200), solved varchar(200)) CHARACTER SET utf8 COLLATE utf8_general_ci";
            //Заебенить автосоздание
            statement.executeUpdate(sql);

            System.out.println("Database created!");

        } catch (SQLException sqlException) {
            if (sqlException.getErrorCode() == 1007) {

                System.out.println(sqlException.getMessage());
            } else {

                sqlException.printStackTrace();
            }
        }
    }

    public void purge(String sender, String solved){
        PreparedStatement purge = preparedStatements.get("purge");
        try {
            purge.setString(1, sender);
            purge.setString(2, solved);

            purge.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int playerReports(String sender, String solved){
        PreparedStatement playerReports = preparedStatements.get("playerReports");
        ResultSet rs = null;
        try {

            playerReports.setString(1, sender);
            playerReports.setString(2, solved);


            rs = playerReports.executeQuery();

            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return 0;
    }

    public void updateReport(String solved, String player, String sender){
        PreparedStatement updateReport = preparedStatements.get("updateReport");
        try {
            updateReport.setString(1, solved );
            updateReport.setString(2, player );
            updateReport.setString(3, sender);
            updateReport.setString(4,"no" );

            updateReport.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> reportList(String col) {
        PreparedStatement reportList = preparedStatements.get("reportList");
        ResultSet rs = null;
        ArrayList<String> list1 = null;
        ArrayList<String> list = new ArrayList<String>();
        try {

            reportList.setString(1, "no" );

            rs = reportList.executeQuery();

            while (rs.next()) {

                String name = rs.getString(col);
                list.add(name);

            }
            return list;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return list1;
    }

    public int totalReports(){
        PreparedStatement totalReports = preparedStatements.get("totalReports");
        ResultSet rs = null;
        try {

            totalReports.setString(1, "no");

            rs = totalReports.executeQuery();

            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return 0;
    }

    public void addReport(String sender, String player, String reason, String comment){
        PreparedStatement addReport = preparedStatements.get("addReport");
        try {
            addReport.setString(1, sender);
            addReport.setString(2, player);
            addReport.setString(3, reason);
            addReport.setString(4, comment);
            addReport.setString(5, "no");

            addReport.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
