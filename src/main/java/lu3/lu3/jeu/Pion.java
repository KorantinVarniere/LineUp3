package lu3.lu3.jeu;

import java.io.Serializable;
import java.util.ArrayList;




/**
 * @author Korantin Varnière
 * @author Lukas Rahire
 * @author Amélie Sabine
 * @author Hugo Vanrobaeys
 *
 * @version 2021-03-30
*/
//package jeu;
public class Pion extends Jouable implements Serializable {
		//lignes nécessaires à l'IHM de celle-ci jusqu'à la ligne 63
		// positions du pion dans le canvas
		double posx = 0, posy = 0;
		double initialX = 0, initialY = 0;
		double width = 50, height = 50;

		//constructeur pour l'IHM
		public Pion(Joueur proprietaire, double posx, double posy) {
			super(proprietaire, null);
			this.posx = posx;
			this.posy = posy;
			this.initialX = posx;
			this.initialY = posy;
		}

		// retourne true si x et y sont dans la zone du pion ( pour le drag and drop )
		public boolean contains(double x, double y) {
			return (x >= posx) && (x <= posx + width) && (y >= posy) && (y <= posy + height);
		}

		public double getX() {
			return posx;
		}

		public double getY() {
			return posy;
		}
		
		public double getInitialX() {
			return this.initialX;
		}
		
		public double getInitialY() {
			return this.initialY;
		}
		
		public void setInitialX(double x) {
			this.initialX = x;
		}
		
		public void setInitialY(double y) {
			this.initialY = y;
		}
		
		public void setX(double x) {
			posx = x;
		}

		public void setY(double y) {
			posy = y;	
		}
		
		public void setWidth(double width) {
			this.width = width;
		}

		public void setHeight(double width) {
			this.width = width;
		}

		public double getWidth() {
			return width;
		}
		
		public double getHeight() {
			return height;
		}
		
// FIN IHM
		
	public Pion(Joueur proprietaire, Sommet position) {
		super(proprietaire, position);
		this.placer(position);
	}

	@Override
	public String toString() {
		return "Pion "+ getPosition() +" ";
	}
	/**
	 *Regarde si le pion situé sur le sommet en paramètre est bloqué 
	 * @param plateau
	 * @param sommet
	 * @return
	 */
	public boolean pionBloque (Plateau plateau , Sommet sommet) {
		ArrayList<Sommet> listSommet = plateau.getADJACENTS().get(sommet.sommetSauvegarde(plateau));
		for(Sommet s:listSommet ) {
			if(s.getProprietairePion()==null)
				return false;
		}
		return true;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pion other = (Pion) obj;
		if (this.getPROPRIETAIRE() == null) {
			if (other.getPROPRIETAIRE() != null)
				return false;
		} else if (!this.getPROPRIETAIRE().equals(other.getPROPRIETAIRE()))
			return false;
		if (this.getPosition() == null) {
			if (other.getPosition() != null)
				return false;
		} else if (!this.getPosition().equals(other.getPosition()))
			return false;
		return true;
	}

}