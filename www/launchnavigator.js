var exec = require("cordova/exec");

module.exports = {
    doLaunch: function(type, destination, origin, success, failure) {
        exec(success || function() {},
            failure || function() {},
            'LaunchNavigator',
    		'doLaunch',
            [type, destination, origin]);
    }
};

// /** ngCordova directive **/
// angular.module('ngCordova.plugins.launchnavigator', [])
//   .factory('$cordovaLaunchNavigator', ['$q', '$window', function ($q, $window) {
//     return {
//       doLaunch: function(type, destination, origin) {
//         var q = $q.defer();
//         $window.launchnavigator.doLaunch(
//           type, destination, origin,
//           function (result) {
//             q.resolve(result);
//           },
//           function (err) {
//             q.reject(err);
//           });
//         return q.promise;
//       }
//     };
//   }]);
