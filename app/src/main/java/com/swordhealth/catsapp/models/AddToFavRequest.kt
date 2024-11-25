/*
 * Copyright (c) 2024. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.swordhealth.catsapp.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddToFavRequest (
    @Expose
    @SerializedName("image_id")
    val imageId: String,
    @Expose
    @SerializedName("sub_id")
    val subId: String
)