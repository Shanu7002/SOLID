package src;

import java.util.Date;

public class ProcessadorDeVendas {

    private final Cliente client;
    private final EstrategiaFrete estrategiaFrete;
    private final Pedido request;

    public ProcessadorDeVendas(Cliente client, EstrategiaFrete estrategiaFrete, Pedido request) {
        this.client = client;
        this.estrategiaFrete = estrategiaFrete;
        this.request = request;
    }
    public void processar() throws Exception {
        client.validateClientDocument();

        Pedido.Result result = request.processValues();
        double total = result.total();
        double tax = result.tax();

        double shipCost = estrategiaFrete.calcularFrete(client);

        double totalFinal = total + tax + shipCost;
        printReceipt(total, tax, shipCost, totalFinal, client, request);
    }

    private void printReceipt(double total, double tax, double shipCost, double totalFinal,Cliente client, Pedido request) {
        System.out.println("=====================================");
        System.out.println("RECIBO DE VENDA");
        System.out.println("=====================================");
        System.out.println("Data: "  + new Date());
        System.out.println("src.src.src.Cliente: "  + client.clientName);
        System.out.println("Documento: "  + client.clientCPF);
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


        EstrategiaFrete estrategia = new FreteSudesteStrategy();

        Pedido request = new Pedido(
                valores,
                quantidades
        );

        ProcessadorDeVendas p = new ProcessadorDeVendas(
                cliente,
                estrategia,
                request);

        try {
            p.processar();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
