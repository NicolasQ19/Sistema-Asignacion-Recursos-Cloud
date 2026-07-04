package ar.edu.unahur.obj2.cloud.operaciones;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;

public class Planificador {
    public void ejecutar(OperacionCluster operacion) throws OverprovisioningException {
        operacion.ejecutar();
    }
    public void deshacer(OperacionCluster operacion) throws OverprovisioningException {
        operacion.deshacer();
    }
}
