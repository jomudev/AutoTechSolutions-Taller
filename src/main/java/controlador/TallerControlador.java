package controlador;

import excepciones.ClienteNoEncontradoException;
import excepciones.PagoInsuficienteException;
import excepciones.StockInsuficienteException;
import hilos.ProcesoServicioHilo;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import modelo.Cliente;
import modelo.DetalleRepuesto;
import modelo.EstadoOrden;
import modelo.Factura;
import modelo.OrdenTrabajo;
import modelo.Repuesto;
import modelo.Servicio;
import modelo.Vehiculo;
import repositorio.RepositorioGenerico;

public class TallerControlador {

    private final RepositorioGenerico<Cliente> repoClientes;
    private final RepositorioGenerico<Vehiculo> repoVehiculos;
    private final RepositorioGenerico<Servicio> repoServicios;
    private final RepositorioGenerico<Repuesto> repoRepuestos;
    private final RepositorioGenerico<OrdenTrabajo> repoOrdenes;
    private final RepositorioGenerico<Factura> repoFacturas;

    private int correlativoOrden = 1;
    private int correlativoFactura = 1;

    public TallerControlador() {
        this.repoClientes = new RepositorioGenerico<>();
        this.repoVehiculos = new RepositorioGenerico<>();
        this.repoServicios = new RepositorioGenerico<>();
        this.repoRepuestos = new RepositorioGenerico<>();
        this.repoOrdenes = new RepositorioGenerico<>();
        this.repoFacturas = new RepositorioGenerico<>();

        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        repoServicios.agregar("SRV-01", new Servicio("SRV-01", "Cambio de aceite", 850.0, 45));
        repoServicios.agregar("SRV-02", new Servicio("SRV-02", "Revisión de frenos", 1200.0, 60));
        repoServicios.agregar("SRV-03", new Servicio("SRV-03", "Diagnóstico electrónico", 950.0, 40));
        repoServicios.agregar("SRV-04", new Servicio("SRV-04", "Alineamiento", 1100.0, 50));
        repoServicios.agregar("SRV-05", new Servicio("SRV-05", "Mantenimiento general", 2500.0, 120));

        repoRepuestos.agregar("REP-01", new Repuesto("REP-01", "Filtro de Aceite", 10, 250.0, 3));
        repoRepuestos.agregar("REP-02", new Repuesto("REP-02", "Aceite Sintetico 5W-30", 15, 350.0, 5));
        repoRepuestos.agregar("REP-03", new Repuesto("REP-03", "Pastillas de Freno Delanteras", 6, 1100.0, 2));
        repoRepuestos.agregar("REP-04", new Repuesto("REP-04", "Bujias de Iridio", 12, 180.0, 4));
        repoRepuestos.agregar("REP-05", new Repuesto("REP-05", "Liquido de Frenos DOT4", 4, 150.0, 2));
    }

    public void registrarCliente(String codigo, String nombre, String identidad, String telefono, String direccion) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new Exception("El codigo del cliente es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre completo es obligatorio.");
        }
        if (identidad == null || identidad.trim().isEmpty()) {
            throw new Exception("La identidad es obligatoria.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new Exception("El telefono es obligatorio.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new Exception("La direccion es obligatoria.");
        }
        if (repoClientes.existe(codigo)) {
            throw new Exception("Ya existe un cliente con el codigo: " + codigo);
        }

        Cliente cliente = new Cliente(codigo, nombre, identidad, telefono, direccion);
        repoClientes.agregar(cliente.getCodigo(), cliente);
    }

    public Cliente buscarCliente(String codigo) throws ClienteNoEncontradoException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ClienteNoEncontradoException("");
        }
        Cliente c = repoClientes.buscarPorClave(codigo);
        if (c == null) {
            throw new ClienteNoEncontradoException(codigo);
        }
        return c;
    }

    public void modificarCliente(String codigo, String nombre, String identidad, String telefono, String direccion) throws Exception {
        Cliente existente = buscarCliente(codigo);
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre completo es obligatorio.");
        }
        if (identidad == null || identidad.trim().isEmpty()) {
            throw new Exception("La identidad es obligatoria.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new Exception("El telefono es obligatorio.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new Exception("La direccion es obligatoria.");
        }

        existente.setNombreCompleto(nombre);
        existente.setIdentidad(identidad);
        existente.setTelefono(telefono);
        existente.setDireccion(direccion);
    }

    public void eliminarCliente(String codigo) throws Exception {
        Cliente c = buscarCliente(codigo);
        for (OrdenTrabajo ot : repoOrdenes.obtenerTodos()) {
            if (ot.getCliente() != null && ot.getCliente().equals(c)) {
                throw new Exception("No se puede eliminar el cliente porque tiene ordenes de trabajo asociadas.");
            }
        }
        repoClientes.eliminar(codigo);
    }

    public Cliente deshacerCliente() {
        return repoClientes.deshacerUltimo();
    }

    public void registrarVehiculo(String placa, String marca, String modelo, int anio, String color, String codigoPropietario) throws Exception {
        if (placa == null || placa.trim().isEmpty()) {
            throw new Exception("La placa del vehiculo es obligatoria.");
        }
        if (marca == null || marca.trim().isEmpty()) {
            throw new Exception("La marca es obligatoria.");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new Exception("El modelo es obligatorio.");
        }
        if (anio < 1960 || anio > 2028) {
            throw new Exception("El año del vehiculo debe estar entre 1960 y 2028.");
        }
        if (color == null || color.trim().isEmpty()) {
            throw new Exception("El color es obligatorio.");
        }
        if (repoVehiculos.existe(placa)) {
            throw new Exception("Ya existe un vehiculo registrado con la placa: " + placa);
        }

        Cliente propietario = buscarCliente(codigoPropietario);
        Vehiculo vehiculo = new Vehiculo(placa, marca, modelo, anio, color, propietario);
        repoVehiculos.agregar(vehiculo.getPlaca(), vehiculo);
    }

    public Vehiculo buscarVehiculo(String placa) throws Exception {
        if (placa == null || placa.trim().isEmpty()) {
            throw new Exception("Debe ingresar una placa.");
        }
        Vehiculo v = repoVehiculos.buscarPorClave(placa);
        if (v == null) {
            throw new Exception("No se encontro ningun vehiculo con la placa: " + placa);
        }
        return v;
    }

    public List<Vehiculo> listarVehiculosPorCliente(String codigoCliente) {
        List<Vehiculo> resultado = new ArrayList<>();
        if (codigoCliente == null || codigoCliente.trim().isEmpty()) {
            return resultado;
        }
        for (Vehiculo v : repoVehiculos.obtenerTodos()) {
            if (v.getPropietario() != null && v.getPropietario().getCodigo().equalsIgnoreCase(codigoCliente.trim())) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public Vehiculo deshacerVehiculo() {
        return repoVehiculos.deshacerUltimo();
    }

    public void registrarServicio(String codigo, String descripcion, double precio, int tiempo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new Exception("El codigo del servicio es obligatorio.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new Exception("La descripcion es obligatoria.");
        }
        if (precio <= 0) {
            throw new Exception("El precio base debe ser un valor positivo.");
        }
        if (tiempo <= 0) {
            throw new Exception("El tiempo estimado debe ser mayor a cero.");
        }
        if (repoServicios.existe(codigo)) {
            throw new Exception("Ya existe un servicio con el codigo: " + codigo);
        }

        Servicio s = new Servicio(codigo, descripcion, precio, tiempo);
        repoServicios.agregar(s.getCodigo(), s);
    }

    public void registrarRepuesto(String codigo, String nombre, int cantidad, double precio, int stockMinimo) throws Exception {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new Exception("El codigo del repuesto es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre del repuesto es obligatorio.");
        }
        if (cantidad < 0) {
            throw new Exception("La cantidad no puede ser negativa.");
        }
        if (precio <= 0) {
            throw new Exception("El precio unitario debe ser mayor a cero.");
        }
        if (stockMinimo < 0) {
            throw new Exception("El stock minimo no puede ser negativo.");
        }
        if (repoRepuestos.existe(codigo)) {
            throw new Exception("Ya existe un repuesto con el codigo: " + codigo);
        }

        Repuesto r = new Repuesto(codigo, nombre, cantidad, precio, stockMinimo);
        repoRepuestos.agregar(r.getCodigo(), r);
    }

    public void registrarEntradaRepuesto(String codigo, int cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("La cantidad a ingresar debe ser mayor a cero.");
        }
        Repuesto r = repoRepuestos.buscarPorClave(codigo);
        if (r == null) {
            throw new Exception("No existe el repuesto con codigo: " + codigo);
        }
        r.registrarEntrada(cantidad);
    }

    public List<Repuesto> obtenerAlertasStock() {
        List<Repuesto> alertas = new ArrayList<>();
        for (Repuesto r : repoRepuestos.obtenerTodos()) {
            if (r.tieneAlertaStockBajo()) {
                alertas.add(r);
            }
        }
        return alertas;
    }

    public Repuesto deshacerRepuesto() {
        return repoRepuestos.deshacerUltimo();
    }

    public String generarNumeroOrden() {
        String num;
        do {
            num = String.format("ORD-%03d", correlativoOrden++);
        } while (repoOrdenes.existe(num));
        return num;
    }

    public OrdenTrabajo crearOrden(String numero, String fecha, String codigoCliente, String placaVehiculo, String diagnostico, String mecanico, List<Servicio> servicios, List<DetalleRepuesto> repuestos) throws Exception {
        if (numero == null || numero.trim().isEmpty()) {
            throw new Exception("El numero de orden es obligatorio.");
        }
        if (repoOrdenes.existe(numero)) {
            throw new Exception("Ya existe una orden con el numero: " + numero);
        }
        Cliente c = buscarCliente(codigoCliente);
        Vehiculo v = buscarVehiculo(placaVehiculo);
        if (diagnostico == null || diagnostico.trim().isEmpty()) {
            throw new Exception("El diagnostico es obligatorio.");
        }
        if (mecanico == null || mecanico.trim().isEmpty()) {
            throw new Exception("El nombre del mecanico es obligatorio.");
        }
        if ((servicios == null || servicios.isEmpty()) && (repuestos == null || repuestos.isEmpty())) {
            throw new Exception("Debe incluir al menos un servicio o repuesto en la orden.");
        }

        if (repuestos != null) {
            for (DetalleRepuesto det : repuestos) {
                Repuesto enStock = repoRepuestos.buscarPorClave(det.getRepuesto().getCodigo());
                if (enStock == null || enStock.getCantidad() < det.getCantidad()) {
                    int disp = enStock != null ? enStock.getCantidad() : 0;
                    throw new StockInsuficienteException(det.getRepuesto().getCodigo(), det.getRepuesto().getNombre(), disp, det.getCantidad());
                }
            }
        }

        OrdenTrabajo ot = new OrdenTrabajo(numero, fecha, c, v, diagnostico, mecanico);
        if (servicios != null) {
            ot.setServicios(servicios);
        }
        if (repuestos != null) {
            ot.setRepuestos(repuestos);
        }
        ot.recalcularCostoTotal();

        repoOrdenes.agregar(ot.getNumeroOrden(), ot);
        return ot;
    }

    public void finalizarOrden(String numeroOrden) throws Exception {
        OrdenTrabajo ot = repoOrdenes.buscarPorClave(numeroOrden);
        if (ot == null) {
            throw new Exception("No se encontro la orden: " + numeroOrden);
        }
        if (ot.getEstado() == EstadoOrden.CANCELADA) {
            throw new Exception("No se puede finalizar una orden cancelada.");
        }
        if (ot.getEstado() == EstadoOrden.FINALIZADA && ot.isInventarioDescontado()) {
            throw new Exception("Esta orden ya fue finalizada previamente.");
        }

        if (!ot.isInventarioDescontado()) {
            for (DetalleRepuesto det : ot.getRepuestos()) {
                Repuesto r = repoRepuestos.buscarPorClave(det.getRepuesto().getCodigo());
                if (r != null) {
                    if (r.getCantidad() < det.getCantidad()) {
                        throw new StockInsuficienteException(r.getCodigo(), r.getNombre(), r.getCantidad(), det.getCantidad());
                    }
                    r.descontarExistencia(det.getCantidad());
                }
            }
            ot.setInventarioDescontado(true);
        }

        ot.setEstado(EstadoOrden.FINALIZADA);
    }

    public void procesarOrdenConHilo(String numeroOrden, ProcesoServicioHilo.Listener listener) throws Exception {
        OrdenTrabajo ot = repoOrdenes.buscarPorClave(numeroOrden);
        if (ot == null) {
            throw new Exception("Seleccione una orden valida.");
        }
        if (ot.isEnProceso()) {
            throw new Exception("La orden " + ot.getNumeroOrden() + " ya esta siendo procesada en segundo plano.");
        }
        if (ot.getEstado() == EstadoOrden.FINALIZADA) {
            throw new Exception("La orden ya se encuentra finalizada.");
        }
        if (ot.getEstado() == EstadoOrden.CANCELADA) {
            throw new Exception("No se puede procesar una orden cancelada.");
        }

        for (DetalleRepuesto det : ot.getRepuestos()) {
            Repuesto r = repoRepuestos.buscarPorClave(det.getRepuesto().getCodigo());
            if (r != null && r.getCantidad() < det.getCantidad()) {
                throw new StockInsuficienteException(r.getCodigo(), r.getNombre(), r.getCantidad(), det.getCantidad());
            }
        }

        ProcesoServicioHilo tarea = new ProcesoServicioHilo(ot, listener, () -> {
            try {
                finalizarOrden(ot.getNumeroOrden());
            } catch (Exception e) {
                if (listener != null) {
                    listener.onError(e.getMessage());
                }
            }
        });

        Thread hilo = new Thread(tarea, "ProcesoOrden-" + ot.getNumeroOrden());
        hilo.start();
    }

    public OrdenTrabajo deshacerOrden() {
        OrdenTrabajo ot = repoOrdenes.deshacerUltimo();
        if (ot != null && ot.isInventarioDescontado()) {
            for (DetalleRepuesto det : ot.getRepuestos()) {
                Repuesto r = repoRepuestos.buscarPorClave(det.getRepuesto().getCodigo());
                if (r != null) {
                    r.registrarEntrada(det.getCantidad());
                }
            }
            ot.setInventarioDescontado(false);
        }
        return ot;
    }

    public String generarNumeroFactura() {
        String num;
        do {
            num = String.format("FAC-%03d", correlativoFactura++);
        } while (repoFacturas.existe(num));
        return num;
    }

    public Factura generarFactura(String numeroFactura, String numeroOrden, double impuesto, double efectivo) throws Exception {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new Exception("El numero de factura es obligatorio.");
        }
        if (repoFacturas.existe(numeroFactura)) {
            throw new Exception("Ya existe una factura con el numero: " + numeroFactura);
        }
        OrdenTrabajo ot = repoOrdenes.buscarPorClave(numeroOrden);
        if (ot == null) {
            throw new Exception("Debe seleccionar una orden valida.");
        }
        if (ot.getEstado() != EstadoOrden.FINALIZADA) {
            throw new Exception("Solo se pueden facturar ordenes con estado 'Finalizada'. Estado actual: " + ot.getEstado().getNombre());
        }
        if (ot.isFacturada()) {
            throw new Exception("La orden " + ot.getNumeroOrden() + " ya fue facturada anteriormente.");
        }
        if (impuesto < 0 || impuesto > 100) {
            throw new Exception("El porcentaje de impuesto debe estar entre 0 y 100.");
        }

        Factura factura = new Factura(numeroFactura, ot, impuesto, efectivo);
        if (efectivo < factura.getTotal()) {
            throw new PagoInsuficienteException(factura.getTotal(), efectivo);
        }

        ot.setFacturada(true);
        repoFacturas.agregar(factura.getNumeroFactura(), factura);
        return factura;
    }

    public Factura deshacerFactura() {
        Factura f = repoFacturas.deshacerUltimo();
        if (f != null && f.getOrdenTrabajo() != null) {
            f.getOrdenTrabajo().setFacturada(false);
        }
        return f;
    }

    public Map<String, Double> obtenerIngresosPorServicio() {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Servicio s : repoServicios.obtenerTodos()) {
            map.put(s.getDescripcion(), 0.0);
        }
        for (OrdenTrabajo ot : repoOrdenes.obtenerTodos()) {
            if (ot.getEstado() != EstadoOrden.CANCELADA) {
                for (Servicio s : ot.getServicios()) {
                    map.put(s.getDescripcion(), map.getOrDefault(s.getDescripcion(), 0.0) + s.getPrecioBase());
                }
            }
        }
        return map;
    }

    public Map<String, Integer> obtenerOrdenesPorEstado() {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (EstadoOrden e : EstadoOrden.values()) {
            map.put(e.getNombre(), 0);
        }
        for (OrdenTrabajo ot : repoOrdenes.obtenerTodos()) {
            map.put(ot.getEstado().getNombre(), map.getOrDefault(ot.getEstado().getNombre(), 0) + 1);
        }
        return map;
    }

    public RepositorioGenerico<Cliente> getRepoClientes() {
        return repoClientes;
    }

    public RepositorioGenerico<Vehiculo> getRepoVehiculos() {
        return repoVehiculos;
    }

    public RepositorioGenerico<Servicio> getRepoServicios() {
        return repoServicios;
    }

    public RepositorioGenerico<Repuesto> getRepoRepuestos() {
        return repoRepuestos;
    }

    public RepositorioGenerico<OrdenTrabajo> getRepoOrdenes() {
        return repoOrdenes;
    }

    public RepositorioGenerico<Factura> getRepoFacturas() {
        return repoFacturas;
    }
}
