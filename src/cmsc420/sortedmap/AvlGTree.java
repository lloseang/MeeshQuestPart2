package cmsc420.sortedmap;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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

	@SuppressWarnings("unchecked")
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
		root = insert(key, value, root);
		return previousValue;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub	
		
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
			return firstKey(root);
	}

	@Override
	public K lastKey() {
		if (root == null)
			throw new NoSuchElementException();
		else
			return lastKey(root);
	}

	@Override
	public SortedMap<K, V> headMap(K toKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<K> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> subMap(K fromKey, K toKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedMap<K, V> tailMap(K fromKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	private boolean containsKey(K key, Node<K,V> root) {
		if(root == null)
			return false;
		
		if(comparator.compare(key, root.key) < 0){
			return containsKey(key, root.left);
		}
		else if (comparator.compare(key, root.key) > 0){
			return containsKey(key, root.right);
		}
		else
			return true;
	}

	private boolean containsValue(V value, Node<K,V> root) {
		if(root == null)
			return false;
		
		if(root.value.equals(value))
			return true;
		
		return containsValue(value, root.left) || containsValue(value, root.right);
	}

	@SuppressWarnings("unchecked")
	private V get(Node<K,V> root, K key){
		if(root == null)
			return null;
		if(comparator.compare(key, root.key) < 0){
			return get(root.left, key);
		} 
		else if(comparator.compare(key, root.key) > 0){
			return get(root.right, key);
		} else {
			return root.value;
		}
	}

	@SuppressWarnings("unchecked")
	private Node<K,V> insert(K key, V value, Node<K,V> root) {
		int balanceFactor;
		if (root == null)
			root = new Node<K,V>(key, value);
		
		// If key is equal to root.key
		else if (comparator.compare(root.key, key) == 0){
			previousValue = root.value;
			root.value = value;
		}
		else {
			// If key is lesser than root.key
			if (comparator.compare(root.key, key) > 0)
				root.left = insert(key, value, root.left);
			// If key is greater than root.key
			else if (comparator.compare(root.key, key) < 0)
				root.right = insert(key, value, root.right);
			
			
			balanceFactor = height(root.left) - height(root.right);
			
			if(balanceFactor > g){
				if(comparator.compare(key, (K) root.left.key) < 0) {
					root = rotateRight(root);
				} else {
					root = rotateLeftRight(root);
				}
			}
			else if (balanceFactor < -g){
				if(comparator.compare(key, (K) root.right.key) > 0) {
					root = rotateLeft(root);
				} else {
					root = rotateRightLeft(root);
				}
			}
		}
		root.height = max(height(root.left), height(root.right)) + 1;
		return root;
	}

	@SuppressWarnings("unchecked")
	private Node<K,V> rotateRightLeft(Node<K,V> root) {
	    root.right = rotateRight( root.right );
        return rotateLeft(root);
	}

	@SuppressWarnings("unchecked")
	private Node<K,V> rotateRight(Node<K,V> root) {
        Node<K,V> temp = root.left;
        root.left = temp.right;
        temp.right = root;
        
        root.height = max( height( root.left ), height( root.right ) ) + 1;
        temp.height = max( height( temp.left ), root.height ) + 1;
        return temp;
	}

	private Node<K,V> rotateLeftRight(Node<K,V> root) {
		root.left = rotateLeft( root.left );
	    return rotateRight(root);
	}

	private Node<K,V> rotateLeft(Node<K,V> root) {
		Node<K,V> temp = root.right;
	    root.right = temp.left;
	    temp.left = root;
	      
	    root.height = max( height( root.left ), height( root.right ) ) + 1;
	    temp.height = max( height( temp.right ), root.height ) + 1;
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

	private K lastKey(Node<K,V> root) {
		if (root.right != null)
			return lastKey(root.right);
		else
			return root.key;
	}

	private K firstKey(Node<K,V> root) {
		if(root.left != null)
			return firstKey(root.left);
		else
			return root.key;	
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

	public class Node<K,V> {
		private K key;
		private V value;
        private Node<K,V> left;
        private Node<K,V> right;
        private int height;
		
		public Node(K key,V value){
			this(key, value, null, null);
		}
		
		public Node(K key, V value, Node<K,V> left, Node<K,V> right){
			this.key = key;
			this.value = value;
			this.left = left;
			this.right = right;
			this.height = 0;
		}	
		
		@Override
		public String toString(){
			return String.format("Node<%s, %s>", this.key, this.value);
		}
	}

	protected class EntrySet implements Set<Map.Entry<K, V>> {

		@Override
		public boolean add(java.util.Map.Entry<K, V> e) {
			if(e == null)
				throw new NullPointerException();
			if(!(e instanceof Map.Entry))
				throw new ClassCastException();
			if(this.contains(e))
				return false;
			
			try {
				AvlGTree.this.put(e.getKey(), e.getValue());
				return true;
			} catch (Exception exception){
				throw new IllegalArgumentException();
			}
		}

		@Override
		public boolean addAll(Collection<? extends java.util.Map.Entry<K, V>> c) {
			for(Map.Entry<K,V> entry : c){
				if(!add(entry))
					return false;
			}
			return true;
		}

		@Override
		public void clear() {
			AvlGTree.this.clear();
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean contains(Object o) {
			if (!(o instanceof Map.Entry))
				throw new ClassCastException();
			
			Map.Entry<K, V> entry = (Map.Entry<K, V>) o;
			if(AvlGTree.this.containsKey(entry.getKey())){
				return AvlGTree.this.get(entry.getKey()).equals(entry.getValue());
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for (Object obj : c){
				if(!(obj instanceof Map.Entry))
					throw new ClassCastException();
				if(!contains(obj))
					return false;
			}
			return true;
		}

		@Override
		public boolean isEmpty() {
			return AvlGTree.this.isEmpty();
		}

		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean remove(Object o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int size() {
			return AvlGTree.this.size();
		}

		@Override
		public Object[] toArray() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T[] toArray(T[] a) {
			// TODO Auto-generated method stub
			return null;
		}
	
	}
	
	public abstract class PrivateEntryIterator<T> implements Iterator<T>{
		Entry<K,V> next;
		Entry<K,V> lastReturned;
		int expectedModCount;
		
		PrivateEntryIterator(Entry<K,V> first){
			expectedModCount = modCount;
			lastReturned = null;
			next = first;
		}
		
		@Override
		public boolean hasNext() {
			return next != null;
		}

		/*
		final Entry<K,V> nextEntry(){
			Entry<K,V> e = next;
			if (e == null)
				throw new NoSuchElementException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			next = successor(e);
		}		
		*/
	}
}