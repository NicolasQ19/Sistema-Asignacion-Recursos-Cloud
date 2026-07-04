package ar.edu.unahur.obj2.cloud.excepciones;

public class OverprovisioningException extends Exception{
    public OverprovisioningException(String mensaje) {
        super(mensaje);
    }
}
