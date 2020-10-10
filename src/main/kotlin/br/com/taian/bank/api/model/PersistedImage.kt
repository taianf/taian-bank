package br.com.taian.bank.api.model

import javax.persistence.*
import javax.xml.bind.*

@Entity
@Table(name = "images")
data class PersistedImage(
    @Id @GeneratedValue
    val id: Long = 0,
    @Lob
    val bytes: ByteArray? = null,
    val mime: String = ""
) {
    fun toStreamingURI(): String {
        //We need to encode the byte array into a base64 String for the browser
        val base64 = DatatypeConverter.printBase64Binary(bytes)

        //Now just return a data string. The Browser will know what to do with it
        return "data:$mime;base64,$base64"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PersistedImage

        if (id != other.id) return false
        if (bytes != null) {
            if (other.bytes == null) return false
            if (!bytes.contentEquals(other.bytes)) return false
        } else if (other.bytes != null) return false
        if (mime != other.mime) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (bytes?.contentHashCode() ?: 0)
        result = 31 * result + mime.hashCode()
        return result
    }
}