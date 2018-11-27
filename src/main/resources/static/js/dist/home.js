/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, { enumerable: true, get: getter });
/******/ 		}
/******/ 	};
/******/
/******/ 	// define __esModule on exports
/******/ 	__webpack_require__.r = function(exports) {
/******/ 		if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 			Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 		}
/******/ 		Object.defineProperty(exports, '__esModule', { value: true });
/******/ 	};
/******/
/******/ 	// create a fake namespace object
/******/ 	// mode & 1: value is a module id, require it
/******/ 	// mode & 2: merge all properties of value into the ns
/******/ 	// mode & 4: return value when already ns object
/******/ 	// mode & 8|1: behave like require
/******/ 	__webpack_require__.t = function(value, mode) {
/******/ 		if(mode & 1) value = __webpack_require__(value);
/******/ 		if(mode & 8) return value;
/******/ 		if((mode & 4) && typeof value === 'object' && value && value.__esModule) return value;
/******/ 		var ns = Object.create(null);
/******/ 		__webpack_require__.r(ns);
/******/ 		Object.defineProperty(ns, 'default', { enumerable: true, value: value });
/******/ 		if(mode & 2 && typeof value != 'string') for(var key in value) __webpack_require__.d(ns, key, function(key) { return value[key]; }.bind(null, key));
/******/ 		return ns;
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = "./src/main/resources/static/js/home.js");
/******/ })
/************************************************************************/
/******/ ({

/***/ "./src/main/resources/static/js/components/LoginControl.js":
/*!*****************************************************************!*\
  !*** ./src/main/resources/static/js/components/LoginControl.js ***!
  \*****************************************************************/
/*! exports provided: default */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, \"default\", function() { return LoginControl; });\nfunction _typeof(obj) { if (typeof Symbol === \"function\" && typeof Symbol.iterator === \"symbol\") { _typeof = function _typeof(obj) { return typeof obj; }; } else { _typeof = function _typeof(obj) { return obj && typeof Symbol === \"function\" && obj.constructor === Symbol && obj !== Symbol.prototype ? \"symbol\" : typeof obj; }; } return _typeof(obj); }\n\nfunction _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError(\"Cannot call a class as a function\"); } }\n\nfunction _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if (\"value\" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }\n\nfunction _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }\n\nfunction _possibleConstructorReturn(self, call) { if (call && (_typeof(call) === \"object\" || typeof call === \"function\")) { return call; } return _assertThisInitialized(self); }\n\nfunction _getPrototypeOf(o) { _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) { return o.__proto__ || Object.getPrototypeOf(o); }; return _getPrototypeOf(o); }\n\nfunction _inherits(subClass, superClass) { if (typeof superClass !== \"function\" && superClass !== null) { throw new TypeError(\"Super expression must either be null or a function\"); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, writable: true, configurable: true } }); if (superClass) _setPrototypeOf(subClass, superClass); }\n\nfunction _setPrototypeOf(o, p) { _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) { o.__proto__ = p; return o; }; return _setPrototypeOf(o, p); }\n\nfunction _assertThisInitialized(self) { if (self === void 0) { throw new ReferenceError(\"this hasn't been initialised - super() hasn't been called\"); } return self; }\n\nvar LoginControl =\n/*#__PURE__*/\nfunction (_React$Component) {\n  _inherits(LoginControl, _React$Component);\n\n  function LoginControl(props) {\n    var _this;\n\n    _classCallCheck(this, LoginControl);\n\n    _this = _possibleConstructorReturn(this, _getPrototypeOf(LoginControl).call(this, props));\n    _this.handleLoginClick = _this.handleLoginClick.bind(_assertThisInitialized(_assertThisInitialized(_this)));\n    _this.handleLogoutClick = _this.handleLogoutClick.bind(_assertThisInitialized(_assertThisInitialized(_this)));\n    _this.state = {\n      isLoggedIn: false\n    };\n    return _this;\n  }\n\n  _createClass(LoginControl, [{\n    key: \"handleLoginClick\",\n    value: function handleLoginClick() {\n      this.setState({\n        isLoggedIn: true\n      });\n    }\n  }, {\n    key: \"handleLogoutClick\",\n    value: function handleLogoutClick() {\n      this.setState({\n        isLoggedIn: false\n      });\n    }\n  }, {\n    key: \"render\",\n    value: function render() {\n      var isLoggedIn = this.state.isLoggedIn;\n      var button;\n\n      if (isLoggedIn) {\n        button = React.createElement(LogoutButton, {\n          onClick: this.handleLogoutClick\n        });\n      } else {\n        button = React.createElement(LoginButton, {\n          onClick: this.handleLoginClick\n        });\n      }\n\n      return React.createElement(\"div\", null, React.createElement(Greeting, {\n        isLoggedIn: isLoggedIn\n      }), button);\n    }\n  }]);\n\n  return LoginControl;\n}(React.Component);\n\n\n\nfunction LoginButton(props) {\n  return React.createElement(\"button\", {\n    onClick: props.onClick\n  }, \"Login\");\n}\n\nfunction LogoutButton(props) {\n  return React.createElement(\"button\", {\n    onClick: props.onClick\n  }, \"Logout\");\n}\n\nfunction UserGreeting(props) {\n  return React.createElement(\"p\", null, \"Welcome back!\");\n}\n\nfunction GuestGreeting(props) {\n  return React.createElement(\"p\", null, \"Please sign up.\");\n}\n\nfunction Greeting(props) {\n  var isLoggedIn = props.isLoggedIn;\n\n  if (isLoggedIn) {\n    return React.createElement(UserGreeting, null);\n  }\n\n  return React.createElement(GuestGreeting, null);\n}\n\n//# sourceURL=webpack:///./src/main/resources/static/js/components/LoginControl.js?");

/***/ }),

/***/ "./src/main/resources/static/js/home.js":
/*!**********************************************!*\
  !*** ./src/main/resources/static/js/home.js ***!
  \**********************************************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
eval("__webpack_require__.r(__webpack_exports__);\n/* harmony import */ var _components_LoginControl__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @components/LoginControl */ \"./src/main/resources/static/js/components/LoginControl.js\");\n\nReactDOM.render(React.createElement(_components_LoginControl__WEBPACK_IMPORTED_MODULE_0__[\"default\"], null), document.getElementById('login_container'));\n\n//# sourceURL=webpack:///./src/main/resources/static/js/home.js?");

/***/ })

/******/ });