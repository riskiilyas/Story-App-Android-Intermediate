package com.riskee.livestorybyriski

import com.riskee.livestorybyriski.data.response.Story


object DataDummy {

    fun generateDummyStoriesResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val quote = Story(
                i.toString(),
                "User$i",
                "lorem ipsum dolor sit amet $i",
                "https://picsum.photos/id/$i/200/300",
                "2022-01-01T00:00:00Z",
                0.0,
                0.0
            )
            items.add(quote)
        }
        return items
    }
}