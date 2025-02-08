package Model;

import Model.PopUp.PopUp;

public interface ObjectMaker {
	/* Interface de Game. */

	public abstract void setPopUp(PopUp popUp);

	public abstract int[] getScreenSize();

	public abstract void serialize();

	public abstract void deserialize();

	public abstract void startGame(boolean newGame);

	public abstract void endGame();

}
