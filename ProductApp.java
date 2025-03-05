import java.util.ArrayList;
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

public class ProductApp {
    public static void main(String[] args) {
        ProductCatalog catalog = new ProductCatalog();

        // Create multiple products and add them to the catalog
        Product[] products = {
            new Product("Laptop", 1200.99, "Electronics", 5),
            new Product("Smartphone", 699.99, "Electronics", 10),
            new Product("Headphones", 149.99, "Accessories", 8),
            new Product("Backpack", 49.99, "Fashion", 15),
            new Product("Water Bottle", 19.99, "Home", 20)
        };

        for (Product product : products) {
            catalog.addProduct(product);
        }

        // Display all products
        catalog.displayProducts();
    }
}
