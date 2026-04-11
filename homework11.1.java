abstract class Product {
    protected int id;
    protected String name;
    protected double price;

    public abstract void create();
}

class Electronics extends Product {
    @Override
    public void create() {
        System.out.println("Создан электронный товар: " + name);
    }
}

class Clothing extends Product {
    @Override
    public void create() {
        System.out.println("Создан товар одежды: " + name);
    }
}

class ProductFactory {
    public static Product createProduct(String type) {
        switch (type) {
            case "electronics":
                return new Electronics();
            case "clothing":
                return new Clothing();
            default:
                throw new IllegalArgumentException("Неизвестный тип товара: " + type);
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Product p1 = ProductFactory.createProduct("electronics");
        p1.name = "Смартфон";
        p1.create();

        Product p2 = ProductFactory.createProduct("clothing");
        p2.name = "Футболка";
        p2.create();
    }
}
