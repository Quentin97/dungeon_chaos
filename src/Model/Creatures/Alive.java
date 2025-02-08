package Model.Creatures;

import Model.Inventory.Inventory;
import Model.Weapons.DamageMaker;

public interface Alive {
	/* Interface des créatures. */

	abstract Integer getPosX();

	abstract Integer getPosY();

	abstract Integer getRad();

	abstract Integer getLives();

	abstract void incrementLives(int quantity);

	abstract void move(int x, int y);

	abstract boolean isInField(int x, int y, int rad, double factor);

	abstract DamageMaker getCurrentWeapon();

	abstract Inventory getInventory();
}
