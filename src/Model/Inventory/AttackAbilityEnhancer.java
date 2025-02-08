package Model.Inventory;

import Model.Handler;
import View.Graphics.Texture;

public class AttackAbilityEnhancer extends Item {
	/* Augmente les dégâts de l'arme utilisée. */
	private static final long serialVersionUID = 5532785985090296487L;
	private Handler handler;

	public AttackAbilityEnhancer(int posX, int posY, int rad, Handler handler) {
		super(posX, posY, rad);
		this.handler = handler;
		id = 4;
		setImage();
		setName("Augmentation d'attaque");
	}

	@Override
	synchronized public void use() {
		handler.getPlayer().getCurrentWeapon().increaseEffect();
		super.setCount(super.getCount() - 1);
	}

	@Override
	public void setImage() {
		setImage(Texture.attackAbilityEnhancer);
	}

}
