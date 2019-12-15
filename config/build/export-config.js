"use strict";
exports.__esModule = true;
var index_1 = require("./index");
if (require.main === module) {
    var c = JSON.parse(JSON.stringify(index_1.config));
    if (c.chain.keys) {
        delete c.chain.keys;
    }
    console.log(JSON.stringify(c));
}
//# sourceMappingURL=export-config.js.map