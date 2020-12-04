package advent2020

import kotlin.test.Test
import kotlin.test.assertEquals

class Day04Test {
    val demoInput = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
            "byr:1937 iyr:2017 cid:147 hgt:183cm\n" +
            "\n" +
            "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
            "hcl:#cfa07d byr:1929\n" +
            "\n" +
            "hcl:#ae17e1 iyr:2013\n" +
            "eyr:2024\n" +
            "ecl:brn pid:760753108 byr:1931\n" +
            "hgt:179cm\n" +
            "\n" +
            "hcl:#cfa07d eyr:2025 pid:166559648\n" +
            "iyr:2011 ecl:brn hgt:59in"

    @Test
    fun testPart1() {
        assertEquals(2, advent2020.day04.resultPart1(demoInput))
    }

}
