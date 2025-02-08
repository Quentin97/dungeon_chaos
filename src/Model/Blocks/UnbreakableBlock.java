package Model.Blocks;

import java.io.Serializable;
import Model.GameObject;
import View.Graphics.Texture;

public class UnbreakableBlock extends GameObject implements Serializable {
	private static final long serialVersionUID = -5112839317863543787L;

	public UnbreakableBlock(int posX, int posY, int rad) {
		super(posX, posY, rad);
		setImage();
	}
	
	public void setImage() {
		setImage(Texture.unbreakableBlock);
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

}
