'use strict';

/* Filters */
angular.module('HAN-Nito.filters', []).
  /** makes a nice formatted date of a provided timestamp **/
  filter('moment', function() {
        return function(dateString, format) {
            moment().lang('nl');
            return moment(dateString).format(format);
        }
    });
