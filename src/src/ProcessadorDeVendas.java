package src.src;

import java.util.Date;

public class ProcessadorDeVendas {

    private final Cliente client;
    private final CalculadoraDeFrete ship;
    private final Pedido request;

    public ProcessadorDeVendas(Cliente client, CalculadoraDeFrete ship, Pedido request) {
        this.client = client;
        this.ship = ship;
        this.request = request;
    }

    public void processar() throws Exception {
        client.validateClientDocument();

        Pedido.Result result = request.processValues();
        double total = result.total();
        double tax = result.tax();

        double shipCost = ship.shipCostCalculator(client.clientType);

        double totalFinal = total + tax + shipCost;
        printReceipt(total, tax, shipCost, totalFinal, client, ship, request);
    }

    private void printReceipt(double total, double tax, double shipCost, double totalFinal, Cliente client, CalculadoraDeFrete ship, Pedido request) {
        System.out.println("=====================================");
        System.out.println("RECIBO DE VENDA");
        System.out.println("=====================================");
        System.out.println("Data: "  + new Date());
        System.out.println("src.src.Cliente: "  + client.clientName);
        System.out.println("Documento: "  + client.clientCPF);
        System.out.println("Local da entega: "  + ship.address + " - " + ship.state);
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

        Cliente cliente = new Cliente(
                "João Silva",
                "12345678901",
                Cliente.ClientType.COMM
        );

        CalculadoraDeFrete ship = new CalculadoraDeFrete(
                "Rua das Flores, 123",
                "SP"
        );

        Pedido request = new Pedido(
                valores,
                quantidades
        );

        ProcessadorDeVendas p = new ProcessadorDeVendas(
                cliente,
                ship,
                request
        );

        try {
            p.processar();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
