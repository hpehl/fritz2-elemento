package dev.fritz2.elemento

import dev.fritz2.dom.html.render
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement

class ElementsTests : FunSpec({

    test("DOM token lists") {
        val element = prepareElement()
        element.classList += "foo"
        element.classList.contains("foo") shouldBe true
        element.classList -= "foo"
        element.classList.contains("foo") shouldBe false
    }

    test("append all") {
        val elements = listOf(
            document.createElement("div"),
            document.createElement("div"),
            document.createElement("div")
        )
        val parent = prepareElement()
        parent.appendAll(elements)
        parent.childElementCount shouldBe 3
    }

    test("remove from parent") {
        val parent = prepareElement()
        val child = document.createElement("div")
        parent.appendChild(child)

        child.removeFromParent()
        child.parentElement shouldBe null
        parent.childElementCount shouldBe 0
    }

    test("remove from parent: none-existing element") {
        document.getElementById("foo").removeFromParent()
    }

    test("remove from parent: no parent") {
        val child = document.createElement("div")
        child.removeFromParent()
    }

    test("elements") {
        val elements = elements {
            div { +"0" }
            div { +"1" }
            div { +"2" }
        }
        elements.size shouldBe 3
        elements[0].textContent shouldBe "0"
        elements[1].textContent shouldBe "1"
        elements[2].textContent shouldBe "2"
    }

    test("aria and tag") {
        val tag = render { div { } }
        tag.aria["a0"] = "0"
        tag.aria["aria-a1"] = "1"

        tag.domNode.getAttribute("aria-a0") shouldBe "0"
        tag.domNode.getAttribute("aria-a1") shouldBe "1"

        ("foo" in tag.aria) shouldBe false
        tag.aria["bar"] shouldBe ""
    }

    test("aria and element") {
        val element = prepareElement()
        element.aria["a0"] = "0"
        element.aria["aria-a1"] = "1"

        element.getAttribute("aria-a0") shouldBe "0"
        element.getAttribute("aria-a1") shouldBe "1"

        ("foo" in element.aria) shouldBe false
        element.aria["bar"] shouldBe ""
    }

    test("hidden") {
        val element = prepareElement()
        element.getAttribute("hidden") shouldBe null

        element.hidden = true
        element.getAttribute("hidden") shouldNotBe null

        element.hidden = false
        element.getAttribute("hidden") shouldBe null
    }

    test("show / hide") {
        val element = prepareElement()

        element.hide()
        element.style.display shouldBe "none"

        element.show()
        element.style.display shouldNotBe "none"
    }
})

private fun prepareElement(): HTMLDivElement {
    val element = render {
        div {}
    }.domNode
    document.body!!.appendChild(element)
    return element
}
