package just.curiosity.perlin_noise;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
  private static final int imageWidth;
  private static final int imageHeight;
  private static final float squareWidth;
  private static final BufferedImage bufferedImage;

  static {
    imageWidth = 512;
    imageHeight = 512;
    squareWidth = (float) imageWidth / 5;
    bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
  }

  public static void main(String[] args) throws IOException {
    for (int y = 0; y < imageHeight; y++) {
      for (int x = 0; x < imageWidth; x++) {
        float value = PerlinNoise.calc(x / squareWidth, y / squareWidth, 20,
          0.55f);

        /*
         * Convert float to int to get color from height
         * value.
         * */

        int height = (int) (value * 10);
        Color color = heightColor(height);

        bufferedImage.setRGB(x, y, color.getRGB());
      }
    }

    ImageIO.write(bufferedImage, "png", new File("terrain.png"));
  }

  /*
   * The function of comparing the height value with
   * the color.
   * */

  private static Color heightColor(int h) {
    return switch (h) {
      case -10 -> new Color(0, 33, 86);
      case -9 -> new Color(0, 35, 91);
      case -8 -> new Color(1, 39, 101);
      case -7 -> new Color(1, 44, 114);
      case -6 -> new Color(1, 48, 126);
      case -5 -> new Color(1, 53, 140);
      case -4 -> new Color(2, 60, 155);
      case -3 -> new Color(1, 66, 171);
      case -2 -> new Color(2, 73, 189);
      case -1 -> new Color(1, 80, 210);
      case 0 -> new Color(255, 213, 0);
      case 1 -> new Color(20, 155, 2);
      case 2 -> new Color(17, 131, 2);
      case 3 -> new Color(13, 103, 2);
      case 4 -> new Color(13, 91, 3);
      case 5 -> new Color(101, 101, 101);
      case 6 -> new Color(114, 112, 112);
      case 7 -> new Color(131, 131, 131);
      case 8 -> new Color(154, 154, 154);
      case 9 -> new Color(175, 175, 175);
      case 10 -> new Color(201, 201, 201);
      default -> (h > 0 ?
        new Color(255, 255, 255) :
        new Color(0, 33, 86));
    };
  }
}
