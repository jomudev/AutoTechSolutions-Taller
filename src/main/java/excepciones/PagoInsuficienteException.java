package excepciones;

public class PagoInsuficienteException extends Exception {

    private final double totalFactura;
    private final double montoRecibido;

    public PagoInsuficienteException(double totalFactura, double montoRecibido) {
        super(String.format("El monto pagado (L. %.2f) es insuficiente para cubrir el total de la factura (L. %.2f). Faltante: L. %.2f", montoRecibido, totalFactura, (totalFactura - montoRecibido)));
        this.totalFactura = totalFactura;
        this.montoRecibido = montoRecibido;
    }

    public double getTotalFactura() {
        return totalFactura;
    }

    public double getMontoRecibido() {
        return montoRecibido;
    }
}
