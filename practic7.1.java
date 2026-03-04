import java.util.*;

interface Command {
    void execute();
    void undo();
}

class Light {
    public void on() { System.out.println("Свет включен"); }
    public void off() { System.out.println("Свет выключен"); }
}

class AirConditioner {
    public void on() { System.out.println("Кондиционер включен"); }
    public void off() { System.out.println("Кондиционер выключен"); }
}

class TV {
    public void on() { System.out.println("Телевизор включен"); }
    public void off() { System.out.println("Телевизор выключен"); }
}

class Curtains {
    public void open() { System.out.println("Шторы открыты"); }
    public void close() { System.out.println("Шторы закрыты"); }
}

class MusicPlayer {
    public void play() { System.out.println("Музыка играет"); }
    public void stop() { System.out.println("Музыка остановлена"); }
}

class LightOnCommand implements Command {
    private Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
    public void undo() { light.off(); }
}

class LightOffCommand implements Command {
    private Light light;
    public LightOffCommand(Light light) { this.light = light; }
    public void execute() { light.off(); }
    public void undo() { light.on(); }
}

class ACOnCommand implements Command {
    private AirConditioner ac;
    public ACOnCommand(AirConditioner ac) { this.ac = ac; }
    public void execute() { ac.on(); }
    public void undo() { ac.off(); }
}

class ACOffCommand implements Command {
    private AirConditioner ac;
    public ACOffCommand(AirConditioner ac) { this.ac = ac; }
    public void execute() { ac.off(); }
    public void undo() { ac.on(); }
}

class TVOnCommand implements Command {
    private TV tv;
    public TVOnCommand(TV tv) { this.tv = tv; }
    public void execute() { tv.on(); }
    public void undo() { tv.off(); }
}

class TVOffCommand implements Command {
    private TV tv;
    public TVOffCommand(TV tv) { this.tv = tv; }
    public void execute() { tv.off(); }
    public void undo() { tv.on(); }
}

class CurtainsOpenCommand implements Command {
    private Curtains curtains;
    public CurtainsOpenCommand(Curtains curtains) { this.curtains = curtains; }
    public void execute() { curtains.open(); }
    public void undo() { curtains.close(); }
}

class CurtainsCloseCommand implements Command {
    private Curtains curtains;
    public CurtainsCloseCommand(Curtains curtains) { this.curtains = curtains; }
    public void execute() { curtains.close(); }
    public void undo() { curtains.open(); }
}

class MusicPlayCommand implements Command {
    private MusicPlayer player;
    public MusicPlayCommand(MusicPlayer player) { this.player = player; }
    public void execute() { player.play(); }
    public void undo() { player.stop(); }
}

class MusicStopCommand implements Command {
    private MusicPlayer player;
    public MusicStopCommand(MusicPlayer player) { this.player = player; }
    public void execute() { player.stop(); }
    public void undo() { player.play(); }
}

class MacroCommand implements Command {
    private List<Command> commands;
    public MacroCommand(List<Command> commands) { this.commands = commands; }
    public void execute() { for (Command command : commands) command.execute(); }
    public void undo() {
        ListIterator<Command> it = commands.listIterator(commands.size());
        while (it.hasPrevious()) it.previous().undo();
    }
}

class RemoteControl {
    private Map<Integer, Command> slots = new HashMap<>();
    private Stack<Command> history = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    public void setCommand(int slot, Command command) { slots.put(slot, command); }

    public void pressButton(int slot) {
        if (slots.containsKey(slot)) {
            Command command = slots.get(slot);
            command.execute();
            history.push(command);
            redoStack.clear();
        } else {
            System.out.println("Ошибка: слот " + slot + " пуст.");
        }
    }

    public void undo() {
        if (!history.isEmpty()) {
            Command command = history.pop();
            command.undo();
            redoStack.push(command);
        } else {
            System.out.println("Нет команд для отмены.");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            history.push(command);
        } else {
            System.out.println("Нет команд для повтора.");
        }
    }
}

public class SmartHomeDemo {
    public static void main(String[] args) {
        RemoteControl remote = new RemoteControl();

        Light light = new Light();
        AirConditioner ac = new AirConditioner();
        TV tv = new TV();
        Curtains curtains = new Curtains();
        MusicPlayer player = new MusicPlayer();

        remote.setCommand(1, new LightOnCommand(light));
        remote.setCommand(2, new ACOnCommand(ac));
        remote.setCommand(3, new TVOnCommand(tv));
        remote.setCommand(4, new CurtainsOpenCommand(curtains));
        remote.setCommand(5, new MusicPlayCommand(player));

        List<Command> partyCommands = Arrays.asList(
            new LightOnCommand(light),
            new ACOnCommand(ac),
            new TVOnCommand(tv),
            new MusicPlayCommand(player)
        );
        remote.setCommand(6, new MacroCommand(partyCommands));

        remote.pressButton(1);
        remote.pressButton(2);
        remote.pressButton(3);
        remote.undo();
        remote.redo();

        System.out.println("\nЗапуск макрокоманды:");
        remote.pressButton(6);

        System.out.println("\nОтмена макрокоманды:");
        remote.undo();

        System.out.println("\nПроверка пустого слота:");
        remote.pressButton(10);
    }
}
