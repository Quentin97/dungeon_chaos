package Model.Blocks;

import java.awt.image.BufferedImage;

import Model.GameObject;
import Model.Handler;
import Model.Creatures.Alive;
import Model.Creatures.MovementObserver;
import View.Graphics.Texture;

public class DoorIn extends GameObject implements MovementObserver {
	private static final long serialVersionUID = 5840311495780942813L;
	private Handler handler;
	private transient BufferedImage[] doorIn;
	private boolean open = false, alreadyOpened = false;

	public DoorIn(int posX, int posY, int rad, Handler handler) {
		super(posX, posY, rad);
		this.handler = handler;
		setImage();
	}

	@Override
	public void setImage() {
		doorIn = new BufferedImage[2];
		doorIn[0] = Texture.doorIn[0];
		doorIn[1] = Texture.doorIn[1];
		if (!alreadyOpened)
			setImage(doorIn[0]);
		else
			setImage(doorIn[1]);
	}

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

	@Override
	public void personMoved(Alive pers) {
		if (pers.isInField(getPosX(), getPosY(), getRad(), 0.4) && !open) {
			// Prévient qu'un changement de labyrinthe est à opérer si le joueur
			// passe la porte.
			open = true;
			alreadyOpened = true;
			handler.getMap().previousMaze();
			open = false;
		}
	}

}
