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
import java.util.List;

/**
 * Classe Joueur, représentant un joueur du jeu.
 */
public class Joueur implements Serializable {

	/**
	 * Compteur automatique de l'ID courant.
	 */

	/**
	 * Nom du joueur.
	 */
	private final String NOM;
  /**
  * Pions des joueur entre 3 et 9
  */
  private List<Pion> pions ;
	/**
  * Le joueur peut avoir un piège
  */
  private Piege monPiege;
	/**
	 * Liste de sommets occupés par le joueur.
	 */
	private List<Sommet> sommetsOccupes;
	
	

	/**
	 * Nombre de pions à disposition.
	 */
	private int nbPions;
	/**
	 * Nombre de pions déjà placé 
	 */
	public int PionPasPoser;
	/**
	 * historique des piege déjà utilisé
	 */
	private boolean[] historiquePiege;
	/**
	 * Constructeur du joueur.
	 * 
	 * @param nom
	 * @param nbPions
	 */
	public Joueur(String nom, int nbPions) {
		String name;
		if (nom.length() > 4) {
			name = nom.substring(0, 4);
		} else {
			name = nom;
			while (name.length() != 4) {
				name += " ";
			}
		}
		this.NOM = name;
		this.nbPions = nbPions;
		this.sommetsOccupes = new ArrayList<Sommet>();
		this.pions = new ArrayList<Pion>();
		this.monPiege = null;
		this.historiquePiege=new boolean[2];
		this.PionPasPoser=0;
	}

	/**
	 * Retourne le joueur sous forme textuel.
	 * @return joueur
	 */
	public String toString() {
		return this.NOM;
	}
	public List<Pion> getPions() {
		return pions;
	}

	public List<Sommet> getsommetsOccupes() {
		return sommetsOccupes;
	}
	
	public int getnbPions() {
		return nbPions;
	}
	

	public Piege getmonPiege() {
		return monPiege;
	}

	public void setmonPiege(Piege monPiege) {
		this.monPiege = monPiege;
	}

	/**
	 * ajoute au joueur courant le pion et sommet passé en paramètre
	 * @param p
	 * @param s
	 */
	public void addPionSommet(Pion p,Sommet s) {
		this.pions.add(p);
		this.nbPions++;
		this.sommetsOccupes.add(s);
	}
	/**
	 * fonction ne fonctionne pas encore très bien
	 * suprimme au joueur le pion et le sommet
	 * @param p
	 * @param s
	 */
	public void removePionSommet(Pion p,Sommet s) {
		this.sommetsOccupes.remove(s);
		this.pions.remove(p);
		this.nbPions--;
		
	}
	
	/**
	 * 
	 * @param plateau
	 * @return
	 */
	public 	boolean joueurBloque (Plateau plateau) {
		for(Pion p:this.pions) {
			if(p.getPosition()==null || !p.pionBloque(plateau, p.getPosition())) {
				return false;
			}
		}
		return true;
	}
	/**
	 * retourne le pion du joueur au sommet indiqué
	 * @param s
	 * @return
	 */
	
	public Pion pionJoueur(Sommet s) {
		if(!this.pions.contains(new Pion(this,s))) return null;
		return this.pions.get(this.pions.indexOf(new Pion(this,s)));
	}

	public boolean getHistoriqueIndice(int i) {
		return this.historiquePiege[i];
	}

	public void setHistoriqueTrue(int i) {
		this.historiquePiege[i]=true;
	}

	
	
}
