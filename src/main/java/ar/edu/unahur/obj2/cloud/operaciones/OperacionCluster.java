package ar.edu.unahur.obj2.cloud.operaciones;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;

public interface OperacionCluster {
    void ejecutar() throws OverprovisioningException;
    void deshacer() throws OverprovisioningException;
}
