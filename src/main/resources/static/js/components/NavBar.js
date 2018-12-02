export default class NavBar extends React.Component {
    constructor(props) {
        super(props);
        this.isLoggedIn = props.isLoggedIn;
    }

    render() {
        return (
            <nav className="navbar navbar-expand-lg navbar-light bg-light">
                <a href="/home">COTR LOGO</a>
                <button className="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <NavList isLoggedIn={this.isLoggedIn} />
            </nav>
        );
    }

}

function NavLink(props) {
    return (
        <li className="nav-item active">
            <a className="nav-link" href={props.href} onClick={props.onClick}>{props.title}</a>
        </li>
    );
}

class NavList extends React.Component {
    constructor(props) {
        super(props);
        this.handleLoginClick = this.handleLoginClick.bind(this);
        this.handleLogoutClick = this.handleLogoutClick.bind(this);
        this.state = {isLoggedIn: props.isLoggedIn};
    }

    handleLoginClick() {
        this.setState({isLoggedIn: true});
    }

    handleLogoutClick() {
        this.setState({isLoggedIn: false});
    }

    render() {
        const isLoggedIn = this.state.isLoggedIn;
        if (isLoggedIn) {
            return (
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav ml-auto">
                        <NavLink href={"/home"} title={"Home"}/>
                        <NavLink href={"#"} title={"Info"}/>
                        <NavLink href={"#"} title={"Gallery"}/>
                        <NavLink href={"#"} title={"Profile"}/>
                        <NavLink href={"#"} title={"Logout"} onClick={this.handleLogoutClick}/>
                    </ul>
                </div>
            );
        } else {
            return (
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav ml-auto">
                        <NavLink href={"/home"} title={"Home"}/>
                        <NavLink href={"#"} title={"Info"}/>
                        <NavLink href={"#"} title={"Gallery"}/>
                        <NavLink href={"#"} title={"Signup/Login"} onClick={this.handleLoginClick}/>
                    </ul>
                </div>
            );
        }
    }
}