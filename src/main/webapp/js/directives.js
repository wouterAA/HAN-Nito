'use strict';

/** specifies the HAN-Nito directives **/
angular.module('HAN-Nito.directives', []).
    /** Chosen is a jQuery plugin for multi select combobox **/
    directive('chosen', function() {
        return {
            restrict: 'A',
            link: function(scope, element, attr) {
                scope.$watch('tags', function() {
                    element.trigger('liszt:updated');
                });
                element.chosen();
            }
       }
    }).
    /** This directive makes it possible to create a drag and drop frame **/
    directive('dndBetweenList', function($parse) {

        return function(scope, element, attrs) {

            // contains the args for this component
            var args = attrs.dndBetweenList.split(',');
            // contains the args for the target
            var targetArgs = $('#'+args[1]).attr('dnd-between-list').split(',');

            // variables used for dnd
            var toUpdate;
            var target;
            var startIndex = -1;
            var toTarget = true;

            // watch the model, so we always know what element
            // is at a specific position
            scope.$watch(args[0], function(value) {
                toUpdate = value;
            },true);

            // also watch for changes in the target list
            scope.$watch(targetArgs[0], function(value) {
                target = value;
            },true);

            // use jquery to make the element sortable (dnd). This is called
            // when the element is rendered
            $(element[0]).sortable({
                items:'> li',
                scroll: true,
                start:function (event, ui) {
                    // on start we define where the item is dragged from
                    startIndex = ($(ui.item).index());
                    toTarget = false;
                },
                stop:function (event, ui) {
                    var newParent = ui.item[0].parentNode.id;

                    // on stop we determine the new index of the
                    // item and store it there
                    var newIndex = ($(ui.item).index());
                    var toMove = toUpdate[startIndex];

                    // we need to remove him from the configured model
                    toUpdate.splice(startIndex,1);

                    if (newParent == args[1]) {
                        // and add it to the linked list
                        target.splice(newIndex,0,toMove);
                    }  else {
                        toUpdate.splice(newIndex,0,toMove);
                    }

                    //Keep this around pl0x
                    // we move items in the array, if we want
                    // to trigger an update in angular use $apply()
                    // since we're outside angulars lifecycle
//                    scope.$apply(targetArgs[0]);
                    scope.$apply(args[0]);

                    scope.$broadcast("Updated Exam", targetArgs[0]);
                },
                connectWith:'#'+args[1]
            })
        }
    }).
    /** This directive animates the exam if you want to delete it **/
    directive('removeexam', function($window) {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function($scope, element, attrs) {
                element.bind('click', function(e){
                    if($window.confirm("Weet u zeker dat u het examen met als onderwerp: \"" + $scope.exam.subject + "\" wilt verwijderen?")) {
                        $scope.deleteExam($scope.exam);
                        $(element).parent().parent().parent().parent().animate({ height: 'toggle', opacity: 'toggle' }, 'slow');
                    }
                });
            }
        }
    }).
    /** This directive animates the question if you want to delete it **/
    directive('removequestion', function($window) {
        return {
            restrict: 'A',
            require: '?ngModel',
            link: function($scope, element, attrs) {
                element.bind('click', function(e) {
                    if($window.confirm("Weet u zeker dat u de vraag: \"" + $scope.question.question + "\" wilt verwijderen?")) {
                        $scope.deleteQuestion($scope.question);
                        $(element).parent().parent().parent().parent().animate({ height: 'toggle', opacity: 'toggle' }, 'slow');
                    }
                });
            }
        }
    });

/** This module gives the programmer access to the bootstrap modals **/
angular.module('ui.bootstrap.modal', []).directive('modal', ['$parse',function($parse) {
    var backdropEl;
    var body = angular.element(document.getElementsByTagName('body')[0]);
    var defaultOpts = {
        backdrop: true,
        escape: true
    };
    return {
        restrict: 'EA',
        link: function(scope, elm, attrs) {
            var opts = angular.extend(defaultOpts, scope.$eval(attrs.uiOptions || attrs.bsOptions || attrs.options));
            var shownExpr = attrs.modal || attrs.show;
            var setClosed;

            if (attrs.close) {
                setClosed = function() {
                    scope.$apply(attrs.close);
                };
            } else {
                setClosed = function() {
                    scope.$apply(function() {
                        $parse(shownExpr).assign(scope, false);
                    });
                };
            }
            elm.addClass('modal');

            if (opts.backdrop && !backdropEl) {
                backdropEl = angular.element('<div class="modal-backdrop"></div>');
                backdropEl.css('display','none');
                body.append(backdropEl);
            }

            function setShown(shown) {
                scope.$apply(function() {
                    model.assign(scope, shown);
                });
            }

            function escapeClose(evt) {
                if (evt.which === 27) { setClosed(); }
            }
            function clickClose() {
                setClosed();
            }

            function close() {
                if (opts.escape) { body.unbind('keyup', escapeClose); }
                if (opts.backdrop) {
                    backdropEl.css('display', 'none').removeClass('in');
                    backdropEl.unbind('click', clickClose);
                }
                elm.css('display', 'none').removeClass('in');
                body.removeClass('modal-open');
            }
            function open() {
                if (opts.escape) { body.bind('keyup', escapeClose); }
                if (opts.backdrop) {
                    backdropEl.css('display', 'block').addClass('in');
                    backdropEl.bind('click', clickClose);
                }
                elm.css('display', 'block').addClass('in');
                body.addClass('modal-open');
            }

            scope.$watch(shownExpr, function(isShown, oldShown) {
                if (isShown) {
                    open();
                } else {
                    close();
                }
            });
        }
    };
}]);