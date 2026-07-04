package ar.edu.unahur.obj2.cloud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;
import ar.edu.unahur.obj2.cloud.excepciones.ValorInvalidoException;
import ar.edu.unahur.obj2.cloud.modelos.Cluster;
import ar.edu.unahur.obj2.cloud.notificaciones.AlarmaSaturacionCritica;
import ar.edu.unahur.obj2.cloud.notificaciones.CloudTrail;
import ar.edu.unahur.obj2.cloud.notificaciones.NotificacionSRE;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class NotificacionesTest {
  //TEST DE LA SEGUNDA PARTE
    private PrintStream salidaOriginal = System.out;
    private ByteArrayOutputStream capturadorDeSalida = new ByteArrayOutputStream();
    private Cluster cluster; 
    private CloudTrail cloudTrail;


  @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(capturadorDeSalida));
        cluster = new Cluster(3, 1000);
        cloudTrail = new CloudTrail();
    }

    @AfterEach
    void tearDown() {
        System.setOut(salidaOriginal);
    }
    
    @Test
    void registraYEliminaObservadores() {
        NotificacionSRE sre = new NotificacionSRE();
        cluster.registrarObservador(cloudTrail);
        cluster.registrarObservador(sre);

        assertTrue(cluster.getObservadores().contains(cloudTrail));
        assertTrue(cluster.getObservadores().contains(sre));

        cluster.eliminarObservador(sre);
        assertFalse(cluster.getObservadores().contains(sre));
    }

    @Test
    void asignacionRegistraMovimientoEnCloudTrail() throws OverprovisioningException {
        cluster.registrarObservador(cloudTrail);
        cluster.asignar(300);

        assertEquals(1, cloudTrail.getMovimientos().size());
        assertTrue(cloudTrail.getMovimientos().get(0).contains("Asignación"));
        assertTrue(cloudTrail.getMovimientos().get(0).contains("300"));
        assertTrue(cloudTrail.getMovimientos().get(0).contains("3"));
    }

    @Test
    void liberacionNotificaAlSRE() {
        cluster.registrarObservador(new NotificacionSRE());
        cluster.liberarCapacidad(50);

        String salida = capturadorDeSalida.toString();
        assertTrue(salida.contains("liberado"));
        assertTrue(salida.contains("50"));
        assertTrue(salida.contains("3"));
    }

    @Test
    void alarmaCriticaSeDisparaSiElClusterQuedaEnNegativo() throws OverprovisioningException {
        cluster.registrarObservador(new AlarmaSaturacionCritica());

        cluster.asignar(1150); // 1000 - 1150 = -150

        String salida = capturadorDeSalida.toString();
        assertTrue(salida.contains("-150"));
    }

    @Test
    void alarmaCriticaNoSeDisparaSiElClusterSigueEnPositivo() throws OverprovisioningException {
        cluster.registrarObservador(new AlarmaSaturacionCritica());
         cluster.asignar(300); // 1000 - 300 = 700

        assertEquals("", capturadorDeSalida.toString().trim());
    }

    @Test
    void operacionQueFallaPorOverprovisioningNoNotificaANadie() {
        cluster.registrarObservador(cloudTrail);
        assertThrows(OverprovisioningException.class, () -> cluster.asignar(5000));

        assertTrue(cloudTrail.getMovimientos().isEmpty());
        assertEquals("", capturadorDeSalida.toString().trim());
    }

    @Test
    void valorInvalidoNoNotificaANadie() {
        cluster.registrarObservador(cloudTrail);

        assertThrows(ValorInvalidoException.class, () -> cluster.asignar(0));

        assertTrue(cloudTrail.getMovimientos().isEmpty());
    }
}
