const express = require('express');
const app = express();
const path = require('path');

process.on('SIGINT', () => {
    console.log("\nGracefully shutting down from SIGINT");
    process.exit(1);
});

app.enable('strict routing');
app.use(function (req, res, next) {
    console.log("Loading request for: /" + path.basename(req.url));
    next();
});
app.use(express.static('/www', {redirect: false}));
app.get('/*', (req, res) => {
    res.sendFile('/www/index.html');
});

console.log('Starting express server on port 3000');
app.listen(3000);