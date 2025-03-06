
import java.util.ArrayList;
import java.util.Scanner;

import java.util.List;


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

    @Override
    public String toString() {
        return "Product{name='" + name + "', price=" + price + 
               ", category='" + category + "', stock=" + stock + "}";
    }
}

class ProductCatalog {
    private List<Product> products;

    public ProductCatalog() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void displayProducts() {
        if (products.isEmpty()) {
            System.out.println("The catalog is empty.");
        } else {
            System.out.println("Product Catalog:");
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }
}
// Cart class to manage the shopping cart
class Cart {
    ArrayList<Product> items = new ArrayList<>();

    void addProduct(Product product) {
        items.add(product);
        System.out.println(product.getName() + " added to the cart.");
    }

    void removeProduct(String productName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().equalsIgnoreCase(productName)) {
                System.out.println(items.get(i).getName() + " removed from the cart.");
                items.remove(i);
                return;
            }
        }
        System.out.println("Product not found in the cart.");
    }

    void displayCart() {
        if (items.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        System.out.println("\nItems in your cart:");
        double total = 0;
        for (Product product : items) {
            System.out.println("- " + product.getName() + " : $" + product.getPrice());
            total += product.getPrice();
        }
        System.out.println("Total Price: $" + total);
    }
}
public class ECommerceApp {
    public static void main(String[] args) {
        // Run the application
        System.out.println("E Commerce Project...Loading UPdated By Students V3");
    }
}