#Prerequisites
* An IDE, preferably [IntelliJ](https://www.jetbrains.com/idea/)
* [Node.js](https://nodejs.org/en/)
* [git](https://git-scm.com/)
* Currently, a compilation tool such as [Koala](http://koala-app.com/) is needed to compile SASS files. This will change and is only necessary if you plan to work with css.
* [Docker](https://www.docker.com/) for local MySQL development

# Setup

* Clone repo to a directory of your choice
* Import project into IntelliJ by selecting the `build.gradle` file within the project. IntelliJ should handle creating a local version of Gradle to handle the project.
* With Node installed, we can use npm to grab our front-end dependencies (npm modules). From the project root (`/con-of-the-rings`) do the following commands to install Webpack, Babel, Jquery (may eventually add React - instead of importing on each page).
```
npm install --save-dev @babel/core @babel/cli @babel/preset-env @babel/preset-react
npm install --save-dev webpack webpack-cli
npm install --save-dev jquery
npm install --save-dev babel-loader
```

> Note: The project contains webpack and babel configs, after the above modules are installed you should be able to run `npx webpack --mode=development` from project root to bundle the js files. After running the command, check `con-of-the-rings/src/main/resources/static/js/dist` to verify it contains packaged js files. 

* IntelliJ has a FileWatcher plugin, install that and set up a watcher to package the js automagically (TODO: set this up for sass)
  * TODO: add image to display the watcher settings
* To set up local MySQL development (which is done through Docker), open terminal and run the following commands
```
> docker pull mysql/mysql-server #This pulls the mysql docker image
> docker run -p 3306:3306 --name con-mysql -e MYSQL_ROOT_PASSWORD=some_password -d mysql:latest #This creates a container from the mysql image, set some_password to whatever you want
> docker run -it --link con-mysql:mysql --rm mysql sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"' #This executes the mysql command line from within the container that is running from the last command
```
* After the third command, you'll need to seed the database data through the mysql CLI - TODO: Learn how to create Dockerfiles and export images so there is no manual data entry for other developers.

### Setup TODO

* Configure Database - _in progress_
* Add auto compilation for SASS files?


### Application TODO

* Create navigation structure to site
  * Global header / footer
* Build login/signup process
  * Add database / define table structure
  * Create user roles for authorization
* Build out gallery page for previous con images/stories
* Add styling to pages (using Bootstrap for responsiveness)
* Additional site needs:
  * Research hosting options - AWS Lighthouse may be enough?
  * Research usage of ti.to for ticketing: https://ti.to/docs/integrations
  * Research building out of an email subscription service
