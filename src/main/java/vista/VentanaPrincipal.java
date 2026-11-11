package vista;

import controlador.TallerControlador;
import excepciones.ClienteNoEncontradoException;
import excepciones.PagoInsuficienteException;
import excepciones.StockInsuficienteException;
import hilos.ProcesoServicioHilo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.DetalleRepuesto;
import modelo.EstadoOrden;
import modelo.Factura;
import modelo.OrdenTrabajo;
import modelo.Repuesto;
import modelo.Servicio;
import modelo.Vehiculo;

public class VentanaPrincipal extends JFrame {

    private final TallerControlador controlador;

    private JTextField txtClienteCodigo;
    private JTextField txtClienteNombre;
    private JTextField txtClienteIdentidad;
    private JTextField txtClienteTelefono;
    private JTextField txtClienteDireccion;
    private JTable tablaClientes;
    private DefaultTableModel modeloTablaClientes;

    private JTextField txtVehiculoPlaca;
    private JTextField txtVehiculoMarca;
    private JTextField txtVehiculoModelo;
    private JTextField txtVehiculoAnio;
    private JTextField txtVehiculoColor;
    private JComboBox<Cliente> cbVehiculoPropietario;
    private JComboBox<Cliente> cbFiltroVehiculoCliente;
    private JTable tablaVehiculos;
    private DefaultTableModel modeloTablaVehiculos;

    private JTextField txtServicioCodigo;
    private JTextField txtServicioDescripcion;
    private JTextField txtServicioPrecio;
    private JTextField txtServicioTiempo;
    private JTable tablaServicios;
    private DefaultTableModel modeloTablaServicios;

    private JTextField txtRepuestoCodigo;
    private JTextField txtRepuestoNombre;
    private JTextField txtRepuestoCantidad;
    private JTextField txtRepuestoPrecio;
    private JTextField txtRepuestoStockMin;
    private JTable tablaRepuestos;
    private DefaultTableModel modeloTablaRepuestos;

    private JTextField txtOrdenNumero;
    private JComboBox<Cliente> cbOrdenCliente;
    private JComboBox<Vehiculo> cbOrdenVehiculo;
    private JTextField txtOrdenDiagnostico;
    private JTextField txtOrdenMecanico;
    private JComboBox<Servicio> cbOrdenServiciosCatalogo;
    private JComboBox<Repuesto> cbOrdenRepuestosCatalogo;
    private JTextField txtOrdenRepuestoCantidad;
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTablaOrdenes;
    private JTable tablaDetallesOrdenActual;
    private DefaultTableModel modeloTablaDetallesOrdenActual;
    private JLabel lblTotalOrdenActual;
    private JProgressBar pgbOrden;
    private JLabel lblEstadoHilo;
    private JButton btnProcesarHilo;

    private final List<Servicio> serviciosOrdenActual = new ArrayList<>();
    private final List<DetalleRepuesto> repuestosOrdenActual = new ArrayList<>();

    private JComboBox<OrdenTrabajo> cbFacturaOrden;
    private JTextField txtFacturaNumero;
    private JTextField txtFacturaSubtotalServicios;
    private JTextField txtFacturaSubtotalRepuestos;
    private JTextField txtFacturaImpuesto;
    private JTextField txtFacturaTotal;
    private JTextField txtFacturaEfectivo;
    private JTextField txtFacturaCambio;
    private JTable tablaFacturas;
    private DefaultTableModel modeloTablaFacturas;

    private CanvasReporte2D canvasReporte;

    public VentanaPrincipal() {
        this.controlador = new TallerControlador();
        initComponents();
        actualizarTodasLasTablas();
    }

    private void initComponents() {
        setTitle("AutoTech Solutions - Sistema de Gestion de Taller Automotriz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("SansSerif", Font.BOLD, 12));

        pestanas.addTab("Clientes", crearPanelClientes());
        pestanas.addTab("Vehiculos", crearPanelVehiculos());
        pestanas.addTab("Servicios Mecanicos", crearPanelServicios());
        pestanas.addTab("Inventario Repuestos", crearPanelRepuestos());
        pestanas.addTab("Ordenes de Trabajo", crearPanelOrdenes());
        pestanas.addTab("Facturacion", crearPanelFacturacion());
        pestanas.addTab("Reportes Java 2D", crearPanelReportes());

        pestanas.addChangeListener(e -> {
            int index = pestanas.getSelectedIndex();
            if (index == 1) {
                actualizarComboPropietarios();
            } else if (index == 4) {
                actualizarCombosOrden();
            } else if (index == 5) {
                actualizarComboOrdenesFinalizadas();
            } else if (index == 6) {
                actualizarReportes();
            }
        });

        add(pestanas, BorderLayout.CENTER);
    }

    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtClienteCodigo = new JTextField(12);
        txtClienteNombre = new JTextField(20);
        txtClienteIdentidad = new JTextField(15);
        txtClienteTelefono = new JTextField(12);
        txtClienteDireccion = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Codigo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelForm.add(txtClienteCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0; panelForm.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; panelForm.add(txtClienteNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Identidad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtClienteIdentidad, gbc);

        gbc.gridx = 2; gbc.gridy = 1; panelForm.add(new JLabel("Telefono:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; panelForm.add(txtClienteTelefono, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Direccion:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; panelForm.add(txtClienteDireccion, gbc);
        gbc.gridwidth = 1;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnGuardar = new JButton("Registrar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnDeshacer = new JButton("Deshacer");
        JButton btnLimpiar = new JButton("Limpiar");

        btnGuardar.addActionListener(e -> registrarCliente());
        btnModificar.addActionListener(e -> modificarCliente());
        btnBuscar.addActionListener(e -> buscarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnDeshacer.addActionListener(e -> deshacerCliente());
        btnLimpiar.addActionListener(e -> limpiarCamposCliente());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnDeshacer);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panelForm.add(panelBotones, gbc);

        modeloTablaClientes = new DefaultTableModel(new String[]{"Codigo", "Nombre", "Identidad", "Telefono", "Direccion"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaClientes = new JTable(modeloTablaClientes);
        tablaClientes.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaClientes.getSelectedRow();
            if (fila >= 0) {
                txtClienteCodigo.setText((String) modeloTablaClientes.getValueAt(fila, 0));
                txtClienteNombre.setText((String) modeloTablaClientes.getValueAt(fila, 1));
                txtClienteIdentidad.setText((String) modeloTablaClientes.getValueAt(fila, 2));
                txtClienteTelefono.setText((String) modeloTablaClientes.getValueAt(fila, 3));
                txtClienteDireccion.setText((String) modeloTablaClientes.getValueAt(fila, 4));
            }
        });

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        return panel;
    }

    private void registrarCliente() {
        try {
            controlador.registrarCliente(
                txtClienteCodigo.getText().trim(),
                txtClienteNombre.getText().trim(),
                txtClienteIdentidad.getText().trim(),
                txtClienteTelefono.getText().trim(),
                txtClienteDireccion.getText().trim()
            );
            JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposCliente();
            actualizarTablaClientes();
            actualizarComboPropietarios();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarCliente() {
        try {
            controlador.modificarCliente(
                txtClienteCodigo.getText().trim(),
                txtClienteNombre.getText().trim(),
                txtClienteIdentidad.getText().trim(),
                txtClienteTelefono.getText().trim(),
                txtClienteDireccion.getText().trim()
            );
            JOptionPane.showMessageDialog(this, "Cliente modificado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaClientes();
            actualizarComboPropietarios();
        } catch (ClienteNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cliente No Encontrado", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCliente() {
        try {
            Cliente c = controlador.buscarCliente(txtClienteCodigo.getText().trim());
            txtClienteNombre.setText(c.getNombreCompleto());
            txtClienteIdentidad.setText(c.getIdentidad());
            txtClienteTelefono.setText(c.getTelefono());
            txtClienteDireccion.setText(c.getDireccion());
            JOptionPane.showMessageDialog(this, "Cliente encontrado: " + c.getNombreCompleto(), "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClienteNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cliente No Encontrado", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        try {
            controlador.eliminarCliente(txtClienteCodigo.getText().trim());
            JOptionPane.showMessageDialog(this, "Cliente eliminado.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposCliente();
            actualizarTablaClientes();
            actualizarComboPropietarios();
        } catch (ClienteNoEncontradoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Cliente No Encontrado", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deshacerCliente() {
        Cliente deshecho = controlador.deshacerCliente();
        if (deshecho != null) {
            JOptionPane.showMessageDialog(this, "Se deshizo la ultima operacion sobre: " + deshecho.getNombreCompleto(), "Deshacer", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaClientes();
            actualizarComboPropietarios();
        } else {
            JOptionPane.showMessageDialog(this, "No hay operaciones para deshacer.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarCamposCliente() {
        txtClienteCodigo.setText("");
        txtClienteNombre.setText("");
        txtClienteIdentidad.setText("");
        txtClienteTelefono.setText("");
        txtClienteDireccion.setText("");
    }

    private void actualizarTablaClientes() {
        modeloTablaClientes.setRowCount(0);
        for (Cliente c : controlador.getRepoClientes().obtenerTodos()) {
            modeloTablaClientes.addRow(new Object[]{
                c.getCodigo(), c.getNombreCompleto(), c.getIdentidad(), c.getTelefono(), c.getDireccion()
            });
        }
    }

    private JPanel crearPanelVehiculos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Vehiculo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtVehiculoPlaca = new JTextField(12);
        txtVehiculoMarca = new JTextField(15);
        txtVehiculoModelo = new JTextField(15);
        txtVehiculoAnio = new JTextField(8);
        txtVehiculoColor = new JTextField(12);
        cbVehiculoPropietario = new JComboBox<>();
        cbFiltroVehiculoCliente = new JComboBox<>();

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Placa:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelForm.add(txtVehiculoPlaca, gbc);

        gbc.gridx = 2; gbc.gridy = 0; panelForm.add(new JLabel("Propietario:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; panelForm.add(cbVehiculoPropietario, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtVehiculoMarca, gbc);

        gbc.gridx = 2; gbc.gridy = 1; panelForm.add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; panelForm.add(txtVehiculoModelo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Año:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelForm.add(txtVehiculoAnio, gbc);

        gbc.gridx = 2; gbc.gridy = 2; panelForm.add(new JLabel("Color:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; panelForm.add(txtVehiculoColor, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrar = new JButton("Registrar Vehiculo");
        JButton btnBuscar = new JButton("Buscar por Placa");
        JButton btnDeshacer = new JButton("Deshacer");
        JButton btnLimpiar = new JButton("Limpiar");

        btnRegistrar.addActionListener(e -> registrarVehiculo());
        btnBuscar.addActionListener(e -> buscarVehiculo());
        btnDeshacer.addActionListener(e -> deshacerVehiculo());
        btnLimpiar.addActionListener(e -> limpiarCamposVehiculo());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnDeshacer);
        panelBotones.add(btnLimpiar);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFiltro.add(new JLabel("Listar por Cliente:"));
        panelFiltro.add(cbFiltroVehiculoCliente);
        JButton btnFiltrar = new JButton("Filtrar");
        JButton btnVerTodos = new JButton("Ver Todos");
        btnFiltrar.addActionListener(e -> filtrarVehiculosPorCliente());
        btnVerTodos.addActionListener(e -> actualizarTablaVehiculos());
        panelFiltro.add(btnFiltrar);
        panelFiltro.add(btnVerTodos);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panelForm.add(panelBotones, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        panelForm.add(panelFiltro, gbc);

        modeloTablaVehiculos = new DefaultTableModel(new String[]{"Placa", "Marca", "Modelo", "Año", "Color", "Propietario"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaVehiculos = new JTable(modeloTablaVehiculos);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaVehiculos), BorderLayout.CENTER);

        return panel;
    }

    private void registrarVehiculo() {
        try {
            Cliente prop = (Cliente) cbVehiculoPropietario.getSelectedItem();
            if (prop == null) {
                throw new Exception("Debe seleccionar un cliente propietario.");
            }
            int anio = Integer.parseInt(txtVehiculoAnio.getText().trim());
            controlador.registrarVehiculo(
                txtVehiculoPlaca.getText().trim(),
                txtVehiculoMarca.getText().trim(),
                txtVehiculoModelo.getText().trim(),
                anio,
                txtVehiculoColor.getText().trim(),
                prop.getCodigo()
            );
            JOptionPane.showMessageDialog(this, "Vehiculo registrado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposVehiculo();
            actualizarTablaVehiculos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El año debe ser un numero entero valido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarVehiculo() {
        try {
            Vehiculo v = controlador.buscarVehiculo(txtVehiculoPlaca.getText().trim());
            txtVehiculoMarca.setText(v.getMarca());
            txtVehiculoModelo.setText(v.getModelo());
            txtVehiculoAnio.setText(String.valueOf(v.getAnio()));
            txtVehiculoColor.setText(v.getColor());
            cbVehiculoPropietario.setSelectedItem(v.getPropietario());
            JOptionPane.showMessageDialog(this, "Vehiculo encontrado: " + v, "Resultado", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deshacerVehiculo() {
        Vehiculo deshecho = controlador.deshacerVehiculo();
        if (deshecho != null) {
            JOptionPane.showMessageDialog(this, "Se deshizo la ultima operacion sobre placa: " + deshecho.getPlaca(), "Deshacer", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaVehiculos();
        } else {
            JOptionPane.showMessageDialog(this, "No hay operaciones de vehiculos para deshacer.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void filtrarVehiculosPorCliente() {
        Cliente sel = (Cliente) cbFiltroVehiculoCliente.getSelectedItem();
        if (sel == null) return;
        modeloTablaVehiculos.setRowCount(0);
        for (Vehiculo v : controlador.listarVehiculosPorCliente(sel.getCodigo())) {
            modeloTablaVehiculos.addRow(new Object[]{
                v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnio(), v.getColor(), v.getPropietario().getNombreCompleto()
            });
        }
    }

    private void limpiarCamposVehiculo() {
        txtVehiculoPlaca.setText("");
        txtVehiculoMarca.setText("");
        txtVehiculoModelo.setText("");
        txtVehiculoAnio.setText("");
        txtVehiculoColor.setText("");
    }

    private void actualizarComboPropietarios() {
        cbVehiculoPropietario.removeAllItems();
        cbFiltroVehiculoCliente.removeAllItems();
        for (Cliente c : controlador.getRepoClientes().obtenerTodos()) {
            cbVehiculoPropietario.addItem(c);
            cbFiltroVehiculoCliente.addItem(c);
        }
    }

    private void actualizarTablaVehiculos() {
        modeloTablaVehiculos.setRowCount(0);
        for (Vehiculo v : controlador.getRepoVehiculos().obtenerTodos()) {
            modeloTablaVehiculos.addRow(new Object[]{
                v.getPlaca(), v.getMarca(), v.getModelo(), v.getAnio(), v.getColor(),
                v.getPropietario() != null ? v.getPropietario().getNombreCompleto() : ""
            });
        }
    }

    private JPanel crearPanelServicios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Catalogo de Servicios Mecanicos"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtServicioCodigo = new JTextField(10);
        txtServicioDescripcion = new JTextField(20);
        txtServicioPrecio = new JTextField(10);
        txtServicioTiempo = new JTextField(10);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Codigo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelForm.add(txtServicioCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0; panelForm.add(new JLabel("Descripcion:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; panelForm.add(txtServicioDescripcion, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Precio Base (L.):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtServicioPrecio, gbc);

        gbc.gridx = 2; gbc.gridy = 1; panelForm.add(new JLabel("Tiempo Estimado (min):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; panelForm.add(txtServicioTiempo, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrar = new JButton("Registrar Servicio");
        JButton btnLimpiar = new JButton("Limpiar");

        btnRegistrar.addActionListener(e -> registrarServicio());
        btnLimpiar.addActionListener(e -> {
            txtServicioCodigo.setText("");
            txtServicioDescripcion.setText("");
            txtServicioPrecio.setText("");
            txtServicioTiempo.setText("");
        });

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4;
        panelForm.add(panelBotones, gbc);

        modeloTablaServicios = new DefaultTableModel(new String[]{"Codigo", "Descripcion", "Precio Base (L.)", "Tiempo (min)"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaServicios = new JTable(modeloTablaServicios);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaServicios), BorderLayout.CENTER);

        return panel;
    }

    private void registrarServicio() {
        try {
            double precio = Double.parseDouble(txtServicioPrecio.getText().trim());
            int tiempo = Integer.parseInt(txtServicioTiempo.getText().trim());
            controlador.registrarServicio(
                txtServicioCodigo.getText().trim(),
                txtServicioDescripcion.getText().trim(),
                precio,
                tiempo
            );
            JOptionPane.showMessageDialog(this, "Servicio registrado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            txtServicioCodigo.setText("");
            txtServicioDescripcion.setText("");
            txtServicioPrecio.setText("");
            txtServicioTiempo.setText("");
            actualizarTablaServicios();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y tiempo deben ser valores numericos validos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTablaServicios() {
        modeloTablaServicios.setRowCount(0);
        for (Servicio s : controlador.getRepoServicios().obtenerTodos()) {
            modeloTablaServicios.addRow(new Object[]{
                s.getCodigo(), s.getDescripcion(), String.format("%.2f", s.getPrecioBase()), s.getTiempoEstimadoMinutos()
            });
        }
    }

    private JPanel crearPanelRepuestos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Gestion de Inventario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtRepuestoCodigo = new JTextField(10);
        txtRepuestoNombre = new JTextField(18);
        txtRepuestoCantidad = new JTextField(8);
        txtRepuestoPrecio = new JTextField(8);
        txtRepuestoStockMin = new JTextField(8);

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Codigo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelForm.add(txtRepuestoCodigo, gbc);

        gbc.gridx = 2; gbc.gridy = 0; panelForm.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; panelForm.add(txtRepuestoNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Cantidad Inicial:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtRepuestoCantidad, gbc);

        gbc.gridx = 2; gbc.gridy = 1; panelForm.add(new JLabel("Precio Unitario (L.):"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; panelForm.add(txtRepuestoPrecio, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Stock Minimo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelForm.add(txtRepuestoStockMin, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRegistrar = new JButton("Registrar Repuesto");
        JButton btnEntrada = new JButton("Entrada de Stock (+)");
        JButton btnAlertas = new JButton("Ver Alertas de Stock");
        JButton btnDeshacer = new JButton("Deshacer");

        btnRegistrar.addActionListener(e -> registrarRepuesto());
        btnEntrada.addActionListener(e -> entradaStock());
        btnAlertas.addActionListener(e -> mostrarAlertasStock());
        btnDeshacer.addActionListener(e -> deshacerRepuesto());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnEntrada);
        panelBotones.add(btnAlertas);
        panelBotones.add(btnDeshacer);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panelForm.add(panelBotones, gbc);

        modeloTablaRepuestos = new DefaultTableModel(new String[]{"Codigo", "Nombre", "Existencia", "Precio Unitario (L.)", "Stock Minimo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaRepuestos = new JTable(modeloTablaRepuestos);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaRepuestos), BorderLayout.CENTER);

        return panel;
    }

    private void registrarRepuesto() {
        try {
            int cant = Integer.parseInt(txtRepuestoCantidad.getText().trim());
            double precio = Double.parseDouble(txtRepuestoPrecio.getText().trim());
            int stockMin = Integer.parseInt(txtRepuestoStockMin.getText().trim());

            controlador.registrarRepuesto(
                txtRepuestoCodigo.getText().trim(),
                txtRepuestoNombre.getText().trim(),
                cant,
                precio,
                stockMin
            );
            JOptionPane.showMessageDialog(this, "Repuesto registrado exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            txtRepuestoCodigo.setText("");
            txtRepuestoNombre.setText("");
            txtRepuestoCantidad.setText("");
            txtRepuestoPrecio.setText("");
            txtRepuestoStockMin.setText("");
            actualizarTablaRepuestos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Cantidad, precio y stock minimo deben ser numeros validos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void entradaStock() {
        int fila = tablaRepuestos.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un repuesto de la tabla para registrar entrada.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String codigo = (String) modeloTablaRepuestos.getValueAt(fila, 0);
        String entradaStr = JOptionPane.showInputDialog(this, "Ingrese la cantidad de unidades a ingresar para " + codigo + ":", "Entrada de Inventario", JOptionPane.QUESTION_MESSAGE);
        if (entradaStr != null && !entradaStr.trim().isEmpty()) {
            try {
                int unidades = Integer.parseInt(entradaStr.trim());
                controlador.registrarEntradaRepuesto(codigo, unidades);
                JOptionPane.showMessageDialog(this, "Entrada registrada exitosamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                actualizarTablaRepuestos();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un numero entero valido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarAlertasStock() {
        List<Repuesto> alertas = controlador.obtenerAlertasStock();
        if (alertas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay repuestos con stock critico. Todo el inventario esta en niveles normales.", "Stock OK", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("Repuestos con stock bajo o agotado:\n\n");
            for (Repuesto r : alertas) {
                sb.append("• ").append(r.getCodigo()).append(" - ").append(r.getNombre())
                  .append(" | Cantidad: ").append(r.getCantidad())
                  .append(" (Minimo: ").append(r.getStockMinimo()).append(")\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Alerta de Existencias", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deshacerRepuesto() {
        Repuesto deshecho = controlador.deshacerRepuesto();
        if (deshecho != null) {
            JOptionPane.showMessageDialog(this, "Se deshizo la ultima operacion sobre repuesto: " + deshecho.getNombre(), "Deshacer", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaRepuestos();
        } else {
            JOptionPane.showMessageDialog(this, "No hay operaciones de repuestos para deshacer.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTablaRepuestos() {
        modeloTablaRepuestos.setRowCount(0);
        for (Repuesto r : controlador.getRepoRepuestos().obtenerTodos()) {
            String estado = r.getCantidad() == 0 ? "AGOTADO" : (r.tieneAlertaStockBajo() ? "STOCK BAJO" : "NORMAL");
            modeloTablaRepuestos.addRow(new Object[]{
                r.getCodigo(), r.getNombre(), r.getCantidad(), String.format("%.2f", r.getPrecioUnitario()), r.getStockMinimo(), estado
            });
        }
    }

    private JPanel crearPanelOrdenes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout(5, 5));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Crear Nueva Orden de Trabajo"));

        JPanel panelDatosBasicos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(3, 3, 3, 3);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtOrdenNumero = new JTextField(controlador.generarNumeroOrden(), 10);
        cbOrdenCliente = new JComboBox<>();
        cbOrdenVehiculo = new JComboBox<>();
        txtOrdenDiagnostico = new JTextField(20);
        txtOrdenMecanico = new JTextField("Carlos Rodriguez", 15);

        cbOrdenCliente.addActionListener(e -> {
            Cliente sel = (Cliente) cbOrdenCliente.getSelectedItem();
            cbOrdenVehiculo.removeAllItems();
            if (sel != null) {
                for (Vehiculo v : controlador.listarVehiculosPorCliente(sel.getCodigo())) {
                    cbOrdenVehiculo.addItem(v);
                }
            }
        });

        gbc.gridx = 0; gbc.gridy = 0; panelDatosBasicos.add(new JLabel("No. Orden:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelDatosBasicos.add(txtOrdenNumero, gbc);

        gbc.gridx = 2; gbc.gridy = 0; panelDatosBasicos.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; panelDatosBasicos.add(cbOrdenCliente, gbc);

        gbc.gridx = 4; gbc.gridy = 0; panelDatosBasicos.add(new JLabel("Vehiculo:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; panelDatosBasicos.add(cbOrdenVehiculo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelDatosBasicos.add(new JLabel("Diagnostico:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 3; panelDatosBasicos.add(txtOrdenDiagnostico, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 4; gbc.gridy = 1; panelDatosBasicos.add(new JLabel("Mecanico:"), gbc);
        gbc.gridx = 5; gbc.gridy = 1; panelDatosBasicos.add(txtOrdenMecanico, gbc);

        JPanel panelAgregarItems = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbOrdenServiciosCatalogo = new JComboBox<>();
        JButton btnAgregarServicio = new JButton("Agregar Servicio");

        cbOrdenRepuestosCatalogo = new JComboBox<>();
        txtOrdenRepuestoCantidad = new JTextField("1", 4);
        JButton btnAgregarRepuesto = new JButton("Agregar Repuesto");

        btnAgregarServicio.addActionListener(e -> {
            Servicio sel = (Servicio) cbOrdenServiciosCatalogo.getSelectedItem();
            if (sel != null && !serviciosOrdenActual.contains(sel)) {
                serviciosOrdenActual.add(sel);
                actualizarTablaDetallesOrdenActual();
            }
        });

        btnAgregarRepuesto.addActionListener(e -> {
            Repuesto sel = (Repuesto) cbOrdenRepuestosCatalogo.getSelectedItem();
            if (sel != null) {
                try {
                    int cant = Integer.parseInt(txtOrdenRepuestoCantidad.getText().trim());
                    if (cant <= 0) {
                        JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    if (sel.getCantidad() < cant) {
                        throw new StockInsuficienteException(sel.getCodigo(), sel.getNombre(), sel.getCantidad(), cant);
                    }
                    repuestosOrdenActual.add(new DetalleRepuesto(sel, cant));
                    actualizarTablaDetallesOrdenActual();
                } catch (StockInsuficienteException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad invalida.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panelAgregarItems.add(new JLabel("Servicios:"));
        panelAgregarItems.add(cbOrdenServiciosCatalogo);
        panelAgregarItems.add(btnAgregarServicio);
        panelAgregarItems.add(new JLabel(" | Repuestos:"));
        panelAgregarItems.add(cbOrdenRepuestosCatalogo);
        panelAgregarItems.add(new JLabel("Cant:"));
        panelAgregarItems.add(txtOrdenRepuestoCantidad);
        panelAgregarItems.add(btnAgregarRepuesto);

        modeloTablaDetallesOrdenActual = new DefaultTableModel(new String[]{"Tipo", "Item", "Cantidad", "Subtotal (L.)"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaDetallesOrdenActual = new JTable(modeloTablaDetallesOrdenActual);
        tablaDetallesOrdenActual.setPreferredScrollableViewportSize(new Dimension(800, 70));

        JPanel panelAccionesOrden = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotalOrdenActual = new JLabel("Total Estimado: L. 0.00");
        lblTotalOrdenActual.setFont(new Font("SansSerif", Font.BOLD, 13));
        JButton btnCrearOrden = new JButton("Guardar Orden");
        JButton btnLimpiarOrden = new JButton("Limpiar Items");

        btnCrearOrden.addActionListener(e -> crearOrdenTrabajo());
        btnLimpiarOrden.addActionListener(e -> limpiarFormularioOrden());

        panelAccionesOrden.add(lblTotalOrdenActual);
        panelAccionesOrden.add(btnCrearOrden);
        panelAccionesOrden.add(btnLimpiarOrden);

        panelSuperior.add(panelDatosBasicos, BorderLayout.NORTH);
        panelSuperior.add(panelAgregarItems, BorderLayout.CENTER);

        JPanel panelDetalleYAccion = new JPanel(new BorderLayout());
        panelDetalleYAccion.add(new JScrollPane(tablaDetallesOrdenActual), BorderLayout.CENTER);
        panelDetalleYAccion.add(panelAccionesOrden, BorderLayout.SOUTH);
        panelSuperior.add(panelDetalleYAccion, BorderLayout.SOUTH);

        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));
        panelInferior.setBorder(BorderFactory.createTitledBorder("Ordenes Registradas y Procesamiento con Hilos"));

        modeloTablaOrdenes = new DefaultTableModel(new String[]{"No. Orden", "Fecha", "Cliente", "Vehiculo", "Mecanico", "Total (L.)", "Estado", "Facturada"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaOrdenes = new JTable(modeloTablaOrdenes);

        JPanel panelHilo = new JPanel(new BorderLayout(10, 5));
        panelHilo.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelBotonesHilo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnProcesarHilo = new JButton("Procesar con Hilo (>= 5 seg)");
        JButton btnDeshacerOrden = new JButton("Deshacer Orden");

        btnProcesarHilo.addActionListener(e -> ejecutarProcesoConHilo());
        btnDeshacerOrden.addActionListener(e -> deshacerOrden());

        panelBotonesHilo.add(btnProcesarHilo);
        panelBotonesHilo.add(btnDeshacerOrden);

        pgbOrden = new JProgressBar(0, 100);
        pgbOrden.setStringPainted(true);
        pgbOrden.setPreferredSize(new Dimension(400, 24));

        lblEstadoHilo = new JLabel("Listo para procesar ordenes.");
        lblEstadoHilo.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel panelProgreso = new JPanel(new BorderLayout(5, 2));
        panelProgreso.add(pgbOrden, BorderLayout.NORTH);
        panelProgreso.add(lblEstadoHilo, BorderLayout.CENTER);

        panelHilo.add(panelBotonesHilo, BorderLayout.WEST);
        panelHilo.add(panelProgreso, BorderLayout.CENTER);

        panelInferior.add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);
        panelInferior.add(panelHilo, BorderLayout.SOUTH);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelInferior, BorderLayout.CENTER);

        return panel;
    }

    private void actualizarCombosOrden() {
        cbOrdenCliente.removeAllItems();
        for (Cliente c : controlador.getRepoClientes().obtenerTodos()) {
            cbOrdenCliente.addItem(c);
        }

        cbOrdenServiciosCatalogo.removeAllItems();
        for (Servicio s : controlador.getRepoServicios().obtenerTodos()) {
            cbOrdenServiciosCatalogo.addItem(s);
        }

        cbOrdenRepuestosCatalogo.removeAllItems();
        for (Repuesto r : controlador.getRepoRepuestos().obtenerTodos()) {
            cbOrdenRepuestosCatalogo.addItem(r);
        }
    }

    private void actualizarTablaDetallesOrdenActual() {
        modeloTablaDetallesOrdenActual.setRowCount(0);
        double total = 0.0;
        for (Servicio s : serviciosOrdenActual) {
            modeloTablaDetallesOrdenActual.addRow(new Object[]{"Servicio", s.getDescripcion(), "1", String.format("%.2f", s.getPrecioBase())});
            total += s.getPrecioBase();
        }
        for (DetalleRepuesto d : repuestosOrdenActual) {
            modeloTablaDetallesOrdenActual.addRow(new Object[]{"Repuesto", d.getRepuesto().getNombre(), d.getCantidad(), String.format("%.2f", d.getSubtotal())});
            total += d.getSubtotal();
        }
        lblTotalOrdenActual.setText("Total Estimado: L. " + String.format("%.2f", total));
    }

    private void crearOrdenTrabajo() {
        try {
            Cliente c = (Cliente) cbOrdenCliente.getSelectedItem();
            Vehiculo v = (Vehiculo) cbOrdenVehiculo.getSelectedItem();
            if (c == null) {
                throw new Exception("Seleccione un cliente.");
            }
            if (v == null) {
                throw new Exception("Seleccione un vehiculo asociado al cliente.");
            }

            controlador.crearOrden(
                txtOrdenNumero.getText().trim(),
                "",
                c.getCodigo(),
                v.getPlaca(),
                txtOrdenDiagnostico.getText().trim(),
                txtOrdenMecanico.getText().trim(),
                new ArrayList<>(serviciosOrdenActual),
                new ArrayList<>(repuestosOrdenActual)
            );

            JOptionPane.showMessageDialog(this, "Orden de trabajo creada con exito.", "Exito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormularioOrden();
            actualizarTablaOrdenes();
        } catch (StockInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormularioOrden() {
        txtOrdenNumero.setText(controlador.generarNumeroOrden());
        txtOrdenDiagnostico.setText("");
        serviciosOrdenActual.clear();
        repuestosOrdenActual.clear();
        actualizarTablaDetallesOrdenActual();
    }

    private void ejecutarProcesoConHilo() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden de la lista para procesar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String numeroOrden = (String) modeloTablaOrdenes.getValueAt(fila, 0);

        try {
            controlador.procesarOrdenConHilo(numeroOrden, new ProcesoServicioHilo.Listener() {
                @Override
                public void onInicio(OrdenTrabajo orden) {
                    btnProcesarHilo.setEnabled(false);
                    pgbOrden.setValue(0);
                    lblEstadoHilo.setText("Iniciando procesamiento de " + orden.getNumeroOrden() + "...");
                    actualizarTablaOrdenes();
                }

                @Override
                public void onProgreso(int porcentaje, String mensaje) {
                    pgbOrden.setValue(porcentaje);
                    lblEstadoHilo.setText(mensaje);
                }

                @Override
                public void onCompletado(OrdenTrabajo orden) {
                    btnProcesarHilo.setEnabled(true);
                    pgbOrden.setValue(100);
                    lblEstadoHilo.setText("Orden " + orden.getNumeroOrden() + " finalizada con exito. Inventario descontado.");
                    actualizarTablaOrdenes();
                    actualizarTablaRepuestos();
                    actualizarComboOrdenesFinalizadas();
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Procesamiento completado para la orden " + orden.getNumeroOrden() + ".\nEstado: FINALIZADA\nExistencias descontadas del inventario.", "Proceso Finalizado", JOptionPane.INFORMATION_MESSAGE);
                }

                @Override
                public void onError(String mensaje) {
                    btnProcesarHilo.setEnabled(true);
                    lblEstadoHilo.setText("Error: " + mensaje);
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, mensaje, "Error en Proceso", JOptionPane.ERROR_MESSAGE);
                }
            });
        } catch (StockInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deshacerOrden() {
        OrdenTrabajo deshecha = controlador.deshacerOrden();
        if (deshecha != null) {
            JOptionPane.showMessageDialog(this, "Se deshizo la operacion sobre la orden: " + deshecha.getNumeroOrden(), "Deshacer", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaOrdenes();
            actualizarComboOrdenesFinalizadas();
        } else {
            JOptionPane.showMessageDialog(this, "No hay ordenes para deshacer.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTablaOrdenes() {
        modeloTablaOrdenes.setRowCount(0);
        for (OrdenTrabajo ot : controlador.getRepoOrdenes().obtenerTodos()) {
            modeloTablaOrdenes.addRow(new Object[]{
                ot.getNumeroOrden(),
                ot.getFecha(),
                ot.getCliente() != null ? ot.getCliente().getNombreCompleto() : "",
                ot.getVehiculo() != null ? ot.getVehiculo().getPlaca() : "",
                ot.getMecanico(),
                String.format("%.2f", ot.getCostoTotal()),
                ot.getEstado().getNombre(),
                ot.isFacturada() ? "SI" : "NO"
            });
        }
    }

    private JPanel crearPanelFacturacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Generacion de Factura"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtFacturaNumero = new JTextField(controlador.generarNumeroFactura(), 10);
        cbFacturaOrden = new JComboBox<>();
        txtFacturaSubtotalServicios = new JTextField(10);
        txtFacturaSubtotalServicios.setEditable(false);
        txtFacturaSubtotalRepuestos = new JTextField(10);
        txtFacturaSubtotalRepuestos.setEditable(false);
        txtFacturaImpuesto = new JTextField("15.0", 6);
        txtFacturaTotal = new JTextField(10);
        txtFacturaTotal.setEditable(false);
        txtFacturaEfectivo = new JTextField(10);
        txtFacturaCambio = new JTextField(10);
        txtFacturaCambio.setEditable(false);

        cbFacturaOrden.addActionListener(e -> recalcularMontosFactura());
        txtFacturaImpuesto.addActionListener(e -> recalcularMontosFactura());

        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("No. Factura:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelForm.add(txtFacturaNumero, gbc);

        gbc.gridx = 2; gbc.gridy = 0; panelForm.add(new JLabel("Orden Finalizada:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; panelForm.add(cbFacturaOrden, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Subtotal Servicios:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelForm.add(txtFacturaSubtotalServicios, gbc);

        gbc.gridx = 2; gbc.gridy = 1; panelForm.add(new JLabel("Subtotal Repuestos:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; panelForm.add(txtFacturaSubtotalRepuestos, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Impuesto (%):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelForm.add(txtFacturaImpuesto, gbc);

        gbc.gridx = 2; gbc.gridy = 2; panelForm.add(new JLabel("Total a Pagar:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; panelForm.add(txtFacturaTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panelForm.add(new JLabel("Efectivo Recibido:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelForm.add(txtFacturaEfectivo, gbc);

        gbc.gridx = 2; gbc.gridy = 3; panelForm.add(new JLabel("Cambio a Entregar:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; panelForm.add(txtFacturaCambio, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnFacturar = new JButton("Facturar y Pagar");
        JButton btnDeshacer = new JButton("Deshacer Factura");

        btnFacturar.addActionListener(e -> generarFactura());
        btnDeshacer.addActionListener(e -> deshacerFactura());

        panelBotones.add(btnFacturar);
        panelBotones.add(btnDeshacer);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        panelForm.add(panelBotones, gbc);

        modeloTablaFacturas = new DefaultTableModel(new String[]{"No. Factura", "Fecha", "No. Orden", "Subtotal Serv.", "Subtotal Rep.", "Impuesto", "Total (L.)", "Pagado (L.)", "Cambio (L.)"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tablaFacturas = new JTable(modeloTablaFacturas);

        panel.add(panelForm, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);

        return panel;
    }

    private void recalcularMontosFactura() {
        OrdenTrabajo ot = (OrdenTrabajo) cbFacturaOrden.getSelectedItem();
        if (ot != null) {
            double subServ = ot.calcularSubtotalServicios();
            double subRep = ot.calcularSubtotalRepuestos();
            txtFacturaSubtotalServicios.setText(String.format("%.2f", subServ));
            txtFacturaSubtotalRepuestos.setText(String.format("%.2f", subRep));
            try {
                double pct = Double.parseDouble(txtFacturaImpuesto.getText().trim());
                double base = subServ + subRep;
                double imp = base * (pct / 100.0);
                double total = base + imp;
                txtFacturaTotal.setText(String.format("%.2f", total));
            } catch (Exception ignored) {
            }
        } else {
            txtFacturaSubtotalServicios.setText("0.00");
            txtFacturaSubtotalRepuestos.setText("0.00");
            txtFacturaTotal.setText("0.00");
        }
    }

    private void generarFactura() {
        try {
            OrdenTrabajo ot = (OrdenTrabajo) cbFacturaOrden.getSelectedItem();
            if (ot == null) {
                throw new Exception("Seleccione una orden de trabajo finalizada para facturar.");
            }
            double impuesto = Double.parseDouble(txtFacturaImpuesto.getText().trim());
            double efectivo = Double.parseDouble(txtFacturaEfectivo.getText().trim());

            Factura f = controlador.generarFactura(
                txtFacturaNumero.getText().trim(),
                ot.getNumeroOrden(),
                impuesto,
                efectivo
            );

            txtFacturaCambio.setText(String.format("%.2f", f.getCambio()));
            JOptionPane.showMessageDialog(this, "Factura emitida con exito!\nTotal: L. " + String.format("%.2f", f.getTotal()) + "\nEfectivo: L. " + String.format("%.2f", f.getEfectivoRecibido()) + "\nCambio: L. " + String.format("%.2f", f.getCambio()), "Factura Creada", JOptionPane.INFORMATION_MESSAGE);

            txtFacturaNumero.setText(controlador.generarNumeroFactura());
            txtFacturaEfectivo.setText("");
            actualizarTablaFacturas();
            actualizarTablaOrdenes();
            actualizarComboOrdenesFinalizadas();
        } catch (PagoInsuficienteException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Pago Insuficiente", JOptionPane.WARNING_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Impuesto y monto en efectivo deben ser numeros validos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deshacerFactura() {
        Factura f = controlador.deshacerFactura();
        if (f != null) {
            if (f.getOrdenTrabajo() != null) {
                f.getOrdenTrabajo().setFacturada(false);
            }
            JOptionPane.showMessageDialog(this, "Se deshizo la factura: " + f.getNumeroFactura(), "Deshacer", JOptionPane.INFORMATION_MESSAGE);
            actualizarTablaFacturas();
            actualizarTablaOrdenes();
            actualizarComboOrdenesFinalizadas();
        } else {
            JOptionPane.showMessageDialog(this, "No hay facturas para deshacer.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarComboOrdenesFinalizadas() {
        cbFacturaOrden.removeAllItems();
        for (OrdenTrabajo ot : controlador.getRepoOrdenes().obtenerTodos()) {
            if (ot.getEstado() == EstadoOrden.FINALIZADA && !ot.isFacturada()) {
                cbFacturaOrden.addItem(ot);
            }
        }
        recalcularMontosFactura();
    }

    private void actualizarTablaFacturas() {
        modeloTablaFacturas.setRowCount(0);
        for (Factura f : controlador.getRepoFacturas().obtenerTodos()) {
            modeloTablaFacturas.addRow(new Object[]{
                f.getNumeroFactura(),
                f.getFecha(),
                f.getOrdenTrabajo() != null ? f.getOrdenTrabajo().getNumeroOrden() : "",
                String.format("%.2f", f.getSubtotalServicios()),
                String.format("%.2f", f.getSubtotalRepuestos()),
                String.format("%.2f", f.getMontoImpuesto()),
                String.format("%.2f", f.getTotal()),
                String.format("%.2f", f.getEfectivoRecibido()),
                String.format("%.2f", f.getCambio())
            });
        }
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelBarra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnActualizar = new JButton("Actualizar Graficos");
        btnActualizar.addActionListener(e -> actualizarReportes());
        panelBarra.add(btnActualizar);

        canvasReporte = new CanvasReporte2D();

        panel.add(panelBarra, BorderLayout.NORTH);
        panel.add(canvasReporte, BorderLayout.CENTER);

        return panel;
    }

    private void actualizarReportes() {
        if (canvasReporte != null) {
            canvasReporte.setDatos(
                controlador.obtenerIngresosPorServicio(),
                controlador.obtenerOrdenesPorEstado()
            );
        }
    }

    private void actualizarTodasLasTablas() {
        actualizarTablaClientes();
        actualizarComboPropietarios();
        actualizarTablaVehiculos();
        actualizarTablaServicios();
        actualizarTablaRepuestos();
        actualizarTablaOrdenes();
        actualizarCombosOrden();
        actualizarTablaFacturas();
        actualizarComboOrdenesFinalizadas();
        actualizarReportes();
    }
}
