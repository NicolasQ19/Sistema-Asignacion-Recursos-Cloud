package ar.edu.unahur.obj2.cloud.excepciones;

public class ValorInvalidoException extends RuntimeException{
    public ValorInvalidoException(String mensaje) {
        super(mensaje);
    }
}
