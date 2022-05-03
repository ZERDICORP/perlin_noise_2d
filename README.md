# perlin_noise_2d :cloud:
#### Just a Perlin Noise (to be more precise, a simple terrain generation based on it).
## Perlin Noise.. how difficult is it? :flushed:
Well.. let's just say..  
This algorithm has already been tested by everyone and sundry :hatched_chick:  

Most people use ready-made implementations (and there is nothing wrong with that).  

But I decided to figure out how it is generally possible to create such cool things (for example, a realistic terrain).

## So how does this algorithm work? :baby:
> There are a billion + 1 good tutorials on the Internet for this algorithm, so without going into details, I will describe the basic steps for calculating the noise value for a specific point on a 2D surface.
#### Step #1 ~ Determination of X and Y coordinates for further calculation of the noise value.
```java
float x = 1.4f;
float y = 1.3f;
```
#### Step #2 ~ Calculation of vectors going from the vertices of the square to the point (we will call them "relative vectors").
```java
// Let the width of the square be 1.
float squareWidth = 1f;

int topLeftX = (int) x; // 1
int topLeftY = (int) y; // 1

// Vectors from each vertex.
float[] topLeftVector = new float[] { x - topLeftX, y - topLeftY }; // 0.4, 0.3
float[] topRightVector = new float[] { squareWidth - topLeftVector[0], topLeftVector[1] }; // 0.6, 0.3
float[] bottomLeftVector = new float[] { topLeftVector[0], squareWidth - topLeftVector[1] }; // 0.4, 0.7
float[] bottomRightVector = new float[] { topRightVector[0], bottomLeftVector[1] }; // 0.6, 0.7
```
#### Step #3 ~ Calculation of gradient vectors for each of the vertices.
> This calculation occurs by finding the so-called hash.  
> In fact, this is some number obtained as a result of various mathematical operations on the X and Y coordinates of each of the vertices.  
> This allows us to return the same result (vector) for the same vertices.  
> In this example, a very simple way to find a hash will be demonstrated (in a real algorithm, something more complex should be used).
```java
float[] gradientVector(int vertexX, int vertexY) {
  int hash = (vertexX * 100) * (vertexY * 100) + (vertexX + vertexY);

  // Now we get a number from 0 to 3 by modulo
  // dividing the hash by the number 4.
  int index = hash % 4;

  // Having an index, we can return one of the
  // vectors.
  return switch (index) {
    case 0 -> new float[]{-1, 0};
    case 1 -> new float[]{0, -1};
    case 2 -> new float[]{1, 0};
    default -> new float[]{0, 1};
  };
}

float[] topLeftGradientVector = gradientVector(topLeftX, topLeftY);
float[] topRightGradientVector = gradientVector(topLeftX + squareWidth, topLeftY);
float[] bottomLeftGradientVector = gradientVector(topLeftX, topLeftY + squareWidth);
float[] bottomRightGradientVector = gradientVector(topLeftX + squareWidth, topLeftY + squareWidth);
```
#### Step #4 ~ Given the gradient vectors and relative vectors, we can find their dot product.
```java
// Since we have the direction of the vectors, the dot
// product can be calculated using a simple formula:
// a.x * b.x + a.y * b.y
float dotProduct(float[] a, float[] b) {
  return a[0] * b[0] + a[1] * b[1];
}

float topLeftValue = dotProduct(topLeftVector, topLeftGradientVector);
float topRightValue = dotProduct(topRightVector, topRightGradientVector);
float bottomLeftValue = dotProduct(bottomLeftVector, bottomLeftGradientVector);
float bottomRightValue = dotProduct(bottomRightVector, bottomRightGradientVector);
```
#### Step #5 ~ Finally, with the dot products, we can interpolate the values to find the noise value for our point.
```java
// We use the linear interpolation function, the formula
// is pretty damn simple too.
float linearInterpolation(float a, float b, float t) {
  return a + (b - a) * t;
}

// First, we interpolate the top left and top right values.
// In addition to the values calculated by the dot product,
// the third variable is involved in the linear interpolation -
// the local X coordinate of the point in the current square
// (essentially the X value of the top left relative vector).
float topValue = linearInterpolation(topLeftValue, topRightValue, topLeftVector[0]);

// Then we interpolate the bottom left and bottom right values.
float bottomValue = linearInterpolation(bottomLeftValue, bottomRightValue, topLeftVector[0]);

// And finally, let's interpolate the values just
// found (now we need the Y coordinate of the top left relative verctor).
float noiseValue = linearInterpolation(topValue, bottomValue, topLeftVector[1]);
```
#### Step #6 ~ You probably thought that's all, but there is a small detail left.. We need a curve function to smooth out the noise.
```java
// We will use quintic curve, but you can also explore other
// options (cosine curve, cubic curve)
float quinticCurve(float t) {
  return t * t * t * (t * (t * 6 - 15) + 10);
}

// This transformation must be done before interpolation.
topLeftVector[0] = quinticCurve(topLeftVector[0]);
topLeftVector[1] = quinticCurve(topLeftVector[1]);

/* do the interpolation here */
```
## Conclusion :beers:

Well, that's all.    
Yes, there are a lot of interesting things, for example, a randomly generated auxiliary array for getting random noise, or the implementation of the multi-octave noise function..  
But for a superficial acquaintance, I think that's enough.  

Have a nice day :3

## Screenshot :heart_eyes:
![image](https://user-images.githubusercontent.com/56264511/165273711-0b8c3da8-cb06-43e9-ac1f-26a106c308a3.png)
