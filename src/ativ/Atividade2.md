## Laboratório Prático 2: Padrão Strategy e Princípios SOLID (Refatorando Regras de Frete)

Na semana passada, você atuou como o cirurgião do código: pegou o src.ProcessadorDeVendas legado (aquele com variáveis como n, tc, números mágicos e métodos gigantes), extraiu métodos e separou as responsabilidades. O código base fornecido no final desta atividade já é o resultado dessa primeira limpeza.

No entanto, o negócio não para. Hoje, o Diretor de Logística avisou que a empresa vai fechar contrato com novas transportadoras (JadLog, Correios PAC e Sedex), cada uma com uma regra complexa de cálculo baseada em peso e região.

Se você olhar para a classe CalculadoraFrete atual no código base, o cálculo ainda é um bloco rígido de if/else. Se você adicionar as novas transportadoras ali, o método vai inchar novamente, a Complexidade Ciclomática vai explodir e você violará o Princípio Aberto/Fechado (OCP) do SOLID.

Sua missão hoje é blindar a arquitetura aplicando o Padrão de Projeto STRATEGY, garantindo que o sistema esteja aberto para novas transportadoras, mas fechado para modificações no código principal.

Missões do Laboratório (Passo a Passo)
A partir do código base fornecido no final deste documento, implemente a seguinte arquitetura orientada a objetos:

Passo 1: Segregação de Interface (ISP - Interface Segregation Principle) Crie uma interface chamada EstrategiaFrete. Ela deve conter um único método enxuto e direto: double calcularFrete(src.Cliente cliente).

Passo 2: Responsabilidade Única (SRP) nas Classes Concretas Crie classes separadas que implementam a interface EstrategiaFrete. Cada classe terá um, e apenas um, motivo para mudar (SRP). Crie as seguintes estratégias concretas para substituir os ifs antigos:

FreteVipStrategy (Retorna 0.0)

FreteSudesteStrategy (Retorna 15.0)

FreteNacionalStrategy (Retorna 50.0)

Passo 3: Inversão de Dependência (DIP) e Substituição de Liskov (LSP) Altere a classe src.ProcessadorDeVendas. Ela não deve mais conhecer a antiga classe concreta CalculadoraFrete.

Ela deve possuir um atributo do tipo da interface: private EstrategiaFrete estrategiaFrete;

Crie um construtor (ou método setter) em src.ProcessadorDeVendas para injetar a estratégia desejada dinamicamente.

No momento do processamento, o código fará apenas: double valorFrete = this.estrategiaFrete.calcularFrete(cliente);

Passo 4: Validando a Arquitetura (OCP - Open/Closed Principle) Altere o seu método main. Instancie o src.ProcessadorDeVendas injetando estratégias diferentes para clientes diferentes. O recibo impresso deve refletir o frete da estratégia injetada.

Reflexão para a entrega: Se o Diretor pedir amanhã o FreteInternacionalStrategy, quantas linhas de código você precisará alterar na classe src.ProcessadorDeVendas? Nenhuma! O sistema está aberto para extensão.

Formato de Entrega
A entrega no portal D2L deve conter um único arquivo .ZIP incluindo:

Pasta src/: Contendo todo o seu projeto Java. Deve ficar visível a interface EstrategiaFrete, as classes concretas de frete e a injeção na classe principal.

Relatório arquitetura.pdf ou .md: Um documento curto respondendo diretamente:

Como a aplicação do Padrão Strategy reduziu a Complexidade Ciclomática da classe original?

Como essa nova estrutura respeita rigorosamente o OCP (Open/Closed Principle) e o DIP (Dependency Inversion Principle)?

Código Base para a Atividade (Ponto de Partida)
Crie as classes abaixo no seu projeto. Este é o código limpo (resultado da aula passada), mas que ainda não possui Padrões de Projeto (GoF). O seu alvo principal de extermínio e refatoração é a classe CalculadoraFrete.

1. Classe src.Cliente

Java
public class src.Cliente {
private String nome;
private String documento;
private int tipoCliente; // 1 = VIP, 2 = Comum
private String estado;

    public src.Cliente(String nome, String documento, int tipoCliente, String estado) {
        this.nome = nome;
        this.documento = documento;
        this.tipoCliente = tipoCliente;
        this.estado = estado;
    }

    public String getNome() { return nome; }
    public String getDocumento() { return documento; }
    public int getTipoCliente() { return tipoCliente; }
    public String getEstado() { return estado; }
    
    public boolean isDocumentoValido() {
        return documento != null && documento.length() == 11;
    }
}
2. Classe Item

Java
public class Item {
private double valor;
private int quantidade;

    public Item(double valor, int quantidade) {
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public double getValor() { return valor; }
    public int getQuantidade() { return quantidade; }
    public double getSubtotal() { return valor * quantidade; }
}
3. Classe CalculadoraFrete (O ALVO DA SUA REFATORAÇÃO STRATEGY)

Java
public class CalculadoraFrete {

    // ATENÇÃO: Este método viola o OCP e tem alta Complexidade Ciclomática.
    // Você deve EXCLUIR esta classe e substituí-la pela Interface e pelas Estratégias Concretas!
    public double calcular(src.Cliente cliente) {
        if (cliente.getTipoCliente() == 1) { 
            return 0.0; // Frete VIP
        } else if (cliente.getTipoCliente() == 2) { 
            String estado = cliente.getEstado();
            if (estado.equals("SP") || estado.equals("RJ") || estado.equals("MG")) {
                return 15.0; // Frete Sudeste
            } else {
                return 50.0; // Frete Nacional
            }
        }
        return 50.0; // Default
    }
}
4. Classe src.ProcessadorDeVendas

Java
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class src.ProcessadorDeVendas {

    // VIOLAÇÃO DE DIP (Dependência de classe concreta). 
    // Isso deve ser substituído pela Interface EstrategiaFrete injetada via construtor!
    private CalculadoraFrete calculadoraFrete = new CalculadoraFrete();

    public void processar(src.Cliente cliente, List<Item> itens) {
        System.out.println("Iniciando processamento...");

        if (!cliente.isDocumentoValido()) {
            System.out.println("Erro: Documento invalido.");
            return;
        }

        double subtotal = calcularSubtotal(itens);
        double impostos = calcularImpostos(itens);
        
        // Aqui o Strategy será acionado no futuro:
        double frete = calculadoraFrete.calcular(cliente); 

        double totalFinal = subtotal + impostos + frete;
        imprimirRecibo(cliente, subtotal, impostos, frete, totalFinal);
    }

    private double calcularSubtotal(List<Item> itens) {
        double total = 0;
        for (Item item : itens) {
            total += item.getSubtotal();
        }
        return total;
    }

    private double calcularImpostos(List<Item> itens) {
        double impostoTotal = 0;
        for (Item item : itens) {
            if (item.getValor() > 100) {
                impostoTotal += item.getSubtotal() * 0.15;
            } else {
                impostoTotal += item.getSubtotal() * 0.05;
            }
        }
        return impostoTotal;
    }

    private void imprimirRecibo(src.Cliente cliente, double subtotal, double impostos, double frete, double totalFinal) {
        System.out.println("=====================================");
        System.out.println("RECIBO DE VENDA");
        System.out.println("=====================================");
        System.out.println("Data: " + new Date());
        System.out.println("src.Cliente: " + cliente.getNome());
        System.out.println("Documento: " + cliente.getDocumento());
        System.out.println("-------------------------------------");
        System.out.println("Subtotal Itens: R$ " + subtotal);
        System.out.println("Total Impostos: R$ " + impostos);
        System.out.println("Frete: R$ " + frete);
        System.out.println("-------------------------------------");
        System.out.println("TOTAL A PAGAR: R$ " + totalFinal);
        System.out.println("=====================================");
    }

    public static void main(String[] args) {
        src.Cliente cliente = new src.Cliente("João Silva", "12345678901", 2, "SP");
        
        List<Item> itens = new ArrayList<>();
        itens.add(new Item(50.0, 2));
        itens.add(new Item(150.0, 1));
        itens.add(new Item(30.0, 3));

        src.ProcessadorDeVendas processador = new src.ProcessadorDeVendas();
        
        // Na sua refatoração, a estratégia será injetada no processador!
        processador.processar(cliente, itens);
    }
}