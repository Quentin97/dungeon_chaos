package Model;

import java.util.ArrayList;
import Controller.StateObserver;
import Model.GameObject;
import Model.Creatures.Player;
import Model.Inventory.Pickable;
import Model.Inventory.ItemGenerator.ItemGenerator;
import Model.PopUp.PopUp;
import Model.PopUp.PopUpManager;
import Model.Serialization.Deserialize;
import Model.Serialization.Serialize;
import View.Window;

public class Game implements ObjectMaker, ChangeableState {
	/*
	 * Classe dont l'instance est utilis�e d�s le lancement de la partie. Game
	 * cr�e un PopUpManager et un Handler (qui conna�t les objets du jeu et la
	 * dimension de l'�cran). Hormis cela, cette classe ne s'occupe que de la
	 * gestion de la s�rialisation et de la d�s�rialisation, du lancement et de
	 * l'arr�t du jeu. L'�tat du jeu est observ� par la souris et le clavier.
	 */

	private ArrayList<StateObserver> stateObservers = new ArrayList<StateObserver>();
	private Window window;
	private Player player;
	private int[] screenSize;
	private transient PopUpManager pom;
	private Handler handler;
	private Map map;

	public Game(Window window) {
		this.window = window;
		pom = new PopUpManager();
		window.setPopUpManager(pom);
		handlerCreation();
	}

	//////////////////// SETTERS & GETTERS ////////////////////
	/* Pour r�cup�rer les attributs objects et player et ajouter items. */

	@Override
	public void setPopUp(PopUp popUp) {
		synchronized (pom) {
			pom.add(popUp);
			popUp.addObserver(window.getPanel());
		}
	}

	@Override
	public int[] getScreenSize() {
		return screenSize;
	}

	//////////////////// CREATION D'UN HANDLER ////////////////////

	private void handlerCreation() {
		handler = new Handler(this, window);
		handler.setGame(this);
		handler.setWindow(window);
		handler.setScreenSize();
	}

	//////////////////// DEMARRAGE / ARRET DU JEU ////////////////////
	
	@Override
	public void startGame(boolean newGame) {
		if (newGame) {
			handlerCreation();
			// Cr�ation d'un joueur
			player = new Player(150, 100, 100, 75, handler);
			handler.setPlayer(player);
			this.window.setPlayer(player);
			this.window.setInventory(player.getInventory());
			map = new Map(handler, player);
		} else {
			try {
				deserialize();
				map.startObjects();
			} catch (Exception e) {
				System.out.println("Aucune sauvegarde n'a �t� trouv�e.");
				startGame(true);
			}
		}
		notifyStateObservers();
	}

	@Override
	public void endGame() {
		/*
		 * M�thode appel�e � la fin du jeu. Le menu d'accueil est r�affich�.
		 */
		player = null;
		map = null;
		handler = null;
		window.launchMenu();
	}

	//////////////////// SERIALISATION ////////////////////

	@Override
	public void serialize() {
		map.pauseObjects(true);
		handler.setScore(Score.getScore(11), Score.getScore(12), Score.getScore(13), Score.getScore(14), Score.getScore(15));
		handler.setWeaponGiven(ItemGenerator.gaveABow, ItemGenerator.gaveAnExplodingBriefcase, ItemGenerator.goblinInvocation, ItemGenerator.skeletonInvocation, ItemGenerator.ghostInvocation);
		Serialize.ser(handler);
		map.startObjects();
	}

	@Override
	public void deserialize() {
		/*
		 * Tous les objets de game et window, qui n'ont pas �t� s�rialis�s,
		 * doivent retrouv�s leurs attributs r�f�rents.
		 */
		this.handler = (Handler) Deserialize.deser();
		handler.setGame(this);
		handler.setWindow(window);
		map = handler.getMap();
		player = (Player) handler.getPlayer();
		this.window.setGameObjects(map.getGameObjects());
		this.window.setPlayer(handler.getPlayer());
		this.window.setInventory(((Player) handler.getPlayer()).getInventory());
		((Player) handler.getPlayer()).getInventory().deleteItem();
		Score.initScore(handler.getScore()[0], handler.getScore()[1], handler.getScore()[2], handler.getScore()[3], handler.getScore()[4]);
		ItemGenerator.initWeaponGiven(handler.weaponGiven()[0], handler.weaponGiven()[1], handler.weaponGiven()[2], handler.weaponGiven()[3], handler.weaponGiven()[4]);
		
		synchronized (map.getGameObjects()) {
			for (GameObject o : map.getGameObjects()) {
				o.demisableAttach(map);
				o.addObserver(window.getPanel());
				o.setImage();
				o.start();
			}
		}

		synchronized (player.getInventory().getInventoryItems()) {
			for (Pickable item : player.getInventory().getInventoryItems()) {
				GameObject i = ((GameObject) item);
				i.demisableAttach(map);
				i.addObserver(window.getPanel());
				i.setImage();
				i.start();
			}
		}
		notifyStateObservers();
	}

	//////////////////// INTERFACE CHANGEABLESTATE ////////////////////

	@Override
	public void notifyStateObservers() {
		/*
		 * Notifie � la souris et au clavier que la partie a repris pour
		 * r�initialiser leurs r�f�rents.
		 */
		synchronized (stateObservers) {
			for (StateObserver so : stateObservers)
				so.stateChange(handler);
		}
	}

	@Override
	public void stateObserverAttach(StateObserver so) {
		stateObservers.add(so);
	}

}