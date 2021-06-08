package lu3.lu3.jeu;

import java.io.Serializable;

/**
 * @author Korantin Varnière
 * @author Lukas Rahire
 * @author Amélie Sabine
 * @author Hugo Vanrobaeys
 *
 * @version 2021-03-25
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lu3.lu3.config.Configuration;

/**
 * Classe Plateau, représentant un plateu de jeu.
 */
public class Plateau implements Serializable {

	/**
	 * Nombre de côtés du plateau.
	 */
	private final int NB_COTE;

	/**
	 * Nombre de couches du plateau.
	 */
	private final int NB_COUCHE;

	/**
	 * Nombre de sommets par couche.
	 */
	private final int NB_SOMMETS_PAR_COUCHE;

	/**
	 * Liste des sommets du plateau.
	 */
	final List<Sommet> SOMMETS;

	/**
	 * Map des sommets adjacents.
	 */
	private final HashMap<Sommet, ArrayList<Sommet>> ADJACENTS;

	/**
	 * Set des arêtes du plateau.
	 */
	private final Set<Arete> ARETES;

	/**
	 * getter du set d'arêtes du plateau.
	 * 
	 * @return
	 */
	public Set<Arete> getARETES() {
		return this.ARETES;
	}

	/**
	 * getter des sommets du plateau.
	 */
	public List<Sommet> getSOMMETS() {
		return this.SOMMETS;
	}
	/**
	 * enregistre le nombre de tours de la partie
	 */
	public int tours;

	/**
	 * Constructeur du plateau complètement paramétré.
	 * 
	 * @param nb_cote
	 * @param nb_couche
	 */
	public Plateau(int nb_cote, int nb_couche, GraphicsContext context) {
		this.NB_COTE = nb_cote;
		this.NB_COUCHE = nb_couche;
		this.NB_SOMMETS_PAR_COUCHE = 2 * this.NB_COTE;
		this.SOMMETS = new ArrayList<Sommet>(this.NB_SOMMETS_PAR_COUCHE * this.NB_COUCHE);
		this.ajouterSommets();
		this.ARETES = this.genererAretes();
		this.ADJACENTS = new HashMap<Sommet, ArrayList<Sommet>>();
		this.ajouterSommetsAdjacents();
		if (Configuration.graphisme) {
			affichageAretes(context);
			affichageSommets(context);
		}
	}

	public int getNB_SOMMETS_PAR_COUCHE() {
		return NB_SOMMETS_PAR_COUCHE;
	}

	public int getNB_COTE() {
		return NB_COTE;
	}

	public int getNB_COUCHE() {
		return NB_COUCHE;
	}

	public HashMap<Sommet, ArrayList<Sommet>> getADJACENTS() {
		return ADJACENTS;
	}

	/**
	 * Constructeur du plateau en fonction du nombre de côté. Nombre de couche par
	 * défaut : 3.
	 * 
	 * @param nb_cote
	 */
	public Plateau(int nb_cote, GraphicsContext context) {
		this(nb_cote, 3, context);
	}

	/**
	 * Constructeur du plateau par défaut. Nombre de couche par défaut : 3. Nombre
	 * de côté par défaut : 4;
	 */
	public Plateau(GraphicsContext context) {
		this(4, 3, context);
	}

	public Plateau() {
		this(4, 3, null);
	}
	
	

	/**
	 * Ajout des sommets au plateau.
	 */
	private void ajouterSommets() {
		double radius = 350;
		double xcenter = 700;// canvas width / 2
		double ycenter = 400;// canvas height / 2
		double angle = 360 / NB_COTE;
		double alpha = angle * Math.PI / 180;
		for (int couche = this.NB_COUCHE; couche > 0; couche--) {
			for (int index = 1; index <= this.NB_SOMMETS_PAR_COUCHE; index += 2) {

				double layerSize = radius - ((couche - 1) * (350 / NB_COUCHE));
				double y = ycenter + (layerSize * Math.cos(alpha * (Math.ceil(index / 2) + 0.5)));
				double x = xcenter + (layerSize * Math.sin(alpha * (Math.ceil(index / 2) + 0.5)));

				Sommet s1 = new Sommet(couche, index);
				this.SOMMETS.add(s1);
				s1.setXY(x, y);

				// milieu d'un côté
				double y1 = ycenter + (layerSize * Math.cos(alpha * (Math.ceil(index / 2) + 1.5)));
				double x1 = xcenter + (layerSize * Math.sin(alpha * (Math.ceil(index / 2) + 1.5)));

				Sommet s2 = new Sommet(couche, index + 1);
				this.SOMMETS.add(s2);
				s2.setXY(x - (x - x1) / 2, y - (y - y1) / 2);  

			}
		}
	}

	/**
	 * ajoute les sommets adjacents dans la HashMap
	 */
	private void ajouterSommetsAdjacents() {
		for (Sommet s : this.SOMMETS) {
			this.ADJACENTS.put(s, this.listeSommetAdjacents(s));
		}
	}

	// version graphique des sommets
	void affichageSommets(GraphicsContext gc) {
		double tailleSommet = 40;
		Iterator<Sommet> sommetsIterator = SOMMETS.iterator();

		// couleur des sommets
		gc.setFill(Color.web("1b1924"));

		// itérateur sur la liste de sommets
		while (sommetsIterator.hasNext()) {
			Sommet s = sommetsIterator.next();
			gc.fillOval(s.getX() - (tailleSommet / 2), s.getY() - (tailleSommet / 2), tailleSommet, tailleSommet);
		}
	}

	// version graphique des arêtes
	void affichageAretes(GraphicsContext gc) {
		// couleur des arêtes
		gc.setStroke(Color.web("A0A0A0"));
		// épaisseur des arêtes
		gc.setLineWidth(15);
		this.getARETES().forEach(arete -> {
			Sommet s1 = arete.getSommets().get(0);
			Sommet s2 = arete.getSommets().get(1);
			gc.strokeLine(s1.getX(), s1.getY(), s2.getX(), s2.getY());
		});
		gc.setStroke(null);
	}

	/**
	 * Recherche quels sont les sommets adjacents a celui passé en paramètre
	 */
	private ArrayList<Sommet> listeSommetAdjacents(Sommet s) {
		ArrayList<Sommet> resultat = new ArrayList<Sommet>();
		int precedent = s.getINDEX() - 1;
		if (s.getINDEX() == 1) {
			precedent = this.NB_SOMMETS_PAR_COUCHE;
		}
		int suivant = s.getINDEX() + 1;
		if (s.getINDEX() == this.NB_SOMMETS_PAR_COUCHE) {
			suivant = 1;
		}
		for (Sommet other : this.SOMMETS) {
			if (other.getCOUCHE() == s.getCOUCHE() && (other.getINDEX() == precedent || other.getINDEX() == suivant)) {
				resultat.add(other);
			}
			if (s.getINDEX() % 2 == 0 && s.getINDEX() == other.getINDEX()
					&& (other.getCOUCHE() == s.getCOUCHE() + 1 || other.getCOUCHE() == s.getCOUCHE() - 1)) {
				resultat.add(other);
			}
		}
		return resultat;
	}

	// générer les arêtes
	public Set<Arete> genererAretes() {
		Set<Arete> aretes = new HashSet<Arete>();
		for (Sommet s : this.SOMMETS) {
			if (s.getAretes() == null) {
				aretes.addAll(genereAretesParSommet(s));
			} else {
				aretes.addAll(s.getAretes());
			}
		}
		return aretes;
	}

	// génère les arêtes d'un Sommet
	public Set<Arete> genereAretesParSommet(Sommet s1) {
		Set<Arete> aretes = new HashSet<Arete>();
		// if(Plateau.listeSommetAdjacents(this) == null) this.adjacents =
		// this.generateAdjacents(board);
		for (Sommet s : listeSommetAdjacents(s1)) {
			aretes.add(Sommet.genereArete(s1, s));
		}
		s1.setAretes(aretes);
		;
		return aretes;
	}
	
	/**
	 * Regarde si le sommet pass� en paramettre est dans un triplet gagnant
	 * 
	 * @param s
	 * @return
	 */
	// TODO fonctionne seulement si il y a 3 couches
	public boolean sommetGagnant(Sommet s) {
		int index = s.getINDEX();
		int couche = s.getCOUCHE();
		if (index % 2 == 0 && s.getProprietairePion() != null) {
			if (this.gagnantEntre(s)) {
				return true;
			}
			if (this.gagnantDessous(s)) {
				return true;
			}if(this.gagnantDessus(s)) {
				return true;
			}
			int indexApres = s.getINDEX() + 1;
			if (s.getINDEX() == this.NB_SOMMETS_PAR_COUCHE) {
				indexApres = 1;
			}
			if (s.getProprietairePion().equals(this.sommetPosition(couche, index - 1).getProprietairePion())
					&& s.getProprietairePion().equals(this.sommetPosition(couche, indexApres).getProprietairePion())) {
				return true;
			}

		} else if (s.getProprietairePion() != null)

		{
			int indexAvant = s.getINDEX() - 1;
			if (s.getINDEX() == 1) {
				indexAvant = this.NB_SOMMETS_PAR_COUCHE;
			}
			if (s.getProprietairePion().equals(this.sommetPosition(couche, indexAvant).getProprietairePion()) && s
					.getProprietairePion().equals(this.sommetPosition(couche, indexAvant - 1).getProprietairePion())) {
				return true;
			}
			int indexApres = s.getINDEX() + 1;
			int indexApresApres = s.getINDEX() + 2;
			if (s.getINDEX() == this.NB_SOMMETS_PAR_COUCHE) {
				indexApres = 1;
			} else if (s.getINDEX() == this.NB_SOMMETS_PAR_COUCHE - 1) {
				indexApresApres = 1;
			}
			if (s.getProprietairePion().equals(this.sommetPosition(couche, indexApres).getProprietairePion()) && s
					.getProprietairePion().equals(this.sommetPosition(couche, indexApresApres).getProprietairePion())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Regarde si le sommet pass� en paramettre est dans un triplet gagnant
	 * 
	 * @param s
	 * @return
	 */
	public boolean sommetGagnant(Sommet s, Joueur joueur) {
		int index = s.getINDEX();
		int couche = s.getCOUCHE();
		if (index % 2 == 0) {
			if (this.gagnantEntre(s, joueur)) {
				return true;
			}
			if (this.gagnantDessous(s, joueur)) {
				return true;
			}if(this.gagnantDessus(s,joueur)) {
				return true;
			}
			int indexApres = s.getINDEX() + 1;
			if (s.getINDEX() == this.NB_SOMMETS_PAR_COUCHE) {
				indexApres = 1;
			}
			if (joueur.equals(this.sommetPosition(couche, index - 1).getProprietairePion())
					&& joueur.equals(this.sommetPosition(couche, indexApres).getProprietairePion())) {
				return true;
			}

		} else {
			int indexAvant = s.getINDEX() - 1;
			if (s.getINDEX() == 1) {
				indexAvant = this.NB_SOMMETS_PAR_COUCHE;
			}
			if (joueur.equals(this.sommetPosition(couche, indexAvant).getProprietairePion()) && joueur.equals(this.sommetPosition(couche, indexAvant - 1).getProprietairePion())) {
				return true;
			}
			int indexApres = s.getINDEX() + 1;
			int indexApresApres = s.getINDEX() + 2;
			if (s.getINDEX() == this.NB_SOMMETS_PAR_COUCHE) {
				indexApres = 1;
			} else if (s.getINDEX() == this.NB_SOMMETS_PAR_COUCHE - 1) {
				indexApresApres = 1;
			}
			if (joueur.equals(this.sommetPosition(couche, indexApres).getProprietairePion()) && joueur.equals(this.sommetPosition(couche, indexApresApres).getProprietairePion())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * regarde pour les sommet %2 si celui au dessu set celui en dessous ont le même
	 * Propriétaire
	 * 
	 * @param s
	 * @return
	 */
	public boolean gagnantEntre(Sommet s) {
		int index = s.getINDEX();
		int couche = s.getCOUCHE();
		if (couche == this.getNB_COUCHE() || couche == 1)
			return false;
		if (s.getProprietairePion().equals(this.sommetPosition(couche - 1, index).getProprietairePion())
				&& s.getProprietairePion().equals(this.sommetPosition(couche + 1, index).getProprietairePion())) {
			return true;
		}
		return false;
	}
	/**
	 * Regarde si les sommet aux dessus sont des sommets gagnants
	 * @param s
	 * @return
	 */
	public boolean gagnantDessus(Sommet s) {
		int index = s.getINDEX();
		int couche = s.getCOUCHE();
		if (couche - 2 <= 0)
			return false;
		if (s.getProprietairePion().equals(this.sommetPosition(couche - 1, index).getProprietairePion())
				&& s.getProprietairePion().equals(this.sommetPosition(couche - 2, index).getProprietairePion())) {
			return true;
		}
		return false;
	}
	/**
	 * Regarde si les sommet en dessous sont des sommets gagnants
	 * @param s
	 * @return
	 */
	public boolean gagnantDessous(Sommet s) {
		int index = s.getINDEX();
		int couche = s.getCOUCHE();
		if (couche + 2 > this.getNB_COUCHE())
			return false;
		if (s.getProprietairePion().equals(this.sommetPosition(couche + 1, index).getProprietairePion())
				&& s.getProprietairePion().equals(this.sommetPosition(couche + 2, index).getProprietairePion())) {
			return true;
		}
		return false;
	}

	/**
	 * regarde pour les sommet %2 si celui au dessu set celui en dessous ont le même
	 * Propriétaire
	 * 
	 * @param s
	 * @return
	 */
	public boolean gagnantEntre(Sommet s,Joueur joueur) {
		int index = s.getINDEX();
		int couche = s.getCOUCHE();
		if (couche == this.getNB_COUCHE() || couche == 1)
			return false;
		if (joueur.equals(this.sommetPosition(couche - 1, index).getProprietairePion())
				&& joueur.equals(this.sommetPosition(couche + 1, index).getProprietairePion())) {
			return true;
		}
		return false;
	}
	/**
	 * Regarde si les sommet aux dessus sont des sommets gagnants
	 * @param s
	 * @return
	 */
	public boolean gagnantDessus(Sommet s,Joueur joueur) {
		int index = s.getINDEX();
		int couche = s.getCOUCHE();
		if (couche - 2 <= 0)
			return false;
		if (joueur.equals(this.sommetPosition(couche - 1, index).getProprietairePion())
				&& joueur.equals(this.sommetPosition(couche - 2, index).getProprietairePion())) {
			return true;
		}
		return false;
	}
	/**
	 * Regarde si les sommet en dessous sont des sommets gagnants
	 * @param s
	 * @return
	 */
	public boolean gagnantDessous(Sommet s,Joueur joueur) {
		int index = s.getINDEX();
		int couche = s.getCOUCHE();
		if (couche + 2 > this.getNB_COUCHE())
			return false;
		if ( joueur.equals(this.sommetPosition(couche + 1, index).getProprietairePion())
				&& joueur.equals(this.sommetPosition(couche + 2, index).getProprietairePion())) {
			return true;
		}
		return false;
	}

	/**
	 * Retourne le sommet � la position donn�e
	 * 
	 * @param couche
	 * @param index
	 * @return le sommet � la position donn�e
	 */
	public Sommet sommetPosition(int couche, int index) {
		int idx = 0;
		if (couche == 2)
			idx = this.NB_SOMMETS_PAR_COUCHE;
		if (couche == 1)
			idx = this.NB_SOMMETS_PAR_COUCHE * 2;
		return this.SOMMETS.get(idx + index - 1);
	}

	/**
	 * Retourne le jeu du moulin en affichage
	 */
	public String toString() {
		if (this.NB_COTE == 4) {
			StringBuilder string = new StringBuilder();
	        string.append(this.sommetPosition(3, 1) + "-------------------------" + this.sommetPosition(3, 2)+ "-------------------------" + this.sommetPosition(3, 3) + "\n");
	        string.append("      |                                   |                                   |  \n");
	        string.append("      |     " + this.sommetPosition(2, 1) + "------------" + this.sommetPosition(2, 2)+ "------------" + this.sommetPosition(2, 3) + "      |     " + "\n");
	        string.append("      |            |                      |                         |         |  \n");
	        string.append("      |            |    " + this.sommetPosition(1, 1) + this.sommetPosition(1, 2)+ this.sommetPosition(1, 3) + "        |         |     " + "\n");
	        string.append("      |            |          |                        |            |         |  \n");
	        string.append(this.sommetPosition(3, 8) + "" + this.sommetPosition(2, 8) + this.sommetPosition(1, 8) + "            "+ this.sommetPosition(1, 4) + this.sommetPosition(2, 4) + this.sommetPosition(3, 4) + "\n");
	        string.append("      |            |          |                        |            |         |  \n");
	        string.append("      |            |    " + this.sommetPosition(1, 7) + this.sommetPosition(1, 6)+ this.sommetPosition(1, 5) + "        |         |     " + "\n");
	        string.append("      |            |                      |                         |         |  \n");
	        string.append("      |     " + this.sommetPosition(2, 7) + "------------" + this.sommetPosition(2, 6)+ "------------" + this.sommetPosition(2, 5) + "      |     " + "\n");
	        string.append("      |                                   |                                   |  \n");
	        string.append(this.sommetPosition(3, 7) + "------------------------" + this.sommetPosition(3, 6)+ "------------------------" + this.sommetPosition(3, 5) + "\n\n");

	        return string.toString();
		} else {
			StringBuilder builder = new StringBuilder();
			
			builder.append("                                       " + this.sommetPosition(3, 1) + "\n");
			builder.append("                                      /            \\" + "\n");
			builder.append("                                    /                \\" + "\n");    
			builder.append("                                  /                    \\" + "\n");
			builder.append("                                /                        \\" + "\n");
			builder.append("                              /                            \\" + "\n");
			builder.append("                            /                               \\" + "\n");     
			builder.append("                          /                                  \\" + "\n");
			builder.append("                         /                                    \\" + "\n");                         
			builder.append("                        /                                      \\" + "\n");                        
			builder.append("                       /                                        \\" + "\n");                       
			builder.append("                      /                "+this.sommetPosition(2, 1)+"              \\" + "\n"); 
			builder.append("                     /                 /         \\                \\" + "\n");                  
			builder.append("                    /                /             \\               \\" + "\n");                  
			builder.append("                   /               /                 \\              \\" + "\n");                  
			builder.append("                  /              /     "+this.sommetPosition(1, 1)+"    \\             \\" + "\n");
			builder.append("                 /             /        /     \\          \\            \\" + "\n");                  
			builder.append("                /            /         /       \\          \\            \\" + "\n");                 
			builder.append("               /            /         /         \\          \\            \\" + "\n");                
			builder.append("    "+this.sommetPosition(3, 6)+"–"+this.sommetPosition(2, 6)+"–"+this.sommetPosition(1, 6)+"   "+this.sommetPosition(1, 2)+"–"+this.sommetPosition(2, 2)+"–"+this.sommetPosition(3, 2) + "\n");
			builder.append("           /       /             /                    \\          \\         \\" + "\n");            
			builder.append("          /       /             /                      \\          \\         \\" + "\n");           
			builder.append("         /       /             /                        \\          \\         \\" + "\n");          
			builder.append("        /       /    "+this.sommetPosition(1, 5)+"–––––"+this.sommetPosition(1, 4)+"–––––"+this.sommetPosition(1, 3)+" \\         \\" + "\n");
			builder.append("       /       /                           |                         \\         \\" + "\n");        
			builder.append("      /       /                            |                          \\         \\" + "\n");       
			builder.append("     /       /                             |                           \\         \\" + "\n");       
			builder.append("    /     "+this.sommetPosition(2, 5)+"––––––––––––––––"+this.sommetPosition(2, 4)+"––––––––––––––––"+this.sommetPosition(2, 3)+"    \\" + "\n");     
			builder.append("   /                                       |                                       \\" + "\n");    
			builder.append("  /                                        |                                        \\" + "\n");   
			builder.append(" /                                         |                                         \\" + "\n");  
			builder.append(this.sommetPosition(3, 5)+"––––––––––––––––––––––––––"+this.sommetPosition(3, 4)+"–––––––––––––––––––––––––––"+this.sommetPosition(3, 3) + "\n");
			
			return builder.toString();
		}
	}

	/**
	 * Demande un joueur courant la position pour placer son nouveau pion et teste
	 * si celui ci est gagnant
	 * 
	 * @param courant
	 * @param adverse
	 */
	public int[] placer1Pion1Joueur(Joueur courant, Joueur adverse) {
		int[] resultat = new int[2];
		int num_couche = -1;
		int num_index = -1;
		do {
			System.out.println("\t______________________" + courant + "_____________________________\n");
			num_couche = Main.demandeIndice("Saisisez la couche du pion :");
			num_index = Main.demandeIndice("Saisisez l'index du pion : ");
		} while (Main.MauvaisIndicePosition(num_couche, num_index, this)
				|| sommetPosition(num_couche, num_index).getProprietairePion() != null);
		this.placePion(courant, num_couche, num_index);
		courant.PionPasPoser--;
		resultat[0] = num_couche;
		resultat[1] = num_index;
		return resultat;

	}

	/**
	 * Place automatique un pion du joueur sans que ce pion soit un pion gagnant
	 * 
	 * @param nbPions
	 * @param joueurs
	 * @return
	 */
	public void placementAutomatique(Joueur j) {
		Random rand=new Random();
			int couche=-1;
			int index=-1;
			boolean sortboucle=true;
			do {
				couche=rand.nextInt(this.NB_COUCHE)+1;
				index=rand.nextInt(this.NB_SOMMETS_PAR_COUCHE )+1;
				if(sommetPosition(couche, index).getProprietairePion()==null) {
					this.placePion(j, couche, index);
					j.PionPasPoser--;
					sortboucle=true;
					if(this.sommetGagnant(sommetPosition(couche, index),j)) {
						this.supressionPion(j, couche, index);
						j.PionPasPoser++;
						sortboucle=false;
					}
				}else {
					sortboucle=false;
				}
			}while ( !sortboucle);
	}

	/*
	 * place correctement un pion dans le plateau et ajoute le pion et le sommet au
	 * joueur
	 * 
	 * @param j
	 * 
	 * @param couche
	 * 
	 * @param index
	 */
	public void placePion(Joueur j, int couche, int index) {
		Sommet s = this.sommetPosition(couche, index);
		Pion p = new Pion(j, s);
		this.placePion(j, s, p);
	}

	public void placePion(Joueur j, Sommet s, Pion p) {
		p.placer(s);
		j.addPionSommet(p, s);
	}

	/**
	 * place le piege pour le joueur
	 * 
	 * @param Joueur j
	 * @param int    couche
	 * @param int    index
	 */
	public void placementPiege(Joueur j, int couche, int index, TypePiege type) {
		Sommet s = this.sommetPosition(couche, index);
		Piege p = new Piege(j, s, type);
		p.placer(s);
		j.setmonPiege(p);
	}

	/**
	 * demande au joueur courant quel pion de l'adversaire retirer et le supprime
	 * 
	 * @param courant
	 * @param adversaire
	 */
	public void supressionPion(Joueur courant, Joueur adversaire) {
		System.out.println("\n\nTu peut maintenant retirer un piont à ton adversaire");
		int num_couche = -1;
		int num_index = -1;
		do {
			num_couche = Main.demandeIndice("Saisisez la couche du pion adversaire que vous souhaitez supprimer :");
			num_index = Main.demandeIndice("Saisisez l'index du pion adversaire que vous souhaitez supprimer : ");
			if (this.sommetGagnant(this.sommetPosition(num_couche, num_index),courant))
				System.err.println("\nTu ne peut pas supprimer un pion qui forme déjà un triplet gagnant");
			else if (sommetPosition(num_couche, num_index).getProprietairePion() != adversaire)
				System.err.println("\nCe piont n'est pas à ton adversaire");

		} while (Main.MauvaisIndicePosition(num_couche, num_index, this)
				|| sommetPosition(num_couche, num_index).getProprietairePion() != adversaire
				|| this.sommetGagnant(this.sommetPosition(num_couche, num_index),courant));
		this.supressionPion(adversaire, num_couche, num_index);
	}

	public void supressionPion(Joueur joueur, int num_couche, int num_index) {
		Pion p = joueur.getPions()
				.get(joueur.getPions().indexOf(new Pion(joueur, this.sommetPosition(num_couche, num_index))));
		joueur.removePionSommet(p, this.sommetPosition(num_couche, num_index));
		p.retirer(this.sommetPosition(num_couche, num_index));
	}
}