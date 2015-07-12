var exec = require("cordova/exec");

module.exports = {
    navigate: function(type, destination, origin, success, failure) {
        exec(success || function() {},
            failure || function() {},
            'LaunchNavigator',
    		'navigate',
            [type, destination, origin]);
    }
};
