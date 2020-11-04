package dev.fritz2.elemento

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.beEmpty
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.string.shouldNotEndWith
import io.kotest.matchers.string.shouldNotStartWith
import io.kotest.property.Arb
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class IdTests : FunSpec({

    test("simple") {
        Id.build("foo") shouldBe "foo"
    }

    test("additional ids") {
        Id.build("foo", "bar", "1-2", "3") shouldBe "foo-bar-1-2-3"
    }

    test("empty and null") {
        Id.build("") shouldBe beEmpty()
        Id.build("-") shouldBe beEmpty()
        Id.build("!@#$%^") shouldBe beEmpty()
        Id.build("foo", "", "", "bar", "") shouldBe "foo-bar"
    }

    test("complex") {
        Id.build("lorem-ipsum") shouldBe "lorem-ipsum"
        Id.build("Lorem Ipsum") shouldBe "lorem-ipsum"
        Id.build("Lorem", "Ipsum") shouldBe "lorem-ipsum"
        Id.build(" Lorem ", " Ipsum ") shouldBe "lorem-ipsum"
        Id.build("l0rem ip5um") shouldBe "l0rem-ip5um"
        Id.build("l0rem", "ip5um") shouldBe "l0rem-ip5um"
        Id.build(" l0rem ", " ip5um ") shouldBe "l0rem-ip5um"
        Id.build("""lorem §±!@#$%^&*()=_+[]{};'\:"|,./<>?`~ ipsum""") shouldBe "lorem-ipsum"
        Id.build("lorem", """§±!@#$%^&*()=_+[]{};'\:"|,./<>?`~""", "ipsum") shouldBe "lorem-ipsum"
    }

    test("similar") {
        // must result in the same ID
        val id1 = Id.build("foo_bar")
        val id2 = Id.build("foo-bar")
        val id3 = Id.build("foo bar")

        id1 shouldBe id2
        id1 shouldBe id3
        id2 shouldBe id3

        // must result in the same ID
        val id4 = Id.build("foobar")
        val id5 = Id.build("fooBar")
        id4 shouldBe id5

        // must not be the same
        id1 shouldNotBe id4
    }

    test("no special chars") {
        checkAll(Arb.string()) {
            val id = Id.build(it)
            id shouldNotContain """\s+""".toRegex()
            id shouldNotContain "[^a-zA-Z0-9-_]".toRegex()
            id shouldNotContain "[A-Z]".toRegex()
            id shouldNotContain "-{2,}".toRegex()
            id shouldNotContain "_"
            id shouldNotStartWith "-"
            id shouldNotEndWith "-"
        }
    }

    test("unique") {
        val ids = mutableSetOf<String>()
        for (i in 0 until 100) {
            ids.add(Id.unique())
        }
        ids.size shouldBe 100 // must contain 100 unique IDs
    }
})
