package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;

public class CanvasReporte2D extends JPanel {

    private Map<String, Double> ingresosPorServicio = new LinkedHashMap<>();
    private Map<String, Integer> ordenesPorEstado = new LinkedHashMap<>();

    private final Color[] coloresServicios = {
        new Color(41, 128, 185),
        new Color(39, 174, 96),
        new Color(243, 156, 18),
        new Color(142, 68, 173),
        new Color(231, 76, 60),
        new Color(22, 160, 133)
    };

    private final Color[] coloresEstados = {
        new Color(241, 196, 15),
        new Color(52, 152, 219),
        new Color(46, 204, 113),
        new Color(231, 76, 60)
    };

    public CanvasReporte2D() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(850, 420));
    }

    public void setDatos(Map<String, Double> ingresos, Map<String, Integer> estados) {
        this.ingresosPorServicio = ingresos != null ? ingresos : new LinkedHashMap<>();
        this.ordenesPorEstado = estados != null ? estados : new LinkedHashMap<>();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int anchoTotal = getWidth();
        int altoTotal = getHeight();
        int mitadAncho = anchoTotal / 2;

        dibujarGraficoIngresos(g2, 20, 20, mitadAncho - 40, altoTotal - 40);
        dibujarGraficoEstados(g2, mitadAncho + 20, 20, mitadAncho - 40, altoTotal - 40);
    }

    private void dibujarGraficoIngresos(Graphics2D g2, int x, int y, int ancho, int alto) {
        g2.setColor(new Color(245, 247, 250));
        g2.fillRect(x, y, ancho, alto);
        g2.setColor(new Color(200, 205, 210));
        g2.drawRect(x, y, ancho, alto);

        g2.setColor(new Color(44, 62, 80));
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Ingresos por Servicio (L.)", x + 15, y + 25);

        if (ingresosPorServicio.isEmpty()) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("No hay datos de servicios", x + 20, y + 60);
            return;
        }

        double maxIngreso = 100.0;
        for (Double val : ingresosPorServicio.values()) {
            if (val > maxIngreso) {
                maxIngreso = val;
            }
        }

        int baseGraficoY = y + alto - 60;
        int alturaMaximaBarra = alto - 120;
        int cantidadBarras = ingresosPorServicio.size();
        int anchoBarra = Math.max(25, (ancho - 60) / (cantidadBarras * 2));
        int pasoX = (ancho - 60) / cantidadBarras;
        int barraX = x + 40;

        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(x + 30, baseGraficoY, x + ancho - 20, baseGraficoY);

        int indexColor = 0;
        for (Map.Entry<String, Double> entry : ingresosPorServicio.entrySet()) {
            double valor = entry.getValue();
            int altoBarra = (int) ((valor / maxIngreso) * alturaMaximaBarra);
            int barraY = baseGraficoY - altoBarra;

            g2.setColor(coloresServicios[indexColor % coloresServicios.length]);
            g2.fillRect(barraX, barraY, anchoBarra, altoBarra);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(barraX, barraY, anchoBarra, altoBarra);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.setColor(Color.BLACK);
            String textoValor = "L." + String.format("%.0f", valor);
            g2.drawString(textoValor, barraX, barraY - 5);

            String nombreCorto = entry.getKey();
            if (nombreCorto.length() > 9) {
                nombreCorto = nombreCorto.substring(0, 7) + "..";
            }
            g2.drawString(nombreCorto, barraX, baseGraficoY + 15);

            barraX += pasoX;
            indexColor++;
        }
    }

    private void dibujarGraficoEstados(Graphics2D g2, int x, int y, int ancho, int alto) {
        g2.setColor(new Color(245, 247, 250));
        g2.fillRect(x, y, ancho, alto);
        g2.setColor(new Color(200, 205, 210));
        g2.drawRect(x, y, ancho, alto);

        g2.setColor(new Color(44, 62, 80));
        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.drawString("Cantidad de Ordenes por Estado", x + 15, y + 25);

        if (ordenesPorEstado.isEmpty()) {
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("No hay ordenes registradas", x + 20, y + 60);
            return;
        }

        int maxOrdenes = 5;
        for (Integer cant : ordenesPorEstado.values()) {
            if (cant > maxOrdenes) {
                maxOrdenes = cant + 1;
            }
        }

        int baseGraficoY = y + alto - 60;
        int alturaMaximaBarra = alto - 120;
        int cantidadBarras = ordenesPorEstado.size();
        int anchoBarra = Math.max(35, (ancho - 60) / (cantidadBarras * 2));
        int pasoX = (ancho - 60) / cantidadBarras;
        int barraX = x + 40;

        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(x + 30, baseGraficoY, x + ancho - 20, baseGraficoY);

        int indexColor = 0;
        for (Map.Entry<String, Integer> entry : ordenesPorEstado.entrySet()) {
            int valor = entry.getValue();
            int altoBarra = (int) (((double) valor / maxOrdenes) * alturaMaximaBarra);
            int barraY = baseGraficoY - altoBarra;

            g2.setColor(coloresEstados[indexColor % coloresEstados.length]);
            g2.fillRect(barraX, barraY, anchoBarra, altoBarra);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(barraX, barraY, anchoBarra, altoBarra);

            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(valor), barraX + (anchoBarra / 3), barraY - 5);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.drawString(entry.getKey(), barraX - 5, baseGraficoY + 15);

            barraX += pasoX;
            indexColor++;
        }
    }
}
