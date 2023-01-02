package br.com.otaviolms.termo.interfaces

interface JogoCallback {
    fun onAcertou(numeroDeTentativas: Int, recomecar: () -> Unit)
    fun onGameOver(recomecar: () -> Unit)
}