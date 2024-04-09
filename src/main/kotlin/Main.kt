import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
@Preview
fun App() {
    var textUser by remember { mutableStateOf("") }
    var textGpt by remember { mutableStateOf("") }
    var sourceLang by remember { mutableStateOf("") }
    var targetLang by remember { mutableStateOf("") }

    var isSimplify by remember { mutableStateOf(false) }
    var isOptimize by remember { mutableStateOf(false) }
    var isStructurize by remember { mutableStateOf(false) }
    var isTranslate by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = sourceLang,
                        textStyle = TextStyle(fontSize = 14.sp),
                        onValueChange = { sourceLang = it },
                        visualTransformation = VisualTransformation.None,

                        label = {
                            Text(
                                "Source language",
                                style = TextStyle(fontSize = 13.sp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )

                    CodeField(
                        text = textUser,
                        onValueChange = { textUser = it },
                        label = "Your code",
                    )
                }

                Column(
                    modifier = Modifier.width(150.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { textGpt = textUser },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Process")
                    }

                    Button(
                        onClick = {
                            val stringSelection = StringSelection(textGpt)
                            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                            clipboard.setContents(stringSelection, null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Copy result")
                    }

                    Button(
                        onClick = {
                            textUser = textGpt
                            textGpt = ""
                            if (isTranslate) {
                                sourceLang = targetLang
                                targetLang = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("<----")
                    }

                    Button(
                        onClick = {
                            textGpt = ""
                            textUser = ""
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Clear")
                    }

                    CheckBoxWithText(
                        text = "Simplify",
                        isChecked = isSimplify,
                        onCheckedChange = { isSimplify = it }
                    )

                    CheckBoxWithText(
                        text = "Optimize",
                        isChecked = isOptimize,
                        onCheckedChange = { isOptimize = it }
                    )

                    CheckBoxWithText(
                        text = "Structurize",
                        isChecked = isStructurize,
                        onCheckedChange = { isStructurize = it }
                    )

                    CheckBoxWithText(
                        text = "Translate",
                        isChecked = isTranslate,
                        onCheckedChange = { isTranslate = it }
                    )

                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Settings")
                    }

                }
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = if (isTranslate) targetLang else sourceLang,
                        textStyle = TextStyle(fontSize = 14.sp),
                        onValueChange = {
                            if (isTranslate) targetLang = it
                        },
                        visualTransformation = VisualTransformation.None,

                        label = {
                            Text(
                                "Target language",
                                style = TextStyle(fontSize = 13.sp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(55.dp)
                    )


                    CodeField(
                        text = textGpt,
                        onValueChange = {},
                        label = "Processed code",
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }


            }
        }
    }
}

@Composable
private fun CodeField(
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    var characterCount by remember { mutableStateOf(text.length) }

    Column(
        modifier = modifier,
    ) {
        TextField(
            value = text,
            textStyle = TextStyle(fontSize = 14.sp),
            onValueChange = {
                characterCount = it.length
                onValueChange(it)
            },
            label = {
                Text(label)
            },
            modifier = Modifier.fillMaxWidth().weight(1f).fillMaxHeight()
        )

        Text(
            text = "$characterCount",
            textAlign = TextAlign.Right,
            style = TextStyle(color = Color.Gray),
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun CheckBoxWithText(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
        )
        Text(
            text = text,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
private fun Settings() {
    var apiKey by remember { mutableStateOf("API key") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("API key") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

//todo add counter of tokens and of price

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Gpt code improver",
        resizable = true,
        state = rememberWindowState(
            width = 1300.dp,
            height = 700.dp,
            position = WindowPosition.Aligned(Alignment.Center)
        ),
    ) {
        App()
    }
}
