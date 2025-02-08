package Controller;

import Model.Handler;

public interface StateObserver {
	/*
	 * Observateur de l'état du jeu. Utilisé par la souris et le clavier
	 * lorsqu'une partie (nouvelle ou ancienne) est chargée.
	 */
	public abstract void stateChange(Handler handler);
}
