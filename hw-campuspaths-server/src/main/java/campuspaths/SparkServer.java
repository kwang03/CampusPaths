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

package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import spark.Spark;
import pathfinder.CampusMap;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        // You should leave these two lines at the very beginning of main().

        // TODO: Create all the Spark Java routes you need here.

        CampusMap map = new CampusMap();
        Gson g = new Gson();

        Spark.get("/path", (req, res) -> {
            String start = req.queryParams("start");
            String end = req.queryParams("end");
            if(start == null ){
                res.status(400);
                return "start building missing";
            }else if(end == null){
                res.status(400);
                return "end building missing";
            } else if(start.equals("") || end.equals("")){
                res.status(201);
                return "default";
            }else if(!map.shortNameExists(start)){
                res.status(400);
                return "start building does not exist";
            } else if(!map.shortNameExists(end)){
                res.status(400);
                return "end building does not exist";
            }
            return g.toJson(map.findShortestPath(start, end));
        });

        Spark.get("/buildings", (req,res) -> g.toJson(map.buildingNames()));
    }

}
