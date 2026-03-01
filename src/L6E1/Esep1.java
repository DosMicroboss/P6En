package L6E1;
import java.util.Scanner;

public class Esep1 {

    interface ICostCalculationStrategy {
        double calculateCost(TravelRequest request);
    }

    enum ServiceClass {
        ECONOMY,
        BUSINESS
    }

    enum DiscountType {
        NONE,
        CHILD,
        PENSIONER
    }

    static class TravelRequest {

        private double distance;
        private ServiceClass serviceClass;
        private DiscountType discountType;
        private int passengers;
        private boolean hasExtraLuggage;

        public TravelRequest(double distance,
                             ServiceClass serviceClass,
                             DiscountType discountType,
                             int passengers,
                             boolean hasExtraLuggage) {
            this.distance = distance;
            this.serviceClass = serviceClass;
            this.discountType = discountType;
            this.passengers = passengers;
            this.hasExtraLuggage = hasExtraLuggage;
        }

        public double getDistance() { return distance; }
        public ServiceClass getServiceClass() { return serviceClass; }
        public DiscountType getDiscountType() { return discountType; }
        public int getPassengers() { return passengers; }
        public boolean hasExtraLuggage() { return hasExtraLuggage; }
    }

    static class PlaneCostStrategy implements ICostCalculationStrategy {

        @Override
        public double calculateCost(TravelRequest request) {

            double baseRate = 9.8;
            double cost = request.getDistance() * baseRate;

            if (request.getServiceClass() == ServiceClass.BUSINESS) {
                cost *= 2;
            }

            if (request.hasExtraLuggage()) {
                cost += 50;
            }

            cost = applyDiscount(cost, request.getDiscountType());

            return cost * request.getPassengers();
        }

        private double applyDiscount(double cost, DiscountType discountType) {
            switch (discountType) {
                case CHILD: return cost * 0.8;
                case PENSIONER: return cost * 0.85;
                default: return cost;
            }
        }
    }

    static class TrainCostStrategy implements ICostCalculationStrategy {

        @Override
        public double calculateCost(TravelRequest request) {

            double baseRate = 9.8;
            double cost = request.getDistance() * baseRate;

            if (request.getServiceClass() == ServiceClass.BUSINESS) {
                cost *= 1.5;
            }

            cost = applyDiscount(cost, request.getDiscountType());

            return cost * request.getPassengers();
        }

        private double applyDiscount(double cost, DiscountType discountType) {
            switch (discountType) {
                case CHILD: return cost * 0.9;
                case PENSIONER: return cost * 0.9;
                default: return cost;
            }
        }
    }

    static class BusCostStrategy implements ICostCalculationStrategy {

        @Override
        public double calculateCost(TravelRequest request) {

            double baseRate = 7.6;
            double cost = request.getDistance() * baseRate;

            cost = applyDiscount(cost, request.getDiscountType());

            return cost * request.getPassengers();
        }

        private double applyDiscount(double cost, DiscountType discountType) {
            switch (discountType) {
                case CHILD: return cost * 0.95;
                case PENSIONER: return cost * 0.9;
                default: return cost;
            }
        }
    }

    static class TravelBookingContext {

        private ICostCalculationStrategy strategy;

        public void setStrategy(ICostCalculationStrategy strategy) {
            this.strategy = strategy;
        }

        public double calculate(TravelRequest request) {
            if (strategy == null) {
                throw new IllegalStateException("Strategy not selected");
            }
            return strategy.calculateCost(request);
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Transport tandanyz: 1 - Samolet, 2 - Poezd, 3 - Avtobus");
        int transportChoice = scanner.nextInt();

        System.out.println("Kashyktyk (km): ");
        double distance = scanner.nextDouble();

        System.out.println("Type: 1 - Econom, 2 - Buisness");
        int classChoice = scanner.nextInt();
        ServiceClass serviceClass =
                (classChoice == 2) ? ServiceClass.BUSINESS : ServiceClass.ECONOMY;

        System.out.println("Jenildik: 0 - Jok, 1 - Bala, 2 - Zeinetker");
        int discountChoice = scanner.nextInt();

        DiscountType discountType;
        switch (discountChoice) {
            case 1: discountType = DiscountType.CHILD; break;
            case 2: discountType = DiscountType.PENSIONER; break;
            default: discountType = DiscountType.NONE;
        }

        System.out.println("Adam sany:");
        int passengers = scanner.nextInt();

        System.out.println("Juk? (true/false):");
        boolean extraLuggage = scanner.nextBoolean();

        TravelRequest request = new TravelRequest(
                distance,
                serviceClass,
                discountType,
                passengers,
                extraLuggage
        );

        TravelBookingContext context = new TravelBookingContext();

        switch (transportChoice) {
            case 1: context.setStrategy(new PlaneCostStrategy()); break;
            case 2: context.setStrategy(new TrainCostStrategy()); break;
            case 3: context.setStrategy(new BusCostStrategy()); break;
            default:
                System.out.println("ERR");
                return;
        }

        double totalCost = context.calculate(request);

        System.out.println("Barlygy: " + totalCost);
    }
}