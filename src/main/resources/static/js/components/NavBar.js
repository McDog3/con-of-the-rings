export default class NavBar extends React.Component {
    constructor(props) {
        super(props);
        this.isLoggedIn = props.isLoggedIn;
    }

    render() {
        return (
            <nav className="navbar navbar-expand-md navbar-light bg-light fixed-top">
                <a href="/home">COTR LOGO</a>
                <button className="navbar-toggler ml-auto" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <NavList isLoggedIn={this.isLoggedIn} />
                <div className="mr-5"></div>
            </nav>
        );
    }
}

function NavLink(props) {
    //TODO: make 'active' be passed in
    return (
        <li className="nav-item active">
            <a className="nav-link" href={props.href} onClick={props.onClick}>{props.title}</a>
        </li>
    );
}

function NavDropdownMenu(props) {
    const isLoggedIn = props.isLoggedIn;
    if (isLoggedIn) {
        return (
            <li className="nav-item dropdown">
                <a className="nav-link dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-haspopup="true"
                   aria-expanded="false">Account</a>
                <div className="dropdown-menu">
                    <NavDropdownMenuLink href={"/account"} title={"Profile"} />
                    <NavDropdownMenuLink href={"#"} title={"Purchases"} />
                    <div className="dropdown-divider"></div>
                    <NavDropdownMenuLink href={"/logout"} title={"Logout"} onClick={props.onClick}/>
                </div>
            </li>
        );
    } else {
        return (
            <NavLink href={"/login"} title={"Login"} onClick={props.onClick}/>
        );
    }
}

function NavDropdownMenuLink(props) {
    return (
        <a className="dropdown-item" href={props.href} onClick={props.onClick}>{props.title}</a>
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
                    <ul className="navbar-nav ml-auto mr-4">
                        <NavLink href={"/home"} title={"Home"}/>
                        <NavLink href={"#"} title={"Info"}/>
                        <NavLink href={"#"} title={"Gallery"}/>
                        <NavLink href={"#"} title={"Attend"}/>
                        <NavDropdownMenu isLoggedIn={isLoggedIn} onClick={this.handleLogoutClick}/>
                    </ul>
                </div>
            );
        } else {
            return (
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav ml-auto mr-4">
                        <NavLink href={"/home"} title={"Home"}/>
                        <NavLink href={"#"} title={"Info"}/>
                        <NavLink href={"#"} title={"Gallery"}/>
                        <NavLink href={"#"} title={"Attend"}/>
                        <NavDropdownMenu isLoggedIn={isLoggedIn} onClick={this.handleLoginClick}/>
                    </ul>
                </div>
            );
        }
    }
}