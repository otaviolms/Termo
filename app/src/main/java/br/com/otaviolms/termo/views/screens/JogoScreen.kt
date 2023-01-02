package br.com.otaviolms.termo.views.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.otaviolms.termo.MainActivity.Companion.totalTentativas
import br.com.otaviolms.termo.extensions.preencherTentativaVazia
import br.com.otaviolms.termo.extensions.preencherTentativasVazias
import br.com.otaviolms.termo.extensions.sortearPalavra
import br.com.otaviolms.termo.interfaces.JogoCallback
import br.com.otaviolms.termo.interfaces.TecladoCallback
import br.com.otaviolms.termo.views.components.PalavraRow
import br.com.otaviolms.termo.views.components.Teclado

@Composable
fun JogoScreen(
    jogoCallback: JogoCallback
) {
    val tentativas = remember { preencherTentativasVazias().toMutableStateList() }
    val tentativasProcessadas = remember { preencherTentativasVazias().toMutableStateList() }
    val palavraSorteada = remember { mutableStateOf(sortearPalavra()) }
    val tentativaAtual = remember { mutableStateOf("") }
    val numeroTentativa = remember { mutableStateOf(0) }

    val letrasCertas = remember { mutableStateOf("") }
    val letrasPosicoesErradas = remember { mutableStateOf("") }
    val letrasNaoExistentes = remember { mutableStateOf("") }

    fun recomecar() {
        for(i in 0 until totalTentativas) {
            tentativas[i] = preencherTentativaVazia()
        }
        palavraSorteada.value = sortearPalavra()
        letrasCertas.value = ""
        letrasPosicoesErradas.value = ""
        letrasNaoExistentes.value = ""
        tentativaAtual.value = ""
        numeroTentativa.value = 0
    }

    fun processarTentativa() {
        var palavraProcessada = "${tentativaAtual.value}"

//                        PROCESSAMENTO DAS LETRAS CERTAS
        palavraSorteada.value.mapIndexed { index, letraOriginal ->
            val letraDigitada = tentativaAtual.value[index]
            if(letraOriginal == letraDigitada) {
                palavraProcessada = StringBuilder(palavraProcessada).also { it.setCharAt(index, '@') }.toString()
                if(!letrasCertas.value.contains(letraOriginal)) letrasCertas.value += letraOriginal
            }
        }

//                        PROCESSAMENTO DAS LETRAS CERTAS NAS POSIÇÕES ERRADAS
        palavraSorteada.value.mapIndexed { index, letraOriginal ->
            val letraDigitada = tentativaAtual.value[index]
            if(letraDigitada != letraOriginal){
                palavraProcessada = palavraProcessada.replaceFirst(letraOriginal, '$')
            }
        }

//                        VERIFICAÇÃO DAS LETRAS CERTAS NAS POSIÇÕES ERRADAS PARA ATUALIZAR O TECLADO
        palavraProcessada.mapIndexed { index, letraProcessada ->
            if(letraProcessada == '$') letrasPosicoesErradas.value += tentativaAtual.value[index]
        }

        tentativas.map { tentativa ->
            tentativa.map {
                if(!letrasCertas.value.contains(it) && !letrasPosicoesErradas.value.contains(it)) letrasNaoExistentes.value += it
            }
        }

        tentativasProcessadas[numeroTentativa.value] = palavraProcessada
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
//        Text(palavraSorteada.value)
        LazyColumn(
            modifier = Modifier.padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(tentativas) { index, tentativa ->
                Log.i("dibugs", "Tentativa: $tentativa")
                PalavraRow(
                    palavraDigitada = tentativa,
                    palavraProcessada = tentativasProcessadas[index],
                    palavraAtiva = index == numeroTentativa.value,
                    jaFoi = index < numeroTentativa.value
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Teclado(
            letrasCertas = letrasCertas.value,
            letrasPosicoesErradas = letrasPosicoesErradas.value,
            letrasNaoExistentes = letrasNaoExistentes.value,
            tecladoCallback = object : TecladoCallback {
                override fun onTeclaPressionada(letra: Char) {
                    if(letra == '<') {
                        tentativaAtual.value = tentativaAtual.value.dropLast(1)
                        tentativas[numeroTentativa.value] = tentativaAtual.value
                    } else {
                        if(tentativaAtual.value.length < 5) {
                            tentativaAtual.value += letra
                            tentativas[numeroTentativa.value] = tentativaAtual.value
                        }
                    }
                }

                override fun onApagar() {
                    tentativaAtual.value = ""
                    tentativas[numeroTentativa.value] = tentativaAtual.value
                }

                override fun onEnviar() {
                    if(tentativaAtual.value == palavraSorteada.value) {
                        Log.i("dibugs", "Parabéns, você acertou a palavra!")
                        jogoCallback.onAcertou(numeroDeTentativas = numeroTentativa.value) { recomecar() }
                    }
                    if(tentativaAtual.value.length == 5) {
                        processarTentativa()

                        numeroTentativa.value++
                        tentativaAtual.value = ""
                    }
                    if(numeroTentativa.value == totalTentativas) {
                        jogoCallback.onGameOver { recomecar() }
                    }
                }
            }
        )
    }
}