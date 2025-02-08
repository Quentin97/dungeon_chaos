package Model.Inventory.ItemGenerator;

import java.io.Serializable;
import java.util.Random;
import Model.Handler;
import Model.Score;
import Model.Creatures.Player;
import Model.Inventory.AttackAbilityEnhancer;
import Model.Inventory.Checkpoint;
import Model.Inventory.Heart;
import Model.Inventory.Pickable;
import Model.Inventory.TimeStop;
import Model.Weapons.Bow;
import Model.Weapons.ExplodingBriefCase;
import Model.Weapons.GhostInvocation;
import Model.Weapons.GoblinInvocation;
import Model.Weapons.SkeletonInvocation;

public class ItemGenerator implements Serializable {
	/*
	 * Cette classe permet de générer un item par le biais de l'envoi du message
	 * generatesItem.
	 */
	private static final long serialVersionUID = 8988048178823764705L;
	public static boolean gaveABow = false, gaveAnExplodingBriefcase = false, goblinInvocation = false,
			skeletonInvocation = false, ghostInvocation = false;

	public static void initWeaponGiven(boolean bow, boolean explodingBriefcase, boolean goblin, boolean skeleton,
			boolean ghost) {
		gaveABow = bow;
		gaveAnExplodingBriefcase = explodingBriefcase;
		goblinInvocation = goblin;
		ghostInvocation = ghost;
		skeletonInvocation = skeleton;
	}

	private static int generatesId() {
		/*
		 * La fréquence d'apparition des objets est déterminée par une fonction
		 * de densité gaussienne.
		 */
		double id = Math.abs(new Random().nextGaussian());
		if (id < 1)
			id = 1; // vie
		else if (id < 1.50)
			id = 4; // augmentation de capacité
		else if (id < 1.75)
			id = 3; // time stop
		else if (id < 2)
			id = 2; // checkpoint
		return (int) id;
	}

	public static Pickable generatesItem(int posX, int posY, int rad, Handler handler) {
		Pickable item;
		// Vérifie s'il n'est pas temps de générer une arme.
		if (!gaveABow && Score.getScore(11) >= 3) {
			item = new Bow(posX, posY, rad, (Player) handler.getPlayer(), handler);
			gaveABow = true;
		} else if (!gaveAnExplodingBriefcase && Score.getScore(11) >= 3 && Score.getScore(12) >= 1) {
			item = new ExplodingBriefCase(posX, posY, rad, (Player) handler.getPlayer(), handler);
			gaveAnExplodingBriefcase = true;
		} else if (!goblinInvocation && Score.getScore(11) >= 3 && Score.getScore(12) >= 1
				&& Score.getScore(13) >= 1) {
			item = new GoblinInvocation(posX, posY, rad, (Player) handler.getPlayer(), handler);
			goblinInvocation = true;
		} else if (!skeletonInvocation && Score.getScore(11) >= 5 && Score.getScore(12) >= 2
				&& Score.getScore(13) >= 2) {
			item = new SkeletonInvocation(posX, posY, rad, (Player) handler.getPlayer(), handler);
			skeletonInvocation = true;
		} else if (!ghostInvocation && Score.getScore(11) >= 10 && Score.getScore(12) >= 2
				&& Score.getScore(13) >= 2) {
			item = new GhostInvocation(posX, posY, rad, (Player) handler.getPlayer(), handler);
			ghostInvocation = true;
			// Génère un objet de type item.
		} else {
			int id = generatesId();
			if (id == 1)
				item = new Heart(posX, posY, rad, handler);
			else if (id == 2)
				item = new Checkpoint(posX, posY, rad, handler);
			else if (id == 3)
				item = new TimeStop(posX, posY, rad, handler);
			else
				item = new AttackAbilityEnhancer(posX, posY, rad, handler);
		}
		return item;
	}
}
