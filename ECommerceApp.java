import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;
import java.util.*;

 
 class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Mcxxxxxxxx"; // Or your actual password

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}





// Team One: 

 class Product {
    private int productID;
    private String name;
    private double price;
    private String category;
    private int stock;

    public Product(int productID, String name, double price, String category, int stock) {
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public Product(String name, double price, String category, int stock) {
        this(-1, name, price, category, stock);
    }

public void addStock(int quantity) {
    this.stock += quantity;
}

    public int getProductID() { return productID; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = stock; }
    public void setPrice(double price) { this.price = price; }

    public boolean reduceStock(int quantity) {
        if (quantity > 0 && quantity <= stock) {
            stock -= quantity;
            updateProductStockInDB();
            return true;
        }
        return false;
    }

   public static List<Product> loadProductsFromDatabase() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Product";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                    rs.getInt("ProductID"),
                    rs.getString("Name"),
                    rs.getDouble("Price"),
                    rs.getString("Category"),
                    rs.getInt("Stock")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }




    private void updateProductStockInDB() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE Product SET Stock = ? WHERE ProductID = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, stock);
            statement.setInt(2, this.productID);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating stock in database.");
            e.printStackTrace();
        }
    }

    public static boolean updatePrice(int productId, double newPrice) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE Product SET Price=? WHERE ProductID=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, newPrice);
            stmt.setInt(2, productId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating product price in database.");
            e.printStackTrace();
            return false;
        }
    }

    public static Product getProductById(int id) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Product WHERE ProductID = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Product(
                    resultSet.getInt("ProductID"),
                    resultSet.getString("Name"),
                    resultSet.getDouble("Price"),
                    resultSet.getString("Category"),
                    resultSet.getInt("Stock")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static boolean addProductToDatabase(String name, double price, String category, int stock) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Product(Name, Price, Category, Stock) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, category);
            stmt.setInt(4, stock);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error inserting product into database.");
            e.printStackTrace();
            return false;
        }
    }



    public void insertIntoDatabase() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Product (Name, Price, Category, Stock) VALUES (?, ?, ?, ?)";
            var statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setString(3, category);
            statement.setInt(4, stock);
            statement.executeUpdate();

            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                this.productID = generatedKeys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error inserting product into database.");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Product{productID=" + productID + ", name='" + name + "', price=" + price +
                ", category='" + category + "', stock=" + stock + "}";
    }
}

class ShoppingCart {
    private List<Product> cart;
    private double total;

    public ShoppingCart() {
        this.cart = new ArrayList<>();
        this.total = 0.0;
    }

    public void addProduct(Product product, int quantity, User user) {
        if (product != null && quantity > 0 && product.getStock() >= quantity) {
            for (int i = 0; i < quantity; i++) {
                cart.add(product);
            }
            total += product.getPrice() * quantity;
            product.reduceStock(quantity);
            addProductToDB(product, quantity, user);
        }
    }


public void addProductToDB(Product product, int quantity, User user) {
    try (Connection connection = DatabaseConnection.getConnection()) {
        // Check if the user has an existing cart
        String cartQuery = "SELECT CartID FROM ShoppingCart WHERE UserID = ? ORDER BY CreatedAt DESC LIMIT 1";
        PreparedStatement cartStmt = connection.prepareStatement(cartQuery);
        cartStmt.setInt(1, user.getUserID());
        ResultSet rs = cartStmt.executeQuery();

        int cartID = -1;
        if (rs.next()) {
            cartID = rs.getInt("CartID");
        } else {
            // If no existing cart, create a new one
            String insertCart = "INSERT INTO ShoppingCart (UserID, CreatedAt) VALUES (?, NOW())";
            PreparedStatement insertCartStmt = connection.prepareStatement(insertCart, Statement.RETURN_GENERATED_KEYS);
            insertCartStmt.setInt(1, user.getUserID());
            insertCartStmt.executeUpdate();

            ResultSet generatedKeys = insertCartStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                cartID = generatedKeys.getInt(1);
            }
        }

        if (cartID != -1) {
            // Insert the product into the CartItem table
            String insertItem = "INSERT INTO CartItem (CartID, ProductID, Quantity) VALUES (?, ?, ?)";
            PreparedStatement insertItemStmt = connection.prepareStatement(insertItem);
            insertItemStmt.setInt(1, cartID);
            insertItemStmt.setInt(2, product.getProductID());
            insertItemStmt.setInt(3, quantity);
            insertItemStmt.executeUpdate();
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    public boolean removeProduct(Product product, int quantity, User user) {
        if (product != null && quantity > 0) {
            int count = 0;
            // Count how many times the product exists in the cart
            for (Product p : cart) {
                if (p.equals(product)) {
                    count++;
                }
            }

            if (count >= quantity) {
                // Remove the product from the cart
                for (int i = 0; i < quantity; i++) {
                    cart.remove(product);
                }
                total -= product.getPrice() * quantity;
                product.addStock(quantity);  // Add the quantity back to stock
                removeProductFromDB(product, quantity, user);
                System.out.println("Removed " + quantity + " of " + product.getName() + " from the cart.");
                return true;
            } else {
                System.out.println("Not enough quantity in the cart to remove.");
                return false;
            }
        }
        return false;
    }

    private void removeProductFromDB(Product product, int quantity, User user) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Get the current cart ID for the user
            String cartQuery = "SELECT CartID FROM ShoppingCart WHERE UserID = ? ORDER BY CreatedAt DESC LIMIT 1";
            PreparedStatement cartStmt = connection.prepareStatement(cartQuery);
            cartStmt.setInt(1, user.getUserID());
            ResultSet rs = cartStmt.executeQuery();

            int cartID = -1;
            if (rs.next()) {
                cartID = rs.getInt("CartID");
            }

            if (cartID != -1) {
                String deleteItemQuery = "DELETE FROM CartItem WHERE CartID = ? AND ProductID = ?";
                PreparedStatement deleteItemStmt = connection.prepareStatement(deleteItemQuery);
                deleteItemStmt.setInt(1, cartID);
                deleteItemStmt.setInt(2, product.getProductID());
                deleteItemStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkout(User user) {
        if (!cart.isEmpty()) {
            System.out.println("Checkout successful. Total: $" + total);
            clearCart(user);
        } else {
            System.out.println("Cart is empty.");
        }
    }

    public void clearCart(User user) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String deleteItems = "DELETE FROM CartItem WHERE CartID IN (SELECT CartID FROM ShoppingCart WHERE UserID = ?)";
            var statement = connection.prepareStatement(deleteItems);
            statement.setInt(1, user.getUserID());
            statement.executeUpdate();
            cart.clear();
            total = 0.0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getTotal() {
        return total;
    }

    public List<Product> getCartItems() {
        return cart;
    }
}

class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private ShoppingCart shoppingCart;
    private boolean isLoggedIn;

    public User(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.shoppingCart = new ShoppingCart();
    }

    public boolean login(String email, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            var statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                this.id = resultSet.getInt("id");
                this.name = resultSet.getString("name");
                this.email = resultSet.getString("email");
                this.password = resultSet.getString("password");
                isLoggedIn = true;
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        isLoggedIn = false;
        return false;
    }

    public static boolean registerUser(String name, String email, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO Users (name, email, password) VALUES (?, ?, ?)";
            var statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static User getUserByEmail(String email) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Users WHERE email = ?";
            var statement = connection.prepareStatement(query);
            statement.setString(1, email);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUserID() {
        return id;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}


class Admin {
    private String username;
    private String password;

   

    private static boolean isLoggedIn = false;
    // Constructor
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }

    // Login method - Authenticate admin with DB
    public static boolean login(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM Admin WHERE Username = ? AND Password = ?";
            var statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            var resultSet = statement.executeQuery();

            if (resultSet.next()) {
                username = resultSet.getString("Username");
                password = resultSet.getString("Password");
                isLoggedIn = true;
                System.out.println("Login successful!");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Invalid credentials, please try again.");
        return false;
    }

    // Check if admin is logged in
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Method to add a product to the catalog (only if logged in)
    public void addProduct(Product product) {
        if (!isLoggedIn) {
            System.out.println("Please log in before adding a product.");
            return;
        }
        product.insertIntoDatabase();  // Insert product into the database
        System.out.println("Product added: " + product.getName());
    }

    // Update product price (only if logged in)
    public void updateProductPrice(Product product, double newPrice) {
        if (!isLoggedIn) {
            System.out.println("Please log in before updating product price.");
            return;
        }
        product.setPrice(newPrice);
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "UPDATE Product SET Price = ? WHERE ProductID = ?";
            var statement = connection.prepareStatement(query);
            statement.setDouble(1, newPrice);
            statement.setInt(2, product.getProductID());
            statement.executeUpdate();
            System.out.println("Updated price of " + product.getName() + " to " + newPrice);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to remove a product from the catalog (only if logged in)
    public void removeProduct(List<Product> catalog, Product product) {
        if (!isLoggedIn) {
            System.out.println("Please log in before updating product.");
            return;
        }
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM Product WHERE ProductID = ?";
            var statement = connection.prepareStatement(query);
            statement.setInt(1, product.getProductID());
            statement.executeUpdate();
            catalog.remove(product);
            System.out.println("Product " + product.getName() + " has been successfully removed.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to display admin details
    @Override
    public String toString() {
        return "Admin{username='" + username + "'}";
    }
}

public class ECommerceApp {
    static Scanner scanner = new Scanner(System.in);
    static List<Product> catalog = new ArrayList<>();
    static User currentUser;

    public static void main(String[] args) {
        boolean loggedIn = false;
        boolean running = true;

        while (running) {
            if (!loggedIn) {
                System.out.println("\n1. Register\n2. Login\n3. Admin Login\n4. Exit");
                System.out.print("Select an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        if (User.registerUser(name, email, password)) {
                            System.out.println("Registration successful.");
                        } else {
                            System.out.println("Registration failed.");
                        }
                    }
                    case 2 -> {
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        User user = User.getUserByEmail(email);
                        if (user != null && user.login(email, password)) {
                            currentUser = user;
                            loggedIn = true;
                            userMenu();
                        } else {
                            System.out.println("Login failed.");
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter admin username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter admin password: ");
                        String password = scanner.nextLine();
                        if (Admin.login(username, password)) {
                            System.out.println("Admin login successful.");
                            adminMenu();
                        } else {
                            System.out.println("Invalid admin credentials.");
                        }
                    }
                    case 4 -> {
                        running = false;
                        System.out.println("Goodbye!");
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        }
    }

    static void userMenu() {
        boolean running = true;
        ShoppingCart cart = currentUser.getShoppingCart();

        while (running) {
            System.out.println("\n===== User Menu =====");
            System.out.println("1. View Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. Remove Product from Cart");
            System.out.println("4. View Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Logout");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.println("\nProduct Catalog:");
                    catalog = Product.loadProductsFromDatabase();
                    if (catalog.isEmpty()) {
                        System.out.println("No products available.");
                    } else {
                        for (int i = 0; i < catalog.size(); i++) {
                            System.out.println((i + 1) + ". " + catalog.get(i));
                        }
                    }
                }
                case 2 -> {
                    if (catalog.isEmpty()) {
                        System.out.println("No products loaded. View products first.");
                        break;
                    }
                    System.out.print("Enter product number: ");
                    int index = scanner.nextInt() - 1;
                    System.out.print("Enter quantity: ");
                    int qty = scanner.nextInt();
                    scanner.nextLine();
                    if (index >= 0 && index < catalog.size()) {
                        Product p = catalog.get(index);
                        cart.addProduct(p, qty, currentUser);
                    } else {
                        System.out.println("Invalid product.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter product number to remove: ");
                    int index = scanner.nextInt() - 1;
                    System.out.print("Enter quantity: ");
                    int qty = scanner.nextInt();
                    scanner.nextLine();
                    if (index >= 0 && index < catalog.size()) {
                        Product p = catalog.get(index);
                        if (cart.removeProduct(p, qty, currentUser)) {
                            System.out.println("Removed from cart.");
                        } else {
                            System.out.println("Item not found or quantity too high.");
                        }
                    } else {
                        System.out.println("Invalid product selection.");
                    }
                }
                case 4 -> {
                    System.out.println("\nCart Contents:");
                    List<Product> items = cart.getCartItems();
                    if (items.isEmpty()) {
                        System.out.println("Cart is empty.");
                    } else {
                        for (Product p : items) {
                            System.out.println(p);
                        }
                        System.out.println("Total: $" + cart.getTotal());
                    }
                }
                case 5 -> cart.checkout(currentUser);
                case 6 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void adminMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product Price");
            System.out.println("3. Create User");
            System.out.println("4. View All Products");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter stock: ");
                    int stock = scanner.nextInt();
                    scanner.nextLine();

                    if (Product.addProductToDatabase(name, price, category, stock)) {
                        System.out.println("Product added successfully.");
                    } else {
                        System.out.println("Failed to add product.");
                    }
                }

                case 2 -> {
                    System.out.print("Enter Product ID to update: ");
                    int productId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new price: ");
                    double newPrice = scanner.nextDouble();
                    scanner.nextLine();

                    if (Product.updatePrice(productId, newPrice)) {
                        System.out.println("Price updated.");
                    } else {
                        System.out.println("Update failed.");
                    }
                }

                case 3 -> {
                    System.out.print("Enter new user's name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();

                    if (User.registerUser(name, email, password)) {
                        System.out.println("User created.");
                    } else {
                        System.out.println("Failed to create user.");
                    }
                }

                case 4 -> {
                    List<Product> all = Product.loadProductsFromDatabase();
                    if (all.isEmpty()) {
                        System.out.println("No products found.");
                    } else {
                        for (Product p : all) {
                            System.out.println(p);
                        }
                    }
                }

                case 5 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

   
}
