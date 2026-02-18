import java.io.*;
import java.util.*;

class ConfigurationManager {
    private static volatile ConfigurationManager instance;
    private Map<String, String> settings;

    private ConfigurationManager() {
        settings = new HashMap<>();
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            synchronized (ConfigurationManager.class) {
                if (instance == null) {
                    instance = new ConfigurationManager();
                }
            }
        }
        return instance;
    }

    public void setSetting(String key, String value) {
        settings.put(key, value);
    }

    public String getSetting(String key) {
        return settings.getOrDefault(key, "Не найдено");
    }

    public void loadFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, String> entry : settings.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        }
    }
}

class Report {
    private String header;
    private String content;
    private String footer;

    public void setHeader(String header) { this.header = header; }
    public void setContent(String content) { this.content = content; }
    public void setFooter(String footer) { this.footer = footer; }

    @Override
    public String toString() {
        return header + "\n" + content + "\n" + footer;
    }
}

interface IReportBuilder {
    void setHeader(String header);
    void setContent(String content);
    void setFooter(String footer);
    Report getReport();
}

class TextReportBuilder implements IReportBuilder {
    private Report report = new Report();

    public void setHeader(String header) { report.setHeader("TEXT HEADER: " + header); }
    public void setContent(String content) { report.setContent("TEXT CONTENT: " + content); }
    public void setFooter(String footer) { report.setFooter("TEXT FOOTER: " + footer); }
    public Report getReport() { return report; }
}

class HtmlReportBuilder implements IReportBuilder {
    private Report report = new Report();

    public void setHeader(String header) { report.setHeader("<h1>" + header + "</h1>"); }
    public void setContent(String content) { report.setContent("<p>" + content + "</p>"); }
    public void setFooter(String footer) { report.setFooter("<footer>" + footer + "</footer>"); }
    public Report getReport() { return report; }
}

class ReportDirector {
    public Report constructReport(IReportBuilder builder, String header, String content, String footer) {
        builder.setHeader(header);
        builder.setContent(content);
        builder.setFooter(footer);
        return builder.getReport();
    }
}

interface Prototype<T> {
    T clone();
}

class Product implements Prototype<Product> {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name; this.price = price; this.quantity = quantity;
    }

    @Override
    public Product clone() {
        return new Product(name, price, quantity);
    }

    @Override
    public String toString() {
        return name + " x" + quantity + " = " + price;
    }
}

class Discount implements Prototype<Discount> {
    private String description;
    private double percentage;

    public Discount(String description, double percentage) {
        this.description = description; this.percentage = percentage;
    }

    @Override
    public Discount clone() {
        return new Discount(description, percentage);
    }

    @Override
    public String toString() {
        return description + " (" + percentage + "%)";
    }
}

class Order implements Prototype<Order> {
    private List<Product> products = new ArrayList<>();
    private double deliveryCost;
    private Discount discount;
    private String paymentMethod;

    public void addProduct(Product product) { products.add(product); }
    public void setDeliveryCost(double cost) { this.deliveryCost = cost; }
    public void setDiscount(Discount discount) { this.discount = discount; }
    public void setPaymentMethod(String method) { this.paymentMethod = method; }

    @Override
    public Order clone() {
        Order copy = new Order();
        for (Product p : products) {
            copy.addProduct(p.clone());
        }
        copy.setDeliveryCost(deliveryCost);
        if (discount != null) copy.setDiscount(discount.clone());
        copy.setPaymentMethod(paymentMethod);
        return copy;
    }

    @Override
    public String toString() {
        return "Order: " + products + ", Delivery=" + deliveryCost +
                ", Discount=" + discount + ", Payment=" + paymentMethod;
    }
}

public class AllPatternsTest {
    public static void main(String[] args) throws Exception {
        ConfigurationManager config1 = ConfigurationManager.getInstance();
        config1.setSetting("theme", "dark");
        ConfigurationManager config2 = ConfigurationManager.getInstance();
        System.out.println("Singleton test: " + config2.getSetting("theme"));

        ReportDirector director = new ReportDirector();
        Report textReport = director.constructReport(new TextReportBuilder(),
                "Отчёт", "Содержимое отчёта", "Конец");
        System.out.println("\nText Report:\n" + textReport);

        Report htmlReport = director.constructReport(new HtmlReportBuilder(),
                "HTML Report", "Some content", "Footer info");
        System.out.println("\nHTML Report:\n" + htmlReport);

        Order order1 = new Order();
        order1.addProduct(new Product("Laptop", 1200, 1));
        order1.addProduct(new Product("Mouse", 25, 2));
        order1.setDeliveryCost(15);
        order1.setDiscount(new Discount("New Year Sale", 10));
        order1.setPaymentMethod("Credit Card");

        Order order2 = order1.clone();
        order2.setPaymentMethod("PayPal");

        System.out.println("\nPrototype test:");
        System.out.println(order1);
        System.out.println(order2);
    }
}
