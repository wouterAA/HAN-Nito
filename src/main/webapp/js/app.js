'use strict';

/** Specifies which objects to load for the users location **/
angular.module('HAN-Nito', ['HAN-Nito.filters', 'HAN-Nito.services', 'HAN-Nito.directives', 'ui', 'ui.bootstrap.modal', 'ngSanitize']).
  config(['$routeProvider',function($routeProvider) {
    $routeProvider.when('/', {templateUrl: 'partials/homepage.html', controller: HomePageCtrl});
    $routeProvider.when('/login', {templateUrl: 'partials/login.html', controller: LoginCtrl});
    $routeProvider.when('/register', {templateUrl: 'partials/register.html', controller: RegisterCtrl});
    $routeProvider.when('/exam/:id', {templateUrl: 'partials/exam.html', controller: LinkQuestionExamCtrl});
    $routeProvider.when('/exams', {templateUrl: 'partials/exams.html', controller: ExamCtrl});
    $routeProvider.when('/exams/create', {templateUrl: 'partials/exams.html', controller: ExamCtrl});
    $routeProvider.when('/questions', {templateUrl: 'partials/questions.html', controller: QuestionCtrl, resolve: QuestionCtrl.questions});
    $routeProvider.when('/questions/create', {templateUrl: 'partials/questions.html', controller: QuestionCtrl});
    $routeProvider.when('/sources', {templateUrl: 'partials/sources.html', controller: SourceCtrl});
    $routeProvider.when('/sources/create', {templateUrl: 'partials/sources.html', controller: SourceCtrl});
    $routeProvider.otherwise({redirectTo: '/', controller: HomePageCtrl});
  }]);