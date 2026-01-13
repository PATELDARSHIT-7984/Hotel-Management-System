package Hotelmanagment;

import java.sql.*;
import java.util.*;

public class Hotelmanagment {
    static Connection con;
    static Scanner sc;
    static long aadharNo;
    static BinarySearchTree bst = new BinarySearchTree();

    public static void main(String[] args) throws Exception {
        sc = new Scanner(System.in);

        String dburl = "jdbc:mysql://localhost:3306/hotel";
        String dbuser = "root";
        String dbpass = "";
        String drivername = "com.mysql.jdbc.Driver";
        Class.forName(drivername);
        con = DriverManager.getConnection(dburl, dbuser, dbpass);
        System.out.println((con != null) ? "Connetion successfull" : "Failed");

        boolean c = true;
        System.out.println("Welcome to Hotel web page" + "\n");
        do {
            System.out.println("***************" + "Welcome to Hotel Shreeji" + "***************" + "\n");

            System.out.println("How may i help you " + "\n");

            System.out.println("Press 1 for Book a room");
            System.out.println("Press 2 for Login");
            System.out.println("Press 3 for exit");
            System.out.println();
            System.out.print("Enter your choise= " + "\n");
            int choise = sc.nextInt();

            // Main switch method:
            switch (choise) {
                case 1:
                    boolean b = true;
                    do {
                        System.out.println("Press 1 for check rooms");
                        System.out.println("Press 2 for check room avalibility");
                        System.out.println("Press 3 for book your room");
                        System.out.println("Press 4 for exit");
                        System.out.println();
                        System.out.print("Enter your choise= " + "\n");
                        int no = sc.nextInt();

                        // switch method under main switch method case 1:
                        switch (no) {
                            case 1:
                                // room deatails:-
                                String sql = "select r_no,r_details,r_price,r_availability from rooms";
                                PreparedStatement pst = con.prepareStatement(sql);
                                ResultSet rs = pst.executeQuery();
                                System.out.println(
                                        "=====================================================================================================");
                                while (rs.next()) {

                                    System.out.println("||" + "Room_no " + rs.getInt(1) + "||" + "\t" + "Room_details "
                                            + rs.getString(2) + "  " + "\t" + "||" + "Room_price " + rs.getInt(3)
                                            + "||"
                                            + "Room_available " + rs.getString(4) + " " + "||");

                                }
                                System.out.println(
                                        "======================================================================================================");
                                break;

                            case 2:
                                // for availability check:-
                                try {
                                    String q = "{call getavailability(?)}";
                                    CallableStatement cs = con.prepareCall(q);
                                    System.out.print("Enter Room.no:");
                                    int bookedRN = sc.nextInt();
                                    cs.setInt(1, bookedRN);
                                    ResultSet rs7 = cs.executeQuery();
                                    while (rs7.next()) {
                                        System.out.print("Room ID: " + rs7.getInt(1));
                                        System.out.print("Room availability: " + rs7.getString(2));
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;

                            case 3:
                                // room deatails:-
                                String sq = "select r_no,r_details,r_price,r_availability from rooms";
                                PreparedStatement pst1 = con.prepareStatement(sq);
                                ResultSet r = pst1.executeQuery();

                                System.out.println(
                                        "=====================================================================================================");
                                while (r.next()) {

                                    System.out.println("||" + "Room_no " + r.getInt(1) + "||" + "\t" + "Room_details "
                                            + r.getString(2) + "  " + "\t" + "||" + "Room_price " + r.getInt(3)
                                            + "||"
                                            + "Room_available " + r.getString(4) + " " + "||");

                                }
                                System.out.println(
                                        "======================================================================================================");

                                // Enter customer data into customer table:-
                                String queryic = "insert into customers (c_first_name, c_last_name, c_aadhar_no, c_room_no, c_booked_days, c_balance_amt,phone_no) values (?, ?, ?, ?, ?, ?,?)";
                                PreparedStatement pst3 = con.prepareStatement(queryic);

                                // Enter customer's firstname & lastname in customer table:-
                                System.out.print("Enter your First Name= ");
                                String firstname = sc.nextLine();
                                pst3.setString(1, firstname);

                                sc.nextLine();

                                System.out.print("Enter your Last Name= ");
                                String lastname = sc.nextLine();
                                pst3.setString(2, lastname);
                                // Enter customer's aadharno:-

                                while (true) {
                                    System.out.print("Enter Your aadharNo(WithoutSpac)= ");
                                    aadharNo = sc.nextLong();
                                    boolean fr = Methods.aadhar(aadharNo);
                                    if (fr == true) {
                                        pst3.setLong(3, aadharNo);
                                        break;
                                    }
                                }

                                // Enter customer's room no in customer table:-
                                System.out.print("Enter your room no= ");
                                int bookroom = sc.nextInt();
                                pst3.setInt(4, bookroom);

                                // Enter customer's days in customer table:-
                                System.out.print("Enter your days=");
                                int bookedDays = sc.nextInt();
                                pst3.setInt(5, bookedDays);

                                // Balance:-
                                String priceQuery = "select r_price from rooms where r_no = ?";
                                PreparedStatement ps = con.prepareStatement(priceQuery);
                                ps.setInt(1, bookroom);
                                ResultSet rs9 = ps.executeQuery();
                                rs9.next();
                                int roomPrice = rs9.getInt(1);

                                pst3.setInt(6, (roomPrice * bookedDays));

                                // Phone no:-
                                while (true) {
                                    System.out.println("Enter your phone no");
                                    String phoneno = sc.next();
                                    boolean gh = Methods.phoneno(phoneno);
                                    if (gh == true) {
                                        pst3.setString(7, phoneno);
                                        break;
                                    }
                                }

                                // execution:-

                                int n = pst3.executeUpdate();
                                System.out.println((n != -1) ? "your room booked successfull" : "Failes");

                                // booking room:-
                                String sql1 = "update rooms set r_availability = 'unavailable' where r_no=?";

                                // room_no:-
                                PreparedStatement pst2 = con.prepareStatement(sql1);

                                pst2.setInt(1, bookroom);
                                pst2.executeUpdate();

                                String queryir = "update rooms set c_aadhar_no=? where r_no=?";
                                PreparedStatement psir = con.prepareStatement(queryir);
                                psir.setLong(1, aadharNo);
                                psir.setInt(2, bookroom);
                                psir.executeUpdate();
                                break;

                            case 4:
                                b = false;
                                break;

                        }
                    } while (b);
                    break;

                case 2:
                    boolean n = true;
                    do {
                        System.out.println("1. Login with Room Number and Aadhar Number");
                        System.out.println("2. Forgot Room Number");
                        System.out.println("3. Go Back" + "\n");

                        System.out.print("Enter your choise:");
                        int choise1 = sc.nextInt();

                        // switch method under main switch method case 2:
                        switch (choise1) {
                            case 1:

                                System.out.print("Enter your room.no: ");
                                int t = sc.nextInt();

                                while (true) {
                                    System.out.print("Enter Your aadharNo(WithoutSpac)= ");
                                    aadharNo = sc.nextLong();
                                    boolean fr = Methods.aadhar(aadharNo);
                                    if (fr == true) {

                                        break;
                                    }
                                }

                                String h = "select c_aadhar_no from rooms where r_no=?";
                                PreparedStatement ty = con.prepareStatement(h);
                                ty.setInt(1, t);
                                ResultSet rs = ty.executeQuery();

                                rs.next();
                                double aad = rs.getDouble(1);

                                if (aadharNo == aad) {
                                    while (true) {

                                        System.out.println("1. Food Order");
                                        System.out.println("2. Check Out");
                                        System.out.println("3. Go Back");
                                        System.out.println("Enter your choise");
                                        int g = sc.nextInt();
                                        switch (g) {

                                            case 1:
                                                String totalOrder = "";
                                                int totalAmount = 0;
                                                while (true) {
                                                    // Display Menu Card

                                                    System.out.println(
                                                            "if you want to see a maximum food price and it's details then Enter 1 otherwise 0");
                                                    int ch = sc.nextInt();
                                                    sc.next();
                                                    for (int i = 0; i < ch; i++) {
                                                        if (ch == 1) {
                                                            foodmax();

                                                        } else if (ch == 0) {
                                                            break;
                                                        }
                                                    }

                                                    System.out.println(displayFoodMenu());
                                                    System.out.print("Enter Item ID: ");
                                                    int itemID = sc.nextInt();

                                                    if (isFoodItemIDValid(itemID)) {

                                                        System.out.print("Enter Item Quantity: ");
                                                        int itemQuantity = sc.nextInt();

                                                        System.out.print("Enter your room.no:=");
                                                        int roomNo = sc.nextInt();
                                                        String curOrder = addOrderToFoodOrderTable(itemID,
                                                                itemQuantity,
                                                                roomNo);
                                                        totalOrder += curOrder;

                                                        int curAmount = totalPrice(itemID, itemQuantity);
                                                        totalAmount += curAmount;
                                                        // method call to add items to database, will return string of
                                                        // order
                                                        // history
                                                        System.out.print("Order More items ? [ Y / N ]: ");
                                                        char ans = sc.next().toLowerCase().charAt(0);
                                                        if (ans == 'n') {
                                                            System.out.println(
                                                                    "+-----------------------+-------+-------+---------------+");
                                                            System.out.println(
                                                                    "| Food Item\t\t| Price\t| Qty\t| Subtotal\t|");
                                                            System.out.println(
                                                                    "+-----------------------+-------+-------+---------------+");
                                                            System.out.print(totalOrder);
                                                            System.out.println(
                                                                    "+-----------------------+-------+-------+---------------+");
                                                            System.out.println("Total Amount: Rs." + totalAmount);
                                                            // add this totalAmount in customers table
                                                            System.out.println(addTotalAmountInCustomerTable(roomNo,
                                                                    aadharNo, totalAmount));

                                                            break;
                                                        }

                                                    } else {
                                                        System.out.println("Please Enter Valid Food ID!");
                                                    }

                                                }
                                                break;
                                            case 2:
                                                System.out.print("Enter your room.no: ");
                                                int roomNo = sc.nextInt();
                                                long aadharNo;

                                                while (true) {
                                                    System.out.print("Enter aadhar.no:");
                                                    aadharNo = sc.nextLong();
                                                    boolean m = Methods.aadhar(aadharNo);
                                                    if (m == true) {
                                                        break;
                                                    }
                                                }

                                                System.out.println(getFinalBill(roomNo, aadharNo));
                                                int grantTotalAmount = grantTotalAmount(roomNo, aadharNo);

                                                System.out.println(
                                                        "Grand Total to be Paid before Checkout: Rs."
                                                                + grantTotalAmount);

                                                while (true) {
                                                    System.out.print("Enter the Amount to Pay: ");
                                                    int payment = sc.nextInt();
                                                    if (payment == grantTotalAmount) {
                                                        System.out
                                                                .println("\n*****************************************");
                                                        System.out.println(
                                                                "Thank you for you Payment!\nPlease visit us Again!");
                                                        System.out
                                                                .println("\n*****************************************");
                                                        // method which will remove user details from all tables
                                                        removeAllCustomerDetails(roomNo, aadharNo);
                                                        System.exit(0);
                                                        break;
                                                    } else {
                                                        System.out.println(
                                                                "Please Enter Exact Amount that is Rs."
                                                                        + grantTotalAmount);
                                                    }
                                                }
                                                break;
                                            case 3:

                                                break;
                                        }
                                    }

                                }

                                break;

                            // Get Room.no with aadhar.no
                            case 2:
                                long aadharNo;

                                while (true) {
                                    System.out.print("Enter aadhar.no:");
                                    aadharNo = sc.nextLong();
                                    boolean n1 = Methods.aadhar(aadharNo);
                                    if (n1 == true) {

                                        String query = "select r_no from rooms where c_aadhar_no=?";
                                        PreparedStatement ps = con.prepareStatement(query);
                                        ps.setDouble(1, aadharNo);
                                        ResultSet rs1 = ps.executeQuery();
                                        if (rs1.next()) {
                                            int roomNo = rs1.getInt(1);
                                            System.out.println("Your room.no is:" + roomNo);
                                        } else {
                                            System.out.println("There is no Room Booked with this Aadhar Number!");
                                        }
                                        break;
                                    }
                                }

                                // Default method:
                            case 3:
                                n = false;
                                break;

                        }
                    } while (n);

                    break;

                case 3:
                    c = false;
                    break;

            }
        } while (c);
    }

    static boolean ava(int bookedRN) throws SQLException {

        String query = "select r_availability from rooms where r_no = ?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, bookedRN);
        ResultSet rs1 = ps.executeQuery();

        rs1.next();

        if (rs1.getString(1).equals("Available")) {
            return true;
        } else {
            return false;
        }
    }

    static void foodmax() throws SQLException {

        int foodprice;

        String query = "select * from food_menu";
        PreparedStatement ps = con.prepareStatement(query);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            foodprice = rs.getInt("f_price");
            bst.insert(foodprice);

        }
        BinarySearchTree bst = new BinarySearchTree();
        int getprice = BinarySearchTree.max(BinarySearchTree.root);
        System.out.println(bst.max(bst.root));

        String q = "select * from food_menu where f_price = ?";
        PreparedStatement st = con.prepareStatement(q);
        st.setInt(1, getprice);
        ResultSet rs0 = st.executeQuery();
        while (rs0.next()) {
            System.out.println("Food Id: " + rs0.getString("f_id"));
            System.out.println("Food Item: " + rs0.getString("f_item"));

        }

    }

    static String displayFoodMenu() throws SQLException {
        String query = "select * from food_menu";
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);

        String result = "+-------+-----------------------+-------+\n";
        result += "|ID\t|Food Item\t\t|Price\t|\n";
        result += "+-------+-----------------------+-------+\n";
        while (rs.next()) {
            result += "|" + rs.getInt(1) + "\t|";

            if (rs.getString(2).length() > 14) {
                result += rs.getString(2) + "\t|";
            } else {
                result += rs.getString(2) + "\t\t|";
            }

            result += rs.getInt(3) + "\t|\n";
        }
        result += "+-------+-----------------------+-------+\n";
        return result;
    }

    static boolean isFoodItemIDValid(int itemID) throws SQLException {
        String query = "select * from food_menu where f_id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, itemID);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return true;
        }
        return false;
    }

    static String addOrderToFoodOrderTable(int itemID, int itemQuantity, int roomNo) throws SQLException {
        // Fetching cur food item details [to add in order table]
        String query = "select * from food_menu where f_id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, itemID);

        ResultSet rs = ps.executeQuery();
        rs.next();

        String fName = rs.getString(2);
        int fPrice = rs.getInt(3);

        // Adding data into food Orders table

        String queryo = "insert into food_order (fid, room_no, fname,fprice,fqty,ftotal) values(?,?,?,?,?,fprice*fqty)";
        PreparedStatement pso = con.prepareStatement(queryo);
        pso.setInt(1, itemID);
        pso.setInt(2, roomNo);
        pso.setString(3, fName);
        pso.setInt(4, fPrice);
        pso.setInt(5, itemQuantity);

        String result = "";
        if (fName.length() > 14) {
            result = "| " + fName + "\t| ";
        } else {
            result = "| " + fName + "\t\t| ";
        }

        result += fPrice + "\t| ";
        result += itemQuantity + "\t| ";
        result += (fPrice * itemQuantity) + "\t\t|\n";

        return result;
    }

    static int totalPrice(int itemID, int itemQuantity) throws SQLException {

        String query = "select * from food_menu where f_id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, itemID);

        ResultSet rs = ps.executeQuery();

        rs.next();
        int fPrice = rs.getInt(1);

        int amt = fPrice * itemQuantity;
        return amt;
    }

    static String addTotalAmountInCustomerTable(int roomNo, long aadharNo, int totalAmount) throws SQLException {

        String query = "update customers set c_balance_amt = (c_balance_amt+?) where (c_aadhar_no=? and c_room_no=? and c_balance_amt != 0)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, totalAmount);
        ps.setLong(2, aadharNo);
        ps.setInt(3, roomNo);

        int affectedRow = ps.executeUpdate();
        if (affectedRow == 1) {
            return "Thank You for Ordering!\nAmount will be Added to your Account!";
        } else {
            return "There was an issue while Adding amount to your Account.\nPlease Try Again Later!";
        }
    }

    static String RoomBill(int roomNo, long aadharNo) throws SQLException {
        String result = "";

        // Making Renting Bill String
        // fetching price and details from rooms table
        String query = "select r_details,r_price from rooms where r_no=? and c_aadhar_no=?;";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, roomNo);
        ps.setLong(2, aadharNo);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String roomDetails = rs.getString(1);
        int roomPrice = rs.getInt(2);

        // fetching days booked from customer table
        String querydays = "select c_booked_days from customers where (c_aadhar_no=? and c_room_no=? and c_balance_amt != 0);";
        PreparedStatement psd = con.prepareStatement(querydays);
        psd.setLong(1, aadharNo);
        psd.setInt(2, roomNo);
        ResultSet rsd = psd.executeQuery();
        rsd.next();
        int daysBooked = rsd.getInt(1);
        result += "\n***************************************************************\n";
        result += "\t\t\tFINAL BILL\n";
        result += "\t\t\t----------\n";
        result += "\nRoom Costs: \n";
        result += "+-------+-------------------------------+-------+-------+-------+\n";
        result += "| Room\t| Details\t\t\t| Price\t| Days\t| Total\t|\n";
        result += "+-------+-------------------------------+-------+-------+-------+\n";
        result += "| " + roomNo + "\t";
        if (roomDetails.length() < 21) {
            result += "| " + roomDetails + "\t\t";
        } else {
            result += "| " + roomDetails + "\t";
        }
        result += "| " + roomPrice + "\t";
        result += "| " + daysBooked + "\t";
        result += "| " + (roomPrice * daysBooked) + "\t|\n";
        result += "+-------+-------------------------------+-------+-------+-------+\n";

        return result;
    }

    private static String TotalFoodBill(int roomNo, long aadharNo) throws SQLException {

        // Making Food Bill

        String result = "";
        String queryf = "select room_no,fname,fprice,sum(fqty),sum(ftotal) from food_order group by room_no,fname having room_no=?";
        PreparedStatement psf = con.prepareStatement(queryf);
        psf.setInt(1, roomNo);
        ResultSet rsf = psf.executeQuery();
        int foodBill = 0;
        result += "\nFood Costs: \n";
        result += "+-----------------------+-------+-------+---------------+\n";
        result += "| Food Item\t\t| Price\t| Qty\t| Subtotal\t|\n";
        result += "+-----------------------+-------+-------+---------------+\n";
        while (rsf.next()) {
            String fName = rsf.getString(2);
            int itemQuantity = rsf.getInt(4);
            int fPrice = rsf.getInt(3);
            int ftotal = rsf.getInt(5);

            if (fName.length() > 14) {
                result += "| " + fName + "\t| ";
            } else {
                result += "| " + fName + "\t\t| ";
            }

            result += fPrice + "\t| ";
            result += itemQuantity + "\t| ";
            result += (ftotal) + "\t\t|\n";

            foodBill += ftotal;
        }
        result += "+-----------------------+-------+-------+---------------+\n";
        result += "You Overall Food Bill is Rs." + foodBill + "\n";
        return result;
    }

    static String getFinalBill(int roomNo, long aadharNo) throws SQLException {

        String roomBill = RoomBill(roomNo, aadharNo);
        String finalFoodBill = TotalFoodBill(roomNo, aadharNo);

        String result = roomBill + finalFoodBill;
        return result;
    }

    static int grantTotalAmount(int roomNo, long aadharNo) throws SQLException {
        String query = "select c_balance_amt from customers where (c_aadhar_no=? and c_room_no=? and c_balance_amt != 0);";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setLong(1, aadharNo);
        ps.setInt(2, roomNo);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int grantTotalAmount = rs.getInt(1);
        return grantTotalAmount;

    }

    static void removeAllCustomerDetails(int roomNo, long aadharNo) throws SQLException {
        removeFoodOrderHistory(roomNo);
        setCustomerBalanceToZero(roomNo, aadharNo);
        removeCusDetailsFromRoomsTable(roomNo);
    }

    private static void removeFoodOrderHistory(int roomNo) throws SQLException {
        String query = "delete from food_order where room_no=?;";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, roomNo);

        ps.executeUpdate();

    }

    private static void setCustomerBalanceToZero(int roomNo, long aadharNo) throws SQLException {
        String query = "update customers set c_balance_amt=0 where (c_aadhar_no=? and c_room_no=? and c_balance_amt != 0); ";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setLong(1, aadharNo);
        ps.setInt(2, roomNo);

        ps.executeUpdate();

    }

    private static void removeCusDetailsFromRoomsTable(int roomNo) throws SQLException {
        String query = "update rooms set c_aadhar_no=null,r_availability=\"Available\" where r_no=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, roomNo);

        ps.executeUpdate();
    }
}

class BinarySearchTree {
    class Node {
        int data;
        Node left, right;

        Node(int data) {
            this.data = data;
            left = right = null;
        }
    }

    static Node root = null;

    // Insert a node into the BST
    void insert(int data) {
        Node n = new Node(data);
        if (root == null) {
            root = n;
            return;
        } else {
            Node temp = root;
            while (true) {
                if (temp.left == null && data < temp.data) {
                    temp.left = n;
                    return;
                } else if (temp.right == null && data > temp.data) {
                    temp.right = n;
                    return;
                } else {
                    if (data < temp.data)
                        temp = temp.left;
                    else
                        temp = temp.right;

                }
            }
        }
    }

    // find min from BST
    static int min(Node root) {
        while (root.left != null) {
            root = root.left;
        }
        return root.data;
    }

    // Find max from BST
    static int max(Node root) {
        while (root.right != null) {
            root = root.right;
        }
        return root.data;
    }

    // Search a given key from BST
    static void search(int key) {
        if (root == null) {
            System.out.println("Tree is empty");
            return;
        } else {
            Node temp = root;
            while (temp != null) {
                if (temp.data == key) {
                    System.out.println("Key found");
                    return;

                } else if (key < temp.data) {
                    temp = temp.left;
                } else if (key > temp.data) {
                    temp = temp.right;
                }
            }
            System.out.println("Key not found");
        }
    }

    // Delete a value from the BST
    static void delete(int value) {
        root = deleteRecursive(root, value);
    }

    static Node deleteRecursive(Node root, int value) {
        if (root == null) {
            return null;
        }
        if (value < root.data) {
            root.left = deleteRecursive(root.left, value);
        } else if (value > root.data) {
            root.right = deleteRecursive(root.right, value);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            root.data = min(root.right);
            root.right = deleteRecursive(root.right, root.data);
        }
        return root;
    }

    // print inorder of BST
    static void inorder(Node node) {
        if (node == null) {
            return;
        } else {
            inorder(node.left);
            System.out.print(node.data + " ");
            inorder(node.right);
        }
    }
}