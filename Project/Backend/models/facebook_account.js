'use strict';

const mongoose = require('mongoose');

const Schema = mongoose.Schema;

const userSchema = mongoose.Schema({

    name: { type: String, require: true },
    email: { type: String, unique: true, require: true },
    hashed_password: String,
    created_at: String,
    temp_password: String,
    temp_password_time: String

});

mongoose.Promise = global.Promise;

const database = "mongodb+srv://Vuhyloe:Vuhy14171408@vuhyloe.nkglu.mongodb.net/All?retryWrites=true&w=majority";
mongoose
    .connect(database, { useNewUrlParser: true, useUnifiedTopology: true, useFindAndModify: false, useCreateIndex: true })
    .then(() => console.log('mongoDB -facbook- connected...'))
    .catch((err) => console.log(err));

module.exports = mongoose.model('facebookAccount', userSchema);