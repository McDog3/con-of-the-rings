const path = require("path");
const jsDirectory = "./src/main/resources/static/js";

module.exports = {
    entry: {home: jsDirectory + '/home.js'},
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
        alias: {
            '@components': path.join(__dirname, './src/main/resources/static/js/components')
        }
    },
    output: {
        path: path.resolve(jsDirectory, "dist"),
        filename: "[name].js"
    }
};