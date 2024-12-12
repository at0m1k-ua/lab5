package com.kpi.lab5

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kpi.lab4.InputField


class ReliabilityCalculator {

    private var inputsMap by mutableStateOf(mapOf<String, String>())

    private var calculationResult by mutableStateOf("Показники ще не обчислено")

    @Composable
    fun View() {
        InputsScreen(
            inputsMap,
            onValueChange = { key, value ->
                inputsMap = inputsMap.toMutableMap().apply { this[key] = value }
            },
            calculationResult = calculationResult,
            onCalculate = { calculate() }
        )
    }

    private fun calculate() {
        val lineLength = inputsMap["lineLength"]?.toDoubleOrNull() ?: .0

        val freq = 0.01 + 0.07 + 0.015 + 0.02 + 0.03*6
        val restorationTime = (0.01*30 + 0.07*lineLength + 0.015*100 + 0.02*15 + 0.18*2) / freq
        val ka = freq * restorationTime / 8760
        val kp = 1.2*43/8760
        val freqDouble = 2*0.295*(ka+kp)
        val freqDoubleSection = freqDouble + 0.02


        calculationResult = """
            Частота відмов одноколової системи: 
            %.5f разів на рік
            
            Частота відмов двоколової системи: 
            %.5f разів на рік
            
            Частота відмов двоколової системи з
            урахуванням секційного вимикача: 
            %.5f разів на рік
        """.trimIndent().format(freq, freqDouble, freqDoubleSection)
    }

    @Composable
    private fun InputsScreen(
        inputsMap: Map<String, String>,
        onValueChange: (String, String) -> Unit,
        calculationResult: String,
        onCalculate: () -> Unit
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(
                label = "Довжина ЛЕП",
                units = "км",
                value = inputsMap["lineLength"] ?: "",
                onValueChange = { onValueChange("lineLength", it) }
            )

            Text(
                calculationResult,
                modifier = Modifier.padding(8.dp)
            )

            Button(
                modifier = Modifier
                    .padding(8.dp)
                    .height(72.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                onClick = { onCalculate() }
            ) {
                Text(
                    "Calculate",
                    fontSize = 24.sp
                )
            }
        }
    }
}
