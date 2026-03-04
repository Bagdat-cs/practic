import java.util.*;

interface IMediator {
    void registerUser(User user);
    void sendMessage(String message, User sender);
    void sendPrivateMessage(String message, User sender, String receiverName);
    void removeUser(User user);
}

class ChatRoom implements IMediator {
    private List<User> users = new ArrayList<>();

    public void registerUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
            user.setMediator(this);
            notifyAllUsers(user.getName() + " присоединился к чату.");
        }
    }

    public void removeUser(User user) {
        if (users.contains(user)) {
            users.remove(user);
            notifyAllUsers(user.getName() + " покинул чат.");
        }
    }

    public void sendMessage(String message, User sender) {
        if (!users.contains(sender)) {
            System.out.println("Ошибка: " + sender.getName() + " не является участником чата.");
            return;
        }
        for (User user : users) {
            if (user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }

    public void sendPrivateMessage(String message, User sender, String receiverName) {
        if (!users.contains(sender)) {
            System.out.println("Ошибка: " + sender.getName() + " не является участником чата.");
            return;
        }
        for (User user : users) {
            if (user.getName().equals(receiverName)) {
                user.receive("[Личное сообщение] " + message, sender.getName());
                return;
            }
        }
        System.out.println("Ошибка: пользователь " + receiverName + " не найден.");
    }

    private void notifyAllUsers(String notification) {
        for (User user : users) {
            user.receiveSystemMessage(notification);
        }
    }
}

abstract class User {
    protected IMediator mediator;
    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void setMediator(IMediator mediator) {
        this.mediator = mediator;
    }

    public void send(String message) {
        if (mediator != null) mediator.sendMessage(message, this);
    }

    public void sendPrivate(String message, String receiverName) {
        if (mediator != null) mediator.sendPrivateMessage(message, this, receiverName);
    }

    public abstract void receive(String message, String sender);
    public abstract void receiveSystemMessage(String message);
}

class ChatUser extends User {
    public ChatUser(String name) { super(name); }

    public void receive(String message, String sender) {
        System.out.println(getName() + " получил сообщение от " + sender + ": " + message);
    }

    public void receiveSystemMessage(String message) {
        System.out.println(getName() + " получил уведомление: " + message);
    }
}

public class MediatorChatDemo {
    public static void main(String[] args) {
        ChatRoom chatRoom = new ChatRoom();

        User user1 = new ChatUser("Алиса");
        User user2 = new ChatUser("Боб");
        User user3 = new ChatUser("Чарли");

        chatRoom.registerUser(user1);
        chatRoom.registerUser(user2);
        chatRoom.registerUser(user3);

        user1.send("Всем привет!");
        user2.sendPrivate("Привет, Алиса!", "Алиса");

        chatRoom.removeUser(user3);

        user3.send("Я еще здесь?");
    }
}
