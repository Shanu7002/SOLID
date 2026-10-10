import java.util.Date;

public class ProcessadorDeVendas {

    // Váriaveis com nomes ruins e mistura de responsabilidades (Large Class / Data Clumps)
    private String n;
    private String doc;
    private int tc;
    private String end;
    private double[] val;
    private int[] qtd;

    public ProcessadorDeVendas(String n, String doc, int tc, String end, double[] val, int[] qtd) {
        this.n = n;
        this.doc = doc;
        this.tc = tc;
        this.end = end;
        this.val = val;
        this.qtd = qtd;
    }

    // Método Longo (Long Method) fazendo de tudo: validação, cálculo, frete e impressão
    public void processar() {
        System.out.println("Iniciando processamento...");

        // Code Smell: Validação misturada e números mágicos (Magic Numbers)
        if (doc == null || doc.length() != 11) {
            System.out.println("Erro: Documento invalido.");
            return;
        }

        double t = 0; // Nome ruim (total)
        double imp = 0; // Nome ruim (imposto)

        // Code Smell: Lógica de cálculo misturada
        for (int i = 0; i < val.length; i++) {
            double sub = val[i] * qtd[i];
            t += sub;

            // Code Smell: Números mágicos espalhados e lógica duplicada
            if (val[i] > 100) {
                imp += sub * 0.15; // 15% de imposto para produtos caros
            } else {
                imp += sub * 0.05; // 5% de imposto para produtos baratos
            }
        }

        double f = 0; // frete

        // Code Smell: Feature Envy / Switch statements / Magic Numbers
        if (tc == 1) { // 1 = Cliente VIP
            f = 0;
        } else if (tc == 2) { // 2 = Cliente Comum
            if (end.contains("SP") || end.contains("RJ") || end.contains("MG")) {
                f = 15.0;
            } else {
                f = 50.0;
            }
        } else {
            f = 50.0;
        }

        double totalFinal = t + imp + f;

        // Code Smell: Responsabilidade de apresentação (View) misturada com negócio
        System.out.println("=====================================");
        System.out.println("RECIBO DE VENDA");
        System.out.println("=====================================");
        System.out.println("Data: " + new Date());
        System.out.println("Cliente: " + n);
        System.out.println("Documento: " + doc);
        System.out.println("-------------------------------------");
        System.out.println("Subtotal Itens: R$ " + t);
        System.out.println("Total Impostos: R$ " + imp);
        System.out.println("Frete: R$ " + f);
        System.out.println("-------------------------------------");
        System.out.println("TOTAL A PAGAR: R$ " + totalFinal);
        System.out.println("=====================================");
    }

    // Método main apenas para o aluno testar se o comportamento mudou
    public static void main(String[] args) {
        double[] valores = {50.0, 150.0, 30.0};
        int[] quantidades = {2, 1, 3};

        ProcessadorDeVendas p = new ProcessadorDeVendas(
                "João Silva",
                "12345678901",
                2,
                "Rua das Flores, 123 - SP",
                valores,
                quantidades
        );

        p.processar();
    }
}