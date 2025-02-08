package Model.Creatures;

import java.awt.image.BufferedImage;
import java.util.Random;

import Model.GameObject;
import Model.Handler;
import Model.Inventory.Inventory;
import Model.Inventory.Item;
import Model.Inventory.Pickable;
import View.Graphics.Texture;

public class Parrot extends Monster {
	/*
	 * Monstre de type perroquet. Il ne peut pas attaquer le joueur. Dès qu'il
	 * est envoyé, il recherche un item au hasard sur la map et se dirige vers
	 * celui-ci. S'il n'en trouve pas, il se dirige directement vers le joueur
	 * et le suit à la trace pour lui voler un objet de son inventaire. Quand il
	 * a récupéré un objet, le perroquet retourne le donner au pirate.
	 */
	private static final long serialVersionUID = -1391497368722619219L;
	private int column = 0;
	private transient BufferedImage[] parrot_up, parrot_left, parrot_down, parrot_right;
	private boolean change = true;
	private transient Thread tParrot;
	private Pirate pirate;
	private Inventory inventory;
	private Handler handler;
	private boolean caughtAnItem = false, hasATarget = true, itemOnTheMap;
	private int[] target = new int[2];
	private transient boolean init = false;

	public Parrot(int posX, int posY, int rad, int lives, Handler handler, Alive ennemy, Pirate pirate) {
		super(posX, posY, rad, lives, handler, ennemy);
		this.handler = handler;
		this.pirate = pirate;
		inventory = new Inventory();
		super.setId(15);
		setImage();
	}

	//////////////////// INVENTAIRE ////////////////////

	public Inventory getInventory() {
		return inventory;
	}

	//////////////////// DEMARRAGE / PAUSE ////////////////////

	@Override
	public void start() {
		if (!init) {
			tParrot = new Thread(this);
			tParrot.start();
			init = true;
			inAction = true;
			pause = false;
			setImage();
		} else {
			synchronized (this) {
				inAction = true;
				this.notify();
				pause = false;
			}
		}
	}

	@Override
	synchronized public void pause() {
		inAction = false;
		pause = true;
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
		 * Boucle infinie pour le thread. Le perroquet cherche un objet sur la
		 * map et s'il n'en trouve pas, il en cherche un dans l'inventaire du
		 * joueur. Une fois l'objet volé, il le rend au pirate qui l'a envoyé.
		 */

		while (super.getLives() > 0 && pirate.getLives() > 0 && handler.getPlayer() != null) {
			try {
				Thread.sleep(400);
				while ((!hasATarget || !caughtAnItem) && inAction && handler.getPlayer() != null) {
					lookForATarget();
					if (hasATarget) {
						go();
						stealAnItem();
					}
					Thread.sleep(200);
				}
				if (caughtAnItem && inAction) {
					goBack();
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		dropInventoryItems();
		demisableNotifyObserver();
	}
	
	//////////////////// ACTIONS ////////////////////

	private void lookForATarget() {
		/* Cherche une cible */
		if (handler.getMap().getItems() != null && handler.getMap().getItems().size() > 0) {
			// Cible sur le plateau de jeu.
			synchronized (handler.getMap().getItems()) {
				Pickable item = handler.getMap().getItems()
						.get(new Random().nextInt(handler.getMap().getItems().size()));
				if (item instanceof Item) {
					target = target((GameObject) item);
					hasATarget = true;
					itemOnTheMap = true;
				} else
					hasATarget = false;
			}
		}

		else if (((Player) handler.getPlayer()).getInventory().getInventoryItems().size() > 1) {
			// Cible : le joueur.
			target[0] = handler.getPlayer().getPosX();
			target[1] = handler.getPlayer().getPosY();
			hasATarget = true;
			itemOnTheMap = false;
		}
	}

	private void go() {
		// Le perroquet vole au-dessus des obstacles.
		while ((Math.abs(getPosX() - target[0]) > 25 || Math.abs(getPosY() - target[1]) > 25) && inAction) {
			int myX = super.getPosX();
			int myY = super.getPosY();
			int distX = myX - target[0];
			int distY = myY - target[1];
			int nextX = (int) (myX - Math.signum(distX) * step);
			int nextY = (int) (myY - Math.signum(distY) * step);

			move(nextX, nextY);

			if (distX == 0 && distY > 0)
				currentImage(4);
			else if (distX == 0 && distY <= 0)
				currentImage(1);
			else if (distX > 0)
				currentImage(2);
			else if (distX < 0)
				currentImage(3);
			try {
				Thread.sleep(timer / 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (!inAction && !caughtAnItem) {
			hasATarget = false;
		}

	}

	private void stealAnItem() {
		if (handler.getMap().getItems() != null && handler.getMap().getItems().size() != 0 && itemOnTheMap) {
			synchronized (handler.getMap().getItems()) {
				for (Pickable item : handler.getMap().getItems()) {
					if (item instanceof Item && ((GameObject) item).isInField(getPosX(), getPosY(), getRad(), 1)) {
						item.pickUp(this);
						caughtAnItem = true;
						return;
					}
				}
			}
		} else if (inventory.getInventoryItems().size() == 0
				&& ((Player) handler.getPlayer()).isInField(getPosX(), getPosY(), getRad(), 1.5)) {
			synchronized (((Player) handler.getPlayer()).getInventory()) {
				Inventory playerInventory = ((Player) handler.getPlayer()).getInventory();
				Pickable item = playerInventory.getInventoryItems()
						.get(new Random().nextInt(playerInventory.getInventoryItems().size()));
				if (item instanceof Item) {
					playerInventory.deleteItem(item);
					Item newItem = (Item) item;
					newItem.setCount(1);
					inventory.add(newItem);
					caughtAnItem = true;
				}
			}
		}
	}

	private void goBack() {
		if (caughtAnItem && inAction) {
			target[0] = pirate.getPosX();
			target[1] = pirate.getPosY();
			hasATarget = true;
			go();
			synchronized (inventory.getInventoryItems()) {
				if (inventory.getInventoryItems().size() != 0 && pirate != null && pirate.getLives() > 0 && inAction) {
					while (inventory.getInventoryItems().size() > 0) {
						Pickable item = inventory.getInventoryItems().get(0);
						inventory.deleteItem(item);
						pirate.getInventory().add(item);
					}
					demisableNotifyObserver();
					pause();
				} else if ((pirate == null || pirate.getLives() <= 0) && inAction) {
					dropInventoryItems();
					super.setLives(0);
					demisableNotifyObserver();
				}
			}
		}
	}

	//////////////////// IMAGE ////////////////////

	@Override
	public void currentImage(int row) {
		if (change) {
			column++;
			if (column >= 4) {
				column = 0;
			}
			int currentColumn = column;

			if (row == 4)
				setImage(parrot_up[currentColumn]);
			else if (row == 2)
				setImage(parrot_left[currentColumn]);
			else if (row == 1)
				setImage(parrot_down[currentColumn]);
			else if (row == 3)
				setImage(parrot_right[currentColumn]);
			change = false;
		} else
			change = true;
	}

	public void setImage() {
		this.parrot_up = Texture.parrot_up;
		this.parrot_left = Texture.parrot_left;
		this.parrot_down = Texture.parrot_down;
		this.parrot_right = Texture.parrot_right;
		currentImage(3);
	}

	/////////////// INTERFACE MOVEMENTOBSERVER ///////////////

	@Override
	public void personMoved(Alive pers) {
		// Ne réagit pas au mouvement des ennemis.
	}
}
