/******************************************************************************
 *  Compilation:  javac BruteCollinearPoints.java
 *  Execution:    java BruteCollinearPoints
 *  Dependencies: Point.java LineSegment.java
 *
 *  A force brute way to examine 4 points at a time and checks whether they all
 *  lie on the same line segment, returning all such line segments.
 *
 *  Edited by Zhanwei(Edison) Ye
 ******************************************************************************/

import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints
{
    /// ArrayList to store line segment found.
    private final ArrayList<LineSegment> mLineSegments = new ArrayList<LineSegment>();
    /// Array to sort input points
    private Point[] mPoints;

    /**
     * Constructor
     *
     * @param points Input points for finding all
     * line segments containing 4 points
     * @throws IllegalArgumentException if the argument to the constructor is null,
     * if any point in the array is null, or if the argument to the constructor
     * contains a repeated point.
     */
    public BruteCollinearPoints(Point[] points)
    {
        // Check for null pointer
        if (points == null)
        {
            throw new IllegalArgumentException("argument is null");
        }

        mPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++)
        {
            mPoints[i] = points[i];
        }

        // check for null elements
        for (int i = 0; i < mPoints.length; i++)
        {
            if (mPoints[i] == null)
            {
                throw new IllegalArgumentException("point is null");
            }
        }

        // check for duplicate elements
        Arrays.sort(mPoints);
        for (int i = 0; i < mPoints.length; i++)
        {
            if (i < mPoints.length - 1 && mPoints[i+1] != null)
            {
                if (mPoints[i].compareTo(mPoints[i+1]) == 0)
                {
                    throw new IllegalArgumentException("duplicate points found");
                }
            }
        }

        // Perform a brute force way to find all line segments
        discoverLineSegments(mPoints);
    }

    /**
     * Perform a brute force way to find all line segments with given points
     *
     * @param points Input points for finding all
     * line segments containing 4 points
     */
    private void discoverLineSegments(final Point[] points)
    {
        // if input has less than four points, no valid segment
        if (points.length < 4)
        {
            return;
        }

        for (int i = 0; i < points.length - 3; i++)
        {
            double slopeIJ, slopeIM, slopeIN;
            for (int j = i + 1; j < points.length - 2; j++)
            {
                slopeIJ = points[i].slopeTo(points[j]);
                for (int m = j + 1; m < points.length - 1; m++)
                {
                    slopeIM = points[i].slopeTo(points[m]);
                    // if the first three points are not in a segment
                    // no need to test the forth one
                    if (slopeIJ != slopeIM)
                    {
                        continue;
                    }
                    for (int n = m + 1; n < points.length; n++)
                    {
                        slopeIN = points[i].slopeTo(points[n]);
                        // We are sure slopeIJ == slopeIM here
                        if (slopeIM == slopeIN)
                        {
                            mLineSegments.add(new LineSegment(points[i], points[n]));
                        }
                    }
                }
            }
        }
    }

    /**
     * Getter for the number of line segments discovered from given points
     *
     * @return number of line segments found
     */
    public int numberOfSegments()
    {
        return mLineSegments.size();
    }

    /**
     * Getter for all found line segments with given points
     *
     * @return all line segments
     */
    public LineSegment[] segments()
    {
        LineSegment[] lineSegment = new LineSegment[mLineSegments.size()];
        mLineSegments.toArray(lineSegment);
        return lineSegment;
    }

    public static void main(String[] args)
    {
       // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++)
        {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points)
        {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments())
        {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}