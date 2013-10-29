package UnitTest;

import org.junit.BeforeClass;

import cmsc420.canonicalsolution.City;
import cmsc420.canonicalsolution.CityLocationComparator;
import cmsc420.canonicalsolution.CityNameComparator;
import cmsc420.sortedmap.AvlGTree;
import cmsc420.*;
import junit.framework.Assert;
import junit.framework.TestCase;

public class Test extends TestCase {
	String alphabet;
	AvlGTree<City, String> avlg;
	City city;
	
    @BeforeClass
    public void setUp() {
    	alphabet = "IABMNOPCDEZKLFGH";
    	
    	avlg = new AvlGTree<City, String>(new CityNameComparator(), 1);
    	for (char a : alphabet.toCharArray()){
    		city = new City(String.valueOf(a), 10, 5, 10, "Black");
			avlg.put(city, String.valueOf(a));
		}
    }

	public void testStressAVLG(){
		
	}
	
	public void testFirstKey(){
		assertEquals("A(10.0, 5.0)", avlg.firstKey().toString());
	}
	
	public void testLastKey(){
		assertEquals("Z(10.0, 5.0)", avlg.lastKey().toString());
	}
	
	public void testSize(){
		System.out.println(avlg.size());
	}
}
