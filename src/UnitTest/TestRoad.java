//package UnitTest;
//
//import java.util.TreeMap;
//import java.util.TreeSet;
//
//import cmsc420.canonicalsolution.City;
//import cmsc420.pmquadtree.PM3Quadtree;
//import cmsc420.pmquadtree.Road;
//import cmsc420.pmquadtree.RoadComparator;
//import cmsc420.sortedmap.AvlGTree;
//import junit.framework.TestCase;
//
//public class TestRoad extends TestCase{
//	public void testEquals(){
//		City A = createCity("A", 16, 16, 1);
//		City B = createCity("B", 48, 16, 1);
//		City C = createCity("C", 46, 48, 1);
//		City D = createCity("D", 16, 48, 1);
//		City E = createCity("E", 30, 30, 1);
//		City F = createCity("F", 32, 32, 1);
////		
//		PM3Quadtree pm3 = new PM3Quadtree();
//		pm3.setRange(64, 64);
//		
//		pm3.add(new Road(A,B));
//		pm3.add(new Road(B,C));
//		pm3.add(new Road(C,D));
//		pm3.add(new Road(D,A));
//		pm3.add(new Road(A,E));
//	}
//	
//	/*
//	 * 
//	 * <mapRoad start="G" end="H"/>
//<mapRoad start="I" end="G"/>
//<mapRoad start="A" end="I"/>
//<mapRoad start="I" end="H"/>
//	 * 
//	 */
//	
//	public void testKeving(){
//		  AvlGTree<String, Integer> temp = new AvlGTree<String, Integer>(null, 1);
//		  TreeMap<String, Integer> treeTemp = new TreeMap<String, Integer>();
//		  
//		  temp.put("Kevin", 1);
//		  treeTemp.put("Kevin", 1);
//		  
//		  System.out.println(temp.toString());
//		  System.out.println(treeTemp);
//		     
//		  System.out.println(temp.hashCode());
//		  System.out.println(treeTemp.hashCode());
//	}
//	public City createCity(String name, int x, int y, int id){
//		return new City(name, x, y, 0, "black");
//	}
//}
