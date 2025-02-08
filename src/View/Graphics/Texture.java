package View.Graphics;

import java.awt.image.BufferedImage;

public class Texture {
	/*
	 * Charge tous les graphismes du jeu. Attributs et méthodes sont statiques.
	 */

	private static final int width = 50, height = 50;
	public static BufferedImage[] player_up, player_left, player_down, player_right;
	public static BufferedImage[] goblin_up, goblin_left, goblin_down, goblin_right;
	public static BufferedImage[] ghost_up, ghost_left, ghost_down, ghost_right;
	public static BufferedImage[] skeleton_up, skeleton_left, skeleton_down, skeleton_right;
	public static BufferedImage[] pirate_left, pirate_jumpleft, pirate_right, pirate_jumpright;
	public static BufferedImage[] parrot_up, parrot_left, parrot_down, parrot_right;
	public static BufferedImage[] arrow, breakableBlock, doorIn, doorOut;
	public static BufferedImage menu, unbreakableBlock, heart, stars, bomb, explosion, goblin_head, ghost_head,
			skeleton_head, pirate_head, parrot_head, inventory, fist, bow, book, timeStop, attackAbilityEnhancer;

	public static void init() {
		menu = (new SpriteSheet(ImageLoader.load("res/Home.png"))).crop(0, 0, 875, 563);

		/* Pour le joueur */
		SpriteSheet playerSprite = new SpriteSheet(ImageLoader.load("res/Player_sprite.png"));
		player_up = new BufferedImage[9];
		player_left = new BufferedImage[9];
		player_down = new BufferedImage[9];
		player_right = new BufferedImage[9];

		for (int i = 0; i < 9; i++) {
			player_up[i] = playerSprite.crop(width * 2 * i, 0, width * 2, height * 2);
			player_left[i] = playerSprite.crop(width * 2 * i, height * 2 * 1, width * 2, height * 2);
			player_down[i] = playerSprite.crop(width * 2 * i, height * 2 * 2, width * 2, height * 2);
			player_right[i] = playerSprite.crop(width * 2 * i, height * 2 * 3, width * 2,
					height * 2);
		}


		/* Pour le goblin */
		SpriteSheet goblinSprite = new SpriteSheet(ImageLoader.load("res/Goblin_sprite.png"));
		goblin_up = new BufferedImage[10];
		goblin_left = new BufferedImage[10];
		goblin_down = new BufferedImage[10];
		goblin_right = new BufferedImage[10];

		for (int i = 0; i < 10; i++) {
			goblin_up[i] = goblinSprite.crop(width * 2 * i, 0, width * 2, height * 2);
			goblin_left[i] = goblinSprite.crop(width * 2 * i, height * 2 * 1, width * 2, height * 2);
			goblin_down[i] = goblinSprite.crop(width * 2 * i, height * 2 * 2, width * 2, height * 2);
			goblin_right[i] = goblinSprite.crop(width * 2 * i, height * 2 * 3, width * 2, height * 2);
		}


		/* Pour le fantôme */
		SpriteSheet ghostSprite = new SpriteSheet(ImageLoader.load("res/Ghost_sprite.png"));
		ghost_up = new BufferedImage[10];
		ghost_left = new BufferedImage[10];
		ghost_down = new BufferedImage[10];
		ghost_right = new BufferedImage[10];

		for (int i = 0; i < 10; i++) {
			ghost_up[i] = ghostSprite.crop(width * 2 * i, height * 2 * 2, width * 2, height * 2);
			ghost_left[i] = ghostSprite.crop(width * 2 * i, height * 2 * 1, width * 2, height * 2);
			ghost_down[i] = ghostSprite.crop(width * 2 * i, 0, width * 2, height * 2);
			ghost_right[i] = ghostSprite.crop(width * 2 * i, height * 2 * 3, width * 2, height * 2);
		}

		/* Pour le squelette */
		SpriteSheet skeletonSprite = new SpriteSheet(ImageLoader.load("res/Skeleton_sprite.png"));
		skeleton_up = new BufferedImage[14];
		skeleton_left = new BufferedImage[14];
		skeleton_down = new BufferedImage[14];
		skeleton_right = new BufferedImage[14];

		for (int i = 0; i < 14; i++) {
			skeleton_left[i] = skeletonSprite.crop(width * 2 * i, 0, width * 2, height * 2);
			skeleton_down[i] = skeleton_left[i];
			skeleton_right[i] = skeletonSprite.crop(width * 2 * i, height * 2 * 1, width * 2, height * 2);
			skeleton_up[i] = skeleton_right[i];
		}

		/* Pour le pirate */ 
		SpriteSheet pirate1Sprite = new SpriteSheet(ImageLoader.load("res/Pirate_sprite.png"));
		SpriteSheet pirate2Sprite = new SpriteSheet(ImageLoader.load("res/Pirate2_sprite.png"));
		pirate_left = new BufferedImage[14];
		pirate_jumpleft = new BufferedImage[5];
		pirate_right = new BufferedImage[14];
		pirate_jumpright = new BufferedImage[5];

		for (int i = 0; i < 6; i++) {
			pirate_right[i] = pirate1Sprite.crop(width * 2 * i, 0, width * 2, height * 2);
		}
		for (int i = 0; i < 8; i++) {
			pirate_right[i + 6] = pirate1Sprite.crop(width * 2 * i, height * 2, width * 2, height * 2);
		}
		for (int i = 7; i > 1; i--) {
			pirate_left[i - 2] = pirate2Sprite.crop(width * 2 * i, 0, width * 2, height * 2);
		}
		for (int i = 7; i >= 0; i--) {
			pirate_left[i + 6] = pirate2Sprite.crop(width * 2 * i, height * 2, width * 2, height * 2);
		}
		for (int i = 0; i < 5; i++) {
			pirate_jumpright[i] = pirate1Sprite.crop(width * 2 * i, height * 4, width * 2, height * 4);
		}
		for (int i = 7; i > 2; i--) {
			pirate_jumpleft[i-3] = pirate2Sprite.crop(width * 2 * i, height * 4, width * 2, height * 4);
		}

		/* Pour le perroquet */ 
		SpriteSheet parrotSprite = new SpriteSheet(ImageLoader.load("res/Parrot_sprite.png"));
		parrot_up = new BufferedImage[4];
		parrot_left = new BufferedImage[4];
		parrot_down = new BufferedImage[4];
		parrot_right = new BufferedImage[4];

		for (int i = 0; i < 4; i++) {
			parrot_up[i] = parrotSprite.crop(width * 2 * i, height * 2 * 3, width * 2, height * 2);
			parrot_left[i] = parrotSprite.crop(width * 2 * i, height * 2 * 1, width * 2, height * 2);
			parrot_down[i] = parrotSprite.crop(width * 2 * i, 0, width * 2, height * 2);
			parrot_right[i] = parrotSprite.crop(width * 2 * i, height * 2 * 2, width * 2, height * 2);
		}

		/* Pour les autres objets */
		SpriteSheet items = new SpriteSheet(ImageLoader.load("res/Items_sprite.png"));
		unbreakableBlock = items.crop(0, 0, width, height);
		breakableBlock = new BufferedImage[4];
		for (int i = 0; i < 4; i++) {
			breakableBlock[i] = items.crop(width * i, height, width, height);
		}
		heart = items.crop(width, 0, width, height);
		stars = items.crop(width * 2, 0, width, height);
		bomb = items.crop(width * 3, 0, width, height);
		explosion = items.crop(width * 4, 0, width, height);
		goblin_head = items.crop(width * 5, 0, width, height);
		ghost_head = items.crop(width * 6, 0, width, height);
		skeleton_head = items.crop(width * 7, 0, width, height);
		pirate_head = items.crop(width * 5, height, width, height);
		parrot_head = items.crop(width * 6, height, width, height);
		arrow = new BufferedImage[8];
		for (int i = 0; i < 7; i++) {
			arrow[i] = items.crop(width * (i + 8), 0, width, height);
		}
		fist = items.crop(width * 16, 0, width, height);
		bow = items.crop(width * 17, 0, width, height);
		book = items.crop(width * 18, 0, width, height);
		timeStop = items.crop(width * 19, 0, width, height);
		attackAbilityEnhancer = items.crop(width * 4, height, width, height);
		inventory = ImageLoader.load("res/inventory.png");
		doorOut = new BufferedImage[2];
		doorOut[0] = items.crop(0, height * 2, width * 2, height * 2);
		doorOut[1] = items.crop(width * 2, height * 2, width * 2, height * 2);
		doorIn = new BufferedImage[2];
		doorIn[0] = items.crop(width * 4, height * 2, width * 2, height * 2);
		doorIn[1] = items.crop(width * 6, height * 2, width * 2, height * 2);
	}

}
