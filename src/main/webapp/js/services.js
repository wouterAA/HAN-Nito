'use strict';

angular.module('HAN-Nito.services', ['ngResource']).
    /** Returns the apiUrl **/
    factory('apiUrl', function($location) {
        console.log($location);
        return 'api';
    }).
    /** Represents a wrapper for the AccountService backend **/
    service('AccountService', function($rootScope, $http, apiUrl) {
        return {
            login: function(credentials) {
                $http.post(apiUrl + "/login", credentials, {}).
                    success(function(data) {
                        $rootScope.username = data;
                        $rootScope.$broadcast('AccountService.login', $rootScope.username);
                    }).error(function() {
                        $rootScope.$broadcast("AccountService.failed");
                    });
            },
            logout: function() {
                $http.post(apiUrl + "/logout", {}, {}).
                    success(function() {
                        $rootScope.username = null;
                        $rootScope.$broadcast('AccountService.logout', $rootScope.username);
                    });
            },
            register: function(registration) {
                $http.post(apiUrl + "/register", registration, {}).
                    success(function(data) {
                        $rootScope.username = data;
                        $rootScope.$broadcast('AccountService.login', $rootScope.username);
                    }).error(function(data, status) {
                        if(status == 409) {
                            $http.post(apiUrl + "/checkusername", registration.username, {}).
                                error(function() {
                                    $rootScope.$broadcast('AccountService.failed.username');
                                });
                            $http.post(apiUrl + "checkemail", registration.email, {}).
                                error(function() {
                                    $rootScope.$broadcast('AccountService.failed.email');
                                });
                        } else {
                            $rootScope.$broadcast('AccountService.failed');
                        }
                    });
            },
            ping: function() {
                $http.get(apiUrl + "/ping", {}).
                    success(function(data) {
                        $rootScope.username = data;
                        $rootScope.$broadcast('AccountService.login', $rootScope.username);
                    });
            }
        };
    }).
    /** Checks if the user is allowed to be at the path he's at **/
    service('PathAuthenticator', function($rootScope, $location, $routeParams) {
        return {
            authenticate: function() {
                if(!this.validate($location.path())) {
                    $location.path('/');
                }
            },
            validate: function(path) {
                if($rootScope.username == null) {
                    if(['/', '/login', '/register'].indexOf(path) === -1) {
                        return false;
                    }
                } else {
                    var protectedUrls = ['/', '/exams', '/exam/', '/exams/create', '/questions', '/questions/create',
                                         '/sources', '/sources/create'];
                    if(Object.keys($routeParams).length === 1) {
                        path = path.substring(0, path.indexOf($routeParams[Object.keys($routeParams)[0]]) -1); //-1 for the extra /
                    }
                    if(protectedUrls.indexOf(path) == -1) {
                        return false;
                    }
                }
                return true;
            },
            validateAndRedirect: function(path) {
                if(!this.validate(path)) {
                    $location.path('/');
                }
            }
        }
    }).
/** Represents a wrapper for the Tag backend **/
    service('Tag', function($http, apiUrl) {
        return {
            getAll: function($scope) {
                var cachedTags = amplify.store("tags") || [];
                $http.get(apiUrl + "/tags", {}).
                    success(function(data) {
                        if(angular.toJson(cachedTags) != angular.toJson(data)) {
                            $scope.$broadcast('Tags.received', data);
                            amplify.store("tags", data);
                        }
                    });
                return cachedTags;
            }
        }
    }).
    /** Represents a wrapper for the DifficultyLevel backend **/
    service('DifficultyLevel', function($http, apiUrl) {
        return {
            getAll: function($scope) {
                var cachedDifficultyLevels = amplify.store("difficultylevels") || [];
                $http.get(apiUrl + "/difficultylevels", {}).
                    success(function(data) {
                        if(angular.toJson(cachedDifficultyLevels) != angular.toJson(data)) {
                            $scope.$broadcast('DifficultyLevels.received', data);
                            amplify.store("difficultylevels", data);
                        }
                    });
                return cachedDifficultyLevels;
            }
        }
    }).
    /** Represents a wrapper for the Question backend **/
    service('Question', function($http, apiUrl) {
        return {
            getAll: function($scope) {
                var cachedQuestions = amplify.store("questions") || [];
                $http.get(apiUrl + "/questions", {}).
                    success(function(data) {
                        if(angular.toJson(cachedQuestions) != angular.toJson(data)) {
                            $scope.$broadcast('Questions.received', data);
                            amplify.store('questions', data);
                        }
                    });
                return cachedQuestions;
            },
            create: function($scope, question) {
                $http.post(apiUrl + "/questions", question, {}).
                    success(function(data) {
                        var cachedQuestions = amplify.store("questions") || [];
                        var newCache = _.union(data, cachedQuestions);
                        amplify.store("questions", newCache);
                        $scope.$broadcast("Question.added", data);
                    }).error(function() {
                        $scope.$broadcast("Question.failed");
                    });
            },
            filter: function($scope, filter) {
                $http.post(apiUrl + "/questions/search", filter, {}).
                    success(function(data) {
                        $scope.$broadcast('Questions.received', data);
                    })
            },
            delete: function($scope, question) {
                var cachedQuestions = amplify.store("questions") || [];
                $http.delete(apiUrl + "/questions/" + question._id.$oid, {}).
                    success(function() {
                        $scope.$broadcast("Question.deleted");
                    });
            }
        }
    }).
    /** Represents a wrapper for the Exam backend **/
    service('Exam', function($http, apiUrl) {
        return {
            getAll: function($scope) {
                var cachedExams = amplify.store("exams") || [];
                $http.get(apiUrl + "/exams", {}).
                    success(function(data) {
                        if(angular.toJson(cachedExams) != angular.toJson(data)) {
                            $scope.$broadcast('Exams.received', data);
                            amplify.store('exams', data);
                        }
                    });
                return cachedExams;
            },
            get: function($scope, id) {
                $http.get(apiUrl + "/exams/" + id, {}).
                    success(function(data) {
                        $scope.$broadcast('Exam.received', data);
                    });
            },
            getFromUser: function($scope, owner) {
                $http.get(apiUrl + "/exams/owner/" + owner, {}).
                    success(function(data) {
                        $scope.$broadcast("Exam.FromUser.received", data);
                    });
            },
            create: function($scope, exam) {
                $http.post(apiUrl + "/exams", exam, {}).
                    success(function(data) {
                        var cachedExams = amplify.store("exams") || [];
                        var newCache = _.union(data, cachedExams);
                        amplify.store("exams", newCache);
                        $scope.$broadcast("Exam.added", data);
                    });
            },
            update: function($scope, examId, exam) {
                _.each(exam.questions, function(question) {
                    if(question._id == null) return;
                    question.id = question._id.$oid;
                });
                $http.put(apiUrl + "/exams/" + examId, exam, {}).
                    success(function(data) {
                        $scope.$broadcast("Exam.updated", data);
                    });
            },
            delete: function($scope, exam) {
                $http.delete(apiUrl + "/exams/" + exam._id.$oid, {}).
                    success(function() {
                        $scope.$broadcast("Exam.deleted");
                    });
            }
        }
    }).
    /** Makes sure if the selected item is selected **/
    service('SelectedAdder', function() {
        return {
            addSelected: function(array) {
                _.each(array, function(obj) {
                    if(obj.selected == null) {
                        obj.selected = false;
                    }
                })
            }
        }
    }).
    /** Represents a wrapper for the Source backend **/
    service('Source', function($http, apiUrl) {
        return {
            getAll: function($scope) {
                var cachedSources = amplify.store("sources") || [];
                $http.get(apiUrl + "/sources", {}).
                    success(function(data) {
                        if(angular.toJson(cachedSources) != angular.toJson(data)) {
                            $scope.$broadcast('Sources.received', data);
                            amplify.store('sources', data);
                        }
                    });
                return cachedSources;
            },
            uploadImage: function($scope, imageInput) {
                var fd = new FormData();
                fd.append('image', imageInput.files[0]);

                var url = apiUrl;
                $.ajax({
                    url: url + '/sources/upload',
                    data: fd,
                    processData: false,
                    contentType: false,
                    type: 'POST',
                    success: function(data){
                        $scope.$broadcast('Source.upload.completed', data);
                    }
                });
            },
            create: function($scope, source) {
                $http.post(apiUrl + "/sources", source, {}).
                    success(function(data) {
                        var cachedSources = amplify.store("sources") || [];
                        var newCache = _.union(data, cachedSources);
                        amplify.store("sources", newCache);
                        $scope.$broadcast("Source.added", data);
                    });
            },
            delete: function($scope, source) {
                $http.delete(apiUrl + "/sources/" + source._id.$oid, {}).
                    success(function() {
                        $scope.$broadcast("Source.deleted");
                    });
            }
        }
    });
