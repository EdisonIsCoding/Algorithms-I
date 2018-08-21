/******************************************************************************
 *  Compilation:  javac FastCollinearPoints.java
 *  Execution:    java FastCollinearPoints
 *  Dependencies: Point.java LineSegment.java
 *
 *  A faster way based on quick sort to examine 4 points at a time and checks
 *  whether they all lie on the same line segment, returning all such line segments.
 *
 *  Edited by Zhanwei(Edison) Ye
 ******************************************************************************/

import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints
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
    public FastCollinearPoints(Point[] points)
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

        // Perform a quick sort base method to find all line segments
        discoverLineSegments(mPoints);
    }

    /**
     * Perform a quick sort base method to find all line segments with given points
     *
     * @param points Input points sorted by neutral order for finding all
     * line segments containing 4 points
     */
    private void discoverLineSegments(final Point[] points)
    {
        // if input has less than four points, no valid segment
        if (points.length < 4)
        {
            return;
        }

        Point[] auxiliaryPoints = new Point[points.length];
        for (int i = 0; i < points.length; i++)
        {
            auxiliaryPoints[i] = points[i];
        }

        for (Point point : points)
        {
            // Sort input points by slope
            Arrays.sort(auxiliaryPoints, point.slopeOrder());

            // find all point with same slope
            int firstDiffSlopeIndex = 1;
            int sameSlopePointCount = 1;
            Point origin = auxiliaryPoints[0];
            double targetSlope = origin.slopeTo(auxiliaryPoints[firstDiffSlopeIndex]);
            double currentSlope;

            while (firstDiffSlopeIndex + sameSlopePointCount <= auxiliaryPoints.length)
            {
                if (firstDiffSlopeIndex + sameSlopePointCount == auxiliaryPoints.length)
                {
                    // Reach the end, check current slope have enough number of points
                    // to be a segment
                    if (sameSlopePointCount >= 3)
                    {
                        // Find a segment, add it to the list
                        addSegment(auxiliaryPoints, firstDiffSlopeIndex, sameSlopePointCount);
                    }
                    break;
                }

                currentSlope = origin.slopeTo(
                    auxiliaryPoints[firstDiffSlopeIndex + sameSlopePointCount]);

                if (currentSlope == targetSlope)
                {
                    sameSlopePointCount++;
                }
                else
                {
                    if (sameSlopePointCount >= 3)
                    {
                        // Find segment, add to list only if the origin is the smallest point
                        // in the segment
                        addSegment(auxiliaryPoints, firstDiffSlopeIndex, sameSlopePointCount);
                    }
                    // Update index and count
                    firstDiffSlopeIndex = firstDiffSlopeIndex + sameSlopePointCount;
                    sameSlopePointCount = 1;
                    targetSlope = origin.slopeTo(auxiliaryPoints[firstDiffSlopeIndex]);
                }
            }
        }
    }

    /**
     * Add line segment to list if there is no duplication or it's not a subsegment of exist
     * line segment.
     *
     * @param points Input array sorted by slope to the origin(points[0])
     * @param start Start index of the slope
     * @param count Count of points which have same slope to the origin
     */
    private void addSegment(final Point[] points, final int start, final int count)
    {
        Arrays.sort(points, start, start + count);
        for (int i = 0; i < count; i++)
        {
            if (points[0].compareTo(points[start+i]) >= 0)
            {
                return;
            }
        }
        mLineSegments.add(new LineSegment(points[0], points[start+count-1]));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments())
        {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}