package Main;

import View.Window;
import Controller.Keyboard;
import Controller.Mouse;
import Model.Game;

public class Main {
 
	public static void main(String[] args) {
		System.out.println("Projet Donjon INFO-H-200 - Quentin Gontier - Bill Ndihokubwayo");
		
		System.out.println("Touches de déplacement : Z Q S D.");
		System.out.println("Accélération : maintenir la touche SHIFT enfoncée.");
		System.out.println("Ouvrir et fermer l'inventaire : touche E.");
		System.out.println("Naviguer dans l'inventaire : touches Z S");
		System.out.println("Utiliser un objet de l'inventaire : touche SPACE.");
		System.out.println("Supprimer un objet de l'inventaire : touche DELETE.");
		System.out.println("Clic gauche de la souris pour tirer.");

		Window window = new Window();
		Game game = new Game(window);		
		Keyboard keyboard = new Keyboard();
		window.setKeyListener(keyboard);
		Mouse mouse = new Mouse();
		window.setMouseListener(mouse);
		game.stateObserverAttach(keyboard);
		game.stateObserverAttach(mouse);
		game.notifyStateObservers();
	}
}