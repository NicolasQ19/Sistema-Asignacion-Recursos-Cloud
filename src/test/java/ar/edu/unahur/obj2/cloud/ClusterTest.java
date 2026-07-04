package ar.edu.unahur.obj2.cloud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;
import ar.edu.unahur.obj2.cloud.excepciones.ValorInvalidoException;
import ar.edu.unahur.obj2.cloud.modelos.Cluster;

public class ClusterTest {
 private Cluster cluster; 


    @BeforeEach
    void setUp(){
        cluster = new Cluster(1, 1000);
    }

    @Test
    void consultarCapacidadCluster(){
        assertEquals(1000, cluster.getCapacidadActual());
    }

    @Test
    void seAsignaUnRecursoYReduceLaCapacidadDisponible() throws OverprovisioningException {
            cluster.asignar(300);
            assertEquals(700, cluster.getCapacidadActual());
        }

    @Test
    void incrementoDeCapacidadTrasLiberacion() {
        cluster.liberarCapacidad(300);
        assertEquals(1300, cluster.getCapacidadActual());
    }

    @Test
    void lanzamientoDeExceptionConAsignacionMenorOigualAcero(){
        assertThrows(ValorInvalidoException.class, () -> cluster.asignar(0));
        assertThrows(ValorInvalidoException.class, () -> cluster.asignar(-50));
    }
}
