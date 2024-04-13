package processor

import Secrets
import client.GptApiClient


class CodeProcessor {
    private val gptApiClient = GptApiClient(
        apiKey = Secrets().apiKey,
        model = "gpt-4-turbo"
    )

    fun processCode(code: String): String {
        return gptApiClient.request(code)
    }

    fun getPrompt(
        isSimplify: Boolean,
        isOptimize: Boolean,
        isStructurize: Boolean,
        isFix: Boolean,
        isDoc: Boolean,
        isComment: Boolean,
        isImprove: Boolean
    ): String {
        val roleString = ""

        val contextString = ""

        val taskString = ""


        return "Improve kotlin code"
    }
}