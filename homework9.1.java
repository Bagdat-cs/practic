class TV {
    public void on() {
        System.out.println("TV включен");
    }
    public void off() {
        System.out.println("TV выключен");
    }
    public void setChannel(int channel) {
        System.out.println("TV переключен на канал " + channel);
    }
}

class AudioSystem {
    public void on() {
        System.out.println("Аудиосистема включена");
    }
    public void off() {
        System.out.println("Аудиосистема выключена");
    }
    public void setVolume(int level) {
        System.out.println("Громкость установлена на " + level);
    }
}

class DVDPlayer {
    public void on() {
        System.out.println("DVD-проигрыватель включен");
    }
    public void play() {
        System.out.println("Воспроизведение DVD");
    }
    public void pause() {
        System.out.println("Пауза DVD");
    }
    public void stop() {
        System.out.println("Остановка DVD");
    }
    public void off() {
        System.out.println("DVD-проигрыватель выключен");
    }
}

class GameConsole {
    public void on() {
        System.out.println("Игровая консоль включена");
    }
    public void startGame(String game) {
        System.out.println("Запуск игры: " + game);
    }
    public void off() {
        System.out.println("Игровая консоль выключена");
    }
}

class HomeTheaterFacade {
    private TV tv;
    private AudioSystem audio;
    private DVDPlayer dvd;
    private GameConsole console;

    public HomeTheaterFacade(TV tv, AudioSystem audio, DVDPlayer dvd, GameConsole console) {
        this.tv = tv;
        this.audio = audio;
        this.dvd = dvd;
        this.console = console;
    }

    public void watchMovie() {
        System.out.println("\n--- Подготовка к просмотру фильма ---");
        tv.on();
        tv.setChannel(1);
        audio.on();
        audio.setVolume(5);
        dvd.on();
        dvd.play();
    }

    public void stopMovie() {
        System.out.println("\n--- Завершение просмотра фильма ---");
        dvd.stop();
        dvd.off();
        audio.off();
        tv.off();
    }

    public void playGame(String game) {
        System.out.println("\n--- Запуск игровой консоли ---");
        tv.on();
        audio.on();
        audio.setVolume(7);
        console.on();
        console.startGame(game);
    }

    public void stopGame() {
        System.out.println("\n--- Завершение игры ---");
        console.off();
        audio.off();
        tv.off();
    }

    public void listenMusic() {
        System.out.println("\n--- Прослушивание музыки ---");
        tv.on();
        audio.on();
        audio.setVolume(6);
        System.out.println("TV установлен на аудиовход");
    }

    public void setVolume(int level) {
        audio.setVolume(level);
    }

    public void turnOffAll() {
        System.out.println("\n--- Выключение всей системы ---");
        tv.off();
        audio.off();
        dvd.off();
        console.off();
    }
}

public class Client {
    public static void main(String[] args) {
        TV tv = new TV();
        AudioSystem audio = new AudioSystem();
        DVDPlayer dvd = new DVDPlayer();
        GameConsole console = new GameConsole();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(tv, audio, dvd, console);

        homeTheater.watchMovie();
        homeTheater.stopMovie();

        homeTheater.playGame("FIFA 2026");
        homeTheater.stopGame();

        homeTheater.listenMusic();
        homeTheater.setVolume(10);

        homeTheater.turnOffAll();
    }
}
