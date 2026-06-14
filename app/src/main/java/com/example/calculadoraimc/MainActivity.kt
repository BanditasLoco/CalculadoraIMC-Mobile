package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraIMCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    AppNavegacion()
                }
            }
        }
    }
}

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicio"){
        composable("inicio"){
            PantallaInicio(navController)
        }
        composable ("resultado/{nombre}/{imc}") {backStackEntry ->
            val nombreResultado = backStackEntry.arguments?.getString("nombre")?:"El Pepe"
            val imcResultado = backStackEntry.arguments?.getDouble("imc")?:0.0
            PantallaResultado(navController, nombreResultado, imcResultado)
        }
    }

}

@Composable
fun PantallaInicio(navController: NavController){

    var nombre by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var errorPeso by remember { mutableStateOf(false) }
    var errorAltura by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Ingreso de Datos", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Ingresa tu nombre") },
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = peso,
            onValueChange = { 
                peso = it
                errorPeso = false
            },
            label = { Text("Ingresa tu peso en Kg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorPeso
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedTextField(
            value = altura,
            onValueChange = { 
                altura = it
                errorAltura = false
            },
            label = { Text("Ingresa tu altura en metros") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorAltura
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(mensajeError, color = Color.Red)
        Button(onClick = {
            val pesoButton = peso.toDoubleOrNull() ?: -1.0
            val alturaButton = altura.toDoubleOrNull() ?: -1.0

            errorPeso = pesoButton <= 0
            errorAltura = alturaButton <= 0

            if (errorPeso && errorAltura){
                mensajeError = "Ingrese un valor de peso y altura valido"
            }else if (errorPeso){
                mensajeError = "Ingrese un valor de peso valido"
            }else if (errorAltura){
                mensajeError = "Ingrese un valor de altura valido"
            }else {
                mensajeError = ""
                val imc = pesoButton / alturaButton.pow(2)
                navController.navigate("resultado/$nombre/$imc")
            }
        }) {
            Text("Calcular IMC")
        }
    }
}

@Composable
fun PantallaResultado(navController: NavController, nombre: String, imc: Double){


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaInicio() {
    val navControl = rememberNavController()
    PantallaInicio(navController = navControl)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaResultado() {
    val navControl = rememberNavController()
    PantallaResultado(navController = navControl, nombre = "Marcelo", imc = 5.8)
}