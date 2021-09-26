package OrdersApp;

@FunctionalInterface
public interface MultiConsumer<T> {
    public void accept(T...t);
}
