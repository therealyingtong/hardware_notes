"use strict";
exports.__esModule = true;
var path = require("path");
// set NODE_CONFIG_DIR 
if (!process.env.hasOwnProperty('NODE_CONFIG_DIR')) {
    process.env.NODE_CONFIG_DIR = path.join(__dirname, '../');
}
if (!process.env.hasOwnProperty('NODE_ENV')) {
    process.env.NODE_ENV = 'local-dev';
}
var config = require('config');
exports.config = config;
//# sourceMappingURL=index.js.map