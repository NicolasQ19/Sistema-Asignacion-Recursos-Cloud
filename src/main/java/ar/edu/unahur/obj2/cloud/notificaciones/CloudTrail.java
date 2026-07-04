package ar.edu.unahur.obj2.cloud.notificaciones;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unahur.obj2.cloud.modelos.Cluster;

public class CloudTrail implements ObservadorCluster{
    private List<String> movimientos = new ArrayList<>();

    @Override
    public void reaccionar(Cluster cluster, String tipoMovimiento, int vcpus) {
        String movimiento = "Movimiento: " + tipoMovimiento + " de " + vcpus + " vCPUs en clúster " + cluster.getId();
        System.out.println(movimiento);
        movimientos.add(movimiento);
    }

    
    public List<String> getMovimientos() {
        return movimientos;
    }
}
