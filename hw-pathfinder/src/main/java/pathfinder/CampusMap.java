/*
 * Copyright (C) 2021 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2021 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

package pathfinder;

import graph.DirectedLabeledGraph;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import pathfinder.parser.CampusBuilding;
import pathfinder.parser.CampusPath;
import pathfinder.parser.CampusPathsParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The CampusMap class implements the ModelAPI for the view/controller to represent the buildings and paths on the University of Washington campus.
 * CampusMap is a immutable representation of points and paths on the University of Washington campus including specific buildings on campus.
 */
public class CampusMap implements ModelAPI {

    //Abstraction Function: Each of the nodes (Points) in campusMap represents various spots on campus and the edges represent paths between these spots on campus where the label of
    //                      the edges represent the physical distance between the spots. buildingMap's keys are the short names of all of the buildings on campus and those keys
    //                      map to the point on campus where they are at corresponding to the points in campusMap. Finally, nameMap maps each of the building's short names to
    //                      their full names as seen on campus.
    //Representation Invariant: campusMap != null, nameMap != null, buildingMap != null
    //                          for all keys k in nameMap, k is not null and nameMap.get(k) is not null.
    //                          for all keys k in buildingMap, k is not null and buildingMap.get(k) is not null.
    //                          (non-null nodes and edges are implied from DirectedLabeledGraph Rep Inv)

    /**
     * The name of file with building data
     */
    private static final String buildingsFile = "campus_buildings.csv";
    /**
     * The name of file with path data
     */
    private static final String pathsFile = "campus_paths.csv";
    /**
     * Graph of campus with nodes being points on campus connected by edges represented by their distance as a double.
     */
    private DirectedLabeledGraph<Point, Double> campusMap;
    /**
     * Map from a buildings short name to its long name
     */
    private Map<String, String> nameMap;
    /**
     * Map from a campus building short name to the point that building is at
     */
    private Map<String, Point> buildingMap;

    /**
     * Specifies whether or not full checkRep is run when called
     */
    private static final boolean DEBUG = false;

    /** Initializes the model of the University of Washington campus to be used by a view/controller
     *
     * @spec.effects Creates a CampusMap object from the provided text files initializing the model of the campus map to be used by the view/controller
     * @throws CampusPathsParser.ParserException if the files cannot be found or parsed as expected
     */
    public CampusMap(){
        campusMap = new DirectedLabeledGraph<>();
        buildingMap = new HashMap<>();
        nameMap = new HashMap<>();

        List<CampusBuilding> buildings = CampusPathsParser.parseCampusBuildings(buildingsFile);
        List<CampusPath> paths = CampusPathsParser.parseCampusPaths(pathsFile);
        for(CampusPath path : paths){
            Point source = new Point(path.getX1(), path.getY1());
            Point dest = new Point(path.getX2(), path.getY2());
            campusMap.addEdge(path.getDistance(), new DirectedLabeledGraph.Node<>(source), new DirectedLabeledGraph.Node<>(dest));
        }

        for(CampusBuilding building : buildings){
            nameMap.put(building.getShortName(), building.getLongName());
            buildingMap.put(building.getShortName(), new Point(building.getX(),building.getY()));
        }
        checkRep();
    }

    @Override
    public boolean shortNameExists(String shortName) {
        return nameMap.containsKey(shortName);
    }

    @Override
    public String longNameForShort(String shortName) {
        if(!shortNameExists(shortName)){
            throw new IllegalArgumentException();
        }
        return nameMap.get(shortName);
    }

    @Override
    public Map<String, String> buildingNames() {
        return new HashMap<>(nameMap);
    }

    @Override
    public Path<Point> findShortestPath(String startShortName, String endShortName) {
        if(startShortName == null || endShortName == null || !shortNameExists(startShortName) || !shortNameExists(endShortName)){
            throw new IllegalArgumentException();
        }
        return CampusPaths.findPath(buildingMap.get(startShortName), buildingMap.get(endShortName), campusMap);
    }

    /**
     * Throws an exception if the representation invariant is violated
     */
    private void checkRep(){
        assert nameMap != null : "nameMap is null";
        assert buildingMap != null : "buildingMap is null";
        assert campusMap != null : "campusMap is null";

        if(DEBUG){
            for(String name : nameMap.keySet()){
                assert name != null : "a short name in name map is null";
                assert nameMap.get(name) != null : "a long name in name map is null";
            }
            for(String name : buildingMap.keySet()){
                assert name != null : "a short name in building map is null";
                assert buildingMap.get(name) != null : "a building's point is null in building map";
            }
        }
    }
}
