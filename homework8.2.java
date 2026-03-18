interface IPaymentProcessor {
    void processPayment(double amount);
}

class PayPalPaymentProcessor implements IPaymentProcessor {
    @Override
    public void processPayment(double amount) {
        System.out.println("Обработка платежа через PayPal на сумму " + amount + " $");
    }
}

class StripePaymentService {
    public void makeTransaction(double totalAmount) {
        System.out.println("Обработка транзакции через Stripe на сумму " + totalAmount + " $");
    }
}

class StripePaymentAdapter implements IPaymentProcessor {
    private StripePaymentService stripeService;

    public StripePaymentAdapter(StripePaymentService stripeService) {
        this.stripeService = stripeService;
    }

    @Override
    public void processPayment(double amount) {
        stripeService.makeTransaction(amount);
    }
}

class SquarePaymentService {
    public void pay(double sum) {
        System.out.println("Обработка платежа через Square на сумму " + sum + " $");
    }
}

class SquarePaymentAdapter implements IPaymentProcessor {
    private SquarePaymentService squareService;

    public SquarePaymentAdapter(SquarePaymentService squareService) {
        this.squareService = squareService;
    }

    @Override
    public void processPayment(double amount) {
        squareService.pay(amount);
    }
}

public class PaymentSystemDemo {
    public static void main(String[] args) {
        IPaymentProcessor paypalProcessor = new PayPalPaymentProcessor();
        paypalProcessor.processPayment(100.0);

        StripePaymentService stripeService = new StripePaymentService();
        IPaymentProcessor stripeProcessor = new StripePaymentAdapter(stripeService);
        stripeProcessor.processPayment(200.0);

        SquarePaymentService squareService = new SquarePaymentService();
        IPaymentProcessor squareProcessor = new SquarePaymentAdapter(squareService);
        squareProcessor.processPayment(300.0);
    }
}
