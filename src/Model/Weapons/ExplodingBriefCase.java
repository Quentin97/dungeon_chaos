package Model.Weapons;

import java.io.Serializable;

import Model.GameObject;
import Model.Handler;
import Model.Chrono.Chrono;
import Model.Creatures.Alive;
import Model.Creatures.Player;
import Model.Explodable.ExplodableObserver;
import View.Graphics.Texture;

public class ExplodingBriefCase extends GameObject implements DamageMaker, Serializable {
	/*
	 * Pose des bombes. Une nouvelle bombe peut être posée quand la précédente a
	 * été posée au moins 1s avant.
	 */
	private static final long serialVersionUID = 1708875780483692086L;
	private static final int factor = 3;
	private ExplodableObserver thrower;
	private boolean pickedUp = false, pause = false;
	private int id, count = 1, effect = -5;
	private Handler handler;
	private Chrono chrono;
	private long loadingTime = 1000;

	public ExplodingBriefCase(int posX, int posY, int actionRad, ExplodableObserver thrower, Handler handler) {
		super(posX, posY, actionRad);
		this.handler = handler;
		setThrower(thrower);
		setImage();
		id = 13;
		chrono = new Chrono();
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
		return "Lanceur de bombes";
	}

	private void setThrower(ExplodableObserver thrower) {
		this.thrower = thrower;
	}

	public void setImage() {
		setImage(Texture.bomb);
	}

	//////////////////// FONCTION : ARME ////////////////////

	@Override
	public void action(int[] target) {
		if (chrono.ready(loadingTime)) {
			Bomb bomb = new Bomb(getPosX(), getPosY(), 50, factor, effect, handler);
			bomb.setThrower(thrower);
			handler.getMap().expDropped(bomb);
			chrono.begin();
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
		pause = true;
		chrono.pauseBegin();
	}

	@Override
	public void start() {
		if (pause) {
			chrono.pauseEnd();
			pause = false;
		}
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
		((Player) handler.getPlayer()).setCurrentWeapon(this);
	}

	@Override
	public void delete() {
	}

}
