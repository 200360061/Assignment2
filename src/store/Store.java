/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package store;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Store {

    private static Scanner sc = new Scanner(System.in);
    private static ArrayList<Product> products = new ArrayList<Product>();

    /**
     * Method to perform required tasks
     *  Login
     *  Create new user
     *  Database connectivity
     *  Password Generator using string and salt
     */
    public static void Tasks() {
        Connection conn = null;
        Statement statement = null;
        System.out.println("Login / New User ? (L/N)");
        char c = sc.next().toLowerCase().charAt(0);
        sc.nextLine();
        // Switch case to Select specific task
        switch (c) {
            case 'n': {
                
                System.out.print("Enter User ID:");
                String userID = sc.nextLine();
                System.out.print("Enter Password:");
                String password = sc.nextLine();
                try {
                    // Creating new User
                    User newUser = new User(userID, password);
                    // Creating connection
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gagandeep", "root", "");
                    PreparedStatement preparedStatement = null;
                    // Sql statement
                    String sql = "Insert into Users values(?,?,?)";
                    preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, userID);
                    preparedStatement.setString(2, PasswordGenerator.getSHA512Password(password, newUser.getSalt()));
                    preparedStatement.setBlob(3, new javax.sql.rowset.serial.SerialBlob(newUser.getSalt()));
                    preparedStatement.execute();
                    System.out.println("Registeration Successfull");
                    Tasks();
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                    Tasks();
                }
                break;
            }
            case 'l': {
                try {
                    System.out.print("Enter User ID:");
                    String userID = sc.nextLine();
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gagandeep", "root", "");
                    ResultSet rs = null;
                    statement = conn.createStatement();
                    // Select username for user
                    rs = statement.executeQuery("Select * from Users where userId = '" + userID + "'");
                    rs.last();
                    if (rs.getRow() == 0) {
                        System.out.println("User Doesn't Exist");
                    } else {
                        // enter password if user exists
                        System.out.print("Enter Password:");
                        String password = sc.nextLine();
                        Blob blob = rs.getBlob("salt");
                        byte[] salt = blob.getBytes(1, (int) blob.length());
                        // if password is same as entered before than show all productd
                        if (PasswordGenerator.getSHA512Password(password, salt).equals(rs.getString("Password"))) {
                            showProducts();
                        }

                    }
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                    Tasks();
                }
                break;

            }
            default: {
                System.out.println("Please Enter Valid Option");
                Tasks();
            }
        }

    }

    /**
     * Method to show all products and Start productTasks
     */
    
    public static void showProducts() {
        // Stream foreach to show products
        products.stream().map(p -> p).forEach(p -> System.out.println(p.toString()));
        productTasks();
    }
/**
 * Method to use stream on product tasks
 */
    public static void productTasks() {
        /*
        Task Names
        */
        System.out.println("1.) Use Filter for Product");
        System.out.println("2.) Sort Products Pricewise");
        System.out.println("3.) Sort Products Namewise");
        System.out.println("4.) Print All Products");
        System.out.println("5.) Add new Product");
        System.out.println("6.) Total Price of Store Products");
        System.out.println("7.) Exit");
        int choice = sc.nextInt();
        sc.nextLine();
        /*
        choice for task
        */
        switch (choice) {
            case 1: {
                System.out.println("Enter Product Name");
                String productFilter = sc.nextLine();
                /*
                using stream filter to filter products
                */
                List<Product> filteredList = products.stream().filter(product -> product.getName().startsWith(productFilter)).collect(Collectors.toList());
                filteredList.stream().map(product -> product).forEach(product -> System.out.println(product.toString()));
                productTasks();
                break;
            }
            case 2: {
                /*
                Using stream sorted to sort products and comparator to sort using specific property of object
                */
                List<Product> sortedList = products.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
                sortedList.stream().map(product -> product).forEach(product -> System.out.println(product.toString()));
                productTasks();
                break;

            }
            case 3: {
                
                /*
                Using stream sorted to sort products and comparator to sort using specific property of object
                */
                List<Product> sortedList = products.stream().sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
                sortedList.stream().map(product -> product).forEach(product -> System.out.println(product.toString()));
                productTasks();
                break;

            }
            case 4: {
                /*
                Using stream sorted to sort products and comparator to sort using specific property of object
                */
                products.stream().map(p -> p).forEach(p -> System.out.println(p.toString()));
                productTasks();
                break;
            }
            case 5: {
                /**
                 * Adding to book
                 */
                String pName;
                int pPrice;
                System.out.println("Enter new Product Name : ");
                pName = sc.nextLine();
                System.out.println("Enter new Product Price : ");
                pPrice = sc.nextInt();
                Product p = new Product(pName, pPrice);
                products.add(p);
                System.out.println("Product Added : ");
                p.showProductdetail();
                productTasks();
                break;

            }
            case 6: {
                /**
                 * Sum of all 
                 */
                int Sum = products.stream().mapToInt(product -> product.getPrice()).sum();
                System.out.println("Total Value of all products : " + Sum);
                productTasks();
                break;
            }
            default: {
                break;
            }
        }
    }
    public static void main(String[] args) {
        products.add(new Product("Laptop", 204));
        products.add(new Product("Macbook", 545));
        products.add(new Product("Cellphone", 891));
        products.add(new Product("Chargers", 884));
        Tasks();
    }
}
