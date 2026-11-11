package modelo;

import excepciones.StockInsuficienteException;
import java.util.Objects;

public class Repuesto {

    private String codigo;
    private String nombre;
    private int cantidad;
    private double precioUnitario;
    private int stockMinimo;

    public Repuesto(String codigo, String nombre, int cantidad, double precioUnitario, int stockMinimo) {
        this.codigo = codigo != null ? codigo.trim().toUpperCase() : "";
        this.nombre = nombre != null ? nombre.trim() : "";
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.stockMinimo = stockMinimo;
    }

    public void registrarEntrada(int unidades) {
        if (unidades > 0) {
            this.cantidad += unidades;
        }
    }

    public void descontarExistencia(int unidades) throws StockInsuficienteException {
        if (unidades <= 0) {
            return;
        }
        if (this.cantidad < unidades) {
            throw new StockInsuficienteException(codigo, nombre, cantidad, unidades);
        }
        this.cantidad -= unidades;
    }

    public boolean tieneAlertaStockBajo() {
        return this.cantidad <= this.stockMinimo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo != null ? codigo.trim().toUpperCase() : "";
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (Stock: " + cantidad + " | L. " + String.format("%.2f", precioUnitario) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Repuesto)) return false;
        Repuesto repuesto = (Repuesto) o;
        return Objects.equals(codigo, repuesto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
