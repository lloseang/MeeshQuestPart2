package cmsc420.sortedmap;

public class Node<K,V> implements java.util.Map.Entry<K, V> {
	private K key;
	private V value;
    protected Node<K,V> left;
    protected Node<K,V> right;
    protected Node<K,V> parent;
    protected int height;
    
    public Node(){
    	this(null, null, null, null, null);
    }
	
    public Node(K key,V value){
		this(key, value, null, null, null);
	}
	
	public Node(K key, V value, Node<K,V> parent){
		this(key, value, null, null, parent);
	}

	public Node(K key, V value, Node<K,V> left, Node<K,V> right, Node<K,V> parent){
		this.key = key;
		this.value = value;
		this.left = left;
		this.right = right;
		this.parent = parent;
		this.height = 0;
	}	
	
	@Override
	public String toString(){
		return String.format("%s=%s", this.key, this.value);
	}
	
	@Override
	public K getKey() {
		return this.key;
	}

	@Override
	public V getValue() {
		return this.value;
	}

	@Override
	public V setValue(V value) {
		if(value == null)
			throw new NullPointerException();
		if(this.value.equals(value)){
			return value;
		} else {
			V temp = this.value;
			this.value = value;	
			return temp;
		}
	}

	public Node<K,V> getLeft(){
		return this.left;
	}
	
	public Node<K,V> getRight(){
		return this.right;
	}
	
	public int hashCode(){
		int keyHash = (this.key == null ? 0 : this.key.hashCode());
		int valueHash = (this.value == null ? 0 : this.value.hashCode());
		return keyHash ^ valueHash;
	}

	final Node<K,V> getFirstEntry(){
		Node<K,V> p = this;
		if(p != null)
			while(p.left != null)
				p = p.left;
		return p;
	}
	
	final Node<K,V> getLastEntry(){
		Node<K,V> p = this;
		if(p != null)
			while(p.right != null)
				p = p.right;
		return p;
	}
}