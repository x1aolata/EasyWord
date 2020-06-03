package com.x1aolata.easyword.helper;

import android.util.Log;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;


/**
 * @author x1aolata
 * @date 2020/5/29 8:28
 * @script Database connections and disconnections
 */
public class DBHelper {

    // 配置信息
    private static String driver = "com.mysql.jdbc.Driver";
    private static String url = "Inaccessible";
    private static String username = "Inaccessible";
    private static String password = "Inaccessible";


    /**
     * 连接MySQL数据库
     * 获取可用的连接对象
     *
     * @return 连接
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    /**
     * 释放资源
     *
     * @param set
     * @param statement  兼容 preparedstatement
     * @param connection
     */
    public static void close(ResultSet set, Statement statement, Connection connection) {
        if (set != null) {
            try {
                set.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
