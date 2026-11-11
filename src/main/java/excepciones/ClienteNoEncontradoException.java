package excepciones;

public class ClienteNoEncontradoException extends Exception {

    private final String codigo;

    public ClienteNoEncontradoException(String codigo) {
        super("No se encontro ningun cliente con el codigo: " + codigo);
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
