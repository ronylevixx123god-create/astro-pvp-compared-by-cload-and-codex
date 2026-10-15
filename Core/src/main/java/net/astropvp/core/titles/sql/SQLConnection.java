/*
 * Decompiled with CFR 0.150.
 */
package net.astropvp.core.titles.sql;

import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLConnection {
    private static Connection con = null;
    public static int query_count = 0;

    public static Connection getConnection(boolean refresh) {
        try {
            if (query_count >= 1000 || refresh) {
                if (con != null) {
                    con.close();
                }
                con = DriverManager.getConnection(SQLHandler.sqlurl, SQLHandler.user, SQLHandler.password);
                query_count = 0;
            }
            if (con == null || con.isClosed()) {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection(SQLHandler.sqlurl, SQLHandler.user, SQLHandler.password);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                con = DriverManager.getConnection(SQLHandler.sqlurl, SQLHandler.user, SQLHandler.password);
            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
        ++query_count;
        return con;
    }

    public static ResultSet getResultSet(String query) {
        PreparedStatement pst = null;
        try {
            pst = SQLConnection.getConnection(false).prepareStatement(query);
            pst.execute();
            ResultSet rs = pst.getResultSet();
            return rs;
        }
        catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void executeSQLQuery(String query, boolean refresh) {
        PreparedStatement pst = null;
        try {
            pst = SQLConnection.getConnection(refresh).prepareStatement(query);
            pst.executeUpdate();
        }
        catch (Exception ex) {
            if (ex instanceof SocketException) {
                try {
                    pst.close();
                    pst = SQLConnection.getConnection(refresh).prepareStatement(query);
                    pst.executeUpdate();
                }
                catch (Exception ex2) {
                    ex2.printStackTrace();
                }
            } else {
                ex.printStackTrace();
            }
        }
        finally {
            try {
                if (pst != null) {
                    pst.close();
                }
            }
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}

