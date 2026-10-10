import java.util.Date;

public class ProcessadorDeVendas {

    public enum ClientType {
        VIP,
        COMM
    }

    // Váriaveis com nomes ruins e mistura de responsabilidades (Large Class / Data Clumps)
    private String clientName;
    private String clientCPF;
    private ClientType clientType;
    private String address;
    private double[] value;
    private int[] quantity;

    public ProcessadorDeVendas(String clientName, String doc, ClientType clientType, String address, double[] value, int[] quantity) {
        this.clientName = clientName;
        this.clientCPF = doc;
        this.clientType = clientType;
        this.address = address;
        this.value = value;
        this.quantity = quantity;
    }

    private static final Integer CPF_LENGTH = 11;
    private static final Integer DISCOUNT_THRESHOLD = 100;
    private static final double EXPENSIVE_PRODUCTS_DISCOUNT = 0.15; // 15%
    private static final double CHEAP_PRODUCTS_DISCOUNT = 0.05; // 5%

    // Método Longo (Long Method) fazendo de tudo: validação, cálculo, frete e impressão
    public void processar() {
        System.out.println("Iniciando processamento...");

        if (clientCPF == null || clientCPF.length() != CPF_LENGTH) {
            System.out.println("Erro: Documento invalido.");
            return;
        }

        double total = 0, tax = 0, shipCost = 0;

        // Code Smell: Lógica de cálculo misturada
        for (int i = 0; i < value.length; i++) {
            double subTotal = value[i] * quantity[i];
            total += subTotal;

            // Code Smell: Números mágicos espalhados e lógica duplicada
            if (value[i] > DISCOUNT_THRESHOLD) {
                tax += subTotal * EXPENSIVE_PRODUCTS_DISCOUNT;
                continue;
            }

            tax += subTotal * CHEAP_PRODUCTS_DISCOUNT;
        }

        // Code Smell: Feature Envy / Switch statements / Magic Numbers
        if (clientType == ClientType.VIP) {
            shipCost = 0;
        } else if (clientType == ClientType.COMM) {
            if (address.contains("SP") || address.contains("RJ") || address.contains("MG")) {
                shipCost = 15.0;
            } else {
                shipCost = 50.0;
            }
        } else {
            shipCost = 50.0;
        }

        double totalFinal = total + tax + shipCost;

        // Code Smell: Responsabilidade de apresentação (View) misturada com negócio
        System.out.println("=====================================");
        System.out.println("RECIBO DE VENDA");
        System.out.println("=====================================");
        System.out.println("Data: "  + new Date());
        System.out.println("Cliente: "  + clientName);
        System.out.println("Documento: "  + clientCPF);
        System.out.println("-------------------------------------");
        System.out.println("Subtotal Itens:  R$" + total);
        System.out.println("Total Impostos: R$"  + tax);
        System.out.println("Frete: R$"  + shipCost);
        System.out.println("-------------------------------------");
        System.out.println("TOTAL A PAGAR: R$"  + totalFinal);
        System.out.println("=====================================");
    }

    // Método main apenas para o aluno testar se o comportamento mudou
    public static void main(String[] args) {
        double[] valores = {50.0, 150.0, 30.0};
        int[] quantidades = {2, 1, 3};

        ProcessadorDeVendas p = new ProcessadorDeVendas(
                "João Silva",
                "12345678901",
                ClientType.COMM,
                "Rua das Flores, 123 - SP",
                valores,
                quantidades
        );

        p.processar();
    }
}
