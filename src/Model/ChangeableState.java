package Model;

import Controller.StateObserver;

public interface ChangeableState {
	/*
	 * L'objet observ� envoie une notification � ses observateur lorsque son
	 * "�tat" change : une (nouvelle ou ancienne) partie vient d'�tre charg�e.
	 */
	public abstract void notifyStateObservers();

	public abstract void stateObserverAttach(StateObserver so);
}
