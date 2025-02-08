package Model.Weapons;

import Model.Inventory.Pickable;

public interface DamageMaker extends Pickable {
	/*
	 * Hérite de Pickable car toutes les armes peuvent être ramassées et
	 * contenues dans l'inventaire.
	 */
	abstract void action(int[] target);

	abstract int getFactor();

	abstract void increaseEffect();
}
