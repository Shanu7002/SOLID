package src.src;

public class Cliente {
    public enum ClientType {
        VIP,
        COMM
    }

    public final String clientName;
    public final String clientCPF;
    public final ClientType clientType;

    public Cliente(String clientName, String clientCPF, ClientType clientType) {
        this.clientName = clientName;
        this.clientCPF = clientCPF;
        this.clientType = clientType;
    }

    private static final Integer CPF_LENGTH = 11;

    public void validateClientDocument() throws Exception{
        if (clientCPF != null && clientCPF.length() != CPF_LENGTH) throw new Exception("Invalid document");
    }
}
