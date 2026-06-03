// app/Http/Controllers/Api/CategoryController.php
<?php
use App\Http\Controllers\Controller;
use App\Models\Category;
use Illuminate\Http\Request;

class CategoryController extends Controller {
    public function index() {
        return response()->json(Category::all());
    }
    public function store(Request $request) {
        $validated = $request->validate(['name' => 'required|string|unique:categories']);
        $category = Category::create($validated);
        return response()->json($category, 201);
    }
}