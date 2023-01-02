package br.com.otaviolms.termo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = BlocoAtualLetra,
    primaryVariant = BlocoAtualAntes,
    secondary = BlocoCerto,
    background = FundoJogo,
    surface = FundoJogo,
    onPrimary = Branco,
    onSecondary = Branco,
    onBackground = Branco,
    onSurface = Branco,
)


@Composable
fun TermoTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = FundoJogo
    )
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}