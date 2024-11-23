package com.swordhealth.catsapp.models

import com.google.gson.annotations.Expose

data class Breed (
    @Expose
    val id: String,
    @Expose
    val name: String,
    @Expose
    val temperament: String,
    @Expose
    val origin: String,
    @Expose
    val description: String,
)