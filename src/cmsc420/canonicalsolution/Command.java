/*
 * @(#)Command.java        1.0 2007/01/23
 *
 * Copyright Ben Zoller (University of Maryland, College Park), 2007
 * All rights reserved. Permission is granted for use and modification in CMSC420 
 * at the University of Maryland.
 */
package cmsc420.canonicalsolution;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Float;
import java.io.IOException;
import java.util.Collection;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.canonicalsolution.PRQuadtree.InternalNode;
import cmsc420.canonicalsolution.PRQuadtree.LeafNode;
import cmsc420.canonicalsolution.PRQuadtree.Node;
import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Circle2D;
import cmsc420.geom.Inclusive2DIntersectionVerifier;
import cmsc420.pmquadtree.PM1Quadtree;
import cmsc420.pmquadtree.PM3Quadtree;
import cmsc420.pmquadtree.PMQuadtree;
import cmsc420.pmquadtree.PMQuadtree.BlackNode;
import cmsc420.pmquadtree.PMQuadtree.GreyNode;
import cmsc420.pmquadtree.PMQuadtree.WhiteNode;
import cmsc420.pmquadtree.Road;
import cmsc420.pmquadtree.RoadAlreadyMappedException;
import cmsc420.pmquadtree.RoadComparator;
import cmsc420.pmquadtree.RoadOutOfBoundsException;
import cmsc420.sortedmap.AvlGTree;

/**
 * Processes each command in the MeeshQuest program. Takes in an XML command
 * node, processes the node, and outputs the results.
 * 
 * @author Ben Zoller
 * @version 2.0, 23 Jan 2007
 */
public class Command {
	/** output DOM Document tree */
	protected Document results;

	/** root node of results document */
	protected Element resultsNode;

	/**
	 * stores created cities sorted by their names (used with listCities
	 * command)
	 */
	protected final TreeMap<String, City> citiesByName = new TreeMap<String, City>();
	protected final TreeSet<City> citiesByLocation = new TreeSet<City>(
			new CityLocationComparator());

	protected AvlGTree<String, City> citiesByNameAvlG;
	protected AvlGTree<City, String> citiesByLocationAvlG;

	/** stores mapped cities in a spatial data structure */
	protected final PRQuadtree prQuadtree = new PRQuadtree();
	protected PMQuadtree pmQuadtree;

	/** spatial width and height of the PR Quadtree */
	protected int spatialWidth, spatialHeight;

	/** graphical mapping tool (used with saveMap command) */
	protected CanvasPlus canvas;

	protected final int PM3 = 3;
	protected final int PM1 = 1;
	protected int pmOrder;
	protected int g;

	/**
	 * Set the graphical mapping tool to be used.
	 * 
	 * @param canvas
	 *            graphical mapping tool
	 */
	public void setCanvas(CanvasPlus canvas) {
		this.canvas = canvas;
	}

	/**
	 * Set the DOM Document tree to send the of processed commands to. Creates
	 * the root results node.
	 * 
	 * @param results
	 *            DOM Document tree
	 */
	public void setResults(Document results) {
		this.results = results;
		resultsNode = results.createElement("results");
		results.appendChild(resultsNode);
	}

	/**
	 * Creates a command result element. Initializes the command name.
	 * 
	 * @param node
	 *            the command node to be processed
	 * @return the results node for the command
	 */
	private Element getCommandNode(final Element node) {
		final Element commandNode = results.createElement("command");
		commandNode.setAttribute("name", node.getNodeName());
		return commandNode;
	}

	/**
	 * Processes an integer attribute for a command. Appends the parameter to
	 * the parameters node of the results. Should not throw a number format
	 * exception if the attribute has been defined to be an integer in the
	 * schema and the XML has been validated beforehand.
	 * 
	 * @param commandNode
	 *            node containing information about the command
	 * @param attributeName
	 *            integer attribute to be processed
	 * @param parametersNode
	 *            node to append parameter information to
	 * @return integer attribute value
	 */
	private int processIntegerAttribute(final Element commandNode,
			final String attributeName, final Element parametersNode) {
		final String value = commandNode.getAttribute(attributeName);

		if (parametersNode != null) {
			/* add the parameters to results */
			final Element attributeNode = results.createElement(attributeName);
			attributeNode.setAttribute("value", value);
			parametersNode.appendChild(attributeNode);
		}

		/* return the integer value */
		return Integer.parseInt(value);
	}

	/**
	 * Processes a string attribute for a command. Appends the parameter to the
	 * parameters node of the results.
	 * 
	 * @param commandNode
	 *            node containing information about the command
	 * @param attributeName
	 *            string attribute to be processed
	 * @param parametersNode
	 *            node to append parameter information to
	 * @return string attribute value
	 */
	private String processStringAttribute(final Element commandNode,
			final String attributeName, final Element parametersNode) {
		final String value = commandNode.getAttribute(attributeName);

		if (parametersNode != null) {
			/* add parameters to results */
			final Element attributeNode = results.createElement(attributeName);
			attributeNode.setAttribute("value", value);
			parametersNode.appendChild(attributeNode);
		}

		/* return the string value */
		return value;
	}

	/**
	 * Reports that the requested command could not be performed because of an
	 * error. Appends information about the error to the results.
	 * 
	 * @param type
	 *            type of error that occurred
	 * @param command
	 *            command node being processed
	 * @param parameters
	 *            parameters of command
	 */
	private void addErrorNode(final String type, final Element command,
			final Element parameters) {
		final Element error = results.createElement("error");
		error.setAttribute("type", type);
		error.appendChild(command);
		error.appendChild(parameters);
		resultsNode.appendChild(error);
	}

	/**
	 * Reports that a command was successfully performed. Appends the report to
	 * the results.
	 * 
	 * @param command
	 *            command not being processed
	 * @param parameters
	 *            parameters used by the command
	 * @param output
	 *            any details to be reported about the command processed
	 */
	private void addSuccessNode(final Element command,
			final Element parameters, final Element output) {
		final Element success = results.createElement("success");
		success.appendChild(command);
		success.appendChild(parameters);
		success.appendChild(output);
		resultsNode.appendChild(success);
	}

	/**
	 * Processes the commands node (root of all commands). Gets the spatial
	 * width and height of the map and send the data to the appropriate data
	 * structures.
	 * 
	 * @param node
	 *            commands node to be processed
	 */
	public void processCommands(final Element node) {
		pmOrder = Integer.parseInt(node.getAttribute("pmOrder"));
		g = Integer.parseInt(node.getAttribute("g"));
		citiesByNameAvlG = new AvlGTree<String, City>(
				String.CASE_INSENSITIVE_ORDER, g);
		citiesByLocationAvlG = new AvlGTree<City, String>(
				new CityLocationComparator(), g);
		spatialWidth = Integer.parseInt(node.getAttribute("spatialWidth"));
		spatialHeight = Integer.parseInt(node.getAttribute("spatialHeight"));

		if (pmOrder == PM3)
			pmQuadtree = new PM3Quadtree();
		else if (pmOrder == PM1)
			pmQuadtree = new PM1Quadtree();

		/* initialize canvas */
		canvas.setFrameSize(spatialWidth, spatialHeight);
		/* add a rectangle to show where the bounds of the map are located */
		canvas.addRectangle(0, 0, (spatialWidth > spatialHeight) ? spatialWidth
				: spatialHeight, (spatialWidth > spatialHeight) ? spatialWidth
				: spatialHeight, Color.WHITE, true);
		canvas.addRectangle(0, 0, spatialWidth, spatialHeight, Color.BLACK,
				false);

		/* set PM Quadtree range */
		pmQuadtree.setRange(spatialWidth, spatialHeight);
		pmQuadtree.setSpatialBound();
		pmQuadtree.setCanvas(canvas);
	}

	/**
	 * Processes a createCity command. Creates a city in the dictionary (Note:
	 * does not map the city). An error occurs if a city with that name or
	 * location is already in the dictionary.
	 * 
	 * @param node
	 *            createCity node to be processed
	 */
	public void processCreateCity(final Element node) {
		final Element commandNode = getCommandNode(node);
		if (node.hasAttribute("id")) {
			commandNode.setAttribute("id", node.getAttribute("id"));
		}
		final Element parametersNode = results.createElement("parameters");

		final String name = processStringAttribute(node, "name", parametersNode);
		final int x = processIntegerAttribute(node, "x", parametersNode);
		final int y = processIntegerAttribute(node, "y", parametersNode);
		final int radius = processIntegerAttribute(node, "radius",
				parametersNode);
		final String color = processStringAttribute(node, "color",
				parametersNode);

		/* create the city */
		final City city = new City(name, x, y, radius, color);

		if (citiesByName.containsKey(name)) {
			addErrorNode("duplicateCityName", commandNode, parametersNode);
		} else if (citiesByLocation.contains(city)) {
			addErrorNode("duplicateCityCoordinates", commandNode,
					parametersNode);
		} else {
			final Element outputNode = results.createElement("output");

			/* add city to dictionary */
			citiesByNameAvlG.put(name, city);
			citiesByLocationAvlG.put(city, name);
			citiesByName.put(name, city);
			citiesByLocation.add(city);

			/* add success node to results */
			addSuccessNode(commandNode, parametersNode, outputNode);
		}
	}

	/**
	 * Clears all the data structures do there are not cities or roads in
	 * existence in the dictionary or on the map.
	 * 
	 * @param node
	 *            clearAll node to be processed
	 */
	public void processClearAll(final Element node) {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");

		/* clear data structures */
		citiesByNameAvlG.clear();
		citiesByLocationAvlG.clear();
		citiesByName.clear();
		citiesByLocation.clear();
		prQuadtree.clear();
		pmQuadtree.clear();

		/* clear canvas */
		canvas.clear();
		/* add a rectangle to show where the bounds of the map are located */
		canvas.addRectangle(0, 0, spatialWidth, spatialHeight, Color.BLACK,
				false);

		/* add success node to results */
		addSuccessNode(commandNode, parametersNode, outputNode);
	}

	/**
	 * Lists all the cities, either by name or by location.
	 * 
	 * @param node
	 *            listCities node to be processed
	 */
	public void processListCities(final Element node) {
		final Element commandNode = getCommandNode(node);
		if(node.hasAttribute("id")){
			commandNode.setAttribute("id", node.getAttribute("id"));
		}
		final Element parametersNode = results.createElement("parameters");
		final String sortBy = processStringAttribute(node, "sortBy",
				parametersNode);

		if (citiesByName.isEmpty()) {
			addErrorNode("noCitiesToList", commandNode, parametersNode);
		} else {
			final Element outputNode = results.createElement("output");
			final Element cityListNode = results.createElement("cityList");

			Collection<City> cityCollection = null;
			if (sortBy.equals("name")) {
				cityCollection = citiesByName.values();
			} else if (sortBy.equals("coordinate")) {
				cityCollection = citiesByLocation;
			} else {
				/* XML validator failed */
				System.exit(-1);
			}

			for (City c : cityCollection) {
				addCityNode(cityListNode, c);
			}
			outputNode.appendChild(cityListNode);

			/* add success node to results */
			addSuccessNode(commandNode, parametersNode, outputNode);
		}
	}

	/**
	 * Creates a city node containing information about a city. Appends the city
	 * node to the passed in node.
	 * 
	 * @param node
	 *            node which the city node will be appended to
	 * @param cityNodeName
	 *            name of city node
	 * @param city
	 *            city which the city node will describe
	 */
	private void addCityNode(final Element node, final String cityNodeName,
			final City city) {
		final Element cityNode = results.createElement(cityNodeName);
		cityNode.setAttribute("name", city.getName());
		cityNode.setAttribute("x", Integer.toString((int) city.getX()));
		cityNode.setAttribute("y", Integer.toString((int) city.getY()));
		cityNode.setAttribute("radius",
				Integer.toString((int) city.getRadius()));
		cityNode.setAttribute("color", city.getColor());
		node.appendChild(cityNode);
	}

	/**
	 * Creates a city node containing information about a city. Appends the city
	 * node to the passed in node.
	 * 
	 * @param node
	 *            node which the city node will be appended to
	 * @param city
	 *            city which the city node will describe
	 */
	private void addCityNode(final Element node, final City city) {
		addCityNode(node, "city", city);
	}

	/**
	 * Processes a saveMap command. Saves the graphical map to a given file.
	 * 
	 * @param node
	 *            saveMap command to be processed
	 * @throws IOException
	 *             problem accessing the image file
	 */
	public void processSaveMap(final Element node) throws IOException {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");

		final String name = processStringAttribute(node, "name", parametersNode);

		final Element outputNode = results.createElement("output");

		/* save canvas to '<name>.png' */
		canvas.save(name);

		/* add success node to results */
		addSuccessNode(commandNode, parametersNode, outputNode);
	}

	/**
	 * Prints out the structure of the PR Quadtree in a human-readable format.
	 * 
	 * @param node
	 *            printPRQuadtree command to be processed
	 */
	public void processPrintAvlTree(final Element node) {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");

		if (citiesByName.isEmpty()) {
			addErrorNode("emptyTree", commandNode, parametersNode);
		} else {
			final Element avlgNode = results.createElement("AvlGTree");
			avlgNode.setAttribute("cardinality",
					String.valueOf(citiesByNameAvlG.size()));
			avlgNode.setAttribute("height",
					String.valueOf(citiesByNameAvlG.getHeight()));
			avlgNode.setAttribute("maxImbalance", String.valueOf(g));
			printAvlTreeHelper(citiesByNameAvlG.getRoot(), avlgNode);

			outputNode.appendChild(avlgNode);
			addSuccessNode(commandNode, parametersNode, outputNode);
		}
	}

	public void processPrintPRQuadtree(final Element node) {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");

		if (prQuadtree.isEmpty()) {
			/* empty PR Quadtree */
			addErrorNode("mapIsEmpty", commandNode, parametersNode);
		} else {
			/* print PR Quadtree */
			final Element quadtreeNode = results.createElement("quadtree");
			printPRQuadtreeHelper(prQuadtree.getRoot(), quadtreeNode);

			outputNode.appendChild(quadtreeNode);

			/* add success node to results */
			addSuccessNode(commandNode, parametersNode, outputNode);
		}
	}

	public void processPrintPMQuadtree(Element node) {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");
		if(node.hasAttribute("id")){
			commandNode.setAttribute("id", node.getAttribute("id"));
		}
		if (pmQuadtree.isEmpty()) {
			addErrorNode("mapIsEmpty", commandNode, parametersNode);
		} else {
			final Element quadtreeNode = results.createElement("quadtree");
			
			quadtreeNode.setAttribute("order", String.valueOf(pmOrder));
			printPMQuadtreeHelper(pmQuadtree.getRoot(), quadtreeNode);

			outputNode.appendChild(quadtreeNode);
			addSuccessNode(commandNode, parametersNode, outputNode);
		}

	}

	public void processMapRoad(Element node) {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");

		final String start = processStringAttribute(node, "start",
				parametersNode);
		final String end = processStringAttribute(node, "end", parametersNode);
		if (node.hasAttribute("id")) {
			commandNode.setAttribute("id", node.getAttribute("id"));
		}

		Element roadCreated = results.createElement("roadCreated");
		Road road;
		roadCreated.setAttribute("end", end);
		roadCreated.setAttribute("start", start);
		
		if(start.compareTo(end) < 0)
			road = new Road(citiesByName.get(end), citiesByName.get(start));
		else 
			road = new Road(citiesByName.get(start), citiesByName.get(end));
		
		outputNode.appendChild(roadCreated);

		if (!citiesByName.containsKey(start)) {
			addErrorNode("startPointDoesNotExist", commandNode, parametersNode);
			return;
		} else if (!citiesByName.containsKey(end)) {
			addErrorNode("endPointDoesNotExist", commandNode, parametersNode);
			return;
		} else if (start.equals(end)) {
			addErrorNode("startEqualsEnd", commandNode, parametersNode);
			return;
		}
		
		
		if (pmQuadtree.getRoads().contains(road)) {
			addErrorNode("roadAlreadyMapped", commandNode, parametersNode);
			return;
		} else if (!Inclusive2DIntersectionVerifier.intersects(road.getLine(),
				pmQuadtree.getSpatialBound())) {
			addErrorNode("roadOutOfBounds", commandNode, parametersNode);
			return;
		} else {
			addSuccessNode(commandNode, parametersNode, outputNode);
		}

		try {
			
			pmQuadtree.add(road);
			final City cStart = road.getStart();
			final City cEnd = road.getEnd();
			canvas.addPoint(cStart.getName(), cStart.getX(), cStart.getY(),
					Color.BLACK);
			canvas.addPoint(cEnd.getName(), cEnd.getX(), cEnd.getY(),
					Color.BLACK);
			canvas.addLine(cStart.getX(), cStart.getY(), cEnd.getX(),
					cEnd.getY(), Color.BLACK);
		} catch (Exception e) {

		}

	}

	private void printAvlTreeHelper(
			cmsc420.sortedmap.Node<String, City> currentNode, Element xmlNode) {
		if (currentNode == null) {
			Element emptyChild = results.createElement("emptyChild");
			xmlNode.appendChild(emptyChild);
		} else {
			final Element node = results.createElement("node");
			node.setAttribute("key", currentNode.getKey().toString());
			node.setAttribute("value", currentNode.getValue().toString());
			printAvlTreeHelper(currentNode.getLeft(), node);
			printAvlTreeHelper(currentNode.getRight(), node);
			xmlNode.appendChild(node);

		}
	}

	/**
	 * Traverses each node of the PR Quadtree.
	 * 
	 * @param currentNode
	 *            PR Quadtree node being printed
	 * @param xmlNode
	 *            XML node representing the current PR Quadtree node
	 */
	private void printPRQuadtreeHelper(final Node currentNode,
			final Element xmlNode) {
		if (currentNode.getType() == Node.EMPTY) {
			Element white = results.createElement("white");
			xmlNode.appendChild(white);
		} else {
			if (currentNode.getType() == Node.LEAF) {
				/* leaf node */
				final LeafNode currentLeaf = (LeafNode) currentNode;
				final Element black = results.createElement("black");
				black.setAttribute("name", currentLeaf.getCity().getName());
				black.setAttribute("x",
						Integer.toString((int) currentLeaf.getCity().getX()));
				black.setAttribute("y",
						Integer.toString((int) currentLeaf.getCity().getY()));
				xmlNode.appendChild(black);
			} else {
				/* internal node */
				final InternalNode currentInternal = (InternalNode) currentNode;
				final Element gray = results.createElement("gray");
				gray.setAttribute("x",
						Integer.toString((int) currentInternal.getCenterX()));
				gray.setAttribute("y",
						Integer.toString((int) currentInternal.getCenterY()));
				for (int i = 0; i < 4; i++) {
					printPRQuadtreeHelper(currentInternal.getChild(i), gray);
				}
				xmlNode.appendChild(gray);
			}
		}
	}

	private void printPMQuadtreeHelper(
			final cmsc420.pmquadtree.PMQuadtree.Node currentNode,
			final Element xmlNode) {
		if (currentNode instanceof WhiteNode) {
			Element white = results.createElement("white");
			xmlNode.appendChild(white);
		} else if (currentNode instanceof BlackNode) {
			final BlackNode currentBlackNode = (BlackNode) currentNode;
			final Element black = results.createElement("black");
			int cardinality = 0;
			if (currentBlackNode.getCity() != null) {
				cardinality++;
				Element city = results.createElement("city");
				City c = currentBlackNode.getCity();
				city.setAttribute("color", c.getColor());
				city.setAttribute("name", c.getName());
				city.setAttribute("radius", String.valueOf(c.getRadius()));
				city.setAttribute("x", String.valueOf(c.getX()));
				city.setAttribute("y", String.valueOf(c.getY()));
				black.appendChild(city);
			}
			for (Road r : currentBlackNode.getRoads()) {
				Element road = results.createElement("road");

				road.setAttribute("end", r.getEnd().getName());
				road.setAttribute("start", r.getStart().getName());

				black.appendChild(road);
				cardinality++;
			}
			black.setAttribute("cardinality", String.valueOf(cardinality));
			xmlNode.appendChild(black);
		} else {
			final GreyNode currentGreyNode = (GreyNode) currentNode;
			final Element gray = results.createElement("gray");
			gray.setAttribute("x", String.valueOf(currentGreyNode.getCenterX()));
			gray.setAttribute("y", String.valueOf(currentGreyNode.getCenterY()));
			for (int i = 0; i < 4; i++) {
				printPMQuadtreeHelper(currentGreyNode.getChildren()[i], gray);
			}
			xmlNode.appendChild(gray);
		}
	}

	public void processRangeCities(final Element node) throws IOException {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");

		if(node.hasAttribute("id")){
			commandNode.setAttribute("id", node.getAttribute("id"));
		}
		
		final TreeSet<City> citiesInRange = new TreeSet<City>(
				new CityNameComparator());

		/* extract values from command */
		final int x = processIntegerAttribute(node, "x", parametersNode);
		final int y = processIntegerAttribute(node, "y", parametersNode);
		final int radius = processIntegerAttribute(node, "radius",
				parametersNode);

		String pathFile = "";
		if (node.getAttribute("saveMap").compareTo("") != 0) {
			pathFile = processStringAttribute(node, "saveMap", parametersNode);
		}

		if (radius == 0) {
			addErrorNode("noCitiesExistInRange", commandNode, parametersNode);
		} else {
			/* get cities within range */
			final Point2D.Double point = new Point2D.Double(x, y);
			rangeCitiesHelper(point, radius, pmQuadtree.getRoot(),
					citiesInRange);

			/* print out cities within range */
			if (citiesInRange.isEmpty()) {
				addErrorNode("noCitiesExistInRange", commandNode,
						parametersNode);
			} else {
				/* get city list */
				final Element cityListNode = results.createElement("cityList");
				for (City city : citiesInRange) {
					addCityNode(cityListNode, city);
				}
				outputNode.appendChild(cityListNode);

				/* add success node to results */
				addSuccessNode(commandNode, parametersNode, outputNode);

				if (pathFile.compareTo("") != 0) {
					/* save canvas to file with range circle */
					canvas.addCircle(x, y, radius, Color.BLUE, false);
					canvas.save(pathFile);
					canvas.removeCircle(x, y, radius, Color.BLUE, false);
				}
			}
		}
	}

	private void rangeCitiesHelper(final Point2D.Double point,
			final int radius, final PMQuadtree.Node node, final TreeSet<City> citiesInRange) {
		if (node instanceof BlackNode) {
			final BlackNode blackNode = (BlackNode) node;
			if(blackNode.getCity() != null){
				final double distance = point.distance(blackNode.getCity().toPoint2D());
				if (distance <= radius) {
					/* city is in range */
					final City city = blackNode.getCity();
					citiesInRange.add(city);
				}
			}
		} else if (node instanceof GreyNode) {
			/* check each quadrant of internal node */
			final GreyNode greyNode = (GreyNode) node;

			final Circle2D.Double circle = new Circle2D.Double(point, radius);
			
			for (int i = 0; i < 4; i++) {
				if (prQuadtree.intersects(circle, greyNode.getRegion(i))) {
					rangeCitiesHelper(point, radius, greyNode.getChildren()[i],
							citiesInRange);
				}
			}
		}
	}

	/**
	 * Finds the nearest city to a given point.
	 * 
	 * @param node
	 *            nearestCity command being processed
	 */
	public void processNearestCity(Element node) {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");
		
		if(node.hasAttribute("id")){
			commandNode.setAttribute("id", node.getAttribute("id"));
		}
		/* extract attribute values from command */
		final int x = processIntegerAttribute(node, "x", parametersNode);
		final int y = processIntegerAttribute(node, "y", parametersNode);

		if (pmQuadtree.isEmpty()) {
			addErrorNode("cityNotFound", commandNode, parametersNode);
			return;
		}
		
		final PriorityQueue<City> cities = new PriorityQueue<City>(citiesByLocation.size(), new CityLocationComparator(new Point2D.Float(x, y)));
		for(City city : pmQuadtree.keySet()){
			cities.add(city);
		}
		addCityNode(outputNode, cities.remove());
		addSuccessNode(commandNode, parametersNode, outputNode);
	}

	class QuadrantDistance implements Comparable<QuadrantDistance> {
		public Node quadtreeNode;
		private double distance;

		public QuadrantDistance(Node node, Point2D.Float pt) {
			quadtreeNode = node;
			if (node.getType() == Node.INTERNAL) {
				InternalNode gray = (InternalNode) node;
				distance = Shape2DDistanceCalculator.distance(pt,
						new Rectangle2D.Float(gray.origin.x, gray.origin.y,
								gray.width, gray.height));
			} else if (node.getType() == Node.LEAF) {
				LeafNode leaf = (LeafNode) node;
				distance = pt.distance(leaf.getCity().pt);
			} else {
				throw new IllegalArgumentException(
						"Only leaf or internal node can be passed in");
			}
		}

		public int compareTo(QuadrantDistance qd) {
			if (distance < qd.distance) {
				return -1;
			} else if (distance > qd.distance) {
				return 1;
			} else {
				if (quadtreeNode.getType() != qd.quadtreeNode.getType()) {
					if (quadtreeNode.getType() == Node.INTERNAL) {
						return -1;
					} else {
						return 1;
					}
				} else if (quadtreeNode.getType() == Node.LEAF) {
					// both are leaves
					return ((LeafNode) quadtreeNode)
							.getCity()
							.getName()
							.compareTo(
									((LeafNode) qd.quadtreeNode).getCity()
											.getName());
				} else {
					// both are internals
					return 0;
				}
			}
		}
	}

	public void processNearestRoad(Element node) {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");
		
		if (node.hasAttribute("id")) {
			commandNode.setAttribute("id", node.getAttribute("id"));
		}
		
		/* extract attribute values from command */
		final int x = processIntegerAttribute(node, "x", parametersNode);
		final int y = processIntegerAttribute(node, "y", parametersNode);

		if (pmQuadtree.getRoads().isEmpty()) {
			addErrorNode("roadNotFound", commandNode, parametersNode);
			return;
		}
		
		final PriorityQueue<Road> roads = new PriorityQueue<Road>(citiesByLocation.size(), new RoadComparator(new Point2D.Float(x, y)));
		for(Road road : pmQuadtree.getRoads()){
			roads.add(road);
		}
		addRoadNode(outputNode, roads.remove());
		addSuccessNode(commandNode, parametersNode, outputNode);
	}

	public void processNearestCityToRoad(final Element node) {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");
		
		if (node.hasAttribute("id")) {
			commandNode.setAttribute("id", node.getAttribute("id"));
		}
		
		final String start = processStringAttribute(node, "start", parametersNode);
		final String end = processStringAttribute(node, "end", parametersNode);
		Road road = pmQuadtree.getRoad(start, end);
		if(road == null){
			addErrorNode("roadIsNotMapped", commandNode,parametersNode);
		} else if (pmQuadtree.getRoads().size() == 1){
			addErrorNode("noOtherCitiesMapped", commandNode,parametersNode);
		} else {
			City c = nearestCityToRoadHelper(road, pmQuadtree.keySet());
			Element city = results.createElement("city");
			city.setAttribute("name", c.name);
			city.setAttribute("radius", String.valueOf(c.getRadius()));
			city.setAttribute("x", String.valueOf(c.getX()));
			city.setAttribute("y", String.valueOf(c.getY()));
			city.setAttribute("color", c.getColor());
			outputNode.appendChild(city);
			addSuccessNode(commandNode, parametersNode, outputNode);
		}
	}


	private City nearestCityToRoadHelper(Road road, TreeSet<City> cities) {
		Line2D line = road.getLine();
		City min = null;
		
		double currentMin = java.lang.Double.POSITIVE_INFINITY;
		for(City city : cities){
			if(road.getStart().equals(city) || road.getEnd().equals(city)){
				continue;
			}
			if(currentMin == java.lang.Double.POSITIVE_INFINITY){
				currentMin = line.ptSegDist(city.toPoint2D());
				min = city;
				continue;
			}
			if(line.ptSegDist(city.toPoint2D()) > currentMin){
				continue;
			} else if(line.ptSegDist(city.toPoint2D()) < currentMin){
				if(min == null){
					currentMin = line.ptSegDist(city.toPoint2D());
					min = city;
					continue;
				}
				currentMin = line.ptSegDist(city.toPoint2D());
				min = city;
			} else {
				if(min.getName().compareTo(city.getName()) > 0){
					currentMin = line.ptSegDist(city.toPoint2D());
					min = city;
				}	
				
			}
		}
		return min;
		
	}

	public void processShortestPath(Element commandNode) {
		// TODO Auto-generated method stub

	}

	public void processRangeRoads(final Element node) throws IOException {
		final Element commandNode = getCommandNode(node);
		final Element parametersNode = results.createElement("parameters");
		final Element outputNode = results.createElement("output");

		if (node.hasAttribute("id")) {
			commandNode.setAttribute("id", node.getAttribute("id"));
		}
		
		final TreeSet<Road> roads = new TreeSet<Road>(new RoadComparator());

		/* extract values from command */
		final int x = processIntegerAttribute(node, "x", parametersNode);
		final int y = processIntegerAttribute(node, "y", parametersNode);
		final int radius = processIntegerAttribute(node, "radius",parametersNode);

		String pathFile = node.getAttribute("saveMap");
		if (node.getAttribute("saveMap").compareTo("") != 0) {
			pathFile = processStringAttribute(node, "saveMap", parametersNode);
		}

		if (radius == 0) {
			addErrorNode("noRoadsExistInRange", commandNode, parametersNode);
		} else {
			/* get cities within range */
			final Point2D.Double point = new Point2D.Double(x, y);
			rangeRoadsHelper(point, radius, pmQuadtree.getRoot(),roads);

			/* print out cities within range */
			if (roads.isEmpty()) {
				addErrorNode("noRoadsExistInRange", commandNode,
						parametersNode);
			} else {
				/* get city list */
				final Element roadListNode = results.createElement("roadList");
				
				for (Road road : roads) {
					addRoadNode(roadListNode, road);
				}
				outputNode.appendChild(roadListNode);
				roads.clear();

				/* add success node to results */
				addSuccessNode(commandNode, parametersNode, outputNode);

				if (pathFile.compareTo("") != 0) {
					/* save canvas to file with range circle */
					canvas.addCircle(x, y, radius, Color.BLUE, false);
					canvas.save(pathFile);
					canvas.removeCircle(x, y, radius, Color.BLUE, false);
				}
			}
		}
	}

	private void addRoadNode(final Element node, final Road road) {
		addRoadNode(node, "road", road);
	}

	private void addRoadNode(Element node, String roadNodeName, Road road) {
		final Element roadNode = results.createElement(roadNodeName);
		roadNode.setAttribute("start", road.getStart().getName());
		roadNode.setAttribute("end", road.getEnd().getName());
		node.appendChild(roadNode);
	}

	private void rangeRoadsHelper(Point2D.Double point, int radius,
			cmsc420.pmquadtree.PMQuadtree.Node root, TreeSet<Road> roads) {
		for(Road road : pmQuadtree.getRoads()){
			if(road.getLine().ptSegDist(point) <= radius){
				roads.add(road);
			}
		}
	}
}
