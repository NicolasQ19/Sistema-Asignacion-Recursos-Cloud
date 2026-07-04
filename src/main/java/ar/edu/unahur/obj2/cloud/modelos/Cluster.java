package ar.edu.unahur.obj2.cloud.modelos;

import ar.edu.unahur.obj2.cloud.excepciones.OverprovisioningException;
import ar.edu.unahur.obj2.cloud.excepciones.ValorInvalidoException;

public class Cluster {
    private Integer id; 
    private int capacidadActual;

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
    }

    public void liberarCapacidad(int vcpus){
        validarValorPositivo(vcpus); 
        this.capacidadActual += vcpus;
    }

    private void validarValorPositivo(int vcpus){
        if(vcpus <= 0){
            throw new ValorInvalidoException("elv valor del vcpus debe ser mayor a cero");
        }
    }
}
