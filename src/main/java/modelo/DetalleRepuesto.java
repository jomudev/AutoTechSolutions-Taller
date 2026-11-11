package modelo;

public class DetalleRepuesto {

    private Repuesto repuesto;
    private int cantidad;
    private double precioUnitario;

    public DetalleRepuesto(Repuesto repuesto, int cantidad) {
        this.repuesto = repuesto;
        this.cantidad = cantidad;
        this.precioUnitario = repuesto != null ? repuesto.getPrecioUnitario() : 0.0;
    }

    public DetalleRepuesto(Repuesto repuesto, int cantidad, double precioUnitario) {
        this.repuesto = repuesto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    public Repuesto getRepuesto() {
        return repuesto;
    }

    public void setRepuesto(Repuesto repuesto) {
        this.repuesto = repuesto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    @Override
    public String toString() {
        return (repuesto != null ? repuesto.getNombre() : "") + " x" + cantidad + " (L. " + String.format("%.2f", getSubtotal()) + ")";
    }
}
