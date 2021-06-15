'use strict';

// const auth = require('basic-auth');
const jwt = require('jsonwebtoken');
// const compare = require('tsscmp')
const register = require('../functions/register');
const login = require('../functions/login');
const profile = require('../functions/profile');
const password = require('../functions/password');
const config = require('../config/config.json');

module.exports = router => {

    router.get('/', (req, res) => res.end('Hello'));

    router.post('/logout', (req, res) => {
        res.status(200).json({ message: 'Logout successfully completed!' })
    });
    router.post('/login', (req, res) => {
        const email = req.body.email;
        const passwords = req.body.password;

        if (!email || !passwords) {
            res.status(401).json({ message: 'Invalid Request !' });
            res.clearCookies('');
            res.end();

        } else {
            login.loginUser(email, passwords)

            .then(result => {
                const token = jwt.sign(result, config.secret, { algorithm: 'HS256', expiresIn: '10h' });
                res.status(result.status).json({ message: result.message, token: token });
            })

            .catch(err =>
                res.status(err.status).json({ message: err.message }));


        }
    });

    router.post('/register', (req, res) => {

        const name = req.body.name;
        const email = req.body.email;
        const passwords = req.body.password;

        if (!name || !email || !passwords || !name.trim() || !email.trim() || !passwords.trim()) {

            res.status(400).json({ message: 'Invalid Request !' });

        } else {

            register.registerUser(name, email, passwords)

            .then(result => {
                res.status(result.status).json({ message: result.message })
            })

            .catch(err => res.status(err.status).json({ message: err.message }));
        }
    });

    router.post('/profile/:id', (req, res) => {
        if (checkToken(req)) {

            profile.getProfile(req.params.id)

            .then(result => res.json(result))

            .catch(err => res.status(err.status).json({ message: err.message }));

        } else {

            res.status(401).json({ message: 'Invalid Token !' });
        }
    });

    router.put('/change_password/:id', (req, res) => {

        if (checkToken(req)) {
            const oldPassword = req.body.password;
            const newPassword = req.body.newPassword;

            if (!oldPassword || !newPassword || !oldPassword.trim() || !newPassword.trim()) {

                res.status(400).json({ message: 'Invalid Request !' });

            } else {

                password.changePassword(req.params.id, oldPassword, newPassword)

                .then(result => res.status(result.status).json({ message: result.message }))

                .catch(err => res.status(err.status).json({ message: err.message }));

            }
        } else {

            res.status(402).json({ message: 'Invalid Token !' });
        }
    });

    router.post('/reset_password/:id/password', (req, res) => {

        const email = req.params.id;
        const token = req.body.token;
        const newPassword = req.body.password;

        if (!token || !newPassword || !token.trim() || !newPassword.trim()) {

            password.resetPasswordInit(email)

            .then(result => res.status(result.status).json({ message: result.message }))

            .catch(err => res.status(err.status).json({ message: err.message }));

        } else {

            password.resetPasswordFinish(email, token, newPassword)

            .then(result => res.status(result.status).json({ message: result.message }))

            .catch(err => res.status(err.status).json({ message: err.message }));
        }
    });

    function checkToken(req) {

        const bearerHeader = req.body.token || req.headers['authorization'];

        if (typeof bearerHeader !== 'undefined') {
            // Split at the space
            const bearer = bearerHeader.split(' ');
            // Get token from array
            const bearerToken = bearer[1];
            // Set the token
            const token = bearerToken;
            if (token) {
                try {
                    return jwt.verify(token, config.secret, (err, result) => {
                        if (err) {
                            console.log(err)
                            return false;
                        } else {
                            // res.json({
                            //     message: req.params.id
                            // })
                            return result.message === req.params.id;
                        }
                    });
                } catch (err) {
                    console.log(err)
                    return false;
                }

            } else {
                console.log("Can take Token! ");
                return false;
            }

        } else {
            console.log("Undefined !");
            // Forbidden
            return false;
        }
    }
}