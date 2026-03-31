import java.util.ArrayList;
import java.util.List;

abstract class FileSystemComponent {
    protected String name;

    public FileSystemComponent(String name) {
        this.name = name;
    }

    public abstract void display(String indent);
    public abstract int getSize();
}

class File extends FileSystemComponent {
    private int size;

    public File(String name, int size) {
        super(name);
        this.size = size;
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Файл: " + name + " (размер: " + size + ")");
    }

    @Override
    public int getSize() {
        return size;
    }
}

class Directory extends FileSystemComponent {
    private List<FileSystemComponent> components = new ArrayList<>();

    public Directory(String name) {
        super(name);
    }

    public void addComponent(FileSystemComponent component) {
        if (!components.contains(component)) {
            components.add(component);
            System.out.println("Добавлен компонент: " + component.name + " в папку " + name);
        } else {
            System.out.println("Компонент " + component.name + " уже существует в папке " + name);
        }
    }

    public void removeComponent(FileSystemComponent component) {
        if (components.contains(component)) {
            components.remove(component);
            System.out.println("Удалён компонент: " + component.name + " из папки " + name);
        } else {
            System.out.println("Компонент " + component.name + " не найден в папке " + name);
        }
    }

    @Override
    public void display(String indent) {
        System.out.println(indent + "Папка: " + name);
        for (FileSystemComponent component : components) {
            component.display(indent + "   ");
        }
    }

    @Override
    public int getSize() {
        int totalSize = 0;
        for (FileSystemComponent component : components) {
            totalSize += component.getSize();
        }
        return totalSize;
    }
}

public class Client {
    public static void main(String[] args) {
        File file1 = new File("Документ.txt", 120);
        File file2 = new File("Фото.jpg", 2000);
        File file3 = new File("Музыка.mp3", 5000);

        Directory root = new Directory("Root");
        Directory docs = new Directory("Docs");
        Directory media = new Directory("Media");

        root.addComponent(docs);
        root.addComponent(media);

        docs.addComponent(file1);
        media.addComponent(file2);
        media.addComponent(file3);

        System.out.println("\n--- Структура файловой системы ---");
        root.display("");

        System.out.println("\nОбщий размер Root: " + root.getSize());
        System.out.println("Размер Docs: " + docs.getSize());
        System.out.println("Размер Media: " + media.getSize());

        media.removeComponent(file2);
        System.out.println("\n--- После удаления Фото.jpg ---");
        root.display("");
        System.out.println("Размер Media: " + media.getSize());
    }
}
