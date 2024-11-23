package com.swordhealth.catsapp.models

import com.google.gson.annotations.Expose

data class AddToFavRequest (
    @Expose
    val imageId: String,
    @Expose
    val subId: String
)