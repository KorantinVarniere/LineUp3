package lu3.lu3.jeu;

import java.io.Serializable;
import java.util.ArrayList;

public class IAMoyen extends IA implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String message="";
	
	public IAMoyen(String nom, int nbPions) {
		super(nom, nbPions);
	}
	
	public static String getMessage() {
		return message;
	}

	/**
	 * Apelle la fonction placeGagnant mais si elle n'a rien fait on
	 * place un pion à coté d'un pion déjà existant. Sinon on fait un
	 * placement random
	 * 
	 * @param plateau
	 * @param adversaire
	 */
	public void placement(Plateau plateau, Joueur adversaire) {
		this.PionPasPoser--;
		int indice = 0;
		while (indice < this.getPions().size()) {
			if(this.placeGagnant(plateau, adversaire)) {
				message += " IA vient d'être téléporter";
				return;
			}
			Pion pion = this.getPions().get(indice);
			for (Sommet sommetAdjacent : plateau.getADJACENTS().get(pion.getPosition())) {
				if (sommetAdjacent.getProprietairePion() == null) {
					plateau.placePion(this, sommetAdjacent.getCOUCHE(), sommetAdjacent.getINDEX());
					message = "IA place un pion "+ sommetAdjacent.getCOUCHE()+" , "+sommetAdjacent.getINDEX();
					return;
				}
			}
			indice++;
		}
		message=super.placement(plateau);
		this.PionPasPoser++;
	}
	/**
	 * Si l'IA à bientôt un triplet gagnant il le place. Sinon on regarde si
	 * l'adversaire à bientot un triplet gagnant et on le bloque en mettant un pion.
	 * @param plateau
	 * @param adversaire
	 * @return
	 */
	private boolean placeGagnant(Plateau plateau, Joueur adversaire) {
		Sommet gagnantIA = presqueGagnant(plateau, this);
		Sommet gagnantAdverse = presqueGagnant(plateau, adversaire);
		if (gagnantIA != null) {
			plateau.placePion(this, gagnantIA.getCOUCHE(), gagnantIA.getINDEX());
			message="IA place un pion position "+gagnantIA.getCOUCHE()+" , "+gagnantIA.getINDEX();
			if(Main.regarderTeleportation(this, plateau, gagnantIA.getCOUCHE(), gagnantIA.getINDEX())) {
				message += "\n IA a été téléporter";
			}
			else{
				Pion pionAdverse = adversaire.getPions().get(0);
				plateau.supressionPion(adversaire, pionAdverse.getPosition().getCOUCHE(),pionAdverse.getPosition().getINDEX());
				message+="\nIA a fait un triplet gagnant et viens d'effacer un de tes pions";
			}
			return true;
		}
		if (gagnantAdverse != null) {
			plateau.placePion(this, gagnantAdverse.getCOUCHE(), gagnantAdverse.getINDEX());
			message="IA place un pion position "+gagnantAdverse.getCOUCHE()+" , "+gagnantAdverse.getINDEX();
			if(Main.regarderTeleportation(this, plateau, gagnantAdverse.getCOUCHE(), gagnantAdverse.getINDEX())) {
				message += "\n IA a été téléporter";
			}
			return true;
		}
		return false;
	}
	/**
	 * On fait appelle à la fonction placeGagnant. Si elle ne fait rien alors on regarde 
	 * si l'adverse à bientôt un triplet gagnant dans ce cas on pause un téléporteur (vérification piège avant)
	 * @param plateau
	 * @param adverse
	 */
	public void jouer(Plateau plateau, Joueur adverse,ArrayList<Pion> pionBloque) {
		if(this.PionPasPoser>0 && placeGagnant(plateau, adverse)) {
			this.PionPasPoser--;
			return;
		}
		Sommet gagnantAdverse = presqueGagnant(plateau, adverse);
		if( this.getmonPiege() == null && this.getHistoriqueIndice(1)==false && gagnantAdverse!=null) {
			message="IA a placé un piège";
			plateau.placementPiege(this, gagnantAdverse.getCOUCHE(), gagnantAdverse.getINDEX(),TypePiege.TELEPORTEUR);
			return;
		}
		ArrayList <Sommet> deplacer= this.deplacementAdjacents(plateau,pionBloque);
		if(deplacer!=null) {
			message="IA a déplacer le pion "+deplacer.get(0).getCOUCHE()+" , "+deplacer.get(0).getINDEX()+" vers "+deplacer.get(1).getCOUCHE()+" , "+deplacer.get(1).getINDEX();
			plateau.supressionPion(this, deplacer.get(0).getCOUCHE(),deplacer.get(0).getINDEX());
			plateau.placePion(this, deplacer.get(1).getCOUCHE(), deplacer.get(1).getINDEX());
			Main.regarderTeleportation(this, plateau, deplacer.get(1).getCOUCHE(), deplacer.get(1).getINDEX());
			return;
		}
		super.deplacementRandom(plateau);
		
	}
	/**
	 * Déplace si possible un pion vers son autre pion le plus près
	 * @param plateau
	 * @param pionBloque
	 * @return
	 */
	private ArrayList <Sommet> deplacementAdjacents (Plateau plateau,ArrayList<Pion> pionBloque) {
		ArrayList <Sommet> resultat=new ArrayList<Sommet>();
		for (Sommet s : this.getsommetsOccupes()) {
			if(! pionBloque.contains(s)) {
				for(Sommet sommetAdjacent : plateau.getADJACENTS().get(s.sommetSauvegarde(plateau))) {
					if(sommetAdjacent.getProprietairePion()==null) {
						for(Sommet bonSommet: plateau.getADJACENTS().get(sommetAdjacent.sommetSauvegarde(plateau))) {
							if(this.equals(bonSommet.getProprietairePion())) {
								resultat.add(s);
								resultat.add(sommetAdjacent);
								return resultat;
							}
								
						}
						
					}
				}
			}
		}
		return null;
	}

	/**
	 * regarde si le joueur passé en paramètre peut avoir un sommet gagnant si oui
	 * alors il retourne le sommet où le joueur doit se placer sinon retourne null
	 * 
	 * @param plateau
	 * @param joueur
	 * @return
	 */
	private static Sommet presqueGagnant(Plateau plateau, Joueur joueur) {
		for (Sommet s : plateau.getSOMMETS()) {
			if (s.getProprietairePion() == null && plateau.sommetGagnant(s, joueur))
				return s;
		}
		return null;
	}
}
