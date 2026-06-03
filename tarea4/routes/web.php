<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\CategoryController;
use App\Http\Controllers\Api\EventController;

Route::apiResource('categories', CategoryControllerController::class)->only(['index','store']);
Route::get('events', [EventController::class, 'index']);
Route::get('events/{event}', [EventController::class, 'show']);
Route::post('events', [EventController::class, 'store']);

Route::get('/', function () {
    return view('welcome');
});
