package UnitTest;

import java.util.TreeSet;

import cmsc420.canonicalsolution.City;
import cmsc420.pmquadtree.Road;
import cmsc420.pmquadtree.RoadComparator;
import junit.framework.TestCase;

public class TestRoad extends TestCase{
	public void testRoad(){
		Road r1 = new Road(new City("A", 2, 2, 10, "A"), new City("B", 30, 30, 10, "B"));
		Road r2 = new Road(new City("A", 12, 12, 10, "A"), new City("D", 12, 4, 10, "D"));
		Road r3 = new Road(new City("E", 6, 5, 10, "E"), new City("F", 9, 5, 10, "F"));
		
		TreeSet<Road> roads = new TreeSet<Road>(new RoadComparator());
		roads.add(r2);
		roads.add(r3);
		roads.add(r1);
		Object r = roads.toArray();
		System.out.println(r.toString());
	}
}
