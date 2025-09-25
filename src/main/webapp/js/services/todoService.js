// Todo Service
angular.module('todoApp').service('TodoService', ['$http', function($http) {
    var baseUrl = '/api/todos';

    this.getAllTodos = function() {
        return $http.get(baseUrl);
    };

    this.getTodoById = function(id) {
        return $http.get(baseUrl + '/' + id);
    };

    this.createTodo = function(todo) {
        return $http.post(baseUrl, todo);
    };

    this.updateTodo = function(id, todo) {``
        return $http.put(baseUrl + '/' + id, todo);
    };

    this.toggleTodo = function(id) {
        return $http.put(baseUrl + '/' + id, { toggle: true });
    };

    this.deleteTodo = function(id) {
        return $http.delete(baseUrl + '/' + id);
    };
}]);

