//package UnitTest;
//
//import java.awt.geom.Line2D;
//import java.awt.geom.Rectangle2D;
//import java.util.TreeSet;
//
//import cmsc420.canonicalsolution.City;
//import cmsc420.geom.Inclusive2DIntersectionVerifier;
//import cmsc420.pmquadtree.PM3Quadtree;
//import cmsc420.pmquadtree.Road;
//import junit.framework.TestCase;
//
//public class TestPM extends TestCase {
//	
//	public void testPM(){
//		PM3Quadtree pmQuadtree = new PM3Quadtree();
//		int spatialWidth = 8;
//		int spatialHeight = 8;
//		Road r1 = new Road(new City("C", 0,0, 10, "C"), new City("B", 7, 8, 10, "B"));	
//		Road r2 = new Road(new City("C", 0,0, 10, "C"), new City("E", 0, 7, 10, "E"));	
//		pmQuadtree.setRange(spatialWidth, spatialHeight);
//		pmQuadtree.setSpatialBound();
//		
//		pmQuadtree.add(r1);
//		pmQuadtree.add(r2);
//	}
//	
//	public void testRectangle(){
//		Rectangle2D.Float rect = new Rectangle2D.Float(0,0,8,8);
//		Line2D.Float line = new Line2D.Float(-1,4,5,4);
//		System.out.println(Inclusive2DIntersectionVerifier.intersects(line, rect));
//	}
//	
//	public void testLine(){
//		Line2D.Float line1 = new Line2D.Float(0,0,1,1);
//		Line2D.Float line2 = new Line2D.Float(3,3,2,2);
//		System.out.println(Inclusive2DIntersectionVerifier.intersects(line1, line2));
//		
//		TreeSet<String> ts = new TreeSet<String>();
//		ts.add("Hello");
//		ts.add("Hello");
//		ts.add("Hello");
//		for(Object s : ts.toArray()){
//			System.out.println(s.toString());
//		}
//		
//	}
//}
