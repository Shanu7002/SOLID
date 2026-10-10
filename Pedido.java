public class Pedido {

    private final double[] value;
    private final int[] quantity;

    public Pedido(double[] value, int[] quantity) {
        this.value = value;
        this.quantity = quantity;
    }

    private static final Integer DISCOUNT_THRESHOLD = 100;
    private static final double EXPENSIVE_PRODUCTS_DISCOUNT = 0.15; // 15%
    private static final double CHEAP_PRODUCTS_DISCOUNT = 0.05; // 5%

    public Result processValues() {
        double total = 0, tax = 0;
        for (int i = 0; i < value.length; i++) {
            double subTotal = value[i] * quantity[i];

            total += subTotal;
            tax += taxCalculator(subTotal, value[i]);
        }
        return new Result(total, tax);
    }

    private double taxCalculator(double subtotal, double productValue) {
        if (productValue > DISCOUNT_THRESHOLD) {
            return subtotal * EXPENSIVE_PRODUCTS_DISCOUNT;
        }
        return subtotal * CHEAP_PRODUCTS_DISCOUNT;
    }

        public record Result(double total, double tax) {}
}
