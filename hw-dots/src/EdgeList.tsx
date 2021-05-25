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

interface EdgeListProps {
    onChange(edges: any): void;  // called when a new edge list is ready
                                 // once you decide how you want to communicate the edges to the App, you should
                                 // change the type of edges so it isn't `any`
}

interface EdgeListState{
    value:string;
}

/**
 * A text field that allows the user to enter the list of edges.
 * Also contains the buttons that the user will use to interact with the app.
 */
class EdgeList extends Component<EdgeListProps,EdgeListState> {

    constructor(props: EdgeListProps) {
        super(props);
        this.state = {
            value:"",
        };
    }

    createList = () => {
        let finalArray: string[][] = [];
        let lines = this.state.value.split('\n');
        for(let i = 0; i < lines.length; i++){
            let line = lines[i];
            let components: string[] = line.split(" ");
            if(components.length !== 3){
                alert("Invalid line specifications");
                break;
            }
            finalArray.push(components);
        }
        return finalArray;
    }

    onInputChange = (event: React.ChangeEvent<HTMLTextAreaElement>) => {
        this.setState({
                value:event.target.value,
            }
        )
    };

    render() {
        return (
            <div id="edge-list">
                Edges <br/>
                <textarea
                    rows={5}
                    cols={30}
                    onChange={this.onInputChange}
                    value={this.state.value}
                /> <br/>
                <button onClick={() => {this.props.onChange(this.createList())}}>Draw</button>
                <button onClick={() => {this.props.onChange([])}}>Clear</button>
            </div>
        );
    }
}

export default EdgeList;
