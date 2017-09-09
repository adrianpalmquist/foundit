//Load dependencies
var http = require('http');

//test json object (database)
var database = [
  {
    "id" : "id-001",
    "name" : "david",
    "location" : {
      "lat" : "20304",
      "lon" : "44569"
    },
    "mail" : "jsjsj@sksk.com",
    "phone" : "0456797959",
  },

  {
    "id" : "id-002",
    "name" : "jesper",
    "location" : {
      "lat" : "20804",
      "lon" : "44539"
    },
    "mail" : "aaa@sksk.com",
    "phone" : "0050797959" 
  }

]

function onRequest(req,res){
    var data = "";
    var parsedData;
    console.log('request received');
    
    req.on('data', function ( chunk ) {
        data += chunk;
        parsedData = JSON.parse( data );

        console.log("Request detected!");

        if( parsedData.name == undefined ) {
          lookUpTag( parsedData, res );
        } else {
          addTag( parsedData, res );
        }
                 
    });
 
}

//Read NFC Tag (look up in db) ---------------------------------------
function lookUpTag( parsedData, res ) {
  console.log("Look-up by id: " + parsedData.id);
  //Loop through database object
  for( key in database ) {
    for( field in database[key] ) {
        //console.log(field + ": " + database[key][field])
      
        if( database[key][field] == parsedData.id ) {
          //if id found, return object belonging to the id
          console.log( "user id match found" );
          res.writeHead( 200, {'Content-Type': 'application/json'} );
          res.end( JSON.stringify( database[key] ) );
        } else {
          res.writeHead(200, {'Content-Type': 'text/plain'});
          res.end( "id not found" );
        }
    }
  }

}

//Add NFC Tag to database ------------------------------------------
function addTag( parsedData, res ) {
  database.push( parsedData );
  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end("Tag added to database: " + JSON.stringify(parsedData));
  console.log(database);
}

var server = http.createServer(onRequest).listen(8000);