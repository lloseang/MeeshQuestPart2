package cmsc420.pmquadtree;

public class RoadAlreadyMappedException extends Exception {
	private static final long serialVersionUID = 1L;
	public RoadAlreadyMappedException(){}
	public RoadAlreadyMappedException(String message){
		super(message);
	}
}
