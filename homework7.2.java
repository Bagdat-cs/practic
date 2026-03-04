import java.util.Scanner;

abstract class Beverage {
    public final void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) {
            addCondiments();
        }
    }

    private void boilWater() {
        System.out.println("Кипятим воду");
    }

    private void pourInCup() {
        System.out.println("Наливаем в чашку");
    }

    protected abstract void brew();
    protected abstract void addCondiments();

    protected boolean customerWantsCondiments() {
        System.out.print("Хотите добавить добавки (y/n)? ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine().toLowerCase();

        if (answer.equals("y")) return true;
        if (answer.equals("n")) return false;

        System.out.println("Некорректный ввод, добавки не будут добавлены.");
        return false;
    }
}

class Tea extends Beverage {
    protected void brew() {
        System.out.println("Завариваем чай");
    }

    protected void addCondiments() {
        System.out.println("Добавляем лимон");
    }
}

class Coffee extends Beverage {
    protected void brew() {
        System.out.println("Завариваем кофе");
    }

    protected void addCondiments() {
        System.out.println("Добавляем сахар и молоко");
    }
}

class HotChocolate extends Beverage {
    protected void brew() {
        System.out.println("Размешиваем какао-порошок в горячей воде");
    }

    protected void addCondiments() {
        System.out.println("Добавляем взбитые сливки");
    }

    protected boolean customerWantsCondiments() {
        return true;
    }
}

public class TemplateMethodDemo {
    public static void main(String[] args) {
        System.out.println("=== Приготовление чая ===");
        Beverage tea = new Tea();
        tea.prepareRecipe();

        System.out.println("\n=== Приготовление кофе ===");
        Beverage coffee = new Coffee();
        coffee.prepareRecipe();

        System.out.println("\n=== Приготовление горячего шоколада ===");
        Beverage hotChocolate = new HotChocolate();
        hotChocolate.prepareRecipe();
    }
}
