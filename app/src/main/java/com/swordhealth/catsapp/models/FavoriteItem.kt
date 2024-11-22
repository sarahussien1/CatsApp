package com.swordhealth.catsapp.models

import java.util.Date

data class FavoriteItem (
    val id :Long,
    val imageId: String,
    val subId: String,
    val createdAt: Date
)