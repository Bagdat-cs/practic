interface IInternalDeliveryService {
    void deliverOrder(String orderId);
    String getDeliveryStatus(String orderId);
    double calculateDeliveryCost(String orderId);
}

class InternalDeliveryService implements IInternalDeliveryService {
    @Override
    public void deliverOrder(String orderId) {
        System.out.println("Внутренняя доставка начата для заказа: " + orderId);
    }

    @Override
    public String getDeliveryStatus(String orderId) {
        return "Статус внутренней доставки для заказа " + orderId + ": Доставлено";
    }

    @Override
    public double calculateDeliveryCost(String orderId) {
        return 10.0;
    }
}

class ExternalLogisticsServiceA {
    public void shipItem(int itemId) {
        System.out.println("Внешняя служба A отправляет товар: " + itemId);
    }

    public String trackShipment(int shipmentId) {
        return "Внешняя служба A отслеживает отправку " + shipmentId + ": В пути";
    }

    public double computeCost(int itemId) {
        return 15.0 + itemId;
    }
}

class LogisticsAdapterA implements IInternalDeliveryService {
    private ExternalLogisticsServiceA serviceA;

    public LogisticsAdapterA(ExternalLogisticsServiceA serviceA) {
        this.serviceA = serviceA;
    }

    @Override
    public void deliverOrder(String orderId) {
        try {
            int itemId = Integer.parseInt(orderId.replaceAll("\\D", ""));
            serviceA.shipItem(itemId);
        } catch (Exception e) {
            System.err.println("Ошибка в адаптере A: " + e.getMessage());
        }
    }

    @Override
    public String getDeliveryStatus(String orderId) {
        int shipmentId = orderId.hashCode();
        return serviceA.trackShipment(shipmentId);
    }

    @Override
    public double calculateDeliveryCost(String orderId) {
        int itemId = orderId.hashCode() % 100;
        return serviceA.computeCost(itemId);
    }
}

class ExternalLogisticsServiceB {
    public void sendPackage(String packageInfo) {
        System.out.println("Внешняя служба B отправляет посылку: " + packageInfo);
    }

    public String checkPackageStatus(String trackingCode) {
        return "Статус внешней службы B для " + trackingCode + ": В ожидании";
    }

    public double estimateFee(String packageInfo) {
        return packageInfo.length() * 2.0;
    }
}

class LogisticsAdapterB implements IInternalDeliveryService {
    private ExternalLogisticsServiceB serviceB;

    public LogisticsAdapterB(ExternalLogisticsServiceB serviceB) {
        this.serviceB = serviceB;
    }

    @Override
    public void deliverOrder(String orderId) {
        try {
            serviceB.sendPackage("Заказ#" + orderId);
        } catch (Exception e) {
            System.err.println("Ошибка в адаптере B: " + e.getMessage());
        }
    }

    @Override
    public String getDeliveryStatus(String orderId) {
        return serviceB.checkPackageStatus("Трек-" + orderId);
    }

    @Override
    public double calculateDeliveryCost(String orderId) {
        return serviceB.estimateFee(orderId);
    }
}

class ExternalLogisticsServiceC {
    public void initiateDelivery(String code, double weight) {
        System.out.println("Внешняя служба C доставляет посылку " + code + " с весом " + weight);
    }

    public String monitorDelivery(String code) {
        return "Внешняя служба C отслеживает " + code + ": Доставлено";
    }

    public double deliveryCharge(double weight) {
        return weight * 5.0;
    }
}
class LogisticsAdapterC implements IInternalDeliveryService {
    private ExternalLogisticsServiceC serviceC;

    public LogisticsAdapterC(ExternalLogisticsServiceC serviceC) {
        this.serviceC = serviceC;
    }

    @Override
    public void deliverOrder(String orderId) {
        try {
            serviceC.initiateDelivery(orderId, orderId.length());
        } catch (Exception e) {
            System.err.println("Ошибка в адаптере C: " + e.getMessage());
        }
    }

    @Override
    public String getDeliveryStatus(String orderId) {
        return serviceC.monitorDelivery(orderId);
    }

    @Override
    public double calculateDeliveryCost(String orderId) {
        return serviceC.deliveryCharge(orderId.length());
    }
}

class DeliveryServiceFactory {
    public static IInternalDeliveryService getService(String type) {
        switch (type.toLowerCase()) {
            case "internal":
                return new InternalDeliveryService();
            case "a":
                return new LogisticsAdapterA(new ExternalLogisticsServiceA());
            case "b":
                return new LogisticsAdapterB(new ExternalLogisticsServiceB());
            case "c":
                return new LogisticsAdapterC(new ExternalLogisticsServiceC());
            default:
                throw new IllegalArgumentException("Неизвестный тип службы: " + type);
        }
    }
}

public class LogisticsSystemDemo {
    public static void main(String[] args) {
        IInternalDeliveryService service1 = DeliveryServiceFactory.getService("internal");
        service1.deliverOrder("Order123");
        System.out.println(service1.getDeliveryStatus("Order123"));
        System.out.println("Стоимость доставки: " + service1.calculateDeliveryCost("Order123") + " $");

        IInternalDeliveryService service2 = DeliveryServiceFactory.getService("a");
        service2.deliverOrder("Order456");
        System.out.println(service2.getDeliveryStatus("Order456"));
        System.out.println("Стоимость доставки: " + service2.calculateDeliveryCost("Order456") + " $");

        IInternalDeliveryService service3 = DeliveryServiceFactory.getService("b");
        service3.deliverOrder("Order789");
        System.out.println(service3.getDeliveryStatus("Order789"));
        System.out.println("Стоимость доставки: " + service3.calculateDeliveryCost("Order789") + " $");

        IInternalDeliveryService service4 = DeliveryServiceFactory.getService("c");
        service4.deliverOrder("OrderABC");
        System.out.println(service4.getDeliveryStatus("OrderABC"));
        System.out.println("Стоимость доставки: " + service4.calculateDeliveryCost("OrderABC") + " $");
    }
}
