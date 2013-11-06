package cmsc420.pmquadtree;

public class RoadOutOfBoundsException extends Exception {
	private static final long serialVersionUID = 1L;
	public RoadOutOfBoundsException(){}
	public RoadOutOfBoundsException(String message){
		super(message);
	}
}
