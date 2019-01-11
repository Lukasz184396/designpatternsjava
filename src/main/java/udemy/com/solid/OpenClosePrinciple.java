package udemy.com.solid;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

//todo fix broken Open Close Principle

public class OpenClosePrinciple {
    public static void main(String[] args) {

        Product apple = new Product("Apple", Color.GREEN, Size.SMALL);
        Product tree = new Product("Tree", Color.GREEN, Size.LARGE);
        Product house = new Product("House", Color.BLUE, Size.LARGE);

        List<Product> products = Arrays.asList(apple, tree, house);

        ProductFilter pf = new ProductFilter();
        System.out.println("Green products (old)");
        pf.filterByColor(products, Color.GREEN)
                .forEach(p-> System.out.println(" - " + p.name + " is green" ));
    }
}

enum Color {
    RED, GREEN, BLUE
}

enum Size {
    SMALL, MEDIUM, LARGE, HUGE
}

class Product {
    public String name;
    public Color color;
    public Size size;

    public Product(String name, Color color, Size size) {
        this.name = name;
        this.color = color;
        this.size = size;
    }
}

class ProductFilter {

    public Stream<Product> filterByColor (
            List<Product> products,
            final Color color) {
        return products.stream().filter(p->p.color == color);
    }

    public Stream<Product> filterBySize (
            List<Product> products,
            final Size size) {
        return products.stream().filter(p->p.size == size);
    }
    public Stream<Product> filterBySizeAndColor(
            List<Product> products,
            final Size size,
            final Color color) {
        return products.stream().filter( p -> p.size ==size && p.color == color);
    }
}