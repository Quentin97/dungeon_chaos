package Model.PopUp;

import java.util.Observable;

public class PopUp extends Observable implements Runnable {
	/* Pop-up apparaissant 1/2 seconde */
	private Thread t;
	private int x, y, duration = 500;
	private String text;
	private PopUpManager pom;
	private int speed = 1;

	public PopUp(int x, int y, String text, boolean random) {
		this.x = x;
		this.y = y;
		this.text = text;
		if (random) {
			this.x += 5 * (int) (Math.random() * 10);
			this.speed = (int) (Math.random() * 3);
		}
		t = new Thread(this);
		t.start();
	}

	public void run() {
		int count = 0;
		while (count < this.duration / 10.0) {
			try {
				Thread.sleep(10);
				y -= (1 + speed);
				count += 1;
				setChanged();
				notifyObservers();
			} catch (InterruptedException e) {
				System.out.println("Erreur pour un pop up");
				e.printStackTrace();
			}
		}
		isDead();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getText() {
		return text;
	}

	private void isDead() {
		if (pom != null)
			pom.update(this);
	}

	public void setPom(PopUpManager pom) {
		this.pom = pom;
	}

}
