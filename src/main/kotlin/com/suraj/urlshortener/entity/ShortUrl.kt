package com.suraj.urlshortener.entity

import jakarta.persistence.*;
import java.time.LocalDateTime

@Entity
@Table(name = "shortened_urls")
data class ShortUrl(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val url: String,
    val hash: String,
    val expires: LocalDateTime = LocalDateTime.now().plusDays(5)
)