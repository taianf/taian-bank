package br.com.taian.bank.api

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class APIApplication

fun main(args: Array<String>) {
    SpringApplication.run(APIApplication::class.java, *args)
}
