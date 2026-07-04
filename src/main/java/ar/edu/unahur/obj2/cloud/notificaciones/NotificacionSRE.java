package ar.edu.unahur.obj2.cloud.notificaciones;

import ar.edu.unahur.obj2.cloud.modelos.Cluster;

public class NotificacionSRE implements ObservadorCluster{
    @Override
    public void reaccionar(Cluster cluster, String tipoMovimiento, int vcpus) {
        String accion = tipoMovimiento.equals("Liberación") ? "liberado" : "asignado";
        System.out.println("Se han " + accion + " " + vcpus + " vCPUs en el clúster " + cluster.getId());
    }

}
