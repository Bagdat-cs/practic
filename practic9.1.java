class RoomBookingSystem {
    public void bookRoom(String guestName) {
        System.out.println("Номер забронирован для гостя: " + guestName);
    }
    public void cancelBooking(String guestName) {
        System.out.println("Бронирование номера отменено для гостя: " + guestName);
    }
    public void checkAvailability() {
        System.out.println("Проверка доступности номеров...");
    }
}

class RestaurantSystem {
    public void bookTable(String guestName, int seats) {
        System.out.println("Столик на " + seats + " мест забронирован для " + guestName);
    }
    public void orderFood(String guestName, String dish) {
        System.out.println("Заказано блюдо '" + dish + "' для " + guestName);
    }
}

class EventManagementSystem {
    public void bookHall(String eventName) {
        System.out.println("Конференц-зал забронирован для мероприятия: " + eventName);
    }
    public void orderEquipment(String eventName, String equipment) {
        System.out.println("Заказано оборудование '" + equipment + "' для мероприятия: " + eventName);
    }
}

class CleaningService {
    public void scheduleCleaning(String room) {
        System.out.println("Уборка запланирована для номера: " + room);
    }
    public void performCleaning(String room) {
        System.out.println("Уборка выполнена в номере: " + room);
    }
}

class TaxiService {
    public void orderTaxi(String guestName) {
        System.out.println("Такси заказано для гостя: " + guestName);
    }
}

class HotelFacade {
    private RoomBookingSystem roomBooking;
    private RestaurantSystem restaurant;
    private EventManagementSystem eventManagement;
    private CleaningService cleaning;
    private TaxiService taxi;

    public HotelFacade(RoomBookingSystem roomBooking, RestaurantSystem restaurant,
                       EventManagementSystem eventManagement, CleaningService cleaning,
                       TaxiService taxi) {
        this.roomBooking = roomBooking;
        this.restaurant = restaurant;
        this.eventManagement = eventManagement;
        this.cleaning = cleaning;
        this.taxi = taxi;
    }

    public void bookRoomWithServices(String guestName, String dish, String room) {
        System.out.println("\n--- Бронирование номера с услугами ---");
        roomBooking.bookRoom(guestName);
        restaurant.orderFood(guestName, dish);
        cleaning.scheduleCleaning(room);
    }

    public void organizeEvent(String eventName, String equipment, String guestName) {
        System.out.println("\n--- Организация мероприятия ---");
        eventManagement.bookHall(eventName);
        eventManagement.orderEquipment(eventName, equipment);
        roomBooking.bookRoom(guestName);
    }

    public void bookRestaurantWithTaxi(String guestName, int seats) {
        System.out.println("\n--- Бронирование ресторана с такси ---");
        restaurant.bookTable(guestName, seats);
        taxi.orderTaxi(guestName);
    }

    public void cancelRoomBooking(String guestName) {
        System.out.println("\n--- Отмена бронирования ---");
        roomBooking.cancelBooking(guestName);
    }

    public void requestCleaning(String room) {
        System.out.println("\n--- Уборка по запросу ---");
        cleaning.performCleaning(room);
    }
}

public class Client {
    public static void main(String[] args) {
        RoomBookingSystem roomBooking = new RoomBookingSystem();
        RestaurantSystem restaurant = new RestaurantSystem();
        EventManagementSystem eventManagement = new EventManagementSystem();
        CleaningService cleaning = new CleaningService();
        TaxiService taxi = new TaxiService();

        HotelFacade hotel = new HotelFacade(roomBooking, restaurant, eventManagement, cleaning, taxi);

        hotel.bookRoomWithServices("Иван Иванов", "Паста Карбонара", "101");

        hotel.organizeEvent("Бизнес-конференция", "Проектор", "Петр Петров");

        hotel.bookRestaurantWithTaxi("Сергей Сергеев", 4);

        hotel.cancelRoomBooking("Иван Иванов");

        hotel.requestCleaning("101");
    }
}
