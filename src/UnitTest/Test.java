package UnitTest;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.BeforeClass;

import cmsc420.canonicalsolution.City;
import cmsc420.canonicalsolution.CityNameComparator;
import cmsc420.sortedmap.AvlGTree;
import junit.framework.TestCase;

public class Test extends TestCase {
	private static City city;
	
    @BeforeClass
    public void setUp() {

    }

	public void testStressAVLG(){

	}
	
	public void testRotateLeft(){
		AvlGTree<City, String> avlg = createAVLGtree("QAZWSXEDCRFV");
		System.out.println(avlg.toString());
	}

	private AvlGTree<City, String> createAVLGtree(String string) {
		AvlGTree<City, String> tree = new AvlGTree<City,String>(new CityNameComparator(), 1);
		for (char a : string.toCharArray()){
			city = new City(String.valueOf(a), 0,0,0,"Black");
			tree.put(city, String.valueOf(a));
		}
		return tree;
	}
	
	public void testEntrySet(){
		AvlGTree<City, String> avlg = createAVLGtree("ZAC");

		Iterator<Entry<City, String>> iter = avlg.entrySet().iterator();
		
		while(iter.hasNext()){
			System.out.println(iter.next().toString());
		}
		System.out.println("Done");
	}

//	
//	public void testFirstKey(){
//		assertEquals("A(10.0, 5.0)", avlg.firstKey().toString());
//	}
//	
//	public void testLastKey(){
//		assertEquals("Z(10.0, 5.0)", avlg.lastKey().toString());
//	}
//	
//	public void testSize(){
//		System.out.println(avlg.size());
//	}
}
