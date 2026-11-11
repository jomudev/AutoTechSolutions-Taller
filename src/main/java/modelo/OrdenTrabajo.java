package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdenTrabajo {

    private String numeroOrden;
    private String fecha;
    private Cliente cliente;
    private Vehiculo vehiculo;
    private String diagnostico;
    private String mecanico;
    private List<Servicio> servicios;
    private List<DetalleRepuesto> repuestos;
    private double costoTotal;
    private EstadoOrden estado;
    private boolean inventarioDescontado;
    private boolean enProceso;
    private boolean facturada;

    public OrdenTrabajo(String numeroOrden, String fecha, Cliente cliente, Vehiculo vehiculo, String diagnostico, String mecanico) {
        this.numeroOrden = numeroOrden != null ? numeroOrden.trim().toUpperCase() : "";
        if (fecha != null && !fecha.isBlank()) {
            this.fecha = fecha.trim();
        } else {
            this.fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        this.cliente = cliente;
        this.vehiculo = vehiculo;
        this.diagnostico = diagnostico != null ? diagnostico.trim() : "";
        this.mecanico = mecanico != null ? mecanico.trim() : "";
        this.servicios = new ArrayList<>();
        this.repuestos = new ArrayList<>();
        this.estado = EstadoOrden.PENDIENTE;
        this.inventarioDescontado = false;
        this.enProceso = false;
        this.facturada = false;
        this.costoTotal = 0.0;
    }

    public double calcularSubtotalServicios() {
        double subtotal = 0.0;
        for (Servicio s : servicios) {
            subtotal += s.getPrecioBase();
        }
        return subtotal;
    }

    public double calcularSubtotalRepuestos() {
        double subtotal = 0.0;
        for (DetalleRepuesto d : repuestos) {
            subtotal += d.getSubtotal();
        }
        return subtotal;
    }

    public void recalcularCostoTotal() {
        this.costoTotal = calcularSubtotalServicios() + calcularSubtotalRepuestos();
    }

    public void agregarServicio(Servicio servicio) {
        if (servicio != null && !servicios.contains(servicio)) {
            servicios.add(servicio);
            recalcularCostoTotal();
        }
    }

    public void agregarRepuesto(Repuesto repuesto, int cantidad) {
        if (repuesto == null || cantidad <= 0) {
            return;
        }
        for (DetalleRepuesto d : repuestos) {
            if (d.getRepuesto().equals(repuesto)) {
                d.setCantidad(d.getCantidad() + cantidad);
                recalcularCostoTotal();
                return;
            }
        }
        repuestos.add(new DetalleRepuesto(repuesto, cantidad));
        recalcularCostoTotal();
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden != null ? numeroOrden.trim().toUpperCase() : "";
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getMecanico() {
        return mecanico;
    }

    public void setMecanico(String mecanico) {
        this.mecanico = mecanico;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios != null ? servicios : new ArrayList<>();
        recalcularCostoTotal();
    }

    public List<DetalleRepuesto> getRepuestos() {
        return repuestos;
    }

    public void setRepuestos(List<DetalleRepuesto> repuestos) {
        this.repuestos = repuestos != null ? repuestos : new ArrayList<>();
        recalcularCostoTotal();
    }

    public double getCostoTotal() {
        recalcularCostoTotal();
        return costoTotal;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public boolean isInventarioDescontado() {
        return inventarioDescontado;
    }

    public void setInventarioDescontado(boolean inventarioDescontado) {
        this.inventarioDescontado = inventarioDescontado;
    }

    public boolean isEnProceso() {
        return enProceso;
    }

    public void setEnProceso(boolean enProceso) {
        this.enProceso = enProceso;
    }

    public boolean isFacturada() {
        return facturada;
    }

    public void setFacturada(boolean facturada) {
        this.facturada = facturada;
    }

    @Override
    public String toString() {
        return numeroOrden + " - " + (cliente != null ? cliente.getNombreCompleto() : "") + " (" + estado.getNombre() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrdenTrabajo)) return false;
        OrdenTrabajo that = (OrdenTrabajo) o;
        return Objects.equals(numeroOrden, that.numeroOrden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroOrden);
    }
}
