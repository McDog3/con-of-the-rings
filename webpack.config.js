const path = require("path");
const jsDirectory = "./src/main/resources/static/js";

module.exports = {
    entry: {likeButton: jsDirectory + '/like_button.js'},
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /(node_modules|bower_components|dist)/,
                loader: "babel-loader",
                options: { presets: ["@babel/env"] }
            },
            {
                test: /\.css$/,
                use: ["style-loader", "css-loader"]
            }
        ]
    },
    resolve: {
        // extensions: [".js"]
    },
    output: {
        path: path.resolve(jsDirectory, "dist"),
        filename: "[name].js"
    }
};