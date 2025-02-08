package Model.Weapons;

import Model.GameObject;
import Model.Handler;

import java.io.Serializable;

import Model.Creatures.Alive;
import Model.Creatures.Player;
import Model.Explodable.ExplodableObserver;
import View.Graphics.Texture;

public class Bow extends GameObject implements DamageMaker, Serializable {
	private static final long serialVersionUID = -2141824115134442257L;
	/*
	 * Crée des flèches quand on le lui demande. C'est un objet pouvant être
	 * ramassé (uniquement par le joueur) et contenu dans l'inventaire. Garde en
	 * mémoire celui qui l'a en sa possession.
	 */
	private static final int factor = 1;
	private ExplodableObserver thrower;
	private boolean pickedUp = false;
	private int id, count = 1, effect = -2;
	private Handler handler;

	public Bow(int posX, int posY, int rad, ExplodableObserver thrower, Handler handler) {
		super(posX, posY, rad);
		this.handler = handler;
		setThrower(thrower);
		setImage(Texture.bow);
		id = 12;
	}
	
	public void setImage() {
		setImage(Texture.bow);
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	@Override
	public void setCount(int count) {
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public String getName() {
		return "Arc";
	}

	@Override
	public int getFactor() {
		return factor;
	}
	
	private void setThrower(ExplodableObserver thrower) {
		this.thrower = thrower;
	}

	//////////////////// FONCTION : ARME ////////////////////

	@Override
	public void action(int[] target) {
		Arrow arrow = new Arrow(getPosX(), getPosY(), 50, factor, target, effect, handler);
		arrow.setThrower(thrower);
		// notifie le jeu qu'une flèche a été envoyée.
		handler.getMap().expDropped(arrow);
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

	//////////////////// OBJET RAMASSABLE ET UTILISABLE ////////////////////

	@Override
	public void pickUp(Alive pers) {
		if (pers instanceof Player) {
			setThrower((Player) pers);
			((Player) pers).newWeapon(this);
		}
		demisableNotifyObserver();
		pickedUp = true;
	}

	@Override
	public boolean isPickedUp() {
		return pickedUp;
	}

	@Override
	public void use() {
		/* L'arme du joueur devient celle-ci. */
		((Player) handler.getPlayer()).setCurrentWeapon(this);
	}

	@Override
	public void delete() {
		return;
	}

}
