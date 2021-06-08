package lu3.lu3.jeu;

import java.io.Serializable;

/**
 * @author Korantin Varnière
 * @author Lukas Rahire
 * @author Amélie Sabine
 * @author Hugo Vanrobaeys
 *
 * @version 2021-03-30
*/
//package jeu;
public class Piege extends Jouable implements Serializable {
	private final TypePiege TYPE;
	public Piege(Joueur proprietaire, Sommet position, TypePiege type) {
		super(proprietaire, position);
		position.setOccupant(this);
		this.TYPE=type;
	}
	public TypePiege getTYPE() {
		return TYPE;
	}
	
}
