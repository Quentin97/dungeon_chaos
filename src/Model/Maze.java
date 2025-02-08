package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import Model.Blocks.BreakableBlock;
import Model.Blocks.DoorIn;
import Model.Blocks.DoorOut;
import Model.Blocks.UnbreakableBlock;
import Model.Creatures.Ghost;
import Model.Creatures.Goblin;
import Model.Creatures.Pirate;
import Model.Creatures.Player;
import Model.Creatures.Skeleton;
import Model.Inventory.Pickable;

public class Maze implements Serializable {
	private static final long serialVersionUID = 6611709915799607519L;
	private Handler handler;
	private int width, height, numberVerticalBlocks, numberHorizontalBlocks;
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	private ArrayList<Pickable> items = new ArrayList<Pickable>();
	private static int currentMaze = -1;
	private static int numberGoblins = 3, numberSkeletons = 0, numberGhosts = 0, numberPirates = 0;
	private static int goblinsLives = 10, skeletonsLives = 10, ghostsLives = 10, piratesLives = 15;

	public Maze(Handler handler, Player player) {
		this.handler = handler;
		width = handler.getScreenSize()[0];
		height = handler.getScreenSize()[1];
		numberVerticalBlocks = 1 + (int) (height / 50);
		numberHorizontalBlocks = (int) (width / 50);
		currentMaze++;
		initNumberMonsters();
		makeMaze();
	}
	
	////////// GETTERS & SETTERS //////////

	public void setGameObjects(ArrayList<GameObject> objects) {
		this.objects = objects;
	}

	public void setItems(ArrayList<Pickable> items) {
		this.items = items;
	}

	public ArrayList<GameObject> getGameObjects() {
		return objects;
	}

	public ArrayList<Pickable> getItems() {
		return items;
	}

	////////// CREATION DU LABYRINTHE PHYSIQUE //////////

	private void makeMaze() {
		int[][] matrix = matrix();

		// Portes d'entrée et de sortie.
		objects.add(new DoorIn(50, 100, 100, handler));
		objects.add(new DoorOut(width - 150, height - 150, 100, handler));

		if (currentMaze < 3 || currentMaze % 2 == 0) {
			// Labyrinthe
			for (int j = 1; j < numberHorizontalBlocks - 2; j++) {
				for (int i = 1; i < numberVerticalBlocks - 2; i++) {
					if (matrix[i][j] == 1) {
						objects.add(new UnbreakableBlock(j * 50, i * 50, 50));
					} else if (matrix[i][j] == 15) {
						objects.add(new BreakableBlock(j * 50, i * 50, 50, 10));
					}
				}
			}

		} else {
			// Arène de combat
			for (int j = 5; j < numberHorizontalBlocks - 6; j++) {
				objects.add(new UnbreakableBlock(j * 50, 3 * 50, 50));
				objects.add(new UnbreakableBlock(j * 50, (numberVerticalBlocks - 5) * 50, 50));
			}

			for (int i = 6; i < numberVerticalBlocks - 7; i++) {
				objects.add(new UnbreakableBlock(3 * 50, i * 50, 50));
				objects.add(new UnbreakableBlock((numberHorizontalBlocks - 4) * 50, i * 50, 50));
			}

			if (numberVerticalBlocks >= 16 && numberHorizontalBlocks >= 22) {
				for (int i = 7; i < numberVerticalBlocks - 8; i++) {
					objects.add(new BreakableBlock(10 * 50, i * 50, 50, 10));
					objects.add(new BreakableBlock((numberHorizontalBlocks - 11) * 50, i * 50, 50, 10));
				}
				for (int j = 13; j < numberHorizontalBlocks - 13; j++) {
					objects.add(new BreakableBlock(j * 50, 6 * 50, 50, 10));
					objects.add(new BreakableBlock(j * 50, (numberVerticalBlocks - 8) * 50, 50, 10));
				}
			}

		}

		// Ajout des monstres
		addMonster(11, numberGoblins);
		addMonster(12, numberSkeletons);
		addMonster(13, numberGhosts);
		addMonster(14, numberPirates);

		initNumberMonsters();
	}

	private int[][] matrix() {
		int[][] chose = new int[numberVerticalBlocks][numberHorizontalBlocks];
		Random r = new Random();
		int l1 = 5 + r.nextInt(3);
		int l2 = 10 + r.nextInt(3);
		int l3 = 15 + r.nextInt(3);
		int c = 3 + r.nextInt(3);
		int d = 15 + r.nextInt(3);
		int e = 27 + r.nextInt(3);
		int c1 = 15 + r.nextInt(5);
		int c2 = 23 + r.nextInt(3);
		int c2b = 28 + r.nextInt(3);
		int c3 = 10 + r.nextInt(2);
		int c4 = l1;
		int c5 = 16 + r.nextInt(7);

		for (int h = 1; h < numberHorizontalBlocks - 1; h++) {
			for (int v = 1; v < numberVerticalBlocks - 1; v++) {

				chose[v][h] = 2 + r.nextInt(40);
			}
		}
		// Pour éviter qu'un carré ne soit positionné au même endroit que le
		// joueur
		chose[2][3] = 3;
		chose[2][2] = 3;
		chose[3][2] = 3;
		chose[3][3] = 3;
		chose[2][4] = 3;
		chose[3][4] = 3;
		// Pour éviter qu'un carré ne soit positionné au même endroit que la
		// porte en haute à gauche
		chose[1][2] = 4;
		chose[1][1] = 4;
		chose[2][1] = 4;
		chose[3][1] = 4;
		chose[4][1] = 4;
		chose[4][2] = 4;

		// Pour éviter qu'un carré ne soit positionné au même endroit que la
		// porte en bas à droite
		chose[((height - 200) / 50) + 1][((width - 200) / 50) + 1] = 5;
		chose[(height - 200) / 50][(width - 200) / 50] = 5;
		chose[(height - 200) / 50][((width - 200) / 50) - 1] = 5;
		chose[((height - 200) / 50) + 1][((width - 200) / 50) - 1] = 5;
		chose[((height - 200) / 50) + 1][((width - 200) / 50) + 1] = 5;
		chose[((height - 200) / 50) - 1][((width - 200) / 50) - 1] = 5;
		chose[((height - 200) / 50)][((width - 200) / 50) + 1] = 5;

		// 1) Variation de la ligne 5 à la ligne 8 d'une barre horizontale de
		// longueur variable: longueur max 13-2=11
		for (int j1 = c; j1 < 13; j1++) {
			chose[l1][j1] = 1;
		}

		// 2) Variation de la ligne 10 à la ligne 12 d'une barre horizontale de
		// longueur variable: longueur max 20-15=5
		for (int j2 = d; j2 < 20; j2++) {
			chose[l2][j2] = 1;

		}

		// 3) Variation de la ligne 15 à la ligne 18 d'une barre horizontale de
		// longueur variable: longueur max 32-27=5
		if (numberHorizontalBlocks > 32) {
			for (int j3 = e; j3 < 32; j3++) {

				chose[l3][j3] = 1;
			}
		} else if (numberHorizontalBlocks < 32) {
			for (int j3 = e; j3 < numberHorizontalBlocks - 4; j3++) {

				chose[l3][j3] = 1;
			}

		}

		// 4) Variation de la colonne 15 à la colonne 20 d'une barre verticale
		// de longueur 7-1=6

		for (int u1 = 1; u1 < 7; u1++) {
			chose[u1][c1] = 1;

		}
		// 5) Variation de la colonne 23 à la colonne 26 d'une barre verticale
		// de longueur 11-1=10
		for (int u2 = 1; u2 < 11; u2++) {

			chose[u2][c2] = 1;
		}
		// 5b) Variation de la colonne 28 à la colonne 31 d'une barre verticale
		// de longueur 13-5=8
		for (int u2b = 5; u2b < 13; u2b++) {

			chose[u2b][c2b] = 1;
		}

		// 6) Variation de la colonne 8 à la colonne 12 d'une barre verticale de
		// longueur 18-10=8
		for (int u3 = 10; u3 < 18; u3++) {
			chose[u3][c3] = 1;
		}

		// 7) Variation de la colonne 2 à la colonne 6 d'une barre verticale de
		// longueur 18-10=8
		for (int u4 = 10; u4 < 18; u4++) {
			chose[u4][c4] = 1;
		}
		// 8) Variation de la colonne 16 à la colonne 23 d'une barre verticale
		// de longueur 16-11=5
		for (int u5 = 14; u5 < 18; u5++) {
			chose[u5][c5] = 1;

		}

		return chose;
	}

	////////// AJOUT DES MONSTRES //////////

	private void initNumberMonsters() {
		/*
		 * Modifie le nombre de monstres de chaque type à créer dans le nouveau
		 * labyrinthe, ainsi que leurs points de vie à la naissance.
		 */
		if (currentMaze < 1) {
			numberGoblins += 2;
			goblinsLives++;
		} else if (currentMaze < 2) {
			numberGoblins ++;
			numberSkeletons += 1;
			goblinsLives += 2;
			skeletonsLives += 2;
		} else {
			if (currentMaze < 3) {
				numberGoblins++;
				numberSkeletons += 2;
				numberGhosts += 2;
				numberPirates = 1;
			}
			goblinsLives += 2;
			skeletonsLives += 2;
			ghostsLives += 4;
			piratesLives += 4;
		}
	}

	private void addMonster(int id, int number) {
		Random r = new Random();
		for (int d = 0; d < number; d++) {
			// Cherche une position disponible pour chaque monstre.
			boolean goodPosition = false;
			int x = 0;
			int y = 0;
			while (!goodPosition) {
				x = 100 + r.nextInt(width - 250);
				y = 100 + r.nextInt(height - 250);
				for (GameObject object : objects) {
					if (object.getRect().intersects(x - 10, y - 10, 120, 120)) {
						goodPosition = false;
						break;
					} else
						goodPosition = true;
				}
			}
			if (goodPosition) {
				if (id == 11) {
					objects.add(new Goblin(x, y, 100, goblinsLives, handler.getPlayer(), handler));
				} else if (id == 12) {
					objects.add(new Skeleton(x, y, 100, skeletonsLives, handler.getPlayer(), handler));
				} else if (id == 13) {
					objects.add(new Ghost(x, y, 100, ghostsLives, handler.getPlayer(), handler));
				} else if (id == 14) {
					objects.add(new Pirate(x, y, 100, piratesLives, handler.getPlayer(), handler));
				} else
					return;
			}
		}
	}

}