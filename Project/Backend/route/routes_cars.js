'use strict';

const count = require('../functions_volume/car_count.js');
const numbers = require('../functions_volume/get_cars.js');

module.exports = function(router) {

    router.get('/', (req, res) => res.end('Hello'));


    router.get('/get', (req, res) => {
      numbers.getCount(req, res)
        .then(result => res.json(result))

	    .catch(err => res.status(err.status).json({ message: err.message }));

    });
     router.post('/post', (req, res) => {

        const number = req.body.volumes;

         if(!number){

            res.status(400).json({ message: 'Invalid Request !' });

         } else {
             count.countCar(number)

             .then(result => {
             res.status(result.status).json({ message: result.message });
            })
            
            .catch(err => res.status(err.status).json({ message: err.message }));
        
         }
     });
}