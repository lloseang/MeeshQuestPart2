package cmsc420.sortedmap;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;

public class AvlGTree <K, V> implements SortedMap<K, V> {
	private Node root;
	private Comparator<? super K> comparator;
	private int g;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

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

	@Override
	public String toString(){
		return toString(root, 1);
	}

	@Override
	public boolean equals(Object o){
		return false;
	}

	private V get(Node root, K key){
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

	private Node insert(K key, V value, Node root) {
		int balanceFactor;
		if (root == null)
			root = new Node(key, value);
		
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
				if(comparator.compare(key, root.left.key) < 0) {
					root = rotateRight(root);
				} else {
					root = rotateLeftRight(root);
				}
			}
			else if (balanceFactor < -g){
				if(comparator.compare(key, root.right.key) > 0) {
					root = rotateLeft(root);
				} else {
					root = rotateRightLeft(root);
				}
			}
		}
		root.height = max(height(root.left), height(root.right)) + 1;
		return root;
	}

	private Node rotateRightLeft(Node root) {
	    root.right = rotateRight( root.right );
        return rotateLeft(root);
	}

	private Node rotateRight(Node root) {
        Node temp = root.left;
        root.left = temp.right;
        temp.right = root;
        
        root.height = max( height( root.left ), height( root.right ) ) + 1;
        temp.height = max( height( temp.left ), root.height ) + 1;
        return temp;
	}

	private Node rotateLeftRight(Node root) {
		root.left = rotateLeft( root.left );
	    return rotateRight(root);
	}

	private Node rotateLeft(Node root) {
		Node temp = root.right;
	    root.right = temp.left;
	    temp.left = root;
	      
	    root.height = max( height( root.left ), height( root.right ) ) + 1;
	    temp.height = max( height( temp.right ), root.height ) + 1;
	    return temp;
	}

	private int height(Node root){
		return root == null ? -1 : root.height;
	}
	
	private int max(int left, int right){
		return left > right ? left: right;
	}
	
	private int size(Node root) {
		if(root == null)
			return 0;
		else
			return 1 + size(root.left) + size(root.right);
	}

	private K lastKey(Node root) {
		if (root.right != null)
			return lastKey(root.right);
		else
			return root.key;
	}

	private K firstKey(Node root) {
		if(root.left != null)
			return firstKey(root.left);
		else
			return root.key;	
	}
	
	StringBuffer tab = new StringBuffer();
	public String toString(Node root, int level){
		if (root == null)
			return "";
		else {
			tab.setLength(0);
			for (int i = 1; i < level; i++)
				tab.append("\t");
	
			return tab.toString() + root.toString() + "\n" + toString(root.left, level + 1) + toString(root.right, level + 1);
		}
	}

	public class Node {
		private K key;
		private V value;
        private Node left;
        private Node right;
        private int height;
		
		public Node(K key,V value){
			this(key, value, null, null);
		}
		
		public Node(K key, V value, Node left, Node right){
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

	protected class EntrySet implements Set {
	
		@Override
		public boolean add(Object e) {
			Map.Entry<K,V> me = (Map.Entry) e;
			
			if(AvlGTree.this.containsKey(e)){
				AvlGTree.this.put(me.getKey(), me.getValue());
				return true;
			} else
				return false;
		}
	
		@Override
		public boolean addAll(Collection c) {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public void clear() {
			AvlGTree.this.clear();	
		}
	
		@Override
		public boolean contains(Object o) {
			Map.Entry<K,V> me = (Map.Entry<K,V>) o;
			return AvlGTree.this.containsKey(me.getKey()) &&
					(me.getValue() == null ? 
							AvlGTree.this.get(me.getKey()) == null :
							me.getValue().equals(AvlGTree.this.get(me.getKey())));
		}
	
		@Override
		public boolean containsAll(Collection c) {
			// TODO Auto-generated method stub
			return false;
		}
	
		@Override
		public boolean isEmpty() {
			return AvlGTree.this.isEmpty();
		}
	
		@Override
		public Iterator iterator() {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public boolean remove(Object o) {
			Map.Entry me = (Map.Entry) o;
			
			boolean b = AvlGTree.this.containsKey(me.getKey());
			AvlGTree.this.remove(me.getKey());
			return b;
		}
	
		@Override
		public boolean removeAll(Collection c) {
			throw new UnsupportedOperationException();
		}
	
		@Override
		public boolean retainAll(Collection c) {
			// TODO Auto-generated method stub
			return false;
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
		public Object[] toArray(Object[] a) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return new EntrySet();
	}
}