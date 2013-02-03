'use strict';

/**
 * This Object represents the Navbar and everything that modifies it.
 * @param $scope is the object itself
 * @param $rootScope is the object managing all the objects
 * @param $location Allows the user to get url information
 * @param $location api for logging in etc
 * @param PathAuthenticator api for checking if the user is allowed to be at his location
 * @constructor
 */
function NavbarController($scope, $rootScope, $location, AccountService, PathAuthenticator) {
    /**
     * username represents the user logged in {@link NavbarController}
     * @type {*}
     */
    $scope.username = $rootScope.username;
    if($scope.username == null) AccountService.ping();

    /** Sets the username if changed **/
    $rootScope.$watch('username', function(username) {
        $scope.username = username;
    });

    /** Returns the current path **/
    $scope.routeIs = function(routeName) {
        return $location.path() === routeName;
    };

    /** Logs the user wants out. **/
    $scope.logout = function() {
        AccountService.logout();
        if(PathAuthenticator.validate($location.path())) {
            $location.path('/');
        }
    }
};

/**
 * This Object represents the Homepage
 * @param $scope is the object itself
 * @constructor
 */
function HomePageCtrl($scope) {
};

/**
 * This Object reprents the login page
 * @param $scope is the object itself is the object itself
 * @param $location Allows the user to get url information
 * @param $location api for logging in etc
 * @param PathAuthenticator api for checking if the user is allowed to be at his location
 * @constructor
 */
function LoginCtrl($scope, $location, AccountService, PathAuthenticator) {
    /** Checks if the user is logged in **/
    PathAuthenticator.authenticate();

    /** Gets called when the user wants to login **/
    $scope.login = function() {
        AccountService.login($scope.credentials);
    }

    /** Gets called when logging in succeeded **/
    $scope.$on("AccountService.login", function(event) {
        toastr.options.timeOut = 3000;
        toastr.options.positionClass = 'toast-bottom-left';
        toastr.success('Login succesvol!');

        $location.path('/');
    });

    /** Gets caleld when the login process failed **/
    $scope.$on("AccountService.failed", function(event) {
        $scope.loginError = "Het inloggen is mislukt. Controlleer uw gegevens."
    });
};

/**
 * This Object represetnts the RegisterPage
 * @param $rootScope is the object managing all the objects
 * @param $scope is the object itself is the object itself
 * @param $location Allows the user to get url information
 * @param $location api for logging in etc
 * @param PathAuthenticator api for checking if the user is allowed to be at his location
 * @constructor
 */
function RegisterCtrl($rootScope, $scope, $location, AccountService, PathAuthenticator) {
    /** Checks if the user is logged in **/
    PathAuthenticator.authenticate();

    /** Gets called when the user wants to register **/
    $scope.register = function() {
        AccountService.register($scope.registration);
    }

    /** Gets called when the user has finished the register process **/
    $rootScope.$on("AccountService.login", function(event, data) {
        $location.path('/');
    });

    /** Gets called when the register process failed because the username is in use **/
    $rootScope.$on("AccountService.failed.username", function(event) {
        $scope.registerError = "De gebruikersnaam \"" + $scope.registration.username + "\" lijkt al in gebruik te zijn. Gebruikt u een ander.";
    });

    /** Gets called when the register process has failed because the email is already inuse **/
    $rootScope.$on("AccountService.failed.email", function(event) {
        $scope.registerError = "Het emailadres \"" + $scope.registration.email + "\" lijkt al in gebruik te zijn. Gebruikt u een ander.";
    });

    /** Gets called when the register process has failed **/
    $rootScope.$on("AccountService.failed", function(event) {
        $scope.registerError = "Er is iets mis gegaan tijdens het registreren. Probeert u het later opnieuw.";
    });
};

/**
 * This Object represents the Exam webpage
 * @param $rootScope is the object managing all the objects
 * @param $scope is the object itself
 * @param $location Allows the user to get url information
 * @param $window document.window api
 * @param apiUrl contains the apiUrl
 * @param PathAuthenticator api for checking if the user is allowed to be at his location
 * @param Exam API for the exam backend
 * @constructor
 */
function ExamCtrl($rootScope, $scope, $location, $window, apiUrl, PathAuthenticator, Exam) {
    /** Checks if the user is logged in **/
    PathAuthenticator.authenticate();

    $scope.url = apiUrl;
    $scope.dateOptions = {format: 'dd-mm-yyyy'}

    /** Checks if the loggedin user is the owner **/
    $scope.isOwner = function(owner) {
        return $rootScope.username === owner;
    }

    /** Checks if you're on the path provided **/
    $scope.routeIs = function(routeName) {
        return $location.path() === routeName;
    };

    /** Gets all exams **/
    $scope.exams = Exam.getAll($scope);
    $scope.$on("Exams.received", function(event, data) {
        $scope.exams = data;
    });

    /** Submits an exam **/
    $scope.submitExam = function() {
        var exam = {
            "subject": $scope.subject,
            "instruction": $scope.introduction
        }
        Exam.create($scope, exam);
    }
    $scope.$on("Exam.added", function(event, data) {
        $location.path("/exams");
    });

    /** Deletes an Exam **/
    $scope.deleteExam = function(exam) {
        Exam.delete($scope, exam);
    }
    $scope.$on("Exam.deleted", function(event) {
        Exam.getAll($scope);
    });

    /** Shows the generate PDF Modal **/
    $scope.showGeneratePDFModal = function(exam) {
        $scope.modalHeader = "Genereer een PDF van het proefwerk met als onderwerp: \"" + exam.subject + "\":";
        $scope.examToGeneratePDFFor = exam;
        $scope.generatePDFModal = true;
    };

    /** Closes the generatePDFModal **/
    $scope.closeGeneratePDFModal = function() {
        $scope.generatePDFModal = false;
    };

    /** Gets called when you want to submit the data specified **/
    $scope.generatePDFFromExam = function() {
        $scope.generatePDFModal = false;
        $scope.showGetUrlsForPDFsModal();
    }

    /** Shows the url modal for the generate pdf use case **/
    $scope.showGetUrlsForPDFsModal = function() {
        var dateParts = $scope.pdfDetails.date.split("-");
        $scope.dateInMillis = new Date(dateParts[2], (dateParts[1] - 1), dateParts[0]).getTime();
        $scope.getUrlsForPDFsModal = true;
    }

    /** Closes the url modal **/
    $scope.closeGetUrlsForPDFsModal = function() {
        $scope.getUrlsForPDFsModal = false;
    }
};

/**
 * This method represents the Question page
 * @param $rootScope is the object managing all the objects
 * @param $scope is the object itself
 * @param $location Allows the user to get url information
 * @param $window document.window api
 * @param PathAuthenticator api for checking if the user is allowed to be at his location
 * @param Question API for the question backend
 * @param Exam API for the exam backend
 * @param Tag API for the tag backend
 * @param DifficultyLevel API for the DifficultyLevel backend 
 * @param SelectedAdder sets the selected
 * @constructor
 */
function QuestionCtrl($rootScope, $scope, $location, $window, PathAuthenticator, Question, Exam, Tag, DifficultyLevel, SelectedAdder) {
    /** Checks if the user is logged in **/
    PathAuthenticator.authenticate();

    /** Checks if you're on the path specified **/
    $scope.routeIs = function(routeName) {
        return $location.path() === routeName;
    };

    /** Checks if the owner logged in is the owner **/
    $scope.isOwner = function(owner) {
        return $rootScope.username === owner;
    }

    /** Gets all the questions **/
    $scope.questions = Question.getAll($scope);
    $scope.$on("Questions.received", function(event, data) {
        $scope.questions = data;
    });

    /** Specifies the questionTypes **/
    $scope.questionTypes = [{questionType: 'OpenQuestion', nl_translation: 'Open vraag'},
                            {questionType: 'MultipleChoiceQuestion', nl_translation: 'Meerkeuze vraag'},
                            {questionType: 'YesNoQuestion', nl_translation: 'Enkele Stellingvraag'},
                            {questionType: 'TheoremQuestion', nl_translation: 'Dubbele Stellingvraag'}];
    SelectedAdder.addSelected($scope.questionTypes);
    $scope.selectedQuestionType = $scope.questionTypes[0];

    /** Sets the difficultyLevels including the defaults **/
    $scope.difficultyLevels = DifficultyLevel.getAll($scope);
    SelectedAdder.addSelected($scope.difficultyLevels);
    $scope.difficultySelection = $scope.difficultyLevels[0];
    $scope.$on('DifficultyLevels.received', function(event, data) {
        $scope.difficultyLevels = data;
        SelectedAdder.addSelected($scope.difficultyLevels);
        $scope.difficultySelection = $scope.difficultyLevels[0];
    });

    /** Gets all tags **/
    $scope.tags = Tag.getAll($scope);
    SelectedAdder.addSelected($scope.tags);
    $scope.$on("Tags.received", function(event, data) {
        $scope.tags = data;
        SelectedAdder.addSelected($scope.tags);
    });

    /** Watches if the questionType has changed. If so than takes action to resolve conflicts **/
    $scope.$watch("selectedQuestionType.questionType", function(newValue) {
        switch(newValue) {
            case "OpenQuestion":
                if(angular.isObject($scope.createdQuestion.answer)) $scope.createdQuestion.answer = null;
                break;
            case "MultipleChoiceQuestion":
                if($scope.createdQuestion.answer != null) $scope.createdQuestion.answer = null;
                break;
            case "YesNoQuestion":
                $scope.createdQuestion.answer = "True";
                break;
            case "TheoremQuestion":
                $scope.createdQuestion.answer = "a";
                break;
            default:
                break;
        }
    });

    /** Filters the questions by sending a request to the backend **/
    $scope.filterQuestions = function() {
        var filter;
        var tags = Array();
        var difficultyLevels = Array();
        var questionTypes = Array();
        _.each($scope.tags, function(element) {
            if(element.selected) {
                tags.push(element.tagname);
            }
        })
        _.each($scope.difficultyLevels, function(element) {
            if(element.selected) {
                difficultyLevels.push(element.difficulty);
            }
        })
        _.each($scope.questionTypes, function(element) {
            if(element.selected) {
                questionTypes.push(element.questionType);
            }
        })
        filter = {
            "tags": tags,
            "difficulty": difficultyLevels,
            "question_type": questionTypes
        }

        Question.filter($scope, filter);
    };

    /** Creates a new question **/
    $scope.submitQuestion = function() {
        var tags = Array();
        _.each($scope.selectedTags, function(element) {
            tags.push(element.tagname);
        });

        var question = {
            "question_type": $scope.selectedQuestionType.questionType,
            "difficulty": $scope.difficultySelection.difficulty,
            "tags": tags
        };

        switch($scope.selectedQuestionType.questionType) {
            case "OpenQuestion":
                question.question = $scope.createdQuestion.question;
                question.correct_answer = $scope.createdQuestion.answer;
                break;
            case "MultipleChoiceQuestion":
                question.question = $scope.createdQuestion.question,
                question.answers = {
                    "a" : $scope.createdQuestion.answer.a,
                    "b" : $scope.createdQuestion.answer.b,
                    "c" : $scope.createdQuestion.answer.c,
                    "d" : $scope.createdQuestion.answer.d
                };
                question.correct_answer = $scope.createdQuestion.correct_answer;
                break;
            case "YesNoQuestion":
                question.question = $scope.createdQuestion.question;
                question.correct_answer = $scope.createdQuestion.answer;
                break;
            case "TheoremQuestion":
                question.theorem1 = $scope.createdQuestion.theorem1;
                question.theorem2 =  $scope.createdQuestion.theorem2;
                question.correct_answer = $scope.createdQuestion.answer;
                break;
            default:
                break;
        };
        Question.create($scope, question);
        $scope.$on("Question.added", function(event, data) {
            $location.path('/questions');
            var noty = noty({text: 'noty - a jquery notification library!'});
        });
        $scope.addQuestionFailed = false;
        $scope.$on("Question.failed", function() {
            $scope.addQuestionFailed = true;
        });
    };

    /** Deletes a question **/
    $scope.deleteQuestion = function(question) {
        Question.delete($scope, question);
    }
    $scope.$on("Question.deleted", function(event) {
        Question.filterQuestions($scope);
        //Question.getAll($scope);
    });

    /** Opens the add question to exam modal **/
    $scope.openQuestionToExamModal = function(question) {
        $scope.modalHeader = "Voeg de vraag: \"" + question.question + "\" aan het volgende proefwerk toe:";
        $scope.questionToAddToExam = question;
        $scope.examsFromUser = Exam.getFromUser($scope, $rootScope.username);
        $scope.addQuestionToExamModal = true;
    };

    /** Gets fired when the user wants to see his exams **/
    $scope.emptyExamList = false;
    $scope.$on("Exam.FromUser.received", function(event, data) {
        if(data.length == 0) $scope.emptyExamList = true;
        $scope.examsFromUser = data;
        $scope.selectedExam = $scope.examsFromUser[0];
    });

    /** Close the modal **/
    $scope.closeQuestionToExamModal = function() {
        $scope.addQuestionToExamModal = false;
    };

    /** adds the question to the exam **/
    $scope.addQuestionToExam = function() {
        $scope.addQuestionToExamModal = false;
        $scope.selectedExam.questions.push($scope.questionToAddToExam);
        Exam.update($scope, $scope.selectedExam._id.$oid, $scope.selectedExam);
    }
    $scope.$on("Exam.updated", function(data) {
        Exam.getAll($scope);
    });
};

/**
 * The SourceCtrl represents the Source webpage.
 * @param $rootScope is the object managing all the objects
 * @param $scope is the object itself
 * @param $window document.window api
 * @param $location Allows the user to get url information
 * @param PathAuthenticator api for checking if the user is allowed to be at his location
 * @param Source API for the source backend
 * @constructor
 */
function SourceCtrl($rootScope, $scope, $window, $location, PathAuthenticator, apiUrl, Source) {
    PathAuthenticator.authenticate();

    /** Checks if the route is the route specified */
    $scope.routeIs = function(routeName) {
        return $location.path() === routeName;
    };

    $scope.url = apiUrl;

    /** Gets all the sources **/
    $scope.sources = Source.getAll($scope);
    $scope.$on("Sources.received", function(event, data) {
        $scope.sources = data;
    });

    /** Cheks if the user loggedin is the owner specified **/
    $scope.isOwner = function(owner) {
        return $rootScope.username == owner;
    }

    /** Sets all the sourcetypes **/
    $scope.sourceTypes = [{sourceType: 'image', nl_translation: 'Plaatje'}];
    $scope.selectedSourceType = $scope.sourceTypes[0];

    /** Creates a source **/
    $scope.submitSource = function() {
        var source = {}

        switch($scope.selectedSourceType.sourceType) {
            case "image":
                Source.uploadImage($scope, $("#fileInput")[0]);

                $scope.$on("Source.upload.completed", function(event, data) {
                    source.objectId = data.objectId;
                    source.thumbnailId = data.thumbnailId;
                    source.description = "some fancy description";

                    Source.create($scope, source);
                });
                break;
            default:
                break;
        }
    }
    $scope.$on("Source.added", function(event, data) {
        $location.path('/sources');
    });

    /** Deletes a source **/
    $scope.deleteSource = function(source) {
        if($window.confirm("Weet u zeker dat u deze bron wilt verwijderen?")) {
            Source.delete($scope, source);
        }
    }
    $scope.$on("Source.deleted", function(event) {
        Source.getAll($scope);
    });
}

/**
 * This object represetns the links questions to exam page
 * @param $scope is the object itself
 * @param $routeParams gives the programmer access to the url params 
 * @param Tag API for the tag backend
 * @param DifficultyLevel API for the DifficultyLevel backend 
 * @param Exam API for the exam backend
 * @param Question API for the question backend
 * @constructor
 */
function LinkQuestionExamCtrl($scope, $routeParams, Tag, DifficultyLevel, Exam, Question) {
    /** Gets all the tags **/
    $scope.tags = Tag.getAll($scope);
    $scope.$on("Tags.received", function(event, data) {
        $scope.tags = data;
    });

    /** Gets all the difficultyLevels **/
    $scope.difficultyLevels = DifficultyLevel.getAll($scope);
    $scope.$on('DifficultyLevels.received', function(event, data) {
        $scope.difficultyLevels = data;
    });

    /** Sets the question types **/
    $scope.questionTypes = [{questionType: 'OpenQuestion', nl_translation: 'Open vraag'},
        {questionType: 'MultipleChoiceQuestion', nl_translation: 'Meerkeuze vraag'},
        {questionType: 'YesNoQuestion', nl_translation: 'Enkele Stellingvraag'},
        {questionType: 'TheoremQuestion', nl_translation: 'Dubbele Stellingvraag'}];

    /** Gets called when the filter is changed **/
    $scope.differentiateQuestions = function(questions, exam) {
        if(questions == null) questions = [];
        if(exam == null) {
            exam = {};
            exam.questions=[];
        }

        var examQuestionIds = {};
        exam.questions.forEach(function(examQuestion) {
            examQuestionIds[examQuestion.id] = examQuestion;
        });

        return questions.filter(function(obj) {
            return !(obj._id.$oid in examQuestionIds);
        });
    }

    /** Gets the exam **/
    Exam.get($scope, $routeParams.id);
    $scope.$on("Exam.received", function(event, data) {
        $scope.exam = data;
        $scope.questions = $scope.differentiateQuestions(Question.getAll($scope), $scope.exam);
    });
    $scope.$on("Questions.received", function(event, data) {
        $scope.questions = $scope.differentiateQuestions(data, $scope.exam);
    });

    /** Updates the exam **/
    $scope.$on("Updated Exam", function(event, data) {
        Exam.update($scope, $scope.exam._id.$oid, $scope.exam);
    });

    /** Filters the questions **/
    $scope.filterQuestions = function() {
        var filter;
        var tags = Array();
        var difficultyLevels = Array();
        var questionTypes = Array();
        _.each($scope.tags, function(element) {
            if(element.selected) {
                tags.push(element.tagname);
            }
        })
        _.each($scope.difficultyLevels, function(element) {
            if(element.selected) {
                difficultyLevels.push(element.difficulty);
            }
        })
        _.each($scope.questionTypes, function(element) {
            if(element.selected) {
                questionTypes.push(element.questionType);
            }
        })
        filter = {
            "tags": tags,
            "difficulty": difficultyLevels,
            "question_type": questionTypes
        }

        Question.filter($scope, filter);
    };
}