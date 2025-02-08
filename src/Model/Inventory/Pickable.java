package Model.Inventory;

import Model.Creatures.Alive;

public interface Pickable {
	/* Interface des objets pouvant être contenus dans l'inventaire. */
	
	public abstract void pickUp(Alive pers);

	public abstract boolean isPickedUp();

	public abstract void use();

	public abstract int getId();

	public abstract void setCount(int count);

	public abstract int getCount();

	public abstract String getName();

	public abstract void delete();
}
