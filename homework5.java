import java.io.*;
import java.util.*;
import java.util.concurrent.*;

enum LogLevel {
    INFO, WARNING, ERROR
}

class Logger {
    private static volatile Logger instance;
    private LogLevel currentLevel = LogLevel.INFO;
    private String logFilePath = "app.log";
    private final Object lock = new Object();

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void setLogLevel(LogLevel level) {
        currentLevel = level;
    }

    public void configureFromFile(String configFile) throws IOException {
        Properties props = new Properties();
        try (FileReader reader = new FileReader(configFile)) {
            props.load(reader);
            String level = props.getProperty("logLevel", "INFO");
            logFilePath = props.getProperty("logFile", "app.log");
            setLogLevel(LogLevel.valueOf(level));
        }
    }

    public void log(String message, LogLevel level) {
        if (level.ordinal() >= currentLevel.ordinal()) {
            synchronized (lock) {
                try (FileWriter fw = new FileWriter(logFilePath, true);
                     BufferedWriter bw = new BufferedWriter(fw)) {
                    bw.write("[" + level + "] " + message);
                    bw.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class LogReader {
    private String logFilePath;

    public LogReader(String logFilePath) {
        this.logFilePath = logFilePath;
    }

    public void readLogs(LogLevel filterLevel) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[" + filterLevel + "]")) {
                    System.out.println(line);
                }
            }
        }
    }
}

class ReportStyle {
    String backgroundColor;
    String fontColor;
    int fontSize;

    public ReportStyle(String bg, String font, int size) {
        this.backgroundColor = bg;
        this.fontColor = font;
        this.fontSize = size;
    }
}

class Report {
    private String header;
    private String content;
    private String footer;
    private List<String> sections = new ArrayList<>();
    private ReportStyle style;

    public void setHeader(String header) { this.header = header; }
    public void setContent(String content) { this.content = content; }
    public void setFooter(String footer) { this.footer = footer; }
    public void addSection(String name, String content) { sections.add(name + ": " + content); }
    public void setStyle(ReportStyle style) { this.style = style; }

    public void export(String format) {
        System.out.println("Exporting " + format + " report...");
        System.out.println("Style: bg=" + style.backgroundColor + ", font=" + style.fontColor + ", size=" + style.fontSize);
        System.out.println(header);
        System.out.println(content);
        for (String s : sections) System.out.println(s);
        System.out.println(footer);
    }
}

interface IReportBuilder {
    void setHeader(String header);
    void setContent(String content);
    void setFooter(String footer);
    void addSection(String sectionName, String sectionContent);
    void setStyle(ReportStyle style);
    Report getReport();
}

class TextReportBuilder implements IReportBuilder {
    private Report report = new Report();
    public void setHeader(String header) { report.setHeader("TEXT HEADER: " + header); }
    public void setContent(String content) { report.setContent("TEXT CONTENT: " + content); }
    public void setFooter(String footer) { report.setFooter("TEXT FOOTER: " + footer); }
    public void addSection(String name, String content) { report.addSection(name, content); }
    public void setStyle(ReportStyle style) { report.setStyle(style); }
    public Report getReport() { return report; }
}

class HtmlReportBuilder implements IReportBuilder {
    private Report report = new Report();
    public void setHeader(String header) { report.setHeader("<h1>" + header + "</h1>"); }
    public void setContent(String content) { report.setContent("<p>" + content + "</p>"); }
    public void setFooter(String footer) { report.setFooter("<footer>" + footer + "</footer>"); }
    public void addSection(String name, String content) { report.addSection("<section>" + name, content + "</section>"); }
    public void setStyle(ReportStyle style) { report.setStyle(style); }
    public Report getReport() { return report; }
}

class ReportDirector {
    public Report constructReport(IReportBuilder builder, ReportStyle style) {
        builder.setHeader("Отчёт");
        builder.setContent("Основное содержимое");
        builder.addSection("Раздел 1", "Данные таблицы");
        builder.addSection("Раздел 2", "Графики");
        builder.setFooter("Конец отчёта");
        builder.setStyle(style);
        return builder.getReport();
    }
}

interface Prototype<T> {
    T clone();
}

class Weapon implements Prototype<Weapon> {
    String name;
    int damage;
    public Weapon(String name, int damage) { this.name = name; this.damage = damage; }
    public Weapon clone() { return new Weapon(name, damage); }
    public String toString() { return name + " (DMG=" + damage + ")"; }
}

class Armor implements Prototype<Armor> {
    String name;
    int defense;
    public Armor(String name, int defense) { this.name = name; this.defense = defense; }
    public Armor clone() { return new Armor(name, defense); }
    public String toString() { return name + " (DEF=" + defense + ")"; }
}

class Skill implements Prototype<Skill> {
    String name;
    public Skill(String name) { this.name = name; }
    public Skill clone() { return new Skill(name); }
    public String toString() { return name; }
}

class Character implements Prototype<Character> {
    int health, strength, agility, intelligence;
    Weapon weapon;
    Armor armor;
    List<Skill> skills = new ArrayList<>();

    public Character(int h, int s, int a, int i, Weapon w, Armor ar) {
        health = h; strength = s; agility = a; intelligence = i;
        weapon = w; armor = ar;
    }

    public void addSkill(Skill skill) { skills.add(skill); }

    public Character clone() {
        Character copy = new Character(health, strength, agility, intelligence,
                weapon.clone(), armor.clone());
        for (Skill sk : skills) copy.addSkill(sk.clone());
        return copy;
    }

    public String toString() {
        return "Character [HP=" + health + ", STR=" + strength + ", AGI=" + agility +
                ", INT=" + intelligence + ", Weapon=" + weapon + ", Armor=" + armor +
                ", Skills=" + skills + "]";
    }
}

public class AllPatternsDemo {
    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getInstance();
        logger.setLogLevel(LogLevel.INFO);

        ExecutorService executor = Executors.newFixedThreadPool(3);
        executor.submit(() -> logger.log("Info message from Thread 1", LogLevel.INFO));
        executor.submit(() -> logger.log("Warning from Thread 2", LogLevel.WARNING));
        executor.submit(() -> logger.log("Error from Thread 3", LogLevel.ERROR));
        executor.shutdown();

        LogReader reader = new LogReader("app.log");
        System.out.println("\n=== ERROR Logs ===");
        reader.readLogs(LogLevel.ERROR);
        ReportDirector director = new ReportDirector();
        ReportStyle style = new ReportStyle("white", "black", 12);

        Report textReport = director.constructReport(new TextReportBuilder(), style);
        textReport.export("TEXT");

        Report htmlReport = director.constructReport(new HtmlReportBuilder(), style);
        htmlReport.export("HTML");

        Character hero = new Character(100, 20, 15, 10,
                new Weapon("Sword", 30), new Armor("Shield", 20));
        hero.addSkill(new Skill("Fireball"));
        hero.addSkill(new Skill("Dash"));

        Character cloneHero = hero.clone();
        cloneHero.addSkill(new Skill("Heal"));

        System.out.println("\nOriginal Hero: " + hero);
        System.out.println("Cloned Hero: " + cloneHero);
    }
}
