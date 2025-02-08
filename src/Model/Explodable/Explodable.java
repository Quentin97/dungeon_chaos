package Model.Explodable;

public interface Explodable {
	
	void explodableAttach(ExplodableObserver exObserver);

	void explodableNotifyObserver();

	abstract int getFactor();

	abstract int getEffect();

	abstract void setThrower(ExplodableObserver thrower);

	abstract ExplodableObserver getThrower();
}
