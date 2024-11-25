package com.swordhealth.catsapp.ui.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.swordhealth.catsapp.models.Cat

data class CatUI (
    val cat: Cat,
    var isFavorite: MutableState<Boolean> = mutableStateOf(false),
    var favoriteID: MutableState<Long?> = mutableStateOf(null)
)