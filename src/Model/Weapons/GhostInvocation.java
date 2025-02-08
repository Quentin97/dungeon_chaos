package Model.Weapons;

import Model.GameObject;
import Model.Handler;
import Model.Chrono.Chrono;
import Model.Creatures.Alive;
import Model.Creatures.Ghost;
import Model.Creatures.Player;
import View.Graphics.Texture;

public class GhostInvocation extends GameObject implements DamageMaker {
	/*
	 * Permet d'invoquer des fantômes. Un temps d'au moins 20s doit s'être
	 * écoulé depuis la précédente invocation.
	 */
	private static final long serialVersionUID = -5523409107430058003L;
	private boolean pickedUp = false;
	private final int id, count = 1;
	private int numberLives = 5, duration = 0;
	private Alive thrower;
	private Handler handler;
	private Chrono chrono;

	public GhostInvocation(int posX, int posY, int rad, Alive thrower, Handler handler) {
		super(posX, posY, rad);
		this.thrower = thrower;
		this.handler = handler;
		setImage();
		id = 16;
		chrono = new Chrono();
	}

	public void setImage() {
		setImage(Texture.ghost_head);
	}

	@Override
	public void pickUp(Alive pers) {
		if (pers instanceof Player) {
			((Player) pers).newWeapon(this);
		}
		demisableNotifyObserver();
		pickedUp = true;
	}

	@Override
	public void use() {
		((Player) handler.getPlayer()).setCurrentWeapon(this);
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setCount(int count) {
		return;
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public String getName() {
		return "Invocation de fantôme";
	}

	@Override
	public void delete() {
		return;
	}

	@Override
	public boolean isPickedUp() {
		return pickedUp;
	}

	@Override
	public void action(int[] target) {
		if (chrono.ready(duration)) {
			Ghost ghost = new Ghost(thrower.getPosX(), thrower.getPosY(), 100, numberLives, null, handler);
			handler.getMap().addObject(ghost);
			ghost.ownerIsPlayer();
			ghost.start();
			chrono.begin();
			duration = 20000;
		}
	}

	@Override
	public int getFactor() {
		return 0;
	}

	@Override
	public void increaseEffect() {
		numberLives++;
	}

	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public void pause() {
		if (pickedUp)
			chrono.pauseBegin();
	}

	@Override
	public void start() {
		if (pickedUp) {
			setImage();
			chrono.pauseEnd();
		}
	}

}
