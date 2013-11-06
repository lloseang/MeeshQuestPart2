package cmsc420.pmquadtree;

public abstract class PMQuadtree {
	protected int spatialWidth;
	protected int spatialHeight;
	
	interface Node{
		
	}
	
	public class BlackNode implements Node{
		
	}
	
	public class GreyNode implements Node{
		
	}
	
	public class WhiteNode implements Node{
		
	}
	
	public PMQuadtree(){
		
	}
	
	public void setRange(int spatialWidth, int spatialHeight){
		this.spatialWidth = spatialWidth;
		this.spatialHeight = spatialHeight;
	}
}
