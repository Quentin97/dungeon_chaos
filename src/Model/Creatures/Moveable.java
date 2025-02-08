package Model.Creatures;

public interface Moveable {
	
	public abstract void addMovementObserver(MovementObserver mo);

	public abstract void notifyMovementObserver();

	public abstract void resetMovementObservers();
}
