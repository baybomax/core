package com.libs.core.design

/**
 * Created by DengBo on 2020-04-06.
 */

interface SwimBehavior {
    fun swim()
}

interface QuackBehavior {
    fun quack()
}

class SwimBehavior1: SwimBehavior {
    override fun swim() {
        println("Swim behavior 1")
    }
}

class SwimBehavior2: SwimBehavior {
    override fun swim() {
        println("Swim behavior 2")
    }
}

class QuackBehavior1: QuackBehavior {
    override fun quack() {
        println("Quack behavior 1")
    }
}

class QuackBehavior2: QuackBehavior {
    override fun quack() {
        println("Quack behavior 2")
    }
}

abstract class Duck {

    var swimBehavior : SwimBehavior? = null
    var quackBehavior: QuackBehavior? = null

    /**
     * The display of duck.
     */
    abstract fun display()

    /**
     * The swim behavior of duck.
     */
    fun swim() = swimBehavior?.swim()

    /**
     * The quack behavior of duck.
     */
    fun quack() = quackBehavior?.quack()
}

class Duck1: Duck() {
    override fun display() {
        println("Duck display 1")
    }
}

class Duck2: Duck() {
    override fun display() {
        println("Duck display 2")
    }
}

class main {
    fun start() {
        // 1. set duck 1
        val duck1 = Duck1().apply {
            swimBehavior  = SwimBehavior1()
            quackBehavior = QuackBehavior1()
        }

        // 2. set duck 2
        val duck2 = Duck2().apply {
            swimBehavior  = SwimBehavior2()
            quackBehavior = QuackBehavior2()
        }

        // 3. duck 1
        duck1.run {
            display()
            quack()
            swim()

            Unit
        }

        // 4. duck 2
        duck2.run {
            display()
            quack()
            swim()

            Unit
        }
    }
}
