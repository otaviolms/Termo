package br.com.otaviolms.termo.views.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.otaviolms.termo.enums.CharEstadosEnum
import br.com.otaviolms.termo.enums.CharEstadosEnum.*
import br.com.otaviolms.termo.enums.TeclaEstadosEnum
import br.com.otaviolms.termo.ui.theme.*

@Preview(showBackground = true)
@Composable
fun PreviewCaractereFrame() {
    PalavraRow(palavraDigitada = "TERMO", palavraProcessada = "$$@@f", palavraAtiva = false, jaFoi = true)
}

@Composable
fun PalavraRow(
    palavraDigitada: String = "",
    palavraProcessada: String,
    palavraAtiva: Boolean = false,
    jaFoi: Boolean = false
) {
    Row {
        palavraProcessada.mapIndexed { index, letraProcessada ->
            val letra = palavraDigitada.getOrElse(index) { ' ' }
            val estado = when {
                palavraAtiva && palavraDigitada.trim().length < index -> LETRA_ANTES
                palavraAtiva && palavraDigitada.trim().length == index -> LETRA_ATUAL
                palavraAtiva && palavraDigitada.trim().length > index -> LETRA_DEPOIS
                !jaFoi -> PROXIMAS
                letraProcessada == '@' -> EXISTE_POSICAO_CERTA
                letraProcessada == '$' -> EXISTE_POSICAO_ERRADA
                else -> NAO_EXISTE
            }
            CaractereFrame(letra = letra, estado = estado)
        }
    }
}

@Composable
fun CaractereFrame(letra: Char = ' ', estado: CharEstadosEnum) {
    val corFundo = when(estado) {
        EXISTE_POSICAO_CERTA -> BlocoCerto
        EXISTE_POSICAO_ERRADA -> BlocoPosicaoErrado
        NAO_EXISTE -> BlocoErrado
        PROXIMAS -> BlocoSimplesProximas
        LETRA_ANTES -> BlocoAtualAntes
        LETRA_ATUAL -> BlocoAtualLetra
        LETRA_DEPOIS -> BlocoAtualDepois
    }

    val corLetras = when(estado) {
        EXISTE_POSICAO_CERTA -> LetraCerta
        EXISTE_POSICAO_ERRADA -> LetraPosicaoErrada
        NAO_EXISTE -> LetraErrada
        PROXIMAS,
        LETRA_ANTES,
        LETRA_ATUAL,
        LETRA_DEPOIS -> LetraSimples
    }

    val palavraAtual = estado == LETRA_ANTES || estado == LETRA_ATUAL || estado == LETRA_DEPOIS

    Surface(
        modifier = Modifier
            .animateContentSize(animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ))
            .size(if(palavraAtual) 64.dp else 60.dp)
            .padding(vertical = 2.dp, horizontal = 1.dp),
        shape = RoundedCornerShape(14.dp),
        color = corFundo
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = letra.uppercase(),
                color = corLetras,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.ExtraBold,
                fontSize = if(palavraAtual) 32.sp else 30.sp,
            )
        }
    }
}