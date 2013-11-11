package cmsc420.pmquadtree;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.util.TreeSet;

import cmsc420.canonicalsolution.City;
import cmsc420.canonicalsolution.CityLocationComparator;
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
	protected TreeSet<Road> roads;
	
	public abstract class Node{
		abstract Node add(Road road, Point2D.Float origin, int width, int height);
	}
	
	public class BlackNode extends Node{
		protected City city;
		protected TreeSet<Road> roads;
		protected int numCities;
		protected Rectangle2D.Float region;
		
		public BlackNode(){
			this.numCities = 0;
			this.roads = new TreeSet<Road>(new RoadComparator());
		}

		@Override
		Node add(Road road, Float origin, int width, int height) {
			region = new Rectangle2D.Float(origin.x, origin.y, width, height);
			
			addRoad(road);
			addCity(road.start);
			addCity(road.end);
			
			if(isValid()){
				return this;
			} else
				return partition(origin, width, height);
		}

		private Node partition(Float origin, int width, int height) {
			GreyNode greyNode = new GreyNode(origin, width, height);
			for(Road road : roads){
				greyNode.add(road);
			}
			return greyNode;
		}

		private boolean isValid() {
			return validator.valid(this);
		}

		private void addRoad(Road road){
				roads.add(road);
		}

		private void addCity(City city) {
			if(Inclusive2DIntersectionVerifier.intersects(city.toPoint2D(), region)){
				if(this.city == null){
					this.city = city;
					numCities++;
					return;	
				}
				if(!this.city.equals(city)){
					numCities++;
					return;
				}
			}
		}
	
		@Override
		public String toString(){
			return roads.toString();
		}
		
		public City getCity(){
			return city;
		}
		
		public TreeSet<Road> getRoads(){
			return roads;
		}
	}
	
	public class GreyNode extends Node{
		protected Point2D.Float origin;
		protected int width;
		protected int height;
		protected Node[] children;
		protected Rectangle2D.Float[] regions;
		protected Rectangle2D.Float region;
		protected Point2D.Float[] origins;
		protected int halfWidth;
		protected int halfHeight;

		public GreyNode(Point2D.Float origin, int width, int height) {
			this.origin = origin;
			this.width = width;
			this.height = height;
			this.halfWidth = width >> 1;
			this.halfHeight = height >> 1;
			this.region = new Rectangle2D.Float(origin.x, origin.y, width, height);
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
                canvas.addLine(cx - halfWidth, cy, cx + halfWidth, cy, Color.GRAY);
                canvas.addLine(cx, cy - halfHeight, cx, cy + halfHeight, Color.GRAY);
			}
		}
		
		public void add(Road road) {
			for(int i = 0; i < 4; i++){
				if(Inclusive2DIntersectionVerifier.intersects(road.getLine(), regions[i])){
					children[i] = children[i].add(road, origins[i], halfWidth, halfHeight);
				}
			}	
		}

		public int getCenterX() {
			return (int) origin.x + halfWidth;
		}

		public int getCenterY() {
			return (int) origin.y + halfHeight;
		}
		
		public Rectangle2D.Float getRegion(int i){
			return regions[i];
		}

		@Override
		public String toString(){
			StringBuffer sb = new StringBuffer();
			for(int i = 0; i < 4; i++){
				sb.append(String.format("%s\n", children[i]));
			}
			return sb.toString();
		}

		@Override
		Node add(Road road, Float origin, int width, int height) {
			add(road);
			return this;
		}
		
		public Node[] getChildren(){
			return children;
		}
	}
	
	public class WhiteNode extends Node{
		
		@Override
		Node add(Road road, Float origin, int width, int height) {
			BlackNode blackNode = new BlackNode();
			return blackNode.add(road, origin, width, height);
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
	
	public PMQuadtree(Validator validator) {
		this.roads = new TreeSet<Road>(new RoadComparator());
		this.spatialOrigin = new Point2D.Float(0, 0);
		this.validator = validator;
		this.root = whiteNode;
	}
	
	public void setRange(int spatialWidth, int spatialHeight){
		this.spatialWidth = spatialWidth;
		this.spatialHeight = spatialHeight;
	}

	public void clear() {
		this.roads.clear();
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

	public void add(Road road){
		roads.add(road);
		root = root.add(road, spatialOrigin, spatialWidth, spatialHeight);
	}
	
	public Node getRoot() {
		return root;
	}
	
	public TreeSet<Road> getRoads(){
		return roads;
	}
	
	public Rectangle2D.Float getSpatialBound(){
		return spatialBound;
	}
	
	public void setSpatialBound() {
		this.spatialBound = new Rectangle2D.Float(spatialOrigin.x, spatialOrigin.y, spatialWidth, spatialHeight);
	}
	
	public TreeSet<City> keySet(){
		TreeSet<City> cities = new TreeSet<City>(new CityLocationComparator());
		if(root instanceof WhiteNode)
			return cities;
		keySet(root, cities);
		return cities;
	}

	private void keySet(Node node, TreeSet<City> cities) {
		if(node instanceof BlackNode){
			BlackNode blackNode = (BlackNode) node;
			final City city = blackNode.getCity();
			if(city != null)
				cities.add(city);
		}
		
		if(node instanceof GreyNode){
			GreyNode greyNode = (GreyNode) node;
			for(int i = 0; i < 4; i++){
				keySet(greyNode.getChildren()[i], cities);
			}
		}
		
	}
	
	public Road getRoad(String start, String end){
		for(Road r : roads){
			if((r.start.getName().equals(start) && r.end.getName().equals(end)) ||
			   (r.end.getName().equals(start) && r.start.getName().equals(end))){
				return r;
			}
		}
		return null;
	}
	
}
