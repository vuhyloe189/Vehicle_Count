'use strict';

const cars = require('../models/traffic.js');

exports.getCount = volumes => 
    new Promise((resolve, reject) => {
        cars.find()

        .then(num => resolve(num))

        .catch(err =>  reject({ status: 500, message: 'Internal Server Error !' }));
});