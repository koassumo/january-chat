package com.geekbrains.chat.server;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class JdbcAuthManager implements AuthManager{
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement ps;

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:jdbc.db");
        stmt = connection.createStatement();
    }


    public static void disconnect() {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private class Entry {
        private String login;
        private String password;
        private String nickname;

        public Entry(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }

    private List<JdbcAuthManager.Entry> users;

    public JdbcAuthManager()throws SQLException  {
        this.users = new ArrayList<>();
        try {
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }

        ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE login != 0;");
            System.out.println(rs);
//            while (rs.next()) {
//                users.add(new JdbcAuthManager.Entry(rs.getString(1), rs.getString(2), rs.getString(3)));
//            }

    }

    @Override
    public void changeNickInAuthArray (String oldNick, String newNick){
        for (JdbcAuthManager.Entry u : users) {
            if (u.nickname.equals(oldNick)) {
                u.nickname = newNick;
            }
        }
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (JdbcAuthManager.Entry u : users) {
            if (u.login.equals(login) && u.password.equals(password)) {
                return u.nickname;
            }
        }
        return null;
    }
}
