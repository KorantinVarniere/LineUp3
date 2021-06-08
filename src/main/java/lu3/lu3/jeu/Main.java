/**
 * @version 19-05-2021
 */

package lu3.lu3.jeu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lu3.lu3.config.Configuration;

public class Main {

	/**
	 * JEU EN VERSION TEXTE
	 */

	public static void main(String[] args) {
		System.out.println("\tLine Up 3\n");
		Scanner scan = new Scanner(System.in);
		String choixSauvegarde = "";
		do {
			System.out.println(" ==============================");
			System.out.println(" 1. Commencer une nouvelle partie  \n 2. Continuer la partie sauvegardé");
			System.out.println(" ==============================");
			choixSauvegarde = scan.nextLine();
		} while (!choixSauvegarde.equals("1") && !choixSauvegarde.equals("2"));
		Plateau plateau;
		Joueur[] joueurs;
		if (choixSauvegarde.equals("2")) {
			ArrayList<Object> objects = (ArrayList<Object>) Sauvegarde.read();
			if (objects == null) {
				Main.main(args);
			}
			plateau = (Plateau) objects.get(0);
			joueurs = (Joueur[]) objects.get(1);
		} else {
			String choixJoueur = "";
			do {
				System.out.println(" ============Joueurs============");
				System.out.println(" 1. 1 joueur  \n 2. 2 joueurs");
				System.out.println(" ==============================");
				choixJoueur = scan.nextLine();
			} while (!choixJoueur.equals("1") && !choixJoueur.equals("2"));

			joueurs = new Joueur[2];
			System.out.print("Nom du joueur 1 :");
			String nom = scan.nextLine();
			joueurs[0] = creer_joueurs(nom);
			if (choixJoueur.equals("2")) {
				System.out.print("Nom du joueur 2 :");
				nom = scan.nextLine();
				joueurs[1] = creer_joueurs(nom);
				System.out.println("\nBienvenue " + joueurs[0] + " et " + joueurs[1] + " !\n\n");
			} else {
				System.out.println("\nBienvenue " + joueurs[0]);
				String choixIA = "";
				do {
					System.out.println(" ============Niveaux============");
					System.out.println(" 1. Simple  \n 2. Moyen");
					System.out.println(" ==============================");
					choixIA = scan.nextLine();
				} while (!choixIA.equals("1") && !choixIA.equals("2"));
				if (choixIA.equals("1")) {
					joueurs[1] = new IA("IA", 0);
				} else {
					joueurs[1] = new IAMoyen("IA", 0);
				}
			}
			
			String nbCotes = "";
			do {
				System.out.print(" Nombre de côtés (3 ou 4) :");
				nbCotes = scan.nextLine();
			} while (!nbCotes.equals("3") && !nbCotes.equals("4"));
			plateau = new Plateau(Integer.parseInt(nbCotes), null);
			
			String choixPlacement = "";
			do {
				System.out.println(" ============Placement============");
				System.out.println(" 1. Manuellement  \n 2. Automatiquement");
				System.out.println(" =================================");
				choixPlacement = scan.nextLine();
			} while (!choixPlacement.equals("1") && !choixPlacement.equals("2"));
			int nbPions;
			do {
				System.out.print(
						" Nombre pions souhaité entre 3 et  " + plateau.getNB_SOMMETS_PAR_COUCHE() + " inclus : ");
				nbPions = scan.nextInt();
			} while (3 > nbPions || nbPions > plateau.getNB_SOMMETS_PAR_COUCHE());
			joueurs[0].PionPasPoser = nbPions;
			joueurs[1].PionPasPoser = nbPions;
			if (choixPlacement.equals("1")) {
				for (int i = 0; i < 3 * 2; i++) {
					System.out.println("\n" + plateau);
					if (joueurs[i % 2].getClass().equals(IAMoyen.class)) {
						((IAMoyen) joueurs[i % 2]).placement(plateau, joueurs[1 - i % 2]);
						System.out.println(("aaaaa"));
						System.out.println(((IAMoyen) joueurs[i % 2]).getMessage());
					} else if (joueurs[i % 2].getClass().equals(IA.class)) {
						((IA) joueurs[i % 2]).placement(plateau);
						System.out.println(((IA) joueurs[i % 2]).getMessage());
					} else {
						int[] indices = plateau.placer1Pion1Joueur(joueurs[i % 2], joueurs[1 - i % 2]);
						if (plateau.sommetGagnant(plateau.sommetPosition(indices[0], indices[1]), joueurs[i % 2])) {
							plateau.supressionPion(joueurs[i % 2], joueurs[1 - i % 2]);
						}
					}
				}
			} else {
				for (int i = 0; i < 3 * 2; i++) {
					plateau.placementAutomatique(joueurs[i % 2]);
				}
			}

		}

		System.out.println("\n" + plateau);
		ArrayList<Pion> pionBloquer = new ArrayList<Pion>();
		ArrayList<Integer> suppPionBloquer = new ArrayList<Integer>();
		while (!jeuFini(joueurs, joueurs[plateau.tours % 2], plateau)) {

			System.out.println("\t______________________" + joueurs[plateau.tours % 2] + " "
					+ joueurs[plateau.tours % 2].getnbPions() + " Pions   +" + joueurs[plateau.tours % 2].PionPasPoser
					+ " Pions à disposition _________________\n");

			int choixAction = 0;
			while (choixAction < 1 || choixAction > 4) {
				choixAction = demandeIndice(
						"Que voulez vous faire? \n 1.Poser un pion \n 2.Poser un piège \n 3.Déplacer un pion \n 4.Enregistrer la partie et quitter \n");
				if (choixAction == 1) { // poser pion
					if (joueurs[plateau.tours % 2].PionPasPoser == 0) {
						System.err.println("Désolé mais vous avez déjà positionné tous vos pions\n");
						choixAction = 0;
					} else {
						int[] indices = plateau.placer1Pion1Joueur(joueurs[plateau.tours % 2],
								joueurs[1 - plateau.tours % 2]);
						regarderTeleportationGagnant(joueurs[plateau.tours % 2], joueurs[1 - plateau.tours % 2],
								indices[0], indices[1], plateau);

					}
				} else if (choixAction == 2) { // poser piege
					if (joueurs[plateau.tours % 2].getmonPiege() != null) {
						System.err.println("Désolé mais vous avez déjà un piège en votre posession\n");
						choixAction = 0;
					} else {
						int choixPiege = 0;
						while (choixPiege <= 0 || choixPiege > 3) {
							choixPiege = demandeIndice(
									"Choix piege \n 1.bloqueur qui bloque un pion adverse \n 2.téléporteur qui téléportera un pion quand il sera sur le téléporteur \n 3.Retour \n  ");
							if (choixPiege == 1) {
								if (joueurs[plateau.tours % 2].getHistoriqueIndice(0) == true) {
									System.err.println("Désolé vous avez déja utilisé un bloqueur\n");
									choixPiege = 0;
								} else {
									joueurs[plateau.tours % 2].setHistoriqueTrue(0);
									int num_couche = -1;
									int num_index = -1;
									do {
										num_couche = demandeIndice(
												"\nSaisisez la couche où vous voulez positionner votre bloqueur  :   ");
										num_index = demandeIndice(
												"Saisisez l'index où vous voulez positionner votre bloqueur :  ");
									} while (MauvaisIndicePosition(num_couche, num_index, plateau)
											|| plateau.sommetPosition(num_couche, num_index)
													.getProprietairePion() == null
											|| plateau.sommetPosition(num_couche, num_index).getProprietairePion()
													.equals(joueurs[plateau.tours % 2]));
									plateau.placementPiege(joueurs[plateau.tours % 2], num_couche, num_index,
											TypePiege.valueOf("BLOQUEUR"));
									pionBloquer.add(joueurs[1 - plateau.tours % 2]
											.pionJoueur(plateau.sommetPosition(num_couche, num_index)));
									suppPionBloquer.add(plateau.tours + 2);
								}
							} else if (choixPiege == 2) {
								if (joueurs[plateau.tours % 2].getHistoriqueIndice(1) == true) {
									System.err.println("Désolé vous avez déja utilisé un téléporteur");
									choixPiege = 0;
								} else {
									joueurs[plateau.tours % 2].setHistoriqueTrue(1);
									int num_couche = -1;
									int num_index = -1;
									do {
										num_couche = demandeIndice(
												"\nSaisisez la couche où vous voulez positionner votre téléporteur  :   ");
										num_index = demandeIndice(
												"Saisisez l'index où vous voulez positionner votre téléporteur :  ");
										if (plateau.sommetPosition(num_couche, num_index)
												.getProprietairePion() != null) {
											System.err.println("Il faut mettre le téléporteur sur une case vide");
										}
									} while (MauvaisIndicePosition(num_couche, num_index, plateau) || plateau
											.sommetPosition(num_couche, num_index).getProprietairePion() != null);
									plateau.placementPiege(joueurs[plateau.tours % 2], num_couche, num_index,
											TypePiege.valueOf("TELEPORTEUR"));
								}
							} else if (choixPiege == 3) {
								choixAction = 0;
							}
						}
					}
				} else if (choixAction == 3) {
					int num_couche = -1;
					int num_index = -1;
					do {
						num_couche = demandeIndice("\nSaisisez la couche du pion que vous voulez déplacer : ");
						num_index = demandeIndice("Saisisez l'index du pion que vous voulez déplacer : ");
						if (pionBloquer.contains(
								joueurs[plateau.tours % 2].pionJoueur(plateau.sommetPosition(num_couche, num_index)))) {
							System.err.println(
									"Désolé mais ce pion a été bloqué par votre adversaire vous ne pouvez pas le bouger pendant ce tour.");
						}
					} while (MauvaisIndicePosition(num_couche, num_index, plateau)
							|| plateau.sommetPosition(num_couche, num_index).getProprietairePion() == null
							|| !plateau.sommetPosition(num_couche, num_index).getProprietairePion()
									.equals(joueurs[plateau.tours % 2])
							|| pionBloquer.contains(joueurs[plateau.tours % 2]
									.pionJoueur(plateau.sommetPosition(num_couche, num_index))));
					int nv_num_couche = -1;
					int nv_num_index = -1;
					do {
						nv_num_couche = demandeIndice("\nSaisisez la couche où vous voulez vous déplacer : ");
						nv_num_index = demandeIndice("Saisisez l'index du où vous voulez vous déplacer : ");
					} while (MauvaisIndicePosition(nv_num_couche, nv_num_index, plateau)
							|| plateau.sommetPosition(nv_num_couche, nv_num_index).getProprietairePion() != null
							|| !plateau.getADJACENTS()
									.get(plateau.sommetPosition(num_couche, num_index).sommetSauvegarde(plateau))
									.contains(plateau.sommetPosition(nv_num_couche, nv_num_index)));
					plateau.supressionPion(joueurs[plateau.tours % 2], num_couche, num_index);
					plateau.placePion(joueurs[plateau.tours % 2], nv_num_couche, nv_num_index);
					regarderTeleportationGagnant(joueurs[plateau.tours % 2], joueurs[1 - plateau.tours % 2],
							nv_num_couche, nv_num_index, plateau);
				} else if (choixAction == 4) {
					Sauvegarde.useSauvegarde(plateau, joueurs);
					System.out.println("Sauvegarde réussi");
					System.exit(0);
				}
			}
			System.out.println("\n" + plateau);
			if (suppPionBloquer.contains(plateau.tours)) {
				pionBloquer.remove(0);
				joueurs[plateau.tours % 2].setmonPiege(null);
			}
			// scan.close();
			plateau.tours++;
			if (joueurs[1].getClass().equals(IAMoyen.class)) {
				((IAMoyen) joueurs[1]).jouer(plateau, joueurs[0], pionBloquer);
				System.out
						.println("\t______________________" + joueurs[1] + " " + joueurs[plateau.tours % 2].getnbPions()
								+ " Pions   +" + joueurs[1].PionPasPoser + " Pions à disposition _________________\n");
				System.out.println(plateau);
				System.out.println(((IAMoyen) joueurs[1]).getMessage());
				plateau.tours++;
			} else if (joueurs[1].getClass().equals(IA.class)) {
				((IA) joueurs[1]).jouer(plateau);
				System.out
						.println("\t______________________" + joueurs[1] + " " + joueurs[plateau.tours % 2].getnbPions()
								+ " Pions   +" + joueurs[1].PionPasPoser + " Pions à disposition _________________\n");
				System.out.println(plateau);
				System.out.println(((IA) joueurs[1]).getMessage());
				plateau.tours++;
			}
		}
		if (joueurs[0].getnbPions() == 2 || joueurs[0].joueurBloque(plateau)) {
			System.out.println("Bravo " + joueurs[1] + " tu as gagné");
		} else {
			System.out.println("Bravo " + joueurs[0] + " tu as gagné");
		}
	}

	/**
	 * JEU EN VERSION GRAPHIQUE
	 */

	private static double x, y;
	private static Canvas canvas = new Canvas(1100, 800);
	private static GraphicsContext context = canvas.getGraphicsContext2D();
	static Joueur[] joueurs;
	static String tour;
	static boolean supprimerPion = false;
	static String supression = "";
	static String finDuJeu = "";
	static String savedGame = "";
	static String deplacementInterdit = "";
	static int nbTours = 0;
	static boolean estSommet = false;
	static boolean estAdjacent = false;
	static boolean tropTot = false;
	static Pion tmp = null;
	static Plateau plateau;
	static boolean jeuFini = false;
	static boolean tourJ1 = true;

	private static Joueur creer_joueurs(String nom) {
		String name;
		if (nom.length() > 4) {
			name = nom.substring(0, 4);
		} else {
			name = nom;
			while (name.length() != 4) {
				name += " ";
			}
		}
		Joueur joueur = new Joueur(name, Configuration.nbPions);
		return joueur;
	}

	/**
	 * IHM
	 */
	public static void start(Stage stage) {
		if (Configuration.chargerPartie) {
			Configuration.chargerPartie = false;
			plateau = Configuration.plateauSauvegarde;
			joueurs = Configuration.joueursSauvegarde;
			nbTours = plateau.tours;
			tourJ1 = (nbTours%2 == 0);
			jeuFini = false;
			finDuJeu = "";
		} else {
			initialisationValeurs();
			joueurs = new Joueur[2];
			plateau = new Plateau(Configuration.nbCotes, 3, context);
			joueurs[0] = creer_joueurs(Configuration.nameJ1);
			joueurs[1] = creer_joueurs(Configuration.nameJ2);
		}
		if (tourJ1) {
			tour = (joueurs[0].toString() + ", à toi de jouer !");
		} else {
			tour = (joueurs[1].toString() + ", à toi de jouer !");
		}
		VBox main = new VBox();
		main.setStyle("-fx-background-color: #f1f3f2;\n" + "		-fx-border-color: black;\n"
				+ "    -fx-border-width: 2;\n" + "    -fx-border-style: solid;");

		Label closeWindow = new Label("X");
		Label minimizeWindow = new Label("—");
		Label lineUp = new Label("LineUp 3");
		lineUp.setStyle("-fx-font-size: 16px;");
		lineUp.setPadding(new Insets(0, 446, 0, 0));
		HBox topContainer = new HBox();
		topContainer.setAlignment(Pos.CENTER_RIGHT);
		topContainer.setSpacing(15);
		topContainer.getChildren().addAll(lineUp, minimizeWindow, closeWindow);
		topContainer.setPadding(new Insets(10, 15, 10, 0));
		topContainer.setStyle("-fx-background-color: #D0D0D0;");

		initialiserPions();
		affichage(canvas, joueurs[0].getPions(), joueurs[1].getPions());

		/**
		 * DRAG AND DROP DES PIONS
		 */
		canvas.setOnMousePressed(pressed -> {
			savedGame = "";
			/**
			 * suppression d'un pion
			 */
			if (supprimerPion) {
				if (tourJ1) {
					for (int i = joueurs[1].getPions().size() - 1; i >= 0; i--) {
						if (joueurs[1].getPions().get(i).getPosition() != null
								&& joueurs[1].getPions().get(i).contains(pressed.getX(), pressed.getY())) {
							plateau.supressionPion(joueurs[1], joueurs[1].getPions().get(i).getPosition().getCOUCHE(),
									joueurs[1].getPions().get(i).getPosition().getINDEX());
							supprimerPion = false;
							supression = "";
							tmp = null;
							nbTours++;
							if (jeuFini(joueurs, joueurs[0], plateau)) {
								jeuFini = true;
								finDuJeu = "Bravo " + joueurs[0] + " tu as gagné !";
								return;
							}
							tourJ1 = !tourJ1;
							tour = (joueurs[1].toString() + ", à toi de jouer !");
							return;
						}
					}
				} else {
					for (int i = joueurs[0].getPions().size() - 1; i >= 0; i--) {
						if (joueurs[0].getPions().get(i).getPosition() != null
								&& joueurs[0].getPions().get(i).contains(pressed.getX(), pressed.getY())) {
							plateau.supressionPion(joueurs[0], joueurs[0].getPions().get(i).getPosition().getCOUCHE(),
									joueurs[0].getPions().get(i).getPosition().getINDEX());
							supprimerPion = false;
							supression = "";
							tmp = null;
							nbTours++;
							if (jeuFini(joueurs, joueurs[0], plateau)) {
								jeuFini = true;
								finDuJeu = "Bravo " + joueurs[1] + " tu as gagné !";
								affichage(canvas, joueurs[0].getPions(), joueurs[1].getPions());
								return;
							}
							tourJ1 = !tourJ1;
							tour = (joueurs[0].toString() + ", à toi de jouer !");
							affichage(canvas, joueurs[0].getPions(), joueurs[1].getPions());
							return;
						}
					}
				}
			}

			// tmp prend la valeur du pion courant
			if (tourJ1) {
				for (int i = joueurs[0].getPions().size() - 1; i > -1; i--) {
					if (joueurs[0].getPions().get(i).contains(pressed.getX(), pressed.getY())) {
						tmp = joueurs[0].getPions().get(i);
						estSommet = false;
						estAdjacent = false;
						return;
					}
				}
			} else {
				for (int i = joueurs[1].getPions().size() - 1; i > -1; i--) {
					if (joueurs[1].getPions().get(i).contains(pressed.getX(), pressed.getY())) {
						tmp = joueurs[1].getPions().get(i);
						estSommet = false;
						estAdjacent = false;
						return;
					}
				}
			}

			// si on clique autre part que sur un pion
			tmp = null;

			if ((pressed.getX() >= 944) && (pressed.getX() <= 944 + 150) && (pressed.getY() >= 10)
					&& (pressed.getY() <= 10 + 34)) {
				savedGame = "partie sauvegardée";
				plateau.tours = nbTours;
				Sauvegarde.useSauvegarde(plateau, joueurs);
			}
			// retour au menu ou quitter le jeu à la fin d'une partie
			if (jeuFini) {
				if ((pressed.getX() >= 490) && (pressed.getX() <= 490 + 200) && (pressed.getY() >= 730)
						&& (pressed.getY() <= 730 + 50)) {
					Menu menu = new Menu();
					menu.start(new Stage());
					stage.close();
				} else if ((pressed.getX() >= 710) && (pressed.getX() <= 710 + 200) && (pressed.getY() >= 730)
						&& (pressed.getY() <= 730 + 50)) {
					stage.close();
				}
			}
		});
		canvas.setOnMouseDragged(drag -> {
			if (!jeuFini) {
				affichage(canvas, joueurs[0].getPions(), joueurs[1].getPions());
				if (tmp != null && !supprimerPion) {
					tmp.setX(drag.getX() - 25);
					tmp.setY(drag.getY() - 25);
				}
			}
		});
		canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				tropTot = false;
				if (tmp != null) {
					for (int i = 0; i < plateau.getSOMMETS().size(); i++) {
						// si on veut placer le pion sur un sommet
						if (tmp.contains(plateau.getSOMMETS().get(i).getX(), plateau.getSOMMETS().get(i).getY())
								&& plateau.getSOMMETS().get(i).getProprietairePion() == null) {// si il n'y a pas
																								// d'occupants sur le
																								// sommet
							if (tmp.getPosition() == null) {
								tmp.setX(plateau.getSOMMETS().get(i).getX() - 25);
								tmp.setY(plateau.getSOMMETS().get(i).getY() - 25);
								tmp.setInitialX(plateau.getSOMMETS().get(i).getX() - 25);
								tmp.setInitialY(plateau.getSOMMETS().get(i).getY() - 25);
								estAdjacent = true;
							} else if (plateau.getADJACENTS().get(tmp.getPosition())
									.contains(plateau.getSOMMETS().get(i))) {
								if (nbTours >= 6) {
									tmp.setX(plateau.getSOMMETS().get(i).getX() - 25);
									tmp.setY(plateau.getSOMMETS().get(i).getY() - 25);
									tmp.setInitialX(plateau.getSOMMETS().get(i).getX() - 25);
									tmp.setInitialY(plateau.getSOMMETS().get(i).getY() - 25);
									estAdjacent = true;
								} else {
									tropTot = true;
								}
							}
							estSommet = true;
						}

					}
					if (!estAdjacent) {
						if (estSommet) {
							if (tropTot) {
								deplacementInterdit = "Placez au moins 3 pions sur le plateau avant d'en déplacer d'autres.";
							} else {
								deplacementInterdit = "Déplacement non autorisé, les pions bougent d'un sommet à un autre.";
							}
						}
						tmp.setX(tmp.getInitialX());
						tmp.setY(tmp.getInitialY());
					}
				}
				verifierSommets();
				if (tmp != null && tmp.getPosition() != null && estAdjacent
						&& plateau.sommetGagnant(tmp.getPosition())) {
					deplacementInterdit = "";
					int i = 0;
					if (tourJ1) {
						while (i < joueurs[0].getsommetsOccupes().size() && !supprimerPion) {
							supression = joueurs[0] + ", choisis un pion de " + joueurs[1] + " à supprimer";
							supprimerPion = true;
							i++;
						}
					} else {
						i = 0;
						while (i < joueurs[1].getsommetsOccupes().size() && !supprimerPion) {
							supression = joueurs[1] + ", choisis un pion de " + joueurs[0] + " à supprimer";
							supprimerPion = true;
							i++;
						}
					}
				}
				if (tmp != null && !supprimerPion && estAdjacent) {
					tourJ1 = !tourJ1;
					nbTours++;
					supression = "";
					deplacementInterdit = "";
					if (tourJ1) {
						tour = (joueurs[0].toString() + ", à toi de jouer !");
					} else {
						tour = (joueurs[1].toString() + ", à toi de jouer !");
					}
				}
				affichage(canvas, joueurs[0].getPions(), joueurs[1].getPions());
			}
		});
		/**
		 * FIN DRAG AND DROP PIONS
		 */

		main.getChildren().addAll(topContainer, canvas);

		// ne pas pouvoir réduire, ni agrandir la fenêtre
		// pour pouvoir déplacer la fenêtre
		topContainer.setOnMousePressed(event -> {
			x = event.getSceneX();
			y = event.getSceneY();
		});
		topContainer.setOnMouseDragged(event -> {
			stage.setX(event.getScreenX() - x);
			stage.setY(event.getScreenY() - y);
		});
		Scene scene = new Scene(main);
		stage.setResizable(false);
		stage.setTitle("LineUp 3");
		stage.setScene(scene);
		stage.show();

		EventHandler<MouseEvent> stageMinimize = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				stage.setIconified(true);
			}
		};
		EventHandler<MouseEvent> stageClose = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				stage.close();
			}
		};
		closeWindow.addEventFilter(MouseEvent.MOUSE_CLICKED, stageClose);
		minimizeWindow.addEventFilter(MouseEvent.MOUSE_CLICKED, stageMinimize);
	}

	/**
	 * réinitialisation des valeurs, pour une nouvelle partie
	 */
	private static void initialisationValeurs() {
		nbTours = 0;
		jeuFini = false;
		tourJ1 = true;
		finDuJeu = "";
	}

	/**
	 * mise à jour l'affichage du canvas
	 * 
	 * @param can
	 * @param listJ1
	 * @param listJ2
	 */
	public static void affichage(Canvas can, List<Pion> listJ1, List<Pion> listJ2) {
		context.clearRect(0, 0, can.getWidth(), can.getHeight());
		plateau.affichageAretes(context);
		plateau.affichageSommets(context);
		// délimitation des zones des 2 joueurs à gauche du canvas
		context.setStroke(Color.BLACK);
		context.setLineWidth(2);
		context.strokeLine(300, 0, 300, 800);
		context.strokeLine(0, 400, 300, 400);
		context.strokeLine(0, 1, 1100, 1);
		context.setFont(Font.font("Verdana", 20));
		context.fillText(joueurs[0].toString(), 10, 30);
		context.fillText(joueurs[1].toString(), 10, 430);
		if(!jeuFini) {
			context.fillText(tour, 310, 30);
		}
		context.fillText(supression, 320, 730);
		context.setFill(Color.RED);
		context.fillText(deplacementInterdit, 320, 780);
		context.setStroke(null);
		for (Pion p : listJ1) {
			context.setFill(Color.web("118c8b"));
			context.fillOval(p.getX(), p.getY(), 50, 50);
			context.setStroke(Color.BLACK);
			context.strokeOval(p.getX(), p.getY(), 50, 50);
		}
		for (Pion p : listJ2) {
			context.setFill(Color.web("f14d49"));
			context.fillOval(p.getX(), p.getY(), 50, 50);
			context.setStroke(Color.BLACK);
			context.strokeOval(p.getX(), p.getY(), 50, 50);
		}
		context.setFill(Color.web("A0A0A0"));
		context.fillRect(944, 10, 150, 34);
		context.setStroke(Color.BLACK);
		context.strokeRect(944, 10, 150, 34);
		context.setFill(Color.BLACK);
		context.fillText("Sauvegarder", 954, 34);
		context.setFill(Color.GREEN);
		context.fillText(savedGame, 600, 785);
		
		if(jeuFini) {
			context.setFill(Color.web("A0A0A0"));
			context.fillRect(490, 730, 200, 50);
			context.setStroke(Color.BLACK);
			context.strokeRect(490, 730, 200, 50);
			context.setFill(Color.BLACK);
			context.fillText("Retour au menu", 510, 762);
			
			context.setFill(Color.web("A0A0A0"));
			context.fillRect(710, 730, 200, 50);
			context.setStroke(Color.BLACK);
			context.strokeRect(710, 730, 200, 50);
			context.setFill(Color.BLACK);
			context.fillText("Quitter", 770, 762);
			context.fillText(finDuJeu, 310, 30);
		}
	}
	
	/**
	 * mise à jour les occupants pour chaque sommet
	 */
	public static void verifierSommets() {
		joueurs[0].getsommetsOccupes().clear();
		joueurs[1].getsommetsOccupes().clear();
		for (int i = 0; i < plateau.getSOMMETS().size(); i++) {
			plateau.getSOMMETS().get(i).getAllOccupant().clear();// a changer pour ne pas supprimer les pieges

			for (int j = 0; j < joueurs[0].getPions().size()/* nb pions à recuperer du menu */; j++) {
				if (joueurs[0].getPions().get(j).contains(plateau.getSOMMETS().get(i).getX(),
						plateau.getSOMMETS().get(i).getY())) {
					plateau.getSOMMETS().get(i).setOccupant(joueurs[0].getPions().get(j));
					joueurs[0].getsommetsOccupes().add(plateau.getSOMMETS().get(i));
					joueurs[0].getPions().get(j).setPosition(plateau.getSOMMETS().get(i));
				}
			}
			for (int j = 0; j < joueurs[1].getPions().size()/* nb pions à recuperer du menu */; j++) {

				if (joueurs[1].getPions().get(j).contains(plateau.getSOMMETS().get(i).getX(),
						plateau.getSOMMETS().get(i).getY())) {
					plateau.getSOMMETS().get(i).setOccupant(joueurs[1].getPions().get(j));
					joueurs[1].getsommetsOccupes().add(plateau.getSOMMETS().get(i));
					joueurs[1].getPions().get(j).setPosition(plateau.getSOMMETS().get(i));
				}
			}
		}
	}

	/**
	 * affichage des pions à l'état initial pour commencer la partie
	 */
	public static void initialiserPions() {
		double taillePion = 50;
		context.setLineWidth(2);
		int nbLigne;
		for (int i = 0; i < Configuration.nbPions; i++) {
			nbLigne = (i / 4) + 1;
			joueurs[0].getPions().add(
					new Pion(joueurs[0], 70 * ((i % 4) + 0.6) - (taillePion / 2), 70 * nbLigne - (taillePion / 2)));
			joueurs[1].getPions().add(new Pion(joueurs[1], 70 * ((i % 4) + 0.6) - (taillePion / 2),
					70 * nbLigne + 400 - (taillePion / 2)));
		}
	}

	public static int demandeIndice(String phrase) {
		Scanner scan = new Scanner(System.in);
		System.out.print(phrase);
		String indice = scan.nextLine();
		return Integer.parseInt(indice);
	}

	public static boolean MauvaisIndicePosition(int num_couche, int num_index, Plateau plateau) {
		if (num_couche < 0 || num_couche > plateau.getNB_COUCHE() || num_index < 0
				|| num_index > plateau.getNB_SOMMETS_PAR_COUCHE())
			System.out.println("mauvais indice");
		return num_couche < 0 || num_couche > plateau.getNB_COUCHE() || num_index < 0
				|| num_index > plateau.getNB_SOMMETS_PAR_COUCHE();
	}

	/**
	 * Regarde si le jeu est fini
	 * 
	 * @param joueurs
	 * @param courant
	 * @param plateau
	 * @return
	 */
	private static boolean jeuFini(Joueur[] joueurs, Joueur courant, Plateau plateau) {
		if (joueurs[0].getnbPions() < 3 && joueurs[0].PionPasPoser == 0)
			return true;
		if (joueurs[1].getnbPions() < 3 && joueurs[1].PionPasPoser == 0)
			return true;
		if (courant.joueurBloque(plateau))
			return true;
		return false;
	}

	/**
	 * Regarde la téléportation on regarde si c'est un sommet gagnant
	 * 
	 * @param courant
	 * @param adverse
	 * @param num_couche
	 * @param num_index
	 * @param plateau
	 */
	private static void regarderTeleportationGagnant(Joueur courant, Joueur adverse, int num_couche, int num_index,
			Plateau plateau) {

		Piege piegeCase = plateau.sommetPosition(num_couche, num_index).getPiege();
		regarderTeleportation(courant, plateau, num_couche, num_index);
		if (plateau.sommetGagnant(plateau.sommetPosition(num_couche, num_index), courant)) {
			plateau.supressionPion(courant, adverse);
		}
	}

	/**
	 * Si le joueur courant est tombé sur un téléporteur il se déplace de façon
	 * aléatoire
	 * 
	 * @param courant
	 * @param plateau
	 * @param num_couche
	 * @param num_index
	 * @return boolean
	 */
	public static boolean regarderTeleportation(Joueur courant, Plateau plateau, int num_couche, int num_index) {
		Piege piegeCase = plateau.sommetPosition(num_couche, num_index).getPiege();
		if (piegeCase != null && TypePiege.TELEPORTEUR.equals(piegeCase.getTYPE())) {
			System.out.println("\nTeleportation : vous etiez sur un téléporteur\n");
			plateau.supressionPion(courant, num_couche, num_index);
			plateau.placementAutomatique(courant);
			piegeCase.getPROPRIETAIRE().setmonPiege(null);
			return true;
		}
		return false;
	}
}