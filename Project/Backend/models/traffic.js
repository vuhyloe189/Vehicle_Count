'use strict';

const mongoose = require('mongoose');

const trafficNuberty = mongoose.Schema({ 
    volumes:{
        type: String,
        default: 0
    }
});

mongoose.Promise = global.Promise;

const database = "mongodb+srv://Vuhyloe:Vuhy14171408@vuhyloe.nkglu.mongodb.net/All?retryWrites=true&w=majority";
mongoose
.connect(database, { useNewUrlParser: true, useUnifiedTopology: true, useFindAndModify: false,useCreateIndex: true })
.then(() => console.log('mongoDB -traffic- connected...'))
.catch((err) => console.log(err));

module.exports = mongoose.model('traffic', trafficNuberty);        