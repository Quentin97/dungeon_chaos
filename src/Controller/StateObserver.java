package Controller;

import Model.Handler;

public interface StateObserver {
	/*
	 * Observateur de l'�tat du jeu. Utilis� par la souris et le clavier
	 * lorsqu'une partie (nouvelle ou ancienne) est charg�e.
	 */
	public abstract void stateChange(Handler handler);
}
