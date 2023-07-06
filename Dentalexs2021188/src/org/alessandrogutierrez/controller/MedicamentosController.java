package org.alessandrogutierrez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.alessandrogutierrez.bean.Medicamento;
import org.alessandrogutierrez.db.Conexion;
import org.alessandrogutierrez.report.GenerarReporte;
import org.alessandrogutierrez.system.Principal;

public class MedicamentosController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList <Medicamento> listaMedicamento;
    private enum operaciones{NUEVO, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, GUARDAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
        
    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtNombreMedicamento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colNombreMedicamento;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
    public void cargarDatos (){
        tblMedicamentos.setItems(getMedicamento());
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory <Medicamento,Integer> ("codigoMedicamento"));
        colNombreMedicamento.setCellValueFactory(new PropertyValueFactory <Medicamento,String>("nombreMedicamento") );
        
    }
    
    public void seleccionarElemento(){
        
        try {
        txtCodigoMedicamento.setText(String.valueOf( ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento() ));
        txtNombreMedicamento.setText(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getNombreMedicamento());  
        
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "¡¡Error no hay fila seleccionada!!");
        }
        
    }
    
    
    public ObservableList<Medicamento> getMedicamento (){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento> ();
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos}");
            ResultSet resultado = procedimiento.executeQuery(); // executeQuery Lo hace y trae retorno -- Execute solo lo hace sin retorno, para editar y elimar se usa
            
            while (resultado.next()){
                lista.add(new Medicamento ( resultado.getInt("codigoMedicamento"),
                                            resultado.getString("nombreMedicamento")
                ));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }
    
    
    public void nuevo(){
        switch (tipoDeOperacion){
            case NINGUNO:
                activarControles(); 
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/alessandrogutierrez/image/Guardar.png"));
                imgEliminar.setImage(new Image("/org/alessandrogutierrez/image/cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/alessandrogutierrez/image/nuevoMed.png"));
                imgEliminar.setImage(new Image("/org/alessandrogutierrez/image/eliminarMed.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();                
                break;
        }
    }
    
    public void guardar(){
        if(txtNombreMedicamento.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Datos Incompletos");
        }else{
            Medicamento registro = new Medicamento ();
            registro.setNombreMedicamento(txtNombreMedicamento.getText());
            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamento(?)}");
                procedimiento.setString(1, registro.getNombreMedicamento());
                procedimiento.execute();
                listaMedicamento.add(registro);

            }catch(Exception e){
                e.printStackTrace();
            }  
        }
    }
    
    public void eliminar(){
        switch (tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");  
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/alessandrogutierrez/image/nuevoMed.png"));
                imgEliminar.setImage(new Image("org/alessandrogutierrez/image/eliminarMed.png"));
                tipoDeOperacion = operaciones.NINGUNO;  
                cargarDatos();
                break;
            
            default:
                if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Elimar Medicamento", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  
                  if (respuesta == JOptionPane.YES_OPTION) {
                     try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");  
                        procedimiento.setInt(1, ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                        procedimiento.execute();
                        listaMedicamento.remove(tblMedicamentos.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                        
                     } catch (Exception e){
                         e.printStackTrace();
                     }
                  }
                  if(respuesta == JOptionPane.NO_OPTION){
                      limpiarControles();
                  }
                }else
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if (tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/alessandrogutierrez/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/alessandrogutierrez/image/cancelar.png"));
                    activarControles();                    
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }else 
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                
                break;
            
            case ACTUALIZAR:
                actualizar();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/alessandrogutierrez/image/editarMed.png"));
                imgReporte.setImage(new Image("/org/alessandrogutierrez/image/reporte.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;               
                break;
        }
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamento(?, ?)}");
            Medicamento registro = (Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem();
            registro.setNombreMedicamento(txtNombreMedicamento.getText());
            
            procedimiento.setInt(1, registro.getCodigoMedicamento());
            procedimiento.setString(2, registro.getNombreMedicamento());
            procedimiento.execute();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void reporte(){
        switch(tipoDeOperacion){
            case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/alessandrogutierrez/image/editarMed.png"));
                imgReporte.setImage(new Image("/org/alessandrogutierrez/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
                
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
                break;            
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("RUTA_IMAGE",MedicamentosController.class.getResource("/org/alessandrogutierrez/image/fondoReporte1.png"));
        parametros.put("codigoMedicamento", null);
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte de Medicamentos", parametros);
    }
        
    
    public void desactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtNombreMedicamento.setEditable(false);
    }
    public void activarControles(){
        //txtCodigoMedicamento.setEditable(true);
        txtNombreMedicamento.setEditable(true);
    }
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtNombreMedicamento.clear();
        tblMedicamentos.getSelectionModel().clearSelection();
    }   
    
    public Principal getEscenarioPrincipal (){
        return escenarioPrincipal;
    }
    
    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
    
}
