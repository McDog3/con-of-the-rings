#Prerequisites
* An IDE, preferably [IntelliJ](https://www.jetbrains.com/idea/)
* [Node.js](https://nodejs.org/en/)
* [git](https://git-scm.com/)
* Currently, a compilation tool such as [Koala](http://koala-app.com/) is needed to compile SASS files. This will change and is only necessary if you plan to work with css.

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


### Setup TODO

* Add Spring Data to project
* Configure Database
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
