package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;
import Model.GameObject;
import Model.Score;
import Model.Creatures.Alive;
import Model.Creatures.Player;
import Model.Inventory.Inventory;
import Model.PopUp.PopUpManager;
import View.Graphics.Texture;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Panel extends JPanel implements Observer {
	/*
	 * Panneau contenant les composants d'affichage, hérite de JPanel.
	 * Observateur des objets de type GameObject.
	 */
	private static final long serialVersionUID = 2L;
	private ArrayList<GameObject> objects;
	private Alive player;
	private Inventory inventory;
	private PopUpManager pom;
	private Font font;

	public Panel() {
		Texture.init();
		this.setFocusable(true);
		this.requestFocusInWindow();
		font = new Font("Times New Roman", Font.BOLD, 40);
	}

	@Override
	synchronized public void paintComponent(Graphics g) {
		if (font != null)
			g.setFont(font);

		// Affiche l'arène
		Arena.render(g, this, this.getWidth(), this.getHeight());

		if (objects != null && player != null && player.getLives() > 0) {
			// Pour éviter les erreurs dues à la non-initialisation des objets.
			synchronized (objects) {
				for (GameObject object : objects) {
					object.render(g, this);
				}
			}
		}
		if (player != null && player.getLives() > 0)
			synchronized (player) {
				// Affiche le nombre de vies du joueur.
				g.setColor(Color.RED);
				g.drawImage(Texture.heart, this.getWidth() / 2 - 200, this.getHeight() - 45, 50, 50, this);
				g.drawString(String.valueOf(player.getLives()), this.getWidth() / 2 - 245, this.getHeight() - 6);
				g.drawImage(((GameObject) ((Player) player).getCurrentWeapon()).getImage(), this.getWidth() - 50,
						this.getHeight() - 50, 50, 50, this);
			}
		// Affiche le score
		Score.render(g, this);

		if (pom != null)

		{
			synchronized (pom) {
				pom.render(g, this);
			}
		}

		if (inventory != null && inventory.isActive()) {
			inventory.render(g, this);
		}

	}

	public void setObjects(ArrayList<GameObject> objects) {
		this.objects = objects;
	}

	public void setPlayer(Alive player) {
		this.player = player;
	}

	@Override
	public void update(Observable o, Object arg) {
		this.repaint();
	}

	public void update() {
		// Surcharge pour que la map s'affiche dès le début.
		this.repaint();
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setPopUpManager(PopUpManager pom) {
		this.pom = pom;
	}
}
