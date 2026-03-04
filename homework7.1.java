import java.util.Stack;

interface Command {
    void execute();
    void undo();
}

class Light {
    public void turnOn() { System.out.println("Свет включен"); }
    public void turnOff() { System.out.println("Свет выключен"); }
}

class Door {
    public void open() { System.out.println("Дверь открыта"); }
    public void close() { System.out.println("Дверь закрыта"); }
}

class Thermostat {
    private int temperature = 22;
    public void increaseTemp() {
        temperature++;
        System.out.println("Температура увеличена до " + temperature);
    }
    public void decreaseTemp() {
        temperature--;
        System.out.println("Температура уменьшена до " + temperature);
    }
}

class TV {
    public void turnOn() { System.out.println("Телевизор включен"); }
    public void turnOff() { System.out.println("Телевизор выключен"); }
}

class LightOnCommand implements Command {
    private Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.turnOn(); }
    public void undo() { light.turnOff(); }
}

class LightOffCommand implements Command {
    private Light light;
    public LightOffCommand(Light light) { this.light = light; }
    public void execute() { light.turnOff(); }
    public void undo() { light.turnOn(); }
}

class DoorOpenCommand implements Command {
    private Door door;
    public DoorOpenCommand(Door door) { this.door = door; }
    public void execute() { door.open(); }
    public void undo() { door.close(); }
}

class DoorCloseCommand implements Command {
    private Door door;
    public DoorCloseCommand(Door door) { this.door = door; }
    public void execute() { door.close(); }
    public void undo() { door.open(); }
}

class IncreaseTempCommand implements Command {
    private Thermostat thermostat;
    public IncreaseTempCommand(Therostat thermostat) { this.thermostat = thermostat; }
    public void execute() { thermostat.increaseTemp(); }
    public void undo() { thermostat.decreaseTemp(); }
}

class DecreaseTempCommand implements Command {
    private Thermostat thermostat;
    public DecreaseTempCommand(Thermostat thermostat) { this.thermostat = thermostat; }
    public void execute() { thermostat.decreaseTemp(); }
    public void undo() { thermostat.increaseTemp(); }
}

class TVOnCommand implements Command {
    private TV tv;
    public TVOnCommand(TV tv) { this.tv = tv; }
    public void execute() { tv.turnOn(); }
    public void undo() { tv.turnOff(); }
}

class TVOffCommand implements Command {
    private TV tv;
    public TVOffCommand(TV tv) { this.tv = tv; }
    public void execute() { tv.turnOff(); }
    public void undo() { tv.turnOn(); }
}

class RemoteControl {
    private Stack<Command> history = new Stack<>();

    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }

    public void undoLastCommand() {
        if (!history.isEmpty()) {
            Command lastCommand = history.pop();
            lastCommand.undo();
        } else {
            System.out.println("Нет команд для отмены!");
        }
    }
}

public class SmartHomeDemo {
    public static void main(String[] args) {
        RemoteControl remote = new RemoteControl();

        Light light = new Light();
        Door door = new Door();
        Thermostat thermostat = new Thermostat();
        TV tv = new TV();

        Command lightOn = new LightOnCommand(light);
        Command doorOpen = new DoorOpenCommand(door);
        Command increaseTemp = new IncreaseTempCommand(thermostat);
        Command tvOn = new TVOnCommand(tv);

        remote.executeCommand(lightOn);
        remote.executeCommand(doorOpen);
        remote.executeCommand(increaseTemp);
        remote.executeCommand(tvOn);

        remote.undoLastCommand();
        remote.undoLastCommand();
        remote.undoLastCommand();
        remote.undoLastCommand();
        remote.undoLastCommand();
    }
}
