package cmsc420.pmquadtree;

import java.awt.geom.Point2D;

import cmsc420.canonicalsolution.City;
import cmsc420.drawing.CanvasPlus;

public abstract class PMQuadtree {
	protected Validator validator;
	protected int spatialWidth;
	protected int spatialHeight;
	protected Point2D.Float spatialOrigin;
	protected Node root;
	protected WhiteNode whiteNode = new WhiteNode();
	protected CanvasPlus canvas;
	
	interface Node{
		Node add(Road road, int lBound, int rBound, int uBound, int dBound);
	}
	
	public class BlackNode implements Node{

		@Override
		public Node add(Road road, int lBound, int rBound, int uBound, int dBound) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class GreyNode implements Node{

		@Override
		public Node add(Road road, int lBound, int rBound, int uBound, int dBound) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	public class WhiteNode implements Node{

		@Override
		public Node add(Road road, int lBound, int rBound, int uBound, int dBound) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
	public PMQuadtree(Validator validator){
		this.spatialOrigin = new Point2D.Float(0,0);
		this.validator = validator;
		this.root = whiteNode;
	}
	
	public void setRange(int spatialWidth, int spatialHeight){
		this.spatialWidth = spatialWidth;
		this.spatialHeight = spatialHeight;
	}

	public void clear() {
		this.root = whiteNode;
	}

	public boolean isEmpty() {
		return root == whiteNode;
	}

	public void setCanvas(CanvasPlus canvas) {
		this.canvas = canvas;
	}

	
	public boolean contains(Road road) {
		// TODO Auto-generated method stub
		return false;
	}

	public void add(Road road, int lBound, int rBound, int uBound, int dBound) {
		root = root.add(road, lBound, rBound, uBound, dBound);
		
	}
}
