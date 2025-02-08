package Model.Chrono;

import java.io.Serializable;

public class Chrono implements Serializable {
	/*
	 * Chrono utilisé pour compter le temps entre deux actions, en soustrayant
	 * le temps de pause.
	 */
	private static final long serialVersionUID = 538132037251991931L;
	private transient long begin, end, pauseBegin = 0, pauseEnd = 0;

	public void begin() {
		begin = System.currentTimeMillis();
	}

	public void pauseBegin() {
		pauseBegin = System.currentTimeMillis();
	}

	public void pauseEnd() {
		if (pauseBegin == 0)
			return;
		pauseEnd = System.currentTimeMillis();
	}

	public void end() {
		end = System.currentTimeMillis();
	}

	public boolean ready(long time) {
		end();
		return ((end - begin - (pauseEnd - pauseBegin)) > time);
	}
}
