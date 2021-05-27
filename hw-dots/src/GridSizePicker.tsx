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

/* A simple TextField that only allows numerical input */

import React, {Component} from 'react';

interface GridSizePickerProps {
    onChange(newSize: number): void;  // called when a new size is picked
}

interface GridSizePickerState{
    value:string; //The text that is actually displayed in the GridSizePicker box.
}


class GridSizePicker extends Component<GridSizePickerProps, GridSizePickerState> {

    constructor(props: GridSizePickerProps) {
        super(props);
        this.state = {
            value:"4"
        };
    }

    onInputChange = (event: React.ChangeEvent<HTMLInputElement>)  : void => {
        // Every event handler with JS can optionally take a single parameter that
        // is an "event" object - contains information about an event. For mouse clicks,
        // it'll tell you thinks like what x/y coordinates the click was at. For text
        // box updates, it'll tell you the new contents of the text box, like we're using
        // below.
        //
        const newSize: number = parseInt(event.target.value);
        if(newSize > 400){
            alert("Grid size too large, please choose a grid size less than 400");
            this.setState({
                value: ""
            })
            this.props.onChange(0);
        }else if(isNaN(newSize)){
            this.setState({
                value: ""
            })
            this.props.onChange(0);
        }else if(newSize < 0){
            alert("Negative grid size, please input a valid grid size");
            this.setState({
                value: ""
            })
            this.props.onChange(0);
        } else {
            this.setState({
                value: newSize.toString()
            });
            this.props.onChange(newSize); // Tell our parent component about the new size.
        }
    };

    render() {
        return (
            <div id="grid-size-picker">
                <label>
                    Grid Size:
                    <input
                        value={this.state.value}
                        onChange={this.onInputChange}
                        type="number"
                        min={1}
                        max={100}
                    />
                </label>
            </div>
        );
    }
}

export default GridSizePicker;
