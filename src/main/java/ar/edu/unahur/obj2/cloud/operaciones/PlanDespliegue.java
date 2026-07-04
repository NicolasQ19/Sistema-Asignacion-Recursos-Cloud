package ar.edu.unahur.obj2.cloud.operaciones;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;

public class PlanDespliegue implements OperacionCluster{
    private List<OperacionCluster> operaciones = new ArrayList<>(); 

    public void agregarOperacion(OperacionCluster operacion) {
        operaciones.add(operacion);
    }

    public void vaciar() {
        operaciones.clear();
    }

    @Override
    public void ejecutar() throws OverprovisioningException {
        List<OperacionCluster> yaEjecutadas = new ArrayList<>();
        for (OperacionCluster operacion : operaciones) {
            try {
                operacion.ejecutar();
                yaEjecutadas.add(operacion);
            } catch (OverprovisioningException e) {
                revertir(yaEjecutadas);
                throw e;
            }
        }
    }

    @Override
    public void deshacer() throws OverprovisioningException {
        revertir(operaciones);
    }

    private void revertir(List<OperacionCluster> ejecutadas) throws OverprovisioningException {
        for (int i = ejecutadas.size() - 1; i >= 0; i--) {
            ejecutadas.get(i).deshacer();
        }
    }   
    
}
