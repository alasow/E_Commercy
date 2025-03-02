import java.util.ArrayList;
import java.util.Scanner;

// Product class to represent items
class Product {
    String name;
    double price;

    Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

// Cart class to manage the shopping cart
class Cart {
    ArrayList<Product> items = new ArrayList<>();

    void addProduct(Product product) {
        items.add(product);
        System.out.println(product.name + " added to the cart.");
    }

    void removeProduct(String productName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).name.equalsIgnoreCase(productName)) {
                System.out.println(items.get(i).name + " removed from the cart.");
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
            System.out.println("- " + product.name + " : $" + product.price);
            total += product.price;
        }
        System.out.println("Total Price: $" + total);
    }
}

// Main class
public class ShoppingCartApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart();

        // Sample products
        Product product1 = new Product("Apple", 1.50);
        Product product2 = new Product("Banana", 0.99);
        Product product3 = new Product("Orange", 1.25);

        while (true) {
            System.out.println("\nShopping Cart Menu:");
            System.out.println("1. Add Apple ($1.50)");
            System.out.println("2. Add Banana ($0.99)");
            System.out.println("3. Add Orange ($1.25)");
            System.out.println("4. Remove item");
            System.out.println("5. View cart");
            System.out.println("6. Checkout & Exit");

            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    cart.addProduct(product1);
                    break;
                case 2:
                    cart.addProduct(product2);
                    break;
                case 3:
                    cart.addProduct(product3);
                    break;
                case 4:
                    System.out.print("Enter product name to remove: ");
                    String removeItem = scanner.nextLine();
                    cart.removeProduct(removeItem);
                    break;
                case 5:
                    cart.displayCart();
                    break;
                case 6:
                    cart.displayCart();
                    System.out.println("Thanks for shopping! Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
