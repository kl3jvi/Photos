package com.github.sikv.photos.domain

enum class ListLayout(val spanCount: Int) {
    LIST(1),
    GRID(3);

    companion object {
        fun findBySpanCount(spanCount: Int): ListLayout {
            return if (spanCount == LIST.spanCount) {
                LIST
            } else {
                GRID
            }
        }
    }
}
