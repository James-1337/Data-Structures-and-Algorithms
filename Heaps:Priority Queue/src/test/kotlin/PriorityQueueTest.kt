import edu.ucdavis.cs.ecs036c.homework7.PriorityQueue
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.test.assertFailsWith

fun <T, P: Comparable <P> > PriorityQueue<T, P>.isValid() : Boolean {
    if (size != locationData.size) return false
    for(i in 0..< size){
        val (data, priority) = priorityData[i]
        if (locationData[data] != i) return false
        val parentIndex = (i-1)/2
        if (i != 0){
            if (priorityData[parentIndex].second > priority) return false
        }
    }
    return true
}

class PriorityQueueTest2 {

    @Test
    fun testComprehensive() {
        println("You gotta come up with your own test cases")
    }

    @Test
    fun testBadPop(){
        assertFailsWith<Exception> {
            val p = PriorityQueue("1" to 1, "2" to 2)
            p.pop()
            p.pop()
            p.pop()
        }
    }

    @Test
    fun testBadInit(){
        assertFailsWith<Exception> {
            PriorityQueue<String, Int>("1" to 1, "1" to 2)
            return
        }
    }

    @Test
    fun testNormal(){
        for(x in 0..<10) {
            val testData = mutableListOf<Pair<String, Int>>()
            for (i in 0..<100) {
                testData.add(Pair("$i", i))
            }
            val testArray = testData.toTypedArray()
            testArray.shuffle()
            val testHeap = PriorityQueue(*testArray)
            assert(testHeap.isValid())
            for (i in 0..<100) {
                val data = testHeap.pop()
                assert(data == "$i")
                assert(testHeap.isValid())
                assert(testHeap.size == 99 - i)
                assert(testHeap.isValid())
            }
        }
    }

    @Test
    fun testNormalUpdateAdd(){
        for(x in 0..<10) {
            val testData = mutableListOf<Pair<String, Int>>()
            for (i in 0..<100) {
                testData.add(Pair("$i", i))
            }
            val testArray = testData.toTypedArray()
            testArray.shuffle()
            val testHeap = PriorityQueue<String, Int>()
            for ((t, p) in testArray){
                testHeap[t] = p
                assert(testHeap.isValid())
            }
            assert(testHeap.isValid())
            for (i in 0..<100) {
                val data = testHeap.pop()
                assert(data == "$i")
                assert(testHeap.isValid())
                assert(testHeap.size == 99 - i)
                assert(testHeap.isValid())
            }
            testArray.shuffle()
            for ((t, p) in testArray){
                testHeap[t] = p
                assert(testHeap.isValid())
            }
            assert(testHeap.isValid())
            for (i in 0..<100) {
                val data = testHeap.pop()
                assert(data == "$i")
                assert(testHeap.isValid())
                assert(testHeap.size == 99 - i)
                assert(testHeap.isValid())
            }

        }
    }

    @Test
    fun testChangingDown() {
        for(x in 0..<10) {
            val testData = mutableListOf<Pair<String, Int>>()
            for (i in 0..<100) {
                testData.add(Pair("$i", i))
            }
            val testArray = testData.toTypedArray()
            testArray.shuffle()
            val testHeap2 = PriorityQueue(*testArray)
            testArray.shuffle()
            for((i, j) in testArray){
                testHeap2.update(i, -j)
                assert(testHeap2.isValid())
            }
            for (i in 99 downTo 0) {
                val data = testHeap2.pop()
                assert(data == "$i")
                assert(testHeap2.isValid())
            }
        }
    }

    @Test
    fun testChangingUp() {
        for(x in 0..<10) {
            val testData = mutableListOf<Pair<String, Int>>()
            for (i in 0..<100) {
                testData.add(Pair("$i", -i))
            }
            val testArray = testData.toTypedArray()
            testArray.shuffle()
            val testHeap2 = PriorityQueue(*testArray)
            testArray.shuffle()
            for((i, j) in testArray){
                testHeap2.update(i, -j)
                assert(testHeap2.isValid())
            }
            for (i in 0 ..<100) {
                val data = testHeap2.pop()
                assert(data == "$i")
                assert(testHeap2.isValid())
            }
        }
    }
}