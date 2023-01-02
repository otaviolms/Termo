package br.com.otaviolms.termo

import android.inputmethodservice.Keyboard
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import br.com.otaviolms.termo.interfaces.JogoCallback
import br.com.otaviolms.termo.ui.theme.*
import br.com.otaviolms.termo.views.components.TeclaGrande
import br.com.otaviolms.termo.views.screens.JogoScreen
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TermoTheme {
                JogoApplication()
            }
        }
    }

    companion object {
        const val totalTentativas = 6
        const val numeroLetras = 5
    }
}

@Composable
fun JogoApplication() {
    val exibirPopup = remember { mutableStateOf(false) }
    val gameover = remember { mutableStateOf(true) }

    Surface(modifier = Modifier.fillMaxSize(), color = FundoJogo) {
        JogoScreen(object : JogoCallback {
            override fun onAcertou(numeroDeTentativas: Int, recomecar: () -> Unit) {
                exibirPopup.value = true
                gameover.value = false
                recomecar.invoke()
            }

            override fun onGameOver(recomecar: () -> Unit) {
                exibirPopup.value = true
                gameover.value = true
                recomecar.invoke()
            }
        })
        if(exibirPopup.value){
            Popup(
                alignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = OverlayPopup
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        if(gameover.value) LottieErrou() else LottieAcertou()
                        Row (
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            TeclaGrande(
                                texto = if(gameover.value) "Tentar outra vez" else "Jogar novamente",
                                cor = if(gameover.value) BlocoPosicaoErrado else BlocoCerto
                            ) {
                                exibirPopup.value = false
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun LottieAcertou() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets1.lottiefiles.com/packages/lf20_taz4H9.json"))
    LottieAnimation(composition = composition, iterations = 1)
}

@Composable
fun LottieErrou() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://assets1.lottiefiles.com/packages/lf20_1zvbfarz.json"))
    LottieAnimation(composition = composition, iterations = 1)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TermoTheme {
        JogoApplication()
    }
}