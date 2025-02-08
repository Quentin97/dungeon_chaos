package View;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JPanel;

import View.Graphics.Text;
import View.Graphics.Texture;

public class Menu extends JPanel {
	/*
	 * Menu principal qui s'affiche au démarrage de la partie ou lorsque le
	 * joueur est mort. Les boutons sont ici des rectangles sur lesquels cliquer
	 * pour effectuer l'une ou l'autre action.
	 */
	private static final long serialVersionUID = 1L;
	private Rectangle newGameButton;
	private Rectangle loadGameButton;
	private Rectangle quitButton;
	private Font font1;
	private Font font2;

	public Menu() {
		this.setVisible(true);
		font1 = new Font("Times New Roman", Font.BOLD, 50);
		font2 = new Font("Times New Roman", Font.BOLD, 30);
	}

	public Rectangle getNewGameButton() {
		return this.newGameButton;
	}

	public Rectangle getLoadGameButton() {
		return this.loadGameButton;
	}

	public Rectangle getQuitButton() {
		return this.quitButton;
	}

	@Override
	public void paintComponent(Graphics g) {
		int centerx = this.getWidth() / 2;
		int centery = this.getHeight() / 2;
		newGameButton = new Rectangle(centerx - 135, centery - 135, 270, 70);
		loadGameButton = new Rectangle(centerx - 135, centery - 35, 270, 70);
		quitButton = new Rectangle(centerx - 135, centery + 65, 270, 70);

		Graphics2D g2d = (Graphics2D) g;

		g.drawImage(Texture.menu, 0, 0, this.getWidth(), this.getHeight(), this);

		Text.drawString(g, "Projet Donjon", centerx, centery - 200, true, Color.BLACK, font1);

		Text.drawString(g, "Nouvelle Partie", centerx, centery - 100, true, Color.BLACK, font2);
		g2d.draw(newGameButton);

		Text.drawString(g, "Reprendre la partie", centerx, centery, true, Color.BLACK, font2);
		g2d.draw(loadGameButton);

		Text.drawString(g, "Quitter", centerx, centery + 100, true, Color.BLACK, font2);
		g2d.draw(quitButton);

	}

}