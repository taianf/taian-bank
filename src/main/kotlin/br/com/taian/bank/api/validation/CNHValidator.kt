package br.com.taian.bank.api.validation

/**
 * Código para validar CNH em java:
 * https://github.com/danielsouza/validarCNH/blob/master/ValidarCNH.java
 */
internal fun validateCNH(cnh: String): Boolean {
    val char1 = cnh[0]
    if (cnh.replace("\\D+".toRegex(), "").length != 11
        || String.format("%0" + 11 + "d", 0).replace('0', char1) == cnh
    ) {
        return false
    }
    var v: Long = 0
    var j: Long = 9
    run {
        var i = 0
        while (i < 9) {
            v += (cnh[i].toInt() - 48) * j
            ++i
            --j
        }
    }
    var dsc: Long = 0
    var vl1 = v % 11
    if (vl1 >= 10) {
        vl1 = 0
        dsc = 2
    }
    v = 0
    j = 1
    var i = 0
    while (i < 9) {
        v += (cnh[i].toInt() - 48) * j
        ++i
        ++j
    }
    val x = v % 11
    val vl2 = if (x >= 10) 0 else x - dsc
    return vl1.toString() + vl2.toString() == cnh.substring(cnh.length - 2)
}
