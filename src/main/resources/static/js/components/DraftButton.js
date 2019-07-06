import $ from "jquery";

export default class DraftButton extends React.Component {
    constructor(props) {
        super(props);
        this.handleDraftCreation = this.handleDraftCreation.bind(this);
        this.state = {isDraftCreated: false};
    }

    handleDraftCreation() {
        this.setState({isDraftCreated: true});
        $.ajax(window.location.origin + '/draft/create', {method: "GET", data: {playerCount: 2}}).done(function(data) {
            console.log(data);
        });
    }

    render() {
        const isDraftPresent = this.state.isDraftCreated;
        let button;
        if (isDraftPresent) {
            button = <CreateDraftButton text={"Draft Created!"} />
        } else {
            button = <CreateDraftButton onClick={this.handleDraftCreation} text={"Start Draft"} />
        }

        return (
            <div>{button}</div>
        )
    }
}

function CreateDraftButton(props) {
    return (
        <button type="button" className="btn btn-primary" onClick={props.onClick}>
            {props.text}
        </button>
    );
}