package Model.Creatures;

import java.awt.image.BufferedImage;
import Model.Handler;
import Model.Weapons.Bow;
import View.Graphics.Texture;

public class Skeleton extends Monster {
	private static final long serialVersionUID = 6913327222444843608L;
	/*
	 * Monstre de type squelette. Attaque à distance en tirant des flèches. Il
	 * est capable d'approcher un joueur et de l'attaquer à distance dès qu'il
	 * le détecte.
	 */
	private int column = 0, columnAtt = 7, orientation = 1;
	private transient BufferedImage[] skeleton_up, skeleton_left, skeleton_down, skeleton_right;
	private boolean change = true;
	private transient Thread tSkeleton;
	private Handler handler;
	private transient boolean init = false;

	public Skeleton(int posX, int posY, int rad, int lives, Alive ennemy, Handler handler) {
		super(posX, posY, rad, lives, handler, ennemy);
		this.handler = handler;
		super.setId(13);
		// 
		super.setWeapon(new Bow(posX, posY, rad, this, handler));
		detectionDistance = 16;
		attackDistance = 8;
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
			tSkeleton = new Thread(this);
			tSkeleton.start();
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
		 * distance maximale d'attaque, le squelette l'attaque. S'il est plus loin
		 * que cette distance mais plus près que la distance maximale de
		 * détection, le squelette se déplace jusqu'à ce qu'il puisse attaquer
		 * l'ennemi. Dans les autres cas, le Thread est mis en pause jusqu'à ce
		 * qu'un ennemi lui indique qu'il s'est déplacé.
		 */
		try {
			while (super.getLives() > 0 && handler.getPlayer() != null && inAction) {
				if (super.check(attackDistance) && handler.getPlayer() != null && ennemy.getLives() > 0) {
					setFight(true);
					super.actionShoot();
					for (int i = 1; i <= 12; i++) {
						currentImage(orientation);
						notifyMovementObserver();
						Thread.sleep(timer * 4);
					}
				} else if (super.check(detectionDistance) && handler.getPlayer() != null && ennemy != null) {
					setFight(false);
					super.go(attackDistance - 3, detectionDistance + 4);
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

			if (row == 1)
				setImage(skeleton_up[currentColumn]);
			else if (row == 2)
				setImage(skeleton_left[currentColumn]);
			else if (row == 3)
				setImage(skeleton_down[currentColumn]);
			else if (row == 4)
				setImage(skeleton_right[currentColumn]);
			change = false;
		} else
			change = true;
	}
	
	public void setImage() {
		this.skeleton_up = Texture.skeleton_up;
		this.skeleton_left = Texture.skeleton_left;
		this.skeleton_down = Texture.skeleton_down;
		this.skeleton_right = Texture.skeleton_right;
		currentImage(3);
	}

}
