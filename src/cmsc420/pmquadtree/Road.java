package cmsc420.pmquadtree;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;

import cmsc420.canonicalsolution.City;

public class Road {
	protected City start;
	protected City end;

	public Road(City start, City end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public String toString(){
		return String.format("%s - %s", start.toString(), end.toString());
	}
	
	public Line2D.Float getLine(){
		return new Line2D.Float(start.getX(), start.getY(), end.getX(), end.getY());
	}
	
	public City getStart(){
		return start;
	}
	
	public City getEnd(){
		return end;
	}
	
}