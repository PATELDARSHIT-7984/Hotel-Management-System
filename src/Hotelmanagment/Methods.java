package Hotelmanagment;

import java.sql.*;
import java.util.*;

public class Methods {
    static Connection con;
    static Scanner sc;

    static void coo() throws SQLException {
        String dburl = "jdbc:mysql://localhost:3306/hotel";
        String dbuser = "root";
        String dbpass = "";

        con = DriverManager.getConnection(dburl, dbuser, dbpass);

        sc = new Scanner(System.in);
    }

    static boolean aadhar(long data) {

        String s = data + "";
        if (s.length() == 12) {

            return true;

        } else {
            System.out.println("Enter valid aadhar no:");
            return false;
        }

    }

    static boolean phoneno(String data) {

        if (data.length() == 10) {
            return true;
        } else {
            System.out.println("Enter valid phone no:");
            return false;
        }
    }

}
