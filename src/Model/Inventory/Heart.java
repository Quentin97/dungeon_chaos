package Model.Inventory;

import Model.Handler;
import View.Graphics.Texture;

public class Heart extends Item {
	/* Incrémente de 1 le nombre de points de vie du joueur. */
	private static final long serialVersionUID = 6861920671712980779L;
	private Handler handler;

	public Heart(int posX, int posY, int rad, Handler handler) {
		super(posX, posY, rad);
		this.handler = handler;
		setImage();
		id = 1;
		setName("Vies");
	}
	
	@Override
	public void setImage() {
		setImage(Texture.heart);
	}

	@Override
	synchronized public void use() {
		handler.getPlayer().incrementLives(1);
		super.setCount(super.getCount() - 1);
	}

}
