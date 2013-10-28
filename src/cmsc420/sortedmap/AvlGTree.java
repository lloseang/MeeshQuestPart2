package cmsc420.sortedmap;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import cmsc420.sortedmap.AvlTree.AvlNode;

public class AvlGTree <K, V> implements SortedMap<K, V> {
	private Node root;
	private Comparator<? super K> comparator;
	private int g;
	private V previousValue;
	
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V put(K key, V value) {
		root = insert(key, value, root);
		return previousValue;
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
				System.out.println("Weighted left");
				
				if(comparator.compare(key, root.left.key) < 0) {
					System.out.println("rotate right");
				} else {
					System.out.println("rotate leftRight");
				}
			}
			else if (balanceFactor < -g){
				System.out.println("Weighted right");
				if(comparator.compare(key, root.right.key) > 0) {
					System.out.println("rotate left");
					root = rotateLeft(root);
				} else {
					System.out.println("rotate rightleft");
				}
			}
		}
		root.height = max(height(root.left), height(root.right)) + 1;
		return root;
	}

	private Node rotateRightLeft(Node root) {
	    root.left = rotateRight( root.left );
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
		root.right = rotateLeft( root.right );
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
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Comparator<? super K> comparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public K firstKey() {
		// TODO Auto-generated method stub
		return null;
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
	public K lastKey() {
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

	
	public class Node {
		private K key;
		private V value;
        private Node left;
        private Node right;
        private int height;
		private StringBuffer tab;
		
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
		
		public String toString(){
			return String.format("Node<%s, %s>", this.key, this.value);
		}
	}
	
	
	public String toString(){
		return toString(root, 1);
	}
	StringBuffer tab = new StringBuffer();
	String left = "left", right = "right", root_dir = "root_dir";
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
	
}