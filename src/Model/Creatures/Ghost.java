package Model.Creatures;

import java.awt.image.BufferedImage;
import Model.Handler;
import Model.Weapons.ExplodingBriefCase;
import View.Graphics.Texture;

public class Ghost extends Monster {
	/*
	 * Monstre de type fantôme. Attaque en déposant des bombes. Il est capable
	 * d'approcher un joueur et de l'attaquer dès qu'il le détecte.
	 */
	private static final long serialVersionUID = 7189375300108237907L;
	private int column = 0, columnAtt = 7, orientation = 1;
	private transient BufferedImage[] ghost_up, ghost_left, ghost_down, ghost_right;
	private boolean change = true;
	private transient Thread tGhost;
	private Handler handler;
	private transient boolean init = false;

	public Ghost(int posX, int posY, int rad, int lives, Alive ennemy, Handler handler) {
		super(posX, posY, rad, lives, handler, ennemy);
		this.handler = handler;
		super.setId(12);
		// Arme du fantôme : bombes.
		super.setWeapon(new ExplodingBriefCase(posX, posY, rad, this, handler));
		detectionDistance = 6;
		attackDistance = 1.5;
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
			tGhost = new Thread(this);
			tGhost.start();
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
		 * distance maximale d'attaque, le fantôme l'attaque. S'il est plus loin
		 * que cette distance mais plus près que la distance maximale de
		 * détection, le fantôme se déplace jusqu'à ce qu'il puisse attaquer
		 * l'ennemi. Dans les autres cas, le Thread est mis en pause jusqu'à ce
		 * qu'un ennemi lui indique qu'il s'est déplacé.
		 */
		try {
			while (super.getLives() > 0 && handler.getPlayer() != null && inAction) {
				if (super.check(attackDistance) && handler.getPlayer() != null && ennemy.getLives() > 0) {
					setFight(true);
					super.actionShoot();
					for (int i = 0; i < 38; i++) {
						currentImage(orientation);
						notifyMovementObserver();
						Thread.sleep(timer * 2);
					}
				} else if (super.check(detectionDistance) && handler.getPlayer() != null && ennemy != null) {
					setFight(false);
					super.go(attackDistance, detectionDistance + 4);
					Thread.sleep(timer * 10);
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
				if (column >= 5) {
					column = 0;
				}
				currentColumn = column;
			} else {
				columnAtt++;
				if (columnAtt >= 9) {
					columnAtt = 5;
				}
				currentColumn = columnAtt;
			}

			if (row == 1)
				setImage(ghost_up[currentColumn]);
			else if (row == 2)
				setImage(ghost_left[currentColumn]);
			else if (row == 3)
				setImage(ghost_down[currentColumn]);
			else if (row == 4)
				setImage(ghost_right[currentColumn]);
			change = false;
		} else
			change = true;
	}

	public void setImage() {
		this.ghost_up = Texture.ghost_up;
		this.ghost_left = Texture.ghost_left;
		this.ghost_down = Texture.ghost_down;
		this.ghost_right = Texture.ghost_right;
		currentImage(3);
	}

}
