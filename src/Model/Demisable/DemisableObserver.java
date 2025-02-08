package Model.Demisable;

public interface DemisableObserver {
	/* Observateur des objets susceptibles de mourir. */
	abstract public void demise(Demisable d);
}
