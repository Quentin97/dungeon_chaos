package Model.Weapons;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import Model.GameObject;
import Model.Handler;
import Model.Explodable.Explodable;
import Model.Explodable.ExplodableObserver;
import Model.PopUp.PopUp;
import View.Panel;
import View.Graphics.Texture;

public class Arrow extends GameObject implements Runnable, Explodable, ExplodableObserver, Serializable {
	/*
	 * Flèche qui se dirige automatiquement vers sa cible. C'est un Thread. A
	 * chaque pas, la flèche notifie à tous ses observateurs qu'elle avance. Si
	 * l'un des observateur est dans son chemin, elle lui-communique qu'elle a
	 * "explosé" et meurt.
	 */

	private static final long serialVersionUID = 7938371623675134560L;
	private int factor = 1, step = 2, duration = 2000, effect;
	private boolean detonated = false;
	private ArrayList<ExplodableObserver> explodableObservers = new ArrayList<ExplodableObserver>();
	private transient BufferedImage[] arrows;
	private int[] target;
	private ExplodableObserver thrower;
	private transient Thread tArrow;
	private Handler handler;
	private boolean inAction;
	private transient boolean init = false;

	public Arrow(int posX, int posY, int rad, int factor, int[] target, int effect, Handler handler) {
		super(posX, posY, rad);
		this.factor = factor;
		this.target = target;
		this.effect = effect;
		this.handler = handler;
		arrows = Texture.arrow;
		start();
	}
	
	//////////////////// DEMARRAGE / ARRET ////////////////////

	@Override
	public void start() {
		if (!init) {
			tArrow = new Thread(this);
			tArrow.start();
			init = true;
			inAction = true;
			setImage();
		} else {
			synchronized (this) {
				inAction = true;
				this.notify();
			}
		}
	}

	@Override
	public void pause() {
		inAction = false;
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	@Override
	public int getFactor() {
		return factor;
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

	//////////////////// IMAGE ////////////////////

	private void currentImage(int nr) {
		super.setImage(arrows[nr]);
	}
	

	public void setImage() {
		arrows = Texture.arrow;
	}

	@Override
	public void render(Graphics g, Panel map) {
		g.drawImage(super.getImage(), super.getPosX() + super.getRad() / 2, super.getPosY() + super.getRad() / 2,
				super.getRad(), super.getRad(), map);
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
		while (!detonated && count < this.duration / 10.0 && super.getPosX() != target[0]
				&& super.getPosY() != target[1]) {
			try {
				Thread.sleep(10);
				count = count + 1;
				followTheLine();
				this.explodableNotifyObserver();
				if (!inAction)
					synchronized (this) {
						this.wait();
					}
			} catch (InterruptedException e) {
				System.out.println("Erreur pour le thread d'une flèche");
				e.printStackTrace();
			}
		}

		this.demisableNotifyObserver();
	}

	//////////////////// MOUVEMENT ////////////////////

	private void followTheLine() {
		/* La flèche se dirige vers sa cible. */
		int myX = super.getPosX();
		int myY = super.getPosY();
		int distX = myX - target[0] - 25;
		int distY = myY - target[1] - 25;
		int nextX = (int) (myX - Math.signum(distX) * step);
		int nextY = (int) (myY - Math.signum(distY) * step);

		if (super.getImage() == null) {
			if (distX < 50 && distX > -50 && distY >= 0) // haut
				currentImage(0);
			else if (distX < 50 && distX > -50 && distY < 0) // gauche
				currentImage(2);
			else if (distY < 50 && distY > -50 && distX >= 0) // bas
				currentImage(1);
			else if (distY < 50 && distY > -50 && distX < 0) // droite
				currentImage(3);
			else if (distX > 0 && distY > 0) // haut gauche
				currentImage(4);
			else if (distX > 0 && distY < 0) // bas gauche
				currentImage(5);
			else if (distX < 0 && distY < 0) // bas droite
				currentImage(6);
			else if (distX < 0 && distY > 0) // haut droite
				currentImage(7);
		}
		super.setPosX(nextX);
		super.setPosY(nextY);
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
				if (!eo.equals(thrower) && inAction) {
					GameObject object = (GameObject) eo;
					if (this.isInField(object.getPosX(), object.getPosY(), object.getRad(), factor)) {
						this.detonated = true;
						eo.exploded(this);
					}
				}
			}
		}
	}

	//////////////////// INTERFACE EXPLODABLEOBSERVER ////////////////////

	@Override
	public void exploded(Explodable exp) {
		GameObject object = (GameObject) exp;
		if (this.isInField(object.getPosX(), object.getPosY(), object.getRad(), exp.getFactor())) {
			this.detonated = true;
			this.inAction = false;
			this.demisableNotifyObserver();
		}
	}

}