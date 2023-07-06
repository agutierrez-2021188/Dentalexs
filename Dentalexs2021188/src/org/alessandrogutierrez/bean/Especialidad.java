package org.alessandrogutierrez.bean;

public class Especialidad {
    private int codigoEspecialidad;
    private String descripcionEspecialidad;

    public Especialidad() {
    }

    public Especialidad(int codigoEspecialidad, String descripcionEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
        this.descripcionEspecialidad = descripcionEspecialidad;
    }

    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }

    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }

    public String getDescripcionEspecialidad() {
        return descripcionEspecialidad;
    }

    public void setDescripcionEspecialidad(String descripcionEspecialidad) {
        this.descripcionEspecialidad = descripcionEspecialidad;
    }

    @Override
    public String toString() {
        return getCodigoEspecialidad() + " | " + getDescripcionEspecialidad();
    }
    
    
}
