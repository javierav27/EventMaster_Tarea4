
<?php

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Event extends Model {
    use HasFactory;
    protected $fillable = ['name','description','date','location','category_id'];
    protected $casts = ['date' => 'date:Y-m-d'];
    public function category() { return $this->belongsTo(Category::class); }
}