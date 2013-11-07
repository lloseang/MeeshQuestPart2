package cmsc420.pmquadtree;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;

import cmsc420.canonicalsolution.City;
import cmsc420.canonicalsolution.PRQuadtree.Node;
import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Circle2D;

public abstract class PMQuadtree {
	protected Validator validator;
	
	protected Point2D.Float spatialOrigin;
	protected int spatialWidth;
	protected int spatialHeight;
	
	protected Node root;
	protected WhiteNode whiteNode = new WhiteNode();
	protected CanvasPlus canvas;
	
	abstract class Node{
		protected int width;
		protected int height;
		protected Point2D.Float origin;
		protected Road road;	
		abstract Node add(Road road, Point2D.Float origin, int width, int height);
		abstract Node addBlackNode(City city, Road road, Point2D.Float origin, int width, int height);
	}
	
	public class BlackNode extends Node{
		protected City city;
		protected GreyNode greyNode;
		
		public BlackNode(City city, Road road, Point2D.Float origin, int width, int height){
			this.city = city;
			this.road = road;
			this.origin = origin;
			this.width = width;
			this.height = height;
		}
		
		@Override
		public Node add(Road road, Point2D.Float origin, int width, int height) {
			greyNode = new GreyNode(road, origin, width, height);
			greyNode.addBlackNode(this.city, this.road, this.origin, this.width, this.height);
			greyNode.addBlackNode(road.start, road, origin, width, height);
			greyNode.addBlackNode(road.end, road, origin, width, height);
			return greyNode;
		}

		@Override
		Node addBlackNode(City city, Road road, Point2D.Float origin, int width, int height) {
			greyNode = new GreyNode(road, origin, width, height);
			greyNode.addBlackNode(this.city, this.road, this.origin, this.width, this.height);
			greyNode.addBlackNode(city, road, origin, width, height);
			
			System.out.println(greyNode.toString());
			return greyNode;
		}
		
		@Override
		public String toString(){
			return String.format("Black|%s|(%d-%d),(%d-%d)>", city.toString(), (int) origin.x, (int) origin.x + width, (int) origin.y, (int) origin.y + height);
		}
	}
	
	public class GreyNode extends Node{
		/** children nodes of this node */
		protected Node[] children;
		protected Rectangle2D.Float[] regions;
		protected Point2D.Float[] origins;
		protected int halfWidth;
		protected int halfHeight;
		
		public void init(){
			children = new Node[4];
			for (int i = 0; i < 4; i++) {
				children[i] = whiteNode;
			}

			origins = new Point2D.Float[4];
			origins[0] = new Point2D.Float(origin.x, origin.y + halfHeight);
			origins[1] = new Point2D.Float(origin.x + halfWidth, origin.y
					+ halfHeight);
			origins[2] = new Point2D.Float(origin.x, origin.y);
			origins[3] = new Point2D.Float(origin.x + halfWidth, origin.y);
			
			regions = new Rectangle2D.Float[4];
			int i = 0;
			while (i < 4) {
				regions[i] = new Rectangle2D.Float(origins[i].x, origins[i].y,
						halfWidth, halfHeight);
				i++;
			}
			
			/* add a cross to the drawing panel */
			if (canvas != null) {
                //canvas.addCross(getCenterX(), getCenterY(), halfWidth, Color.BLACK);
				int cx = getCenterX();
				int cy = getCenterY();
                canvas.addLine(cx - halfWidth, cy, cx + halfWidth, cy, Color.BLACK);
                canvas.addLine(cx, cy - halfHeight, cx, cy + halfHeight, Color.BLACK);
			}
		}
		
		public GreyNode(Road road, Point2D.Float origin, int width, int height) {
			this.origin = origin;
			this.width = width;
			this.height = height;
			this.halfWidth = width >> 1;
			this.halfHeight = height >> 1;
			
			this.init();
		}
		
		public int getCenterX() {
			return (int) origin.x + halfWidth;
		}

		public int getCenterY() {
			return (int) origin.y + halfHeight;
		}
		
		@Override
		public Node add(Road road, Float origin, int width, int height) {
			/* If the two cities are in the same quadrant, 
			 * partition into grey nodes again */
			for (int i = 0; i < 4; i++) {
				if(intersects(road.start.toPoint2D(), regions[i]) && intersects(road.end.toPoint2D(), regions[i])){
					children[i] = children[i].add(road, origins[i], halfWidth, halfHeight);
					return this;
				}
			}
			
			/* If the two cities are not in the same quadrant, 
			 * partition into grey nodes again */
			Node temp = null;
			for (int i = 0; i < 4; i++){
				if(intersects(road.start.toPoint2D(), regions[i])){
					temp = children[i].addBlackNode(road.start, road, origins[i], halfWidth, halfHeight);
					children[i] = temp == null ? children[i] : temp;
				}
				if(intersects(road.end.toPoint2D(), regions[i])){
					temp = children[i].addBlackNode(road.end, road, origins[i], halfWidth, halfHeight);
					children[i] = temp == null ? children[i] : temp;
				}
			}
			return this;
		}

		@Override
		Node addBlackNode(City city, Road road, Point2D.Float origin, int width, int height) {
			for (int i = 0; i < 4; i++){
				if(intersects(city.toPoint2D(), regions[i])){
					children[i] = children[i].addBlackNode(city, road, origins[i], halfWidth, halfHeight);
					System.out.println(children[i].toString());
					return children[i];
				}
			}
			return null;
		}
		
		@Override
		public String toString(){
			return "Grey";
		}
	}
	
	public class WhiteNode extends Node{
		@Override
		public Node add(Road road, Point2D.Float origin, int width, int height) {
			GreyNode grey = new GreyNode(road, origin, width, height);
			grey.addBlackNode(road.start, road, origin, width, height);
			grey.addBlackNode(road.end, road, origin, width, height);
			return grey;
		}

		@Override
		Node addBlackNode(City city, Road road, Point2D.Float origin, int width, int height) {
			return new BlackNode(city, road, origin, width, height);
		}
		
		@Override
		public String toString(){
			return "White";
		}
	}
	
	public PMQuadtree(){
		this.spatialOrigin = new Point2D.Float(0,0);
		this.root = whiteNode;
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

	public void add(Road road) {
		root = root.add(road, spatialOrigin, spatialWidth, spatialHeight);
	}
	
	protected boolean intersects(Point2D point, Rectangle2D rect) {
		return (point.getX() >= rect.getMinX() && point.getX() < rect.getMaxX()
				&& point.getY() >= rect.getMinY() && point.getY() < rect
				.getMaxY());
	}
	
	public boolean intersects(Circle2D circle, Rectangle2D rect) {
		final double radiusSquared = circle.getRadius() * circle.getRadius();

		/* translate coordinates, placing circle at origin */
		final Rectangle2D.Double r = new Rectangle2D.Double(rect.getX()
				- circle.getCenterX(), rect.getY() - circle.getCenterY(), rect
				.getWidth(), rect.getHeight());

		if (r.getMaxX() < 0) {
			/* rectangle to left of circle center */
			if (r.getMaxY() < 0) {
				/* rectangle in lower left corner */
				return ((r.getMaxX() * r.getMaxX() + r.getMaxY() * r.getMaxY()) < radiusSquared);
			} else if (r.getMinY() > 0) {
				/* rectangle in upper left corner */
				return ((r.getMaxX() * r.getMaxX() + r.getMinY() * r.getMinY()) < radiusSquared);
			} else {
				/* rectangle due west of circle */
				return (Math.abs(r.getMaxX()) < circle.getRadius());
			}
		} else if (r.getMinX() > 0) {
			/* rectangle to right of circle center */
			if (r.getMaxY() < 0) {
				/* rectangle in lower right corner */
				return ((r.getMinX() * r.getMinX() + r.getMaxY() * r.getMaxY()) < radiusSquared);
			} else if (r.getMinY() > 0) {
				/* rectangle in upper right corner */
				return ((r.getMinX() * r.getMinX() + r.getMinY() * r.getMinY()) <= radiusSquared);
			} else {
				/* rectangle due east of circle */
				return (r.getMinX() <= circle.getRadius());
			}
		} else {
			/* rectangle on circle vertical centerline */
			if (r.getMaxY() < 0) {
				/* rectangle due south of circle */
				return (Math.abs(r.getMaxY()) < circle.getRadius());
			} else if (r.getMinY() > 0) {
				/* rectangle due north of circle */
				return (r.getMinY() <= circle.getRadius());
			} else {
				/* rectangle contains circle center point */
				return true;
			}
		}
	}
}
