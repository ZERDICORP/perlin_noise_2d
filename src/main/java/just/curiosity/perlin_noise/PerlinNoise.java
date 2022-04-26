package just.curiosity.perlin_noise;

import java.util.Random;

public final class PerlinNoise {
  private static final float gradientRange;
  private static final byte[] auxiliaryBytes;
  private static final float[][] gradientVectors;
  private static final float[] d0;
  private static final float[] d1;
  private static final float[] d2;
  private static final float[] d3;

  static {
    gradientRange = 5.5f;
    gradientVectors = new float[][]{
      {gradientRange, 0}, {-gradientRange, 0},
      {0, gradientRange}, {0, -gradientRange}
    };

    new Random().nextBytes(auxiliaryBytes = new byte[512]);

    d0 = new float[2];
    d1 = new float[2];
    d2 = new float[2];
    d3 = new float[2];
  }

  /*
   * Multi octave noise.
   * */

  public static float calc(float x, float y, int octaves, float persistence) {
    float amplitude = 1;
    float max = 0;
    float result = 0;

    while (octaves-- > 0) {
      max += amplitude;
      result += calc(x, y) * amplitude;
      amplitude *= persistence;
      x *= 2;
      y *= 2;
    }

    return result / max;
  }

  /*
   * Main noise function.
   * */

  public static float calc(float x, float y) {
    /*
     * The top left corner of the square.
     * */

    int tLX = (int) Math.floor(x);
    int tLY = (int) Math.floor(y);

    /*
     * The local position of the point in the current
     * square.
     * */

    float dX = x - tLX;
    float dY = y - tLY;

    /*
     * Calculation of vectors emanating from the vertices
     * of a square to a point.
     * */

    d0[0] = dX;
    d0[1] = dY;

    d1[0] = dX - 1;
    d1[1] = dY;

    d2[0] = dX;
    d2[1] = dY - 1;

    d3[0] = dX - 1;
    d3[1] = dY - 1;

    /*
     * Calculation of gradient vectors for each of
     * the vertices.
     * */

    float[] g0 = gradientVector(tLX, tLY);
    float[] g1 = gradientVector(tLX + 1, tLY);
    float[] g2 = gradientVector(tLX, tLY + 1);
    float[] g3 = gradientVector(tLX + 1, tLY + 1);

    /*
     * Dot product of gradient vectors by relative vectors.
     * */

    float q0 = dotProduct(d0, g0);
    float q1 = dotProduct(d1, g1);
    float q2 = dotProduct(d2, g2);
    float q3 = dotProduct(d3, g3);

    dX = quinticCurve(dX);
    dY = quinticCurve(dY);

    float l1 = linearInterpolation(q0, q1, dX);
    float l2 = linearInterpolation(q2, q3, dX);

    return linearInterpolation(l1, l2, dY);
  }

  private static float[] gradientVector(int x, int y) {
    int hash = ((x * 234231123) ^ (y * 234231123) + 234231123);
    int aux = auxiliaryBytes[hash & (auxiliaryBytes.length - 1)];

    return gradientVectors[aux & (gradientVectors.length - 1)];
  }

  private static float dotProduct(float[] a, float[] b) {
    return a[0] * b[0] + a[1] * b[1];
  }

  private static float linearInterpolation(float a, float b, float t) {
    return a + (b - a) * t;
  }

  static float quinticCurve(float t) {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }
}
