package Model.Inventory;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import Model.GameObject;
import View.Panel;
import View.Graphics.Text;
import View.Graphics.Texture;

public class Inventory extends Observable implements Serializable {
	/*
	 * Inventaire. Contient les objets ramassés et indique leur nombre. Hérite
	 * de Observable pour notifier le panel quand l'inventaire est ouvert et les
	 * changements qui y sont effectués afin que le panel se rafraîchisse. Si
	 * l'objet ramassé n'est pas encore dans l'inventaire, il est ajouté dans
	 * une ArrayList. Sinon, il disparait et l'attribut count est décrémenté.
	 * Les objets récupérables et utilisables sont aussi bien des armes que des
	 * items ou des sorts.
	 */

	private static final long serialVersionUID = -6231596962341120274L;
	private transient boolean active = false; // inventaire ouvert
	private int selectedItem;
	private ArrayList<Pickable> inventoryItems;
	private int invX = 544, invY = 227, invWidth = 512, invHeight = 384, invListCenterX = invX + 171,
			invListCenterY = invY + invHeight / 2 + 5, invListSpacing = 30;
	private int invImageX = invX + 392, invImageY = invY + 42, invImageWidth = 50, invImageHeight = 50;
	private int invCountX = invX + 417, invCountY = invY + 135, invUseX = invX + 419, invUseY = invListCenterY;
	private static final int size = 15;

	public Inventory() {
		inventoryItems = new ArrayList<Pickable>();
		selectedItem = 0;
	}

	//////////////////// FONCTIONS PROPRES A L'INVENTAIRE ////////////////////

	synchronized public void open() {
		active = true;
		if (inventoryItems != null)
			synchronized (inventoryItems) {
				for (Pickable item : inventoryItems)
					synchronized (item) {
						if (item.getCount() <= 0)
							inventoryItems.remove(item);
					}
			}
		setChanged();
		notifyObservers();
	}

	public void close() {
		active = false;
		setChanged();
		notifyObservers();
	}

	public boolean isActive() {
		return active;
	}

	public boolean isFull() {
		int count = 0;
		for (Pickable item : inventoryItems) {
			count += item.getCount();
		}
		if (count >= size) {
			return true;
		} else
			return false;
	}

	//////////////////// GESTION DES ITEMS ////////////////////

	public ArrayList<Pickable> getInventoryItems() {
		return inventoryItems;
	}

	public void add(Pickable item) {
		/* Ajoute l'objet récupéré à l'inventaire. */
		if (inventoryItems.size() < size) {
			for (Pickable i : inventoryItems) {
				if (i.getId() == item.getId() && i.getCount() < size) {
					i.setCount(i.getCount() + item.getCount());
					return;
				}
			}
			inventoryItems.add(item);
		}
	}

	public void useItem() {
		/* Envoie un message à l'objet sélectionné pour qu'il soit utilisé. */
		if (inventoryItems.size() != 0 && inventoryItems.get(selectedItem).getCount() > 0) {
			inventoryItems.get(selectedItem).use();
		}
		if (inventoryItems.get(selectedItem).getCount() <= 0) {
			deleteItem(inventoryItems.get(selectedItem));
		}
		setChanged();
		notifyObservers();
	}

	public void deleteItem() {
		deleteItem(inventoryItems.get(selectedItem));
	}

	public void deleteItem(Pickable item) {
		/* Envoie un message à l'objet sélectionné pour qu'il soit supprimé. */
		if (inventoryItems.size() != 0) {
			if (item instanceof Item) {
				item.delete();
				if (item.getCount() <= 0) {
					if (inventoryItems.size() > 0 && inventoryItems.get(selectedItem) == item && selectedItem != 0)
						selectedItem--;
					inventoryItems.remove(item);
				}
			}
		}
		setChanged();
		notifyObservers();
	}

	public void incrementSelectedItem(int i) {
		if (selectedItem + i < 0 || selectedItem + i >= inventoryItems.size()) {
			return;
		}
		selectedItem += i;
		setChanged();
		notifyObservers();
	}

	//////////////////// FONCTION GRAPHIQUE ////////////////////

	public void render(Graphics g, Panel map) {
		/*
		 * Permet de dessiner l'inventaire, le nom des objets ramassés, leur
		 * image et leur nombre.
		 */
		if (!active)
			return;

		g.drawImage(Texture.inventory, invX, invY, invWidth, invHeight, map);

		int len = inventoryItems.size();
		if (len == 0 || selectedItem >= len)
			return;

		Font font1 = new Font("Times New Roman", Font.BOLD, 20);
		Font font2 = new Font("Times New Roman", Font.BOLD, 15);

		// Nom des objets récupérés pour chaque "ligne" de l'inventaire
		for (int i = -5; i < 6; i++) {
			if (selectedItem + i < 0 || selectedItem + i >= len)
				continue;
			if (i == 0) {
				Text.drawString(g, "> " + inventoryItems.get(selectedItem + i).getName() + " <", invListCenterX,
						invListCenterY + i * invListSpacing, true, Color.YELLOW, font1);
			} else {
				Text.drawString(g, inventoryItems.get(selectedItem + i).getName(), invListCenterX,
						invListCenterY + i * invListSpacing, true, Color.WHITE, font1);
			}
		}

		Pickable item = inventoryItems.get(selectedItem);
		// Image des objets
		g.drawImage(((GameObject) item).getImage(), invImageX, invImageY, invImageWidth, invImageHeight, null);

		// Nombre d'objets récupérés
		Text.drawString(g, Integer.toString(item.getCount()), invCountX, invCountY, true, Color.WHITE, font1);
		Text.drawString(g, "Utiliser (Espace)", invUseX, invUseY, true, Color.WHITE, font2);
		Text.drawString(g, "Utiliser (Delete)", invUseX, invUseY + 46, true, Color.WHITE, font2);
	}

}
