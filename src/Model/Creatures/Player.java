package Model.Creatures;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import Model.GameObject;
import Model.Handler;
import Model.Explodable.Explodable;
import Model.Explodable.ExplodableObserver;
import Model.Inventory.Inventory;
import Model.Inventory.Pickable;
import Model.PopUp.PopUp;
import Model.Weapons.HandToHand;
import View.Graphics.Texture;
import Model.Weapons.DamageMaker;

public class Player extends GameObject implements Alive, Moveable, ExplodableObserver, Serializable {
	/*
	 * Possède une arme de type HandToHand par défaut. Peut ramasser des armes
	 * et des objets au fur et à mesure de son avancée dans le jeu. Possède un
	 * inventaire.
	 */
	private static final long serialVersionUID = 7386467683591668720L;
	private ArrayList<MovementObserver> movementObservers = new ArrayList<MovementObserver>();
	private DamageMaker weapon;
	private int lives, column = 0;
	private transient BufferedImage[] player_up, player_left, player_down, player_right;
	private boolean change = true, pause = false;
	private Inventory inventory;
	private Handler handler;

	public Player(int posX, int posY, int rad, int lives, Handler handler) {
		super(posX, posY, rad);
		this.lives = lives;
		this.handler = handler;
		inventory = new Inventory();
		weapon = new HandToHand(posX, posY, rad, this, handler);
		weapon.pickUp(this);
		setImage();
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	@Override
	public Integer getLives() {
		if (lives > 0)
			return lives;
		else
			return 0;
	}

	@Override
	public void incrementLives(int quantity) {
		lives += quantity;
		if (lives <= 0) {
			demisableNotifyObserver();
		}
		handler.getGame().setPopUp(new PopUp(super.getPosX(), super.getPosY(), String.valueOf(quantity), true));
	}

	public Inventory getInventory() {
		return inventory;
	}

	//////////////////// REAGIT A LA SOURIS ////////////////////

	public void click(int x, int y) {
		/* Méthode appelée lorsque la souris détecte un événement. */
		if (!inventory.isActive() && !pause) {
			synchronized (handler.getMap().getItems()) {
				for (Pickable item : handler.getMap().getItems()) {
					if (((GameObject) item).isInField(x - 25, y - 25, this.getRad(), 1) && !inventory.isFull()) {
						// ramasse l'objet
						item.pickUp(this);
						return;
					}
				}
			}
			// tire
			int[] target = new int[2];
			target[0] = x - 100;
			target[1] = y - 100;
			shoot(target);
		} else
			return;
	}

	//////////////////// ARMER & TIRER ////////////////////

	public void newWeapon(DamageMaker weapon) {
		inventory.add(weapon);
	}

	public void setCurrentWeapon(DamageMaker weapon) {
		this.weapon = weapon;
		((GameObject) weapon).setPosX(super.getPosX());
		((GameObject) weapon).setPosY(super.getPosY());
	}
	
	@Override
	public DamageMaker getCurrentWeapon() {
		return weapon;
	}

	private void shoot(int[] target) {
		weapon.action(target);
	}

	//////////////////// DEPLACEMENT ////////////////////

	@Override
	public void move(int x, int y) {
		/*
		 * Permet au joueur de se déplacer en la position (x + PosX, y + PosY)
		 * si cette position n'est pas celle d'un obstacle.
		 */
		if (!pause) {
			int nextX = super.getPosX() + x;
			int nextY = super.getPosY() + y;
			if (nextX < 20
					|| nextX > handler.getScreenSize()[0] - 105 || nextY < 30
					|| nextY > handler.getScreenSize()[1] - 150)
				return;
			if (!handler.getMap().presenceObstacle(nextX, nextY, super.getRad())) {
				super.setPosX(nextX);
				super.setPosY(nextY);
				((GameObject) weapon).setPosX(nextX);
				((GameObject) weapon).setPosY(nextY);
				if (x == 0 && y < 0)
					currentImage(1);
				else if (x == 0 && y > 0)
					currentImage(3);
				else if (x < 0)
					currentImage(2);
				else if (x > 0)
					currentImage(4);
				notifyMovementObserver();
			}
		}
	}

	//////////////////// OBJET PHYSIQUE ////////////////////

	@Override
	public boolean isObstacle() {
		return false;
	}
	
	//////////////////// DEMARRAGE / ARRET ////////////////////

	@Override
	public void pause() {
		pause = true;
	}

	@Override
	public void start() {
		pause = false;
		setImage();
	}

	//////////////////// IMAGE ////////////////////

	public void currentImage(int row) {
		/*
		 * Détermine l'image actuelle du joueur en fonction de son orientation.
		 */
		if (change) { // Divise le nombre de fps par 2.
			column++;
			if (column >= 9)
				column = 0;
			if (row == 1)
				setImage(player_up[column]);
			else if (row == 2)
				setImage(player_left[column]);
			else if (row == 3)
				setImage(player_down[column]);
			else if (row == 4)
				setImage(player_right[column]);
			change = false;
		} else
			change = true;
	}

	public void setImage() {
		this.player_up = Texture.player_up;
		this.player_left = Texture.player_left;
		this.player_down = Texture.player_down;
		this.player_right = Texture.player_right;
		currentImage(3);
	}

	////////// FONCTION DE L'INTERFACE EXPLODABLEOBSERVER //////////

	@Override
	public void exploded(Explodable exp) {
		GameObject object = (GameObject) exp;
		if (this.isInField(object.getPosX(), object.getPosY(), object.getRad(), exp.getFactor()))
			// Le joueur perd des vies s'il est touché. 
			incrementLives(exp.getEffect());
	}

	////////// FONCTION DE L'INTERFACE MOVEABLEPLAYER //////////

	@Override
	public void addMovementObserver(MovementObserver mo) {
		movementObservers.add(mo);
	}
	
	@Override
	public void resetMovementObservers() {
		movementObservers = new ArrayList<MovementObserver>();
	}

	@Override
	public void notifyMovementObserver() {
		if (movementObservers != null)
			synchronized (movementObservers) {
				for (MovementObserver mo : movementObservers) {
					mo.personMoved(this);
				}
			}
	}

}
