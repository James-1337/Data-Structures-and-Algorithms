package edu.ucdavis.cs.ecs036c

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import kotlin.random.Random

class HashTableTest {

    @Test
    fun testLots() {
        for (x in 0..<100) {
            val h = HashTable<String, Int>()
            val tweak = "Tweak:" + Random.nextInt().toString()
            val iterations = 10000
            var strings : Array<String?> = arrayOfNulls(iterations)
            for (i in 0 ..< iterations){
                strings[i] = i.toString() + tweak
            }
            @Suppress("UNCHECKED_CAST")
            strings as Array<String>
            strings.shuffle()
            var i = 0
            for (s in strings) {
                assert(s !in h)
                h[s] = i
                assert(s in h)
                assert(h[s] == i)
                assert(h.size == i + 1)
                i++
            }
            strings.shuffle()
            i = 0
            for (s in strings) {
                assert(s in h)
                h.remove(s)
                assert(s !in h)
                assert(h[s] == null)
                if(i < iterations -1 ) {
                    h[strings[i + 1]] = i
                    assert(h[strings[i+1]] == i)
                }
                assert(h.size == iterations - 1 - i)
                assert(h.occupied == iterations)
                i++
            }
            val savedSize = h.storage.size
            strings.shuffle()
            i = 0
            for (s in strings) {
                assert(s !in h)
                h[s] = i
                assert(h.size == i + 1)
                assert(h[s] == i)
                assert(h.occupied == iterations)
                i++
            }
            // There shouldn't be any growth in the internal storage
            // as deleted items just get re-overwritten.
            assert(h.storage.size == savedSize)
        }
    }
}