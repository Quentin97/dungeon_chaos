package Model;

import java.awt.Graphics;
import java.io.Serializable;

import View.Panel;
import View.Graphics.Texture;

public class Score implements Serializable {
	private static final long serialVersionUID = 4090839170449509521L;
	/* Affiche le nombre de monstres de chaque type tués. */
	private static int score_goblin = 0, score_ghost = 0, score_skeleton = 0, score_pirate = 0, score_parrot = 0;
	
	public static void initScore(int goblin, int ghost, int skeleton, int pirate, int parrot) {
		score_goblin = goblin;
		score_ghost = ghost;
		score_skeleton = skeleton;
		score_pirate = pirate;
		score_parrot = parrot;
	}

	public static void setScore(int monster) {
		if (monster == 11)
			score_goblin++;
		else if (monster == 12)
			score_ghost++;
		else if (monster == 13)
			score_skeleton++;
		else if (monster == 14)
			score_pirate++;
		else if (monster == 15)
			score_parrot++;
	}

	public static int getScore(int monster){
		if (monster == 11) 
			return score_goblin;
		else if (monster == 12) 
			return score_ghost;
		else if (monster == 13) 
			return score_skeleton;
		else if (monster == 14) 
			return score_pirate;
		else if (monster == 15) 
			return score_parrot;
		return 0;
	}
	
	public static void render(Graphics g, Panel map) {
		g.drawImage(Texture.goblin_head, map.getWidth() / 2 - 100, map.getHeight() - 50, 50, 50, map);
		g.drawString(String.valueOf(Score.getScore(11)), map.getWidth() / 2 - 125, map.getHeight() - 6);
		
		g.drawImage(Texture.ghost_head, map.getWidth() / 2, map.getHeight() - 50, 50, 50, map);
		g.drawString(String.valueOf(Score.getScore(12)), map.getWidth() / 2 - 25, map.getHeight() - 6);
		
		g.drawImage(Texture.skeleton_head, map.getWidth() / 2 + 100, map.getHeight() - 50, 50, 50, map);
		g.drawString(String.valueOf(Score.getScore(13)), map.getWidth() / 2 + 75, map.getHeight() - 6);
		
		g.drawImage(Texture.pirate_head, map.getWidth() / 2 + 200, map.getHeight() - 50, 50, 50, map);
		g.drawString(String.valueOf(Score.getScore(14)), map.getWidth() / 2 + 175, map.getHeight() - 6);
		
		g.drawImage(Texture.parrot_head, map.getWidth() / 2 + 300, map.getHeight() - 50, 50, 50, map);
		g.drawString(String.valueOf(Score.getScore(15)), map.getWidth() / 2 + 275, map.getHeight() - 6);
	}

}
