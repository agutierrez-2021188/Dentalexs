
package org.alessandrogutierrez.bean;

public class DetalleReceta {
    private int codigoDReceta;
    private String dosis;
    private int codigoReceta;
    private int codigoMedicamento;

    public DetalleReceta() {
    }

    public DetalleReceta(int codigoDReceta, String dosis, int codigoReceta, int codigoMedicamento) {
        this.codigoDReceta = codigoDReceta;
        this.dosis = dosis;
        this.codigoReceta = codigoReceta;
        this.codigoMedicamento = codigoMedicamento;
    }

    public int getCodigoDReceta() {
        return codigoDReceta;
    }

    public void setCodigoDReceta(int codigoDReceta) {
        this.codigoDReceta = codigoDReceta;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public int getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(int codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public int getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(int codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }
    
    
}
