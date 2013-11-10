package cmsc420.pmquadtree;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class RoadComparator implements Comparator<Road> {
	private Point2D.Float point;
	private Double distanceToRoad1;
	private Double distanceToRoad2;
	
	public RoadComparator(){
		this.point = null;
	}
	
	public RoadComparator(Point2D.Float point){
		this.point = point;
	}
	
	public int compare(Road road1, Road road2) {
		if(point == null){
			if(road1.start.getName().compareTo(road2.start.getName()) == 0){
				return road1.end.getName().compareTo(road2.end.getName());
			} else
				return road1.start.getName().compareTo(road2.start.getName());
		} else {
			distanceToRoad1 = road1.getLine().ptSegDist(point);
			distanceToRoad2 = road2.getLine().ptSegDist(point);
			
			if(distanceToRoad1 > distanceToRoad2){
				return 1;
			} else if (distanceToRoad1 < distanceToRoad2 ){
				return -1;
			} else {
				return 0;
			}
		}
	}
}
