import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    var isSimplify by remember { mutableStateOf(false) }
    var isOptimize by remember { mutableStateOf(false) }
    var isStructurize by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CodeField(
                    text = textUser,
                    onValueChange = { textUser = it },
                    label = "Your code",
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )

                Column(
                    modifier = Modifier.width(140.dp),
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
                            textGpt = ""
                            textUser = ""
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Clear")
                    }

                    // open settings window
                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Settings")
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
                }

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
            onValueChange = {
                characterCount = it.length
                onValueChange(it)
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth().weight(1f).fillMaxHeight()
        )

        Text(
            text = "$characterCount",
            textAlign = TextAlign.Right,
            style = TextStyle(color = Color.Gray),
            // place text on the right border of textfield
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
