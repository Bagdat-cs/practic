import java.util.ArrayList;
import java.util.List;

abstract class OrganizationComponent {
    protected String name;

    public OrganizationComponent(String name) {
        this.name = name;
    }

    public abstract void display(String indent);
    public abstract double getBudget();
    public abstract int getEmployeeCount();

    public abstract OrganizationComponent findEmployee(String employeeName);

    public abstract void listEmployees(List<Employee> employees);
}

class Employee extends OrganizationComponent {
    private String position;
    private double salary;

    public Employee(String name, String position, double salary) {
        super(name);
        this.position = position;
        this.salary = salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Сотрудник: " + name + " (" + position + "), зарплата: " + salary);
    }

    @Override
    public double getBudget() {
        return salary;
    }

    @Override
    public int getEmployeeCount() {
        return 1;
    }

    @Override
    public OrganizationComponent findEmployee(String employeeName) {
        return this.name.equalsIgnoreCase(employeeName) ? this : null;
    }

    @Override
    public void listEmployees(List<Employee> employees) {
        employees.add(this);
    }
}

class Contractor extends OrganizationComponent {
    private String position;
    private double fixedPayment;

    public Contractor(String name, String position, double fixedPayment) {
        super(name);
        this.position = position;
        this.fixedPayment = fixedPayment;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Контрактор: " + name + " (" + position + "), оплата: " + fixedPayment);
    }

    @Override
    public double getBudget() {
        return 0;
    }

    @Override
    public int getEmployeeCount() {
        return 1;
    }

    @Override
    public OrganizationComponent findEmployee(String employeeName) {
        return this.name.equalsIgnoreCase(employeeName) ? this : null;
    }

    @Override
    public void listEmployees(List<Employee> employees) {
    }
}

class Department extends OrganizationComponent {
    private List<OrganizationComponent> components = new ArrayList<>();

    public Department(String name) {
        super(name);
    }

    public void addComponent(OrganizationComponent component) {
        if (!components.contains(component)) {
            components.add(component);
            System.out.println("Добавлен компонент: " + component.name + " в отдел " + name);
        } else {
            System.out.println("Компонент " + component.name + " уже существует в отделе " + name);
        }
    }

    public void removeComponent(OrganizationComponent component) {
        if (components.contains(component)) {
            components.remove(component);
            System.out.println("Удалён компонент: " + component.name + " из отдела " + name);
        } else {
            System.out.println("Компонент " + component.name + " не найден в отделе " + name);
        }
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Отдел: " + name);
        for (OrganizationComponent component : components) {
            component.display(indent + "   ");
        }
    }

    @Override
    public double getBudget() {
        double total = 0;
        for (OrganizationComponent component : components) {
            total += component.getBudget();
        }
        return total;
    }

    @Override
    public int getEmployeeCount() {
        int count = 0;
        for (OrganizationComponent component : components) {
            count += component.getEmployeeCount();
        }
        return count;
    }

    @Override
    public OrganizationComponent findEmployee(String employeeName) {
        for (OrganizationComponent component : components) {
            OrganizationComponent found = component.findEmployee(employeeName);
            if (found != null) return found;
        }
        return null;
    }

    @Override
    public void listEmployees(List<Employee> employees) {
        for (OrganizationComponent component : components) {
            component.listEmployees(employees);
        }
    }
}

public class Client {
    public static void main(String[] args) {
        Employee emp1 = new Employee("Иван Иванов", "Менеджер", 50000);
        Employee emp2 = new Employee("Петр Петров", "Разработчик", 40000);
        Employee emp3 = new Employee("Сергей Сергеев", "Аналитик", 45000);

        Contractor contractor1 = new Contractor("Алексей Алексеев", "Фрилансер", 20000);

        Department itDept = new Department("IT");
        Department hrDept = new Department("HR");
        Department root = new Department("Компания");

        itDept.addComponent(emp2);
        itDept.addComponent(emp3);
        itDept.addComponent(contractor1);

        hrDept.addComponent(emp1);

        root.addComponent(itDept);
        root.addComponent(hrDept);

        System.out.println("\n--- Структура организации ---");
        root.display("");

        System.out.println("\nОбщий бюджет компании: " + root.getBudget());
        System.out.println("Общее количество сотрудников: " + root.getEmployeeCount());

        emp2.setSalary(45000);
        System.out.println("\nПосле изменения зарплаты Петра Петрова:");
        System.out.println("Общий бюджет компании: " + root.getBudget());

        OrganizationComponent found = root.findEmployee("Сергей Сергеев");
        if (found != null) {
            System.out.println("\nНайден сотрудник:");
            found.display("   ");
        }

        List<Employee> employees = new ArrayList<>();
        itDept.listEmployees(employees);
        System.out.println("\nСотрудники отдела IT:");
        for (Employee e : employees) {
            e.display("   ");
        }
    }
}
