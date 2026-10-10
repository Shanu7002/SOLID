public class CalculadoraDeFrete {

    public final String address;
    public final String state;

    public CalculadoraDeFrete(String address, String state) {
        this.address = address;
        this.state = state;
    }

    public double shipCostCalculator(Cliente.ClientType clientType) {
        if (clientType == Cliente.ClientType.VIP) {
            return 0;
        }
        if (state.equals("SP") || state.equals("RJ") || state.equals("MG")) {
            return 15.0;
        }
        return 50.0;
    }
}
