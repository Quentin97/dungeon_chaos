package View.Graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	/* Charge une image. */

	public static BufferedImage load(String path) {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("Impossible de charger l'image.");
			System.exit(1); // Arr�t pour �viter erreurs.
		}
		return null;
	}

}
