import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Composable
@Preview
fun App() {
    var textUser by remember { mutableStateOf("Hello, World!") }
    var textGpt by remember { mutableStateOf("Hello, World!") }
    var isSimplify by remember { mutableStateOf(false) }
    var isOptimize by remember { mutableStateOf(false) }
    var isStructure by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextField(
                    value = textUser,
                    onValueChange = { textUser = it },
                    label = { Text("Your code") },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )

                Column(
                    modifier = Modifier.width(130.dp),
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
                        text = "Structure",
                        isChecked = isStructure,
                        onCheckedChange = { isStructure = it }
                    )
                }

                TextField(
                    value = textGpt,
                    onValueChange = {},
                    label = { Text("Processed code") },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}

@Composable
private fun CheckBoxWithText(
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically) // Align the text vertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically) // Align the text vertically
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center, // Center align the text
                modifier = Modifier.padding(start = 8.dp, end = 8.dp) // Add padding for spacing
            )
        }
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
