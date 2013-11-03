package cmsc420.sortedmap;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class AvlGTree <K, V> implements SortedMap<K, V> {
	private Node<K,V> root;
	private Comparator<? super K> comparator;
	private int g;
	private transient int modCount;
	private V previousValue;
	private Collection<V> values;
	private Set<java.util.Map.Entry<K, V>> entries;
	private Set<K> keySet;
	
	// Default constructor
	public AvlGTree(){
		this.root = null;
		this.comparator = null;
		this.previousValue = null;
	}
	
	// If this generic wildcard confuses you, reread the end of the
	// part 1 spec for the introduction to generics.
	public AvlGTree(final Comparator<? super K> comparator, final int g){
		this.root = null; 
		this.comparator = comparator;
		this.g = g;
	}
	
	@Override
	public void clear() {
		root = null;
	}

	@Override
	public boolean containsKey(Object key) {
		if (key == null)
			throw new NullPointerException();
		return containsKey((K)key, root);
	}

	@Override
	public boolean containsValue(Object value) {
		if (value == null)
			throw new NullPointerException();
		return containsValue((V)value, root);
	}
	
	@Override
	public V get(Object key) {
		if(key == null)
			throw new NullPointerException();
		if(root == null)
			return null;
		return get(root, (K) key);
	}
	
	@Override
	public boolean isEmpty() {
		return root == null ? true : false;
	}

	@Override
	public V put(K key, V value) {
		if(root == null)
			root = insert(key, value, root, null);
		else
			root = insert(key, value, root, root.parent);
		return previousValue;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		if(m == null)
			throw new NullPointerException();
			
		for(java.util.Map.Entry<? extends K, ? extends V> e: m.entrySet()){
			if(!(e instanceof Node<?,?>))
				throw new ClassCastException();
			this.put(e.getKey(), e.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		if (root == null)
			return 0;
		else
			return size(root);
	}

	@Override
	public Comparator<? super K> comparator() {
		return comparator;
	}

	@Override
	public K firstKey() {
		if (root == null)
			throw new NoSuchElementException();
		else
			return root.getFirstEntry().getKey();
	}

	@Override
	public K lastKey() {
		if (root == null)
			throw new NoSuchElementException();
		else
			return root.getLastEntry().getKey();
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		return headMap(toKey, false);
	}

	private NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
		return null;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		return subMap(fromKey, true, toKey, false);
	}

	private NavigableMap<K, V> subMap(K fromKey, boolean b, K toKey, boolean c) {
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		return tailMap(fromKey, true);
	}

	private NavigableMap<K, V> tailMap(K fromKey, boolean b) {
		return null;
		//Change
	}

	@Override
	public String toString(){
		return toString(root, 1);
	}

	@Override
	public boolean equals(Object o){
		if(o == this)
			return true;
		
		if(!(o instanceof AvlGTree))
			return false;
		
		Map<K,V> map = (Map<K,V>) o;
		
		if(map.size() != this.size())
			return false;
		
		try {
			Iterator<Entry<K, V>> iter = this.entrySet().iterator();
			while(iter.hasNext()){
				Entry<K,V> e = iter.next();
				K key = e.getKey();
				V value = e.getValue();
				if(value == null) {
					if(!(map.get(key) == null) && map.containsKey(key))
						return false;
				} else {
					if (!value.equals(map.get(key)))
						return false;
				}
			}
		} catch (Exception e){
			return false;
		}
		
		return true;
	}

	@Override
	public Collection<V> values() {
		Collection<V> vs = values;
		return (vs != null) ? vs : (values = new Values());
	}

	@Override
	public Set<K> keySet() {
		Set<K> ks = keySet;
		return (ks != null) ? ks : (keySet = new KeySet());
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> es = entries;
		return (es != null) ? es : (entries = new EntrySet());
	}

	private boolean containsKey(K key, Node<K,V> root) {
		if(root == null)
			return false;
		
		if(comparator.compare(key, root.getKey()) < 0){
			return containsKey(key, root.left);
		}
		else if (comparator.compare(key, root.getKey()) > 0){
			return containsKey(key, root.right);
		}
		else
			return true;
	}

	private boolean containsValue(V value, Node<K,V> root) {
		if(root == null)
			return false;
		if(root.getValue().equals(value))
			return true;
		
		return containsValue(value, root.left) || containsValue(value, root.right);
	}

	private V get(Node<K,V> root, K key){
		if(root == null)
			return null;
		if(comparator.compare(key, root.getKey()) < 0){
			return get(root.left, key);
		} 
		else if(comparator.compare(key, root.getKey()) > 0){
			return get(root.right, key);
		} else {
			return root.getValue();
		}
	}

	private Node<K,V> insert(K key, V value, Node<K,V> root, Node<K,V> parent) {
		int balanceFactor;
		if (root == null)
			root = new Node<K, V>(key, value, parent);
		
		// If key is equal to root.getKey()
		else if (comparator.compare(root.getKey(), key) == 0){
			previousValue = root.getValue();
			root.setValue(value);
		}
		else {
			// If key is lesser than root.getKey()
			if (comparator.compare(root.getKey(), key) > 0)
				root.left = insert(key, value, root.left, root);
			// If key is greater than root.getKey()
			else if (comparator.compare(root.getKey(), key) < 0)
				root.right = insert(key, value, root.right, root);
			
			
			balanceFactor = height(root.left) - height(root.right);
			
			if(balanceFactor > g){
				if(comparator.compare(key, (K) root.left.getKey()) < 0) {
					root = rotateRight(root);
				} else {
					root = rotateLeftRight(root);
				}
			}
			else if (balanceFactor < -g){
				if(comparator.compare(key, (K) root.right.getKey()) > 0) {
					root = rotateLeft(root);
				} else {
					root = rotateRightLeft(root);
				}
			}
		}
		root.height = max(height(root.left), height(root.right)) + 1;
		return root;
	}

	private Node<K,V> rotateRightLeft(Node<K,V> root) {
	    root.right = rotateRight( root.right );
        return rotateLeft(root);
	}

	private Node<K,V> rotateRight(Node<K,V> root) {
        Node<K,V> temp = root.left;
        temp.parent = root.parent;
        root.parent = temp;
   
        root.left = temp.right;
        if(root.left != null)
        	root.left.parent = root;
        
        temp.right = root;
        
        root.height = max( height( root.left ), height( root.right ) ) + 1;
        temp.height = max( height( temp.left ), root.height ) + 1;
        
        if(temp.left != null)
        	temp.left.parent = temp;
        
        temp.right.parent = temp;
        return temp;
	}

	private Node<K,V> rotateLeftRight(Node<K,V> root) {
		root.left = rotateLeft( root.left );
	    return rotateRight(root);
	}

	private Node<K,V> rotateLeft(Node<K,V> root) {
		Node<K,V> temp = root.right;
		temp.parent = root.parent;
		root.parent = temp;
		
	    root.right = temp.left;
	    if(root.right != null)
	    	root.right.parent = root;
	    
	    temp.left = root;
	      
	    root.height = max( height( root.left ), height( root.right ) ) + 1;
	    temp.height = max( height( temp.right ), root.height ) + 1;
	    
	    if(temp.right != null)
	    	temp.right.parent = temp;
	    
	    temp.left.parent = temp;
	    return temp;
	}

	private int height(Node<K,V> root){
		return root == null ? -1 : root.height;
	}
	
	private int max(int left, int right){
		return left > right ? left: right;
	}
	
	private int size(Node<K,V> root) {
		if(root == null)
			return 0;
		else
			return 1 + size(root.left) + size(root.right);
	}

	StringBuffer tab = new StringBuffer();
	public String toString(Node<K,V> root, int level){
		if (root == null)
			return "";
		else {
			tab.setLength(0);
			for (int i = 1; i < level; i++)
				tab.append("\t");
	
			return tab.toString() + root.toString() + "\n" + toString(root.left, level + 1) + toString(root.right, level + 1);
		}
	}

	protected class EntrySet extends AbstractSet<Map.Entry<K, V>> {

		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new EntryIterator(root.getFirstEntry());
		}
		
		@Override
		public int size() {
			return AvlGTree.this.size();
		}

//		@Override
//		public void clear() {
//			AvlGTree.this.clear();
//		}
//		
//		@Override
//		public boolean contains(Object o) {
//			if (!(o instanceof Map.Entry))
//				throw new ClassCastException();
//			
//			Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
//		
//			V value = entry.getValue();
//			Node<K,V> p = getEntry(entry.getKey());
//			return p != null && valEquals(p.getValue(), value);
//		}
//
//		@Override
//		public boolean remove(Object o) {
//			throw new UnsupportedOperationException();
//		}
	}
	
	final class EntryIterator extends PrivateEntryIterator<Map.Entry<K,V>>{
		
		EntryIterator(Node<K, V> first) {
			super(first);
		}

		@Override
		public java.util.Map.Entry<K, V> next() {
			return nextEntry();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	final class ValueIterator extends PrivateEntryIterator<V>{

		ValueIterator(Node<K, V> first) {
			super(first);
		}
		
		@Override
		public V next() {
			return nextEntry().getValue();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
		
	}
	
	final class KeyIterator extends PrivateEntryIterator<K>{

		KeyIterator(Node<K, V> first) {
			super(first);
		}

		@Override
		public K next() {
			return nextEntry().getKey();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	protected abstract class PrivateEntryIterator<T> implements Iterator<T>{
		Node<K,V> next;
		Node<K,V> lastReturned;
		int expectedModCount;
		
		PrivateEntryIterator(Node<K,V> first){
			expectedModCount = modCount;
			lastReturned = null;
			next = first;
		}
		
		@Override
		public boolean hasNext() {
			return next != null;
		}

		final Node<K,V> nextEntry(){
			Node<K,V> e = next;
			if (e == null)
				throw new NoSuchElementException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			next = successor(e);
			lastReturned = e;
			return e;
		}		
		
	}
	
	protected class KeySet extends AbstractSet<K>{

		@Override
		public Iterator<K> iterator() {
			return new KeyIterator(root.getFirstEntry());
		}

		@Override
		public int size() {
			return AvlGTree.this.size();
		}


	}

	protected class Values extends AbstractCollection<V>{

		@Override
		public Iterator<V> iterator() {
			return new ValueIterator(root.getFirstEntry());
		}

		@Override
		public int size() {
			return AvlGTree.this.size();
		}
		
	}
	
	public Node<K, V> successor(Node<K, V> e) {
		if(e == null)
			return null;
		else if (e.right != null){
			Node<K,V> parent = e.right;
			while(parent.left != null)
				parent = parent.left;
			return parent;
		} else {
			Node<K,V> parent = e.parent;
			Node<K,V> child = e;
			while(parent != null && child == parent.right){
				child = parent;
				parent = parent.parent;
			}
			return parent;
		}
	}

	/* Returns the map's entry for the given Key, or null if 
	 * the map does not contain an entry for the key*/
	final Node<K,V> getEntry(K key){
		if(key == null)
			throw new NullPointerException();
		Node<K,V> p = root;
		while(p != null) {
			int cmp = comparator.compare(key, p.getKey());
			
			if(cmp < 0)
				p = p.left;
			else if (cmp > 0)
				p = p.right;
			else
				return p;
		}
		return null;
	}

	final static  boolean valEquals(Object o1, Object o2){
		return (o1 == null ? o2 == null :  o1.equals(o2));
	}
}