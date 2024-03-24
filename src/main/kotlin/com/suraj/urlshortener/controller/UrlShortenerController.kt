package com.suraj.urlshortener.controller

import com.suraj.urlshortener.dto.UrlShortenResponse
import com.suraj.urlshortener.dto.UrlShortenerRequest
import com.suraj.urlshortener.entity.ShortUrl

import com.suraj.urlshortener.service.UrlShortenerService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class UrlShortenerController(private val service: UrlShortenerService) {

    @GetMapping("/")
    fun index(): List<ShortUrl>{
        return service.getAll()
    }

    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody  request: UrlShortenerRequest): UrlShortenResponse{
        val hash = service.encoder(request.url)
        return UrlShortenResponse(hash)

    }

    @GetMapping("/{hash}")
    fun resolve(@PathVariable hash: String): ResponseEntity<HttpStatus>{
        val target = service.decoder(hash) ?: return ResponseEntity.notFound().build()
        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(target))
            .header(HttpHeaders.CONNECTION, "close")
            .build()
    }


}