package cmsc420.pmquadtree;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.util.TreeSet;

import cmsc420.canonicalsolution.City;
import cmsc420.canonicalsolution.PRQuadtree.Node;
import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Circle2D;
import cmsc420.geom.Inclusive2DIntersectionVerifier;

public abstract class PMQuadtree {
	protected Validator validator;
	
	protected Point2D.Float spatialOrigin;
	protected int spatialWidth;
	protected int spatialHeight;
	
	protected Node root;
	protected WhiteNode whiteNode = new WhiteNode();
	protected CanvasPlus canvas;
	protected Rectangle2D.Float spatialBound;
	
	abstract class Node{
		protected int width;
		protected int height;
		protected Point2D.Float origin;
		protected Road road;	
		protected TreeSet<Road> roads;
		protected Rectangle2D.Float region;
		abstract Node add(Road road, Point2D.Float origin, int width, int height, Rectangle2D.Float region);
	}
	
	public class BlackNode extends Node{
		protected City city;
		protected GreyNode greyNode;
		protected TreeSet<Road> roads;
		
		public BlackNode(City city, Road road, Point2D.Float origin, int width, int height, Rectangle2D.Float region){
			this.city = city;
			this.road = road;
			this.origin = origin;
			this.width = width;
			this.height = height;
			this.region = region;
			this.roads = new TreeSet<Road>(new RoadComparator());
			this.roads.add(this.road);
		}
		
		public void setBounds(Point2D.Float origin, int width, int height){
			this.origin = origin;
			this.width = width;
			this.height = height;
		}
		
		public void setCity(City city){
			this.city = city;
		}
		
		@Override
		public Node add(Road road, Point2D.Float origin, int width, int height, Rectangle2D.Float region) {		
			if(road.start.equals(road.end))
				throw new RuntimeException();
			
			Line2D.Float segment = road.getLine();
			
			for(Road r : roads){
				if(Inclusive2DIntersectionVerifier.intersects(segment, r.getLine())){
					throw new RuntimeException();
				}
			}
			
			if(road.start.equals(city) || road.end.equals(city)){
				roads.add(road);
				return this;
			}
			
			GreyNode grey = new GreyNode(road, origin, width, height, region);
			if(Inclusive2DIntersectionVerifier.intersects(road.start.toPoint2D(), region)){
				
			}
			
			
			
			
			return null;
		}
		
		@Override
		public String toString(){
			return String.format("%s|<%d-%d, %d-%d>|%s", city == null ? "Null" : city.toString(), (int) origin.x, (int) origin.x + width, (int) origin.y, (int) origin.y + height, roads.toString());
		}

	}
	
	public class GreyNode extends Node{
		/** children nodes of this node */
		protected Node[] children;
		protected Rectangle2D.Float[] regions;
		protected Point2D.Float[] origins;
		protected int halfWidth;
		protected int halfHeight;
		protected Rectangle2D.Float region;
		
		public void init(){
			children = new Node[4];
			for (int i = 0; i < 4; i++) {
				children[i] = whiteNode;
			}

			origins = new Point2D.Float[4];
			origins[0] = new Point2D.Float(origin.x, origin.y + halfHeight);
			origins[1] = new Point2D.Float(origin.x + halfWidth, origin.y + halfHeight);
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
		
		public GreyNode(Road road, Point2D.Float origin, int width, int height, Rectangle2D.Float region) {
			this.origin = origin;
			this.width = width;
			this.height = height;
			this.halfWidth = width >> 1;
			this.halfHeight = height >> 1;
			this.region = region;
			this.init();
		}
		
		public int getCenterX() {
			return (int) origin.x + halfWidth;
		}

		public int getCenterY() {
			return (int) origin.y + halfHeight;
		}
		
		@Override
		public Node add(Road road, Float origin, int width, int height, Rectangle2D.Float region) {
			if(road.start.equals(road.end))
				throw new RuntimeException();
			
			Line2D.Float segment = road.getLine();
			for (int i = 0; i < 4; i++){
				if(Inclusive2DIntersectionVerifier.intersects(segment, regions[i])){
					children[i] = children[i].add(road, origins[i], halfWidth, halfHeight, regions[i]);
				}
			}
			return this;
		}

	
		
		@Override
		public String toString(){
			return "Grey";
		}
	}
	
	public class WhiteNode extends Node{
		@Override
		public Node add(Road road, Point2D.Float origin, int width, int height, Rectangle2D.Float region) {
			if(road.start.equals(road.end))
				throw new RuntimeException();
		
			boolean startOutOfBounds = Inclusive2DIntersectionVerifier.intersects(road.start.toPoint2D(), spatialBound);
			boolean endOutOfBounds = Inclusive2DIntersectionVerifier.intersects(road.end.toPoint2D(), spatialBound);
			Line2D.Float line = road.getLine();
			BlackNode blackNode;
			if(!(startOutOfBounds && endOutOfBounds)){	
				if (startOutOfBounds)
					return new BlackNode(road.start, road, origin, width, height, region);
				if (endOutOfBounds)
					return new BlackNode(road.end, road, origin, width, height, region);
			}
		
			GreyNode grey = new GreyNode(road, origin, width, height, region);
			for (int i = 0; i < 4; i++){	
				if(Inclusive2DIntersectionVerifier.intersects(line, grey.regions[i])){
					blackNode = new BlackNode(null, road, origin, width, height, grey.regions[i]);
					if(Inclusive2DIntersectionVerifier.intersects(road.start.toPoint2D(), grey.regions[i])){
						blackNode.setCity(road.start);
					}
					if(Inclusive2DIntersectionVerifier.intersects(road.end.toPoint2D(), grey.regions[i])){
						blackNode.setCity(road.end);
					}
					blackNode.setBounds(grey.origins[i], grey.halfWidth, grey.halfHeight);
					grey.children[i] = blackNode;
				}
			}
			return grey;
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
	
	public void setSpatialBound(){
		this.spatialBound = new Rectangle2D.Float(0,0,this.spatialWidth, this.spatialHeight);
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
		root = root.add(road, spatialOrigin, spatialWidth, spatialHeight, spatialBound);
	}
	
	protected boolean intersectsBound(Point2D point){
		return (point.getX() >= spatialBound.getMinX() && point.getX() < spatialBound.getMaxX()
				&& point.getY() >= spatialBound.getMinY() && point.getY() < spatialBound.getMaxY());
	}
	
	protected boolean intersects(Point2D point, Rectangle2D rect) {
		return (point.getX() >= rect.getMinX() && point.getX() <= rect.getMaxX()
				&& point.getY() >= rect.getMinY() && point.getY() <= rect.getMaxY());
	}
}
