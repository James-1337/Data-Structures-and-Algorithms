package edu.ucdavis.cs.ecs036c

import kotlin.math.absoluteValue

class HashTable<K, V>(var initialCapacity: Int = 8) {
    data class HashTableEntry<K, V>(val key: K, var value: V, var deleted : Boolean = false);
    // The number of elements in the storage that exist, whether or not they are marked deleted
    internal var occupied = 0

    // The number of non-deleted elements.
    internal var privateSize = 0

    // And the internal storage array
    internal var storage: Array<HashTableEntry<K, V>?> = arrayOfNulls(initialCapacity)

    val size: Int
        get() = privateSize

    // An iterator of key/value pairs, done by using a sequence and calling yield
    // on each pair that is in the table and VALID
    operator fun iterator() : Iterator<Pair<K, V>> =
        sequence<Pair<K, V>> {
            // go through every valid entry
            for (entry in storage){
                if (entry != null && !entry.deleted){
                    // yield the key/value pair
                    yield(Pair(entry.key, entry.value))
                }
            }
    }.iterator()

    override fun toString() : String = this.iterator().asSequence().joinToString(prefix="{", postfix="}",
        limit = 200) { "[${it.first}/${it.second}]" }


    // Internal resize function.  It should copy all the
    // valid entries but ignore the deleted entries.
    private fun resize(){
        // make a copy of the storage
        val oldStorage = storage
        // set the storage to a complete new version with double the size
        occupied = 0
        privateSize = 0
        storage = arrayOfNulls(oldStorage.size*2)

        // copy entries into the storage
        for (entry in oldStorage){
            if (entry != null && !entry.deleted){
                // yield the key/value pair
                this[entry.key]= entry.value
            }
        }
    }

    // check if the key is in the hash table
    operator fun contains(key: K): Boolean {
        // make the hashes
        val hash = key.hashCode().absoluteValue
        var index = hash % storage.size

        // iterate through
        while (storage[index] != null){
            // check if the key is in the table and return true if so
            if ((storage[index]!!.key == key) && (!storage[index]?.deleted!!)){
                return true
            }
            // increment the index
            index = (index + 1) % storage.size
        }

        // return false otherwise
        return false
    }

    // Get returns null if the key doesn't exist
    operator fun get(key: K): V? {
        // make the hashes
        val hash = key.hashCode().absoluteValue
        var index = hash % storage.size

        // get the value of the key by iterating
        while (storage[index] != null){
            // check if the key is what we're looking for
            if ((storage[index]?.key == key) && (!storage[index]?.deleted!!)){
                return storage[index]!!.value
            }
            // increment the index
            index = (index + 1) % storage.size
        }

        // return null otherwise
        return null
    }

    // IF the key exists just update the corresponding data.
    // If the key doesn't exist, find a spot to insert it.
    // If you need to insert into a NEW entry, resize if
    // the occupancy (active & deleted entries) is >75%
    operator fun set(key: K, value: V) {
        // make an entry and the hashes
        val entry = HashTableEntry(key, value)
        val hash = key.hashCode().absoluteValue
        var index = hash % storage.size

        // while the key is not null
        while (storage[index] != null){
            // check if the key is the same and it's not deleted
            if ((storage[index]?.key == key) && (!storage[index]?.deleted!!) && (contains(key))){
                // update the value
                storage[index]!!.value = value
                return
            }

            // check if the entry is deleted
            else if (storage[index]?.deleted!! && !contains(key)){
                // replace the key/value and add 1 to size
                storage[index] = HashTableEntry(key, value)
                privateSize++
                return
            }

            // increase the index
            index = (index + 1) % storage.size
        }

        // check if size is appropriate
        if (occupied > storage.size * 0.75){
            resize()
            set(key, value)
            return
        }

        // change the key/value pair from null to a hash and add 1 to size
        storage[index] = HashTableEntry(key, value)
        privateSize++
        occupied++

        // return
        return

    }

    // If the key doesn't exist remove does nothing
    fun remove(key: K) {
        // check if the key exists
        if (contains(key)) {
            // make the hash
            val hash = key.hashCode().absoluteValue
            var index = hash % storage.size

            // probe until the key is found
            while (storage[index]?.key != key){
                index = (index + 1) % storage.size
            }

            // make the entry deleted
            storage[index]!!.deleted = true

            // take 1 away from size
            privateSize--
        }
    }

}
