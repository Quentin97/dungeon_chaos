package Model.Weapons;

import java.io.Serializable;
import java.util.ArrayList;

import Model.GameObject;
import Model.Handler;
import Model.Explodable.Explodable;
import Model.Explodable.ExplodableObserver;
import Model.PopUp.PopUp;
import View.Graphics.Texture;

public class Bomb extends GameObject implements Runnable, Explodable, ExplodableObserver, Serializable {
	/*
	 * Bombe qui une fois déposée, explose après un temps prédéfini, à moins
	 * qu'une autre bombe ait explosé avant, ou qu'une flèche l'ait touchée.
	 */

	private static final long serialVersionUID = -7282563926162789160L;
	private int factor, duration = 3000, effect;
	private boolean detonated = false;
	private ArrayList<ExplodableObserver> explodableObservers = new ArrayList<ExplodableObserver>();
	private ExplodableObserver thrower;
	private transient Thread tBomb;
	private Handler handler;
	private boolean inAction;
	private transient boolean init = false;

	public Bomb(int posX, int posY, int rad, int factor, int effect, Handler handler) {
		super(posX, posY, rad);
		this.factor = factor;
		this.effect = effect;
		this.handler = handler;
		setImage();
		start();
	}

	@Override
	public void start() {
		if (!init) {
			tBomb = new Thread(this);
			tBomb.start();
			init = true;
			inAction = true;
			setImage();
		} else {
			synchronized (this) {
				inAction = true;
				this.notify();
			}
		}
		super.setChanged();
		super.notifyObservers();
	}

	@Override
	public void pause() {
		inAction = false;
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	@Override
	public int getFactor() {
		return this.factor;
	}

	@Override
	public int getEffect() {
		if ((int) (Math.random() * 10) == 0) {
			effect *= 2;
			handler.getGame().setPopUp(new PopUp(getPosX(), getPosY(), "Coup critique", true));
		}
		return effect;
	}

	@Override
	public void setThrower(ExplodableObserver thrower) {
		this.thrower = thrower;
	}

	@Override
	public ExplodableObserver getThrower() {
		return thrower;
	}

	@Override
	public void setImage() {
		setImage(Texture.bomb);
	}

	//////////////////// OBJET PHYSIQUE ////////////////////

	@Override
	public boolean isObstacle() {
		return false;
	}

	//////////////////// INTERFACE RUNNABLE ////////////////////

	@Override
	public void run() {
		int count = 0;
		while (!this.detonated && count < this.duration / 10.0) {
			try {
				Thread.sleep(10);
				count = count + 1;
				setChanged();
				notifyObservers();
				if (!inAction)
					synchronized (this) {
						this.wait();
					}
			} catch (InterruptedException e) {
				System.out.println("Erreur pour le thread d'une bombe");
				e.printStackTrace();
			}
		}
		this.explodableNotifyObserver();
		setImage(Texture.explosion);
		setChanged();
		notifyObservers();
		try {
			Thread.sleep(500); // pour afficher l'image de l'explosion 1/2
								// seconde
		} catch (InterruptedException e) {
			System.out.println("Erreur pour le thread d'une bombe");
			e.printStackTrace();
		}
		this.demisableNotifyObserver();
	}

	//////////////////// INTERFACE EXPLODABLE ////////////////////

	@Override
	public void explodableAttach(ExplodableObserver eo) {
		synchronized (explodableObservers) {
			explodableObservers.add(eo);
		}
	}

	@Override
	public void explodableNotifyObserver() {
		synchronized (explodableObservers) {
			for (ExplodableObserver eo : explodableObservers) {
				if (!eo.equals(thrower))
					eo.exploded(this);
			}
		}
	}

	//////////////////// INTERFACE EXPLODABLEOBSERVER ////////////////////

	@Override
	public void exploded(Explodable exp) {
		GameObject object = (GameObject) exp;
		if (this.isInField(object.getPosX(), object.getPosY(), object.getRad(), exp.getFactor())) {
			this.detonated = true;
		}
	}

}
