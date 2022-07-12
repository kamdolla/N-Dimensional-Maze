const express   = require('express');
const app       = express();
const PORT      = 8080;

let java = require('java');
java.classpath.push('Main.jar');


app.use(express.json())

app.listen(
    PORT,
    () => console.log(`Listening on http://localhost:${PORT}...`)
)

app.get('/maze', (req, res) => {
    const { dimension } = req.body;
    const { size }      = req.body;
    
    let mazeObj = java.newInstanceSync("Maze", dimension, size);

    let mazeDat = java.callMethodSync(mazeObj, "create");
    let mazeSol = java.callMethodSync(mazeObj, "solve");

    let numberOfNodes       = java.callMethodSync(mazeObj, "getNumNodes");
    let numberOfNodeWalls   = java.callMethodSync(mazeObj, "getNumNodeWalls");

    res.status(200).send({
        dimension : dimension,
        size : size,
        numberOfNodes : numberOfNodes,
        numberOfNodeWalls: numberOfNodeWalls,
        mazeDat: mazeDat,
        mazeSol: mazeSol
    })
});