package cmsc420.sortedmap;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class AvlGTree <K, V> implements SortedMap<K, V> {
	private Node<K,V> root;
	private Comparator<? super K> comparator;
	private int g;
	private transient int modCount;
	private V previousValue;
	
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean containsValue(Object value) {
		if (value == null)
			throw new NullPointerException();
		return containsValue((V)value, root);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
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
		
	}

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
		return null;
	}

	@Override
	public Set<K> keySet() {
		return null;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		return null;
	}

	@Override
	public Collection<V> values() {
		return null;
	}

	@Override
	public String toString(){
		return toString(root, 1);
	}

	@Override
	public boolean equals(Object o){
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new EntrySet();
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
        root.left = temp.right;
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
	    root.right = temp.left;
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
		public void clear() {
			AvlGTree.this.clear();
		}
		
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				throw new ClassCastException();
			
			Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
		
			V value = entry.getValue();
			Node<K,V> p = getEntry(entry.getKey());
			return p != null && valEquals(p.getValue(), value);
		}

		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int size() {
			return AvlGTree.this.size();
		}

		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new EntryIterator(root.getFirstEntry());
		}
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
	
	public abstract class PrivateEntryIterator<T> implements Iterator<T>{
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
}