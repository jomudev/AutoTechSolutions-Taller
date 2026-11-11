package excepciones;

public class StockInsuficienteException extends Exception {

    private final String codigoRepuesto;
    private final int stockDisponible;
    private final int cantidadSolicitada;

    public StockInsuficienteException(String codigoRepuesto, String nombreRepuesto, int stockDisponible, int cantidadSolicitada) {
        super("Stock insuficiente para " + nombreRepuesto + " (" + codigoRepuesto + "). Disponible: " + stockDisponible + ", Solicitado: " + cantidadSolicitada);
        this.codigoRepuesto = codigoRepuesto;
        this.stockDisponible = stockDisponible;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
        this.codigoRepuesto = "";
        this.stockDisponible = 0;
        this.cantidadSolicitada = 0;
    }

    public String getCodigoRepuesto() {
        return codigoRepuesto;
    }

    public int getStockDisponible() {
        return stockDisponible;
    }

    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }
}
