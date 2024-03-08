import edu.ucdavis.cs.ecs36c.Baconator
import org.junit.jupiter.api.Test

class BaconatorTest {

    @Test
    fun executeBasicTest() {
        val b = Baconator("moviedata.csv")
        /*
         * This is FAR from comprehensive on the testing...
         * But it will check that you have the format right
         */
        val testpath = b.getBaconpath("Neil Fingleton")
        assert(testpath.size == 3)
        assert(testpath[0] == "Neil Fingleton")
        assert(testpath[1] == "X-Men: First Class")
        assert(testpath[2] == "Kevin Bacon")
        println("$testpath")

    }

    @Test
    fun executeBasicTest2() {
        val b = Baconator("moviedata.csv")

        val testpath = b.getBaconpath("Dwayne Johnson")
        assert(testpath.size == 5)
    }

    @Test
    fun executeBasicTest3() {
        val b = Baconator("moviedata.csv")

        val testpath = b.getBaconpath("Kevin Bacon")
        assert(testpath.size == 1)
    }

    @Test
    fun executeBasicTest4() {
        val b = Baconator("moviedata.csv")

        val testpath = b.getBaconpath("Avatar")
        assert(testpath.size == 0)
    }

    @Test
    fun executeBasicTest5() {
        val b = Baconator("moviedata.csv")

        val testpath = b.getBaconpath("Yao Chin")
        assert(testpath[0] == "Yao Chin")
        assert(testpath[1] == "Doom")
        assert(testpath[2] == "Dexter Fletcher")
        assert(testpath[3] == "Muppets Most Wanted")
        assert(testpath[4] == "James McAvoy")
        assert(testpath[5] == "X-Men: First Class")
        assert(testpath[6] == "Kevin Bacon")
        println("$testpath")
        assert(testpath.size == 7)
    }
}