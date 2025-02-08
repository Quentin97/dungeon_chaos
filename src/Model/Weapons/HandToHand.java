package Model.Weapons;

import java.awt.Graphics;
import java.io.Serializable;

import Model.GameObject;
import Model.Handler;
import Model.Creatures.Alive;
import Model.Creatures.Player;
import Model.Explodable.ExplodableObserver;
import View.Panel;
import View.Graphics.Texture;

public class HandToHand extends GameObject implements DamageMaker, Serializable {
	/* Arme de corps à corps. */
	private static final long serialVersionUID = 5531360773325748952L;
	private static final int factor = 1;
	private ExplodableObserver thrower;
	private Handler handler;
	private boolean pickedUp;
	private int id, count = 1, effect = -1;

	public HandToHand(int posX, int posY, int actionRad, ExplodableObserver thrower, Handler handler) {
		super(posX, posY, actionRad);
		this.handler = handler;
		this.thrower = thrower;
		setImage();
		id = 11;
	}

	public void setImage() {
		setImage(Texture.fist);
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	@Override
	public int getFactor() {
		return factor;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setCount(int count) {
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public String getName() {
		return "Attaque corps à corps";
	}

	@Override
	public void render(Graphics g, Panel map) {
	}

	//////////////////// FONCTION : ARME ////////////////////

	@Override
	public void action(int[] target) {
		/* Décrémente le nombre de vies du premier ennemi trouvé à proximité. */
		synchronized (handler.getMap().getGameObjects()) {
			for (GameObject object : handler.getMap().getGameObjects()) {
				if (object instanceof Alive && !object.equals(thrower)) {
					if (object.isInField(this.getPosX(), this.getPosY(), this.getRad(), factor)) {
						((Alive) object).incrementLives(effect);
						return;
					}
				}
			}
		}
	}

	@Override
	public void increaseEffect() {
		effect--;
	}

	//////////////////// OBJET PHYSIQUE ////////////////////

	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public void pause() {
	}

	@Override
	public void start() {
		setImage();
	}

	//////////////////// UTILISABLE ////////////////////

	@Override
	public void pickUp(Alive player) {
		((Player) player).newWeapon(this);
		pickedUp = true;
	}

	@Override
	public boolean isPickedUp() {
		return pickedUp;
	}

	@Override
	public void use() {
		((Player) handler.getPlayer()).setCurrentWeapon(this);
	}

	@Override
	public void delete() {
	}

}
