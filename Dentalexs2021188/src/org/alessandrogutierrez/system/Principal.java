/*
    Nombre: Alessandro Emanuel Gutiérrez Boc
    Código Técnico: IN5AV
    Carné: 2021188
    Fecha de creación: 05/04/2022
    Modificaciones: 20/04/2022 , 22/04/2022, 23/04/2022
                    03/05/2022 , 06/05/2022, 07/05/2022
                    11/05/2022 , 14/05/2022, 
                    25/05/2022 , 27/05/2022, 
                    01/06/2022 , 03/06/2022,
                    08/06/2022 , 10/06/2022,
                    12/06/2022
 */
package org.alessandrogutierrez.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.alessandrogutierrez.controller.CitaController;
import org.alessandrogutierrez.controller.DetalleRecetaController;
import org.alessandrogutierrez.controller.DoctorController;
import org.alessandrogutierrez.controller.EspecialidadesController;
import org.alessandrogutierrez.controller.LoginController;
import org.alessandrogutierrez.controller.MedicamentosController;
import org.alessandrogutierrez.controller.MenuPrincipalController;
import org.alessandrogutierrez.controller.PacienteController;
import org.alessandrogutierrez.controller.ProgramadorController;
import org.alessandrogutierrez.controller.RecetaController;
import org.alessandrogutierrez.controller.UsuarioController;

public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA = "/org/alessandrogutierrez/view/";
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Dentalexs 2022");
        escenarioPrincipal.getIcons().add(new Image("/org/alessandrogutierrez/image/Logo.PNG"));
       // Parent root = FXMLLoader.load(getClass().getResource("/org/alessandrogutierrez/view/MenuPrincipalView.fxml"));
       // Scene escena = new Scene (root);
       // escenarioPrincipal.setScene(escena);
        ventanaCita();
        //ventanaCita();
        escenarioPrincipal.show();
    };
    
    public void menuPrincipal (){
      try {
          MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena ("MenuPrincipalView.fxml",453,445);
          ventanaMenu.setEscenarioPrincipal (this);
      }catch (Exception e){
          e.printStackTrace();
      }
            
    }
    
    public void ventanaProgramador (){
        try {
            ProgramadorController vistaProgramador = (ProgramadorController) cambiarEscena ("ProgramadorView.fxml",702,404);
            vistaProgramador.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
                
    }
    
    public void ventanaPacientes (){
        try {
            PacienteController vistaPacientes = (PacienteController)cambiarEscena("PacienteView.fxml",1100,400);
            vistaPacientes.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaMedicamentos(){
        try {
            MedicamentosController vistaMedicamentos = (MedicamentosController)cambiarEscena("MedicamentosView.fxml",800,400);
            vistaMedicamentos.setEscenarioPrincipal(this);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaEspecialidades(){
        try {
            EspecialidadesController vistaEspecialidades = (EspecialidadesController)cambiarEscena("EspecialidadesView.fxml",800,400);
            vistaEspecialidades.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void ventanaDoctores(){
        try{
            DoctorController vistaDoctor = (DoctorController) cambiarEscena("DoctorView.fxml",900,400);
            vistaDoctor.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaRecetas(){
        try{
            RecetaController vistaReceta = (RecetaController) cambiarEscena ("RecetaView.fxml",900,400);
            vistaReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDetalleRecetas(){
        try{
            DetalleRecetaController vistaDReceta = (DetalleRecetaController) cambiarEscena ("DetalleRecetaView.fxml",900,400);
            vistaDReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void ventanaLogin(){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml",617,437);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaUsuario(){
        try{
            UsuarioController usuario = (UsuarioController)cambiarEscena("UsuarioView.fxml",700, 380);
            usuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCita(){
        try{
            CitaController cita = (CitaController)cambiarEscena("CitaView.fxml",1100,460);
            cita.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena (String fxml, int ancho, int alto)throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader ();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene( (AnchorPane)cargadorFXML.load(archivo),ancho,alto );
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    };
    
}
