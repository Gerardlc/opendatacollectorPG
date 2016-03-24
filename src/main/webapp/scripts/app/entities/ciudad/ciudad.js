'use strict';

angular.module('opendatacollectorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ciudad', {
                parent: 'entity',
                url: '/ciudads',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'opendatacollectorApp.ciudad.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ciudad/ciudads.html',
                        controller: 'CiudadController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ciudad');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('ciudad.detail', {
                parent: 'entity',
                url: '/ciudad/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'opendatacollectorApp.ciudad.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ciudad/ciudad-detail.html',
                        controller: 'CiudadDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('ciudad');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Ciudad', function($stateParams, Ciudad) {
                        return Ciudad.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ciudad.new', {
                parent: 'ciudad',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ciudad/ciudad-dialog.html',
                        controller: 'CiudadDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombre: null,
                                    pais: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ciudad', null, { reload: true });
                    }, function() {
                        $state.go('ciudad');
                    })
                }]
            })
            .state('ciudad.edit', {
                parent: 'ciudad',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ciudad/ciudad-dialog.html',
                        controller: 'CiudadDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Ciudad', function(Ciudad) {
                                return Ciudad.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ciudad', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('ciudad.delete', {
                parent: 'ciudad',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ciudad/ciudad-delete-dialog.html',
                        controller: 'CiudadDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Ciudad', function(Ciudad) {
                                return Ciudad.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ciudad', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
