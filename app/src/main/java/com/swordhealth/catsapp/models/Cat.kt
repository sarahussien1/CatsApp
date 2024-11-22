package com.swordhealth.catsapp.models

data class Cat(
    val id: String,
    val url: String,
    val breeds: List<Breed>
)