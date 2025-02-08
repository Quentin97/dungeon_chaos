package Model.Creatures;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Model.GameObject;
import Model.Handler;
import Model.Chrono.Chrono;
import Model.Demisable.Demisable;
import Model.Demisable.DemisableObserver;
import Model.Inventory.Inventory;
import Model.Inventory.Pickable;
import Model.Weapons.HandToHand;
import View.Graphics.Texture;

public class Pirate extends Monster implements DemisableObserver {
	private static final long serialVersionUID = -3331682531726040669L;
	/*
	 * Monstre de type pirate. Attaque au corps à corps. Il est capable
	 * d'approcher un joueur et de l'attaquer dès qu'il le détecte. Envoie des
	 * perroquets lui chercher des objets sur le plateau de jeu ou dans
	 * l'inventaire du joueur si celui-ci est trop loin.
	 */
	private int column = 0, columnAtt = 7, orientation = 1, duration = 5000;
	private transient BufferedImage[] pirate_left, pirate_right;
	private boolean change = true;
	private transient Thread tPirate;
	private ArrayList<Parrot> parrots = new ArrayList<Parrot>();
	private Handler handler;
	private Inventory inventory;
	private transient boolean init = false;
	private Chrono chrono;

	public Pirate(int posX, int posY, int rad, int lives, Alive ennemy, Handler handler) {
		super(posX, posY, rad, lives, handler, ennemy);
		this.handler = handler;
		inventory = new Inventory();
		chrono = new Chrono();
		super.setId(14);
		// Arme du pirate : corps à corps.
		super.setWeapon(new HandToHand(posX, posY, rad, this, handler));
		detectionDistance = 6;
		attackDistance = 1;
		setImage();
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	//////////////////// DEMARRAGE / ARRET ////////////////////

	@Override
	public void start() {
		/*
		 * Si début du jeu, un nouveau Thread est créé. Sinon, le Thread est
		 * relancé.
		 */
		if (!init) {
			tPirate = new Thread(this);
			tPirate.start();
			init = true;
			inAction = true;
			pause = false;
			setImage();
		} else {
			synchronized (this) {
				inAction = true;
				this.notify();
				pause = false;
				chrono.pauseEnd();
			}
		}
	}

	@Override
	synchronized public void pause() {
		inAction = false;
		pause = true;
		chrono.pauseBegin();
		if (super.getLives() <= 0) {
			dropInventoryItems();
		}
	}

	synchronized private void dropInventoryItems() {
		/* Drop les objets de son inventaire lorsqu'il meurt. */
		synchronized (inventory.getInventoryItems()) {
			for (Pickable item : inventory.getInventoryItems()) {
				synchronized (item) {
					((GameObject) item).setPosX(super.getPosX() + (int) (100 * Math.random()));
					((GameObject) item).setPosY(super.getPosY() + (int) (100 * Math.random()));
					handler.getMap().setItem(item);
				}
			}
		}
	}

	//////////////////// INTERFACE RUNNABLE ////////////////////

	@Override
	public void run() {
		/*
		 * Boucle infinie pour le Thread. Si l'ennemi est plus près que la
		 * distance maximale d'attaque, le pirate l'attaque. S'il est plus loin
		 * que cette distance mais plus près que la distance maximale de
		 * détection, le pirate se déplace jusqu'à ce qu'il puisse attaquer
		 * l'ennemi. Dans les autres cas, le pirate envoie des perroquets
		 * chercher des objets.
		 */
		try {
			while (super.getLives() > 0 && handler.getPlayer() != null) {
				if (super.check(attackDistance) && handler.getPlayer() != null && inAction) {
					setFight(true);
					super.actionShoot();
					for (int i = 0; i < 6; i++) {
						currentImage(orientation);
						Thread.sleep(timer * 4);
					}
				} else if (super.check(detectionDistance) && handler.getPlayer() != null) {
					setFight(false);
					super.go(attackDistance - 0.2, detectionDistance - 1);
				} else if (parrots.size() <= 3 && chrono.ready(duration) && inAction) {
					// Envoie un perroquet s'il n'y en a pas plus que 3 sur le
					// plateau et si un certain temps s'est écoulé depuis que le
					// précédent perroquet est parti.
					Parrot parrot = new Parrot(getPosX(), getPosY(), getRad(), 4, handler, handler.getPlayer(), this);
					parrot.demisableAttach(this);
					chrono.begin();
					synchronized (handler.getMap().getGameObjects()) {
						handler.getMap().addObject(parrot);
					}
					parrots.add(parrot);
					parrot.start();
					Thread.sleep(timer * 10);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//////////////////// IMAGE ////////////////////

	@Override
	public void currentImage(int row) {
		this.orientation = row;
		if (change) {
			int currentColumn;
			if (!isFighting()) {
				column++;
				if (column >= 6) {
					column = 0;
				}
				currentColumn = column;
			} else {
				columnAtt++;
				if (columnAtt >= 14) {
					columnAtt = 6;
				}
				currentColumn = columnAtt;
			}

			if (row == 1 || row == 3 || row == 4)
				setImage(pirate_right[currentColumn]);
			else if (row == 2)
				setImage(pirate_left[currentColumn]);
			change = false;
		} else
			change = true;
	}

	public void setImage() {
		this.pirate_left = Texture.pirate_left;
		this.pirate_right = Texture.pirate_right;
		currentImage(3);
	}
	
	/////////////// INTERFACE DEMISABLEOBSERVER ///////////////

	@Override
	synchronized public void demise(Demisable d) {
		parrots.remove((Parrot) d);
	}

}
