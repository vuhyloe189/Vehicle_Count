'use strict';

const number = require('../models/traffic.js');

exports.countCar = (volumes) => 
    new Promise((resolve, reject) => {

        const countCar = new number({
            volumes: volumes
        }) ;

        countCar.save()

        .then(() => resolve({ status: 201, message: 'New data was added !' }))


        .catch(() => reject({ status: 500, message: 'Internal Server Error !' }));
});

