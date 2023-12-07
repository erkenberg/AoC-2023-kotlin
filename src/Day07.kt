private data class CamelCard(val label: Char, val withJokers: Boolean = false) {
    fun getValue(): Int {
        return when (this.label) {
            'A' -> 14
            'K' -> 13
            'Q' -> 12
            'J' -> if (withJokers) 0 else 11
            'T' -> 10
            else -> this.label.digitToInt()
        }
    }
}

private data class CamelCardHand(val cards: List<CamelCard>, val bid: Int, val withJokers: Boolean = false) :
    Comparable<CamelCardHand> {
    // From https://stackoverflow.com/questions/3463964/regex-to-calculate-straight-poker-hand
    // slightly adapted, expects an ordered list
    val onePairRegex = Regex(".*(\\w)\\1.*")
    val twoPairRegex = Regex(".*(\\w)\\1.*(\\w)\\2.*")
    val threeOfAKindRegex = Regex(".*(\\w)\\1\\1.*")
    val fullHouseRegex = Regex("(\\w)\\1\\1(\\w)\\2|(\\w)\\3(\\w)\\4\\4")
    val fourOfAKindRegex = Regex(".*(\\w)\\1{3}.*")
    val fiveOfAKindRegex = Regex(".*(\\w)\\1{4}.*")

    private fun getTypeStrength(): Int {
        if (withJokers) {
            var best = 0
            for (option in charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')) {
                val sorted = cards.map { if (it.label == 'J') option else it.label }.sorted().joinToString("")
                val new = when {
                    fiveOfAKindRegex.matches(sorted) -> 7
                    fourOfAKindRegex.matches(sorted) -> 6
                    fullHouseRegex.matches(sorted) -> 5
                    threeOfAKindRegex.matches(sorted) -> 4
                    twoPairRegex.matches(sorted) -> 3
                    onePairRegex.matches(sorted) -> 2
                    else -> 1
                }
                if (new > best) best = new
            }
            return best
        }

        val sorted = cards.map { it.label }.sorted().joinToString("")
        return when {
            fiveOfAKindRegex.matches(sorted) -> 7
            fourOfAKindRegex.matches(sorted) -> 6
            fullHouseRegex.matches(sorted) -> 5
            threeOfAKindRegex.matches(sorted) -> 4
            twoPairRegex.matches(sorted) -> 3
            onePairRegex.matches(sorted) -> 2
            else -> 1
        }
    }

    override fun compareTo(other: CamelCardHand): Int {
        val ownTypeStrength = getTypeStrength()
        val otherTypeStrength = other.getTypeStrength()
        if (ownTypeStrength != otherTypeStrength) {
            return ownTypeStrength.compareTo(otherTypeStrength)
        } else {
            cards.forEachIndexed { index, card ->
                if (card.getValue() != other.cards[index].getValue()) {
                    return card.getValue().compareTo(other.cards[index].getValue())
                }
            }
        }
        return 0
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val hands = input.map { it ->
            val (hand, bid) = it.split(whiteSpaceRegex)
            val cards = hand.map { CamelCard(it) }
            CamelCardHand(cards, bid.toInt())
        }
        return hands.sorted().map { it.bid }.reduceIndexed { index, sum, current ->
            sum + current * (index + 1)
        }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map { it ->
            val (hand, bid) = it.split(whiteSpaceRegex)
            val cards = hand.map { CamelCard(it, true) }
            CamelCardHand(cards, bid.toInt(), true)
        }
        return hands.sorted().map { it.bid }.reduceIndexed { index, sum, current ->
            sum + current * (index + 1)
        }
    }

    // test if implementation meets criteria from the description
    check(part1(readInput("Day07_test")) == 6440)
    check(part2(readInput("Day07_test")) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
