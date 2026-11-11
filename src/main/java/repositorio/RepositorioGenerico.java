package repositorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class RepositorioGenerico<T> {

    private static class Operacion<T> {
        private final String tipo;
        private final String clave;
        private final T elemento;

        public Operacion(String tipo, String clave, T elemento) {
            this.tipo = tipo;
            this.clave = clave;
            this.elemento = elemento;
        }

        public String getTipo() {
            return tipo;
        }

        public String getClave() {
            return clave;
        }

        public T getElemento() {
            return elemento;
        }
    }

    private final List<T> listaElementos = new ArrayList<>();
    private final Map<String, T> mapaElementos = new HashMap<>();
    private final Stack<Operacion<T>> historialOperaciones = new Stack<>();

    public void agregar(String clave, T elemento) {
        String key = clave != null ? clave.trim().toUpperCase() : "";
        listaElementos.add(elemento);
        mapaElementos.put(key, elemento);
        historialOperaciones.push(new Operacion<>("AGREGAR", key, elemento));
    }

    public List<T> obtenerTodos() {
        return listaElementos;
    }

    public T buscarPorClave(String clave) {
        if (clave == null) {
            return null;
        }
        return mapaElementos.get(clave.trim().toUpperCase());
    }

    public boolean existe(String clave) {
        if (clave == null) {
            return false;
        }
        return mapaElementos.containsKey(clave.trim().toUpperCase());
    }

    public void eliminar(String clave) {
        if (clave == null) {
            return;
        }
        String key = clave.trim().toUpperCase();
        T elemento = mapaElementos.remove(key);
        if (elemento != null) {
            listaElementos.remove(elemento);
            historialOperaciones.push(new Operacion<>("ELIMINAR", key, elemento));
        }
    }

    public T deshacerUltimo() {
        if (!historialOperaciones.isEmpty()) {
            Operacion<T> op = historialOperaciones.pop();
            if ("AGREGAR".equals(op.getTipo())) {
                listaElementos.remove(op.getElemento());
                mapaElementos.remove(op.getClave());
                return op.getElemento();
            } else if ("ELIMINAR".equals(op.getTipo())) {
                listaElementos.add(op.getElemento());
                mapaElementos.put(op.getClave(), op.getElemento());
                return op.getElemento();
            }
        }
        return null;
    }

    public boolean puedeDeshacer() {
        return !historialOperaciones.isEmpty();
    }

    public int cantidad() {
        return listaElementos.size();
    }
}
