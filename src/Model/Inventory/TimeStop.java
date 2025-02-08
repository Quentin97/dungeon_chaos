package Model.Inventory;

import Model.Handler;
import Model.PopUp.PopUp;
import View.Graphics.Texture;

public class TimeStop extends Item implements Runnable {
	/*
	 * Permet d'arrêter le temps durant 10 secondes. Un décompte s'affiche en
	 * haut de l'écran.
	 */
	private static final long serialVersionUID = 34204170631577156L;
	private Handler handler;
	private transient Thread t;
	private int clock = 10;

	public TimeStop(int posX, int posY, int rad, Handler handler) {
		super(posX, posY, rad);
		this.handler = handler;
		setImage();
		id = 3;
		setName("Arrêt temporel");
	}

	@Override
	synchronized public void use() {
		t = new Thread(this);
		t.start();
		super.setCount(super.getCount() - 1);
	}

	@Override
	public void run() {
		handler.setTimeStop(true);
		handler.getMap().pauseObjects(false);
		while (clock >= 0)
			try {
				handler.getGame().setPopUp(new PopUp(handler.getScreenSize()[0] / 2, 75, String.valueOf(clock), false));
				Thread.sleep(1000);
				clock--;
			} catch (InterruptedException e) {
			}
		handler.setTimeStop(false);
		handler.getMap().startObjects();
	}

	@Override
	public void setImage() {
		setImage(Texture.timeStop);
	}

}
