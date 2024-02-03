package edu.ucdavis.cs.ecs036c

import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import java.security.SecureRandom
import kotlin.random.asKotlinRandom
import kotlin.test.assertFailsWith
import edu.ucdavis.cs.ecs036c.testing.*
import java.util.concurrent.TimeUnit
import kotlin.reflect.KProperty
import kotlin.time.measureTime
import kotlin.reflect.jvm.javaField

/**
 * Randomness is often really useful for testing,
 * and this makes sure we have a guaranteed GOOD
 * random number generator
 */
val secureRNG = SecureRandom().asKotlinRandom()


class DynamicArrayTest  () {
    /**
     * You will need to write MANY more tests, but this is just a simple
     * example: it creates a DynamicArray<String> of 3 entries,
     * and then calls the toString() function.  Since toString needs
     * iterator to work this actually tests a remarkable amount of your code!
     */
    @Test
    fun testInit(){
        val testArray = arrayOf("A", "B", "C")
        // The * operator here expands an array into the arguments
        // for a variable-argument function
        val data = toDynamicArray(*testArray)
        assert(data.toString() == "[A, B, C]")
    }

    @Test
    fun testInitResize(){
        val testArray = arrayOf("A", "B", "C", "D", "E", "F")
        // The * operator here expands an array into the arguments
        // for a variable-argument function
        val data = toDynamicArray(*testArray)
        assert(data.toString() == "[A, B, C, D, E, F]")
    }


    /**
     * Similarly, we give you this test as well.
     */
    @Test
    fun testBasicInit(){
        val testArray = arrayOf(0,1,2,3,4,5)
        testArray.shuffle(random = secureRNG)
        val data = toDynamicArray(*testArray)
        // Useful kotlin shortcut:  Kotlin's native Arrays
        // (and also our DynamicArray) has a dynamic
        // field
        for(x in data.indices){
            assert(testArray[x] == data[x])
        }
        assert(testArray.size == data.size)
        assertFailsWith<IndexOutOfBoundsException>() {
            data[-1]
        }
        assertFailsWith<IndexOutOfBoundsException>() {
            data[data.size]
        }
    }

    @Test
    fun testBasicNullableData(){
        val data = DynamicArray<Int?>()
        for(x in 0..<5){
            data.append(x)
        }
        for(x in 5..<10){
            data.append(null)
        }
        for(x in 0..<10){
            if(x < 5){
                assert(data[x] == x)
            } else {
                assert(data[x] == null)
            }
        }
    }

    /*
     * An example of a timing test, ensuring that append() is constant time.
     * You will want other timing tests as well.
     */
    @Test
    @Timeout(5, unit = TimeUnit.SECONDS)
    fun testTiming(){
        fun internal(iterationCount : Int){
            val data: DynamicArray<Int> = toDynamicArray()
            for(x in 0..<iterationCount){
                assert(data.size == x)
                data.append(x)
                assert(data[x] == x)
            }
        }
        val small = measureTime{internal(1000)}
        val large = measureTime{internal(100000)}
        // We have it be 200 rather than 100 to account for measurement errors
        // in timing, as this is something that can routinely occur.
        assert(large < (small * 200))
    }


    /*
     * This is how we check to make sure you don't add any more
     * fields to the class and that the types are right.
     *
     * We are making this test public so you can both see how it
     * works (it is a fair bit of interesting meta-programming,
     * that is, programming to manipulate programming)
     * and to make sure that you don't add any more fields
     * to your class.
     */
    @Test
    @GradescopeAnnotation("Test Introspection", maxScore = 0)
    fun testIntrospection(){
        val fields = DynamicArray::class.members
        val allowedFields = mapOf("privateSize" to "int",
            "size" to null,
            "start" to "int",
            "storage" to "java.lang.Object[]",
            "indices" to null
        )
        for (item in fields){
            if (item is KProperty){
                if (item.name !in allowedFields){
                    println("Unknown additional varibale: "+ item.name)
                    assert(false)
                }
                val javaType = item.javaField?.type?.getTypeName()
                if(javaType != allowedFields[item.name]){
                    println("Declared " + item.name + " as incorrect type " +
                    javaType)
                    assert(false)
                }
            }
        }
    }

    // test that the prepend function and get functions work
    @Test
    @Timeout(5, unit = TimeUnit.SECONDS)
    fun testPrepend(){
        // dynamic array of values from 0 to 5
        val testArray = arrayOf(0,1,2,3,4,5)
        val testList = toDynamicArray(*testArray)

        // use prepend to add 6 to the front
        testList.prepend(6)

        // use get() to see if the first index is 6
        assert(testList[0] == 6)

        // check index out of bound
        assertFailsWith<IndexOutOfBoundsException>() {
            testList[9]
        }
        assertFailsWith<IndexOutOfBoundsException>() {
            testList[-1]
        }

        // prepend with nothing in the list
        val nothing = DynamicArray<Int>()
        nothing.prepend(1)
    }

    // test that the set function works
    @Test
    fun testSet(){
        // dynamic array of values from 0 to 5
        val testArray = arrayOf(0,1,2,3,4,5)
        val testList = toDynamicArray(*testArray)

        // use set() to set the last index to 6 and the fourth to 5
        testList.set(5, 6)
        testList.set(4, 5)

        // check
        assert(testList.get(5) == 6)
        assert(testList.get(4) == 5)

        // check index out of bound
        assertFailsWith<IndexOutOfBoundsException>() {
            testList.set(9, 9)
        }
        assertFailsWith<IndexOutOfBoundsException>() {
            testList.set(-1, 9)
        }

    }

    // test that the insertAt function works
    @Test
    @Timeout(5, unit = TimeUnit.SECONDS)
    fun testInsertAt(){
        // dynamic array of values from 0 to 5
        val testArray = arrayOf(0,1,2,3,4,5)
        val testList = toDynamicArray(*testArray)

        // use insertAt() to set the last index to 6 and the fourth to 5
        // and the first to 9 and the second to 15
        testList.insertAt(6, 6)
        testList.insertAt(3, 5)
        testList.insertAt(0, 9)
        testList.insertAt(1, 15)

        // check
        assert(testList.get(9) == 6)
        assert(testList.get(6) == 3)
        assert(testList.get(0) == 9)
        assert(testList.get(1) == 15)

        // check index out of bound
        assertFailsWith<IndexOutOfBoundsException>() {
            testList.insertAt(20, 9)
        }
        assertFailsWith<IndexOutOfBoundsException>() {
            testList.insertAt(-1, 9)
        }

    }

    // test that the removeAt function works
    @Test
    fun testRemoveAt(){
        // dynamic array of values from 0 to 5
        val testArray = arrayOf(0,1,2,3,4,5)
        val testList = toDynamicArray(*testArray)

        // dynamic array of 2 values
        val singleArray = arrayOf(0, 1)
        val testSingle = toDynamicArray(*singleArray)

        // use removeAt() to remove the last, fourth, second, and first index
        val last = testList.removeAt(5)
        val fourth = testList.removeAt(3)
        val second = testList.removeAt(1)
        val first = testList.removeAt(0)

        // use removeAt() to remove the whole list
        testSingle.removeAt(1)
        testSingle.removeAt(0)

        // check
        assert(testList.get(0) == 2)
        assert(testList.get(1) == 4)
        assert(last == 5)
        assert(second == 1)
        assert(first == 0)
        assert(fourth == 3)

        // check index out of bound
        assertFailsWith<IndexOutOfBoundsException>() {
            testList.removeAt(20)
        }
        assertFailsWith<IndexOutOfBoundsException>() {
            testList.removeAt(-1)
        }
        assertFailsWith<IndexOutOfBoundsException>() {
            testSingle.removeAt(0)
        }

        // test with random order of numbers from 0-10
        val test = arrayOf(0,1,2,3,4,5,6,7,8,9,10)
        test.shuffle(random = secureRNG)
        val list = toDynamicArray(*test)

        val temp = list.get(0)
        assert(temp == list.removeAt(0))
        assert(list.size == 10)

    }

    // test that the indexOf and contain functions work
    @Test
    fun testIndexOf(){
        // dynamic array of values from 0 to 5
        val testArray = arrayOf(0,1,2,3,4,5)
        val testList = toDynamicArray(*testArray)

        // check the index of the 5 and 6 and if it contains 2 and 6
        assert(testList.indexOf(5) == 5)
        assert(testList.indexOf(6) == -1)
        assert(testList.contains(2))
        assert(!testList.contains(6))

    }

    // test that the fold function works
    @Test
    fun testFold(){
        // dynamic array of values from 0 to 5
        val testArray = arrayOf(0,1,2,3,4,5)
        val testList = toDynamicArray(*testArray)

        // dynamic array of 1 value
        val singleArray = arrayOf(0)
        val testSingle = toDynamicArray(*singleArray)
        testSingle.removeAt(0)

        // check
        val single = (testSingle.fold(5){initial, addition -> initial + addition })
        val multiplication = (testList.fold(5){initial, times -> initial * times })

        assert(single == 5)
        assert(multiplication == 0)

        // check division by 0
        assertFailsWith<ArithmeticException>() {
            (testList.fold(5){initial, divide -> initial / divide })
        }

    }

    // check that the map and mapInPlace functions work
    @Test
    fun testMap(){
        // dynamic array of values from 0 to 5 and a copy multiplied by 5
        val testArray = arrayOf(0,1,2,3,4,5)
        val testList = toDynamicArray(*testArray)
        val copyList = testList.map { initial -> initial*5 }

        // map in place multiplied by 5
        testList.mapInPlace { initial -> initial*5 }

        // check
        assert(copyList.get(0) == 0)
        assert(copyList.get(3) == 15)

        assert(testList.get(0) == 0)
        assert(testList.get(3) == 15)

        // check division by 0
        assertFailsWith<ArithmeticException>() {
            (testList.map{initial -> initial / 0 })
        }

        assertFailsWith<ArithmeticException>() {
            (testList.mapInPlace{initial -> initial / 0 })
        }

    }

    // check that the filter and filterInPlace functions work
    @Test
    fun testFilter(){
        // dynamic array of values from 0 to 5 and a filter of >5
        val testArray = arrayOf(0,1,2,3,4,5)
        val testList = toDynamicArray(*testArray)
        val copyList = testList.filter { initial -> initial > 3 }
        val extremeList = testList.filter { initial -> initial > 999 }

        // check
        assert(copyList.get(0) == 4)
        assert(copyList.get(1) == 5)

        // filter in place >3
        testList.filterInPlace { initial -> initial > 3 }
        assert(testList.get(0) == 4)

        // filter in place > 999
        testList.filterInPlace { initial -> initial > 999 }

        // check that the lists are empty
        assertFailsWith<IndexOutOfBoundsException>() {
            extremeList.get(0)
        }
        assertFailsWith<IndexOutOfBoundsException>() {
            testList.get(0)
        }

        // test with random order of numbers from 0-20
        val test = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)
        test.shuffle(random = secureRNG)
        val list = toDynamicArray(*test)

        list.filterInPlace { initial -> (initial-1) > 9 }
        assert(list.size == 10)

    }

}
