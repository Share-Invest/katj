package com.hh2.katj.favorite.repository

import com.hh2.katj.favorite.model.entity.Favorite
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface FavoriteRepository : JpaRepository<Favorite, Long> {
    fun findByTitle(title: String): Favorite?
    fun findByUserIdAndId(userId: Long?, favoriteId: Long?): Favorite?

}