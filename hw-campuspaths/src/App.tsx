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

import React, {Component} from 'react';
import MapView from "./MapView";
import PathFinder from "./PathFinder";

export interface Path{
    cost:number;
    start:Point;
    path:Segment[];
}

interface Point{
    x:number;
    y:number;
}

interface Segment{
    start:Point;
    end:Point;
    cost:number;
}

interface AppState {
    pathStart: string;  // Start of displayed path
    pathEnd:string; //End of displayed path
    path: Path | undefined;
}



class App extends Component<{}, AppState> {

    constructor(props: any) {
        super(props);
        this.state = {
            pathStart:"",
            pathEnd:"",
            path:undefined
        };
    }

    //Callback method for when the draw button is clicked
    onDraw = (start:string, end:string): void => {
        this.setState({
            pathStart:start,
            pathEnd:end,
        })
        this.makeRequest();
    }

    makeRequest = async () => {
        try {
            let response = await fetch("http://localhost:4567/path?start=".concat(this.state.pathStart, "&end=", this.state.pathEnd));
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return;
            }
            let path;
            if(response.status === 201){
                path = undefined;
            }else{
                path = await response.json() as Path;
            }
            this.setState({
                path: path
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    render() {
        return (
            <div>
                <PathFinder onChange = {this.onDraw}/>
                <MapView path={this.state.path}/>
            </div>
        );
    }

}

export default App;
