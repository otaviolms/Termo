package br.com.otaviolms.termo.extensions

import br.com.otaviolms.termo.MainActivity.Companion.numeroLetras
import br.com.otaviolms.termo.MainActivity.Companion.totalTentativas
import br.com.otaviolms.termo.models.palavrasParaSortear

fun sortearPalavra(): String = palavrasParaSortear.random()

fun preencherTentativaVazia(): String = " ".repeat(numeroLetras)

fun preencherTentativasVazias(): ArrayList<String> {
    val tentativasArrayList = arrayListOf<String>()
    for(i in 0 until totalTentativas) tentativasArrayList.add(preencherTentativaVazia())
    return tentativasArrayList
}
