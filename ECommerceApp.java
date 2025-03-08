
import java.util.ArrayList;
import java.util.Scanner;

import java.util.List;

// Team One: 
class Product {
    private String name;
    private double price;
    private String category;
    private int stock;

    public Product(String name, double price, String category, int stock) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getStock() { return stock; }
    public void setPrice(double price) {
        this.price = price;
    }
    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price + 
               ", category='" + category + "', stock=" + stock + "}";
    }

    // Missed By Team :
  // Method to reduce stock
  public boolean reduceStock(int quantity) {
    if (quantity > 0 && quantity <= stock) {
        stock -= quantity;
        return true;
    }
    return false; // Not enough stock or invalid quantity
}

}


// Team Two : 
// Cart class to manage the shopping cart

 class ShoppingCart {     // Naming issue
    private List<Product> cart;
    private double total;

    // Constructor
    public ShoppingCart() {
        this.cart = new ArrayList<>();
        this.total = 0.0;
    }

    // Method to add a product to the cart
    public void addProduct(Product product, int quantity) {      // Team adds one by one with out maintaining stock status
        if (product != null && quantity > 0 && product.getStock() >= quantity) {
            for (int i = 0; i < quantity; i++) {
                cart.add(product);
            }
            total += product.getPrice() * quantity;
            product.reduceStock(quantity);
        }
    }

    // Method to remove a product from the cart
    public void removeProduct(Product product, int quantity) {
        if (cart.contains(product) && quantity > 0) {
            int removedCount = 0;
            List<Product> toRemove = new ArrayList<>();
            for (Product p : cart) {
                if (p.equals(product) && removedCount < quantity) {
                    toRemove.add(p);
                    removedCount++;
                }
            }
            cart.removeAll(toRemove);
            total -= product.getPrice() * removedCount;
            product.reduceStock(-removedCount); // Restocking removed items
        }
    }

    // Method to get the total price of the cart
    public double getTotal() {
        return total;
    }

    // Method to get cart items
    public List<Product> getCartItems() {
        return new ArrayList<>(cart);
    }

    // Method to clear the cart
    public void clearCart() {
        for (Product product : cart) {
            product.reduceStock(-1); // Restocking each removed item
        }
        cart.clear();
        total = 0.0;
    }

    // Method to display cart contents
    @Override
    public String toString() {
        return "ShoppingCart{cart=" + cart + ", total=" + total + "}";
    }
}

// Team Three :
 class User {
    private String name;
    private String email;
    private String password;
    private ShoppingCart shoppingCart;  // Team missed this
    private boolean isLoggedIn;
    // Constructor
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.shoppingCart = new ShoppingCart(); // Each user has a shopping cart
    }

    

    // Getter methods
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

// Method to log in
    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            isLoggedIn = true;
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Invalid credentials.");
            return false;
        }
    }
     // Method to checkout
     public void checkout() {
        if (isLoggedIn) {
            System.out.println("Checkout successful! Your total is: " + shoppingCart.getTotal());
            shoppingCart.clearCart();
        } else {
            System.out.println("Please log in to proceed with checkout.");
        }
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    // Method to update user details
    public void updateUserDetails(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Method to display user details
    @Override
    public String toString() {
        return "User{name='" + name + "', email='" + email + "'}";
    }
}

// Team Four:

 class Admin {
    private String username;
    private String password;
    private boolean isLoggedIn;

    // Constructor
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = false;
    }

// Login method
public boolean login(String username, String password) {
    if (this.username.equals(username) && this.password.equals(password)) {
        isLoggedIn = true;
        System.out.println("Login successful!");
        return true;
    } else {
        System.out.println("Invalid credentials, please try again.");
        return false;
    }
}

// Check if admin is logged in
public boolean isLoggedIn() {
    return isLoggedIn;
}

    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Method to add a product to the catalog
   // Add a product (only if logged in)
   public void addProduct(Product product) {
    if (!isLoggedIn) {
        System.out.println("Please log in before adding a product.");
        return;
    }
    // Add the product (implementation not shown)
    System.out.println("Product added: " + product.getName());
}
 // Update product price (only if logged in)
public void updateProductPrice(Product product, double newPrice) {
    if (!isLoggedIn) {
        System.out.println("Please log in before updating product price.");
        return;
    }
    // Update the product price (implementation not shown)
    product.setPrice(newPrice);
    System.out.println("Updated price of " + product.getName() + " to " + newPrice);
}

    // Method to remove a product from the catalog
    public void removeProduct(List<Product> catalog, Product product) {
        if (!isLoggedIn) {
            System.out.println("Please log in before updating product price.");
            return;
        }

        catalog.remove(product);
        System.out.println("Product Of " + product.getName() + " Is Successfully removed ");
    } 

    // Method to display admin details
    @Override
    public String toString() {
        return "Admin{username='" + username + "'}";
    }
}

public class ECommerceApp {
    public static void main(String[] args) {
        // Run the application
        System.out.println("E Commerce Project..................");
         // Step 1: Create Admin and Login
         Admin admin = new Admin("admin123", "adminpass");
         System.out.println("\nAdmin trying to log in...");
         admin.login("admin123", "adminpass");
 
         // Step 2: Create Products
         List<Product> catalog = new ArrayList<>();
         Product product1 = new Product("Laptop", 800.0, "Electronics", 10);
         Product product2 = new Product("Phone", 500.0, "Electronics", 15);
         Product product3 = new Product("Headphones", 100.0, "Accessories", 20);
 
         catalog.add(product1);
         catalog.add(product2);
         catalog.add(product3);
 
         System.out.println("\nAdmin adding products...");
         admin.addProduct(product1);
         admin.addProduct(product2);
         admin.addProduct(product3);
 
         // Step 3: Create Users
         User user1 = new User("Alasow", "Alasow@example.com", "alicepass");
         User user2 = new User("Asli", "Asli@example.com", "aslipass");
 
         System.out.println("\nUsers created: ");
         System.out.println(user1);
         System.out.println(user2);
 
         // Step 4: Log in Users
         System.out.println("\n Alasow logging in...");
         user1.login("Alasow@example.com", "alicepass");
 
         System.out.println("\nAsli logging in...");
         user2.login("Asli@example.com", "aslipass");
 
         // Step 5: Shopping Cart Operations
         ShoppingCart cart1 = user1.getShoppingCart();
         ShoppingCart cart2 = user2.getShoppingCart();
 
         System.out.println("\nAlasow adding products to cart...");
         cart1.addProduct(product1, 2);
         cart1.addProduct(product3, 1);
 
         System.out.println("\nAsli adding products to cart...");
         cart2.addProduct(product2, 1);
         cart2.addProduct(product3, 2);
 
         System.out.println("\nAlasow's cart: " + cart1);
         System.out.println("Asli's cart: " + cart2);
 
         // Step 6: Removing Products
         System.out.println("\nAlasow removing 1 Laptop...");
         cart1.removeProduct(product1, 1);
         System.out.println("Alasow's cart after removal: " + cart1);
 
         // Step 7: Checkout
         System.out.println("\nAlasow proceeding to checkout...");
         user1.checkout();
 
         System.out.println("\nAsli proceeding to checkout...");
         user2.checkout();
    }
}