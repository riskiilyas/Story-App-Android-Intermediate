package com.riskee.livestorybyriski.data.response

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("story") val story: Story
)