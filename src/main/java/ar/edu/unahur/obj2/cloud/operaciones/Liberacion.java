package ar.edu.unahur.obj2.cloud.operaciones;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;
import ar.edu.unahur.obj2.cloud.excepciones.ValorInvalidoException;
import ar.edu.unahur.obj2.cloud.modelos.Cluster;

public class Liberacion implements OperacionCluster {
    private Cluster cluster;
    private int vcpus;
    public Liberacion(Cluster cluster, int vcpus) {
        if (vcpus <= 0) {
            throw new ValorInvalidoException("El valor de vCPUs debe ser mayor a cero");
        }
        this.cluster = cluster;
        this.vcpus = vcpus;
    }
    @Override
    public void deshacer() throws OverprovisioningException {
        cluster.liberarCapacidad(vcpus);
    }
    
    @Override
    public void ejecutar() throws OverprovisioningException {
        cluster.asignar(vcpus);  
    } 
}
