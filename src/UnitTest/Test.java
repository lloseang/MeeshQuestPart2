package UnitTest;

import java.util.Iterator;
import java.util.Map.Entry;

import org.junit.BeforeClass;

import cmsc420.canonicalsolution.City;
import cmsc420.canonicalsolution.CityNameComparator;
import cmsc420.sortedmap.AvlGTree;
import junit.framework.TestCase;

public class Test extends TestCase {
	private static City city;
	private static AvlGTree<City, String> avlg;
	private static StringBuffer resultBuffer;
	private static String expected;
	
    @BeforeClass
    public void setUp() {
    	avlg = createAVLGtree("QAZWSXEDCRFVTGBYHNUJMIKOLP");
    	resultBuffer = new StringBuffer();
    }

	public void testStressAVLG(){

	}

	public void testKeySet(){
		resultBuffer.setLength(0);
		for(City key : avlg.keySet()){
			resultBuffer.append(key.toString());
		}
		expected = "A(0.0, 0.0)B(0.0, 0.0)C(0.0, 0.0)D(0.0, 0.0)E(0.0, 0.0)F(0.0, 0.0)"
				+ "G(0.0, 0.0)H(0.0, 0.0)I(0.0, 0.0)J(0.0, 0.0)K(0.0, 0.0)L(0.0, 0.0)"
				+ "M(0.0, 0.0)N(0.0, 0.0)O(0.0, 0.0)P(0.0, 0.0)Q(0.0, 0.0)R(0.0, 0.0)"
				+ "S(0.0, 0.0)T(0.0, 0.0)U(0.0, 0.0)V(0.0, 0.0)W(0.0, 0.0)X(0.0, 0.0)"
				+ "Y(0.0, 0.0)Z(0.0, 0.0)";
		assertEquals(expected, resultBuffer.toString());
	}

	public void testEntrySet(){
		resultBuffer.setLength(0);
		AvlGTree<City, String> avlg = createAVLGtree("QAZWSXE");
		Iterator<Entry<City, String>> iter = avlg.entrySet().iterator();
		
		while(iter.hasNext()){
			resultBuffer.append(iter.next().toString());
		}
		
		expected = "Node<A(0.0, 0.0), A-Value>Node<E(0.0, 0.0), E-Value>Node<Q(0.0, 0.0), "
				+ "Q-Value>Node<S(0.0, 0.0), S-Value>Node<W(0.0, 0.0), W-Value>Node<X(0.0, 0.0), "
				+ "X-Value>Node<Z(0.0, 0.0), Z-Value>";
		
		assertEquals(expected, resultBuffer.toString());
	}

	public void testValues(){
		resultBuffer.setLength(0);
		AvlGTree<City, String> avlg = createAVLGtree("QAZWSXEDCRFVTGBYHNUJMIKOLP");
		Iterator<String> iter = avlg.values().iterator();
		
		while(iter.hasNext()){
			resultBuffer.append(iter.next().toString());
		}
		expected = "A-ValueB-ValueC-ValueD-ValueE-ValueF-ValueG-ValueH-ValueI-ValueJ-ValueK-Value"
				+ "L-ValueM-ValueN-ValueO-ValueP-ValueQ-ValueR-ValueS-ValueT-ValueU-ValueV-ValueW-"
				+ "ValueX-ValueY-ValueZ-Value";
		assertEquals(expected, resultBuffer.toString());
	}

	public void testFirstKey(){
		assertEquals("A(0.0, 0.0)", avlg.firstKey().toString());
	}
	
	public void testLastKey(){
		assertEquals("Z(0.0, 0.0)", avlg.lastKey().toString());
	}

	private AvlGTree<City, String> createAVLGtree(String string) {
		AvlGTree<City, String> tree = new AvlGTree<City,String>(new CityNameComparator(), 1);
		for (char a : string.toCharArray()){
			city = new City(String.valueOf(a), 0,0,0,"Black");
			tree.put(city, String.valueOf(a) + "-Value");
		}
		return tree;
	}

}
