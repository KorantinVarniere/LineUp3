package lu3.lu3.jeu;

import java.io.Serializable;

/**
 *@author Korantin Varnière
 * @author Lukas Rahire
 * @author Amélie Sabine
 * @author Hugo Vanrobaeys
 *
 * @version 2021-03-30
*/
public abstract class Jouable implements Serializable {
	/**
	 * Propriétaire du jouable (pion ou piege).
	 */
	private final Joueur PROPRIETAIRE;
  
	@Override
	public String toString() {
		return " " + PROPRIETAIRE + " ";
	}

	/**
	 * Position du jouable (null par défaut)
	 */
	private Sommet position;
	/**
	 * Constructeur d'un jouable
	 * @param proprietaire
	 * @param position
	 */
	public Jouable(Joueur proprietaire, Sommet position) {
		this.PROPRIETAIRE = proprietaire;
		this.position = position;
	}

	/**
	 * Constructeur d'un jouable sans position
	 * @param proprietaire
	 */
	public Jouable(Joueur proprietaire) {
		this(proprietaire, null);
	}

	/**
	 * Placement d'un jouable.
	 * @param sommet
	 */
	public void placer(Sommet sommet) {
		if(sommet==null)
			return;
		if(sommet.getAllOccupant().contains(this))
			return;
		this.position = sommet;
		sommet.setOccupant(this);
	}
	
	public void retirer (Sommet sommet) {
		sommet.retirerJouable(this);
		this.position=null;
		
	}

	  public Joueur getPROPRIETAIRE(){
	    return this.PROPRIETAIRE;
	  }
	
	  public Sommet getPosition(){
	    return this.position;
	  }
	
	  public void setPosition(Sommet p){
	    this.position = p;
	  }
	  /**
	   * méthode equals
	   */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jouable other = (Jouable) obj;
		if (PROPRIETAIRE == null) {
			if (other.PROPRIETAIRE != null)
				return false;
		} else if (!PROPRIETAIRE.equals(other.PROPRIETAIRE))
			return false;
		return true;
	}
	  

}