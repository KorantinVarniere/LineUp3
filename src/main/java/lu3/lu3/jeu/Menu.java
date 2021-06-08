package lu3.lu3.jeu;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lu3.lu3.config.Configuration;

public class Menu extends Application implements EventHandler<ActionEvent> {
	ComboBox<String> choixNbCotes = new ComboBox<String>();
	ComboBox<String> choixNbPions = new ComboBox<String>();
	class ChangeListenerForCB implements ChangeListener<String>{

  		@Override
  		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
  			if(choixNbCotes.getSelectionModel().getSelectedIndex()!=-1) {
  				choixNbPions.getItems().clear();
  				for(int i = 3; i <=(choixNbCotes.getSelectionModel().getSelectedIndex()+3)*2; i++) {
  					choixNbPions.getItems().add((i)+ " pions");
  				}
  			}
  			
  		}
      	
      }
	private double x, y;
      public void start(Stage stage) {
    	// Contenu du BP Top
    	Label closeWindow = new Label("X");
    	Label minimizeWindow = new Label("—");
    	Label cancelAction = new Label("↵");
    	Label title = new Label("LineUp 3");
        title.setTextFill(Color.web("white"));
    	title.setStyle("-fx-font-size: 45px;");
    	Label subtitle = new Label("Menu Principal");
    	subtitle.setTextFill(Color.web("white"));
    	subtitle.setStyle("-fx-font-size: 20px;");
    	HBox closeToTheRight = new HBox();
    	closeToTheRight.setAlignment(Pos.CENTER_RIGHT);
    	closeToTheRight.setSpacing(15);
    	closeToTheRight.getChildren().addAll(minimizeWindow, closeWindow);
    	VBox.setMargin(closeToTheRight, new Insets(10,10,0,0));
    	VBox topContainer = new VBox();
    	topContainer.setPrefSize(600, 100);
    	topContainer.setAlignment(Pos.TOP_CENTER);
    	topContainer.getChildren().addAll(closeToTheRight, title, subtitle);
    	
    	HBox topContainerBP = new HBox();
    	topContainerBP.setAlignment(Pos.CENTER);
    	topContainerBP.setPrefSize(600, 100);
    	topContainerBP.setStyle("-fx-background-color: #808080;");
    	topContainerBP.getChildren().add(topContainer);
    	topContainerBP.setPadding(new Insets(0,0,10,0));
    	
    	// Contenu du BP Middle Menu de sélection des joueurs
    	Label selection = new Label("Que voulez-vous faire ?");
    	selection.setPadding(new Insets(40,0,55,0));
    	selection.setStyle("-fx-font-size: 18px;");
    	
    	Button saveButton = new Button("Reprendre une partie sauvegardée");
    	saveButton.setPrefSize(280, 34);
    	saveButton.setOnAction(e -> {
    		ArrayList<Object> objects = (ArrayList<Object>) Sauvegarde.read();
			Configuration.chargerPartie = true;
			Configuration.plateauSauvegarde = (Plateau) objects.get(0);
			Configuration.joueursSauvegarde = (Joueur[]) objects.get(1);
			
			stage.close();
			Main.start(stage);
    	});
        HBox saveBox = new HBox();
        saveBox.setPadding(new Insets(0,0,40,0));
        saveBox.setAlignment(Pos.CENTER);
        saveBox.getChildren().add(saveButton);
    	
        Button selectionButton = new Button("Commencer une nouvelle partie");
        selectionButton.setPrefSize(280, 34);
    	
    	// Contenu du BP Middle 2 Joueurs
    	Label playerOne = new Label("Nom du joueur 1");
    	playerOne.setPrefSize(305, 25);
    	playerOne.setPadding(new Insets(10,0,10,5));
    	playerOne.setStyle("-fx-font-size: 18px;");
    	TextField inputNameOne = new TextField();
    	inputNameOne.setPromptText("Nom du joueur 1");
    	inputNameOne.setPrefSize(440, 24);
    	inputNameOne.setStyle("-fx-border-radius: 20;");
    	Label playerTwo = new Label("Nom du joueur 2");
    	playerTwo.setPrefSize(305, 25);
    	playerTwo.setPadding(new Insets(10,0,10,5));
    	playerTwo.setStyle("-fx-font-size: 18px;");
    	TextField inputNameTwo = new TextField();
    	inputNameTwo.setPromptText("Nom du joueur 2");
    	inputNameTwo.setPrefSize(440, 24);
    	inputNameTwo.setStyle("-fx-border-radius: 20;");
    	VBox name1 = new VBox();
    	name1.setAlignment(Pos.CENTER);
    	name1.setPrefSize(300, 71);
    	name1.setPadding(new Insets(18,0,0,90));
    	name1.getChildren().addAll(playerOne, inputNameOne);
    	VBox name2 = new VBox();
    	name2.setAlignment(Pos.CENTER);
    	name2.setPrefSize(300, 71);
    	name2.setPadding(new Insets(18,90,0,0));
    	name2.getChildren().addAll(playerTwo, inputNameTwo);
    	HBox namesContainer = new HBox();
    	namesContainer.getChildren().addAll(name1, name2);
    	namesContainer.setSpacing(60);
    	namesContainer.setPadding(new Insets(0,20,0,0));
    	
    	// Contenu du BP Middle ( commun aux scenes 1 ou 2 joueurs )
    	Label config = new Label("Configuration du plateau :");
    	config.setPrefSize(385, 50);
    	config.setPadding(new Insets(25,0,15,0));
    	config.setStyle("-fx-font-size: 18px;");
        
        choixNbCotes.getItems().addAll(" 3 côtés ", " 4 côtés ", " 5 côtés ", " 6 côtés ", " 7 côtés ", " 8 côtés ");
        choixNbCotes.setPromptText("côtés par couche");
        
        
        choixNbPions.setPromptText("nombre de pions");
        choixNbPions.getItems().add("3 pions");
        choixNbCotes.valueProperty().addListener(new ChangeListenerForCB());
        
        HBox containerComboBoxes = new HBox();
        containerComboBoxes.setPadding(new Insets(0,20,35,0));
        containerComboBoxes.setSpacing(30);
        containerComboBoxes.getChildren().addAll(choixNbCotes, choixNbPions);
        containerComboBoxes.setAlignment(Pos.CENTER);

        Label verification = new Label();
        verification.setTextFill(Color.web("red"));
        Button playButton = new Button("Jouer");
        VBox.setMargin(playButton, new Insets(0,0,3,0));
        playButton.setPrefSize(88, 34);
        // action event
        
        // action event
        EventHandler<ActionEvent> buttonPressed2Players = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if(inputNameOne.getText().isBlank()||inputNameTwo.getText().isBlank()) {
                	verification.setText("Veuillez renseigner un nom pour chaque joueur");
                }else if(choixNbCotes.getSelectionModel().getSelectedIndex()==-1) {
                	verification.setText("Veuillez choisir le nombre de côtés du plateau");
                }else if(choixNbPions.getSelectionModel().getSelectedIndex()==-1) {
                	verification.setText("Veuillez choisir le nombre de pions pour chaque joueur");
            	}else {
                	verification.setText("");
                	Configuration.nameJ1 = inputNameOne.getText();
                	Configuration.nameJ2 = inputNameTwo.getText();
                	Configuration.nbJoueurs = 2;
                	Configuration.nbCotes = Integer.parseInt(choixNbCotes.getSelectionModel().getSelectedItem().charAt(1) + "");
                	try {
                		Configuration.nbPions = Integer.parseInt(choixNbPions.getSelectionModel().getSelectedItem().substring(0, 2));
                	} catch (Exception ex) {
                		Configuration.nbPions = Integer.parseInt(choixNbPions.getSelectionModel().getSelectedItem().charAt(0) + "");
                	}
                	Configuration.nbCouches = 3;
                	
                	stage.close();
                	Main.start(stage);
                }
            }
        };
        
    	VBox centerContainerBP = new VBox();
    	centerContainerBP.setAlignment(Pos.TOP_CENTER);
    	centerContainerBP.getChildren().addAll(selection, saveBox, selectionButton);
    	
    	
    	// BorderPane
    	BorderPane root = new BorderPane(centerContainerBP, topContainerBP, null, null, null);
    	root.setPrefSize(600, 400);
    	//root.setStyle("-fx-background-color: #440052");
    	//ne pas pouvoir réduire, ni agrandir la fenêtre
		stage.initStyle(StageStyle.UNDECORATED);
		//pour pouvoir déplacer la fenêtre
		root.setOnMousePressed(event ->{
			x = event.getSceneX();
			y = event.getSceneY();
		});
		root.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - x);
			stage.setY(event.getScreenY() - y);
		});
    	
    	Scene scene = new Scene(root);
        stage.setTitle(" Test ");
        stage.setScene(scene);
        stage.show();
        
        // action event
        EventHandler<ActionEvent> selectionButtonPressed = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
            	centerContainerBP.getChildren().clear();
                	centerContainerBP.getChildren().addAll(namesContainer, config,containerComboBoxes, playButton, verification);
                	closeToTheRight.getChildren().clear();
                    closeToTheRight.getChildren().addAll(cancelAction, minimizeWindow, closeWindow);
                    playButton.setOnAction(buttonPressed2Players);
            }
            	
        };
  
        // when button is pressed
        selectionButton.setOnAction(selectionButtonPressed);
        
        //Creating the mouse event handler 
    	EventHandler<MouseEvent> stageMinimize = new EventHandler<MouseEvent>() { 
    	   @Override 
    	   public void handle(MouseEvent e) { 
    		   stage.setIconified(true);
    	   } 
    	};   
    	//Creating the mouse event handler 
    	EventHandler<MouseEvent> cancelStage = new EventHandler<MouseEvent>() { 
    	   @Override 
    	   public void handle(MouseEvent e) { 
    		   closeToTheRight.getChildren().clear();
    		   centerContainerBP.getChildren().clear();
               closeToTheRight.getChildren().addAll(minimizeWindow, closeWindow);
               centerContainerBP.getChildren().addAll(selection, saveBox, selectionButton);
    	   } 
    	};   

    	EventHandler<MouseEvent> stageClose = new EventHandler<MouseEvent>() { 
     	   @Override 
     	   public void handle(MouseEvent e) { 
     		   stage.close();
     	   } 
     	};   
    	//Adding event Filter 
    	closeWindow.addEventFilter(MouseEvent.MOUSE_CLICKED, stageClose);
    	minimizeWindow.addEventFilter(MouseEvent.MOUSE_CLICKED, stageMinimize);
    	
    	cancelAction.addEventFilter(MouseEvent.MOUSE_CLICKED, cancelStage);
      }
	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		Application.launch(args);
	}
      
     
}