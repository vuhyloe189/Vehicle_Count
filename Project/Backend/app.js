'use strict';

const express = require('express');
const app = express();
const passport = require('passport');
const logger = require('morgan');
const dotenv = require("dotenv");
const router = express.Router();
const router_vol = express.Router();
const port = process.env.PORT || 9000;

const cookieParser = require('cookie-parser')
const session = require('express-session')

require('./config/facebook_passport')(passport);

dotenv.config();
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(logger('dev'));

app.use(session({ secret: process.env.TOKEN_SECRET }));
app.use(passport.initialize());
app.use(passport.session());
app.use(cookieParser());

module.exports = router;
require('./route/routes')(router);
app.use('/authenticate', router);

module.exports = router_vol;
require('./route/routes_cars')(router_vol);
app.use('/cars', router_vol);


app.get('/auth/facebook', passport.authenticate('facebook', { scope: 'email' }));

app.get('/auth/facebook/callback', passport.authenticate('facebook', { failureRedirect: '/login' }),
    (req, res) => {
        res.status(200).json({ message: 'Login successfully !' });
    });

const server = require('http').createServer(app);
server.listen(port, () => {
    console.log(`Server is running on port: ${port}`);
});
server.on('connection', () => { '' });