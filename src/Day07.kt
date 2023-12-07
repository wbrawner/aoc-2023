fun main() {
    fun part1(input: List<String>): Long = input.parseHands()
        .sorted()
        .foldIndexed(0) { rank, sum, hand ->
            sum + hand.bid * (rank + 1)
        }

    fun part2(input: List<String>): Long = input.parseHands()
        .sorted()
        .foldIndexed(0) { rank, sum, hand ->
            sum + hand.bid * (rank + 1)
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(Hand("AAAAJ").type == Hand.Type.FIVE_OF_A_KIND)
    check(Hand("AAAKJ").type == Hand.Type.FOUR_OF_A_KIND)
    check(Hand("AAAJJ").type == Hand.Type.FIVE_OF_A_KIND)
    check(Hand("AAKKJ").type == Hand.Type.FULL_HOUSE)
    check(Hand("AAKQJ").type == Hand.Type.THREE_OF_A_KIND)
    check(Hand("AKQJT").type == Hand.Type.ONE_PAIR)
    check(Hand("AAKJJ").type == Hand.Type.FOUR_OF_A_KIND)
    check(Hand("AAJJJ").type == Hand.Type.FIVE_OF_A_KIND)
    check(Hand("AKJJJ").type == Hand.Type.FOUR_OF_A_KIND)
    check(Hand("AJJJJ").type == Hand.Type.FIVE_OF_A_KIND)
    check(Hand("JJJJJ").type == Hand.Type.FIVE_OF_A_KIND)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

data class Hand(val cards: String, val bid: Long = 0L) : Comparable<Hand> {
    val type: Type

    init {
        val cardCounts = mutableMapOf<Char, Int>()
        cards.forEach { card ->
            val count = cardCounts[card] ?: 0
            cardCounts[card] = count + 1
        }
        type = when (cardCounts.keys.size) {
            1 -> Type.FIVE_OF_A_KIND
            2 -> when (cardCounts.entries.maxOf { it.value }) {
                4 -> Type.FOUR_OF_A_KIND.withJokers(cardCounts['J'])
                else -> Type.FULL_HOUSE.withJokers(cardCounts['J'])
            }

            3 -> when (cardCounts.entries.maxOf { it.value }) {
                3 -> Type.THREE_OF_A_KIND.withJokers(cardCounts['J'])
                else -> Type.TWO_PAIR.withJokers(cardCounts['J'])
            }

            4 -> Type.ONE_PAIR.withJokers(cardCounts['J'])
            5 -> Type.HIGH_CARD.withJokers(cardCounts['J'])
            else -> throw IllegalArgumentException("Invalid number of cards in hand")
        }
    }

    enum class Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND,
    }

    private fun Type.withJokers(jokers: Int?): Type {
        if (jokers == null || jokers == 0) return this
        return when (this) {
            Type.FOUR_OF_A_KIND -> Type.FIVE_OF_A_KIND
            Type.THREE_OF_A_KIND -> Type.FOUR_OF_A_KIND
            Type.FULL_HOUSE -> Type.FIVE_OF_A_KIND
            Type.TWO_PAIR -> when (jokers) {
                1 -> Type.FULL_HOUSE
                2 -> Type.FOUR_OF_A_KIND
                else -> throw IllegalStateException("Two of a pair with more than 2 jokers")
            }
            Type.ONE_PAIR -> Type.THREE_OF_A_KIND
            Type.HIGH_CARD -> Type.ONE_PAIR
            Type.FIVE_OF_A_KIND -> Type.FIVE_OF_A_KIND
        }
    }

    override fun compareTo(other: Hand): Int = if (type == other.type) {
        repeat(cards.length) { cardIndex ->
            val comparison = cards[cardIndex].cardValue().compareTo(other.cards[cardIndex].cardValue())
            if (comparison != 0) {
                return@compareTo comparison
            }
        }
        0
    } else {
        type.compareTo(other.type)
    }
}

fun Char.cardValue() = when (this) {
    'A' -> 14
    'K' -> 13
    'Q' -> 12
    'J' -> 1 // Part1: 11
    'T' -> 10
    else -> digitToInt()
}

fun List<String>.parseHands(): List<Hand> = map {
    val (cards, bid) = it.split(' ')
    Hand(cards, bid.toLong())
}