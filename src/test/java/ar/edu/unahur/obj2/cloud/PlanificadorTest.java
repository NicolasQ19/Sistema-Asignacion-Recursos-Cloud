package ar.edu.unahur.obj2.cloud;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;
import ar.edu.unahur.obj2.cloud.modelos.Cluster;
import ar.edu.unahur.obj2.cloud.operaciones.Asignacion;
import ar.edu.unahur.obj2.cloud.operaciones.Liberacion;
import ar.edu.unahur.obj2.cloud.operaciones.PlanDespliegue;
import ar.edu.unahur.obj2.cloud.operaciones.Planificador;

public class PlanificadorTest {
    private Cluster cluster;
    private Planificador planificador;

    @BeforeEach
    void setUp() {
        cluster = new Cluster(2, 1000);
        planificador = new Planificador();
    }

    @Test
    void ejecutarOperacion() throws OverprovisioningException {
        planificador.ejecutar(new Asignacion(cluster, 300));
        assertEquals(700, cluster.getCapacidadActual());
    }

    @Test
    void DeshacerOperacion() throws OverprovisioningException {
        Asignacion asignacion = new Asignacion(cluster, 300);
        planificador.ejecutar(asignacion);
        planificador.deshacer(asignacion);
        assertEquals(1000, cluster.getCapacidadActual());
    }

    void ejecucionEnOrdenDelPlanDespliegueComoUnicaOperacionCluster() throws OverprovisioningException {
        PlanDespliegue plan = new PlanDespliegue();
        plan.agregarOperacion(new Asignacion(cluster, 300));
        plan.agregarOperacion(new Liberacion(cluster, 100));
        plan.agregarOperacion(new Asignacion(cluster, 200));

       
        planificador.ejecutar(plan);

        assertEquals(600, cluster.getCapacidadActual());
    } 

    void dadoUnPlanQueVaciaSusOperacionesPendientesQuedaSinOperaciones() throws OverprovisioningException {
        PlanDespliegue plan = new PlanDespliegue();
        plan.agregarOperacion(new Asignacion(cluster, 300));
        plan.vaciar();

        planificador.ejecutar(plan);

        // Al no quedar operaciones registradas, ejecutar el plan no altera el clúster
        assertEquals(1000, cluster.getCapacidadActual());
    }
}
