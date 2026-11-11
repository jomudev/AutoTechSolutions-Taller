package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Factura {

    private String numeroFactura;
    private String fecha;
    private OrdenTrabajo ordenTrabajo;
    private double subtotalServicios;
    private double subtotalRepuestos;
    private double porcentajeImpuesto;
    private double montoImpuesto;
    private double total;
    private double efectivoRecibido;
    private double cambio;

    public Factura(String numeroFactura, OrdenTrabajo ordenTrabajo, double porcentajeImpuesto, double efectivoRecibido) {
        this.numeroFactura = numeroFactura != null ? numeroFactura.trim().toUpperCase() : "";
        this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.ordenTrabajo = ordenTrabajo;
        this.subtotalServicios = ordenTrabajo != null ? ordenTrabajo.calcularSubtotalServicios() : 0.0;
        this.subtotalRepuestos = ordenTrabajo != null ? ordenTrabajo.calcularSubtotalRepuestos() : 0.0;
        this.porcentajeImpuesto = porcentajeImpuesto;
        double base = this.subtotalServicios + this.subtotalRepuestos;
        this.montoImpuesto = Math.round(base * (porcentajeImpuesto / 100.0) * 100.0) / 100.0;
        this.total = Math.round((base + this.montoImpuesto) * 100.0) / 100.0;
        this.efectivoRecibido = efectivoRecibido;
        this.cambio = Math.round((efectivoRecibido - this.total) * 100.0) / 100.0;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getFecha() {
        return fecha;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public double getSubtotalServicios() {
        return subtotalServicios;
    }

    public double getSubtotalRepuestos() {
        return subtotalRepuestos;
    }

    public double getPorcentajeImpuesto() {
        return porcentajeImpuesto;
    }

    public double getMontoImpuesto() {
        return montoImpuesto;
    }

    public double getTotal() {
        return total;
    }

    public double getEfectivoRecibido() {
        return efectivoRecibido;
    }

    public double getCambio() {
        return cambio;
    }

    @Override
    public String toString() {
        return numeroFactura + " - Total: L. " + String.format("%.2f", total);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Factura)) return false;
        Factura factura = (Factura) o;
        return Objects.equals(numeroFactura, factura.numeroFactura);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroFactura);
    }
}
