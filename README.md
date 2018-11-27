# Setup

* Clone repo
* Import project into IntelliJ by selecting the `build.gradle` file within the project. IntelliJ should handle creating a local version of Gradle to handle the project.
* Currently, a compilation tool such as [Koala](http://koala-app.com/) is needed to compile the SASS files
Install Node.js, use npm commands to get packages we need: 

`npm install --save-dev @babel/core @babel/cli @babel/preset-env @babel/preset-react`
`npm install --save-dev webpack webpack-cli`
`npm install --save-dev jquery`
`npm install --save-dev babel-loader`

Afterwards, you can compile the modules using `npx webpack --mode=development`


### Project setup TODO

* Add Spring Data to project
* Add front end framework? React or Elm are among possibilities.
  * Webpack and Babel?
* Add auto compilation for SASS files?


### Application TODO

* Create navigation structure to site
  * Global header / footer
* Build login/signup process
  * Add database
  * Create user roles for authorization
* Build out gallery page for previous con images/stories
* Add styling to pages (using Bootstrap for responsiveness)
* Additional site needs:
  * Research hosting options - AWS Lighthouse may be enough?
  * Research usage of ti.to for ticketing: https://ti.to/docs/integrations
  * Research building out of an email subscription service
