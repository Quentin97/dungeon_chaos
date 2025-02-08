package Model;

import Controller.StateObserver;

public interface ChangeableState {
	/*
	 * L'objet observé envoie une notification à ses observateur lorsque son
	 * "état" change : une (nouvelle ou ancienne) partie vient d'être chargée.
	 */
	public abstract void notifyStateObservers();

	public abstract void stateObserverAttach(StateObserver so);
}
