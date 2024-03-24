package com.suraj.urlshortener.repository

import com.suraj.urlshortener.entity.ShortUrl
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface ShortUrlRepository : JpaRepository<ShortUrl, Long> {
    fun findByHash(hash: String): ShortUrl?
    fun findByUrl(url: String): ShortUrl?

    @Modifying
    @Query("DELETE FROM ShortUrl su WHERE su.expires < :expires")
    @Transactional
    fun deleteByExpiresBefore(@Param("expires") expires: LocalDateTime): Int

}