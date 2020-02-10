package com.geekbrains.chat.server;

import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) throws SQLException {
        new Server(8189);
    }
} 