const express = require('express')
const app = express()
var pg = require('pg')
var format = require('pg-format')
var PGUSER = 'postgres'
var PGDATABASE = 'foundit'
var sourceFile = require('../server.js');
var config = {
    user: 'postgres',
    password: 'foundit',
    database: 'foundit',
    max: 10,
    idleTimeoutMillis: 30000
}

var id = sourceFile.id;
console.log(id);
var pool = new pg.Pool(config);
var myClient


pool.connect(function (err, client, done) {
  if (err) console.log(err)
  app.listen(3000, function () {
    console.log('listening on 3000')
  })
    myClient = client
  var idQuery = format('SELECT * from foundit WHERE id = %L', id)
  myClient.query(idQuery, function (err, result) {
    if (err) {
      console.log(err)
    }

    console.log(result.rows[0])
  })
})


