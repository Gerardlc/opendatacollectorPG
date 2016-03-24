'use strict';

describe('Controller Tests', function() {

    describe('MigracionBarrio Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMigracionBarrio, MockBarrio;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMigracionBarrio = jasmine.createSpy('MockMigracionBarrio');
            MockBarrio = jasmine.createSpy('MockBarrio');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MigracionBarrio': MockMigracionBarrio,
                'Barrio': MockBarrio
            };
            createController = function() {
                $injector.get('$controller')("MigracionBarrioDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opendatacollectorApp:migracionBarrioUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
