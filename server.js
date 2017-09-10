//Load dependencies
const express = require('express')
const app = express()
var pg = require('pg')
var http = require('http');
var https = require('https');
var querystring = require('querystring');
// Authentication to 46elks -----------------------------------------------
var username = 'uce2edbc8eb5345786bfbbc50d6677417';
var password = 'EEFAD56AB0A6EC5508CC27296AC9B9B9';
var postFields = {
  from:    "Foundit", 
  to:      "+46768970887", 
  message: "ID search detected"
  }

var key = new Buffer(username + ':' + password).toString('base64');
var postData = querystring.stringify(postFields);

var options = {
  hostname: 'api.46elks.com',
  path:     '/a1/SMS',
  method:   'POST',
  headers:  {
    'Authorization': 'Basic ' + key
    }
  };

var format = require('pg-format')
var PGUSER = 'postgres'
var PGDATABASE = 'foundit'
var config = {
    user: 'postgres',
    password: 'foundit',
    database: 'foundit',
    max: 10,
    idleTimeoutMillis: 30000
}
var pool = new pg.Pool(config);
var myClient

//Test json object (database) ---------------------------------------------
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

    

    console.log("Request detected!");
    
    req.on('data', function ( chunk ) {
        data += chunk;
        parsedData = JSON.parse( data );
	var id = parsedData.id
	
	pool.connect(function (err, client, done) {
	if (err) console.log(err)
	app.listen(3000, function () {
	    console.log('listening on 3000')
	})
	    myClient = client
	    if(parsedData.name == undefined)
		var idQuery = format('SELECT * from foundit WHERE id = %L', id)
	    else{
		var values = [
		      parsedData.id,
                    parsedData.name,
                    parsedData.lat,
                    parsedData.long,
                    parsedData.mail,
                    parsedData.phone
		];
		var idQuery = format('INSERT INTO foundit (id, name, lat, long, mail, phone) VALUES(%L',values);
	    }
	    myClient.query(idQuery, function (err, result) {
		if (err) {
		    console.log(err)
		}
		console.log("Result: " + JSON.stringify(result.rows[0]))
	    })
	})

	
	//if( parsedData.name == undefined ) {
	
	   // res.writeHead( 200, {'Content-Type': 'text/plain'} );
            //res.end(  db.result );
          //lookUpTag( parsedData, res );
       // } else {
       //   addTag( parsedData, res );
       // }
                 
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
              res.writeHead( 200, {'Content-Type': 'application/json'} );
              res.end( JSON.stringify( database[key] ) );

              console.log( "user id match found, responded with user tag" );
              sendSMS();
              return;
          }
      }
  }

  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end( "id not found" );

}
//¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨

//¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨¨

//Add NFC Tag to database ------------------------------------------
function addTag( parsedData, res ) {
  database.push( parsedData );
  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end("Tag added to database: " + JSON.stringify(parsedData));
  console.log(database);
}


function sendSMS() {
  var callback = function(response) {
    var str = ''
    response.on('data', function (chunk) {
      str += chunk;
    });
  
    response.on('end', function () {
      console.log(str);
    });
  }
  
  // Start the web request.
  var request = https.request(options, callback);
  
  // Send the real data away to the server.
  request.write(postData);
  
  // Finish sending the request.
  request.end()
}

var server = http.createServer(onRequest).listen(8000);
