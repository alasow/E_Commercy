# E_Commercy

E-Commerce Shopping Cart System
Assignment Overview
This project is designed to help students apply Object-Oriented Programming (OOP) concepts such as encapsulation, inheritance, polymorphism, and abstraction in Java. The goal of the project is to build a simple E-Commerce Shopping Cart System. The system allows users to manage products in the cart, apply discounts, and perform checkout operations. It also includes an Admin panel for managing products in the catalog.

Objective
The purpose of this assignment is to:

Design and implement an E-Commerce Shopping Cart using Java.
Practice OOP concepts such as class design, encapsulation, inheritance, and polymorphism.
Create a user interface (CLI) for interaction with the system.
Develop functionality for managing products, adding/removing them from the cart, and processing orders.
Project Structure
Classes:
Product
Represents an individual product in the store.

Attributes: name, price, category, stock
Methods: Constructor, getters, toString(), reduceStock()
ShoppingCart
Manages the user's shopping cart.

Attributes: cart (List of Products), total (double)
Methods: Add product, remove product, view cart, apply discount, checkout.
User
Represents a customer user who can interact with the shopping cart.

Attributes: name, email, password, shoppingCart
Methods: Add products to the cart, view the cart, checkout.
Admin
Manages the product catalog.

Attributes: username, password
Methods: Add products to the catalog, remove products from the catalog, view catalog.
ECommerceApp
Main driver of the application that ties everything together and runs the system.

Manages user interaction, product catalog, user actions like adding products to the cart, checkout, etc.
How to Run the Application
Clone or Download the Repository:

Download the project files or clone the repository to your local machine.
Set Up Your Environment:

Ensure you have Java 8 or above installed.
If using an IDE like IntelliJ IDEA or Eclipse, open the project folder in the IDE.
Compile the Code:

If using a terminal or command line, navigate to the project folder and compile the Java files:
nginx
Copy
Edit
javac *.java
Run the Application:

# After compiling the files, run the ECommerceApp class:
nginx
Copy
Edit
java ECommerceApp
User Guide
1. Product Catalog
The admin can add and remove products from the catalog. Each product has:
Name: The product's name.
Price: The price of the product.
Category: The product's category (e.g., Electronics, Clothing).
Stock: The number of items available.
2. Shopping Cart
Users can add products to their shopping cart and remove products as needed.
The cart displays the products with their quantity and the total cost.
Users can apply a discount to their total cart price.
3. Checkout Process
When the user is ready to purchase, they can checkout and the cart will be cleared.
If the user has an insufficient balance or the product is out of stock, appropriate error messages will be displayed.
4. Admin Panel
Admin can manage products:
Add new products to the catalog.
Remove products from the catalog.
View the product catalog.
Key Functionalities
Add Product to Cart:

Users can add products by specifying the product and the quantity.
Example: Add "Smartphone" to the cart with quantity 2.
Remove Product from Cart:

Users can remove products from the cart if they no longer want them.
Example: Remove "Smartphone" from the cart.
View Cart:

Users can view the contents of their shopping cart, including product names, quantities, and total cost.
Apply Discount:

Users can apply a discount to the total cost of their cart.
Checkout:

After reviewing the cart, users can proceed to checkout, at which point their cart will be cleared.
Admin Actions:

Admin can add new products to the catalog and view existing products.
Project Structure Example
java
Copy
Edit
// Product.java
public class Product {
    private String name;
    private double price;
    private String category;
    private int stock;
    
    // Constructor, getters, reduceStock() method, etc.
}

// ShoppingCart.java
public class ShoppingCart {
    private List<Product> cart;
    private double total;

    // Methods: addProduct(), removeProduct(), applyDiscount(), checkout()
}

// User.java
public class User {
    private String name;
    private String email;
    private String password;
    private ShoppingCart shoppingCart;
    
    // Methods: addToCart(), viewCart(), checkout()
}

// Admin.java
public class Admin {
    private String username;
    private String password;

    // Methods: addProduct(), removeProduct(), viewCatalog()
}

// ECommerceApp.java
public class ECommerceApp {
    public static void main(String[] args) {
        // Run the application
    }
}
Extensions (Optional for Extra Credit)
Database Integration:

Implement database support using SQLite or MySQL to persist user data, product catalog, and orders.
Discount System:

Implement a more advanced discount system where users can receive a percentage discount based on their purchase amount or a fixed discount code.
Admin Panel Interface:

Create a command-line interface (CLI) to manage products and view the catalog from an admin's perspective.
Graphical User Interface (GUI):

Extend the project to a GUI application using JavaFX or Swing, allowing users to interact with the system through buttons, forms, and dialog boxes.
Deliverables
All Java source code files for the project.
Test cases demonstrating how the system works (e.g., adding products, checking out, applying discounts).
A brief user guide (this README) explaining how to use the system.
(Optional) Extension features for extra credit.
Grading Criteria
Correctness: The application must work as described with no critical errors or bugs.
OOP Concepts: Proper use of encapsulation, inheritance, polymorphism, and abstraction.
Code Organization: Code should be clean, well-organized, and modular.
Optional Features: Bonus points for optional extensions like database integration or a GUI.
Contact Information
If you have any questions or need help during the development of this project, feel free to contact your instructor or teaching assistant.

Good luck and have fun building your E-Commerce Shopping Cart System!

