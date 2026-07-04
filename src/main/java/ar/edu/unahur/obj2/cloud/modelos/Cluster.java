package ar.edu.unahur.obj2.cloud.modelos;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;
import ar.edu.unahur.obj2.cloud.excepciones.ValorInvalidoException;
import ar.edu.unahur.obj2.cloud.notificaciones.ObservadorCluster;

public class Cluster {
    private Integer id; 
    private int capacidadActual;
    private List<ObservadorCluster> observadores = new ArrayList<>();

    public Cluster(Integer id, int capacidadActual) {
        this.id = id;
        this.capacidadActual = capacidadActual;
    }

    public Integer getId() {
        return id;
    }

    public int getCapacidadActual() {
        return capacidadActual;
    } 

    public void asignar(int vcpus) throws OverprovisioningException{
        validarValorPositivo(vcpus);
        if(this.capacidadActual - vcpus < -200){
            throw new OverprovisioningException("no se puede asignar" + vcpus);
        }
        this.capacidadActual -= vcpus;
        notificarObservadores("Asignación", vcpus);
    }

    public void liberarCapacidad(int vcpus){
        validarValorPositivo(vcpus);
        this.capacidadActual += vcpus;
        notificarObservadores("Liberación", vcpus);
    }

    private void validarValorPositivo(int vcpus){
        if(vcpus <= 0){
            throw new ValorInvalidoException("elv valor del vcpus debe ser mayor a cero");
        }
    }

    public void registrarObservador(ObservadorCluster observador) {
        this.observadores.add(observador);
    }

    public void eliminarObservador(ObservadorCluster observador) {
        this.observadores.remove(observador);
    }

    public List<ObservadorCluster> getObservadores() {
        return observadores;
    }

    private void notificarObservadores(String tipoMovimiento, int vcpus) {
        observadores.forEach(o -> o.reaccionar(this, tipoMovimiento, vcpus));
    }
}
