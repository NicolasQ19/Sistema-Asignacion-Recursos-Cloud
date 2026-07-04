package ar.edu.unahur.obj2.cloud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void ejecutaOperacionIndividual() throws OverprovisioningException {
        planificador.ejecutar(new Asignacion(cluster, 300));
        assertEquals(700, cluster.getCapacidadActual());
    }

    @Test
    void deshaceOperacionIndividual() throws OverprovisioningException {
        Asignacion asignacion = new Asignacion(cluster, 300);
        planificador.ejecutar(asignacion);
        planificador.deshacer(asignacion);
        assertEquals(1000, cluster.getCapacidadActual());
    }

    @Test
    void ejecutaPlanComoUnaSolaOperacion() throws OverprovisioningException {
        PlanDespliegue plan = new PlanDespliegue();
        plan.agregarOperacion(new Asignacion(cluster, 300));
        plan.agregarOperacion(new Liberacion(cluster, 100));
        plan.agregarOperacion(new Asignacion(cluster, 200));

        planificador.ejecutar(plan);

        assertEquals(600, cluster.getCapacidadActual());
    }

    @Test
    void vaciarPlanLoDejaSinOperaciones() throws OverprovisioningException {
        PlanDespliegue plan = new PlanDespliegue();
        plan.agregarOperacion(new Asignacion(cluster, 300));
        plan.vaciar();

        planificador.ejecutar(plan);

        assertEquals(1000, cluster.getCapacidadActual());
    }

    @Test
    void deshacePlanCompletoEnOrdenInverso() throws OverprovisioningException {
        PlanDespliegue plan = new PlanDespliegue();
        plan.agregarOperacion(new Asignacion(cluster, 300));
        plan.agregarOperacion(new Liberacion(cluster, 100));

        planificador.ejecutar(plan);
        planificador.deshacer(plan);

        assertEquals(1000, cluster.getCapacidadActual());
    }

    @Test
    void planQueFallaAMitadDeCaminoRevierteLoYaEjecutadoYPropagaElError() {
        PlanDespliegue plan = new PlanDespliegue();
        plan.agregarOperacion(new Asignacion(cluster, 300));   // se ejecuta OK: 1000 -> 700
        plan.agregarOperacion(new Liberacion(cluster, 50));    // se ejecuta OK: 700 -> 750
        plan.agregarOperacion(new Asignacion(cluster, 5000));  // falla: 750 - 5000 < -200

        assertThrows(OverprovisioningException.class, () -> planificador.ejecutar(plan));

        assertEquals(1000, cluster.getCapacidadActual(), "Las operaciones ya ejecutadas del plan deben revertirse");
    }
}
