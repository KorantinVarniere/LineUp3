package lu3.lu3.jeu;

import java.io.Serializable;
import java.util.List;

public class Arete implements Serializable {
	private List<Sommet> sommets;
	
	public Arete(List<Sommet> sommets) {
		this.sommets = sommets;
	}
	
	public List<Sommet> getSommets() {
		return sommets;
	}

	public void setAretes(List<Sommet> sommets) {
		this.sommets = sommets;
	}
}
