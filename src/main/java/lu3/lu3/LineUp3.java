/**
 * @author VARNIERE Korantin
 * @author SABINE Am�lie
 * @author RAHIRE Lukas
 * @author VANROBAEYS Hugo
 * 
 * @version 2021-05-07
 */
package lu3.lu3;

import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import lu3.lu3.config.Configuration;
import lu3.lu3.jeu.Joueur;
import lu3.lu3.jeu.Main;
import lu3.lu3.jeu.Menu;

/**
 * Classe de l'application, permettant de lancer le logiciel.
 */
public class LineUp3 extends Application {

	public static double x;
	public static double y;
	public static Canvas canvas = new Canvas(1100, 800);
	public static GraphicsContext context = canvas.getGraphicsContext2D();
	public static Joueur[] joueurs;

	/**
	 * M�thode principale, appelant le lancement de l'application.
	 * 
	 * @param args - Arguments pass�s au programme.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * M�thode lan�ant l'application Line Up 3.
	 *
	 * @param stage - Stage de l'application.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		Scanner scan = new Scanner(System.in);

		System.out.println("\t\tLine Up 3 :");

		String mode = "";
		while (!mode.equals("1") && !mode.equals("2")) {
			System.out.println();

			System.out.println("1. Mode Graphique");
			System.out.println("2. Mode Textuel");

			System.out.println();

			mode = scan.nextLine();
			
			if (!mode.equals("1") && !mode.equals("2")) {
				System.err.println("Saisie invalide !");
			}
		}
		
		if (mode.equals("1")) {
			Configuration.graphisme = true;
			Menu menu = new Menu();
			menu.start(stage);
		} else {
			Main.main(null);
		}
	}
}
