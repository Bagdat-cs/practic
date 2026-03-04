using System;
using System.Collections.Generic;

namespace SmartHomeCommandPattern
{
    public interface ICommand
    {
        void Execute();
        void Undo();
    }

    public class Light
    {
        public void On() => Console.WriteLine("Свет включен");
        public void Off() => Console.WriteLine("Свет выключен");
    }

    public class AirConditioner
    {
        public void On() => Console.WriteLine("Кондиционер включен");
        public void Off() => Console.WriteLine("Кондиционер выключен");
    }

    public class TV
    {
        public void On() => Console.WriteLine("Телевизор включен");
        public void Off() => Console.WriteLine("Телевизор выключен");
    }

    public class Curtains
    {
        public void Open() => Console.WriteLine("Шторы открыты");
        public void Close() => Console.WriteLine("Шторы закрыты");
    }

    public class MusicPlayer
    {
        public void Play() => Console.WriteLine("Музыка играет");
        public void Stop() => Console.WriteLine("Музыка остановлена");
    }

    public class LightOnCommand : ICommand
    {
        private Light light;
        public LightOnCommand(Light light) { this.light = light; }
        public void Execute() => light.On();
        public void Undo() => light.Off();
    }

    public class LightOffCommand : ICommand
    {
        private Light light;
        public LightOffCommand(Light light) { this.light = light; }
        public void Execute() => light.Off();
        public void Undo() => light.On();
    }

    public class ACOnCommand : ICommand
    {
        private AirConditioner ac;
        public ACOnCommand(AirConditioner ac) { this.ac = ac; }
        public void Execute() => ac.On();
        public void Undo() => ac.Off();
    }

    public class ACOffCommand : ICommand
    {
        private AirConditioner ac;
        public ACOffCommand(AirConditioner ac) { this.ac = ac; }
        public void Execute() => ac.Off();
        public void Undo() => ac.On();
    }

    public class TVOnCommand : ICommand
    {
        private TV tv;
        public TVOnCommand(TV tv) { this.tv = tv; }
        public void Execute() => tv.On();
        public void Undo() => tv.Off();
    }

    public class TVOffCommand : ICommand
    {
        private TV tv;
        public TVOffCommand(TV tv) { this.tv = tv; }
        public void Execute() => tv.Off();
        public void Undo() => tv.On();
    }

    public class CurtainsOpenCommand : ICommand
    {
        private Curtains curtains;
        public CurtainsOpenCommand(Curtains curtains) { this.curtains = curtains; }
        public void Execute() => curtains.Open();
        public void Undo() => curtains.Close();
    }

    public class CurtainsCloseCommand : ICommand
    {
        private Curtains curtains;
        public CurtainsCloseCommand(Curtains curtains) { this.curtains = curtains; }
        public void Execute() => curtains.Close();
        public void Undo() => curtains.Open();
    }

    public class MusicPlayCommand : ICommand
    {
        private MusicPlayer player;
        public MusicPlayCommand(MusicPlayer player) { this.player = player; }
        public void Execute() => player.Play();
        public void Undo() => player.Stop();
    }

    public class MusicStopCommand : ICommand
    {
        private MusicPlayer player;
        public MusicStopCommand(MusicPlayer player) { this.player = player; }
        public void Execute() => player.Stop();
        public void Undo() => player.Play();
    }

    public class MacroCommand : ICommand
    {
        private List<ICommand> commands;
        public MacroCommand(List<ICommand> commands) { this.commands = commands; }

        public void Execute()
        {
            foreach (var command in commands)
                command.Execute();
        }

        public void Undo()
        {
            for (int i = commands.Count - 1; i >= 0; i--)
                commands[i].Undo();
        }
    }

    public class RemoteControl
    {
        private Dictionary<int, ICommand> slots = new Dictionary<int, ICommand>();
        private Stack<ICommand> history = new Stack<ICommand>();
        private Stack<ICommand> redoStack = new Stack<ICommand>();

        public void SetCommand(int slot, ICommand command)
        {
            slots[slot] = command;
        }

        public void PressButton(int slot)
        {
            if (slots.ContainsKey(slot))
            {
                ICommand command = slots[slot];
                command.Execute();
                history.Push(command);
                redoStack.Clear();
            }
            else
            {
                Console.WriteLine($"Ошибка: слот {slot} пуст.");
            }
        }

        public void Undo()
        {
            if (history.Count > 0)
            {
                ICommand command = history.Pop();
                command.Undo();
                redoStack.Push(command);
            }
            else
            {
                Console.WriteLine("Нет команд для отмены.");
            }
        }

        public void Redo()
        {
            if (redoStack.Count > 0)
            {
                ICommand command = redoStack.Pop();
                command.Execute();
                history.Push(command);
            }
            else
            {
                Console.WriteLine("Нет команд для повтора.");
            }
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            RemoteControl remote = new RemoteControl();

            Light light = new Light();
            AirConditioner ac = new AirConditioner();
            TV tv = new TV();
            Curtains curtains = new Curtains();
            MusicPlayer player = new MusicPlayer();

            remote.SetCommand(1, new LightOnCommand(light));
            remote.SetCommand(2, new ACOnCommand(ac));
            remote.SetCommand(3, new TVOnCommand(tv));
            remote.SetCommand(4, new CurtainsOpenCommand(curtains));
            remote.SetCommand(5, new MusicPlayCommand(player));

            var partyCommands = new List<ICommand>
            {
                new LightOnCommand(light),
                new ACOnCommand(ac),
                new TVOnCommand(tv),
                new MusicPlayCommand(player)
            };
            remote.SetCommand(6, new MacroCommand(partyCommands));

            remote.PressButton(1);
            remote.PressButton(2);
            remote.PressButton(3);
            remote.Undo();
            remote.Redo();

            Console.WriteLine("\nЗапуск макрокоманды:");
            remote.PressButton(6);

            Console.WriteLine("\nОтмена макрокоманды:");
            remote.Undo();

            Console.WriteLine("\nПроверка пустого слота:");
            remote.PressButton(10);
        }
    }
}
