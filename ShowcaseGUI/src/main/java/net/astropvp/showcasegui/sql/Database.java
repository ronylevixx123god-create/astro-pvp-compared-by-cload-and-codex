package net.astropvp.showcasegui.sql;

import net.astropvp.showcasegui.ShowcaseGUI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Database {
    private String url;
    private String host;
    private String username;
    private String password;
    private String database;
    protected static Connection connection;
    public int lastUpdate;

    public Database() {
        this(ShowcaseGUI.get().getConfig().getString("sql.hostname"), ShowcaseGUI.get().getConfig().getString("sql.username"), ShowcaseGUI.get().getConfig().getString("sql.password"), ShowcaseGUI.get().getConfig().getString("sql.database"));
    }

    public Database(String host, String username, String password, String database) {
        this.username = username;
        this.password = password;
        this.database = database;
        this.url = "jdbc:mysql://" + host + ":3306/" + database;
    }

    public void executeBatch(String update) {
        try {
            PreparedStatement pst = this.getConnection().prepareStatement(update);

            try {
                pst.executeBatch();
                pst.close();
            } catch (Throwable var6) {
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (pst != null) {
                pst.close();
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public void executeUpdate(String update) {
        try {
            PreparedStatement pst = this.getConnection().prepareStatement(update);

            try {
                pst.execute();
                pst.close();
            } catch (Throwable var6) {
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }

                throw var6;
            }

            if (pst != null) {
                pst.close();
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }

    }

    public boolean isConnected() {
        try {
            Connection connect = this.getConnection();
            return connect != null;
        } catch (Exception var2) {
            return false;
        }
    }

    public synchronized Connection getConnection() {
        try {
            if (connection != null) {
                if (connection.isValid(1)) {
                    return connection;
                }

                connection.close();
            }

            connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return connection;
    }

    public String getDatabase() {
        return this.database;
    }
}
