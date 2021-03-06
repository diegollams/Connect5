package com.thebitcorps.connect5.models;

/**
 * Created by diegollams on 11/19/15.
 */
public class Point {
	private int x,y;
	private int fitness;


	public Point() {
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
		this.fitness = 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Point point = (Point) o;

		if (x != point.x) return false;
		return y == point.y;

	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}

}
