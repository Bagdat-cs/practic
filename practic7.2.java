import java.util.Scanner;

abstract class ReportGenerator {
    public final void generateReport() {
        log("Начало генерации отчета...");
        collectData();
        formatData();
        generateHeader();
        generateBody();
        generateFooter();
        if (customerWantsSave()) {
            saveReport();
        } else {
            sendByEmail();
        }
        log("Завершение генерации отчета.");
    }

    protected void collectData() {
        System.out.println("Сбор данных для отчета...");
    }

    protected abstract void formatData();
    protected abstract void generateHeader();
    protected abstract void generateBody();
    protected abstract void generateFooter();
    protected abstract void saveReport();

    protected boolean customerWantsSave() {
        System.out.print("Хотите сохранить отчет в файл (y/n)? ");
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.nextLine().toLowerCase();
        if (answer.equals("y")) return true;
        if (answer.equals("n")) return false;
        System.out.println("Некорректный ввод. Отчет будет отправлен по email.");
        return false;
    }

    protected void sendByEmail() {
        System.out.println("Отчет отправлен по электронной почте.");
    }

    protected void log(String message) {
        System.out.println("[LOG]: " + message);
    }
}

class PdfReport extends ReportGenerator {
    protected void formatData() { System.out.println("Форматирование данных для PDF..."); }
    protected void generateHeader() { System.out.println("Создание заголовка PDF..."); }
    protected void generateBody() { System.out.println("Создание содержимого PDF..."); }
    protected void generateFooter() { System.out.println("Создание нижнего колонтитула PDF..."); }
    protected void saveReport() { System.out.println("Сохранение PDF-отчета в файл."); }
}

class ExcelReport extends ReportGenerator {
    protected void formatData() { System.out.println("Форматирование данных для Excel..."); }
    protected void generateHeader() { System.out.println("Создание заголовка Excel..."); }
    protected void generateBody() { System.out.println("Создание таблицы Excel..."); }
    protected void generateFooter() { System.out.println("Создание нижнего колонтитула Excel..."); }
    protected void saveReport() { System.out.println("Сохранение Excel-отчета в файл."); }
}

class HtmlReport extends ReportGenerator {
    protected void formatData() { System.out.println("Форматирование данных для HTML..."); }
    protected void generateHeader() { System.out.println("<h1>Заголовок HTML отчета</h1>"); }
    protected void generateBody() { System.out.println("<p>Основное содержимое HTML отчета</p>"); }
    protected void generateFooter() { System.out.println("<footer>Подвал HTML отчета</footer>"); }
    protected void saveReport() { System.out.println("Сохранение HTML-отчета в файл."); }
}

class CsvReport extends ReportGenerator {
    protected void formatData() { System.out.println("Форматирование данных для CSV..."); }
    protected void generateHeader() { System.out.println("Создание заголовка CSV..."); }
    protected void generateBody() { System.out.println("Создание строк CSV..."); }
    protected void generateFooter() { System.out.println("Добавление итоговой строки CSV..."); }
    protected void saveReport() { System.out.println("Сохранение CSV-отчета в файл."); }
}

public class TemplateMethodDemo {
    public static void main(String[] args) {
        System.out.println("=== Генерация PDF отчета ===");
        ReportGenerator pdf = new PdfReport();
        pdf.generateReport();

        System.out.println("\n=== Генерация Excel отчета ===");
        ReportGenerator excel = new ExcelReport();
        excel.generateReport();

        System.out.println("\n=== Генерация HTML отчета ===");
        ReportGenerator html = new HtmlReport();
        html.generateReport();

        System.out.println("\n=== Генерация CSV отчета ===");
        ReportGenerator csv = new CsvReport();
        csv.generateReport();
    }
}
