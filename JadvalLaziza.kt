class Ustoz(
    val id: Int,
    val ism: String,
    val fan: String
)

class Sinf(
    val id: Int,
    val sinfNomi: String
)

class Dars(
    val fan: String,
    val ustoz: Ustoz,
    val parami: Boolean = false
)

fun main() {
    val ustozlar = mutableListOf<Ustoz>()
    ustozlar.add(Ustoz(1, "Z.Amirov", "Algebra"))
    ustozlar.add(Ustoz(2, "X.Matchonov", "Fizika"))
    ustozlar.add(Ustoz(3, "M.Mirg`iyosov", "Mobil"))
    ustozlar.add(Ustoz(4, "Z.Amirov", "Geometriya"))
    ustozlar.add(Ustoz(5, "X.Saydaliyeva", "Adabiyot"))
    ustozlar.add(Ustoz(6, "X.Saydaliyeva", "Ona tili"))
    ustozlar.add(Ustoz(7, "T.Choriyev", "CHQBT"))
    ustozlar.add(Ustoz(8, "N.Abdujalilova", "O'zbekiston tarixi"))
    ustozlar.add(Ustoz(9, "N.Abdujalilova", "Jahon tarixi"))
    ustozlar.add(Ustoz(10, "A.Butayev", "Jismoniy tarbiya"))
    ustozlar.add(Ustoz(11, "M.Sharipova", "Rus tili"))
    ustozlar.add(Ustoz(12, "M.Shoraxmetova", "Tarbiya"))
    ustozlar.add(Ustoz(13, "N.Gavhar", "Kelajak soati"))
    ustozlar.add(Ustoz(14, "Sh.Gozal", "IELTS"))
    ustozlar.add(Ustoz(15, "Sh.Gozal", "Ingliz"))

    val sinf = Sinf(1, "10-03")

    val kunlar = listOf("Dushanba", "Seshanba", "Chorshanba", "Payshanba", "Juma")
    val kunlikDarslar = mapOf(
        "Dushanba" to 6,
        "Seshanba" to 7,
        "Chorshanba" to 6,
        "Payshanba" to 7,
        "Juma" to 7
    )

    val ustozBand = mutableMapOf<String, MutableMap<Int, Ustoz>>()
    val jadval = mutableMapOf<String, MutableMap<Int, Dars>>()

    for (kun in kunlar) {
        ustozBand[kun] = mutableMapOf()
        jadval[kun] = mutableMapOf()
    }

    val barchaDarslar = mutableListOf<String>().apply {
        repeat(3) { add("Mobil_PARA") }
        repeat(2) { add("Algebra_PARA") }
        repeat(1) { add("Geometriya_PARA") }
        repeat(2) { add("Fizika_PARA") }
        repeat(1) { add("Ingliz_PARA") }
        repeat(1) { add("Tarbiya") }
        repeat(2) { add("Adabiyot") }
        repeat(2) { add("Ona tili") }
        repeat(2) { add("CHQBT") }
        repeat(2) { add("O'zbekiston tarixi") }
        repeat(1) { add("Jahon tarixi") }
        repeat(1) { add("Jismoniy tarbiya") }
        repeat(2) { add("Rus tili") }
        repeat(1) { add("IELTS") }
        repeat(1) { add("Kelajak soati") }
    }

    barchaDarslar.shuffle()

    val kunlikRoyxat = mutableMapOf<String, MutableList<String>>()
    val kunQolganSoat = mutableMapOf<String, Int>()

    for (kun in kunlar) {
        kunlikRoyxat[kun] = mutableListOf()
        kunQolganSoat[kun] = kunlikDarslar[kun]!!
    }

    var currentDayIndex = 0

    for (dars in barchaDarslar) {
        val uzunlik = if (dars.endsWith("_PARA")) 2 else 1
        var joylandi = false

        for (i in 0 until kunlar.size) {
            val index = (currentDayIndex + i) % kunlar.size
            val kun = kunlar[index]
            val qolgan = kunQolganSoat[kun]!!

            if (qolgan >= uzunlik) {
                kunlikRoyxat[kun]!!.add(dars)
                kunQolganSoat[kun] = qolgan - uzunlik
                currentDayIndex = (index + 1) % kunlar.size
                joylandi = true
                break
            }
        }

        if (!joylandi) {
            val oxirgiKun = kunlar.last()
            val qolgan = kunQolganSoat[oxirgiKun]!!
            if (qolgan >= uzunlik) {
                kunlikRoyxat[oxirgiKun]!!.add(dars)
                kunQolganSoat[oxirgiKun] = qolgan - uzunlik
            }
        }
    }

    for (kun in kunlar) {
        val buKun = kunlikRoyxat[kun]!!
        var soat = 1
        var index = 0

        while (index < buKun.size) {
            if (soat > kunlikDarslar[kun]!!) break

            val nomi = buKun[index]
            val parami = nomi.endsWith("_PARA")
            val asliFan = nomi.replace("_PARA", "")
            val ustoz = ustozlar.find { it.fan == asliFan }

            if (ustoz == null) {
                index++
                continue
            }

            if (parami) {
                if (soat + 1 <= kunlikDarslar[kun]!!) {
                    if (ustozBand[kun]!![soat] == null && ustozBand[kun]!![soat + 1] == null) {
                        val d = Dars(asliFan, ustoz, true)
                        jadval[kun]!![soat] = d
                        jadval[kun]!![soat + 1] = d
                        ustozBand[kun]!![soat] = ustoz
                        ustozBand[kun]!![soat + 1] = ustoz
                        soat += 2
                        index++
                    } else soat++
                } else soat++
            } else {
                if (ustozBand[kun]!![soat] == null) {
                    val d = Dars(asliFan, ustoz)
                    jadval[kun]!![soat] = d
                    ustozBand[kun]!![soat] = ustoz
                    soat++
                    index++
                } else soat++
            }
        }
    }

    println("========================================")
    println("     ${sinf.sinfNomi} SINFI JADVALI")
    println("========================================")

    for (kun in kunlar) {
        println("\n$kun:")
        val kunDarslari = jadval[kun]!!
        var i = 1
        while (i <= kunlikDarslar[kun]!!) {
            val dars = kunDarslari[i]
            if (dars == null) {
                println("  $i. (Bo'sh)")
                i++
            } else if (dars.parami && kunDarslari[i + 1]?.fan == dars.fan) {
                println("  $i-${i + 1}. ${dars.fan} (PARA) - ${dars.ustoz.ism}")
                i += 2
            } else {
                println("  $i. ${dars.fan} - ${dars.ustoz.ism}")
                i++
            }
        }
    }

    val tanlanganUstoz = ustozlar.find { it.fan == "Algebra" }!!
    println("\n========================================")
    println("  ${tanlanganUstoz.ism} NING JADVALI")
    println("========================================")

    var jami = 0
    for (kun in kunlar) {
        val soatlar = mutableListOf<Int>()
        for (s in 1..kunlikDarslar[kun]!!) {
            if (ustozBand[kun]!![s] == tanlanganUstoz) {
                soatlar.add(s)
                jami++
            }
        }
        if (soatlar.isEmpty()) println("$kun: Dam olish kuni")
        else println("$kun: ${soatlar.joinToString(", ")} soat - ${sinf.sinfNomi}")
    }

    println("\nJami: $jami soat")
}

