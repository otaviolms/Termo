package br.com.otaviolms.termo.views.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.otaviolms.termo.enums.TeclaEstadosEnum
import br.com.otaviolms.termo.enums.TeclaEstadosEnum.*
import br.com.otaviolms.termo.interfaces.TecladoCallback
import br.com.otaviolms.termo.ui.theme.*

@Composable
fun Teclado(
    letrasCertas: String,
    letrasPosicoesErradas: String,
    letrasNaoExistentes: String,
    tecladoCallback: TecladoCallback?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.padding(bottom = 18.dp)
    ) {
        LinhaTeclas(
            letras = "QWERTYUIOP",
            letrasCertas = letrasCertas,
            letrasPosicoesErradas = letrasPosicoesErradas,
            letrasNaoExistentes = letrasNaoExistentes,
            tecladoCallback = tecladoCallback,
        )
        LinhaTeclas(
            letras = "ASDFGHJKL",
            letrasCertas = letrasCertas,
            letrasPosicoesErradas = letrasPosicoesErradas,
            letrasNaoExistentes = letrasNaoExistentes,
            tecladoCallback = tecladoCallback
        )
        LinhaTeclas(
            letras = "ZXCVBNM<",
            letrasCertas = letrasCertas,
            letrasPosicoesErradas = letrasPosicoesErradas,
            letrasNaoExistentes = letrasNaoExistentes,
            tecladoCallback = tecladoCallback
        )
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 16.dp)
        ) {
            TeclaGrande("ENVIAR", VerdeQuadrado) { tecladoCallback?.onEnviar() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTeclado() {
    Teclado(
        letrasCertas = "ABTGKS",
        letrasPosicoesErradas = "LIJ",
        letrasNaoExistentes = "TUEN",
        tecladoCallback = null,
    )
}

@Composable
fun LinhaTeclas(
    letras: String,
    letrasCertas: String,
    letrasPosicoesErradas: String,
    letrasNaoExistentes: String,
    tecladoCallback: TecladoCallback?
) {
    Row (
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        letras.map { letra ->
            val estadoTecla = when {
                letrasCertas.contains(letra) -> EXISTE_POSICAO_CERTA
                letrasPosicoesErradas.contains(letra) -> EXISTE_POSICAO_ERRADA
                letrasNaoExistentes.contains(letra) -> NAO_EXISTE
                else -> LIBERADA
            }

            if(letra == '<') {
                TeclaApagar(letra = letra) { tecladoCallback?.onTeclaPressionada(it) }
            } else {
                Tecla(letra = letra, estado = estadoTecla) { tecladoCallback?.onTeclaPressionada(it) }
            }
        }
    }
}

@Composable
fun Tecla(
    letra: Char,
    estado: TeclaEstadosEnum,
    onTeclaPressionada: (Char) -> Unit
) {
    val corFundo = when(estado) {
        EXISTE_POSICAO_CERTA -> BlocoCerto
        EXISTE_POSICAO_ERRADA -> BlocoPosicaoErrado
        NAO_EXISTE -> BlocoErrado
        BLOQUEADA -> BlocoSimples
        LIBERADA -> BlocoSimples
    }
    val corLetras = when(estado) {
        EXISTE_POSICAO_CERTA -> LetraCerta
        EXISTE_POSICAO_ERRADA -> LetraPosicaoErrada
        NAO_EXISTE -> LetraErrada
        BLOQUEADA -> LetraSimples
        LIBERADA -> LetraSimples
    }

    val shape = RoundedCornerShape(10.dp)

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .clip(shape)
            .clickable {
                if (estado != BLOQUEADA) onTeclaPressionada.invoke(letra)
            },
        shape = shape,
        color = corFundo
    ) {
        Text(
            text = letra.uppercase(),
            modifier = Modifier
                .padding(vertical = 13.dp, horizontal = 12.dp)
                .alpha(if (estado == NAO_EXISTE) 0.3f else 1f),
            color = corLetras,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun TeclaApagar(
    letra: Char,
    onTeclaPressionada: (Char) -> Unit
) {
    val shape = RoundedCornerShape(10.dp)

    Surface(
        modifier = Modifier
            .padding(2.dp)
            .clip(shape)
            .clickable { onTeclaPressionada.invoke(letra) },
        shape = shape,
        color = BlocoApagar
    ) {
        Icon(
            Icons.Default.ArrowBack,
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 18.dp),
            contentDescription = "Apagar",
            tint = LetraSimples
        )
    }
}

@Composable
fun RowScope.TeclaGrande(texto: String, cor: Color, onPressed: () -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .clip(shape)
            .weight(1f)
            .clickable { onPressed.invoke() },
        shape = shape,
        color = cor
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 14.dp),
            color = Branco,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
    }
}