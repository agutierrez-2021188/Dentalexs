package org.alessandrogutierrez.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.alessandrogutierrez.bean.Cita;
import org.alessandrogutierrez.bean.Doctor;
import org.alessandrogutierrez.bean.Medicamento;
import org.alessandrogutierrez.bean.Paciente;
import org.alessandrogutierrez.db.Conexion;
import org.alessandrogutierrez.system.Principal;

public class CitaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;  
    private ObservableList <Paciente> listaPaciente;
    private ObservableList <Doctor> listaDoctor;
    private ObservableList <Cita> listaCita;
    private DatePicker fechaC;
    
    @FXML private TextField txtCodigoCita;
    @FXML private TextField txtTratamiento;
    @FXML private TextField txtDescripcionActual;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private Spinner <Integer> spiHora;
    @FXML private Spinner <Integer> spiMinuto;
    @FXML private Spinner <Integer> spiSegundo;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblCitas;
    @FXML private ComboBox cmbCodigoPaciente;
    @FXML private ComboBox cmbNumeroColegiado;
    @FXML private TableColumn colCodigoCita;
    @FXML private TableColumn colFechaCita;    
    @FXML private TableColumn colHoraCita; 
    @FXML private TableColumn colTratamiento; 
    @FXML private TableColumn colDescripcionActual; 
    @FXML private TableColumn colCodigoPaciente; 
    @FXML private TableColumn colNumeroColegiado; 
    @FXML private GridPane grpFechas;
    SpinnerValueFactory<Integer> hora = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(00, 23);    
    SpinnerValueFactory<Integer> minuto = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(00, 59);    
    SpinnerValueFactory<Integer> segundo = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(00, 59);
    
    @Override 
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoPaciente.setItems(getPaciente());
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setItems(getDoctor());
        cmbNumeroColegiado.setDisable(true); 
        
        fechaC = new DatePicker(Locale.ENGLISH);
        fechaC.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fechaC.getCalendarView().todayButtonTextProperty().set("Today");
        fechaC.getCalendarView().setShowWeeks(false);
        grpFechas.add(fechaC, 3, 0);
        fechaC.getStylesheets().add("/org/alessandrogutierrez/resource/DatePicker.css");
        
        hora.setValue(00);
        spiHora.setValueFactory(hora);
        
        minuto.setValue(00);
        spiMinuto.setValueFactory(minuto);
        
        segundo.setValue(00);
        spiSegundo.setValueFactory(segundo);
    }        
    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodigoCita.setCellValueFactory(new PropertyValueFactory <Cita, Integer>("codigoCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory <Cita, Date>("fechaCita"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita, Time>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory <Cita , String >("tratamiento"));
        colDescripcionActual.setCellValueFactory(new PropertyValueFactory <Cita , String>("descripcionActual"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory <Cita, Integer>("codigoPaciente"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("numeroColegiado"));
    }
    
    public void seleccionarElemento(){
        try {
            txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
            fechaC.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
            hora.setValue(Integer.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita().getHours()));
            spiHora.setValueFactory(hora);
            minuto.setValue(Integer.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita().getMinutes()));
            spiMinuto.setValueFactory(minuto); 
            segundo.setValue(Integer.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita().getSeconds()));
            spiSegundo.setValueFactory(segundo);
//            txtHoraCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
            txtTratamiento.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento());
            txtDescripcionActual.setText(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDescripcionActual());
            cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
            cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));            
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "¡¡Error no hay fila seleccionada!!"); 
        }
    }
    
    
    public ObservableList<Cita>getCita(){
        ArrayList <Cita> lista = new ArrayList <Cita>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCitas}");
            ResultSet resultado = procedimiento.executeQuery();
                
            while(resultado.next()){
                lista.add(new Cita(resultado.getInt("codigoCita"),
                resultado.getDate("fechaCita"),
                resultado.getTime("horaCita"),
                resultado.getString("tratamiento"),
                resultado.getString("descripCondActual"),
                resultado.getInt("codigoPaciente"),
                resultado.getInt("numeroColegiado")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCita = FXCollections.observableArrayList(lista);
    }

    public ObservableList<Paciente> getPaciente (){
        ArrayList<Paciente> lista = new ArrayList<Paciente> ();
        try{
        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes}");
            ResultSet resultado = procedimiento.executeQuery();    
         
            while(resultado.next()){
                lista.add(new Paciente (resultado.getInt("codigoPaciente"),
                                       resultado.getString("nombresPaciente"),
                                       resultado.getString("apellidosPaciente"),
                                       resultado.getString("sexo"),
                                       resultado.getDate("fechaNacimiento"),
                                       resultado.getString("direccionPaciente"),
                                       resultado.getString("telefonoPersonal"),
                                       resultado.getDate("fechaPrimeraVisita") ));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
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
    
    // --- ----
    
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

    public Paciente buscarPaciente(int codigoPaciente){
        Paciente resultado = null;        
        try{
         PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPaciente(?)}");
         procedimiento.setInt(1, codigoPaciente);
         ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                 resultado = new Paciente(registro.getInt("codigoPaciente"),
                                        registro.getString("nombresPaciente"),
                                        registro.getString("apellidosPaciente"),
                                        registro.getString("sexo"),
                                        registro.getDate("fechaNacimiento"),
                                        registro.getString("direccionPaciente"),
                                        registro.getString("telefonoPersonal"),
                                        registro.getDate("fechaPrimeraVisita"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
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
        try{
        Cita registro = new Cita();
        registro.setFechaCita(fechaC.getSelectedDate());
        registro.setHoraCita((new java.sql.Time(spiHora.getValue(), spiMinuto.getValue(), spiSegundo.getValue())));
        registro.setTratamiento(txtTratamiento.getText());
        registro.setDescripcionActual(txtDescripcionActual.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());  
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCita(?, ?, ?, ?, ?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(2, registro.getHoraCita());
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDescripcionActual());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
            }catch(Exception e){
                e.printStackTrace(); 
        }  
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Datos incompletos");
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
                if(tblCitas.getSelectionModel().getSelectedItem() != null){
                  int respuesta = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el registro?", "Elimar Cita", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                  
                  if (respuesta == JOptionPane.YES_OPTION) {
                     try {
                        PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCita(?)}");  
                        procedimiento.setInt(1, ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                        procedimiento.execute();
                        listaCita.remove(tblCitas.getSelectionModel().getSelectedIndex());
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
                if (tblCitas.getSelectionModel().getSelectedItem() != null){
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/alessandrogutierrez/image/actualizar.png"));
                    imgReporte.setImage(new Image("/org/alessandrogutierrez/image/cancelar.png"));
                    activarControles();
                    cmbCodigoPaciente.setDisable(true);
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
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCita(?, ?, ?, ?, ?)}");
            Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
            
            registro.setFechaCita(fechaC.getSelectedDate());
            registro.setHoraCita((new java.sql.Time(spiHora.getValue(), spiMinuto.getValue(), spiSegundo.getValue())));
            registro.setTratamiento(txtTratamiento.getText());
            registro.setDescripcionActual(txtDescripcionActual.getText());
            
            procedimiento.setInt(1, registro.getCodigoCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setTime(3,  registro.getHoraCita());
            procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDescripcionActual());
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
        txtCodigoCita.setEditable(false);
        txtTratamiento.setEditable(false);
        txtDescripcionActual.setEditable(false);
        txtCodigoCita.setEditable(false);
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setDisable(true);
        spiHora.setEditable(false);
        spiMinuto.setEditable(false);
        spiSegundo.setEditable(false);
    }
    
    public void activarControles(){
        txtTratamiento.setEditable(true);
        txtDescripcionActual.setEditable(true);      
        cmbCodigoPaciente.setDisable(false);
        cmbNumeroColegiado.setDisable(false);
        spiHora.setEditable(true);
        spiMinuto.setEditable(true);
        spiSegundo.setEditable(true);
    }
    
    public void limpiarControles(){
        hora.setValue(00);
        spiHora.setValueFactory(hora);
            
        minuto.setValue(00);
        spiMinuto.setValueFactory(minuto);
        
        segundo.setValue(00);
        spiSegundo.setValueFactory(segundo);
        txtCodigoCita.clear();
        txtTratamiento.clear();
        txtDescripcionActual.clear();
        cmbCodigoPaciente.getSelectionModel().select(null);
        cmbNumeroColegiado.getSelectionModel().select(null);        
        tblCitas.getSelectionModel().clearSelection();
        fechaC.setSelectedDate(null);
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
