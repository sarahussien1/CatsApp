package com.swordhealth.catsapp.ui.models

import com.swordhealth.catsapp.models.Cat

data class CatUI (
    val cat: Cat,
    val isFavorite: Boolean,
    val favoriteID: Long?
)