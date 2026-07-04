package ar.edu.unahur.obj2.cloud.notificaciones;

import ar.edu.unahur.obj2.cloud.modelos.Cluster;

public class AlarmaSaturacionCritica implements ObservadorCluster{

    @Override
    public void reaccionar(Cluster cluster, String tipoMovimiento, int vcpus) {
        if (cluster.getCapacidadActual() < 0) {
            System.out.println("EL CLUSTER " + cluster.getId()
                + " quedó en overprovisioning con capacidad " + cluster.getCapacidadActual() + " vCPUs.");
        }
        
    }
}
