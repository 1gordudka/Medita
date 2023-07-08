package com.igordudka.medita.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.igordudka.medita.R
import com.igordudka.medita.ui.theme.background
import com.igordudka.medita.ui.theme.cardColor
import com.igordudka.medita.ui.theme.interFamily
import com.igordudka.medita.ui.theme.size16
import com.igordudka.medita.ui.theme.size18
import com.igordudka.medita.ui.theme.size35
import kotlin.math.sin

@Composable
fun DefaultScreen(
    content: @Composable () -> Unit
) {

    Column(
        Modifier
            .fillMaxSize()
            .background(background)
            .systemBarsPadding(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceEvenly) {
        content()
    }
}

@Composable
fun Logo(
    isBig: Boolean
) {

    Image(painter = painterResource(id = R.drawable.big_logo), contentDescription = null,
        Modifier
            .padding(8.dp)
            .width(if (isBig) 256.dp else 56.dp)
            .height(if (isBig) 171.dp else 38.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    title: String,
    isFailure: Boolean,
    supportingText: String,
    trailingIcon: @Composable () -> Unit
) {

    OutlinedTextField(value = text, onValueChange = { onTextChanged(it) },
    modifier = Modifier.padding(16.dp),
    colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White.copy(alpha = 0.7f),
        unfocusedContainerColor = cardColor,
        focusedContainerColor = cardColor,
        cursorColor = cardColor,
        unfocusedTextColor = Color.White,
        errorSupportingTextColor = Color(0xFF950606),
        errorContainerColor = cardColor,
        errorCursorColor = Color(0xFF950606),
        errorPlaceholderColor = Color.White,
        errorTextColor = Color.White.copy(alpha = 0.7f),
        disabledPlaceholderColor = Color.White,
        focusedPlaceholderColor = Color.White,
        unfocusedPlaceholderColor = Color.White,
        disabledBorderColor = Color.Transparent,
        focusedBorderColor = Color.White,
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = Color(0xFF950606),
        unfocusedSupportingTextColor = Color.White,
        errorTrailingIconColor = Color(0xFF950606)
    ), placeholder = { Text(text = title, fontSize = size18,
        fontFamily = interFamily)},
    shape = RoundedCornerShape(11.dp),
        singleLine = true,
        textStyle = TextStyle(fontSize = size16),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        isError = isFailure,
        supportingText = {
            if (isFailure){
                Text(
                    text = supportingText,
                    fontFamily = interFamily,
                    fontSize = size16,
                )
            }
        },
        trailingIcon = trailingIcon
    )
}

@Composable
fun StatsCard(
    amount: Int,
    description: String,
    modifier: Modifier,
    onClick: () -> Unit
) {

    Card(modifier = modifier
        .padding(12.dp)
        .sizeIn(minHeight = 100.dp, minWidth = 100.dp)
        .clip(RoundedCornerShape(26.dp))
        .clickable {
            onClick()
        }, shape = RoundedCornerShape(26.dp),
    colors = CardDefaults.cardColors(containerColor = cardColor)) {
        Column(Modifier.padding(16.dp)
        ) {
            Row (verticalAlignment = Alignment.Bottom){
                Text(text = "$amount", fontSize = size35, color = Color.White,
                    fontFamily = interFamily)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(id = R.string.min), fontSize = size16, color = Color.White
                    .copy(alpha = 0.8f), fontFamily = interFamily)
            }
            Spacer(modifier = Modifier.height(7.dp))
            Text(text = description, fontSize = size18, color = Color.White
                .copy(alpha = 0.9f), fontFamily = interFamily)
        }
    }
}

@Composable
fun DefaultTextButton(
    text: String,
    onClick: () -> Unit
) {

    TextButton(onClick = onClick) {
        Text(text = text, color = Color.White, fontSize = size35,
        fontWeight = FontWeight.Thin, fontFamily = interFamily)
    }
}

@Composable
fun DefaultNumTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    title: String,
    isFailure: Boolean,
    supportingText: String,
    trailingIcon: @Composable () -> Unit
) {

    OutlinedTextField(value = text, onValueChange = { onTextChanged(it) },
        modifier = Modifier.padding(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White.copy(alpha = 0.7f),
            unfocusedContainerColor = cardColor,
            focusedContainerColor = cardColor,
            cursorColor = cardColor,
            unfocusedTextColor = Color.White,
            errorSupportingTextColor = Color(0xFF950606),
            errorContainerColor = cardColor,
            errorCursorColor = Color(0xFF950606),
            errorPlaceholderColor = Color.White,
            errorTextColor = Color.White.copy(alpha = 0.7f),
            disabledPlaceholderColor = Color.White,
            focusedPlaceholderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            disabledBorderColor = Color.Transparent,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = Color(0xFF950606),
            unfocusedSupportingTextColor = Color.White,
            errorTrailingIconColor = Color(0xFF950606)
        ), placeholder = { Text(text = title, fontSize = size18,
            fontFamily = interFamily)},
        shape = RoundedCornerShape(11.dp),
        singleLine = true,
        textStyle = TextStyle(fontSize = size16),
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences, keyboardType = KeyboardType.Number),
        isError = isFailure,
        supportingText = {
            if (isFailure){
                Text(
                    text = supportingText,
                    fontFamily = interFamily,
                    fontSize = size16,
                )
            }
        },
        trailingIcon = trailingIcon
    )
}

@Composable
fun DefaultDialog(
    content: @Composable () -> Unit,
    title: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit
) {

    AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = confirmButton,
        text = {
            Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
                content()
            }
        }, containerColor = Color(0xFF161616),
    dismissButton = dismissButton, title = title)
}