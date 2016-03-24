'use strict';

angular.module('opendatacollectorApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('subCategoria', {
                parent: 'entity',
                url: '/subCategorias',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'opendatacollectorApp.subCategoria.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/subCategoria/subCategorias.html',
                        controller: 'SubCategoriaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('subCategoria');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('subCategoria.detail', {
                parent: 'entity',
                url: '/subCategoria/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'opendatacollectorApp.subCategoria.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/subCategoria/subCategoria-detail.html',
                        controller: 'SubCategoriaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('subCategoria');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'SubCategoria', function($stateParams, SubCategoria) {
                        return SubCategoria.get({id : $stateParams.id});
                    }]
                }
            })
            .state('subCategoria.new', {
                parent: 'subCategoria',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/subCategoria/subCategoria-dialog.html',
                        controller: 'SubCategoriaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombre: null,
                                    descripcion: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('subCategoria', null, { reload: true });
                    }, function() {
                        $state.go('subCategoria');
                    })
                }]
            })
            .state('subCategoria.edit', {
                parent: 'subCategoria',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/subCategoria/subCategoria-dialog.html',
                        controller: 'SubCategoriaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SubCategoria', function(SubCategoria) {
                                return SubCategoria.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('subCategoria', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('subCategoria.delete', {
                parent: 'subCategoria',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/subCategoria/subCategoria-delete-dialog.html',
                        controller: 'SubCategoriaDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SubCategoria', function(SubCategoria) {
                                return SubCategoria.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('subCategoria', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
