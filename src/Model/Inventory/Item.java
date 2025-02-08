package Model.Inventory;

import java.io.Serializable;
import Model.GameObject;
import Model.Creatures.Alive;

public abstract class Item extends GameObject implements Pickable, Serializable {
	/*
	 * Objets n'ayant d'autre fonction que celles d'être ramassés et utilisés.
	 * Un id est défini pour chaque type d'item.
	 */

	private static final long serialVersionUID = 4385041354178274459L;
	private boolean pickedUp = false;
	protected int id;
	private String name;
	private int count = 1;

	public Item(int posX, int posY, int rad) {
		super(posX, posY, rad);
	}

	//////////////////// SETTERS & GETTERS ////////////////////

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void setCount(int count) {
		this.count = count;
		if (count == 0) {
			delete();
		}
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getCount() {
		return count;
	}

	//////////////////// OBJET PHYSIQUE ////////////////////

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

	//////////////////// OBJET RAMASSABLE ET UTILISABLE ////////////////////

	@Override
	public void pickUp(Alive pers) {
		/*
		 * L'objet s'ajoute à l'inventaire sans se soucier ce que ce dernier
		 * fait de l'information. Ensuite, il est supprimé si l'inventaire ne
		 * l'a pas ajouté.
		 */
		pickedUp = true;
		pers.getInventory().add(this);
		demisableNotifyObserver();
	}

	@Override
	public boolean isPickedUp() {
		count++;
		return pickedUp;
	}

	@Override
	abstract public void use();

	@Override
	public void delete() {
		if (count <= 0)
			demisableNotifyObserver();
		else {
			count--;
			if (count <= 0)
				demisableNotifyObserver();
		}
	}

}
