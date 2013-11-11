package cmsc420.canonicalsolution;


import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 * Compares two cities based on location of x and y coordinates. First compares
 * the x values of each {@link City}. If the x values are the same, then the y values of
 * each City are compared.
 * 
 * @author Ben Zoller
 * @version 1.0, 23 Jan 2007
 */
public class CityLocationComparator implements Comparator<City> {
	private Point2D.Float point;
	private double distanceToOne;
	private double distanceToTwo;
	
	public CityLocationComparator(){
		this.point = null;
	}
	
	public CityLocationComparator(Point2D.Float point){
		this.point = point;
	}
	public int compare(final City one, final City two) {
		if (this.point == null) {
			if (one.getX() < two.getX()) {
				return -1;
			} else if (one.getX() > two.getX()) {
				return 1;
			} else {
				/* one.getX() == two.getX() */
				if (one.getY() < two.getY()) {
					return -1;
				} else if (one.getY() > two.getY()) {
					return 1;
				} else {
					/* one.getY() == two.getY() */
					return 0;
				}
			}
		} else {
			distanceToOne = point.distance(one.toPoint2D());
			distanceToTwo = point.distance(two.toPoint2D());
			if(distanceToOne > distanceToTwo){
				return 1;
			} else if (distanceToOne < distanceToTwo){
				return -1;
			} else {
				return 0;
			}
		}
	}
}