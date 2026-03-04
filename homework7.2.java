using System;

namespace TemplateMethodPattern
{
    abstract class Beverage
    {
        public void PrepareRecipe()
        {
            BoilWater();
            Brew();
            PourInCup();
            if (CustomerWantsCondiments())
            {
                AddCondiments();
            }
        }

        private void BoilWater()
        {
            Console.WriteLine("Кипятим воду");
        }

        private void PourInCup()
        {
            Console.WriteLine("Наливаем в чашку");
        }

        protected abstract void Brew();
        protected abstract void AddCondiments();

        protected virtual bool CustomerWantsCondiments()
        {
            Console.Write("Хотите добавить добавки (y/n)? ");
            string answer = Console.ReadLine()?.ToLower();

            if (answer == "y") return true;
            if (answer == "n") return false;

            Console.WriteLine("Некорректный ввод, добавки не будут добавлены.");
            return false;
        }
    }

    class Tea : Beverage
    {
        protected override void Brew()
        {
            Console.WriteLine("Завариваем чай");
        }

        protected override void AddCondiments()
        {
            Console.WriteLine("Добавляем лимон");
        }
    }

    class Coffee : Beverage
    {
        protected override void Brew()
        {
            Console.WriteLine("Завариваем кофе");
        }

        protected override void AddCondiments()
        {
            Console.WriteLine("Добавляем сахар и молоко");
        }
    }

    class HotChocolate : Beverage
    {
        protected override void Brew()
        {
            Console.WriteLine("Размешиваем какао-порошок в горячей воде");
        }

        protected override void AddCondiments()
        {
            Console.WriteLine("Добавляем взбитые сливки");
        }

        protected override bool CustomerWantsCondiments()
        {
            return true;
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("=== Приготовление чая ===");
            Beverage tea = new Tea();
            tea.PrepareRecipe();

            Console.WriteLine("\n=== Приготовление кофе ===");
            Beverage coffee = new Coffee();
            coffee.PrepareRecipe();

            Console.WriteLine("\n=== Приготовление горячего шоколада ===");
            Beverage hotChocolate = new HotChocolate();
            hotChocolate.PrepareRecipe();
        }
    }
}
