package dev.fritz2.elemento

import dev.fritz2.elemento.AttributeOperator.CONTAINS
import dev.fritz2.elemento.AttributeOperator.CONTAINS_TOKEN
import dev.fritz2.elemento.AttributeOperator.CONTAINS_WORD
import dev.fritz2.elemento.AttributeOperator.ENDS_WITH
import dev.fritz2.elemento.AttributeOperator.STARTS_WITH
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ByTests : FunSpec({

    test("selector") {
        By.selector("#foo > .bar:hover").selector shouldBe "#foo > .bar:hover"
    }

    test("id") {
        By.id("id").selector shouldBe "#id"
    }

    test("element") {
        By.element("button").selector shouldBe "button"
    }

    test("classname") {
        By.classname("single-class").selector shouldBe ".single-class"
        By.classname("fas", "fa-check").selector shouldBe ".fas.fa-check"
        By.classname("first", "second", "third").selector shouldBe ".first.second.third"
    }

    test("attribute") {
        By.attribute("attr").selector shouldBe "[attr]"
        By.attribute("attr", "value").selector shouldBe "[attr=value]"
    }

    test("attribute position") {
        By.attribute("attr", "value", STARTS_WITH).selector shouldBe "[attr^=value]"
        By.attribute("attr", "value", ENDS_WITH).selector shouldBe "[attr$=value]"
        By.attribute("attr", "value", CONTAINS).selector shouldBe "[attr*=value]"
        By.attribute("attr", "value", CONTAINS_WORD).selector shouldBe "[attr~=value]"
        By.attribute("attr", "value", CONTAINS_TOKEN).selector shouldBe "[attr|=value]"
    }

    test("attribute quotes") {
        By.attribute("attr", "--value").selector shouldBe """[attr="--value"]"""
        By.attribute(
            "attr",
            "value-with-0815_and-under-score"
        ).selector shouldBe "[attr=value-with-0815_and-under-score]"
        By.attribute("attr", "va.lue").selector shouldBe """[attr="va.lue"]"""
        By.attribute("attr", "v a l u e").selector shouldBe """[attr="v a l u e"]"""
        By.attribute("attr", "http://redhat.com").selector shouldBe """[attr="http://redhat.com"]"""
        By.attribute("attr", "-").selector shouldBe """[attr="-"]"""
        By.attribute("attr", "-1-m").selector shouldBe """[attr="-1-m"]"""
        By.attribute("attr", "0815").selector shouldBe """[attr="0815"]"""
    }

    test("data") {
        By.data("item").selector shouldBe "[data-item]"
        By.data("item", "value").selector shouldBe "[data-item=value]"
        By.data("prettyLongDataItem").selector shouldBe "[data-pretty-long-data-item]"
        By.data("pretty-long-data-item").selector shouldBe "[data-pretty-long-data-item]"
    }

    test("data position") {
        By.data("item", "value", STARTS_WITH).selector shouldBe "[data-item^=value]"
        By.data("item", "value", ENDS_WITH).selector shouldBe "[data-item$=value]"
        By.data("item", "value", CONTAINS).selector shouldBe "[data-item*=value]"
        By.data("item", "value", CONTAINS_WORD).selector shouldBe "[data-item~=value]"
        By.data("item", "value", CONTAINS_TOKEN).selector shouldBe "[data-item|=value]"
    }

    test("and") {
        By.element("div").and(By.classname("class")).selector shouldBe "div.class"
        // the more complicated variant of By.classname("fas", "fa-check")
        By.classname("fas").and(By.classname("fa-check")).selector shouldBe ".fas.fa-check"
    }

    test("descendant") {
        By.classname("foo").desc(By.classname("bar")).selector shouldBe ".foo .bar"
        By.classname("foo")
            .desc(
                By.classname("bar")
                    .desc(By.id("id"))
            ).selector shouldBe ".foo .bar #id"
    }

    test("child") {
        By.classname("foo").child(By.classname("bar")).selector shouldBe ".foo > .bar"
        By.classname("foo")
            .child(
                By.classname("bar")
                    .child(By.id("id"))
            ).selector shouldBe ".foo > .bar > #id"
    }

    test("adjacent sibling") {
        By.classname("foo").adjacentSibling(By.classname("bar")).selector shouldBe ".foo + .bar"
        By.classname("foo")
            .adjacentSibling(
                By.classname("bar")
                    .adjacentSibling(By.id("id"))
            ).selector shouldBe ".foo + .bar + #id"
    }

    test("sibling") {
        By.classname("foo").sibling(By.classname("bar")).selector shouldBe ".foo ~ .bar"
        By.classname("foo")
            .sibling(
                By.classname("bar")
                    .sibling(By.id("id"))
            ).selector shouldBe ".foo ~ .bar ~ #id"
    }

    test("all") {
        By.group(
            By.id("foo"), By.classname("bar"),
            By.element("button")
        ).selector shouldBe "#foo, .bar, button"
    }

    test("different nesting") {
        val selector = "#id ul > .foo ~ [data-item]"
        val toplevel = By.id("id")
            .desc(By.element("ul"))
            .child(By.classname("foo"))
            .sibling(By.data("item"))
        val nested = By.id("id")
            .desc(
                By.element("ul")
                    .child(
                        By.classname("foo")
                            .sibling(By.data("item"))
                    )
            )
        toplevel.selector shouldBe selector
        nested.selector shouldBe selector
        toplevel.selector shouldBe nested.selector
    }

    test("complex") {
        val selector = "#main [data-list-item|=foo] a[href^=\"http://\"] > .fas.fa-check, .external[hidden]"
        val complex = By.group(
            By.id("main")
                .desc(
                    By.data("listItem", "foo", CONTAINS_TOKEN)
                        .desc(
                            By.element("a")
                                .and(By.attribute("href", "http://", STARTS_WITH))
                                .child(By.classname("fas", "fa-check"))
                        )
                ),
            By.classname("external").and(By.attribute("hidden"))
        )
        complex.selector shouldBe selector
    }

    test("identity") {
        val one = By.classname("foo")
        val two = By.classname("foo")
        one shouldBe two
        one.selector shouldBe two.selector
        one.toString() shouldBe two.toString()
        one.hashCode() shouldBe two.hashCode()
    }
})