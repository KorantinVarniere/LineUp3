package lu3.lu3.jeu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javafx.stage.FileChooser;


public class Sauvegarde  {
	/*
	private static void savePlateau(Plateau plateau) {
		FileChooser chooser = new FileChooser();
		File file = chooser.showSaveDialog(null);
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(file))) {
			oos.writeObject(plateau);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Plateau readPlateau() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(null);
		try (ObjectInputStream ois = new ObjectInputStream( 
				new FileInputStream(file))) {
			return (Plateau) ois.readObject();
		} catch (Exception e) {
			System.err.println("Aucune sauvegarde");
		}
		return null;
	}
*/
	private static void save(List<Object> obj) {
		FileChooser chooser = new FileChooser();
		File file = chooser.showSaveDialog(null);
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream(file))) {
			oos.writeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<Object> read() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(null);
		try (ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(file))) {
			return (List<Object>) ois.readObject();
		} catch (Exception e) {
			System.err.println("Mauvaise sauvegarde");
		}
		return null;
	}
	
	public static void useSauvegarde(Plateau p, Joueur[] joueurs) {
		List<Object> obj= new ArrayList<Object>(3);
		obj.add(p);
		obj.add(joueurs);
		save(obj);
	}

}