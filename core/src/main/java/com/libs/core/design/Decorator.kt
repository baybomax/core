package com.libs.core.design

/**
 * Created by DengBo on 2020-04-11.
 */

interface Beverage {
    val description: String
    val worth: Int

    fun display()
    fun cost(): Int
}

abstract class CondimentDecorator: Beverage {
    override val description: String
        get() = "调料："
}

class HouseBlend: Beverage {
    override val description: String
        get() = "HouseBlend"
    override val worth: Int
        get() = 10
    override fun display() {
        println("$description : ${cost()} ¥")
    }
    override fun cost(): Int {
        return worth
    }
}

class DarkRoast: Beverage {
    override val description: String
        get() = "DarkRoast"
    override val worth: Int
        get() = 20
    override fun display() {
        println("$description : ${cost()} ¥")
    }
    override fun cost(): Int {
        return worth
    }
}

class MoCha(val decorator: Beverage): CondimentDecorator() {
    override val description: String
        get() = super.description + "Mocha"

    override val worth: Int
        get() = 2

    override fun display() {
        println("$description ${decorator.description} ${cost()} $")
    }

    override fun cost(): Int {
        return worth + decorator.cost()
    }

}

class Whip(val decorator: Beverage): CondimentDecorator() {
    override val description: String
        get() = super.description + "Whip"

    override val worth: Int
        get() = 4

    override fun display() {
        println("$description ${decorator.description} ${cost()} $")
    }

    override fun cost(): Int {
        return worth + decorator.cost()
    }

}

class DecoratorMan {
    fun main() {
        val hb = HouseBlend()
        val dr = DarkRoast()
        val m = MoCha(hb)
        val w = Whip(m)

        dr.display()
        m.display()
        w.display()
    }
}
