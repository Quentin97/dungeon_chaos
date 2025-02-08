package Model.Demisable;

public interface Demisable {
	/* Objets susceptibles de mourir. */
	
	void demisableAttach(DemisableObserver po);

	void demisableNotifyObserver();
}
