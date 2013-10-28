package UnitTest;

import cmsc420.canonicalsolution.City;
import cmsc420.canonicalsolution.CityLocationComparator;
import cmsc420.canonicalsolution.CityNameComparator;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.*;
import junit.framework.TestCase;

public class Test extends TestCase {
	public void testAVLG(){
		City baltimore = new City("D", 10, 5, 10, "Black");
		City boston = new City("H", 15, 10, 20, "Blue");
		City miami = new City("E", 15, 5, 100, "Red");
		
		
		AvlGTree<City, String> avlg = new AvlGTree<City, String>(new CityNameComparator(), 1);
		avlg.put(baltimore, "B'almore");
		avlg.put(boston, "boston");
		avlg.put(miami, "Miami");
		
		System.out.print("yes");;
	}
}
