package Model.Creatures;

import java.awt.image.BufferedImage;
import Model.Handler;
import Model.Weapons.HandToHand;
import View.Graphics.Texture;

public class Goblin extends Monster {
	/*
	 * Monstre de type gobelin. Attaque au corps à corps. il est capable
	 * d'approcher un joueur et de l'attaquer dès qu'il le détecte.
	 */
	private static final long serialVersionUID = -8844387423876448830L;
	private int column = 0, columnAtt = 7, orientation = 1;
	private transient BufferedImage[] goblin_up, goblin_left, goblin_down, goblin_right;
	private boolean change = true;
	private transient Thread tGoblin;
	private Handler handler;
	private transient boolean init = false;

	public Goblin(int posX, int posY, int rad, int lives, Alive ennemy, Handler handler) {
		super(posX, posY, rad, lives, handler, ennemy);
		this.handler = handler;
		super.setId(11);
		// Arme du gobelin : corps à corps.
		super.setWeapon(new HandToHand(posX, posY, rad, this, handler));
		detectionDistance = 6;
		attackDistance = 1;
		setImage();
	}

	//////////////////// DEMARRAGE / ARRET ////////////////////

	@Override
	public void start() {
		/*
		 * Si début du jeu, un nouveau Thread est créé. Sinon, le Thread est
		 * relancé.
		 */
		if (!init) {
			tGoblin = new Thread(this);
			tGoblin.start();
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
	public void pause() {
		inAction = false;
		pause = true;
	}

	//////////////////// INTERFACE RUNNABLE ////////////////////

	@Override
	public void run() {
		/*
		 * Boucle infinie pour le Thread. Si l'ennemi est plus près que la
		 * distance maximale d'attaque, le gobelin l'attaque. S'il est plus loin
		 * que cette distance mais plus près que la distance maximale de
		 * détection, le gobelin se déplace jusqu'à ce qu'il puisse attaquer
		 * l'ennemi. Dans les autres cas, le Thread est mis en pause jusqu'à ce
		 * qu'un ennemi lui indique qu'il s'est déplacé.
		 */
		try {
			while (super.getLives() > 0 && handler.getPlayer() != null && inAction) {
				if (super.check(attackDistance) && handler.getPlayer() != null && ennemy.getLives() > 0) {
					setFight(true);
					super.actionShoot();
					for (int i = 0; i < 6; i++) {
						currentImage(orientation);
						notifyMovementObserver();
						Thread.sleep(timer * 4);
					}
				} else if (super.check(detectionDistance) && handler.getPlayer() != null && ennemy != null
						&& inAction) {
					setFight(false);
					super.go(attackDistance - 0.2, detectionDistance + 2);
				} else {
					inAction = false;
					hasATarget = false;
				}
				if (!inAction)
					synchronized (this) {
						this.wait();
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
				if (column >= 7) {
					column = 0;
				}
				currentColumn = column;
			} else {
				columnAtt++;
				if (columnAtt >= 10) {
					columnAtt = 7;
				}
				currentColumn = columnAtt;
			}

			if (row == 1)
				setImage(goblin_up[currentColumn]);
			else if (row == 2)
				setImage(goblin_left[currentColumn]);
			else if (row == 3)
				setImage(goblin_down[currentColumn]);
			else if (row == 4)
				setImage(goblin_right[currentColumn]);
			change = false;
		} else
			change = true;
	}

	public void setImage() {
		this.goblin_up = Texture.goblin_up;
		this.goblin_left = Texture.goblin_left;
		this.goblin_down = Texture.goblin_down;
		this.goblin_right = Texture.goblin_right;
		currentImage(3);
	}

}
