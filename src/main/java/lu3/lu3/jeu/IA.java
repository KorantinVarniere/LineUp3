package lu3.lu3.jeu;

import java.io.Serializable;
import java.util.Random;

public class IA extends Joueur implements Serializable{
	private static String message="";
	public IA(String nom, int nbPions) {
		super(nom, nbPions);
	}
	public static String getMessage() {
		return message;
	}
	/**
	 * Place de façon aléatoire un pion du joueur dans le plateau
	 * 
	 * @param p
	 */
	public String placement(Plateau p) {
		Random rand = new Random();
		int couche = -1;
		int index = -1;
		do {
			couche = rand.nextInt(p.getNB_COUCHE()) + 1;
			index = rand.nextInt(p.getNB_SOMMETS_PAR_COUCHE()) + 1;
		} while (p.sommetPosition(couche, index).getProprietairePion() != null);
		p.placePion(this, couche, index);
		message="IA place un pion position "+couche+" , "+index;
		if(Main.regarderTeleportation(this, p, couche, index)) {
			message += "\n IA a été téléporter";
		}
		this.PionPasPoser--;
		return message;
	}

	/**
	 * La IA dépose un pion si elle en a en réserve sinon elle déplace un pion
	 * 
	 * @param plateau
	 */
	public void jouer(Plateau plateau) {
		if (this.PionPasPoser > 0) {
			this.placement(plateau);
			return;
		}
		this.deplacementRandom(plateau);
	}

	/**
	 * Déplacement d'un pion de façon aléatoire
	 * 
	 * @param plateau
	 */
	public void deplacementRandom(Plateau plateau) {
		Random rand = new Random();
		int couche = -1;
		int index = -1;
		Sommet s;
		do {
			couche = rand.nextInt(plateau.getNB_COUCHE()) + 1;
			index = rand.nextInt(plateau.getNB_SOMMETS_PAR_COUCHE()) + 1;
			s = plateau.sommetPosition(couche, index);
		} while (s.getProprietairePion() == null || s.getProprietairePion() != this
				|| this.pionJoueur(s).pionBloque(plateau, s));
		int nv_num_couche = -1;
		int nv_num_index = -1;
		do {
			nv_num_couche = rand.nextInt(plateau.getNB_COUCHE()) + 1;
			nv_num_index = rand.nextInt(plateau.getNB_SOMMETS_PAR_COUCHE()) + 1;
		} while (plateau.sommetPosition(nv_num_couche, nv_num_index).getProprietairePion() != null
				|| !plateau.getADJACENTS().get(plateau.sommetPosition(couche, index))
						.contains(plateau.sommetPosition(nv_num_couche, nv_num_index)));

		plateau.supressionPion(this, couche, index);
		plateau.placePion(this, nv_num_couche, nv_num_index);
		message = "IA a déplacer le pion " + couche +" , "+index+" vers "+nv_num_couche+" , "+nv_num_index;
	}

	
}