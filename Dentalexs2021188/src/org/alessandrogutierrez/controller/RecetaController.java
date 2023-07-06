    package org.alessandrogutierrez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alessandrogutierrez.bean.Doctor;
import org.alessandrogutierrez.bean.Receta;
import org.alessandrogutierrez.db.Conexion;
import org.alessandrogutierrez.report.GenerarReporte;
import org.alessandrogutierrez.system.Principal;

public class RecetaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, GUARDAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Receta> listaReceta;
    private ObservableList <Doctor> listaDoctor;
    private DatePicker fechaReceta;
    
    @FXML private TextField txtCodigoReceta;
    @FXML private GridPane grpFechas;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colFechaReceta;
    @FXML private TableColumn colNumeroColegiado;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;  
    @FXML private ImageView imgReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(true); 
        
        fechaReceta = new DatePicker(Locale.ENGLISH);
        fechaReceta.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaReceta.getCalendarView().todayButtonTextProperty().set("Today");
        fechaReceta.getCalendarView().setShowWeeks(false);
        grpFechas.add(fechaReceta, 3, 0);
        fechaReceta.getStylesheets().add("/org/alessandrogutierrez/resource/DatePicker.css");
    }

    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta, Date>("fechaReceta"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("numeroColegiado"));
    }
    
    public void seleccionarElemento(){
        try{
            txtCodigoReceta.setText(String.valueOf(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            fechaReceta.selectedDateProperty().set(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getFechaReceta());
            cmbNumeroColegiado.getSelectionModel().select( buscarDoctor(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
        }catch(Exception e) {
            JOptionPane.showMessageDialog(null, "¡¡Error no hay fila seleccionada!!"); 
        }
    }
    
    public Doctor buscarDoctor(int numeroColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor (registro.getInt("numeroColegiado"),
                                        registro.getString("nombresDoctor"),
                                        registro.getString("apellidosDoctor"),
                                        registro.getString("telefonoContacto"),
                                        registro.getInt("codigoEspecialidad")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return resultado;
    }
    
    public ObservableList<Receta> getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta> ();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRecetas}");
            ResultSet resultado = procedimiento.executeQuery();    
         
            while(resultado.next()){
                lista.add(new Receta (resultado.getInt("codigoReceta"),
                                      resultado.getDate("fechaReceta"),
                                      resultado.getInt("numeroColegiado") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaReceta = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDoctores}");
        ResultSet resultado = procedimiento.executeQuery();
        
            while(resultado.next()){    
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                     resultado.getString("nombresDoctor"),
                                     resultado.getString("apellidosDoctor"),
                                     resultado.getString("telefonoContacto"),
                                     resultado.getInt("codigoEspecialidad")));
            }
        
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
    } 
    
    
    public void nuevo(){
        switch(tipoDeOperacion){
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
                imgNuevo.setImage(new Image("/org/alessandrogutierrez/image/nuevo.png"));
                imgEliminar.setImage(new Image("/org/alessandrogutierrez/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;  
        }
    }
    
    public void guardar(){
        //try{
            Receta registro = new Receta();
            registro.setFechaReceta(fechaReceta.getSelectedDate());
            registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());

            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarReceta(?,?)}");
                procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime()));
                procedimiento.setInt(2, registro.getNumeroColegiado());
                procedimiento.execute();
                listaReceta.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
//        }catch(Exception e){
//            JOptionPane.showMessageDialog(null, "Error datos incompletos");
//        }
    }
    
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");  
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/alessandrogutierrez/image/nuevo.png"));
                imgEliminar.setImage(new Image("org/alessandrogutierrez/image/Eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
            
            default:
                if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Elimar Receta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                            procedimiento.setInt(1, ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                            procedimiento.execute();
                            listaReceta.remove(tblRecetas.getSelectionModel().getSelectedIndex());
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
            if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                imgEditar.setImage(new Image("/org/alessandrogutierrez/image/actualizar.png"));
                imgReporte.setImage(new Image("/org/alessandrogutierrez/image/cancelar.png"));
                activarControles(); 
                cmbNumeroColegiado.setDisable(true);
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
                imgEditar.setImage(new Image("/org/alessandrogutierrez/image/editar.png"));
                imgReporte.setImage(new Image("/org/alessandrogutierrez/image/reporte.png"));
                desactivarControles();
                limpiarControles();
                tipoDeOperacion = operaciones.NINGUNO;  
                break;
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarReceta(?,?)}");
            Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();
            registro.setFechaReceta(fechaReceta.getSelectedDate());
            
            procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime()));
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
                imgEditar.setImage(new Image("/org/alessandrogutierrez/image/editar.png"));
                imgReporte.setImage(new Image("/org/alessandrogutierrez/image/reporte.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
                
            case NINGUNO:
                imprimirReporte();
                limpiarControles();
                break;
        }
    }
    
    public void imprimirReporte(){
        try{
        Map parametros = new HashMap();
        int codReceta = ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta(); 
        parametros.put("RUTA_IMAGE",PacienteController.class.getResource("/org/alessandrogutierrez/image/fondoReporte1.png"));
        parametros.put("RUTA_SUB", RecetaController.class.getResource("/org/alessandrogutierrez/report/ReporteDetalleReceta.jasper"));
        parametros.put("FIRMA", RecetaController.class.getResource("/org/alessandrogutierrez/image/firma.png"));
        parametros.put("codReceta", codReceta);
        GenerarReporte.mostrarReporte("ReporteReceta.jasper", "Reporte de la Receta", parametros);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null ,"Debes Seleccionar Un Elemento");
        }
    }    
    
    
    
    
    
    
    public void activarControles(){
        cmbNumeroColegiado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoReceta.clear();
        cmbNumeroColegiado.getSelectionModel().select(null);
        fechaReceta.setSelectedDate(null);
        tblRecetas.getSelectionModel().clearSelection();
    }
    
    public void desactivarControles(){
        txtCodigoReceta.setEditable(false);
        cmbNumeroColegiado.setDisable(true);
        
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
   
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }   

}
