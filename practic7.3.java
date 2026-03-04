import java.util.*;

interface IMediator {
    void registerUser(IUser user, String channelName);
    void removeUser(IUser user, String channelName);
    void sendMessage(String message, IUser sender, String channelName);
    void sendPrivateMessage(String message, IUser sender, String receiverName, String channelName);
}

interface IUser {
    String getName();
    void receive(String message, String sender);
    void receiveSystemMessage(String message);
}

class User implements IUser {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void receive(String message, String sender) {
        System.out.println(name + " получил сообщение от " + sender + ": " + message);
    }

    public void receiveSystemMessage(String message) {
        System.out.println(name + " получил уведомление: " + message);
    }
}

class ChatMediator implements IMediator {
    private Map<String, List<IUser>> channels = new HashMap<>();

    public void registerUser(IUser user, String channelName) {
        channels.putIfAbsent(channelName, new ArrayList<>());
        if (!channels.get(channelName).contains(user)) {
            channels.get(channelName).add(user);
            notifyAll(channelName, user.getName() + " присоединился к каналу " + channelName + ".");
        }
    }

    public void removeUser(IUser user, String channelName) {
        if (channels.containsKey(channelName) && channels.get(channelName).contains(user)) {
            channels.get(channelName).remove(user);
            notifyAll(channelName, user.getName() + " покинул канал " + channelName + ".");
        }
    }

    public void sendMessage(String message, IUser sender, String channelName) {
        if (!channels.containsKey(channelName)) {
            System.out.println("Ошибка: канал " + channelName + " не существует.");
            return;
        }
        if (!channels.get(channelName).contains(sender)) {
            System.out.println("Ошибка: " + sender.getName() + " не состоит в канале " + channelName + ".");
            return;
        }
        for (IUser user : channels.get(channelName)) {
            if (user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }

    public void sendPrivateMessage(String message, IUser sender, String receiverName, String channelName) {
        if (!channels.containsKey(channelName)) {
            System.out.println("Ошибка: канал " + channelName + " не существует.");
            return;
        }
        for (IUser user : channels.get(channelName)) {
            if (user.getName().equals(receiverName)) {
                user.receive("[Личное сообщение] " + message, sender.getName());
                return;
            }
        }
        System.out.println("Ошибка: пользователь " + receiverName + " не найден в канале " + channelName + ".");
    }

    private void notifyAll(String channelName, String notification) {
        for (IUser user : channels.get(channelName)) {
            user.receiveSystemMessage(notification);
        }
    }
}

public class MediatorChatDemo {
    public static void main(String[] args) {
        ChatMediator mediator = new ChatMediator();

        IUser alice = new User("Алиса");
        IUser bob = new User("Боб");
        IUser charlie = new User("Чарли");
        IUser diana = new User("Диана");

        mediator.registerUser(alice, "General");
        mediator.registerUser(bob, "General");
        mediator.registerUser(charlie, "Sports");
        mediator.registerUser(diana, "Sports");

        alice.receiveSystemMessage("=== Демонстрация ===");
        mediator.sendMessage("Привет всем в General!", alice, "General");
        mediator.sendMessage("Сегодня играем в футбол!", charlie, "Sports");

        mediator.sendPrivateMessage("Привет, Алиса!", bob, "Алиса", "General");

        mediator.sendMessage("Я хочу написать в Sports", bob, "Sports");

        mediator.removeUser(diana, "Sports");
        mediator.sendMessage("Диана ушла, кто будет капитаном?", charlie, "Sports");

        mediator.sendMessage("Сообщение в несуществующий канал", alice, "Music");
    }
}
