package L6E2;

import java.util.*;

interface IObserver {
    void update(String stockName, double newPrice);
    String getName();
}

interface ISubject {
    void subscribe(String stockName, IObserver observer);
    void unsubscribe(String stockName, IObserver observer);
    void setStockPrice(String stockName, double newPrice);
}


class StockExchange implements ISubject {

    private Map<String, Double> stocks = new HashMap<>();
    private Map<String, List<IObserver>> observers = new HashMap<>();

    @Override
    public void subscribe(String stockName, IObserver observer) {
        observers.putIfAbsent(stockName, new ArrayList<>());
        observers.get(stockName).add(observer);
        System.out.println(observer.getName() + " " + stockName + " tirkelgen ");
    }

    @Override
    public void unsubscribe(String stockName, IObserver observer) {
        if (observers.containsKey(stockName)) {
            observers.get(stockName).remove(observer);
            System.out.println(observer.getName() + " " + stockName + " tirkeluden bas tartty");
        }
    }

    @Override
    public void setStockPrice(String stockName, double newPrice) {
        stocks.put(stockName, newPrice);
        System.out.println("\nBirzha: " + stockName + " akzia bagasy "  + newPrice + " bagaga auysti ");
        notifyObservers(stockName, newPrice);
    }

    private void notifyObservers(String stockName, double newPrice) {
        if (observers.containsKey(stockName)) {
            for (IObserver observer : observers.get(stockName)) {
                observer.update(stockName, newPrice);
            }
        }
    }
}

class Trader implements IObserver {

    private String name;

    public Trader(String name) {
        this.name = name;
    }

    @Override
    public void update(String stockName, double newPrice) {
        System.out.println("Treider " + name +
                " janartu kabyldady: " + stockName +
                " = " + newPrice);
    }

    @Override
    public String getName() {
        return name;
    }
}

class TradingRobot implements IObserver {

    private String name;
    private double buyThreshold;
    private double sellThreshold;

    public TradingRobot(String name, double buyThreshold, double sellThreshold) {
        this.name = name;
        this.buyThreshold = buyThreshold;
        this.sellThreshold = sellThreshold;
    }

    @Override
    public void update(String stockName, double newPrice) {
        if (newPrice <= buyThreshold) {
            System.out.println("Bot " + name + " " + stockName +
                    " akziasyn " + newPrice
                    + " bagaga satyp aldy ");
        } else if (newPrice >= sellThreshold) {
            System.out.println("Bot " + name + " " + stockName +
                    " akziasyn " + newPrice +
                    " bagaga satty ");
        } else {
            System.out.println("Bot " + name + " " + stockName
                    +  " karauda " +
                    ", agymdagy baga: " + newPrice);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}

public class Esep2 {

    public static void main(String[] args) {

        StockExchange exchange = new StockExchange();

        Trader trader1 = new Trader("Dossymzhan");
        Trader trader2 = new Trader("Amanat");

        TradingRobot robot1 = new TradingRobot("AutoBot", 90, 150);

        exchange.subscribe("AAPL", trader1);
        exchange.subscribe("AAPL", robot1);
        exchange.subscribe("GOOG", trader2);
        exchange.subscribe("GOOG", robot1);

        exchange.setStockPrice("AAPL", 120);
        exchange.setStockPrice("AAPL", 85);
        exchange.setStockPrice("GOOG", 160);

        exchange.unsubscribe("AAPL", trader1);

        exchange.setStockPrice("AAPL", 200);
    }
}