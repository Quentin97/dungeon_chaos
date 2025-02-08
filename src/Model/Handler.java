package Model;

import java.io.Serializable;
import java.util.ArrayList;
import Model.Creatures.Alive;
import Model.Inventory.Pickable;
import View.Window;

public class Handler implements Serializable {
	/*
	 * Cette méthode a deux fonctions : Premièrement, elle sert à accéder à
	 * certains objets difficiles à référencer bas dans la hiérarchie.
	 * Deuxièmement, c'est le noeud du réseau d'objets, qui va être sérialisé.
	 */
	private static final long serialVersionUID = 4716071971437894210L;
	private transient ObjectMaker game;
	private transient Window window;
	private Alive player;
	private ArrayList<GameObject> objects;
	private ArrayList<Pickable> items;
	private int[] screenSize;
	private Map map;
	private boolean timeStop = false;
	private int score_goblin = 0, score_ghost = 0, score_skeleton = 0, score_pirate = 0, score_parrot = 0;
	private boolean gaveABow = false, gaveAnExplodingBriefcase = false, goblinInvocation = false,
			skeletonInvocation = false, ghostInvocation = false;

	public Handler(Game game, Window window) {
		setGame(game);
		setWindow(window);
		setScreenSize();
	}

	public void setGame(ObjectMaker game) {
		this.game = game;
	}

	public ObjectMaker getGame() {
		return game;
	}

	public void setWindow(Window window) {
		this.window = window;
	}

	public Window getWindow() {
		return window;
	}

	public void setPlayer(Alive player) {
		this.player = player;
	}

	public Alive getPlayer() {
		return player;
	}

	public void setScreenSize() {
		screenSize = new int[2];
		screenSize = window.getScreenSize();
		screenSize[0] -= screenSize[0] % 50;
		screenSize[1] -= screenSize[1] % 50;
	}

	public int[] getScreenSize() {
		return screenSize;
	}

	public void setGameObjects(ArrayList<GameObject> objects) {
		this.objects = objects;
	}

	public ArrayList<GameObject> getGameObjects() {
		return objects;
	}

	public ArrayList<Pickable> getItems() {
		return items;
	}

	public void setItems(ArrayList<Pickable> items) {
		this.items = items;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public boolean isTimeStop() {
		return timeStop;
	}

	public void setTimeStop(boolean timeStop) {
		this.timeStop = timeStop;
	}

	public void setScore(int score_goblin, int score_ghost, int score_skeleton, int score_pirate, int score_parrot) {
		this.score_goblin = score_goblin;
		this.score_ghost = score_ghost;
		this.score_skeleton = score_skeleton;
		this.score_pirate = score_pirate;
		this.score_parrot = score_parrot;
	}

	public int[] getScore() {
		int[] score = new int[5];
		score[0] = score_goblin;
		score[1] = score_ghost;
		score[2] = score_skeleton;
		score[3] = score_pirate;
		score[4] = score_parrot;
		return score;
	}

	public void setWeaponGiven(boolean gaveABow, boolean gaveAnExplodingBriefcase, boolean goblinInvocation,
			boolean skeletonInvocation, boolean ghostInvocation) {
		this.gaveABow = gaveABow;
		this.gaveAnExplodingBriefcase = gaveAnExplodingBriefcase;
		this.goblinInvocation = goblinInvocation;
		this.ghostInvocation = ghostInvocation;
		this.skeletonInvocation = skeletonInvocation;
	}

	public boolean[] weaponGiven() {
		boolean[] tab = new boolean[5];
		tab[0] = gaveABow;
		tab[1] = gaveAnExplodingBriefcase;
		tab[2] = goblinInvocation;
		tab[3] = ghostInvocation;
		tab[4] = skeletonInvocation;
		return tab;
	}

}
