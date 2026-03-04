using System;

namespace TemplateMethodReports
{
    abstract class ReportGenerator
    {
        public void GenerateReport()
        {
            Log("Начало генерации отчета...");
            CollectData();
            FormatData();
            GenerateHeader();
            GenerateBody();
            GenerateFooter();
            if (CustomerWantsSave())
            {
                SaveReport();
            }
            else
            {
                SendByEmail();
            }
            Log("Завершение генерации отчета.");
        }

        protected virtual void CollectData()
        {
            Console.WriteLine("Сбор данных для отчета...");
        }

        protected abstract void FormatData();
        protected abstract void GenerateHeader();
        protected abstract void GenerateBody();
        protected abstract void GenerateFooter();
        protected abstract void SaveReport();

        protected virtual bool CustomerWantsSave()
        {
            Console.Write("Хотите сохранить отчет в файл (y/n)? ");
            string answer = Console.ReadLine()?.ToLower();

            if (answer == "y") return true;
            if (answer == "n") return false;

            Console.WriteLine("Некорректный ввод. Отчет будет отправлен по email.");
            return false;
        }

        protected virtual void SendByEmail()
        {
            Console.WriteLine("Отчет отправлен по электронной почте.");
        }

        protected void Log(string message)
        {
            Console.WriteLine($"[LOG]: {message}");
        }
    }

    class PdfReport : ReportGenerator
    {
        protected override void FormatData()
        {
            Console.WriteLine("Форматирование данных для PDF...");
        }

        protected override void GenerateHeader()
        {
            Console.WriteLine("Создание заголовка PDF...");
        }

        protected override void GenerateBody()
        {
            Console.WriteLine("Создание содержимого PDF...");
        }

        protected override void GenerateFooter()
        {
            Console.WriteLine("Создание нижнего колонтитула PDF...");
        }

        protected override void SaveReport()
        {
            Console.WriteLine("Сохранение PDF-отчета в файл.");
        }
    }

    class ExcelReport : ReportGenerator
    {
        protected override void FormatData()
        {
            Console.WriteLine("Форматирование данных для Excel...");
        }

        protected override void GenerateHeader()
        {
            Console.WriteLine("Создание заголовка Excel...");
        }

        protected override void GenerateBody()
        {
            Console.WriteLine("Создание таблицы Excel...");
        }

        protected override void GenerateFooter()
        {
            Console.WriteLine("Создание нижнего колонтитула Excel...");
        }

        protected override void SaveReport()
        {
            Console.WriteLine("Сохранение Excel-отчета в файл.");
        }
    }

    class HtmlReport : ReportGenerator
    {
        protected override void FormatData()
        {
            Console.WriteLine("Форматирование данных для HTML...");
        }

        protected override void GenerateHeader()
        {
            Console.WriteLine("<h1>Заголовок HTML отчета</h1>");
        }

        protected override void GenerateBody()
        {
            Console.WriteLine("<p>Основное содержимое HTML отчета</p>");
        }

        protected override void GenerateFooter()
        {
            Console.WriteLine("<footer>Подвал HTML отчета</footer>");
        }

        protected override void SaveReport()
        {
            Console.WriteLine("Сохранение HTML-отчета в файл.");
        }
    }

    class CsvReport : ReportGenerator
    {
        protected override void FormatData()
        {
            Console.WriteLine("Форматирование данных для CSV...");
        }

        protected override void GenerateHeader()
        {
            Console.WriteLine("Создание заголовка CSV...");
        }

        protected override void GenerateBody()
        {
            Console.WriteLine("Создание строк CSV...");
        }

        protected override void GenerateFooter()
        {
            Console.WriteLine("Добавление итоговой строки CSV...");
        }

        protected override void SaveReport()
        {
            Console.WriteLine("Сохранение CSV-отчета в файл.");
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("=== Генерация PDF отчета ===");
            ReportGenerator pdf = new PdfReport();
            pdf.GenerateReport();

            Console.WriteLine("\n=== Генерация Excel отчета ===");
            ReportGenerator excel = new ExcelReport();
            excel.GenerateReport();

            Console.WriteLine("\n=== Генерация HTML отчета ===");
            ReportGenerator html = new HtmlReport();
            html.GenerateReport();

            Console.WriteLine("\n=== Генерация CSV отчета ===");
            ReportGenerator csv = new CsvReport();
            csv.GenerateReport();
        }
    }
}
