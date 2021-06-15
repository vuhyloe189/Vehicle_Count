const passport = require('passport'),
    FacebookStrategy = require('passport-facebook').Strategy;

const User = require('../models/facebook_account');

module.exports = () => {
    passport.use(new FacebookStrategy({
            clientID: 184416350233051,
            clientSecret: '1e50cbeccd2ee954f84c204c09c67bfd',
            callbackURL: "http://localhost:9000/auth/facebook/callback",
            enableProof: true,
            profileFields: ['email', 'displayName', 'photos']

        },
        (token, refreshToken, profile, done) => {
            console.log(profile)
            return done(null, profile)
        }
    ));

    passport.serializeUser(function(user, done) {
        done(null, user.id);
    });

    passport.deserializeUser(function(id, done) {
        User.findById(id, function(err, user) {
            done(err, user);
        });
    });
}