package com.suraj.urlshortener.service


import com.suraj.urlshortener.entity.ShortUrl

import com.suraj.urlshortener.repository.ShortUrlRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDateTime


@Service
class UrlShortenerService(private val shortUrlRepository: ShortUrlRepository,
                          @Value("\${app.domain}") private val appDomain: String){
    private val digest = MessageDigest.getInstance("SHA-256")

    fun hash(url:String, length: Int = 6) : String{
        val bytes = digest.digest(url.toByteArray())
        val hash = String.format("%32x", BigInteger(1, bytes))

        return hash.take(length)
    }

    fun encoder(url: String): String{
        val entity : ShortUrl? = shortUrlRepository.findByUrl(url)
        if(entity != null){
            return entity.hash
        }
        val hash = hash(url)

        val shortUrlEntity = ShortUrl(url = url, hash = hash)

        val savedEntity = shortUrlRepository.save(shortUrlEntity)


        return hash
    }



    fun decoder(hash:String) : String? {
        val entity = shortUrlRepository.findByHash(hash) ?: return null
        return entity.url
    }


    @Scheduled(cron = "0 0 0 * * ")
    fun deleteExpiredUrls() {
        val now = LocalDateTime.now()
        shortUrlRepository.deleteByExpiresBefore(now)
    }

    fun getAll() : List<ShortUrl> {
        return shortUrlRepository.findAll()
    }







}