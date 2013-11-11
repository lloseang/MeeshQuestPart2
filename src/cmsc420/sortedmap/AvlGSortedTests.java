package cmsc420.sortedmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;








import junit.framework.TestCase;
import static org.junit.Assert.*;

import org.junit.Test;

import cmsc420.canonicalsolution.City;
import cmsc420.canonicalsolution.CityLocationComparator;

//import cmsc420.meeshquest.part2.AvlGNode;
//import cmsc420.meeshquest.part2.City;
//import cmsc420.meeshquest.part2.CityCoordinateComparator;

public class AvlGSortedTests extends TestCase{

	public void testEntrySet(){
		AvlGTree<String, Integer> t1=new AvlGTree<String, Integer>(String.CASE_INSENSITIVE_ORDER, 1);
		SortedMap<String, Integer> s1=new TreeMap<String, Integer>();
		t1.put("a", 1);
		t1.put("b", 2);
		t1.put("c", 3);
		t1.put("d", 4);
		t1.put("e", 5);
		Set<java.util.Map.Entry<String, Integer>> e1=t1.entrySet();
		Iterator<java.util.Map.Entry<String, Integer>> i1=t1.entrySet().iterator();
		while(i1.hasNext()){
			java.util.Map.Entry<String, Integer> e=i1.next();
			assertTrue(t1.containsKey(e.getKey()));
			assertTrue(t1.containsValue(e.getValue()));
			s1.put(e.getKey(), e.getValue());
			assertTrue(t1.get(e.getKey()).equals(s1.get(e.getKey())));
		}
		Set<java.util.Map.Entry<String, Integer>> e2=s1.entrySet();
		assertTrue(t1.equals(s1));
		assertTrue(t1.entrySet().equals(s1.entrySet()));
		assertTrue(e1.equals(e2));
		assertTrue(e1.hashCode()==e2.hashCode());
		t1.put("f", 6);
		assertFalse(t1.equals(s1));
		assertFalse(e1.equals(e2));
		assertFalse(t1.entrySet().equals(s1.entrySet()));
		assertFalse(e1.hashCode()==e2.hashCode());
		s1.put("f", 6);
		assertTrue(t1.equals(s1));
		assertTrue(e1.equals(e2));
		assertTrue(t1.entrySet().equals(s1.entrySet()));
		assertTrue(e1.hashCode()==e2.hashCode());
	}

	public void testKeySet(){
		AvlGTree<String, Integer> t1=new AvlGTree<String, Integer>(String.CASE_INSENSITIVE_ORDER, 1);
		SortedMap<String, Integer> s1=new TreeMap<String, Integer>();
		t1.put("a", 1);
		t1.put("b", 2);
		t1.put("c", 3);
		t1.put("d", 4);
		t1.put("e", 5);
		Set<String> e1=t1.keySet();
		Iterator<String> i1=t1.keySet().iterator();
		while(i1.hasNext()){
			String e=i1.next();
			assertTrue(t1.containsKey(e));
			s1.put(e, t1.get(e));
			assertTrue(t1.get(e).equals(s1.get(e)));
		}
		Set<String> e2=s1.keySet();
		assertTrue(t1.equals(s1));
		assertTrue(e1.equals(e2));
		assertTrue(e1.hashCode()==e2.hashCode());
		t1.put("f", 6);
		assertFalse(t1.equals(s1));
		assertFalse(e1.equals(e2));
		assertFalse(e1.hashCode()==e2.hashCode());
		s1.put("f", 6);
		assertTrue(t1.equals(s1));
		assertTrue(e1.equals(e2));
		assertTrue(e1.hashCode()==e2.hashCode());
	}

	public void testValues(){
		AvlGTree<String, Integer> t1=new AvlGTree<String, Integer>(String.CASE_INSENSITIVE_ORDER, 1);
		SortedMap<String, Integer> s1=new TreeMap<String, Integer>();
		t1.put("a", 1);
		t1.put("b", 2);
		t1.put("c", 3);
		t1.put("d", 4);
		t1.put("e", 5);
		s1.putAll(t1);
		Collection<Integer> e1=t1.values();
		Iterator<Integer> i1=t1.values().iterator();
		while(i1.hasNext()){
			Integer e=i1.next();
			assertTrue(t1.containsValue(e));
		}
		Collection<Integer> e2=s1.values();
		Iterator<Integer> i2=s1.values().iterator();
		while(i2.hasNext()){
			Integer e=i2.next();
			assertTrue(s1.containsValue(e));
		}
		t1.put("f", 6);
		assertTrue(e1.contains(6));
		assertFalse(e1.equals(e2));
		s1.put("f", 6);
	}
	
	
	public void testSubHeadTailMap1(){
		AvlGTree<String, Integer> t1=new AvlGTree<String, Integer>(String.CASE_INSENSITIVE_ORDER, 1);
		t1.put("a", 1);
		t1.put("b", 2);
		t1.put("c", 3);
		t1.put("d", 4);
		t1.put("e", 5);
		t1.put("h", 7);
		SortedMap<String, Integer> sub1=t1.subMap("a", "g");
		t1.put("f", 6);
		
		assertTrue(sub1.firstKey().equals( t1.firstKey()));
		assertFalse(sub1.lastKey().equals(t1.lastKey()));
		assertTrue(sub1.containsKey("f"));
		assertTrue(sub1.lastKey().equals("f"));
		sub1=t1.tailMap("a");
		assertTrue(sub1.lastKey().equals("h"));
		assertTrue(sub1.equals(t1));
		assertTrue(t1.equals(sub1));	
	}
	
	public void testSubHeadTailMap2(){
		AvlGTree<String, Integer> a1=new AvlGTree<String, Integer>(String.CASE_INSENSITIVE_ORDER,1);
		SortedMap<String, Integer> t1=new TreeMap<String, Integer>();
		t1.put("a", 1);
		t1.put("b", 2);
		t1.put("c", 3);
		t1.put("d", 4);
		t1.put("e", 5);
		t1.put("f", 6);
		t1.put("g", 7);
		t1.put("h", 8);
		a1.putAll(t1);
		assertTrue(a1.equals(t1));
		SortedMap<String, Integer> sub1=a1.tailMap("a");
		SortedMap<String, Integer> sub2=t1.tailMap("a");
		assertTrue(sub1.equals(sub2));
		assertTrue(sub2.equals(sub1));
		assertTrue(sub1.equals(a1));
		assertTrue(sub1.equals(t1));
		assertTrue(t1.equals(sub1));
		assertTrue(a1.equals(sub1));
		sub1=a1.headMap("d");
		sub2=t1.headMap("d");
		assertTrue(sub1.equals(sub2));
		assertTrue(sub2.equals(sub1));
		assertFalse(sub1.equals(a1));
		assertFalse(sub1.equals(t1));
		assertFalse(t1.equals(sub1));
		assertFalse(a1.equals(sub1));
		sub1=a1.tailMap("e");
		sub2=t1.tailMap("e");
		assertTrue(sub2.equals(sub1));
		assertEquals(sub1.firstKey(), sub2.firstKey());
		assertEquals(sub1.lastKey(), sub2.lastKey());
		Iterator<java.util.Map.Entry<String, Integer>> e1= sub2.entrySet().iterator();
		while(e1.hasNext()){
			java.util.Map.Entry<String, Integer> e=e1.next();
			assertTrue(sub1.containsKey(e.getKey()));
			assertTrue(sub1.containsValue(e.getValue()));
			assertEquals(sub1.size(), sub2.size());
		}

		assertTrue(sub2.equals(sub1));
		
		assertFalse(sub1.equals(a1));
		assertFalse(sub1.equals(t1));
		assertFalse(t1.equals(sub1));
		assertFalse(a1.equals(sub1));	
	}
	//Tests clear, put, equals, isEmpty, size
	@Test
	public void testSortedMapFunctions1(){
		AvlGTree<String, Integer> t1=new AvlGTree<String, Integer>(String.CASE_INSENSITIVE_ORDER, 1);
		assertTrue(t1.isEmpty());

		SortedMap<String, Integer> s1=new TreeMap<String, Integer>();
		SortedMap<String, Integer> s2=new TreeMap<String, Integer>();
		assertTrue(s1.equals(t1));
		assertTrue(t1.equals(s1));
		
		t1.put("a", 1);
		assertFalse(s1.equals(t1));
		assertFalse(t1.equals(s1));
		t1.clear();
		assertTrue(t1.equals(s1));
		assertTrue(s1.equals(t1));
		assertTrue(t1.isEmpty());
		assertTrue(t1.size()==s1.size());
	}
	
	//Tests putAll, containsKey, containsValue, equals, hashCode, get, size
	@Test
	public void testSortedMapFunctions2(){
		SortedMap<String, Integer> t1=new TreeMap<String, Integer>();
		t1.put("a", 1);
		t1.put("b", 2);
		t1.put("c", 3);
		t1.put("d", 4);
		t1.put("e", 5);
		AvlGTree<String, Integer> a1=new AvlGTree<String, Integer>(String.CASE_INSENSITIVE_ORDER, 1);
		a1.putAll(t1);
		assertTrue(a1.containsKey("a"));
		assertFalse(a1.containsKey("f"));
		assertTrue(a1.containsValue(4));
		assertFalse(a1.containsValue(6));
		assertTrue(t1.get("a").equals(a1.get("a")));
		assertTrue(t1.size()==a1.size());
		assertTrue(a1.equals(t1));
		assertEquals(a1.hashCode(), t1.hashCode());
		t1.put("f", 6);
		assertFalse(t1.size()==a1.size());
		assertFalse(a1.equals(t1));
		assertFalse(a1.hashCode()== t1.hashCode());
	}
	
	@Test
	public void test1(){
		AvlGTree<String, Integer> t1=new AvlGTree<String, Integer>(String.CASE_INSENSITIVE_ORDER, 1);
		SortedMap<String, Integer> t2=new TreeMap<String, Integer>();
		assertTrue(t1.equals(t2));
		
		assertTrue(t1.hashCode()==t2.hashCode());
		assertTrue(t2.equals(t1));
		for(int var=0; var<100; var++){
			t1.put(Integer.toString(var), var);
			assertFalse(t1.equals(t2));
			assertFalse(t1.hashCode()==t2.hashCode());
			t2.put(Integer.toString(var), var);
			assertTrue(t1.equals(t2));
			assertTrue(t1.hashCode()==t2.hashCode());
		}
		assertTrue(t1.equals(t2));
		Set<java.util.Map.Entry<String, Integer>> s1=t1.entrySet();
		Set<java.util.Map.Entry<String, Integer>> s2=t2.entrySet();
		assertTrue(s1.equals(s2));
		assertTrue(s2.equals(s1));
		Iterator<java.util.Map.Entry<String, Integer>> i1=s1.iterator();
		Iterator<java.util.Map.Entry<String, Integer>> i2=s2.iterator();
		while(i1.hasNext()){
			assertTrue(s1.contains(i1.next()));
		}
		t1.put("101", 101);
		assertFalse(t1.equals(t2));
		Node<String, Integer> a=new Node<String, Integer>("101", 101);
		t2.put("101", 101);
		assertTrue(s1.contains(a));
		assertTrue(s2.contains(a));
		assertTrue(t1.equals(t2));
		assertTrue(t1.put("0", 42).equals(t2.put("0", 42)));
	}
	
	public class IntegerComparator implements Comparator<Integer>{
		@Override
		public int compare(Integer o1, Integer o2) {
			return o1 > o2 ? 1 : o1 < o2 ? -1 : 0;
		}
	}
	
	@Test
	public void testPutFirstKeyLastKey(){
		AvlGTree<Integer,Integer> a=new AvlGTree<Integer, Integer>(new IntegerComparator(), 2);
		TreeMap<Integer, Integer> t=new TreeMap<Integer, Integer>();
		assertEquals(a, t);
		assertEquals(t, a);
		assertEquals(a.entrySet(), t.entrySet());
		assertEquals(t.entrySet(), a.entrySet());
		assertEquals(a.keySet(), t.keySet());
		assertEquals(t.keySet(), a.keySet());
		assertEquals(a.hashCode(), t.hashCode());
		assertEquals(a.entrySet().hashCode(), t.entrySet().hashCode());
		assertEquals(a.keySet().hashCode(), t.keySet().hashCode());
		for(int i=50; i<100; i++){
			a.put(i, i);
			t.put(i, i);
			assertEquals(a, t);
			assertEquals(t, a);
			assertEquals(a.entrySet(), t.entrySet());
			assertEquals(t.entrySet(), a.entrySet());
			assertEquals(a.keySet(), t.keySet());
			assertEquals(t.keySet(), a.keySet());
			assertEquals(a.hashCode(), t.hashCode());
			assertEquals(a.entrySet().hashCode(), t.entrySet().hashCode());
			assertEquals(a.keySet().hashCode(), t.keySet().hashCode());
			assertEquals(a.firstKey(), t.firstKey());
			assertEquals(a.lastKey(), t.lastKey());
		}
		Set<Map.Entry<Integer, Integer>> es=a.entrySet();
		Set<Integer> ks=a.keySet();
		Set<Map.Entry<Integer, Integer>> tes=t.entrySet();
		Set<Integer> tks=t.keySet();
		Iterator<Map.Entry<Integer, Integer>> ei=es.iterator();
		Iterator<Integer> ki=ks.iterator();
		for(int j=0; j<200; j++){
			a.put(j, j);
			t.put(j, j);
			assertEquals(a, t);
			assertEquals(t, a);

			assertEquals(es, tes);
			assertEquals(tes, es);
			assertEquals(es.hashCode(), tes.hashCode());
			assertEquals(ks, tks);
			assertEquals(tks,ks);
			assertEquals(ks.hashCode(), tks.hashCode());
			assertEquals(a.firstKey(), t.firstKey());
			assertEquals(a.lastKey(), t.lastKey());
			assertTrue(es.contains(new Node(j, j)));
			assertTrue(ks.contains(j));
		}
		a.clear();
		t.clear();
		for(int i=0; i<200; i++){
			assertFalse(a.containsKey(i));
			assertFalse(a.containsValue(i));
			assertFalse(es.contains(new Node(i, i)));
			assertFalse(ks.contains(i));
			
		}
		assertEquals(a, t);
		assertEquals(t, a);
		assertEquals(a.entrySet(), t.entrySet());
		assertEquals(t.entrySet(), a.entrySet());
		assertEquals(a.keySet(), t.keySet());
		assertEquals(t.keySet(), a.keySet());
		assertEquals(a.hashCode(), t.hashCode());
		assertEquals(a.entrySet().hashCode(), t.entrySet().hashCode());
		assertEquals(a.keySet().hashCode(), t.keySet().hashCode());
		assertEquals(es, tes);
		assertEquals(tes, es);
		assertEquals(es.hashCode(), tes.hashCode());
		assertEquals(ks, tks);
		assertEquals(tks,ks);
		assertEquals(ks.hashCode(), tks.hashCode());
		for(int i=0; i<100; i++){
			t.put(i, i);
			assertFalse(t.equals(a));
			assertFalse(a.containsKey(i));
			assertFalse(a.containsValue(i));
		}
		for(int i=0; i<100; i++){
			assertEquals(a.size(), i);
			a.put(i, i);
			assertTrue(a.lastKey().equals( i));
			assertTrue(es.contains(new Node(i, i)));
			assertTrue(ks.contains(i));
		}		
	
		assertEquals(es, tes);
		assertEquals(tes, es);
		assertEquals(es.hashCode(), tes.hashCode());
		assertEquals(ks, tks);
		assertEquals(tks,ks);
		assertEquals(ks.hashCode(), tks.hashCode());
	}
	
//	@Test
//	public void testSubMapHard(){
//		AvlGTree<Integer,Integer> a=new AvlGTree<Integer, Integer>(new IntegerComparator(), 3);
//		TreeMap<Integer, Integer> t=new TreeMap<Integer, Integer>();
//		for(int i=0; i<100; i++){
//			a.put(i, i);
//			t.put(i, i);
//		}
//		for(int i=1; i<100; i++){
//			assertEquals(a.subMap(0, i), t.subMap(0, i));
//			assertEquals(a.subMap(0, i).hashCode(), t.subMap(0, i).hashCode());
//			assertEquals(a.subMap(0, i).entrySet(), t.subMap(0, i).entrySet());
//			assertEquals(a.subMap(0, i).keySet(), t.subMap(0, i).keySet());
//			assertEquals(a.subMap(0, i).entrySet().hashCode(), t.subMap(0, i).entrySet().hashCode());
//			assertEquals(a.subMap(0, i).keySet().hashCode(), t.subMap(0, i).keySet().hashCode());
//			assertTrue(a.subMap(0, i).containsKey(i-1));
//			Iterator<Entry<Integer, Integer>> ses=t.subMap(0, i).entrySet().iterator();
//			Entry curr=ses.next();
//			while(ses.hasNext()){
//				assertTrue(a.subMap(0, i).containsKey(curr.getKey()));
//				assertTrue(a.subMap(0, i).containsValue(curr.getValue()));
//				curr=ses.next();
//			}
//			assertTrue(a.subMap(0, i).containsValue(0));
//			assertFalse(a.subMap(0, i).containsKey(i));
//			assertFalse(a.subMap(0, i).containsValue(i));
//			assertEquals(a.headMap(i), t.headMap(i));
//			assertEquals(a.headMap(i).hashCode(), t.headMap(i).hashCode());
//			assertEquals(a.headMap(i).entrySet(), t.headMap(i).entrySet());
//			assertEquals(a.headMap(i).entrySet().hashCode(), t.headMap(i).entrySet().hashCode());
//			assertEquals(a.headMap(i).keySet(), t.headMap(i).keySet());
//			assertEquals(a.headMap(i).keySet().hashCode(), t.headMap(i).keySet().hashCode());
//			assertFalse(a.headMap(i).containsKey(i));
//			assertTrue(a.headMap(i).containsKey(i-1));
//
//			 ses=t.headMap(i).entrySet().iterator();
//			 curr=ses.next();
//			while(ses.hasNext()){
//				assertTrue(a.headMap(i).containsKey(curr.getKey()));
//				assertTrue(a.headMap(i).containsValue(curr.getValue()));
//				curr=ses.next();
//			}
//			twoMapsEqualityTest(a.tailMap(i), t.tailMap(i));
//			assertTrue(a.tailMap(i).containsKey(i));
//
//			 ses=t.tailMap(i).entrySet().iterator();
//			 curr=ses.next();
//			while(ses.hasNext()){
//				assertTrue(a.tailMap(i).containsKey(curr.getKey()));
//				assertTrue(a.tailMap(i).containsValue(curr.getValue()));
//				curr=ses.next();
//			}
//			
//			
//		}
//		a.clear();
//		t.clear();
//		for(int i=0; i<1000; i++){
//			a.put(i, i);
//			t.put(i, i);
//		}
//		for(int i=0; i<990; i++){
//			twoMapsEqualityTest(a.subMap(i, i+10), t.subMap(i, i+10));
//			assertTrue(a.subMap(i, i+10).containsKey(i+9));
//			Iterator<Entry<Integer, Integer>> ses=t.subMap(i, i+10).entrySet().iterator();
//			Entry curr=ses.next();
//			while(ses.hasNext()){
//				assertTrue(a.subMap(i, i+10).containsKey(curr.getKey()));
//				assertTrue(a.subMap(i, i+10).containsValue(curr.getValue()));
//				curr=ses.next();
//			}
//			assertTrue(a.subMap(i, i+10).containsValue(i));
//			assertFalse(a.subMap(i, i+10).containsKey(i+10));
//			assertFalse(a.subMap(i, i+10).containsValue(i+10));
//		}
//		
//	}

//	@Test
//	public void testSubMapNotContaining(){
//		AvlGTree<Integer,Integer> a=new AvlGTree<Integer, Integer>(new IntegerComparator(), 3);
//		TreeMap<Integer, Integer> t=new TreeMap<Integer, Integer>();
//		SortedMap sub1=a.subMap(0, 100);
//		SortedMap sub2=t.subMap(0, 100);
//		for(int i=0; i<1; i++){
//			a.put(i, i);
//			t.put(i, i);
//		}
//		assertEquals(sub2, sub1);
//		assertEquals(sub1, sub2);
//		twoMapsEqualityTest(sub1, sub2);
//		sub1=a.headMap(50);
//		sub2=t.headMap(50);
//		twoMapsEqualityTest(sub1, sub2);
//		sub2=t.tailMap(50);
//		sub1=a.tailMap(50);
//		twoMapsEqualityTest(sub1, sub2);
//		assertEquals(a.subMap(0, 100).containsKey(100),t.subMap(0, 100).containsKey(100));
//		assertEquals(a.subMap(0, 100).containsKey(99),t.subMap(0, 100).containsKey(99));
//		assertEquals(a.subMap(0, 100).containsKey(101),t.subMap(0, 100).containsKey(101));
//		assertEquals(a.subMap(0, 10).containsKey(100),t.subMap(0, 10).containsKey(100));
//		assertEquals(a.subMap(0, 10).containsKey(0),t.subMap(0, 10).containsKey(0));
//		assertEquals(a.subMap(0, 0).headMap(0).containsKey(0), t.subMap(0, 0).headMap(0).containsKey(0));
//		assertEquals(t.subMap(0, 0).containsKey(-1), a.subMap(0, 0).containsKey(-1));
//		assertEquals(t.subMap(0, 101), a.subMap(0, 101));
//		assertEquals(t.subMap(0, 1).tailMap(0), a.subMap(0, 1).tailMap(0));
//		
//	}
	
	public void twoMapsEqualityTest(SortedMap a, SortedMap b){
		assertEquals(a, b);
		assertEquals(b, a);
		assertEquals(a.size(), b.size());

		assertEquals(a.comparator(), b.comparator());
		assertEquals(b.hashCode(), a.hashCode());
		assertEquals(a.entrySet(), b.entrySet());
		assertEquals(b.entrySet(), a.entrySet());
		assertEquals(a.entrySet().hashCode(), b.entrySet().hashCode());
		assertEquals(a.entrySet().size(), b.entrySet().size());
		assertEquals(a.keySet(), b.keySet());
		assertEquals(b.keySet(), a.keySet());
		assertEquals(a.keySet().size(), b.keySet().size());
		assertEquals(a.keySet().hashCode(), b.keySet().hashCode());
		if(a.size()>0){
		assertEquals(a.firstKey(), b.firstKey());
		assertEquals(a.lastKey(), b.lastKey());
		}
		Iterator<Entry> es=a.entrySet().iterator();
		Entry curr=null;
		while(es.hasNext()){
			curr= es.next();
			assertTrue(b.containsKey(curr.getKey()));
			assertTrue(b.containsValue(curr.getValue()));
		}
		es=b.entrySet().iterator();
		while(es.hasNext()){
			curr= es.next();
			assertTrue(a.containsKey(curr.getKey()));
			assertTrue(a.containsValue(curr.getValue()));
		}
		Integer curr2=null;
		Iterator<Integer> es2=a.keySet().iterator();
		while(es2.hasNext()){
			curr2= es2.next();
			assertTrue(b.containsKey(curr2));
			assertTrue(b.containsValue(a.get(curr2)));
		}
		es2=b.keySet().iterator();
		while(es2.hasNext()){
			curr2= es2.next();
			assertTrue(a.containsKey(curr2));
			assertTrue(a.containsValue(b.get(curr2)));
		}
		
		
	}
	@Test
	public void testClear() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER, 2);

		assertEquals(avl.size(), 0);
		
		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.clear();
		assertEquals(avl.size(), 0);

		avl.clear();
	}
//	
	@Test(expected=NoSuchElementException.class)
	public void testFirstKeyException() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		avl.firstKey();
	}
//	
	@Test
	public void testFirstKey() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		TreeMap<String, City> tm = new TreeMap<String, City>();

		avl.put("D", new City("D", 0, 0, 10, "black"));
		tm.put("D", new City("D", 0, 0, 10, "black"));
		assertEquals(avl.firstKey(), tm.firstKey());
		
		avl.put("B", new City("B", 20, 20, 10, "black"));
		tm.put("B", new City("B", 20, 20, 10, "black"));
		assertEquals(avl.firstKey(), tm.firstKey());

		avl.put("C", new City("C", 40, 40, 10, "black"));
		tm.put("C", new City("C", 40, 40, 10, "black"));
		assertEquals(avl.firstKey(), tm.firstKey());

		avl.put("A", new City("A", 60, 60, 10, "black"));
		tm.put("A", new City("A", 60, 60, 10, "black"));
		assertEquals(avl.firstKey(), tm.firstKey());

		avl.put("E", new City("E", 80, 80, 10, "black"));
		tm.put("E", new City("E", 80, 80, 10, "black"));
		assertEquals(avl.firstKey(), tm.firstKey());
		AvlGTree<Integer, Integer> avl2 = new AvlGTree<Integer, Integer>(new IntegerComparator(),1);
		avl.clear();
		for(int i=1000; i>0; i--){
			avl2.put(i, i);
		}
		Iterator i=avl2.entrySet().iterator();
		Map.Entry<Integer, Integer> curr=null;
		while(i.hasNext()){
			curr=(Entry<Integer, Integer>) i.next();
			assertTrue(avl2.containsKey(curr.getKey()));
			assertTrue(avl2.containsValue(curr.getValue()));
		}
	}

	@Test(expected=NoSuchElementException.class)
	public void testLastKeyException() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		avl.lastKey();
	}
//	
	@Test
	public void testLastKey() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);

		avl.put("D", new City("D", 0, 0, 10, "black"));
		assertEquals(avl.lastKey(), "D");
		
		avl.put("B", new City("B", 20, 20, 10, "black"));
		assertEquals(avl.lastKey(), "D");

		avl.put("C", new City("C", 40, 40, 10, "black"));
		assertEquals(avl.lastKey(), "D");

		avl.put("A", new City("A", 60, 60, 10, "black"));
		assertEquals(avl.lastKey(), "D");

		avl.put("E", new City("E", 80, 80, 10, "black"));
		assertEquals(avl.lastKey(), "E");
	}
//	
//
	@Test(expected=NullPointerException.class)
	public void testContainsKeyNull() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);

		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.containsKey(null);
	}
//	
	@Test
	public void testContainsKey() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER, 2);

		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.put("B", new City("B", 20, 20, 10, "black"));
		avl.put("C", new City("C", 40, 40, 10, "black"));
		avl.put("D", new City("D", 0, 0, 10, "black"));
		avl.put("E", new City("E", 80, 80, 10, "black"));
		
		assertTrue(avl.containsKey("A"));
		assertTrue(avl.containsKey("B"));
		assertTrue(avl.containsKey("C"));
		assertTrue(avl.containsKey("D"));
		assertTrue(avl.containsKey("E"));
	}
//
//	@Test(expected=NullPointerException.class)
//	public void testContainsValueNull() {
//		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
//
//		avl.put("A", new City("A", 60, 60, 10, "black"));
//		avl.containsValue(null);
//	}
//	
	@Test
	public void testContainsValue() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);

		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.put("B", new City("B", 20, 20, 10, "black"));
		avl.put("C", new City("C", 40, 40, 10, "black"));
		avl.put("D", new City("D", 0, 0, 10, "black"));
		avl.put("E", new City("E", 80, 80, 10, "black"));
		
		assertTrue(avl.containsValue(new City("A", 60, 60, 10, "black")));
		assertTrue(avl.containsValue(new City("B", 20, 20, 10, "black")));
		assertTrue(avl.containsValue(new City("C", 40, 40, 10, "black")));
		assertTrue(avl.containsValue(new City("D", 0, 0, 10, "black")));
		assertTrue(avl.containsValue(new City("E", 80, 80, 10, "black")));
	}

	@Test(expected=NullPointerException.class)
	public void testGetNull() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);

		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.get(null);
	}
//	
	@Test
	public void testGet() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);

		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.put("B", new City("B", 20, 20, 10, "black"));
		avl.put("C", new City("C", 40, 40, 10, "black"));
		avl.put("D", new City("D", 0, 0, 10, "black"));
		avl.put("E", new City("E", 80, 80, 10, "black"));
		
		assertEquals(avl.get("A"), new City("A", 60, 60, 10, "black"));
		assertEquals(avl.get("B"), new City("B", 20, 20, 10, "black"));
		assertEquals(avl.get("C"), new City("C", 40, 40, 10, "black"));
		assertEquals(avl.get("D"), new City("D", 0, 0, 10, "black"));
		assertEquals(avl.get("E"), new City("E", 80, 80, 10, "black"));
	}
//	
	@Test(expected=NullPointerException.class)
	public void testPutNullKey() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		avl.put(null, new City("A", 60, 60, 10, "black"));
	}
//	
	@Test(expected=NullPointerException.class)
	public void testPutNullValue() { 
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		avl.put("A", null);
	}
//	
//	// we will assume that put works correctly.
//	// possibly add a height test later, though.
//	
//	// ClassCastException is covered in the City class; 
//	// no need for test
//	
	@Test
	public void testPutAll() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER, 2);
		AvlGTree<String, City> tmp = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER, 2);

		tmp.put("A", new City("A", 60, 60, 10, "black"));
		tmp.put("B", new City("B", 20, 20, 10, "black"));
		tmp.put("C", new City("C", 40, 40, 10, "black"));
		tmp.put("D", new City("D", 0, 0, 10, "black"));
		tmp.put("E", new City("E", 80, 80, 10, "black"));
		
		avl.clear();
		avl.putAll(tmp);
	
		assertEquals(avl.get("A"), new City("A", 60, 60, 10, "black"));
		assertEquals(avl.get("B"), new City("B", 20, 20, 10, "black"));
		assertEquals(avl.get("C"), new City("C", 40, 40, 10, "black"));
		assertEquals(avl.get("D"), new City("D", 0, 0, 10, "black"));
		assertEquals(avl.get("E"), new City("E", 80, 80, 10, "black"));
	}
//	
	@Test
	public void testKeySet2() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);

		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.put("B", new City("B", 20, 20, 10, "black"));
		avl.put("C", new City("C", 40, 40, 10, "black"));
		avl.put("D", new City("D", 0, 0, 10, "black"));
		avl.put("E", new City("E", 80, 80, 10, "black"));
		
		Set<String> keySet = avl.keySet();

		assertTrue(keySet.contains("A"));
		assertTrue(keySet.contains("B"));
		assertTrue(keySet.contains("C"));
		assertTrue(keySet.contains("D"));
		assertTrue(keySet.contains("E"));
		
		avl.put("F", new City("F", 100, 100, 10, "black"));

		assertTrue(keySet.contains("F"));
	}
//	
	@Test
	public void testValues2() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		TreeMap<String, City> tm=new TreeMap<String, City>();
		

		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.put("B", new City("B", 20, 20, 10, "black"));
		avl.put("C", new City("C", 40, 40, 10, "black"));
		avl.put("D", new City("D", 0, 0, 10, "black"));
		avl.put("E", new City("E", 80, 80, 10, "black"));
		tm.putAll(avl);
		Collection<City> values = avl.values();
		assertTrue(values.contains(new City("A", 60, 60, 10, "black")));
		assertTrue(values.contains(new City("B", 20, 20, 10, "black")));
		assertTrue(values.contains(new City("C", 40, 40, 10, "black")));
		assertTrue(values.contains(new City("D", 0, 0, 10, "black")));
		assertTrue(values.contains(new City("E", 80, 80, 10, "black")));
		
		avl.put("F", new City("F", 100, 100, 10, "black"));

		assertTrue(values.contains(new City("F", 100, 100, 10, "black")));
	}
//	
	@Test
	public void testEntrySet2() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		TreeMap<String, City> tm = new TreeMap<String, City>();
		assertEquals(tm.entrySet(), avl.entrySet());
		assertEquals(avl.entrySet(), tm.entrySet());
		assertEquals(tm.entrySet().hashCode(), avl.entrySet().hashCode());

		avl.put("A", new City("A", 60, 60, 10, "black"));
		avl.put("B", new City("B", 20, 20, 10, "black"));
		avl.put("C", new City("C", 40, 40, 10, "black"));
		avl.put("D", new City("D", 0, 0, 10, "black"));
		avl.put("E", new City("E", 80, 80, 10, "black"));
		
		Set<Map.Entry<String, City>> entrySet = avl.entrySet();
		
		assertTrue(entrySet.contains(new Node<String, City>("A", new City("A", 60, 60, 10, "black"))));
		assertTrue(entrySet.contains(new Node<String, City>("B", new City("B", 20, 20, 10, "black"))));
		assertTrue(entrySet.contains(new Node<String, City>("C", new City("C", 40, 40, 10, "black"))));
		assertTrue(entrySet.contains(new Node<String, City>("D", new City("D", 0, 0, 10, "black"))));
		assertTrue(entrySet.contains(new Node<String, City>("E", new City("E", 80, 80, 10, "black"))));
		
		avl.put("F", new City("F", 100, 100, 10, "black"));
		
		entrySet.contains(new Node<String, City>("F", new City("F", 100, 100, 10, "black")));
		
		tm.putAll(avl);
		
		assertEquals(tm, avl);
		assertEquals(tm, avl);
		assertEquals(tm.entrySet(), avl.entrySet());
		assertEquals(avl.entrySet(), tm.entrySet());
		assertEquals(tm.entrySet().hashCode(), avl.entrySet().hashCode());
	}
	
	@Test
	public void testEquals() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		TreeMap<String, City> map = new TreeMap<String, City>();
		
		map.put("A", new City("A", 60, 60, 10, "black"));
		map.put("B", new City("B", 20, 20, 10, "black"));
		map.put("C", new City("C", 40, 40, 10, "black"));
		map.put("D", new City("D", 0, 0, 10, "black"));
		map.put("E", new City("E", 80, 80, 10, "black"));
		
		avl.putAll(map);
		
		assertTrue(map.equals(avl));
		assertTrue(avl.equals(map));
	}
	
	@Test
	public void testHashCode() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		TreeMap<String, City> tm = new TreeMap<String, City>();

		tm.put("A", new City("A", 60, 60, 10, "black"));
		tm.put("B", new City("B", 20, 20, 10, "black"));
		tm.put("C", new City("C", 40, 40, 10, "black"));
		tm.put("D", new City("D", 0, 0, 10, "black"));
		tm.put("E", new City("E", 80, 80, 10, "black"));
		
		avl.putAll(tm);
		
		assertEquals(tm.hashCode(), avl.hashCode());
	}
//	
	@Test
	public void testSubMap() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		TreeMap<String, City> tm = new TreeMap<String, City>();

		avl.put("Atlanta", new City("Atlanta", 20, 20, 10, "Black"));
		avl.put("Baltimore", new City("Baltimore", 60, 60, 10, "Black"));
		avl.put("Boston", new City("Boston", 40, 40, 10, "Black"));
		avl.put("Chicago", new City("Chicago", 0, 0, 10, "Black"));
		avl.put("Los_A", new City("LA", 80, 80, 10, "Black"));
		avl.put("Miami", new City("Miami", 40, 40, 10, "Black"));
	
		tm.putAll(avl);
		
		SortedMap<String, City> a = tm.subMap("Atlanta", "Los_A");
		SortedMap<String, City> b = avl.subMap("A", "Los_A");
		assertEquals(a.firstKey(), b.firstKey());
		assertEquals(a.size(), b.size());

//		assertEquals(a.comparator(), b.comparator());
		assertTrue(a.entrySet().equals(b.entrySet()));
		assertEquals(a.entrySet(), b.entrySet());
		assertEquals(a.keySet(), b.keySet());
		assertEquals(a.toString(), b.toString());
		assertEquals(a.equals(b), b.equals(a));
		assertEquals(a.lastKey(), b.lastKey());
		assertEquals(a.hashCode(), b.hashCode());
	}
//		
	@Test
	public void testHeadMap() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		TreeMap<String, City> tm = new TreeMap<String, City>();

		avl.put("Atlanta", new City("Atlanta", 20, 20, 10, "Black"));
		avl.put("Baltimore", new City("Baltimore", 60, 60, 10, "Black"));
		avl.put("Boston", new City("Boston", 40, 40, 10, "Black"));
		avl.put("Chicago", new City("Chicago", 0, 0, 10, "Black"));
		avl.put("Los_A", new City("LA", 80, 80, 10, "Black"));
		avl.put("Miami", new City("Miami", 40, 40, 10, "Black"));
	
		tm.putAll(avl);
		
		SortedMap<String, City> a = tm.headMap("C");
		SortedMap<String, City> b = avl.headMap("C");
		assertEquals(a.firstKey(), b.firstKey());
		assertEquals(a.lastKey(), b.lastKey());
		assertEquals(a.size(), b.size());
//		assertEquals(a.comparator(), b.comparator());
		assertEquals(a.entrySet(), b.entrySet());
		assertEquals(a.keySet(), b.keySet());
//		assertEquals(a.toString(), b.toString());
		assertEquals(a.equals(b), b.equals(a));
		assertEquals(a.hashCode(), b.hashCode());
		
		avl.clear();
		tm.clear();
	}
	
	@Test
	public void testTailMap() {
		AvlGTree<String, City> avl = new AvlGTree<String, City>(String.CASE_INSENSITIVE_ORDER,2);
		TreeMap<String, City> tm = new TreeMap<String, City>();

		avl.put("Atlanta", new City("Atlanta", 20, 20, 10, "Black"));
		avl.put("Baltimore", new City("Baltimore", 60, 60, 10, "Black"));
		avl.put("Boston", new City("Boston", 40, 40, 10, "Black"));
		avl.put("Chicago", new City("Chicago", 0, 0, 10, "Black"));
		avl.put("Los_A", new City("LA", 80, 80, 10, "Black"));
		avl.put("Miami", new City("Miami", 40, 40, 10, "Black"));
	
		tm.putAll(avl);
		
		SortedMap<String, City> a = tm.tailMap("C");
		SortedMap<String, City> b = avl.tailMap("C");
		assertEquals(a.firstKey(), b.firstKey());
		assertEquals(a.lastKey(), b.lastKey());
		assertEquals(a.size(), b.size());
		assertEquals(a.comparator(), b.comparator());
		assertEquals(a.entrySet(), b.entrySet());
		assertEquals(a.keySet(), b.keySet());
		assertEquals(a.toString(), b.toString());
		assertEquals(a.equals(b), b.equals(a));
		assertEquals(a.hashCode(), b.hashCode());

		avl.clear();
		tm.clear();
	}

	@Test
	public void testSimpleDataSubMap() {
		AvlGTree<Integer, Integer> a = new AvlGTree<Integer, Integer>();
		TreeMap<Integer, Integer> b = new TreeMap<Integer, Integer>();
		
		for (int i = 1; i < 100; i++) {
			a.put(i, i);
			b.put(i, i);
		}

		assertEquals(b.firstKey(), a.firstKey());
		assertEquals(b.lastKey(), a.lastKey());
		assertEquals(b.size(), a.size());
		assertEquals(b.comparator(), a.comparator());
		assertEquals(b.entrySet(), a.entrySet());
		assertEquals(b.keySet(), a.keySet());
		assertEquals(b.toString(), a.toString());
		assertEquals(b.equals(a), a.equals(b));
		assertEquals(b.hashCode(), a.hashCode());
		
		SortedMap<Integer, Integer> subMapA = a.subMap(40, 99);
		SortedMap<Integer, Integer> subMapB = b.subMap(40, 99);
		
		//assertEquals(subMapB.firstKey(), subMapA.firstKey());
		//assertEquals(subMapB.lastKey(), subMapA.lastKey());
		assertEquals(subMapB.size(), subMapA.size());
		assertEquals(subMapB.comparator(), subMapA.comparator());
		assertEquals(subMapB.entrySet(), subMapA.entrySet());
		assertEquals(subMapB.keySet(), subMapA.keySet());
		assertEquals(subMapB.toString(), subMapA.toString());
		assertEquals(subMapB.equals(subMapA), subMapA.equals(subMapB));
		assertEquals(subMapB.hashCode(), subMapA.hashCode());
		
		Set<Entry<Integer, Integer>> setA = subMapA.entrySet();
		Set<Entry<Integer, Integer>> setB = subMapB.entrySet();
		
		assertEquals(setB.containsAll(setA), setA.containsAll(setB));
		assertEquals(setB.equals(setA), setA.equals(setB));
		assertEquals(setB.hashCode(), setA.hashCode());
		assertEquals(setB.size(), setA.size());
		
		Set<Integer> ksetA = subMapA.keySet();
		Set<Integer> ksetB = subMapB.keySet();

		assertEquals(ksetB.containsAll(ksetA), ksetA.containsAll(ksetB));
		assertEquals(ksetB.equals(ksetA), ksetA.equals(ksetB));
		assertEquals(ksetB.hashCode(), ksetA.hashCode());
		assertEquals(ksetB.size(), ksetA.size());
		
		Collection<Integer> vsetA = subMapA.values();
		Collection<Integer> vsetB = subMapB.values();

		assertEquals(vsetB.containsAll(vsetA), vsetA.containsAll(ksetB));
		assertEquals(vsetB.size(), vsetA.size());
	}
	
	@Test
	public void testSimpleDataHeadMap() {
		AvlGTree<Integer, Integer> a = new AvlGTree<Integer, Integer>();
		TreeMap<Integer, Integer> b = new TreeMap<Integer, Integer>();
		
		for (int i = 99; i >= 0; i--) {
			a.put(i, i);
			b.put(i, i);
		}

		assertEquals(b.firstKey(), a.firstKey());
		assertEquals(b.lastKey(), a.lastKey());
		assertEquals(b.size(), a.size());
		assertEquals(b.comparator(), a.comparator());
		assertEquals(b.entrySet(), a.entrySet());
		assertEquals(b.keySet(), a.keySet());
		assertEquals(b.toString(), a.toString());
		assertEquals(b.equals(a), a.equals(b));
		assertEquals(b.hashCode(), a.hashCode());
		
		SortedMap<Integer, Integer> subMapB = b.headMap(20).subMap(-1, 20);
		//System.out.println(subMapB.entrySet());
		SortedMap<Integer, Integer> subMapA = a.headMap(20).subMap(-1, 20);
		//System.out.println(subMapA.entrySet());
		
		//assertEquals(subMapB.firstKey(), subMapA.firstKey());
		//assertEquals(subMapB.lastKey(), subMapA.lastKey());
		assertEquals(subMapB.size(), subMapA.size());
		assertEquals(subMapB.comparator(), subMapA.comparator());
		assertEquals(subMapB.entrySet(), subMapA.entrySet());
		assertEquals(subMapB.keySet(), subMapA.keySet());
		assertEquals(subMapB.toString(), subMapA.toString());
		assertEquals(subMapB.equals(subMapA), subMapA.equals(subMapB));
		assertEquals(subMapB.hashCode(), subMapA.hashCode());
		
		Set<Entry<Integer, Integer>> setA = subMapA.entrySet();
		Set<Entry<Integer, Integer>> setB = subMapB.entrySet();
		
		assertEquals(setB.containsAll(setA), setA.containsAll(setB));
		assertEquals(setB.equals(setA), setA.equals(setB));
		assertEquals(setB.hashCode(), setA.hashCode());
		assertEquals(setB.size(), setA.size());
		
		Set<Integer> ksetA = subMapA.keySet();
		Set<Integer> ksetB = subMapB.keySet();

		assertEquals(ksetB.containsAll(ksetA), ksetA.containsAll(ksetB));
		assertEquals(ksetB.equals(ksetA), ksetA.equals(ksetB));
		assertEquals(ksetB.hashCode(), ksetA.hashCode());
		assertEquals(ksetB.size(), ksetA.size());
		
		Collection<Integer> vsetA = subMapA.values();
		Collection<Integer> vsetB = subMapB.values();
/*
		System.out.println("headMap:");
		System.out.println(vsetA);
		System.out.println(vsetB);
		System.out.println();
*/	
		assertEquals(vsetB.containsAll(vsetA), vsetA.containsAll(ksetB));
		assertEquals(vsetB.size(), vsetA.size());
	}
	
	@Test
	public void testSimpleDataTailMap() {
		AvlGTree<Integer, Integer> a = new AvlGTree<Integer, Integer>(new IntegerComparator(),4);
		TreeMap<Integer, Integer> b = new TreeMap<Integer, Integer>();
		
		for (int i = 50; i >= 0; i--) {
			a.put(i, i);
			b.put(i, i);
		}

		for (int i = 51; i < 100; i++) {
			a.put(i, i);
			b.put(i, i);
		}
		for(int i=0; i<100;i++){
			for(int j=i; j<100; j++){
				assertEquals(b.subMap(i, j), a.subMap(i, j));
				if(i-j>0){
				assertEquals(b.subMap(i, j).tailMap(i), a.subMap(i, j).tailMap(i));
				assertEquals(b.subMap(i, j).tailMap(j), a.subMap(i, j).tailMap(j));
				assertEquals(b.subMap(i, j).headMap(i), a.subMap(i, j).headMap(i));
				assertEquals(b.subMap(i, j).headMap(j), a.subMap(i, j).headMap(j));
				}
			}
		}
		for(int i=0; i<100; i++){
			assertEquals(b.headMap(i), a.headMap(i));
			assertEquals(a.headMap(i), b.headMap(i));
			assertEquals(b.tailMap(i), a.tailMap(i));
			assertEquals(a.tailMap(i), b.tailMap(i));
		}
		
		assertEquals(b.firstKey(), a.firstKey());
		assertEquals(b.lastKey(), a.lastKey());
		assertEquals(b.size(), a.size());
		assertEquals(b.comparator(), a.comparator());
		assertEquals(b.entrySet(), a.entrySet());
		assertEquals(b.keySet(), a.keySet());
		assertEquals(b.toString(), a.toString());
		assertEquals(b.equals(a), a.equals(b));
		assertEquals(b.hashCode(), a.hashCode());
		
		SortedMap<Integer, Integer> subMapB = b.tailMap(99).tailMap(99);
		SortedMap<Integer, Integer> subMapA = a.tailMap(99).tailMap(99);
		
		//assertEquals(subMapB.firstKey(), subMapA.firstKey());
		assertEquals(subMapB.lastKey(), subMapA.lastKey());
		assertEquals(subMapB.size(), subMapA.size());
		assertEquals(subMapB.comparator(), subMapA.comparator());
		assertEquals(subMapB.entrySet(), subMapA.entrySet());
		assertEquals(subMapB.keySet(), subMapA.keySet());
		assertEquals(subMapB.toString(), subMapA.toString());
		assertEquals(subMapB.equals(subMapA), subMapA.equals(subMapB));
		assertEquals(subMapB.hashCode(), subMapA.hashCode());
		
		Set<Entry<Integer, Integer>> setA = subMapA.entrySet();
		Set<Entry<Integer, Integer>> setB = subMapB.entrySet();
		
		assertEquals(setB.containsAll(setA), setA.containsAll(setB));
		assertEquals(setB.equals(setA), setA.equals(setB));
		assertEquals(setB.hashCode(), setA.hashCode());
		assertEquals(setB.size(), setA.size());
		
		Set<Integer> ksetA = subMapA.keySet();
		Set<Integer> ksetB = subMapB.keySet();

		assertEquals(ksetB.containsAll(ksetA), ksetA.containsAll(ksetB));
		assertEquals(ksetB.equals(ksetA), ksetA.equals(ksetB));
		assertEquals(ksetB.hashCode(), ksetA.hashCode());
		assertEquals(ksetB.size(), ksetA.size());
		
		Collection<Integer> vsetA = subMapA.values();
		Collection<Integer> vsetB = subMapB.values();

		/*
		System.out.println("tailMap:");
		System.out.println(vsetA);
		System.out.println(vsetB);
		System.out.println();
		*/
		assertEquals(vsetB.containsAll(vsetA), vsetA.containsAll(ksetB));
		assertEquals(vsetB.size(), vsetA.size());
	}
	
	@Test
	public void testAVLGCityComparator() {
		CityLocationComparator clc =  new CityLocationComparator();
		AvlGTree<City, City> avl = new AvlGTree<City, City>(clc, 1);
		TreeMap<City, City> tm = new TreeMap<City, City>(clc);
		TreeSet<City> ts = new TreeSet<City>(clc);

		City atl = new City("Atlanta", 20, 20, 10, "Black");
		City balt = new City("Baltimore", 60, 60, 10, "Black");
		City bos = new City("Boston", 40, 40, 10, "Black");
		City chi = new City("Chicago", 0, 0, 10, "Black");
		City la = new City("LA", 80, 80, 10, "Black");
		City mia = new City("Miami", 40, 40, 10, "Black");
		
		avl.put(atl, atl);
		avl.put(balt, balt);
		avl.put(bos, bos);
		avl.put(chi, chi);
		avl.put(la, la);
		avl.put(mia, mia);
		
		tm.putAll(avl);
		
		ts.add(atl);
		ts.add(balt);
		ts.add(bos);
		ts.add(chi);
		ts.add(la);
		ts.add(mia);
		
		City tmp1 = new City ("tmp1", 0, 100, 20, "blue");
		City tmp2 = new City ("tmp2", 60, 50, 10, "white");
		
		assertEquals(ts.first(), avl.firstKey());
		assertEquals(ts.last(), avl.lastKey());
		assertEquals(ts.size(), avl.size());
		assertEquals(ts.comparator(), avl.comparator());
		assertEquals(tm.toString(), avl.toString());
		assertEquals(tm.equals(avl), avl.equals(tm));
		assertEquals(tm.hashCode(), avl.hashCode());
		
		SortedMap<City, City> subMapA = avl.tailMap(tmp2);
		SortedMap<City, City> subMapB = tm.tailMap(tmp2);
		
		assertEquals(subMapB.firstKey(), subMapA.firstKey());
		assertEquals(subMapB.lastKey(), subMapA.lastKey());
		assertEquals(subMapB.size(), subMapA.size());
		assertEquals(subMapB.comparator(), subMapA.comparator());
		assertEquals(subMapB.entrySet(), subMapA.entrySet());
		assertEquals(subMapB.keySet(), subMapA.keySet());
		assertEquals(subMapB.toString(), subMapA.toString());
		assertEquals(subMapB.equals(subMapA), subMapA.equals(subMapB));
		assertEquals(subMapB.hashCode(), subMapA.hashCode());
		
		Set<Entry<City, City>> setA = subMapA.entrySet();
		Set<Entry<City, City>> setB = subMapB.entrySet();
		
		assertEquals(setB.containsAll(setA), setA.containsAll(setB));
		assertEquals(setB.equals(setA), setA.equals(setB));
		assertEquals(setB.hashCode(), setA.hashCode());
		assertEquals(setB.size(), setA.size());
		
		Set<City> ksetA = subMapA.keySet();
		Set<City> ksetB = subMapB.keySet();

		assertEquals(ksetB.containsAll(ksetA), ksetA.containsAll(ksetB));
		assertEquals(ksetB.equals(ksetA), ksetA.equals(ksetB));
		assertEquals(ksetB.hashCode(), ksetA.hashCode());
		assertEquals(ksetB.size(), ksetA.size());
		
		Collection<City> vsetA = subMapA.values();
		Collection<City> vsetB = subMapB.values();

		assertEquals(vsetB.size(), vsetA.size());
		assertEquals(vsetB.containsAll(vsetA), vsetA.containsAll(ksetB));
	}
	
}
