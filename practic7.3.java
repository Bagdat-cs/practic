using System;
using System.Collections.Generic;

namespace MediatorChatSystem
{
    interface IMediator
    {
        void RegisterUser(IUser user, string channelName);
        void RemoveUser(IUser user, string channelName);
        void SendMessage(string message, IUser sender, string channelName);
        void SendPrivateMessage(string message, IUser sender, string receiverName, string channelName);
    }

    interface IUser
    {
        string Name { get; }
        void Receive(string message, string sender);
        void ReceiveSystemMessage(string message);
    }

    class User : IUser
    {
        public string Name { get; private set; }

        public User(string name)
        {
            Name = name;
        }

        public void Receive(string message, string sender)
        {
            Console.WriteLine($"{Name} получил сообщение от {sender}: {message}");
        }

        public void ReceiveSystemMessage(string message)
        {
            Console.WriteLine($"{Name} получил уведомление: {message}");
        }
    }

    class ChatMediator : IMediator
    {
        private Dictionary<string, List<IUser>> channels = new Dictionary<string, List<IUser>>();

        public void RegisterUser(IUser user, string channelName)
        {
            if (!channels.ContainsKey(channelName))
            {
                channels[channelName] = new List<IUser>();
            }

            if (!channels[channelName].Contains(user))
            {
                channels[channelName].Add(user);
                NotifyAll(channelName, $"{user.Name} присоединился к каналу {channelName}.");
            }
        }

        public void RemoveUser(IUser user, string channelName)
        {
            if (channels.ContainsKey(channelName) && channels[channelName].Contains(user))
            {
                channels[channelName].Remove(user);
                NotifyAll(channelName, $"{user.Name} покинул канал {channelName}.");
            }
        }

        public void SendMessage(string message, IUser sender, string channelName)
        {
            if (!channels.ContainsKey(channelName))
            {
                Console.WriteLine($"Ошибка: канал {channelName} не существует.");
                return;
            }

            if (!channels[channelName].Contains(sender))
            {
                Console.WriteLine($"Ошибка: {sender.Name} не состоит в канале {channelName}.");
                return;
            }

            foreach (var user in channels[channelName])
            {
                if (user != sender)
                {
                    user.Receive(message, sender.Name);
                }
            }
        }

        public void SendPrivateMessage(string message, IUser sender, string receiverName, string channelName)
        {
            if (!channels.ContainsKey(channelName))
            {
                Console.WriteLine($"Ошибка: канал {channelName} не существует.");
                return;
            }

            var receiver = channels[channelName].Find(u => u.Name == receiverName);
            if (receiver != null)
            {
                receiver.Receive($"[Личное сообщение] {message}", sender.Name);
            }
            else
            {
                Console.WriteLine($"Ошибка: пользователь {receiverName} не найден в канале {channelName}.");
            }
        }

        private void NotifyAll(string channelName, string notification)
        {
            foreach (var user in channels[channelName])
            {
                user.ReceiveSystemMessage(notification);
            }
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            ChatMediator mediator = new ChatMediator();

            IUser alice = new User("Алиса");
            IUser bob = new User("Боб");
            IUser charlie = new User("Чарли");
            IUser diana = new User("Диана");

            mediator.RegisterUser(alice, "General");
            mediator.RegisterUser(bob, "General");
            mediator.RegisterUser(charlie, "Sports");
            mediator.RegisterUser(diana, "Sports");

            alice.ReceiveSystemMessage("=== Демонстрация ===");
            mediator.SendMessage("Привет всем в General!", alice, "General");
            mediator.SendMessage("Сегодня играем в футбол!", charlie, "Sports");

            mediator.SendPrivateMessage("Привет, Алиса!", bob, "Алиса", "General");

            mediator.SendMessage("Я хочу написать в Sports", bob, "Sports");

            mediator.RemoveUser(diana, "Sports");
            mediator.SendMessage("Диана ушла, кто будет капитаном?", charlie, "Sports");

            mediator.SendMessage("Сообщение в несуществующий канал", alice, "Music");
        }
    }
}
