package Controller;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import Model.Handler;
import Model.ObjectMaker;
import Model.Creatures.Player;
import View.Window;

public class Mouse implements MouseListener, StateObserver {
	private Player player;
	private Window window;
	private ObjectMaker game;
	private boolean init = true, init2 = true;

	@Override
	public void mouseClicked(MouseEvent event) {
		/* Dans le menu d'accueil. */
		int mx = event.getX();
		int my = event.getY();

		if (!init && !init2)
			return;
		else if (init && init2) {
			if (window.getMenu().getNewGameButton().contains(mx, my)) {
				window.launchPan();
				init = false;
			} else if (window.getMenu().getLoadGameButton().contains(mx, my)) {
				window.launchPan();
				init = false;
			} else if (window.getMenu().getQuitButton().contains(mx, my)) {
				System.exit(1);
				init = false;
			}
		} else if (!init && init2) {
			if (window.getMenu().getNewGameButton().contains(mx, my)) {
				game.startGame(true);
				window.launchPan();
				init2 = false;
			} else if (window.getMenu().getLoadGameButton().contains(mx, my)) {
				game.startGame(false); // pour l'instant
				window.launchPan();
				init2 = false;
			} else if (window.getMenu().getQuitButton().contains(mx, my)) {
				System.exit(1);
				init2 = false;
			}

		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		// L'utilisateur a cliqué sur le plateau de jeu.
		if (player == null)
			return;
		player.click(event.getX(), event.getY());
	}

	@Override
	public void mouseReleased(MouseEvent event) {
	}

	@Override
	public void stateChange(Handler handler) {
		// Référents mis à jour dans le cas du chargement d'une ancienne partie.
		if (handler.getPlayer() != null) {
			this.player = (Player) handler.getPlayer();
		}
		this.window = handler.getWindow();
		this.game = handler.getGame();
	}

	public void init() {
		this.init = true;
		this.init2 = true;
	}

}
