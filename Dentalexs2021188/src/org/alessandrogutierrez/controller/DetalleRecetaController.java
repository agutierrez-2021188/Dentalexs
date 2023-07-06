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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.alessandrogutierrez.bean.DetalleReceta;
import org.alessandrogutierrez.bean.Medicamento;
import org.alessandrogutierrez.bean.Receta;
import org.alessandrogutierrez.db.Conexion;
import org.alessandrogutierrez.system.Principal;

public class DetalleRecetaController implements Initializable{
  
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, GUARDAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <DetalleReceta> listaDReceta;
    private ObservableList <Receta> listaReceta;
    private ObservableList <Medicamento> listaMedicamento;
    
    
    @FXML private TextField txtCodigoDReceta;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbCodigoReceta;
    @FXML private ComboBox cmbCodigoMedicamento;
    @FXML private TableView tblDetalleRecetas;
    @FXML private TableColumn colCodigoDReceta;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colCodigoMedicamento;
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
        cmbCodigoReceta.setItems(getReceta());
        cmbCodigoReceta.setDisable(true); 
        
        cmbCodigoMedicamento.setItems(getMedicamento());
        cmbCodigoMedicamento.setDisable(true);
    }
    
    
    public void cargarDatos(){
        tblDetalleRecetas.setItems(getDReceta());
        colCodigoDReceta.setCellValueFactory(new PropertyValueFactory <DetalleReceta, Integer>("codigoDReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory <DetalleReceta, String>("dosis"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoReceta"));
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory <DetalleReceta, Integer>("codigoMedicamento"));
    }
    
    public void seleccionarElemento(){
        try{
            txtCodigoDReceta.setText(String.valueOf(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDReceta()));
            txtDosis.setText(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getDosis());
            cmbCodigoReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
            cmbCodigoMedicamento.getSelectionModel().select(buscarMedicamento(((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "¡¡Error no hay fila seleccionada!!");
        }
    }
    
    public Receta buscarReceta(int codigoReceta){
        Receta resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarReceta(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Receta (registro.getInt("codigoReceta"),
                                        registro.getDate("fechaReceta"),
                                        registro.getInt("numeroColegiado")
                
                
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }    
    
    public Medicamento buscarMedicamento(int codigoMedicamento){
        Medicamento resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medicamento (registro.getInt("codigoMedicamento"),
                                             registro.getString("nombreMedicamento")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public ObservableList<DetalleReceta> getDReceta(){
        ArrayList <DetalleReceta> lista = new ArrayList <DetalleReceta> ();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarDetalleRecetas}");
            ResultSet resultado = procedimiento.executeQuery();    
         
            while(resultado.next()){
                lista.add(new DetalleReceta (resultado.getInt("codigoDetalleReceta"),
                                             resultado.getString("dosis"),
                                             resultado.getInt("codigoReceta"),
                                             resultado.getInt("codigoMedicamento") ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }        

        return listaDReceta = FXCollections.observableArrayList(lista);
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
    
    public ObservableList<Medicamento> getMedicamento (){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        
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
            tipoDeOperacion=operaciones.GUARDAR;
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
            tipoDeOperacion=operaciones.NINGUNO; 
            cargarDatos();
            break;
        }
    }    
     
    public void guardar(){
        try{    
            DetalleReceta registro = new DetalleReceta();
            registro.setDosis(txtDosis.getText());
            registro.setCodigoReceta(((Receta)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
            registro.setCodigoMedicamento(((Medicamento)cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());

            try{
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarDetalleReceta(?, ?, ?)}");
                procedimiento.setString(1, registro.getDosis());
                procedimiento.setInt(2, registro.getCodigoReceta());
                procedimiento.setInt(3, registro.getCodigoMedicamento());
                procedimiento.execute();
                listaDReceta.add(registro);
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error datos incompletos");
        }
    }
    
    public void eliminar (){
        switch(tipoDeOperacion){
            case GUARDAR:
            desactivarControles();
            limpiarControles();
            cmbCodigoReceta.getSelectionModel().clearSelection();
            btnNuevo.setText("Nuevo");
            btnEliminar.setText("Eliminar");  
            btnEditar.setDisable(false);
            btnReporte.setDisable(false);
            imgNuevo.setImage(new Image("/org/alessandrogutierrez/image/nuevo.png"));
            imgEliminar.setImage(new Image("/org/alessandrogutierrez/image/Eliminar.png"));
            tipoDeOperacion = operaciones.NINGUNO;  
            break;
            
            default:
            if (tblDetalleRecetas.getSelectionModel().getSelectedItem() != null){
                int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Elimar Detalle Receta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (respuesta == JOptionPane.YES_OPTION){
                    try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarDetalleReceta(?)}");                     
                    procedimiento.setInt(1, ((DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem()).getCodigoDReceta());
                    procedimiento.execute();
                    listaDReceta.remove(tblDetalleRecetas.getSelectionModel().getSelectedIndex());
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
            if(tblDetalleRecetas.getSelectionModel().getSelectedItem() != null){
                btnEditar.setText("Actualizar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(true);
                btnEliminar.setDisable(true);
                imgEditar.setImage(new Image("/org/alessandrogutierrez/image/actualizar.png"));
                imgReporte.setImage(new Image("/org/alessandrogutierrez/image/cancelar.png"));
                activarControles();
                cmbCodigoMedicamento.setDisable(true);
                cmbCodigoReceta.setDisable(true);
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
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarDetalleReceta(?, ?)}");
            DetalleReceta registro = (DetalleReceta)tblDetalleRecetas.getSelectionModel().getSelectedItem();
            registro.setDosis(txtDosis.getText());
            procedimiento.setInt(1, registro.getCodigoDReceta());
            procedimiento.setString(2, registro.getDosis());
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
                break;  
        }
    }
    
    public void desactivarControles(){
        txtCodigoDReceta.setEditable(false);
        txtDosis.setEditable(false);
        cmbCodigoReceta.setDisable(true);
        cmbCodigoMedicamento.setDisable(true);
        
    }
    
    public void activarControles(){
        txtDosis.setEditable(true);
        cmbCodigoReceta.setDisable(false);
        cmbCodigoMedicamento.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoDReceta.clear();
        txtDosis.clear();
        tblDetalleRecetas.getSelectionModel().clearSelection();
        cmbCodigoReceta.getSelectionModel().select(null);
        cmbCodigoMedicamento.getSelectionModel().select(null);
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
