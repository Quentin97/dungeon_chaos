package Model.Serialization;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serialize {
	public static void ser(Object o) {
		try {
			FileOutputStream fileOut = new FileOutputStream("save/game.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(o);
			out.close();
			fileOut.close();
			System.out.println("Les donn�es ont �t� sauv�es dans save/game.ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}
}
