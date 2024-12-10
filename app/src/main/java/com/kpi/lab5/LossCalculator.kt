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


class LossCalculator {

    private var inputsMap by mutableStateOf(mapOf<String, String>())

    private var calculationResult by mutableStateOf("Показники ще не обчислено")

    @Composable
    fun View() {
        InputsScreen(
            inputsMap = inputsMap,
            onValueChange = { key, value ->
                inputsMap = inputsMap.toMutableMap().apply { this[key] = value }
            },
            calculationResult = calculationResult,
            onCalculate = { calculate() }
        )
    }

    private fun calculate() {
        val planLoss = inputsMap["planLoss"]?.toDoubleOrNull() ?: .0
        val emergencyLoss = inputsMap["emergencyLoss"]?.toDoubleOrNull() ?: .0

        val mPlan = 4*5.12*6451
        val mEmergency = 0.01*45*5.12*6451
        val mLoss = planLoss*mPlan + emergencyLoss*mEmergency

        calculationResult = "Збитки від перерв електропостачання:\n%.2f грн".format(mLoss)
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
                label = "Збитки у разі планового вимкнення",
                units = "грн/кВт*год",
                value = inputsMap["planLoss"] ?: "",
                onValueChange = { onValueChange("planLoss", it) }
            )
            InputField(
                label = "Збитки у разі аварійного вимкнення",
                units = "грн/кВт*год",
                value = inputsMap["emergencyLoss"] ?: "",
                onValueChange = { onValueChange("emergencyLoss", it) }
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
