package com.thebrownfoxx.kard.logic

import com.thebrownfoxx.kard.logic.extension.normalize
import com.thebrownfoxx.kard.logic.turn.Card
import com.thebrownfoxx.kard.logic.turn.TurnType

data class Player(
    val id: Int,
    val name: String,
    val hp: Int,
    val cards: List<Card>,
) {
    constructor(id: Int, name: String) : this(
        id = id,
        name = name,
        hp = MaxHp,
        cards = Array(5) { Card.draw() }.toList(),
    )

    companion object {
        const val MaxHp = 100
        const val MaxDamage = 25
        const val HealAmount = 50
    }

    val dead get() = hp <= 0

    val availableTurnTypes = TurnType.values().filter { it.card in cards || it.card == null }

    fun damagedBy(amount: Int) = copy(hp = (hp - amount).normalize(0, MaxHp))

    fun healedBy(amount: Int) = copy(hp = (hp + amount).normalize(0, MaxHp))

    fun usedCard(card: Card?) = copy(cards = if (card != null) cards - card else cards)
        .run { if (card == null) drawnCard() else this }

    private fun drawnCard() = copy(cards = cards + Card.draw())
}