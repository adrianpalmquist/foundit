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

        console.log("Request detected! Requested id: " + parsedData.id);

        //Read NFC Tag (look up in db) ---------------------------------------
 
        //Loop through database object
        for( key in database ) {
          for( field in database[key] ) {
              //console.log(field + ": " + database[key][field])
            
              if( database[key][field] == parsedData.id ) {
                //if id found, return object belonging to the id
                res.writeHead( 200, {'Content-Type': 'application/json'} );
                console.log( "user id match found" );
                res.end( JSON.stringify( database[key] ) );
              } else {
                res.writeHead(200, {'Content-Type': 'text/plain'});
                res.end( "id not found" );
              }
          }
        }

        //--------------------------------------------------------------------
                  
    });
 
}

var server = http.createServer(onRequest).listen(8000);