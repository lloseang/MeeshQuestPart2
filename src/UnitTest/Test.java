//package UnitTest;
//
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//import org.junit.BeforeClass;
//
//import cmsc420.canonicalsolution.City;
//import cmsc420.canonicalsolution.CityNameComparator;
//import cmsc420.sortedmap.AvlGTree;
//import junit.framework.TestCase;
//
//public class Test extends TestCase {
//	private static City city;
//	private static AvlGTree<String, String> avlg;
//	private static StringBuffer resultBuffer;
//	private static String expected;
//	
//    @BeforeClass
//    public void setUp() {
//    	avlg = createAVLGtreeString("GBYHNUJMIKOLP");
//    	resultBuffer = new StringBuffer();
//    }
//
//	public void testEquals(){
//		AvlGTree<City, String> avlg1 = createAVLGtree("RECCAC");
//		AvlGTree<City, String> avlg2 = createAVLGtree("CACCER");
//		assertTrue(avlg1.equals(avlg2));
//		assertTrue(avlg1.equals(avlg2));
//	}
//	
//	public void testFirstKey(){
////		System.out.println(avlg.containsKey("P"));
//	}
//	
//	public void testContainsValue(){
//		System.out.println(avlg.containsValue(null));
//	}
//	
//	private AvlGTree<City, String> createAVLGtree(String string) {
//		AvlGTree<City, String> tree = new AvlGTree<City,String>(new CityNameComparator(), 1);
//		for (char a : string.toCharArray()){
//			city = new City(String.valueOf(a), 0,0,0,"Black");
//			tree.put(city, String.valueOf(a) + "-Value");
//		}
//		return tree;
//	}
//	
//	private AvlGTree<String, String> createAVLGtreeString(String string){
//		AvlGTree<String, String> tree = new AvlGTree<String,String>(String.CASE_INSENSITIVE_ORDER,1);
//		for (char a : string.toCharArray()){
//			tree.put(String.valueOf(a), String.valueOf(a) + "-Value");
//		}
//		return tree;
//	}
//		
//	final class MyEntry<K, V> implements Map.Entry<K, V> {
//	    private final K key;
//	    private V value;
//
//	    public MyEntry(K key, V value) {
//	        this.key = key;
//	        this.value = value;
//	    }
//
//	    @Override
//	    public K getKey() {
//	        return key;
//	    }
//
//	    @Override
//	    public V getValue() {
//	        return value;
//	    }
//
//	    @Override
//	    public V setValue(V value) {
//	        V old = this.value;
//	        this.value = value;
//	        return old;
//	    }
//	}
//
//}
