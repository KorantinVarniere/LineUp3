package lu3.lu3.jeu;
/**
 * @author Korantin Varnière
 * @author Lukas Rahire
 * @author Amélie Sabine
 * @author Hugo Vanrobaeys
 *
 * @version 2021-03-25
 */


import java.io.Serializable;
import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Set;

 

/**
 * Classe Sommet, représentant un sommet du plateau, sur lequel peuvent se placer les pions.
 */
public class Sommet implements Serializable {

	/**
	 * Couche du sommet dans le plateau.
	 */
	private final int COUCHE;
	/**
	 * Index du sommet dans la couche.
	 */
	private final int INDEX;
	/**
	 * Le sommet est occupé par une pièce jouable
	 */
	private ArrayList<Jouable> occupant;
	
	//nécessaire pour l'IHM, coordonnées de chaque sommet dans le canvas
		private double x;
		private double y;
		private Set<Arete> aretes;
	
	/**
	 * Constructeur du sommet.
	 * 
	 * @param couche
	 * @param index
	 */
	public Sommet(int couche, int index) {
		this.COUCHE = couche;
		this.INDEX = index;
		this.occupant = new ArrayList<Jouable>();
	}

  public void setOccupant (Jouable occupant){
    this.occupant.add(occupant);
  }
  
	public int getCOUCHE() {
		return COUCHE;
	}

	public int getINDEX() {
		return INDEX;
	}
	public Sommet sommetSauvegarde(Plateau p) {
		return p.sommetPosition(this.getCOUCHE(),this.getINDEX());
	}
	/**
	 * Retourne le nom du joueur propriétaire du pion qu'il y a sur le sommet ou null si il n'y a pas de pion
	 * @return
	 */
	public Joueur getProprietairePion() {
		if(this.occupant.size()==0)return null;
		int i=0;
		while(i<this.occupant.size()&& this.occupant.get(i).getClass().equals(Piege.class)) {
			i++;
		}
	    if(i==this.occupant.size()) return null;
		return this.occupant.get(i).getPROPRIETAIRE();
	}
	/**
	 * retourne le piege positionné au sommmet courant
	 * @return
	 */
	public Piege getPiege() {
		if(this.occupant.size()==0)return null;
		int i=0;
		while(i<this.occupant.size()&& !this.occupant.get(i).getClass().equals(Piege.class)) {
			i++;
		}
	    if(i==this.occupant.size()) return null;
		return (Piege) this.occupant.get(i);
	}
	
	/**
	 * retourne tous les jouable sur le sommet
	 * @return
	 */
	public ArrayList<Jouable> getAllOccupant(){
		return this.occupant;
	}
	public void retirerJouable (Jouable j) {
		this.occupant.remove(j);
	}
	/**
	 * toString : Affichage du sommet en mode textuel.
	 * 
	 * @return sommet en mode textuel.
	 */
	@Override
	public String toString() {
		return "(" + this.COUCHE + ", " + this.INDEX + ", " + this.getProprietairePion()+ ")";
	}
	
	// IHM ajout ( jusqu'à la fin du fichier )
	
	public void setXY(double x, double y) {
  		this.x = x;
  		this.y = y;
  	}
  	public Set<Arete> getAretes() {
		return aretes;
	}

	public void setAretes(Set<Arete> aretes) {
		this.aretes = aretes;
	}
  	public double getX() {
  		return this.x;
  	}
  	public double getY() {
  		return this.y;
  	}
  	public static Arete genereArete(Sommet s1, Sommet s2) {
		return new Arete(Arrays.asList(s1,s2));
	}
}
