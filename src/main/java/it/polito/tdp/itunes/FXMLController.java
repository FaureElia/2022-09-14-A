/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnComponente"
    private Button btnComponente; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSet"
    private Button btnSet; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doComponente(ActionEvent event) {
    	Album al=this.cmbA1.getValue();
    	if(al==null) {
    		
    	}
    	
    	Set <Album> connessa=this.model.getComponente(al);
    	int somma=0;
    	for (Album a: connessa) {
    		somma+=a.getDurata();
    	}
    	txtResult.appendText("dimensione componente: "+connessa.size());
    	txtResult.appendText("DurataTotale: "+somma);
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String durataS=txtDurata.getText();
    	if (durataS.equals("")) {
    		this.txtResult.setText("valore d obbligatorio");
    		return;
    	}
    	Double duration;
    	try {
    		duration=Double.parseDouble(durataS);
    		model.creaGrafo(duration);
    		this.cmbA1.getItems().addAll(model.getAlbums());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("la durata deve essere un valore numerico!");
    	}
    	
    }

    @FXML
    void doEstraiSet(ActionEvent event) {
    	Album a1=this.cmbA1.getValue();
    	String dTotS=this.txtX.getText();
    	
    	if(a1==null) {
    		txtResult.appendText("Specificare durata totale");
    		return;
    	}
    	try {
    		double dTot=Double.parseDouble(dTotS);
    		Set<Album> ottimi =model.ricercaSetMassimo(a1,dTot);
    	}catch(NumberFormatException e) {
    		
    	}
    	

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnComponente != null : "fx:id=\"btnComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSet != null : "fx:id=\"btnSet\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
