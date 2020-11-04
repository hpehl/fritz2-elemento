package dev.fritz2.elemento

import kotlinx.browser.document

public object Id {

    private var counter: Int = 0
    private const val UNIQUE_ID = "id-"

    /** Creates an identifier guaranteed to be unique within this document. */
    public fun unique(): String {
        var id: String
        do {
            id = "$UNIQUE_ID$counter"
            counter = if (counter == Int.MAX_VALUE) {
                0
            } else {
                counter + 1
            }
        } while (document.getElementById(id) != null)
        return id
    }

    /** Creates an identifier guaranteed to be unique within this document. The unique part comes last. */
    public fun unique(id: String, vararg additionalIds: String): String = "${build(id, *additionalIds)}-${unique()}"

    public fun build(id: String, vararg additionalIds: String): String {
        val segments = listOf(id, *additionalIds)
        return segments.joinToString("-") { asId(it) }
            .replace("-{2,}".toRegex(), "-")
            .trimStart('-')
            .trimEnd('-')
    }

    private fun asId(text: String): String = text.split("[-\\s]").asSequence()
        .map { it.replace("""\s+""".toRegex(), "-") }
        .map { it.replace("[^a-zA-Z0-9-_]".toRegex(), "-") }
        .map { it.replace('_', '-') }
        .map { it.toLowerCase() }
        .filter { it.isNotEmpty() }
        .joinToString()
}
