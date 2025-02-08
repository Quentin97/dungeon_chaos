package Model;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import Model.Blocks.DoorIn;
import Model.Blocks.DoorOut;
import Model.Creatures.Monster;
import Model.Creatures.MovementObserver;
import Model.Creatures.Parrot;
import Model.Creatures.Player;
import Model.Demisable.Demisable;
import Model.Demisable.DemisableObserver;
import Model.Explodable.Explodable;
import Model.Explodable.ExplodableObserver;
import Model.Inventory.Pickable;
import Model.Inventory.ItemGenerator.ItemGenerator;

public class Map implements DemisableObserver, Serializable {
	private static final long serialVersionUID = 5639535211349028193L;
	private Handler handler;
	private Player player;
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private ArrayList<Pickable> items = new ArrayList<Pickable>();
	private ArrayList<Maze> mazes = new ArrayList<Maze>();
	private int width, height, currentMaze = 0, numberMazes = 1;

	/*
	 * Cette classe est le plateau de jeu. Elle s'occupe de la cr�ation de
	 * labyrinthe et de la gestion des interactions entre les GameObjects. Elle
	 * permet la navigation entre les diff�rents labyrinthes et �tablit les
	 * diff�rents liens entre observateurs et observ�s � chaque fois qu'un
	 * nouveau GameObject est � placer sur le plateau de jeu.
	 */

	public Map(Handler handler, Player player) {
		this.handler = handler;
		handler.setMap(this);
		this.player = player;
		addObject(player);
		width = handler.getScreenSize()[0];
		height = handler.getScreenSize()[1];
		createMaze();
		init();
	}

	//////////////////// SETTERS & GETTERS ////////////////////
	/* Pour r�cup�rer les attributs objects et player et ajouter items. */

	public ArrayList<GameObject> getGameObjects() {
		return this.objects;
	}

	public void setItem(Pickable item) {
		synchronized (items) {
			addObject((GameObject) item);
			items.add(item);
		}
	}

	public void addObject(GameObject object) {
		synchronized (objects) {
			object.demisableAttach(this);
			object.addObserver(handler.getWindow().getPanel());
			if (object instanceof DoorIn || object instanceof DoorOut)
				// Player observ� par les portes.
				player.addMovementObserver((MovementObserver) object);
			else if (object instanceof Monster && player == ((Monster) object).getEnnemy())
				// Player observ� par les monstres ennemis.
				player.addMovementObserver((MovementObserver) object);
			else if (object instanceof Monster) {
				// Monstres ennemis et amis s'observent.
				for (GameObject o : objects)
					if (o instanceof Monster && !((Monster) o).belongsToPlayer()) {
						((Monster) object).addMovementObserver((MovementObserver) o);
						((Monster) o).addMovementObserver((MovementObserver) object);
					}
			}
			objects.add(object);
		}
	}

	public ArrayList<Pickable> getItems() {
		return items;
	}

	public Player getPlayer() {
		return player;
	}

	////////// CREATION DU LABYRINTHE ET INITIALISATION //////////

	private void createMaze() {
		Maze maze = new Maze(handler, player);
		mazes.add(maze);
	}

	synchronized private void init() {
		/*
		 * M�thode appel�e � chaque changement de labyrinthe. Les attributs
		 * objets et items sont r�initialis�s. Idem pour les observateurs des
		 * mouvements du joueur.
		 */
		player.resetMovementObservers();
		this.objects = new ArrayList<GameObject>();
		this.items = new ArrayList<Pickable>();

		// Ajoute les nouveaux objets cr��s � la map
		synchronized (mazes.get(currentMaze).getGameObjects()) {
			for (GameObject object : mazes.get(currentMaze).getGameObjects())
				addObject(object);
		}
		items = mazes.get(currentMaze).getItems();
		objects.add(player);
		handler.getWindow().setGameObjects(this.getGameObjects());
		handler.setGameObjects(objects);

		// Une fois le plateau de jeu pr�t, les GameObjects peuvent d�marrer.
		startObjects();
	}

	///////////// SAUVEGARDE ET CHANGEMENT DE LABYRINTHE /////////////

	private void saveMaze() {
		pauseObjects(false);
		mazes.get(currentMaze).setGameObjects(objects);
		mazes.get(currentMaze).setItems(items);
	}

	public void previousMaze() {
		if (currentMaze == 0) {
			// Retour � l'�cran d'accueil
			pauseObjects(true);
			mazes = null;
			handler.getGame().endGame();
		} else {
			// Passage dans le labyrinthe pr�c�dent
			saveMaze();
			currentMaze--;
			init();
			player.setPosX(width - 200);
			player.setPosY(height - 200);
		}
	}

	public void nextMaze() {
		synchronized (player) {
			synchronized (mazes) {
				saveMaze();
				if (currentMaze < numberMazes - 1) {
					// Passage dans le labyrinthe suivant si d�j� cr��.
					currentMaze++;
					init();
				} else {
					// Cr�ation du labyrinthe suivant sinon.
					createMaze();
					currentMaze++;
					numberMazes++;
					init();
				}
				player.setPosX(150);
				player.setPosY(100);
			}
		}

	}

	//////////////////// OBSTACLES ////////////////////

	public boolean presenceObstacle(int x, int y, int rad) {
		/*
		 * D�termine si un GameObject intersecte un quelconque autre GameObjet
		 * du plateau de jeu en v�rifiant si leurs attributs Rectangle
		 * s'intersectent. Si oui, la m�thode demande si l'objet est r�ellement
		 * un obstacle et si oui, on ne prend pas la peine de v�rifier les
		 * autres.
		 */
		Rectangle rect = new Rectangle(x + (int) rad * 2 / 12, y + (int) rad / 8, rad * (int) 3 / 5, rad * (int) 4 / 5);
		boolean obstacle = false;
		synchronized (objects) {
			for (GameObject object : objects) {
				if (rect.intersects(object.getRect())) {
					obstacle = object.isObstacle();
				}
				if (obstacle) {
					break;
				}
			}
		}
		return obstacle;
	}

	//////////////////// OBJET DEPOSE ////////////////////

	public void expDropped(Explodable exp) {
		/* Un objet de type Explodable a �t� d�pos� sur le plateau de jeu. */
		GameObject o = (GameObject) exp;
		if (objects != null)
			synchronized (objects) {
				for (GameObject object : objects) {
					/*
					 * Les Explodable s'observent mutuellement. Rend tous les
					 * ExplodableObserver observateurs de Explodable.
					 */

					if (object instanceof Explodable && exp instanceof ExplodableObserver) {
						((Explodable) object).explodableAttach(((ExplodableObserver) exp));
					}
					if (object instanceof ExplodableObserver) {
						exp.explodableAttach(((ExplodableObserver) object));
					}
				}
				addObject(o);
			}
	}

	////////// DEMARRE/STOPPE OBJETS //////////

	public void pauseObjects(boolean pausePlayer) {
		synchronized (objects) {
			for (GameObject object : objects) {
				if (!(object instanceof Player && !pausePlayer)) {
					object.pause();
				}
			}
		}
	}

	public void startObjects() {
		synchronized (objects) {
			if (!handler.isTimeStop() && !player.getInventory().isActive())
				for (GameObject object : objects) {
					object.start();
				}
		}
	}

	////// DEFINITION DES FONCTIONS DE L'INTERFACE DEMISABLEOBSERVER //////

	@Override
	public void demise(Demisable object) {
		/*
		 * Map est observateur de l'�tat de vie ou de mort des GameObjects. Si
		 * l'un d'entre eux doit mourir, il le supprime de l'ArrayList objects.
		 */
		if (objects != null) {
			synchronized (objects) {
				if (object instanceof Monster && !(object instanceof Parrot)) {
					// Un item appara�t �ventuellement quand le monstre meurt.
					// Le score s'incr�mente.
					Score.setScore(((Monster) object).getId());
					setItem(ItemGenerator.generatesItem(((Monster) object).getPosX(), ((Monster) object).getPosY(), 50,
							handler));
				} else if (object instanceof Pickable) {
					items.remove(object);
				} else if (object instanceof Player) {
					// Le jeu se termine si le joueur meurt.
					pauseObjects(true);
					handler.getGame().endGame();
				}
				if (objects != null)
					objects.remove(object);
			}
		}
	}

}
