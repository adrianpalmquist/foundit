/*
const http = require('http');

require("jsdom").env("", function(err, window) {
  if (err) {
      console.error(err);
      return;
  }

  var $ = require("jquery")(window);
});

http.createServer((request, response) => {
  console.log('Request received');

  const { headers, method, url } = request;
  let body = [];
  request.on('error', (err) => {
    console.error(err);
  }).on('data', (chunk) => {
    body.push(chunk);
  }).on('end', () => {
    body = Buffer.concat(body).toString();

    
    //Response
    response.on('error', (err) => {
      console.error(err);
    });

    response.statusCode = 200;
    response.setHeader('Content-Type', 'application/json');

    const responseBody = { headers, method, url, body };

    response.write('response');
    response.end();

    // END OF NEW STUFF
  });
}).listen(8080);
*/

//test json object (database)
var database = {
  
    "id" : "id-001",
    "name" : "david",
    "location" : {
      "lat" : "20304",
      "lon" : "44569"
    },
    "mail" : "jsjsj@sksk.com",
    "phone" : "0456797959",
  
    "id" : "id-002",
    "name" : "jesper",
    "location" : {
      "lat" : "20804",
      "lon" : "44539"
    },
    "mail" : "aaa@sksk.com",
    "phone" : "0050797959" 
  
}


var http = require('http');

function onRequest(req,res){
    var data = "";
    var parsedData;
    console.log('request received');
    
    req.on('data', function (chunk) {
        data += chunk;
        parsedData = JSON.parse(data);

        console.log("requested id: " + parsedData.id);

        /**
         * Read NFC Tag (look up in db)
         */
        
        if( parsedData.id == "1" ){
          res.writeHead(200, {'Content-Type': 'application/json'});
          console.log("user id match found");
          res.end(JSON.stringify(parsedData));
        } else {
          res.writeHead(200, {'Content-Type': 'text/plain'});
          res.end("id not found");
        }
                  
        /*  
        if(database.hasOwnProperty(parsedData.id)){
            res.writeHead(200, {'Content-Type': 'application/json'});
            console.log("user id match found");
            res.end(parsedData);
        } else {
            res.writeHead(200, {'Content-Type': 'text/plain'});
            res.end("id not found");
        }*/
        

    });
 
}

var server = http.createServer(onRequest).listen(8000);