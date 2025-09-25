// Todo Controller
angular.module('todoApp').controller('TodoController', ['$scope', 'TodoService', function($scope, TodoService) {

    // Initialize variables
    $scope.todos = [];
    $scope.filteredTodos = [];
    $scope.newTodo = {};
    $scope.filter = 'all';
    $scope.loading = false;

    // Load all todos on page load
    $scope.loadTodos = function() {
        $scope.loading = true;
        TodoService.getAllTodos()
            .then(function(response) {
                $scope.todos = response.data;
                $scope.applyFilter();
                $scope.loading = false;
            })
            .catch(function(error) {
                console.error('Error loading todos:', error);
                $scope.loading = false;
                alert('Error loading todos. Please try again.');
            });
    };

    // Add new todo
    $scope.addTodo = function() {
        if (!$scope.newTodo.title || $scope.newTodo.title.trim() === '') {
            return;
        }

        $scope.loading = true;
        var todo = {
            title: $scope.newTodo.title.trim(),
            description: $scope.newTodo.description ? $scope.newTodo.description.trim() : ''
        };

        TodoService.createTodo(todo)
            .then(function(response) {
                $scope.todos.push(response.data);
                $scope.applyFilter();
                $scope.newTodo = {}; // Clear form
                $scope.loading = false;
            })
            .catch(function(error) {
                console.error('Error adding todo:', error);
                $scope.loading = false;
                alert('Error adding todo. Please try again.');
            });
    };

    // Toggle todo completion status
    $scope.toggleTodo = function(todo) {
        TodoService.toggleTodo(todo.id)
            .then(function(response) {
                // Update the todo in the list
                var index = $scope.todos.findIndex(t => t.id === todo.id);
                if (index !== -1) {
                    $scope.todos[index] = response.data;
                }
                $scope.applyFilter();
            })
            .catch(function(error) {
                console.error('Error toggling todo:', error);
                // Revert the checkbox state
                todo.completed = !todo.completed;
                alert('Error updating todo. Please try again.');
            });
    };

    // Start editing a todo
    $scope.editTodo = function(todo) {
        todo.editing = true;
        todo.editTitle = todo.title;
        todo.editDescription = todo.description;
    };

    // Save edited todo
    $scope.saveTodo = function(todo) {
        if (!todo.editTitle || todo.editTitle.trim() === '') {
            return;
        }

        var updatedTodo = {
            title: todo.editTitle.trim(),
            description: todo.editDescription ? todo.editDescription.trim() : '',
            completed: todo.completed
        };

        TodoService.updateTodo(todo.id, updatedTodo)
            .then(function(response) {
                var index = $scope.todos.findIndex(t => t.id === todo.id);
                if (index !== -1) {
                    $scope.todos[index] = response.data;
                    $scope.todos[index].editing = false;
                }
                $scope.applyFilter();
            })
            .catch(function(error) {
                console.error('Error updating todo:', error);
                alert('Error updating todo. Please try again.');
            });
    };

    // Cancel editing
    $scope.cancelEdit = function(todo) {
        todo.editing = false;
        delete todo.editTitle;
        delete todo.editDescription;
    };

    // Delete todo
    $scope.deleteTodo = function(todo) {
        if (confirm('Are you sure you want to delete this todo?')) {
            TodoService.deleteTodo(todo.id)
                .then(function(response) {
                    var index = $scope.todos.findIndex(t => t.id === todo.id);
                    if (index !== -1) {
                        $scope.todos.splice(index, 1);
                    }
                    $scope.applyFilter();
                })
                .catch(function(error) {
                    console.error('Error deleting todo:', error);
                    alert('Error deleting todo. Please try again.');
                });
        }
    };

    // Set filter
    $scope.setFilter = function(filter) {
        $scope.filter = filter;
        $scope.applyFilter();
    };

    // Apply current filter
    $scope.applyFilter = function() {
        switch ($scope.filter) {
            case 'pending':
                $scope.filteredTodos = $scope.todos.filter(function(todo) {
                    return !todo.completed;
                });
                break;
            case 'completed':
                $scope.filteredTodos = $scope.todos.filter(function(todo) {
                    return todo.completed;
                });
                break;
            default:
                $scope.filteredTodos = $scope.todos;
        }
    };

    // Get pending count
    $scope.getPendingCount = function() {
        return $scope.todos.filter(function(todo) {
            return !todo.completed;
        }).length;
    };

    // Get completed count
    $scope.getCompletedCount = function() {
        return $scope.todos.filter(function(todo) {
            return todo.completed;
        }).length;
    };

    // Format date for display
    $scope.formatDate = function(dateString) {
        if (!dateString) return '';
        var date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    };

    // Load todos on controller initialization
    $scope.loadTodos();
}]);

