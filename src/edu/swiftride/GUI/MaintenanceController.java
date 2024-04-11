/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.swiftride.GUI;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;
import edu.swiftride.Services.EntrepriseCRUD;
import edu.swiftride.Services.GarageCRUD;
import edu.swiftride.Services.MailMaintenance;
import edu.swiftride.Services.MaintenanceCRUD;
import edu.swiftride.Services.VoitureCRUD;
import edu.swiftride.entities.DetailMaintenanceVoitures;
import edu.swiftride.entities.Entreprise;
import edu.swiftride.entities.Garage;
import edu.swiftride.entities.Maintenance;
import edu.swiftride.entities.Voiture;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.mail.MessagingException;
/**
 * FXML Controller class
 *
 * @author dhibi
 */
public class MaintenanceController implements Initializable {
    
   @FXML
   private ComboBox<Garage> cb_garage;
      @FXML
   private ComboBox<Voiture> cb_voiture;
     
     
     int id_g=0;
     int id_v=0;
    @FXML
    private TableView tb_maintenaces;
    @FXML
    private TableColumn tb_id;
    @FXML
    private TableColumn tb_date;
    @FXML
    private TableColumn tb_datef;
    @FXML
    private TableColumn tb_type;
     @FXML
    private TableColumn tb_voiture;
    @FXML
    private JFXButton bt_save;
    @FXML
    private JFXButton bt_modify;
    @FXML
    private JFXButton bt_delete;
    @FXML
    private TextField tf_recherche;
    @FXML
    private DatePicker pk_date;
    @FXML
    private RadioButton rb_reparation;
    @FXML
    private RadioButton rb_entretien;
    @FXML
    private ToggleGroup type;
    @FXML
    private Label lb_date;
    @FXML
    private Label lb_rd;
    @FXML
    private Label lb_garage;
    @FXML
    private Label lb_voiture;
    @FXML
    private Label lb_base;
    @FXML 
    private JFXTimePicker pk_timed;
    @FXML 
    private JFXTimePicker pk_timef;
    @FXML
    private DatePicker pk_fin;
     @FXML
    private Label lb_pk2;
     @FXML
     private JFXToggleButton active;
    
    MaintenanceCRUD mc = new MaintenanceCRUD();
    
    VoitureCRUD vc = new VoitureCRUD();
    
    EntrepriseCRUD ec = new EntrepriseCRUD();
    
    public static ObservableList<DetailMaintenanceVoitures> listM ;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
            pk_date.setValue(LocalDate.now());
        
        pk_date.setDayCellFactory(pcker-> new DateCell(){
         public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            LocalDate today = LocalDate.now();

            setDisable(empty || date.compareTo(today) < 0 );
        }
        });
        
           pk_fin.setValue(LocalDate.now());
        
        pk_fin.setDayCellFactory(pcker-> new DateCell(){
         public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            LocalDate today = LocalDate.now();

            setDisable(empty || date.compareTo(today) < 0 );
        }
        });
        
        pk_timed.setIs24HourView(true);
        pk_timed.setDefaultColor(Color.rgb(255,127,80));
        pk_timef.setIs24HourView(true);
        pk_timef.setDefaultColor(Color.rgb(255,127,80));
        
        pk_timed.setVisible(true);
        
        displayMaintenance();
        getVoitureItems();
        setCellValueFromTableToText();
        
         FilteredList<DetailMaintenanceVoitures> filtreData = new FilteredList<>(listM , p->true);
       
        
         tf_recherche.textProperty().addListener((observable, oldValue, newValue) ->{
            filtreData.setPredicate( maintenance ->{
                
             if (newValue == null || newValue.isEmpty()) {
            return true;
             }
               
             String lowerCaseFilter = newValue.toLowerCase();
             if(maintenance.getType().toLowerCase().contains(lowerCaseFilter)){
                 
                 return true;
             }
             else if(maintenance.getDate_maintenance().toString().toLowerCase().contains(lowerCaseFilter))
             {  return true ; }
              else if(maintenance.getFin_maintenance().toString().toLowerCase().contains(lowerCaseFilter))
             {  return true ; }
             else
                 return false;
            });
       });
       
       SortedList<DetailMaintenanceVoitures> sortedData = new SortedList<>(filtreData);
       sortedData.comparatorProperty().bind(tb_maintenaces.comparatorProperty());
       tb_maintenaces.setItems(sortedData);
       
       
    }  
    
     
      public void getVoitureItems(){
        VoitureCRUD vc= new VoitureCRUD();
        List<String> str = new ArrayList();
         List<Voiture> lg = vc.diplayVoitures();
         
         cb_voiture.getItems().addAll(lg);
         
          cb_voiture.setOnAction(ae ->{
            id_v = cb_voiture.getSelectionModel().getSelectedItem().getId();
        });
    }
      
      String maintenance_type=null;

    @FXML
    private void getMaintenance(ActionEvent event) {
        
        if(rb_reparation.isSelected()){
            maintenance_type=rb_reparation.getText();
        }
        
        else if (rb_entretien.isSelected()){
            
            maintenance_type=rb_entretien.getText();
        }
        System.out.println(maintenance_type);
        
    }

    @FXML
    private void saveMaintenance(ActionEvent event) {
        boolean isExiste=false;
        List<Maintenance> l = mc.listDesEntites();
      LocalDateTime datetimed =null;
       LocalDateTime datetimef=null;
        
        if(!pk_date.isEditable() && pk_fin.isEditable() && pk_timed.isEditable() && pk_timef.isEditable()
                && !rb_entretien.isPressed()|| !rb_reparation.isPressed() && id_v!=0){
          LocalDate dd =  pk_date.getValue();
        LocalDate df = pk_fin.getValue();
        
        LocalTime timed=pk_timed.getValue();
        LocalTime timef=pk_timef.getValue();
        
       datetimed =LocalDateTime.of(dd,timed);
        datetimef =LocalDateTime.of(df,timef);
        Maintenance m = new Maintenance();
        m.setDate_maintenance(Timestamp.valueOf(datetimed));
        m.setFin_maintenance(Timestamp.valueOf(datetimef));
        m.setId_garage(id_g);
        m.setId_voiture(id_v);
        m.setType(maintenance_type);
        
         for( Maintenance m1 : l){
             LocalDateTime d = m.getDate_maintenance().toLocalDateTime();
             LocalDateTime d1 = m1.getDate_maintenance().toLocalDateTime();
             LocalDate dy=d.toLocalDate();
             LocalDate dyy= d1.toLocalDate();
            if (m.getId_voiture()==m1.getId_voiture() && dy.compareTo(dyy)==0) {
           
        
                lb_base.setText("Ce Maintenace déja existe");
                lb_base.setTextFill(Color.RED);
                System.out.println("kiko"+isExiste);
                isExiste=true;
                break;
            }
        }
        if(!isExiste){
            try {
                mc.ajouterEntitie(m);
                pk_date.setValue(LocalDate.now());
                maintenance_type=null;
                
                FXMLLoader loader= new FXMLLoader(getClass().getResource("ajouterGarage.fxml"));
                Stage stage = new Stage();
                AjouterGarageController dc = new AjouterGarageController();
                loader.setController(dc);
                List<Maintenance> lm = mc.getMaintenace(m);
                Maintenance ms = new Maintenance();
                for(Maintenance mm : lm){
                    ms=mm;
                }
                dc.setM(ms);
                Parent roote = loader.load();
                Scene sc = new Scene(roote);
                 stage.setScene(sc);  
                   stage.show();
                
                displayMaintenance();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        }
        else{
            
            if(datetimed==LocalDateTime.now()){
             lb_date.setText("* Ce champ est OBLIGATOIRE !");
             lb_date.setTextFill(Color.RED);
            }
            if(datetimef==LocalDateTime.now()){
             lb_pk2.setText("* Ce champ est OBLIGATOIRE !");
             lb_pk2.setTextFill(Color.RED);
            }
            if(!rb_entretien.isPressed()|| !rb_reparation.isPressed()){
             lb_rd.setText("* Ce champ est OBLIGATOIRE !");
             lb_rd.setTextFill(Color.RED);   
            }
            if(id_v==0){
             lb_voiture.setText("* Ce champ est OBLIGATOIRE !");
             lb_voiture.setTextFill(Color.RED);   
            }
        }
        
    }
    
    
    public void displayMaintenance(){
        
        
        List<Voiture> voitures = vc.diplayDetailVoitures();
        
        System.out.println(voitures);
        

        List<DetailMaintenanceVoitures> details = new ArrayList<>();
        
        
        for(Voiture v : voitures){
      
            List<Maintenance> maintenaces = mc.getwithCarId(v.getId());
            
            for(Maintenance m : maintenaces){
                System.out.println(m.getId());
                  DetailMaintenanceVoitures d = new DetailMaintenanceVoitures();
                  
                  
                  
                  
                    d.setId(m.getId());
                    d.setDate_maintenance(m.getDate_maintenance());
                    d.setFin_maintenance(m.getFin_maintenance());
                    d.setType(m.getType());
                    d.setVoiture(v.getMatricule().concat(" - ").concat(v.getMarque()));
                    
                     details.add(d);
                }
                
            }
            
           
            System.out.println(details);
        
        tb_id.setCellValueFactory(new PropertyValueFactory("id"));
        tb_date.setCellValueFactory(new PropertyValueFactory("date_maintenance"));
        tb_type.setCellValueFactory(new PropertyValueFactory("type"));
        tb_datef.setCellValueFactory(new PropertyValueFactory("fin_maintenance"));
       tb_voiture.setCellValueFactory(new PropertyValueFactory("voiture"));
        
        listM =FXCollections.observableArrayList(details);
        
        tb_maintenaces.setItems(listM);
        
    }

    @FXML
    private void deleteMaintenance(ActionEvent event) {
        
        if(tb_maintenaces.getSelectionModel().getSelectedItems().isEmpty()){
              Alert a = new Alert(Alert.AlertType.ERROR);
                a.setContentText("Vous devez sélectionner le Maintenance !");
                a.setTitle("ERREUER");
                 Optional<ButtonType> res=a.showAndWait();
                if(res.get() ==ButtonType.OK){
                a.close();
        }
        }else{
            
        
         DetailMaintenanceVoitures m =(DetailMaintenanceVoitures) tb_maintenaces.getSelectionModel().getSelectedItem();
         
         if(m.getId()!=0){
         
          Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("Voulez-vous vraiment supprimer le Maintenance N°"+m.getId());
                a.setTitle("CONFIRMER");
                 Optional<ButtonType> res=a.showAndWait();
                if(res.get() ==ButtonType.OK){
                    if(mc.supprimerEntite(m.getId())){
                     listM.remove(m);
        } 
                }
                else if (res.get()==ButtonType.CANCEL){
                    System.out.println("alert closed");
                }
         }
         else{
              Alert b = new Alert(Alert.AlertType.ERROR);
                                b.setContentText("Vous devez sélectionner le Maintenance !");
                                b.setTitle("ERREUR");
                                Optional<ButtonType> res=b.showAndWait();
                                if(res.get() ==ButtonType.OK){
                                    System.out.println("alert closed");
                                }

         }
         
        }
        
    }
     int id =0;
     private void setCellValueFromTableToText(){
       
        tb_maintenaces.setOnMouseClicked((MouseEvent event) -> {
            DetailMaintenanceVoitures m= (DetailMaintenanceVoitures) tb_maintenaces.getItems().get(tb_maintenaces.getSelectionModel()
                    .getSelectedIndex());
            LocalDateTime loc = m.getDate_maintenance().toLocalDateTime();
            LocalDateTime locc = m.getFin_maintenance().toLocalDateTime();
            pk_date.setValue(loc.toLocalDate());
            pk_fin.setValue(locc.toLocalDate());
            
            pk_timed.setValue(loc.toLocalTime());
            pk_timef.setValue(locc.toLocalTime());
            id = m.getId();
            String[] str = m.getVoiture().split("-");
            
            System.out.println(str[0]);
             System.out.println(str[1]);
             Voiture v = vc.getVoitureWithMatriculAndMarque(str[1].trim(),str[0].trim());
             System.out.println("hhh"+vc.getVoitureWithMatriculAndMarque("polo","21565uu5"));
            cb_voiture.setValue(v);
            if(m.getType().equals(rb_entretien.getText())){
            rb_entretien.setSelected(true);
            }
            else if (m.getType().equals(rb_reparation.getText())){
                rb_reparation.setSelected(true);
            }
        });
        
    }

    @FXML
    private void modifyMaintenance(ActionEvent event) {
        LocalDate dd =  pk_date.getValue();
        LocalDate df = pk_fin.getValue();
        
        LocalTime timed=pk_timed.getValue();
        LocalTime timef=pk_timef.getValue();
        
        LocalDateTime datetimed =LocalDateTime.of(dd,timed);
        LocalDateTime datetimef =LocalDateTime.of(df,timef);
        
        
        Maintenance m = new Maintenance();
        
        m.setDate_maintenance(Timestamp.valueOf(datetimed));
        m.setFin_maintenance(Timestamp.valueOf(datetimef));
        m.setId_voiture(id_v);
        m.setType(maintenance_type);
        m.setId(id);
        if(m.getDate_maintenance()!=null && m.getId()!=0 ){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setContentText("Voulez-vous vraiment modifier le maintenance N°"+m.getId());
                a.setTitle("CONFIRMER");
                 Optional<ButtonType> res=a.showAndWait();
                if(res.get() ==ButtonType.OK){
        
                    if(mc.modifierEntite(m)){
                         pk_date.setValue(LocalDate.now());
                          maintenance_type=null;
                    displayMaintenance();
                    }
        
                }
                 else if (res.get()==ButtonType.CANCEL){
                    System.out.println("alert closed");
                }
    }
    
                else{
                Alert b = new Alert(Alert.AlertType.ERROR);
                b.setContentText("Vous devez sélectionner le Maintenance !");
                b.setTitle("ERREUR");
                Optional<ButtonType> res=b.showAndWait();
                if(res.get() ==ButtonType.OK){
                System.out.println("alert closed");
    }
}
    }
    
    
    public Maintenance getDispTime(List<Garage> garages ){
        
       Maintenance m = new Maintenance();
       
       for(Garage g : garages){
           
           List<Maintenance> listm = mc.getMaintenaceWithDateAndGarageId(g.getId(), Date.valueOf(LocalDate.now().plusDays(30)));
           
           System.out.println(" fkkk"+Date.valueOf(LocalDate.now().plusDays(30)));
           
           
        LocalDateTime dateTime = LocalDateTime.now().plusDays(30);
        System.out.println(" fkkk"+LocalDateTime.now().plusDays(30));
        
        LocalDateTime newDateTime = dateTime.withHour(8).withMinute(30).withSecond(0);
        System.out.println(" fkkk8888"+newDateTime);
        
        LocalDateTime newDateTime2 = dateTime.withHour(17).withMinute(30).withSecond(0);
        
        System.out.println("tyt"+listm.size());
        if(listm.size()!=0){
           for(int i=0 ;i<listm.size() ;i++){
               System.err.println("ee"+listm.get(i).getDate_maintenance().toLocalDateTime().withSecond(0).minusHours(1));
               
               if(listm.get(i).getDate_maintenance().toLocalDateTime().withSecond(0).minusHours(1).isAfter(newDateTime)){
                    System.out.println("tyt");
                  m.setDate_maintenance( Timestamp.valueOf(listm.get(i).getDate_maintenance().toLocalDateTime().minusHours(1)));
                    m.setId_garage(g.getId());
                   break;
               }
               
               else if(listm.get(listm.size()-1).getFin_maintenance().toLocalDateTime().plusHours(1).isBefore(newDateTime2)){
                   
                   m.setDate_maintenance(Timestamp.valueOf(listm.get(i).getDate_maintenance().toLocalDateTime().plusDays(1)));
                   m.setId_garage(g.getId());
                    break;
               }
               
               else{
                   
                   LocalTime time1 = listm.get(i).getFin_maintenance().toLocalDateTime().toLocalTime();
                   LocalTime time2 = listm.get(i+1).getFin_maintenance().toLocalDateTime().toLocalTime();
                   
                   
                   if(time1.until(time2, ChronoUnit.HOURS)>=2){
                       
                        m.setDate_maintenance(Timestamp.valueOf(listm.get(i).getFin_maintenance().toLocalDateTime().plusHours(1)));
                        m.setId_garage(g.getId());
                       break;
                   }
                   
               }
           }
          
           }
        else{
            System.out.println("khrajet");
            m.setId_garage(g.getId());
            m.setDate_maintenance(Timestamp.valueOf(newDateTime));
            break;
        }
           
       }
        
        return m;
    }
    
      
    public void EntretintAuto() {
        
        System.out.println("fom activate");
        
        VoitureCRUD vc = new VoitureCRUD();
        
        GarageCRUD gc = new  GarageCRUD();
        
        List<Garage> listg = gc.displayGarages();
        
        System.out.println("fomautogg"+listg);
        
       List <Voiture> voitures = vc.diplayDetailVoitures();
       
       for(Voiture v : voitures){
           
          int ageV = Period.between( v.getDate_circulation().toLocalDate() , LocalDate.now()).getYears();
           System.out.println("fomauto1"+ageV);
           
           if(ageV>=3){
               System.out.println("fomauto"+v);
               
               List<Maintenance> listm = mc.getwithCarId(v.getId());
               
               if(listm.isEmpty()){
                   System.out.println("ff"+listm);
                   Maintenance m ;
                   Maintenance newM = new Maintenance();
                   
                  m = getDispTime(listg);
                   System.out.println("fomauto8"+m);
                   newM.setDate_maintenance(m.getDate_maintenance());
                   newM.setFin_maintenance(Timestamp.valueOf(m.getDate_maintenance().toLocalDateTime().plusHours(1)));
                   newM.setType("Entretien");
                   newM.setId_garage(m.getId_garage());
                   newM.setId_voiture(v.getId());
                   
                   mc.ajouterEntitie(newM);
                   System.out.println("fomauto9"+newM);
                   
                   
                   Entreprise e = ec.getwithCarId(v.getId_entreprise());
                   System.out.println("55fff"+e+"kkkkkkkk"+v.getId_entreprise());
                   //partie mail
                    MailMaintenance.setEmail(e.getEmail());
                    MailMaintenance.setNomEn(e.getNom());
                     MailMaintenance.setDate(newM.getDate_maintenance());
                     MailMaintenance.setDatef(newM.getFin_maintenance());
                    MailMaintenance.sendVerificationCode();
                   
                   
               }
               
               else if (listm.size()>0){
                   
                   for(int i=0 ; i<listm.size() ; i++){
                       
                       System.out.println("tes1"+ (listm.get(listm.size()-1)));
                       LocalDate d1 = listm.get(listm.size()-1).getDate_maintenance().toLocalDateTime().toLocalDate();
                       LocalDate d2 = LocalDate.now();
                       
                       int an =Period.between(d1, d2).getYears();
                       
                       if(an>=1){
                           
                        Maintenance m ;
                        Maintenance newM = new Maintenance();

                         m = getDispTime(listg);

                        newM.setDate_maintenance(m.getDate_maintenance());
                        newM.setFin_maintenance(Timestamp.valueOf(m.getDate_maintenance().toLocalDateTime().plusHours(1)));
                        newM.setType("Entretien");
                        newM.setId_garage(m.getId());
                        newM.setId_voiture(v.getId());
                        mc.ajouterEntitie(newM);
                   System.out.println("fomauto9"+newM);
                   
                   
                   Entreprise e = ec.getwithCarId(v.getId_entreprise());
                   System.out.println("55fffffffffffffffffffffffffff"+e+"kkkkkkkk"+v.getId_entreprise());
                   //partie mail
                    MailMaintenance.setEmail(e.getEmail());
                     MailMaintenance.setDate(newM.getDate_maintenance());
                     MailMaintenance.setDatef(newM.getFin_maintenance());
                    MailMaintenance.sendVerificationCode();
                       }
                       else{
                           
                           System.out.println("tout est bien");
                       }
                       
                   }
                   
               }
               ///bech naamel mta3 l'entretient 
           }
           
           if(divisible(v.getKilometrage())){
               
               List<Maintenance> listm = mc.getwithCarId(v.getId());
               
               if(listm.isEmpty()){
                   System.out.println("ff"+listm);
                   Maintenance m ;
                   Maintenance newM = new Maintenance();
                   
                  m = getDispTime(listg);
                   System.out.println("fomauto8"+m);
                   newM.setDate_maintenance(m.getDate_maintenance());
                   newM.setFin_maintenance(Timestamp.valueOf(m.getDate_maintenance().toLocalDateTime().plusHours(1)));
                   newM.setType("Entretien");
                   newM.setId_garage(m.getId_garage());
                   newM.setId_voiture(v.getId());
                   
                   mc.ajouterEntitie(newM);
                   System.out.println("fomauto9"+newM);
                   
                   
                   Entreprise e = ec.getwithCarId(v.getId_entreprise());
                   System.out.println("55fff"+e+"kkkkkkkk"+v.getId_entreprise());
                   //partie mail
                    MailMaintenance.setEmail(e.getEmail());
                    MailMaintenance.setNomEn(e.getNom());
                     MailMaintenance.setDate(newM.getDate_maintenance());
                     MailMaintenance.setDatef(newM.getFin_maintenance());
                    MailMaintenance.sendVerificationCode();
                   
                   
               }
               
               else if (listm.size()>0){
                   
                   for(int i=0 ; i<listm.size() ; i++){
                       
                       System.out.println("tes1"+ (listm.get(listm.size()-1)));
                       LocalDate d1 = listm.get(listm.size()-1).getDate_maintenance().toLocalDateTime().toLocalDate();
                       LocalDate d2 = LocalDate.now();
                       
                       long an =ChronoUnit.DAYS.between(d1, d2);
                       
                       if(an>=4){
                           
                        Maintenance m ;
                        Maintenance newM = new Maintenance();

                         m = getDispTime(listg);

                        newM.setDate_maintenance(m.getDate_maintenance());
                        newM.setFin_maintenance(Timestamp.valueOf(m.getDate_maintenance().toLocalDateTime().plusHours(1)));
                        newM.setType("Entretien");
                        newM.setId_garage(m.getId());
                        newM.setId_voiture(v.getId());
                        mc.ajouterEntitie(newM);
                   System.out.println("fomauto9"+newM);
                   
                   
                   Entreprise e = ec.getwithCarId(v.getId_entreprise());
                   System.out.println("55fffffffffffffffffffffffffff"+e+"kkkkkkkk"+v.getId_entreprise());
                   //partie mail
                    MailMaintenance.setEmail(e.getEmail());
                     MailMaintenance.setDate(newM.getDate_maintenance());
                     MailMaintenance.setDatef(newM.getFin_maintenance());
                    MailMaintenance.sendVerificationCode();
                       }
                       else{
                           
                           System.out.println("tout est bien");
                       }
                       
                   }
                   
               }
           
                
           }
           
       }
        
        
    }
    
    
    public boolean divisible(int kilometrage){
       return kilometrage%5000 == 0;
    }
    
    public void actionButton() throws MessagingException{
        EntretintAuto();
        displayMaintenance();
    }
    
 
}
