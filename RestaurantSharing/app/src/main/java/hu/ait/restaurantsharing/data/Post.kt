package hu.ait.restaurantsharing.data

data class Post(
    var uid: String = "",
    var author: String = "",
    var restaurantName: String ="",
    var cuisine: Int = 0,
    var dishes: String = "",
    var rating: Float = 0.0f,
    var priceRange: String = "",
    var img1: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0

)