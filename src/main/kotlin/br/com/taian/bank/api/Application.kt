package br.com.taian.bank.api

import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
