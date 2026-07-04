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
    void capacidadInicial(){
        assertEquals(1000, cluster.getCapacidadActual());
    }

    @Test
    void asignarReduceCapacidad() throws OverprovisioningException {
            cluster.asignar(300);
            assertEquals(700, cluster.getCapacidadActual());
        }

    @Test
    void liberarAumentaCapacidad() {
        cluster.liberarCapacidad(300);
        assertEquals(1300, cluster.getCapacidadActual());
    }

    @Test
    void valorMenorOIgualACeroLanzaExcepcion(){
        assertThrows(ValorInvalidoException.class, () -> cluster.asignar(0));
        assertThrows(ValorInvalidoException.class, () -> cluster.asignar(-50));
    }

    @Test
    void liberarConValorMenorOIgualACeroLanzaExcepcion(){
        assertThrows(ValorInvalidoException.class, () -> cluster.liberarCapacidad(0));
        assertThrows(ValorInvalidoException.class, () -> cluster.liberarCapacidad(-50));
    }

    @Test
    void getIdDevuelveElIdDelCluster(){
        assertEquals(1, cluster.getId());
    }

    @Test
    void asignarHastaElLimiteExactoFunciona() throws OverprovisioningException {
        cluster.asignar(1200);
        assertEquals(-200, cluster.getCapacidadActual());
    }

    @Test
    void superarElLimiteLanzaExcepcionYNoModificaEstado() {
        assertThrows(OverprovisioningException.class, () -> cluster.asignar(1201));
        assertEquals(1000, cluster.getCapacidadActual(), "El cluster no debe modificarse si la operación falla");
    }
}
