package cmsc420.pmquadtree;

import java.util.Comparator;

public class RoadComparator implements Comparator<Road> {
	public int compare(Road road1, Road road2) {
		if(road1.start.getName().compareTo(road2.start.getName()) == 0){
			return road1.end.getName().compareTo(road2.end.getName());
		} else
			return road1.start.getName().compareTo(road2.start.getName());
	}
}
