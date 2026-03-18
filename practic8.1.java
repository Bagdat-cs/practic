interface Beverage {
    String getDescription();
    double cost();
}

class Espresso implements Beverage {
    @Override
    public String getDescription() {
        return "Espresso";
    }

    @Override
    public double cost() {
        return 3.0;
    }
}

class Tea implements Beverage {
    @Override
    public String getDescription() {
        return "Tea";
    }

    @Override
    public double cost() {
        return 2.5;
    }
}

class Latte implements Beverage {
    @Override
    public String getDescription() {
        return "Latte";
    }

    @Override
    public double cost() {
        return 4.0;
    }
}

class Mocha implements Beverage {
    @Override
    public String getDescription() {
        return "Mocha";
    }

    @Override
    public double cost() {
        return 4.5;
    }
}

abstract class BeverageDecorator implements Beverage {
    protected Beverage beverage;

    public BeverageDecorator(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription();
    }

    @Override
    public double cost() {
        return beverage.cost();
    }
}

class Milk extends BeverageDecorator {
    public Milk(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Milk";
    }

    @Override
    public double cost() {
        return beverage.cost() + 0.5;
    }
}

class Sugar extends BeverageDecorator {
    public Sugar(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Sugar";
    }

    @Override
    public double cost() {
        return beverage.cost() + 0.2;
    }
}

class WhippedCream extends BeverageDecorator {
    public WhippedCream(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Whipped Cream";
    }

    @Override
    public double cost() {
        return beverage.cost() + 0.7;
    }
}

class VanillaSyrup extends BeverageDecorator {
    public VanillaSyrup(Beverage beverage) {
        super(beverage);
    }

    @Override
    public String getDescription() {
        return beverage.getDescription() + ", Vanilla Syrup";
    }

    @Override
    public double cost() {
        return beverage.cost() + 0.8;
    }
}

public class CafeOrderSystem {
    public static void main(String[] args) {
        Beverage order1 = new Sugar(new Milk(new Espresso()));
        System.out.println(order1.getDescription() + " -> $" + order1.cost());

        Beverage order2 = new WhippedCream(new Sugar(new Tea()));
        System.out.println(order2.getDescription() + " -> $" + order2.cost());

        Beverage order3 = new WhippedCream(new VanillaSyrup(new Latte()));
        System.out.println(order3.getDescription() + " -> $" + order3.cost());

        Beverage order4 = new VanillaSyrup(new Sugar(new Milk(new Mocha())));
        System.out.println(order4.getDescription() + " -> $" + order4.cost());
    }
}
