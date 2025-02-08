package Model.PopUp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import View.Panel;
import View.Graphics.Text;

public class PopUpManager {
	/*
	 * Gère l'ensemble des pop ups et leur affichage.
	 */
	private ArrayList<PopUp> popUps;
	private Font font;

	public PopUpManager() {
		popUps = new ArrayList<PopUp>();
		font = new Font("Times New Roman", Font.BOLD, 24);
	}

	synchronized public void update(PopUp popUp) {
			popUps.remove(popUp);
	}

	public void add(PopUp popUp) {
		synchronized (popUps) {
			popUps.add(popUp);
			popUp.setPom(this);
		}
	}

	public void render(Graphics g, Panel map) {
		synchronized (popUps) {
			for (PopUp popUp : popUps) {
				Text.drawString(g, popUp.getText(), popUp.getX(), popUp.getY(), true, Color.BLUE, font);
			}
		}
	}
}
