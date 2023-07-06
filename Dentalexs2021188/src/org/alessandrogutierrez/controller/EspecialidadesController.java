
package org.alessandrogutierrez.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import org.alessandrogutierrez.bean.Especialidad;
import org.alessandrogutierrez.db.Conexion;
import org.alessandrogutierrez.system.Principal;

public class EspecialidadesController implements Initializable {
    private Principal escenarioPrincipal;
    private ObservableList <Especialidad> listaEspecialidad;
    private enum operaciones{NUEVO, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, GUARDAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
            
    
    @FXML private TextField txtCodigoEspecialidad;
    @FXML private TextField txtDescripcionEspecialidad;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblEspecialidades;
    @FXML private TableColumn colCodigoEspecialidad;
    @FXML private TableColumn colDescripcionEspecialidad;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
    
   
    public void cargarDatos(){
        tblEspecialidades.setItems(getEspecialidad());
        colCodigoEspecialidad.setCellValueFactory(new PropertyValueFactory <Especialidad, Integer> ("codigoEspecialidad"));
        colDescripcionEspecialidad.setCellValueFactory(new PropertyValueFactory <Especialidad, String>("descripcionEspecialidad"));
    }
                    
    public void seleccionarElemento (){
        try {
           txtCodigoEspecialidad.setText(String.valueOf( ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad() ));
           txtDescripcionEspecialidad.setText(((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getDescripcionEspecialidad()); 
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "¡¡Error no hay fila seleccionada!!");
        }
    }
    
    public ObservableList<Especialidad> getEspecialidad (){ // Trae los datos de MySQL
        ArrayList<Especialidad> lista = new ArrayList<> ();
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEspecialidades}");
            ResultSet resultado = procedimiento.executeQuery();
            
            while (resultado.next()){
                lista.add(new Especialidad ( resultado.getInt("codigoEspecialidad"), 
                                             resultado.getString("descripcion")                                            
                ));
            }  
        }catch(Exception e){
            e.printStackTrace();
        }

        return listaEspecialidad = FXCollections.observableArrayList(lista);
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
                cargarDatos();
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
        if(txtDescripcionEspecialidad.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Datos Incompletos");
        }else{
            Especialidad registro = new Especialidad ();
            registro.setDescripcionEspecialidad(txtDescripcionEspecialidad.getText());
            try {
                 PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEspecialidad(?)}");
                 procedimiento.setString(1, registro.getDescripcionEspecialidad());
                 procedimiento.execute();
                 listaEspecialidad.add(registro);
            }catch (Exception e){
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
            if (tblEspecialidades.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Elimar Especialidad", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
                if (respuesta == JOptionPane.YES_OPTION){
                    try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEspecialidad(?)}"); 
                        procedimiento.setInt(1, ((Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem()).getCodigoEspecialidad());
                        procedimiento.execute();
                        listaEspecialidad.remove(tblEspecialidades.getSelectionModel().getSelectedIndex());
                        limpiarControles();
                    }catch(Exception e){
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
                if (tblEspecialidades.getSelectionModel().getSelectedItem() != null){
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarEspecialidad(?, ?)}");
            Especialidad registro = (Especialidad)tblEspecialidades.getSelectionModel().getSelectedItem();
            registro.setDescripcionEspecialidad(txtDescripcionEspecialidad.getText());
            
            procedimiento.setInt(1, registro.getCodigoEspecialidad());
            procedimiento.setString(2, registro.getDescripcionEspecialidad());
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
        }
    }
    
    public void desactivarControles (){
        txtCodigoEspecialidad.setEditable(false);
        txtDescripcionEspecialidad.setEditable(false);
    }
    public void activarControles (){
        //txtCodigoEspecialidad.setEditable(true);
        txtDescripcionEspecialidad.setEditable(true);
    }
    public void limpiarControles (){
        txtCodigoEspecialidad.clear();
        txtDescripcionEspecialidad.clear();
        tblEspecialidades.getSelectionModel().clearSelection();
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
