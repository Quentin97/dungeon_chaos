package Model;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import Model.Demisable.Demisable;
import Model.Demisable.DemisableObserver;
import View.Panel;

public abstract class GameObject extends Observable implements Demisable, Serializable {

	private static final long serialVersionUID = -2813940267731673560L;
	/*
	 * Classe mère de tous les objets "physiques" du jeu. Détermine une position
	 * en x, en y, un rayon, un rectangle. Cette classe est observée
	 * (Observable) par le panel (Observer) pour la tenir au courant de
	 * l'évolution des attributs posX, posY ou image. Elle est observée par la
	 * classe Game (Demisable) pour connaître indiquer quand l'objet est mort et
	 * peut être supprimé du jeu.
	 */
	private int posX;
	private int posY;
	private int rad; // Portée d'action des objects
	private Rectangle rect; // Zone d'occupation des objets
	private transient BufferedImage image;
	private transient ArrayList<DemisableObserver> observers;

	public GameObject(int posX, int posY, int rad) {
		setPosX(posX);
		setPosY(posY);
		setRad(rad);
		rect = new Rectangle(posX + (int) rad * 2 / 12, posY + (int) rad / 8, rad * (int) 3 / 5, rad * (int) 4 / 5);
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	public void setPosX(int posX) {
		this.posX = posX;
		setChanged();
		notifyObservers();
	}

	public void setPosY(int posY) {
		this.posY = posY;
		setChanged();
		notifyObservers();
	}

	public void setRad(int rad) {
		this.rad = rad;
	}

	public void setImage(BufferedImage image) {
		/* Change l'attribut myImage et le renseigne à la classe observeuse. */
		this.image = image;
		setChanged();
		notifyObservers();
	}

	public abstract void setImage();

	public Integer getPosX() {
		return this.posX;
	}

	public Integer getPosY() {
		return this.posY;
	}

	public Integer getRad() {
		return this.rad;
	}

	public BufferedImage getImage() {
		return image;
	}

	public Rectangle getRect() {
		return rect;
	}

	//////////////////// FONCTION GRAPHIQUE ////////////////////

	public void render(Graphics g, Panel pan) {
		g.drawImage(image, posX, posY, image.getWidth(), image.getHeight(), pan);
	}

	//////////////////// FONCTIONS OBJET PHYSIQUE ////////////////////

	synchronized public boolean isInField(int x, int y, int rad, double factor) {
		/*
		 * Compare les positions d'objets en utilisant la norme euclidienne.
		 */
		double dist1 = (Math.pow((this.posX + this.rad / 2 - x - rad / 2), 2)
				+ Math.pow((this.posY + this.rad / 2 - y - rad / 2), 2));
		double dist2 = factor * Math.pow((this.rad / 2 + rad / 2), 2);
		return dist1 < dist2;
	}

	public abstract boolean isObstacle();

	public abstract void pause();

	public abstract void start();

	///////// DEFINITION DES FONCTIONS DE l'INTERFACE DEMISABLE //////////

	@Override
	public void demisableAttach(DemisableObserver po) {
		/* Ajoute un observateur */
		if (observers == null)
			observers = new ArrayList<DemisableObserver>();
		observers.add(po);
	}

	@Override
	public void demisableNotifyObserver() {
		if (observers != null) {
			for (DemisableObserver observer : observers) {
				// Indique aux observateurs que l'objet est mort.
				observer.demise(this);
			}
		}
		setChanged();
		notifyObservers();
	}

}
