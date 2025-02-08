package Model.Creatures;

import java.util.ArrayList;

import Model.GameObject;
import Model.Handler;
import Model.Explodable.Explodable;
import Model.Explodable.ExplodableObserver;
import Model.Inventory.Inventory;
import Model.PopUp.PopUp;
import Model.Weapons.DamageMaker;

public abstract class Monster extends GameObject
		implements Alive, ExplodableObserver, Runnable, MovementObserver, Moveable {
	private static final long serialVersionUID = 4643608675552010402L;
	protected ArrayList<MovementObserver> movementObservers = new ArrayList<MovementObserver>();
	private int lives, id;
	private DamageMaker weapon;
	protected int timer = 40, step = 2;
	protected double detectionDistance, attackDistance;
	private boolean fight = false;
	private Handler handler;
	protected boolean inAction = true, pause = false, dead = false, hasATarget = false, belongsToPlayer = false;
	protected Alive ennemy;

	public Monster(int posX, int posY, int rad, int lives, Handler handler, Alive ennemy) {
		super(posX, posY, rad);
		this.lives = lives;
		this.ennemy = ennemy;
		this.handler = handler;
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	@Override
	public Integer getLives() {
		return lives;
	}

	public void setFight(boolean fight) {
		this.fight = fight;
	}

	public boolean isFighting() {
		return fight;
	}

	public DamageMaker getCurrentWeapon() {
		return weapon;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

	public abstract void currentImage(int i);

	public Alive getEnnemy() {
		return ennemy;
	}

	public void ownerIsPlayer() {
		belongsToPlayer = true;
	}

	public boolean belongsToPlayer() {
		return belongsToPlayer;
	}

	//////////////////// VIE ////////////////////

	@Override
	synchronized public void incrementLives(int quantity) {
		lives += quantity;
		if (lives <= 0 && !dead) {
			dead = true;
			demisableNotifyObserver();
			pause();
		}
		// Un pop-up indique le nombre de vies perdues.
		handler.getGame().setPopUp(new PopUp(super.getPosX(), super.getPosY(), String.valueOf(quantity), true));
	}

	//////////////////// CHERCHE UNE CIBLE A ATTAQUER ////////////////////

	protected boolean check(double i) {
		/* Vérifie la présence d'un ennemi à proximité. */
		if (handler.getPlayer() == null || ennemy == null)
			return false;
		return ennemy.isInField(super.getPosX(), super.getPosY(), super.getRad(), i);
	}

	protected int[] target(GameObject object) {
		/* Initialise la cible. */
		int[] target = new int[2];
		target[0] = object.getPosX();
		target[1] = object.getPosY();
		return target;
	}

	//////////////////// MOUVEMENT ////////////////////

	private boolean outOfScreen(int nextX, int nextY) {
		// Vérifie que le monstre ne veut pas se déplacer hors du plateau.
		return nextX < 20 || nextX > handler.getScreenSize()[0] - 100 || nextY < 50
				|| nextY > handler.getScreenSize()[1] - 150;
	}

	protected void go(double min, double max) {
		/*
		 * Détermine la prochaine position du monstre. 8 directions de
		 * déplacement sont possibles. Le monstre se déplace tant que sa cible
		 * est située entre une distance minimale et une distance maximale de
		 * lui. Si un obstacle est rencontré, le monstre le contourne.
		 */
		while (!check(min) && check(max) && inAction && ennemy != null) {
			int myX = super.getPosX();
			int myY = super.getPosY();
			int distX = myX - ennemy.getPosX();
			int distY = myY - ennemy.getPosY();
			int nextX = (int) (myX - Math.signum(distX) * step);
			int nextY = (int) (myY - Math.signum(distY) * step);
			int hopeX = (int) (myX - Math.signum(distX) * step);
			int hopeY = (int) (myY - Math.signum(distY) * step);

			try {
				if (outOfScreen(nextX, nextY))
					return;

				if (handler.getMap().presenceObstacle(nextX, myY, super.getRad())) {
					nextX = myX;
				}
				if (handler.getMap().presenceObstacle(myX, nextY, super.getRad())) {
					nextY = myY;
				}

				if (nextX != myX || nextY != myY) {
					move(nextX, nextY);
					if (distX == 0 && distY > 0)
						currentImage(1);
					else if (distX == 0 && distY <= 0)
						currentImage(3);
					else if (distX > 0)
						currentImage(2);
					else if (distX < 0)
						currentImage(4);

					// Si le monstre est bloqué par un obstacle
				} else if (hopeX != myX) {
					while (handler.getMap().presenceObstacle(hopeX, hopeY, super.getRad())
							&& !handler.getMap().presenceObstacle(myX, hopeY + step, super.getRad())
							&& !outOfScreen(myX, hopeY + step) && inAction) {
						hopeY += step;
						move(myX, hopeY);
						currentImage(2);
						Thread.sleep(timer);
					}
					if (!outOfScreen(myX, hopeY + step)) {
						move(myX, super.getPosY() + step);
						currentImage(2);
					}
				} else if (hopeY != myY) {
					while (handler.getMap().presenceObstacle(hopeX, hopeY, super.getRad())
							&& !handler.getMap().presenceObstacle(hopeX + step, myY, super.getRad())
							&& !outOfScreen(hopeX, myY) && inAction) {
						hopeX += step;
						move(hopeX, myY);
						currentImage(4);
						Thread.sleep(timer);
					}
					move(super.getPosX() + step, myY);
					currentImage(4);
				}
				Thread.sleep(timer);
			}

			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void move(int x, int y) {
		/* Permet au monstre de se déplacer en la position (x, y). */
		super.setPosX(x);
		super.setPosY(y);
		if (weapon != null) {
			((GameObject) weapon).setPosX(x);
			((GameObject) weapon).setPosY(y);
		}
		notifyMovementObserver();

	}

	//////////////////// PEUT ATTAQUER ////////////////////

	public void setWeapon(DamageMaker weapon) {
		/* Initialise l'arme du monstre en fonction du monstre. */
		this.weapon = weapon;
	}

	public void actionShoot() {
		shoot(target((GameObject) ennemy));
	}

	private void shoot(int[] target) {
		/* Attaque le joueur en envoyant un message à l'arme. */
		weapon.action(target);
		notifyMovementObserver();
	}

	//////////////////// OBJET PHYSIQUE ////////////////////

	public boolean isObstacle() {
		return false;
	}

	//////////////// FONCTION DE L'INTERFACE EXPLODABLEOBSERVER ////////////////

	@Override
	public void exploded(Explodable exp) {
		GameObject object = (GameObject) exp;
		if (this.isInField(object.getPosX(), object.getPosY(), object.getRad(), exp.getFactor())) {
			incrementLives(exp.getEffect());
		}
	}

	/////////////// INTERFACE MOVEMENTOBSERVER ///////////////

	@Override
	public void personMoved(Alive pers) {
		if (!hasATarget || pers.getLives() > 0) {
			ennemy = pers;
			if (!pause && check(detectionDistance))
				// Thread relancé si un ennemi qui vient de bouger est détecté.
				synchronized (this) {
					inAction = true;
					this.notify();
					pause = false;
					hasATarget = true;
				}
		}
	}

	/////////////// INTERFACE MOVEABLE ///////////////

	@Override
	public void addMovementObserver(MovementObserver mo) {
		movementObservers.add(mo);
	}

	@Override
	public void notifyMovementObserver() {
		if (movementObservers != null && !pause)
			synchronized (movementObservers) {
				for (MovementObserver mo : movementObservers) {
					mo.personMoved(this);
				}
			}
	}

	@Override
	public void resetMovementObservers() {
		return;
	}

}
