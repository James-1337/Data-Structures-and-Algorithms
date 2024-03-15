package edu.ucdavis.cs.ecs036c.homework7

import kotlin.math.absoluteValue

/*
 * Class for a priority queue that supports the comparable trait
 * on elements.  It sets up to return the lowest value priority (a min heap),
 * if you want the opposite use a comparable object that is reversed.
 *
 * You could use this for implementing Dijkstra's in O(|V + E| log (V) ) time instead
 * of the default O(V^2) time.
 */
class PriorityQueue<T, P: Comparable<P>> {

    /*
     * Invariants that need to be maintained:
     *
     * priorityData must always be in heap order
     * locationData must map every data element to its
     * corresponding index in the priorityData, and
     * must not include any extraneous entries.
     *
     * You must NOT change these variable names and you MUST
     * maintain these invariants, as the autograder checks that
     * the internal structure is maintained.
     */
    val priorityData = mutableListOf<Pair<T, P>>()
    val locationData = mutableMapOf<T, Int>()

    /*
    * Size function is just the internal size of the priority queue...
    */
    val size : Int
        get() = priorityData.size



    /*
     * This is a secondary constructor that takes a series of
     * data/priority pairs.  It should put the pairs in the heap
     * and then call heapify/ensure the invariants are maintained
     */
    constructor (vararg init: Pair<T, P>) {
        // add pairs into priorityData using a for loop
        for (pair in init){
            priorityData.add(pair)
        }

        // call heapify to build a min heap
        this.heapify()
    }

    /*
     * Heapify should ensure that the constraints are all updated.  This
     * is called by the secondary constructor.
     */
    fun heapify(){
        // set a counter
        var i = size - 1
        // for each element in priorityData backwards
        for (pair in priorityData.reversed()){
            // set the index in locationData
            locationData[pair.first] = i
            // call sink
            sink(i)
            // minus 1 to i
            i--
        }
    }

    // swap function for swapping nodes
    fun swap(i: Int, j: Int){
        // temporarily save the element
        val temp = priorityData[i]

        // perform the swap in priorityData
        priorityData[i] = priorityData[j]
        priorityData[j] = temp

        // update locationData
        locationData[priorityData[i].first] = i
        locationData[priorityData[j].first] = j
    }

    /*
     * We support ranged-sink so that this could also be
     * used for heapsort, so sink without it just specifies
     * the range.
     */
    fun sink(i : Int) {
        sink(i, priorityData.size)
    }

    /*
     * The main sink function.  It accepts a range
     * argument, that by default is the full array, and
     * which considers that only indices < range are valid parts
     * of the heap.  This enables sink to be used for heapsort.
     */
    fun sink(i : Int, range: Int){
        // initialization
        val leftChild = 2*i + 1
        val rightChild = 2*i + 2

        // if the node does not have a left child, return
        if (leftChild >= range){
            return
        }

        // if the node only has a left child and no right child
        if (rightChild >= range){
            // check priority and swap if needed
            if (priorityData[leftChild].second < priorityData[i].second){
                // swap
                swap(i, leftChild)
            }
            // return
            return
        }

        // otherwise both children exist
        // if the left child's priority is less than the parent's and right child's, swap
        if (priorityData[leftChild].second < priorityData[i].second &&
            priorityData[leftChild].second < priorityData[rightChild].second){
            // swap
            swap(i, leftChild)
            // recursively sink the left child
            sink(leftChild, range)
            // return
            return
        }

        // if the right child's priority is less than the parent's and left child's, swap
        if (priorityData[rightChild].second < priorityData[i].second &&
            priorityData[rightChild].second < priorityData[leftChild].second){
            // swap
            swap(i, rightChild)
            // recursively sink the left child
            sink(rightChild, range)
            // return
            return
        }
    }

    /*
     * And the swim operation as well...
     */
    fun swim(i : Int) {
        // if the node is the root, return
        if (i == 0){
            return
        }

        // set the index
        var ind = i

        // while the index is more than 0
        while (ind > 0){
            // set the parent node
            val parent = (ind - 1)/2

            // if the node is less than the parent
            if (priorityData[parent].second > priorityData[ind].second){
                // swap positions between the node and its parent
                swap(parent, ind)
            }
            // otherwise return
            else {
                return
            }

            // update the index to its parent
            ind = (ind - 1)/2
        }
    }


    /*
     * This pops off the data with the lowest priority.  It MUST
     * throw an exception if there is no data left.
     */
    fun pop() : T {
        // throw an exception if there's no data
        if (size == 0){
            throw Exception()
        }

        // swap the root and the last element
        swap(0, size-1)

        // record the data and remove it
        val temp = priorityData.removeLast().first

        // remove the old root from locationData
        locationData.remove(temp)

        // recover the heap's properties
        this.heapify()

        // return
        return temp
    }

    /*
     * And this function enables updating the priority of something in
     * the queue.  It should sink or swim the element as appropriate to update
     * its new priority.
     *
     * If the key doesn't exist it should create a new one
     */
    fun update(data: T, newPriority: P ) {
        // if the data is not in locationData
        if (data !in locationData){
            // add a new pair to priorityData
            priorityData.add(Pair(data, newPriority))
            // add it to locationData too
            locationData[data] = size - 1
            // swim the new pair
            swim(size - 1)
        }

        // if the data is already in locationData, update its priority
        priorityData[locationData[data]!!] = Pair(data, newPriority)
        // swim and sink the data
        swim(locationData[data]!!)
        sink(locationData[data]!!)
    }

    /*
     * A convenient shortcut for update, allowing array assignment
     */
    operator fun set(data: T, newPriority: P) {
        update(data, newPriority)
    }

    /*
     * You don't need to implement this function but it is
     * strongly advised that you do so for testing purposes, to check
     * that all invariants are correct.
     */
    fun isValid() : Boolean {
        // check that the size is correct
        if (size != locationData.size){
            return false
        }
        // check that the data corresponds between priorityData and locationData
        for (i in 0..<size) {
            val (data, priority) = priorityData[i]
            if (locationData[data] != i){
                return false
            }
            val parentIndex = (i - 1) / 2
            if (i != 0) {
                if (priorityData[parentIndex].second > priority){
                    return false
                }
            }
        }
        return true
    }
}
