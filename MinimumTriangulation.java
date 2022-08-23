package PolygontTessalation;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MinimumTriangulation {

	private ArrayList<Point2D> polygon;
	private Double[][] memo;

	public MinimumTriangulation() {

	}

	// Suppose the convex polygon with n vertices, all vertices named from 0 to n-1 in
	// clockwise direction ( 0,1,2,..,n-1)
	// pick a vertex 'k' (0 < k < n-1). The minimum cost of given polygon equal to
	// the minimum cost of 2 sub polygons (0,..,k) and (k,..,n-1) plus disances of
	// two diagonals
	// [0,k] and [k,n-1] (because those diagonals still be internal diagonals of the
	// original polygon)
	public Double minCostBruteForce(ArrayList<Point2D> polygon) {
		this.polygon = polygon;
		return minCostBF(0, this.polygon.size() - 1);
	}

	private Double minCostBF(int i, int j) {
		if (j < i + 3)
			return (double) 0;
		Double cost = Double.MAX_VALUE;
		for (int k = i + 1; k < j; k++) {
			cost = Math.min(cost, minCostBF(i, k) + minCostBF(k, j) + distance(i, k) + distance(k, j));
		}
		return cost;
	}

	// To me I think using memoization is the exact approach, it growth in
	// polynomial time, it's similar to brute force approach but using array to
	// store the result of subproblem
	// It prevents solving overlapped subproblems, however it's not the fastest
	// solution but it maintains the correctness.
	public void minCostMemo(ArrayList<Point2D> polygon) {
		this.polygon = polygon;
		int size = polygon.size();
		memo = new Double[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				memo[i][j] = (double) -1;
			}
		}
		long startime = System.nanoTime();
		System.out.println("Minimum triangulation cost using memoization: " + MemoSearch(0, this.polygon.size() - 1));
		System.out.println("Duration for memoization technique: " + (System.nanoTime() - startime) + "\n");

	}

	private Double MemoSearch(int i, int j) {
		if (j < i + 3)
			return (double) 0;
		if (memo[i][j] != -1)
			return memo[i][j];

		Double min = Double.MAX_VALUE;
		for (int k = i + 1; k < j; k++) {
			min = Math.min(min, MemoSearch(i, k) + MemoSearch(k, j) + distance(i, k) + distance(k, j));
		}
		memo[i][j] = min;
		return min;
	}

	// Greedy technique

	public Double mincostGreedy(ArrayList<Point2D> polygon) {
		int size = polygon.size();
		if (size < 4)
			return (double) 0;

		Double minCost = Double.MAX_VALUE;
		int eliminatedIndex = 0;
		// find the shortest internal edge in clockwise direction
		for (int i = 0; i < size; i++) {
			double distance = polygon.get(i).distance(polygon.get((i + 2) % size));
			if (distance < minCost) {
				minCost = distance;
				eliminatedIndex = (i + 1) % size;
			}
		}
		// remove the vertex in the middle of vertex_i and vertex_i+2, we have smaller
		// convex polygon as a subproblem.
		polygon.remove(eliminatedIndex);

		return minCost + mincostGreedy(polygon);

	}

	private Double distance(int a, int b) {
		if (Math.abs((b - a) % polygon.size()) < 2)
			return (double) 0;
		return polygon.get(a).distance(polygon.get(b));
	}

	public static void main(String[] args) {
		ArrayList<Point2D> convexPolygon = new ArrayList<Point2D>();
//		convexPolygon.add(new Point2D.Double(0, 0));
//		convexPolygon.add(new Point2D.Double(0, 2));		//	Polygon 1 for testing
//		convexPolygon.add(new Point2D.Double(1, 2));
//		convexPolygon.add(new Point2D.Double(2, 1));
//		convexPolygon.add(new Point2D.Double(1, 0));
//		convexPolygon.add(new Point2D.Double(1 / 2, -2));

		convexPolygon.add(new Point2D.Double(3, -4));
		convexPolygon.add(new Point2D.Double(0, -5));
		convexPolygon.add(new Point2D.Double(-3, -4));
		convexPolygon.add(new Point2D.Double(-6, -2));
		convexPolygon.add(new Point2D.Double(-7, 1)); 		// Polygon 2 for testing
		convexPolygon.add(new Point2D.Double(-7, 4));
		convexPolygon.add(new Point2D.Double(-4, 9));
		convexPolygon.add(new Point2D.Double(-2, 10));
		convexPolygon.add(new Point2D.Double(4, 10));
		convexPolygon.add(new Point2D.Double(8, 8));
		
		//when we create the polygon without these points, the results will be the same
		//But when added those, the result of greedy method is not optimal
		// The greedy method does not always give the optimal solution but it really fast
		
		// convexPolygon.add(new Point2D.Double(8,4));	 	
		// convexPolygon.add(new Point2D.Double(6,-1));	 	

		MinimumTriangulation tessalation = new MinimumTriangulation();
		Long startime = System.nanoTime();

		System.out.println("Minimum triangulation cost using brute force: "+ tessalation.minCostBruteForce(convexPolygon));
		System.out.println("Duration for brute force: " + (System.nanoTime() - startime) + "\n");

		tessalation.minCostMemo(convexPolygon);

		startime = System.nanoTime();
		System.out.println("Minimum triangulation cost using greedy technique: " + tessalation.mincostGreedy(convexPolygon));
		System.out.println("Duration for greedy technique: " + (System.nanoTime() - startime));
	}
}
