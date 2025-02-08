package Model.Blocks;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import Model.GameObject;
import Model.Explodable.Explodable;
import Model.Explodable.ExplodableObserver;
import View.Graphics.Texture;

public class BreakableBlock extends GameObject implements ExplodableObserver, Serializable {
	private static final long serialVersionUID = -5112839317863543787L;
	private double state;
	private static double crackingResistance = 2;
	private transient BufferedImage[] breakableBlock;

	public BreakableBlock(int posX, int posY, int rad, double state) {
		super(posX, posY, rad);
		this.state = state;
		setImage();
	}

	public void setImage() {
		this.breakableBlock = Texture.breakableBlock;
		setImage(breakableBlock[0]);
	}

	public boolean isObstacle() {
		return true;
	}

	@Override
	public void pause() {
	}

	@Override
	public void start() {
		setImage();
	}

	@Override
	public void exploded(Explodable exp) {
		/* Le bloc se fissure au fur et à mesure qu'il subit des dégâts. */
		GameObject object = (GameObject) exp;
		if (this.isInField(object.getPosX() + object.getRad() / 2, object.getPosY() + object.getRad() / 2,
				object.getRad(), exp.getFactor())) {
			state += exp.getEffect() / crackingResistance;
			if (state <= 0) {
				demisableNotifyObserver();
			} else if (state < 2) {
				setImage(breakableBlock[3]);
			} else if (state < 5) {
				setImage(breakableBlock[2]);
			} else if (state < 10) {
				setImage(breakableBlock[1]);
			}
		}
	}

}
