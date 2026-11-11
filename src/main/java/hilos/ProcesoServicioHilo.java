package hilos;

import javax.swing.SwingUtilities;
import modelo.EstadoOrden;
import modelo.OrdenTrabajo;

public class ProcesoServicioHilo implements Runnable {

    public interface Listener {
        void onInicio(OrdenTrabajo orden);
        void onProgreso(int porcentaje, String mensaje);
        void onCompletado(OrdenTrabajo orden);
        void onError(String mensaje);
    }

    private final OrdenTrabajo orden;
    private final Listener listener;
    private final Runnable accionFinalizar;

    public ProcesoServicioHilo(OrdenTrabajo orden, Listener listener, Runnable accionFinalizar) {
        this.orden = orden;
        this.listener = listener;
        this.accionFinalizar = accionFinalizar;
    }

    @Override
    public void run() {
        if (orden.isEnProceso()) {
            if (listener != null) {
                SwingUtilities.invokeLater(() -> listener.onError("La orden " + orden.getNumeroOrden() + " ya se encuentra en procesamiento."));
            }
            return;
        }

        if (orden.getEstado() == EstadoOrden.FINALIZADA) {
            if (listener != null) {
                SwingUtilities.invokeLater(() -> listener.onError("La orden " + orden.getNumeroOrden() + " ya está finalizada."));
            }
            return;
        }

        if (orden.getEstado() == EstadoOrden.CANCELADA) {
            if (listener != null) {
                SwingUtilities.invokeLater(() -> listener.onError("No se puede procesar una orden cancelada."));
            }
            return;
        }

        orden.setEnProceso(true);
        orden.setEstado(EstadoOrden.EN_PROCESO);

        if (listener != null) {
            SwingUtilities.invokeLater(() -> listener.onInicio(orden));
        }

        String[] pasos = {
            "Inspeccion inicial del vehiculo",
            "Diagnostico electronico y verificacion",
            "Desmontaje de piezas",
            "Preparacion de repuestos solicitados",
            "Instalacion y ajuste de repuestos",
            "Ejecucion de servicios tecnicos",
            "Calibracion y pruebas de motor",
            "Revision de frenos y niveles",
            "Lavado de bahia y control de calidad",
            "Finalizacion del servicio"
        };

        try {
            int totalPasos = pasos.length;
            for (int i = 0; i < totalPasos; i++) {
                Thread.sleep(550);
                int porcentaje = ((i + 1) * 100) / totalPasos;
                String detalle = "Paso " + (i + 1) + "/" + totalPasos + ": " + pasos[i];
                if (listener != null) {
                    final int p = porcentaje;
                    SwingUtilities.invokeLater(() -> listener.onProgreso(p, detalle));
                }
            }

            orden.setEnProceso(false);

            SwingUtilities.invokeLater(() -> {
                if (accionFinalizar != null) {
                    accionFinalizar.run();
                }
                if (listener != null) {
                    listener.onCompletado(orden);
                }
            });

        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            orden.setEnProceso(false);
            if (listener != null) {
                SwingUtilities.invokeLater(() -> listener.onError("Proceso interrumpido: " + ex.getMessage()));
            }
        }
    }
}
