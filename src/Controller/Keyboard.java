package Controller;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Model.Handler;
import Model.Creatures.Alive;
import Model.Creatures.Player;
import Model.Inventory.Inventory;

public class Keyboard implements KeyListener, StateObserver {
	/*
	 * Re�oit un event et agit en cons�quence. Une m�thode de Player ou de
	 * Inventory sera appel�e.
	 */
	private Inventory inventory;
	private Alive player;
	private boolean[] keys;
	private int speed;
	public boolean up, down, left, right, e, u, del, active = false;
	private Handler handler;

	@Override
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
		int modifiers = event.getModifiers();

		/*
		 * Si la touche MAJ est laiss�e enfonc�e, le joueur court. Sa vitesse
		 * d'avance est de 10 pixels. Sinon, il marche et sa vitesse d'avance
		 * est de 5 pixels. 8 directions sont possibles. Lorsqu'une touche est
		 * enfonc�e, dans le tableau keys, � la place correspondant � son
		 * KeyCode, une valeur true est enregistr�e. Lorsque la touche est
		 * rel�ch�e, une valeur false est enregistr�e.
		 */
		
		if (player == null || inventory == null)
			return;

		// Vitesse de d�placement
		if ((modifiers & InputEvent.SHIFT_MASK) == 0)
			speed = 5;
		else
			speed = 10;

		if (key < 0 || key >= keys.length)
			return;
		
		keys[key] = true;

		// On r�cup�re l'�tat de chaque touche : enfonc�e ou non
		up = keys[KeyEvent.VK_Z];
		left = keys[KeyEvent.VK_Q];
		down = keys[KeyEvent.VK_S];
		right = keys[KeyEvent.VK_D];
		e = keys[KeyEvent.VK_E];
		u = keys[KeyEvent.VK_SPACE];
		del = keys[KeyEvent.VK_DELETE];

		// Inventaire non actif : le joueur se d�place
		if (!active && inventory != null) {
			if (up && left) {
				player.move(-speed, -speed);
			} else if (up && right) {
				player.move(speed, -speed);
			} else if (down && left) {
				player.move(-speed, speed);
			} else if (down && right) {
				player.move(speed, speed);
			} else if (up) {
				player.move(0, -speed);
			} else if (left) {
				player.move(-speed, 0);
			} else if (down) {
				player.move(0, speed);
			} else if (right) {
				player.move(speed, 0);
			} else if (e) {
				handler.getMap().pauseObjects(false);
				inventory.open();
				active = true;
			}
			// Inventaire actif : action dans l'inventaire.
		} else if (up) {
			inventory.incrementSelectedItem(-1);
		} else if (down) {
			inventory.incrementSelectedItem(1);
		} else if (u) {
			inventory.useItem();
		} else if (del) {
			inventory.deleteItem();
		} else if (e) {
			inventory.close();
			handler.getMap().startObjects();
			active = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent event) {
	}

	@Override
	public void keyReleased(KeyEvent event) {
		// Touche rel�ch�e
		int key = event.getKeyCode();
		if (key < 0 || key >= keys.length)
			return;
		keys[key] = false;
	}

	@Override
	public void stateChange(Handler handler) {
		// R�f�rents mis � jour dans le cas du chargement d'une ancienne partie.
		if (handler.getPlayer() != null) {
			this.handler = handler;
			this.player = (Player) handler.getPlayer();
			this.inventory = ((Player) handler.getPlayer()).getInventory();
		}
		keys = new boolean[256];
	}
}