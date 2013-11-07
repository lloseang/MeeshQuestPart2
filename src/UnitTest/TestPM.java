package UnitTest;

import cmsc420.canonicalsolution.City;
import cmsc420.pmquadtree.PM3Quadtree;
import cmsc420.pmquadtree.Road;
import junit.framework.TestCase;

public class TestPM extends TestCase {
	
	public void testPM(){
		PM3Quadtree pmQuadtree = new PM3Quadtree();
		int spatialWidth = 32;
		int spatialHeight = 32;
		pmQuadtree.setRange(spatialWidth, spatialHeight);
		
		Road r1 = new Road(new City("A", 2, 2, 10, "A"), new City("B", 30, 30, 10, "B"));
		Road r2 = new Road(new City("C", 12, 12, 10, "C"), new City("D", 12, 4, 10, "D"));
		Road r3 = new Road(new City("E", 6, 5, 10, "E"), new City("F", 9, 5, 10, "F"));
		pmQuadtree.add(r1);
		pmQuadtree.add(r2);
		pmQuadtree.add(r3);
		
		System.out.println("Success!");
	}
}
