package com.example.nottinder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(onLoginVerified: () -> Unit, onCreateProfileClicked: () -> Unit) {
    //val viewModel: LoginViewModel = hiltViewModel()
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val commonPadding = 20.dp
            val titleSize = 70.sp
            Text(
                text = "Not\nTinder",
                fontSize = titleSize,
                textAlign = TextAlign.Center,
                lineHeight = titleSize,
                modifier = Modifier
                    .padding(commonPadding)
            )

            Spacer(Modifier.weight(1f))

            var username by rememberSaveable { mutableStateOf("") }
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Name") },
                modifier = Modifier.padding(commonPadding)
            )

            val buttonWidth = 150.dp
            Button(
                onClick = { /*viewModel.verifyUser(username)*/ },
                modifier = Modifier.width(buttonWidth)
            ) {
                Text(text = "Login")
            }

            Button(onClick = onCreateProfileClicked, modifier = Modifier.width(buttonWidth)) {
                Text(text = "Create Account", textAlign = TextAlign.Center)
            }

            Spacer(Modifier.weight(1f))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen({}) { }
}