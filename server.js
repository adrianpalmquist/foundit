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

var http = require('http');
var counter = 0;

function onRequest(req,res){
    console.log('request received');
    counter++;

    var returnObject = {"data" : counter};
    var returnObjectString = JSON.stringify(returnObject);
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('_testcb(\'returnObjectString\')');
    //'_testcb(\'{"message": "Hello world!"}\')'
}

var server = http.createServer(onRequest).listen(8000);