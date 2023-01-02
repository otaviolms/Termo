package br.com.otaviolms.termo.interfaces

interface TecladoCallback {
    fun onTeclaPressionada(letra: Char)
    fun onApagar()
    fun onEnviar()
}