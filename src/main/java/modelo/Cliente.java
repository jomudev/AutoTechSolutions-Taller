package modelo;

import java.util.Objects;

public class Cliente {

    private String codigo;
    private String nombreCompleto;
    private String identidad;
    private String telefono;
    private String direccion;

    public Cliente(String codigo, String nombreCompleto, String identidad, String telefono, String direccion) {
        this.codigo = codigo != null ? codigo.trim().toUpperCase() : "";
        this.nombreCompleto = nombreCompleto != null ? nombreCompleto.trim() : "";
        this.identidad = identidad != null ? identidad.trim() : "";
        this.telefono = telefono != null ? telefono.trim() : "";
        this.direccion = direccion != null ? direccion.trim() : "";
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo != null ? codigo.trim().toUpperCase() : "";
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombreCompleto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(codigo, cliente.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
