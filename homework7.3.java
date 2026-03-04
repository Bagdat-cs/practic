using System;
using System.Collections.Generic;

namespace MediatorPatternChat
{
    interface IMediator
    {
        void RegisterUser(User user);
        void SendMessage(string message, User sender);
        void SendPrivateMessage(string message, User sender, string receiverName);
        void RemoveUser(User user);
    }

    class ChatRoom : IMediator
    {
        private List<User> users = new List<User>();

        public void RegisterUser(User user)
        {
            if (!users.Contains(user))
            {
                users.Add(user);
                user.SetMediator(this);
                NotifyAll($"{user.Name} присоединился к чату.");
            }
        }

        public void RemoveUser(User user)
        {
            if (users.Contains(user))
            {
                users.Remove(user);
                NotifyAll($"{user.Name} покинул чат.");
            }
        }

        public void SendMessage(string message, User sender)
        {
            if (!users.Contains(sender))
            {
                Console.WriteLine($"Ошибка: {sender.Name} не является участником чата.");
                return;
            }

            foreach (var user in users)
            {
                if (user != sender)
                {
                    user.Receive(message, sender.Name);
                }
            }
        }

        public void SendPrivateMessage(string message, User sender, string receiverName)
        {
            if (!users.Contains(sender))
            {
                Console.WriteLine($"Ошибка: {sender.Name} не является участником чата.");
                return;
            }

            var receiver = users.Find(u => u.Name == receiverName);
            if (receiver != null)
            {
                receiver.Receive($"[Личное сообщение] {message}", sender.Name);
            }
            else
            {
                Console.WriteLine($"Ошибка: пользователь {receiverName} не найден.");
            }
        }

        private void NotifyAll(string notification)
        {
            foreach (var user in users)
            {
                user.ReceiveSystemMessage(notification);
            }
        }
    }

    abstract class User
    {
        protected IMediator mediator;
        public string Name { get; private set; }

        public User(string name)
        {
            Name = name;
        }

        public void SetMediator(IMediator mediator)
        {
            this.mediator = mediator;
        }

        public void Send(string message)
        {
            mediator?.SendMessage(message, this);
        }

        public void SendPrivate(string message, string receiverName)
        {
            mediator?.SendPrivateMessage(message, this, receiverName);
        }

        public abstract void Receive(string message, string sender);
        public abstract void ReceiveSystemMessage(string message);
    }

    class ChatUser : User
    {
        public ChatUser(string name) : base(name) { }

        public override void Receive(string message, string sender)
        {
            Console.WriteLine($"{Name} получил сообщение от {sender}: {message}");
        }

        public override void ReceiveSystemMessage(string message)
        {
            Console.WriteLine($"{Name} получил уведомление: {message}");
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            ChatRoom chatRoom = new ChatRoom();

            User user1 = new ChatUser("Алиса");
            User user2 = new ChatUser("Боб");
            User user3 = new ChatUser("Чарли");

            chatRoom.RegisterUser(user1);
            chatRoom.RegisterUser(user2);
            chatRoom.RegisterUser(user3);

            user1.Send("Всем привет!");
            user2.SendPrivate("Привет, Алиса!", "Алиса");

            chatRoom.RemoveUser(user3);

            user3.Send("Я еще здесь?");
        }
    }
}
