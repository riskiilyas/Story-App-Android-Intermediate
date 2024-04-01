package com.riskee.livestorybyriski.data.response

import com.google.gson.annotations.SerializedName

data class ListStoryResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("listStory") val listStory: List<Story>
)