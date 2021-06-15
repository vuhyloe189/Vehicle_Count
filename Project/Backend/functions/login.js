'use strict';
const passport = require('passport');
const user = require('../models/user');
const bcrypt = require('bcryptjs');

exports.loginUser = (email, password) =>

    new Promise((resolve, reject) => {

        user.findOne({ email: email })

        .then(users => {
                const hashed_password = users.hashed_password;

                if (users.length === 0 || !user) {
                    reject({ status: 404, message: 'User does not exit' });
                }

                if (bcrypt.compareSync(password, hashed_password)) {
                    resolve({ status: 200, message: email });
                } else {
                    reject({ status: 400, message: 'Invalid Credentials !' });
                }
            })
            // node
            .catch(() => reject({ status: 500, message: 'Internal Server Error ! ' }));

    });