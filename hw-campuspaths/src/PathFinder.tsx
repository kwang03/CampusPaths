import React, {Component} from 'react';

interface PathFinderState{
    start:string; //The current selected start building
    end:string; //The current selected end building
    options:any; //The JSON object representing the short and long names of the buildings
    shortNames:string[]; //The short names of all of the buildings
}

interface PathFinderProps{
    onChange(start: string, end:string): void;
}

/**
 * PathFinder is the component consisting of the two dropdown menus to select start and end buildings in addition to the two buttons 'draw' and 'clear'
 */
class PathFinder extends Component<PathFinderProps, PathFinderState> {
    constructor(props: PathFinderProps) {
        super(props);
        this.state = {
            shortNames:[],
            options:[],
            start:"",
            end:""
        };
        this.makeRequest();
    }

    //Sets state to the default state when clear is clicked
    onClear = () => {
        this.setState({
            start:"",
            end:""
        });
        this.props.onChange("","");
    }

    //Makes request to Spark server, fetching the data about the building names
    makeRequest = async () => {
        try {
            let response = await fetch("http://localhost:4567/buildings");
            if (!response.ok) {
                alert("The status is wrong! Expected: 200, Was: " + response.status);
                return;
            }
            let buildings = await response.json();
            this.setState({
                options:buildings,
                shortNames:Object.keys(buildings),
            })
        } catch (e) {
            alert("There was an error contacting the server.");
            console.log(e);
        }
    };

    //Changes this components state when a different start building is chosen
    handleStartChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        this.setState({
            start:event.target.value,
        });
    }
    //Changes this components state when a different end building is chosen
    handleEndChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
        this.setState({
            end:event.target.value,
        });
    }

    render(){
        return (
            <div>
                <select onChange={this.handleStartChange}>
                    <option value="">-----{"Select Start Building"}-----</option>
                    {this.state.shortNames.map((building) => <option value={building}>{this.state.options[building]}</option>)}
                </select>
                <select onChange={this.handleEndChange}>
                    <option value="">-----{"Select End Building"}-----</option>
                    {this.state.shortNames.map((building) => <option value={building}>{this.state.options[building]}</option>)}
                </select>
                <button onClick={() => this.props.onChange(this.state.start, this.state.end)}>
                    Find Path
                </button>
                <button onClick={() => this.onClear()}>
                    Clear
                </button>
            </div>
        )
    }
}



export default PathFinder;