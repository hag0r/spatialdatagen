# A data generator for spatial data

This tool is a simple data generator that creates spatial data. Currently, it is able to create *points* and *polygons* with *uniform* distribution only.

## Usage
After cloning, build the project with *sbt*
```
sbt assembly
```
and run with
```
java -jar target/scala-2.11/spatialdatagen.sh -n 10 -t point
```
This will create 10 points in [WKT](https://en.wikipedia.org/wiki/Well-known_text) format and print them on your screen (stdout).

The following cli options are available:
```
Usage: Spatial Data Generator [options] [file]

  --min-x <value>       Minimal x value (for polygons for the center, Default = -180)
  --max-x <value>       Maximal x value (for polygons for the center, Default = 180)
  --min-y <value>       Minimal y value (for polygons for the center, Default = -90)
  --max-y <value>       Maximal y value (for polygons for the center, Default = 90)
  --x-radius <value>    Maximal x radius of the ellipse to generate polygons (only needed for polygons)
  --y-radius <value>    Maximal y radius of the ellipse to generate polygons (only needed for polygons)
  -a, --approx <value>  Maximun number of points to use to represent a polygon, default = 100
  -n, --num <value>     Number of elements to generate in total, default = 10
  --id                  Generate an ID (Long) for each object
  -q, --quiet           Do not print execution time statistics
  -t, --types <value>   Comma separated list of types to generate (point,polygon)
  -i, --interval <value>  create interval with given min and max bounds
  file                  Output file to write results to. Use <stdout> if empty
```

## Data generation strategies

For points, we use the Java/Scala random number generator to create the coordinate values for `x` and `y`.

Polygons are a little more complex to generate. Currently, we create a random center point and create an ellipe around it with the specified max radius in `x` and `y` direction, resoectively. The actual radius is a random value between 1 and the respective max value. Then we create between 3 and `approx` points on that ellipse and use these points as the polygon's boundary points.
This results in convex polygons only!

The intervals (`-i`, `--interval`) can be used to create a (temporal) interval. The generated value will be added to the output as two fields `start` and `end`.

For `-t point,polygon --id --interval 1,10` the result will look like

```
0;POINT(25.97324975670142 -65.94055919025132);3;7
```
Where the first field is the ID, then the WKT of the geometry (in this case a point), and then followed by the start value of the interval (3) and the end value (7).

## TODO
* more geometry types (linestring, circle, ...)
* concave polygons
* payload data?
* configurable separator (currently `;` only)
