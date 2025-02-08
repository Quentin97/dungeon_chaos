package View;

import java.awt.Color;
import java.awt.Graphics;

import View.Graphics.Texture;

public class Arena {
	/* Crée l'image de fond du plateau de jeu. */

	public static void render(Graphics g, Panel map, int xSize, int ySize) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, xSize, ySize); // Fond
		int numberHorizontalBlocks = (int) xSize / 50;
		int numberVerticalBlocks = 1 + (int) ySize / 50;
		for (int i = 1; i < numberHorizontalBlocks; i++) {
			for (int j = 1; j < numberVerticalBlocks; j++) {
				int x = i;
				int y = j;
				g.setColor(Color.LIGHT_GRAY);
				g.fillRect(x * 50, y * 50, 48, 48);
				g.setColor(Color.BLACK);
				g.drawRect(x * 50, y * 50, 48, 48);
			}
		}

		for (int i = 0; i < numberHorizontalBlocks; i++) {
			g.drawImage(Texture.unbreakableBlock, i * 50, 0, 50, 50, map);
			g.drawImage(Texture.unbreakableBlock, i * 50, (numberVerticalBlocks - 1) * 50, 48, 48, map);
		}
		for (int j = 1; j < numberVerticalBlocks - 1; j++) {
			g.drawImage(Texture.unbreakableBlock, 0, j * 50, 50, 50, map);
			g.drawImage(Texture.unbreakableBlock, (numberHorizontalBlocks - 1) * 50, j * 50, 48, 48, map);
		}
	}

}
