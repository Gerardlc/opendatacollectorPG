'use strict';

angular.module('opendatacollectorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('recuento', {
                parent: 'entity',
                url: '/recuentos',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'opendatacollectorApp.recuento.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recuento/recuentos.html',
                        controller: 'RecuentoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('recuento');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('recuento.detail', {
                parent: 'entity',
                url: '/recuento/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'opendatacollectorApp.recuento.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/recuento/recuento-detail.html',
                        controller: 'RecuentoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('recuento');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Recuento', function($stateParams, Recuento) {
                        return Recuento.get({id : $stateParams.id});
                    }]
                }
            })
            .state('recuento.new', {
                parent: 'recuento',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/recuento/recuento-dialog.html',
                        controller: 'RecuentoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('recuento', null, { reload: true });
                    }, function() {
                        $state.go('recuento');
                    })
                }]
            })
            .state('recuento.edit', {
                parent: 'recuento',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/recuento/recuento-dialog.html',
                        controller: 'RecuentoDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Recuento', function(Recuento) {
                                return Recuento.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('recuento', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('recuento.delete', {
                parent: 'recuento',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/recuento/recuento-delete-dialog.html',
                        controller: 'RecuentoDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Recuento', function(Recuento) {
                                return Recuento.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('recuento', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
