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
fun App() {
    var sourceCode by remember { mutableStateOf("") }
    var resultCode by remember { mutableStateOf("") }
    var sourceLang by remember { mutableStateOf("") }
    var targetLang by remember { mutableStateOf("") }

    var isSimplify by remember { mutableStateOf(false) }
    var isOptimize by remember { mutableStateOf(false) }
    var isStructurize by remember { mutableStateOf(false) }
    var isTranslate by remember { mutableStateOf(false) }
    var isFix by remember { mutableStateOf(false) }
    var isDoc by remember { mutableStateOf(false) }
    var isComment by remember { mutableStateOf(false) }

    val settingsWindowState = remember { mutableStateOf(false) }

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
                    ParamField(
                        text = sourceLang,
                        onValueChange = { sourceLang = it },
                        label = "Source language"
                    )

                    CodeField(
                        text = sourceCode,
                        onValueChange = { sourceCode = it },
                        label = "Your code",
                    )
                }

                Column(
                    modifier = Modifier.width(150.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    ButtonText(
                        text = "Process",
                        onClick = { resultCode = sourceCode },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ButtonText(
                        text = "Copy result",
                        onClick = {
                            val stringSelection = StringSelection(resultCode)
                            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                            clipboard.setContents(stringSelection, null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    ButtonText(
                        text = "<----",
                        onClick = {
                            if (resultCode.isNotEmpty()) sourceCode = resultCode
                            resultCode = ""
                            if (isTranslate) {
                                sourceLang = targetLang
                                targetLang = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )


                    ButtonText(
                        text = "Clear all",
                        onClick = {
                            resultCode = ""
                            sourceCode = ""
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

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
                        onCheckedChange = {
                            targetLang = ""
                            isTranslate = it
                        }
                    )

                    CheckBoxWithText(
                        text = "Fix",
                        isChecked = isFix,
                        onCheckedChange = { isFix = it }
                    )

                    CheckBoxWithText(
                        text = "Doc",
                        isChecked = isDoc,
                        onCheckedChange = { isDoc = it }
                    )

                    CheckBoxWithText(
                        text = "Comment",
                        isChecked = isComment,
                        onCheckedChange = { isComment = it }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    ButtonText(
                        text = "Settings",
                        onClick = { settingsWindowState.value = !settingsWindowState.value },
                        modifier = Modifier.fillMaxWidth().align(Alignment.End)
                    )


                }
                Column(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ParamField(
                        text = if (isTranslate) targetLang else sourceLang,
                        onValueChange = {
                            isTranslate = true
                            targetLang = it
                        },
                        label = "Target language"
                    )


                    CodeField(
                        text = resultCode,
                        onValueChange = {},
                        label = "Processed code",
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                }
            }
        }
    }

    // Settings Window
    if (settingsWindowState.value) {
        Window(
            onCloseRequest = { settingsWindowState.value = false },
            title = "Settings",
            resizable = false,
            state = rememberWindowState(
                width = 600.dp,
                height = 400.dp,
                position = WindowPosition.Aligned(Alignment.Center)
            ),
        ) {
            Settings()
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
            textStyle = TextStyle(fontSize = 12.sp),
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
private fun ParamField(
    text: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    OutlinedTextField(
        value = text,
        textStyle = TextStyle(fontSize = 14.sp),
        onValueChange = onValueChange,
        visualTransformation = VisualTransformation.None,

        label = {
            Text(
                label,
                style = TextStyle(fontSize = 13.sp)
            )
        },
        modifier = Modifier.fillMaxWidth().height(55.dp)
    )
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
private fun ButtonText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text)
    }
}


@Composable
private fun Settings() {
    var apiKey by remember { mutableStateOf("") }
    var defaultSourceLang by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ParamField(
            text = apiKey,
            onValueChange = { apiKey = it },
            label = "Api key"
        )

        ParamField(
            text = defaultSourceLang,
            onValueChange = { defaultSourceLang = it },
            label = "Default code language"
        )

        Spacer(modifier = Modifier.weight(1f)) // Добавляем разделитель

        ButtonText(
            text = "Save",
            onClick = { },
            modifier = Modifier.fillMaxWidth().align(Alignment.End) // Помещаем кнопку внизу
        )
    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "GptCodeTools",
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
