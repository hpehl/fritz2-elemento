package dev.fritz2.elemento

import dev.fritz2.dom.html.RenderContext
import dev.fritz2.dom.html.renderElement
import dev.fritz2.elemento.AttributeOperator.STARTS_WITH
import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLImageElement

internal interface AriaSamples {

    fun RenderContext.tagAria() {
        val tag = div {
            +"Some text"
        }
        tag.aria["label"] = "More info"
        val yes = "label" in tag.aria
        val moreInfo = tag.aria["label"]
    }

    fun elementAria() {
        val element = document.createElement("div")
        element.aria["label"] = "More info"
        val yes = "label" in element.aria
        val moreInfo = element.aria["label"]
    }
}

internal interface BySamples {

    fun complex() {
        By.group(
            By.id("main")
                .desc(
                    By.data("listItem", "foo")
                        .desc(
                            By.element("a")
                                .and(By.attribute("href", "https://", STARTS_WITH))
                                .child(By.classname("fas", "fa-check"))
                        )
                ),
            By.classname("external").and(By.attribute("hidden"))
        )
    }

    fun and() {
        By.element("button").and(By.classname("primary"))
        By.element("input").and(By.attribute("type", "checkbox"))
    }
}

internal interface DebugSamples {

    fun RenderContext.debug() {
        val menu = nav {
            ul {
                li {
                    a { +"Foo"; href("#foo") }
                }
                li {
                    a { +"Bar"; href("#bar") }
                }
            }
        }
        console.log(menu.domNode.debug()) // <nav></nav>

        val link = a {
          href("#foo")
          +"Foo"
        }
        console.log(link.domNode.debug()) // <a href="#foo"></a>

        val img = img { src("./logo.svg")}
        console.log(img.domNode.debug()) // <img src="./logo.svg"/>

        val br = document.createElement("br")
        console.log(br.debug()) // <br/>
    }
}

internal interface IdSamples {

    fun build() {
        Id.build("") // ""
        Id.build("-") // ""
        Id.build("!@#$%^") // ""
        Id.build("lorem", "", "", "ipsum", "") // "lorem-ipsum"
        Id.build("lorem-ipsum") // "lorem-ipsum"
        Id.build("Lorem Ipsum") // "lorem-ipsum"
        Id.build("Lorem", "Ipsum") // "lorem-ipsum"
        Id.build(" Lorem ", " Ipsum ") // "lorem-ipsum"
        Id.build("l0rem ip5um") // "l0rem-ip5um"
        Id.build("l0rem", "ip5um") // "l0rem-ip5um"
        Id.build(" l0rem ", " ip5um ") // "l0rem-ip5um"
        Id.build("""lorem §±!@#$%^&*()=_+[]{};'\:"|,./<>?`~ ipsum""") // "lorem-ipsum"
        Id.build("lorem", """§±!@#$%^&*()=_+[]{};'\:"|,./<>?`~""", "ipsum") // "lorem-ipsum"
    }
}