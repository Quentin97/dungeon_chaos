package Model.Weapons;

import Model.Inventory.Pickable;

public interface DamageMaker extends Pickable {
	/*
	 * H�rite de Pickable car toutes les armes peuvent �tre ramass�es et
	 * contenues dans l'inventaire.
	 */
	abstract void action(int[] target);

	abstract int getFactor();

	abstract void increaseEffect();
}
