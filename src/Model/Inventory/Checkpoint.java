package Model.Inventory;

import Model.Handler;
import View.Graphics.Texture;

public class Checkpoint extends Item {
	/* Livre permettant la sauvegarde du jeu à l'instant où il est utilisé. */
	private static final long serialVersionUID = -3482442889941807000L;
	private Handler handler;
	private boolean used = false;

	public Checkpoint(int posX, int posY, int rad, Handler handler) {
		super(posX, posY, rad);
		this.handler = handler;
		setImage();
		id = 2;
		setName("Sauvegarde");
	}

	@Override
	public void setImage() {
		setImage(Texture.book);
	}

	@Override
	public void use() {
		super.setCount(super.getCount() - 1);
		if (!used && handler != null) {
			used = true;
			handler.getGame().serialize();
		} else
			return;
	}
}
