package com.libs.core.design

/**
 * Created by DengBo on 2020-04-07.
 */

interface Subject {
    fun registerObserver(o: Observer)
    fun removeObserver(o: Observer)
    fun notifyObservers()
    fun setChanged()
}

interface Observer {
    fun update(s: Subject)
}

interface Display {
    fun display()
}

class Whether: Subject {

    val observers = mutableListOf<Observer>()

    var changed: Boolean = false
    var temp: Int = 0
    var humi: Int = 0

    override fun registerObserver(o: Observer) {
        observers.add(o)
    }

    override fun removeObserver(o: Observer) {
        observers.remove(o)
    }

    override fun notifyObservers() {
        if (changed) {
            observers.forEach {
                it.update(this)
            }
            setChanged()
        }
    }

    override fun setChanged() {
        changed = !changed
    }

    fun measure(temp: Int, humi: Int) {
        this.temp = temp
        this.humi = humi

        setChanged()
        notifyObservers()
    }

}

class DisplayObserver1(s: Subject?): Display, Observer {

    var temp: Int = 0
    var humi: Int = 0

    init {
        s?.registerObserver(this)
    }

    override fun update(s: Subject) {
        (s as? Whether)?.let {
            temp = it.temp
            humi = it.humi
        }
    }

    override fun display() {
        println("Display observer 1: >> temp is $temp, humi is $humi")
    }
}

class observable {
    fun start() {
        val w = Whether()
        val o = DisplayObserver1(w)
        w.measure(10, 100)
        o.display()
    }
}
