// Angular App Module
angular.module('todoApp', [])
.config(['$httpProvider', function($httpProvider) {
    // Configure HTTP provider for CORS
    $httpProvider.defaults.headers.common['Content-Type'] = 'application/json';
    $httpProvider.defaults.headers.common['Accept'] = 'application/json';
}]);

