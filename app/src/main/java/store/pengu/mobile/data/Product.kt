package store.pengu.mobile.data

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize
data class Product (
    val id: Long,
    val name: String,
    val barcode: String?,
    val productRating: Float,
    val userRating: Int,
    val ratings: List<Int>
)