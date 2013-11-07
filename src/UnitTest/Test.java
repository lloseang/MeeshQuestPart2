package UnitTest;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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

	public void testEquals(){
		AvlGTree<City, String> avlg1 = createAVLGtree("RECCAC");
		AvlGTree<City, String> avlg2 = createAVLGtree("CACCER");
		assertTrue(avlg1.equals(avlg2));
		avlg2.put(new City("A", 0,0,0,"Black"), "A");
		assertFalse(avlg1.equals(avlg2));
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

	public void testEntrySetToArray(){
		resultBuffer.setLength(0);
		expected = "Node<A(0.0, 0.0), A-Value>Node<B(0.0, 0.0), B-Value>Node<C(0.0, 0.0),"
				+ " C-Value>Node<D(0.0, 0.0), D-Value>Node<E(0.0, 0.0), E-Value>Node<F(0.0, 0.0),"
				+ " F-Value>Node<G(0.0, 0.0), G-Value>Node<H(0.0, 0.0), H-Value>Node<I(0.0, 0.0), "
				+ "I-Value>Node<J(0.0, 0.0), J-Value>Node<K(0.0, 0.0), K-Value>Node<L(0.0, 0.0),"
				+ " L-Value>Node<M(0.0, 0.0), M-Value>Node<N(0.0, 0.0), N-Value>Node<O(0.0, 0.0),"
				+ " O-Value>Node<P(0.0, 0.0), P-Value>Node<Q(0.0, 0.0), Q-Value>Node<R(0.0, 0.0),"
				+ " R-Value>Node<S(0.0, 0.0), S-Value>Node<T(0.0, 0.0), T-Value>Node<U(0.0, 0.0),"
				+ " U-Value>Node<V(0.0, 0.0), V-Value>Node<W(0.0, 0.0), W-Value>Node<X(0.0, 0.0),"
				+ " X-Value>Node<Y(0.0, 0.0), Y-Value>Node<Z(0.0, 0.0), Z-Value>";
		for(Object e : avlg.entrySet().toArray()){
			resultBuffer.append(e.toString());
		}
		assertEquals(expected, resultBuffer.toString());
	}

	public void testEntrySetSize(){
		resultBuffer.setLength(0);
		resultBuffer.append(avlg.entrySet().size());
		expected = "26";
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

	public void testSize(){
		assertEquals("26", String.valueOf(avlg.size()));
	}
	
	public void testCompare(){
		AvlGTree<String, String> avlg = createAVLGtreeString("ABCE");
		SortedMap<String, String> avlg1 = avlg.subMap("F", "H");
		System.out.println(avlg1.entrySet().size());
		System.out.println(avlg1.entrySet().contains(new MyEntry<String, String>("D","D")));
		for(Object o : avlg1.entrySet().toArray()){
			System.out.println(o.toString());
		}
//		Iterator iter = avlg1.entrySet().iterator();
//		avlg1.put("D","D");
//		while(iter.hasNext()){
//			System.out.println(iter.next());
//		}
		
	}
	
	public void testEntrySet2(){
		SortedMap<String, String> m = new AvlGTree<String, String>(String.CASE_INSENSITIVE_ORDER, 1);
		m.put("Auto","Fail");
		Set<Map.Entry<String, String> > s = m.entrySet();
		m.put("F","---");
		if(!s.contains(new MyEntry<String, String>("F", "---"))){
			System.out.println("You fail!");
			fail();
		}
	}
	
	public void testSubTreeMap(){
	      TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
	      SortedMap<Integer, String> treemapincl = new TreeMap<Integer, String>();
	            
	      // populating tree map
	      treemap.put(2, "two");
	      treemap.put(1, "one");
	      
	       
	      treemapincl = treemap.subMap(1, 3);
	      treemapincl.put(3, "three");
	      System.out.println(treemapincl);
	}
	
	public void testAvlGTreeSubMap(){
		AvlGTree<String, String> tree = createAVLGtreeString("ABCDFG");
		SortedMap<String, String> sm = tree.subMap("A", "E");
		tree.put("E", "E-Value");
		
		for(String value : sm.values()){
			
		}
	}
	
	private AvlGTree<City, String> createAVLGtree(String string) {
		AvlGTree<City, String> tree = new AvlGTree<City,String>(new CityNameComparator(), 1);
		for (char a : string.toCharArray()){
			city = new City(String.valueOf(a), 0,0,0,"Black");
			tree.put(city, String.valueOf(a) + "-Value");
		}
		return tree;
	}
	
	private AvlGTree<String, String> createAVLGtreeString(String string){
		AvlGTree<String, String> tree = new AvlGTree<String,String>(String.CASE_INSENSITIVE_ORDER,1);
		for (char a : string.toCharArray()){
			tree.put(String.valueOf(a), String.valueOf(a) + "-Value");
		}
		return tree;
	}
		
	final class MyEntry<K, V> implements Map.Entry<K, V> {
	    private final K key;
	    private V value;

	    public MyEntry(K key, V value) {
	        this.key = key;
	        this.value = value;
	    }

	    @Override
	    public K getKey() {
	        return key;
	    }

	    @Override
	    public V getValue() {
	        return value;
	    }

	    @Override
	    public V setValue(V value) {
	        V old = this.value;
	        this.value = value;
	        return old;
	    }
	}

}
