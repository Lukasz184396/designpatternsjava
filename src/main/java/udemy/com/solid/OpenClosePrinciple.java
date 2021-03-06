package udemy.com.solid;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/*
    MY SUMMARY - CONCLUSIONS

    In the broken option to add new filter we need to MODIFY ProductFilter
    we have to add new method so we broke rule CLOSE FOR MODIFICATION

    The solution use Open for Extension so we add
    interface Specification<T>
    interface Filter<T>
    class ColorSpecification implements Specification<Product>
    class SizeSpecification implements Specification<Product>
    class BetterFilter implements Filter<Product>

    BetterFilter is more elastic because in filer method as the second parameter
    is passed interface Specification instead of enum.
    ColorSpecification and SizeSpecification implements Specification interface
    se we can use it in place when Specification expected.

    We can use new condition in BetterFilter by passing new class which implements Specification interface.
*/
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

        //Test of new solution
        BetterFilter bf = new BetterFilter();
        System.out.println("Green products (new)");
        bf.filter(products,new ColorSpecification(Color.GREEN))
                .forEach(p-> System.out.println(" - " + p.name + " is green" ));

        //Test of new solution with couple filter conditions
        System.out.println("Large green products");
        bf.filter(products, new AndSpecification<>(
                new ColorSpecification(Color.GREEN), new SizeSpecification(Size.LARGE)))
                .forEach(p-> System.out.println(" - " + p.name + " is large and green" ));
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


// solution
interface Specification<T> {
    boolean isSatisfied(T item);
}

interface Filter<T> {
    Stream<T> filter (List<T> items, Specification<T> spec);
}

class ColorSpecification implements Specification<Product> {

    private Color color;

    public ColorSpecification(Color color) {
        this.color = color;
    }

    @Override
    public boolean isSatisfied(Product item) {
        return item.color == color;
    }
}

class SizeSpecification implements Specification<Product> {
    private Size size;

    public SizeSpecification(Size size) {
        this.size = size;
    }

    @Override
    public boolean isSatisfied(Product item) {
        return item.size == size;
    }
}

class BetterFilter implements Filter<Product> {

    @Override
    public Stream<Product> filter(List<Product> items, Specification<Product> spec) {
        return items.stream().filter(p-> spec.isSatisfied(p));
    }
}

// Added combination of multiple filter conditions
class AndSpecification<T> implements Specification<T> {

    private Specification<T> first, second;

    public AndSpecification(Specification<T> first, Specification<T> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isSatisfied(T item) {
        return first.isSatisfied(item) && second.isSatisfied(item);
    }
}