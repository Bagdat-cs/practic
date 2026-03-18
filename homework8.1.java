interface IReport {
    String generate();
}

class SalesReport implements IReport {
    @Override
    public String generate() {
        return "Отчет по продажам: [Заказ1: 100$, Заказ2: 200$, Заказ3: 150$]";
    }
}

class UserReport implements IReport {
    @Override
    public String generate() {
        return "Отчет по пользователям: [Пользователь1: Активен, Пользователь2: Неактивен, Пользователь3: Активен]";
    }
}

abstract class ReportDecorator implements IReport {
    protected IReport report;

    public ReportDecorator(IReport report) {
        this.report = report;
    }

    @Override
    public String generate() {
        return report.generate();
    }
}

class DateFilterDecorator extends ReportDecorator {
    private String startDate;
    private String endDate;

    public DateFilterDecorator(IReport report, String startDate, String endDate) {
        super(report);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String generate() {
        return report.generate() + "\n[Фильтрация по датам: " + startDate + " - " + endDate + "]";
    }
}

class SortingDecorator extends ReportDecorator {
    private String criteria;

    public SortingDecorator(IReport report, String criteria) {
        super(report);
        this.criteria = criteria;
    }

    @Override
    public String generate() {
        return report.generate() + "\n[Сортировка по: " + criteria + "]";
    }
}

class CsvExportDecorator extends ReportDecorator {
    public CsvExportDecorator(IReport report) {
        super(report);
    }

    @Override
    public String generate() {
        return report.generate() + "\n[Экспортировано в формат CSV]";
    }
}

class PdfExportDecorator extends ReportDecorator {
    public PdfExportDecorator(IReport report) {
        super(report);
    }

    @Override
    public String generate() {
        return report.generate() + "\n[Экспортировано в формат PDF]";
    }
}

class AmountFilterDecorator extends ReportDecorator {
    private double minAmount;

    public AmountFilterDecorator(IReport report, double minAmount) {
        super(report);
        this.minAmount = minAmount;
    }

    @Override
    public String generate() {
        return report.generate() + "\n[Фильтрация по минимальной сумме: " + minAmount + "$]";
    }
}
class ActiveUserFilterDecorator extends ReportDecorator {
    public ActiveUserFilterDecorator(IReport report) {
        super(report);
    }

    @Override
    public String generate() {
        return report.generate() + "\n[Фильтрация: только активные пользователи]";
    }
}

public class ReportSystemDemo {
    public static void main(String[] args) {
        IReport salesReport = new CsvExportDecorator(
                                new DateFilterDecorator(
                                    new SalesReport(), "2026-01-01", "2026-03-01"));
        System.out.println(salesReport.generate());

        IReport userReport = new PdfExportDecorator(
                                new ActiveUserFilterDecorator(new UserReport()));
        System.out.println(userReport.generate());

        IReport salesReport2 = new SortingDecorator(
                                new AmountFilterDecorator(new SalesReport(), 150), "Сумма");
        System.out.println(salesReport2.generate());

        IReport salesReport3 = new PdfExportDecorator(
                                new SortingDecorator(
                                    new DateFilterDecorator(new SalesReport(), "2026-02-01", "2026-03-01"), "Дата"));
        System.out.println(salesReport3.generate());
    }
}
