package modelo;

import java.util.Objects;

public class Servicio {

    private String codigo;
    private String descripcion;
    private double precioBase;
    private int tiempoEstimadoMinutos;

    public Servicio(String codigo, String descripcion, double precioBase, int tiempoEstimadoMinutos) {
        this.codigo = codigo != null ? codigo.trim().toUpperCase() : "";
        this.descripcion = descripcion != null ? descripcion.trim() : "";
        this.precioBase = precioBase;
        this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo != null ? codigo.trim().toUpperCase() : "";
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getTiempoEstimadoMinutos() {
        return tiempoEstimadoMinutos;
    }

    public void setTiempoEstimadoMinutos(int tiempoEstimadoMinutos) {
        this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
    }

    @Override
    public String toString() {
        return codigo + " - " + descripcion + " (L. " + String.format("%.2f", precioBase) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Servicio)) return false;
        Servicio servicio = (Servicio) o;
        return Objects.equals(codigo, servicio.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
