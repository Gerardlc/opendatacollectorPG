'use strict';

describe('Controller Tests', function() {

    describe('Barrio Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBarrio, MockMigracionBarrio;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBarrio = jasmine.createSpy('MockBarrio');
            MockMigracionBarrio = jasmine.createSpy('MockMigracionBarrio');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Barrio': MockBarrio,
                'MigracionBarrio': MockMigracionBarrio
            };
            createController = function() {
                $injector.get('$controller')("BarrioDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opendatacollectorApp:barrioUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
