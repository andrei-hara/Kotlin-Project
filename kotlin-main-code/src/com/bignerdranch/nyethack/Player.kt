package com.bignerdranch.nyethack

import java.io.File

class Player(_name : String,
             override var healthPoints: Int = 100,
             val isBlessed: Boolean,
             private val isImmortal: Boolean) : Fightable {
    var name = _name
        get() = "${field.capitalize()} of $hometown"
        private set(value){
            field = value.trim()
        }
    val hometown by lazy  {selectHometown()}
    var currentPosition = Coordinate(0, 0)

    init{
        name = _name
        require(healthPoints > 0, {"Health points must be greater than zero."})
        require(name.isNotBlank(), {"Player must have a name."})
    }

    constructor(name : String) : this(name,
        isBlessed = true,
        isImmortal = false) {
        if (name.toLowerCase() == "kar") healthPoints = 40
    }

    fun auraColor(): String {
        val auraVisible = isBlessed && healthPoints > 50 || isImmortal
        var auraColor = if (auraVisible) "GREEN" else "NONE"
        return auraColor
    }

    fun formatHealthStatus() =
        when (healthPoints) {
            100 -> "is in great condition!"
            in 90..99 -> "has a few scratches"
            in 75..89 -> if (isBlessed) "has some minor wounds but will heal quickly" else " has some minor wounds"
            in 15..74 -> "looks pretty hurt"
            else -> "is in awful condition!"
        }

    fun castFireball(numFireballs : Int = 2) =
        println("A glass of fireball springs intro existence. (x$numFireballs)")

    private fun selectHometown() = File("data/towns.txt")
        .readText()
        .split("\n")
        .shuffled()
        .first()
        .replace("\r","")

    override val diceCount = 3

    override val diceSides: Int = 6


    override fun attack(opponent: Fightable) : Int {
        val damageDealt = if(isBlessed){
            damageRoll * 2
        } else{
            damageRoll
        }
        opponent.healthPoints -= damageDealt
        return damageDealt
    }

    override fun toString(): String {
        return name
    }
}