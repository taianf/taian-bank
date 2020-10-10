package br.com.taian.bank.api.model

import org.springframework.web.multipart.*

internal fun MultipartFile.toPersistedImage(): PersistedImage =
    PersistedImage(bytes = this.bytes, mime = this.contentType.toString())
 