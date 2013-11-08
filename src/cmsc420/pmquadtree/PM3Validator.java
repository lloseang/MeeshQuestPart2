package cmsc420.pmquadtree;

import cmsc420.pmquadtree.PMQuadtree.BlackNode;

public class PM3Validator implements Validator{

	@Override
	public boolean valid(BlackNode black) {
		return black.numCities <= 1 ? true : false;
	}

}
