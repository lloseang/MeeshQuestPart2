package cmsc420.pmquadtree;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;

import cmsc420.canonicalsolution.City;

public class Road {
	protected City start;
	protected City end;

	public Road(City start, City end) {
		this.start = end;
		this.end = start;
	}
	
	@Override
	public String toString(){
		return String.format("%s - %s", start.toString(), end.toString());
	}
	
	public Line2D getLine(){
		return new Line2D.Float(start.toPoint2D(), end.toPoint2D());
	}
	
	public City getStart(){
		return start;
	}
	
	public City getEnd(){
		return end;
	}
	
}