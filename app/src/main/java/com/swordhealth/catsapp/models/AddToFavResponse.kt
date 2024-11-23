package com.swordhealth.catsapp.models

import com.google.gson.annotations.Expose

data class AddToFavResponse (
    @Expose
    val message: String,
    @Expose
    val id: Long
)