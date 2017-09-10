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

var key = new Buffer(username + ':' + password).toString('base64');

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
    "id" : "1",
    "name" : "David Oskarsson",
      "lat" : "58.407",
      "lon" : "15.6277",
    "mail" : "david@gmail.com",
    "phone" : "+46700000000",
  },

  {
    "id" : "2",
    "name" : "Jesper Adriansson",
      "lat" : "55.432",
      "lon" : "15.457",
    "mail" : "aaa@outlook.com",
    "phone" : "+4600507979" 
  }

]

function onRequest(req,res){
    var data = "";
    var parsedData;

    console.log("Request detected!");
    
    req.on('data', function ( chunk ) {
        data += chunk;
        parsedData = JSON.parse(data);
	var id = parsedData.id

	/*
	pool.connect(function (err, client, done) {
	if (err) console.log(err)
	app.listen(3000, function () {
	    console.log('listening on 3000')
	})
	    myClient = client;

	    //Look up id in database
	    if(parsedData.name == undefined) {

		/*
		var idQuery = format('SELECT * from foundit WHERE id = %L', id)

	        myClient.query(idQuery, function (err, result) {
		    if (err) {
			console.log(err)
		    }
		
	            console.log("ID found!");
		    console.log(JSON.stringify(result.rows[0]));
	        })
		
	    } else {

		
		
		//Insert new tag into database
		var values = [parsedData.id, parsedData.name,parsedData.lat, parsedData.lon,
                    parsedData.mail, parsedData.phone
			     ];
		console.log(values);
		var idQuery = format('INSERT INTO foundit (id, name, lat, long, mail, phone) VALUES (%s,%s,%s,%s,%s,%s)',values);
		
	            myClient.query(idQuery, function (err, result) {
		    if (err) {
			console.log(err)
		    }
		
			console.log("Inserted tag in database");
			console.log( JSON.stringify(parsedData) );
		   
	        })
	
	    }
	   */
	    
	    /*myClient.query(idQuery, function (err, result) {
		if (err) {
		    console.log(err)
		}
		
	        console.log("Result: " + JSON.stringify(result.rows[0]))
	    })
	})*/

	var p = parsedData;
	if( parsedData.id != undefined && parsedData.id != "" && (parsedData.name == undefined && parsedData.lat == undefined && parsedData.lon == undefined
								  && parsedData.mail == undefined && parsedData.phone == undefined) ) {
            lookUpTag( parsedData, res );
        } else if(p.id != undefined && p.name != undefined && p.lat != undefined && p.lon != undefined && p.mail != undefined && p.phone != undefined) {
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
              res.writeHead( 200, {'Content-Type': 'application/json'} );
              res.end("User ID found! " + JSON.stringify( database[key] ) );

              console.log( "user id match found, responded with user tag" );
              sendSMS( database[key].phone, database[key].name, database[key].lat, database[key].lon );
              return;
          }
      }
  }

  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end( "id not found" );

}


//Add NFC Tag to database ------------------------------------------
function addTag( parsedData, res ) {
  database.push( parsedData );
  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end("Tag added to database: " + JSON.stringify(parsedData));
  console.log("New tag added to database. New database is now: " + database);
}

//Set message and send SMS to user
function sendSMS( phone, name, latitude, longitude ) {

    var postFields = {
	from:    "Foundit", 
	to:      "+46768970887",
	message: "Hello " + name + "! Your NFC tag was recently scanned at this location: https://www.openstreetmap.org/?mlat="+latitude+"&mlon="+longitude+"#map=12/"+latitude+"/"+longitude	
    }

    var postData = querystring.stringify(postFields);
    
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
