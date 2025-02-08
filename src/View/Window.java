package View;

import java.awt.Frame;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

import Controller.Mouse;
import Model.GameObject;
import Model.Creatures.Alive;
import Model.Inventory.Inventory;
import Model.PopUp.PopUpManager;
import View.Panel;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class Window extends JFrame {
	/*
	 * Hérite de JFrame. Affiche une fenêtre, écoute le clavier et la souris et
	 * crée un lien de dépendance avec un objet panel et un objet menu. Fait du
	 * panel un observateur privilégié de tous les objets de type GameObject du
	 * jeu.
	 */
	private static final long serialVersionUID = 1L;
	private Mouse mouse;
	Panel pan = new Panel();
	Menu menu = new Menu();

	public Window() {
		super();
		setTitle("Projet Donjon");
		pack(); // Pour afficher la fenêtre en grand écran.
		setDefaultLookAndFeelDecorated(true);
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null); // Au centre
		setVisible(true);
		setDefaultCloseOperation(Window.EXIT_ON_CLOSE); // Pour fermer la
														// fenêtre en appuyant
														// sur la croix.
		getContentPane().add(this.menu); // Ajoute la map au Content Pane de la
											// fenêtre.
		setResizable(false);
	}
	
	////////// SETTERS & GETTERS //////////

	public Menu getMenu() {
		return this.menu;
	}

	public Panel getPanel() {
		return this.pan;
	}

	public void setGameObjects(ArrayList<GameObject> objects) {
		/*
		 * Donne à la map les référents de tous les GameObjects du jeu. La map
		 * devient un observateur privilégié de tous ces objects.
		 */
		pan.setObjects(objects);
		if (objects != null) {
			for (GameObject object : objects) {
				object.addObserver(pan);
			}
		}
		pan.update();
	}

	public void setPlayer(Alive player) {
		pan.setPlayer(player);
	}

	public void setInventory(Inventory inventory) {
		pan.setInventory(inventory);
		inventory.addObserver(pan);
	}

	public void setPopUpManager(PopUpManager pom) {
		pan.setPopUpManager(pom);
	}

	public void setKeyListener(KeyListener keyboard) {
		addKeyListener(keyboard);
	}

	public void setMouseListener(MouseListener mouse) {
		addMouseListener(mouse);
		this.mouse = (Mouse) mouse;
	}

	public int[] getScreenSize() {
		return new int[] { this.getWidth(), this.getHeight() };
	}
	
	////////// POUR LANCER LE PANEL ADEQUAT //////////

	public void launchPan() {
		getContentPane().remove(menu);
		invalidate();
		validate();
		getContentPane().add(pan);
	}

	public void launchMenu() {
		getContentPane().remove(pan);
		invalidate();
		validate();
		getContentPane().add(menu);
		menu.setVisible(true);
		mouse.init();
		menu.repaint();
	}

}