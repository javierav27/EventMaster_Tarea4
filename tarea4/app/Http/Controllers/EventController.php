// app/Http/Controllers/Api/EventController.php
<?php

use App\Http\Controllers\Controller;
use App\Models\Event;
use Illuminate\Http\Request;

class EventController extends Controller {
    public function index(Request $request) {
        $query = Event::with('category');
        if ($request->has('category_id')) {
            $query->where('category_id', $request->category_id);
        }
        return response()->json($query->get());
    }
    public function show(Event $event) {
        return response()->json($event->load('category'));
    }
    public function store(Request $request) {
        $validated = $request->validate([
            'name' => 'required|string',
            'description' => 'required|string',
            'date' => 'required|date',
            'location' => 'required|string',
            'category_id' => 'required|exists:categories,id'
        ]);
        $event = Event::create($validated);
        return response()->json($event->load('category'), 201);
    }
}
