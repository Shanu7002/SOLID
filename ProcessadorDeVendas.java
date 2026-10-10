import java.util.Date;

public class ProcessadorDeVendas {

    public enum ClientType {
        VIP,
        COMM
    }

    private final String clientName;
    private final String clientCPF;
    private final ClientType clientType;
    private final String address;
    private final String state;
    private final double[] value;
    private final int[] quantity;

    public ProcessadorDeVendas(String clientName, String clientCPF, ClientType clientType, String address, String state, double[] value, int[] quantity) {
        this.clientName = clientName;
        this.clientCPF = clientCPF;
        this.clientType = clientType;
        this.address = address;
        this.state = state;
        this.value = value;
        this.quantity = quantity;
    }

    private static final Integer CPF_LENGTH = 11;
    private static final Integer DISCOUNT_THRESHOLD = 100;
    private static final double EXPENSIVE_PRODUCTS_DISCOUNT = 0.15; // 15%
    private static final double CHEAP_PRODUCTS_DISCOUNT = 0.05; // 5%

    public void processar() throws Exception {
        if (!validateClientDocument(clientCPF)) throw new Exception("Invalid document");

        double total = 0, tax = 0;

        for (int i = 0; i < value.length; i++) {
            double subTotal = value[i] * quantity[i];

            total += subTotal;
            tax += taxCalculator(subTotal, value[i]);
        }
        double shipCost = shipCostCalculator(clientType);

        double totalFinal = total + tax + shipCost;
        printReceipt(total, tax, shipCost, totalFinal);
    }

    private double shipCostCalculator(ClientType clientType) {
        if (clientType == ClientType.VIP) {
            return 0;
        }
        if (state.equals("SP") || state.equals("RJ") || state.equals("MG")) {
            return 15.0;
        }
        return 50.0;
    }

    private double taxCalculator(double subtotal, double productValue) {
        if (productValue > DISCOUNT_THRESHOLD) {
            return subtotal * EXPENSIVE_PRODUCTS_DISCOUNT;
        }
        return subtotal * CHEAP_PRODUCTS_DISCOUNT;
    }

    private boolean validateClientDocument(String doc) {
        return doc != null && doc.length() == CPF_LENGTH;
    }

    private void printReceipt(double total, double tax, double shipCost, double totalFinal) {
        System.out.println("=====================================");
        System.out.println("RECIBO DE VENDA");
        System.out.println("=====================================");
        System.out.println("Data: "  + new Date());
        System.out.println("Cliente: "  + clientName);
        System.out.println("Documento: "  + clientCPF);
        System.out.println("Local da entega: "  + address + " - " + state);
        System.out.println("-------------------------------------");
        System.out.println("Subtotal Itens:  R$" + total);
        System.out.println("Total Impostos: R$"  + tax);
        System.out.println("Frete: R$"  + shipCost);
        System.out.println("-------------------------------------");
        System.out.println("TOTAL A PAGAR: R$"  + totalFinal);
        System.out.println("=====================================");
    }

    public static void main(String[] args) {
        double[] valores = {50.0, 150.0, 30.0};
        int[] quantidades = {2, 1, 3};

        ProcessadorDeVendas p = new ProcessadorDeVendas(
                "João Silva",
                "12345678901",
                ClientType.COMM,
                "Rua das Flores, 123",
                "SP",
                valores,
                quantidades
        );

        try {
            p.processar();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
