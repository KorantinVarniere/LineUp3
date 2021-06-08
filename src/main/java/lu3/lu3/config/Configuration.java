/**
 * @author VARNIERE Korantin
 * @author SABINE Am�lie
 * @author RAHIRE Lukas
 * @author VANROBAEYS Hugo
 * 
 * @version 2021-05-07
 */
package lu3.lu3.config;

import java.util.ResourceBundle;

import lu3.lu3.jeu.Joueur;
import lu3.lu3.jeu.Placement;
import lu3.lu3.jeu.Plateau;

/**
 * Classe de configuration, contenant les variables n�cessaires au fonctionnement de l'application.
 */
public abstract class Configuration {
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("lu3.lu3.config.config");

	/**
	 * Constantes.
	 */
	public static final String TITRE_JEU = bundle.getString("titre_jeu");
	public static final int LARGEUR_ECRAN = Integer.parseInt(bundle.getString("largeur_ecran"));
	public static final int HAUTEUR_ECRAN = Integer.parseInt(bundle.getString("hauteur_ecran"));
	
	/**
	 * Variables.
	 */
	public static boolean pleinEcran = bundle.getString("plein_ecran").equals("oui");
	public static boolean redimensionnable = bundle.getString("redimensionnable").equals("oui");
	
	public static int nbJoueurs = 0;
	public static int nbCouches = 0;
	public static int nbCotes = 0;
	public static int nbPions = 0;
	public static Placement placement = Placement.MANUEL;
	
	public static String nameJ1 = "";
	public static String nameJ2 = "IA  ";
	
	public static boolean enMenu = true;
	public static boolean enJeu = false;
	
	public static boolean graphisme = false;
	
	public static boolean chargerPartie = false;
	public static Plateau plateauSauvegarde = null;
	public static Joueur[] joueursSauvegarde = new Joueur[2];
	
}
