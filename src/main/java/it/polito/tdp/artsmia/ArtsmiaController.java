package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.artsmia.model.Arco;
import it.polito.tdp.artsmia.model.Artist;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {
	
	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private Button btnArtistiConnessi;

    @FXML
    private Button btnCalcolaPercorso;

    @FXML
    private ComboBox<String> boxRuolo;

    @FXML
    private TextField txtArtista;

    @FXML
    private TextArea txtResult;

    @FXML
    void doArtistiConnessi(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola artisti connessi");
    	
    	txtResult.setText("");
    	
    	List <Arco> archi = model.getArchi();
    	
    	if (archi == null) {
    		txtResult.setText("Prima crea il grafico");
    		return;
    	}
    	
    	for (Arco a : archi) {
    		txtResult.appendText(a+"\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Calcola percorso");
    	
    	String raw = txtArtista.getText();
    	int n;
    	try {
    		n = Integer.parseInt(raw);
    		
    	}
    	catch (NumberFormatException e) {
    		txtResult.appendText("Il parametro inserito non è un numero\n");
    		return;
    	}
    	
    	List <Artist> percorso = model.getPercorso(n);
    	
    	if (percorso == null) {
    	    txtResult.setText("Artista non presente");
    	    return;
    	}
    	
    	txtResult.setText("Trovato percorso di "+percorso.size()+" elementi:\n");
    	
    	for (Artist a : percorso) {
    		txtResult.appendText(a+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Crea grafo");
    	
    	if (boxRuolo.getItems().isEmpty()) {
    	    txtResult.setText("Nessun ruolo trovato");
    	    return;
    	}
    	if (boxRuolo.getValue() == null) {
    		txtResult.setText("Devi scegliere un opzione");
    	    return;
    	}
    	
    	model.creaGrafo(boxRuolo.getValue());
    	
    }

    public void setModel(Model model) {
    	this.model = model;
    	boxRuolo.getItems().addAll(model.getRuoli());
    }

    
    @FXML
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnArtistiConnessi != null : "fx:id=\"btnArtistiConnessi\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert boxRuolo != null : "fx:id=\"boxRuolo\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtArtista != null : "fx:id=\"txtArtista\" was not injected: check your FXML file 'Artsmia.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";
    }
}
