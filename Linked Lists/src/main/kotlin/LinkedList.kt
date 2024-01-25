package edu.davis.cs.ecs036c

/**
 * Welcome to the first normal data structure homework assignment for ECS 036C
 *
 * In this you will be implementing many functions for the most
 * basic LinkedList class.  It is strongly advised that you start by
 * implementing toLinkedList (which accepts a vararg array), append
 * (which toLinkedList uses), and get first, as those 3 function are
 * required for all the test code.
 *
 * You will also need to write a lot of tests, as we provide only the
 * most basic tests and the autograder deliberately hides the test results
 * until after the grades are released.
 */

/**
 * This is the data class for a cell within the LinkedList.  You won't need to
 * add anything to this class.
 */
data class LinkedListCell<T>(var data:T, var next:LinkedListCell<T>?) {
}

/**
 * This is the basic LinkedList class, you will need to
 * implement several member functions in this class to
 * implement a LinkedList library.
 */
class LinkedList<T> {
    // You should have 3 variables:
    // A pointer to the first cell or null
    // A pointer to the last cell or null
    // An internal record to keep track of the number
    // of elements present.
    private var headCell: LinkedListCell<T>? = null
    private var tailCell: LinkedListCell<T>? = null
    private var privateSize = 0

    /**
     * This allows .size to be accessed but not set, with the private
     * variable to track the actual size elsewhere.
     */
    val size: Int
        get() = privateSize


    /**
     * You want to implement an iterator for the DATA in the LinkedList,
     * So you will need to implement a computeNext() function.
     *
     * Note: Kotlin will continue to treat at as nullable within the
     * computeNext function, because you could have some concurrent access
     * that could change the structure of the list.
     *
     * Thus in accessing the data one option you can use is an elvis operator ?:
     * to throw an IllegalStateException if there is an inconsistency
     * during iteration.
     *
     * We will not check whether your code would actually throw such an error,
     * because we aren't assuming the LinkedList class is thread safe, but by
     * doing that you can make the Kotlin type system happy.
     *
     * As an alternative you could just use the !!. operation that
     * will throw a NullPointerException instead.  Either are acceptable.
     */
    class LinkedListIterator<T>(var at: LinkedListCell<T>?):AbstractIterator<T>(){
        override fun computeNext():Unit {
            // check if the value in the linked list is null
            if(at == null){
                // finish if so
                done()
            }
            // otherwise set the next value to the data in the next linked list cell
            else{
                setNext(at!!.data)
                at = at?.next
            }
        }
    }

    /**
     * You will also want an iterator for the cells themselves in the LinkedList,
     * as there are multiple cases where you are going to want to iterate over
     * the cells not just the data in the cells.
     */
    class LinkedListCellIterator<T>(var at: LinkedListCell<T>?):AbstractIterator<LinkedListCell<T>>(){
        override fun computeNext():Unit {
            // check if the next value in the linked list is null
            if(at == null){
                // finish if so
                done()
            }
            // otherwise set the next value to the next linked list cell
            else{
                setNext(at!!)
                at = at?.next
            }
        }
    }

    /**
     * Append ads an item to the end of the list.  It should be
     * a constant-time (O(1)) function regardless of the number
     * of elements in the LinkedList
     */
    fun append(item:T)  {
        // make a linked list if there are no elements in the linked list
        if (privateSize == 0){
            // set the head to be the first node
            headCell = LinkedListCell<T>(item, null)

            // make the tail point to it
            // (this seems as if it's making the tail before the head but this is
            // only for when the linked list only has 1 item as the tail always
            // points to the end)
            tailCell = LinkedListCell<T>(item, headCell)

            // add 1 to the linked list count
            privateSize += 1
        }

        // otherwise add a new cell to the end of the linked list
        else{
            // create a new cell in the linked list and add data to it
            val tempCell = LinkedListCell<T>(item, null)

            // change the pointer of the last cell to the new cell
            tailCell?.next?.next = tempCell
            // change the pointer pointing to the last cell to the new cell
            tailCell?.next = tempCell

            // add 1 to the linked list count
            privateSize += 1
        }

    }

    /**
     * Adds an item to the START of the list.  It should be
     * a constant-time (O(1)) function.
     */
    fun prepend(item: T)  {
        // make a linked list if there are no elements in the linked list
        if (privateSize == 0){
            // set the head to be the first node
            headCell = LinkedListCell<T>(item, null)

            // make the tail point to it
            // (this seems as if it's making the tail before the head but this is
            // only for when the linked list only has 1 item as the tail always
            // points to the end)
            tailCell = LinkedListCell<T>(item, headCell)

            // add 1 to the linked list count
            privateSize += 1
        }

        // otherwise add a new cell to the start of the linked list
        else{
            // create a copy of the head cell
            val tempCell = headCell

            // change the head cell to the new cell and point it to the copy
            headCell = LinkedListCell<T>(item, tempCell)

            // add 1 to the linked list count
            privateSize += 1
        }

    }

    /**
     * Get the data at the specified index.  For a linked-list
     * this is an O(N) operation in general, but it should be O(1)
     * for both the first and last element.
     *
     * Invalid indices should throw an IndexOutOfBoundsException
     */
    operator fun get(index: Int) : T{
        // check if index is too small or too big and give error if so
        if (index < 0 || index > (privateSize - 1)) {
            throw IndexOutOfBoundsException("Negative Index")
        }

        // get the last element
        if (index == (privateSize - 1)){
            return(tailCell?.next!!.data)
        }

        // otherwise start iterating through the linked list to get the index
        else{
            // create a temporary cell that's equal to the head cell
            var tempCell = headCell
            // set the temporary cell to the next cell an index number of times
            repeat(index){
                tempCell = tempCell?.next
            }

            return(tempCell!!.data)
        }

    }

    /**
     * Replace the data at the specified index.  Again, this is an
     * O(N) operation, except if it is the first or last element
     * in which case it should be O(1)
     *
     * Invalid indexes should throw an IndexOutOfBoundsException
     */
    operator fun set(index: Int, data: T) : Unit {
        // check if index is too small or too big and give error if so
        if (index < 0 || index > (privateSize - 1)) {
            throw IndexOutOfBoundsException("Negative Index")
        }

        // if index is the first element
        else if (index == 0){
            // change the data in head cell
            headCell?.data = data
        }

        // if index is the last element
        else if (index == (privateSize - 1)){
            // replace it
            tailCell?.next?.data = data
        }

        // otherwise start iterating through the linked list to get the index
        else{
            // create a temporary cell that's equal to the head cell
            var tempCell = headCell
            // set the temporary cell to the next cell an (index - 1) number of times
            repeat(index-1){
                tempCell = tempCell?.next
            }
            // replace the next cell
            tempCell?.next?.data = data
        }

    }

    /**
     * This inserts the element at the index.
     *
     * If the index isn't valid, throw an IndexOutOfBounds exception
     *
     * This should be O(1) for the start and the end, O(n) for all other cases.
     */
    fun insertAt(index: Int, value: T) {
        // check if index is too small or too big and give error if so
        if (index < 0 || index > privateSize) {
            throw IndexOutOfBoundsException("Negative Index")
        }

        // if index is the first element
        if (index == 0){
            // create a new cell in the linked list equal to the head cell
            val tempCell = headCell
            // make the head cell the new cell
            headCell = LinkedListCell<T>(value, tempCell)

            // add 1 to the linked list count
            privateSize += 1
        }

        // if index is the last element
        else if (index == privateSize){
            // create a new cell in the linked list and add data to it
            // and make it point to nothing
            val insertCell = LinkedListCell<T>(value, null)
            // make the old last cell point to the new cell
            tailCell?.next?.next = insertCell
            // insert in back
            tailCell?.next = insertCell

            // add 1 to the linked list count
            privateSize += 1
        }

        // if index is the second element
        else if (index == 1){
            // create a new cell in the linked list equal to the position of the head cell
            val tempCell = LinkedListCell<T>(value, headCell?.next)
            // make the head cell point to the new cell
            headCell?.next = tempCell

            // add 1 to the linked list count
            privateSize += 1
        }

        // otherwise start iterating through the linked list to get to the index
        else{
            // create a temporary cell to move along in the list
            var tempCell = headCell
            // set the temporary cell to the next cell an (index - 1) number of times
            repeat(index-1){
                tempCell = tempCell?.next
            }

            // create a cell to be inserted pointing to the cell after the temporary cell
            val insertCell = LinkedListCell<T>(value, tempCell?.next)
            // make the previous cell point to the inserted cell
            tempCell?.next = insertCell

            // add 1 to the linked list count
            privateSize += 1

        }
    }

    /**
     * This removes the element at the index and return the data that was there.
     *
     * Again, if the data doesn't exist it should throw an
     * IndexOutOfBoundsException.
     *
     * This is O(N), and there is no shortcut possible for the last element
     */
    fun removeAt(index: Int) : T {
        // check if index is too small or too big and give error if so
        if (index < 0 || index >= privateSize) {
            throw IndexOutOfBoundsException("Negative Index")
        }

        // check if index is 0
        if (index == 0){
            // make a temporary value
            val temp = headCell!!.data

            // check if index 0 is the tail cell
            if (headCell == tailCell?.next){
                // remove the tail cell
                tailCell = null
                headCell = null
            }
            // make the head move forward one
            headCell = headCell?.next

            // remove 1 from the linked list count
            privateSize -= 1

            return(temp)
        }

        // check if index is 1
        else if (index == 1){
            // make a temporary value
            val temp = headCell?.next!!.data

            // check if index 1 is the tail cell
            if (headCell?.next == tailCell?.next){
                // move the tail cell back one
                tailCell?.next = headCell
            }

            // make the head jump forward one
            headCell?.next = headCell?.next?.next

            // remove 1 from the linked list count
            privateSize -= 1

            return(temp)
        }

        else{
            // create a temporary cell to keep track
            var tempCell = headCell
            // set the temporary cell to the next cell an (index - 2) number of times
            repeat(index-2){
                tempCell = tempCell?.next
            }

            // check if the last element is being removed
            if (tempCell?.next?.next == tailCell?.next){
                // set the tail to the cell before the last
                tailCell?.next = tempCell?.next
            }

            // create another temporary cell to store the data of the removed cell
            val storeCell = tempCell?.next?.next

            // make it so that the removed cell is skipped over
            tempCell?.next?.next = tempCell?.next?.next?.next

            // remove 1 from the linked list count
            privateSize -= 1

            // return the stored data
            return(storeCell!!.data)

        }

    }

    /**
     * This does a linear search for the item to see
     * what index it is at, or -1 if it isn't in the list
     */
    fun indexOf(item:T) :Int {
        // make a count
        var count = 0
        // use the iterator to check if the item is in the list
        for (data in iterator()){
            // check if the item is equal to the current cell
            if(data == item){
                return (count)
            }
            count += 1
        }

        // return -1 if item not found
        return (-1)
    }

    /**
     * Because we have indexOf already defined, we can do
     * contains as a one-liner, so we can do (x in list) and
     * have that convention work.
     */
    operator fun contains(item:T) = (indexOf(item) != -1 )


    /**
     * This needs to return an Iterator for the data in the cells.  This allows
     * the "for (x in aLinkedList) {...} to work as expected.
     *
     * You want your iterator to be one of the first things you ensure
     * works because you are going to want to do things like
     * for (x in this) {...} in your own internal code
     */
    operator fun iterator() = LinkedListIterator(headCell)

    /**
     * An internal helper function that returns an iterator for the
     * cells themselves.  This is very useful for both mapInPlace and
     * other functions you may need to implement.
     */
    fun cellIterator() = LinkedListCellIterator(headCell)

    /**
     * A very useful function for debugging, as it will print out
     * the list in a convenient form.  Actually showing you the code
     * as is rather than having you implement it, because it gives you
     * an idea of how powerful things are now that you have an iterator
     * and can convert that iterator to a sequence (which supports fold).
     */
    override fun toString(): String {
        return iterator()
            .asSequence()
            .fold("[") {initial , item ->
                if (initial != "[") initial + ", " + item.toString()
                else "[" + item.toString()} + "]"
    }

    /**
     * Of course, however, you have to implement your own version of fold
     * directly...  If the list is empty, fold returns the initial value.
     *
     * Otherwise, it accumulates a new value by applying the function
     * for each element.  See the toString() function for an example of
     * how to use fold
     */
    fun <R>fold(initial: R, operation: (R, T) -> R): R {
        // make a temporary value
        var temp = initial

        // return the initial value if the list is empty
        if (privateSize == 0){
            return(initial)
        }

        // otherwise go through every element in the list
        else{
            for (data in iterator()){
                // apply the operation to every element with the acculumator
                temp = operation(temp, data)
            }
        }

        return(temp)
    }

    /**
     * And you need to implement map, creating a NEW LinkedList
     * and applying the function to each element in the old list.
     *
     * One useful note, because append is constant time, you
     * can just go in order and make a new list.
     */
    fun <R>map(operator: (T)->R): LinkedList<R>{
        // make a new linked list
        val newLinkedList = LinkedList<R>()

        // go through every element in the linked list
        for (data in iterator()){
            // append an item to each with the operator
            newLinkedList.append(operator(data))
        }

        return(newLinkedList)
    }

    /**
     * You also need to implement mapInPlace.  mapInPlace is like Map with a difference:
     * instead of creating a new list it applies the function to each data
     * element and uses that to replace the elements in the existing list, returning
     * the list itself when done.
     */
    fun mapInPlace(operator: (T)->T) : LinkedList<T>{
        // go through every cell in the linked list
        for (cell in cellIterator()){
            // apply the operation to the data in the cell
            cell.data = operator(cell.data)
        }

        return (this)
    }

    /**
     * Filter creates a new list by only adding the elements in the original list
     * that are true when the operator is applied to the element.
     */
    fun filter(operator: (T)->Boolean) : LinkedList<T>{
        // make a new linked list
        val newLinkedList = LinkedList<T>()

        // go through every element in the linked list
        for (data in iterator()){
            // check if the expression evaluates to true
            if(operator(data)){
                // add the data to the new linked list
                newLinkedList.append(data)
            }
        }

        return(newLinkedList)
    }

    /**
     * And filterInPlace.  filterInPlace will keep only the elements
     * that are true.
     */
    fun filterInPlace(operator: (T)->Boolean) : LinkedList<T>{
        // make a new linked list and some temporary cells and a counter
        var tempCell1 : LinkedListCell<T>? = null
        var tempCell2 : LinkedListCell<T>? = null

        // make a value keeper
        var trueFalse = 0

        // go through every cell in the linked list
        for (cell in cellIterator()){
            // check if the expression evaluates to true
            if(operator(cell.data)){

                // check if this is the first time
                if (trueFalse == 0){
                    // make the value keeper equal to 1
                    trueFalse = 1

                    // set the head to this cell
                    headCell = cell

                    // set the tail to this cell
                    tailCell?.next = cell

                    // set a temporary cell equal to the current cell
                    tempCell1 = cell
                }
                // check if the value keeper is 1
                else if (trueFalse == 1){
                    // make the value keeper equal to 2
                    trueFalse = 2
                    // make this come after the head cell
                    headCell?.next = cell

                    // set the tail to this cell
                    tailCell?.next = cell

                    // set a new temporary cell to the current cell
                    tempCell2 = cell
                }
                // check if the value keeper is 2
                else if (trueFalse == 2){
                    // make the value keeper equal to 3
                    trueFalse = 3

                    // make the last temporary cell point to a new temporary cell
                    tempCell1?.next?.next = cell

                    // set the tail to this cell
                    tailCell?.next = cell

                    // set a new temporary cell to the current cell
                    tempCell1 = cell
                }
                // check if the value keeper is 3
                else if (trueFalse == 3){
                    // make the value keeper equal to 2
                    trueFalse = 2

                    // make the last temporary cell point to a new temporary cell
                    tempCell2?.next?.next = cell

                    // set a new temporary cell to the current cell
                    tempCell2 = cell

                    // set the tail to this cell
                    tailCell?.next = cell
                }
            }

            // otherwise if the expression does not evaluate to true
            else{
                // remove 1 from the linked list count
                privateSize -= 1
            }
        }

        // check if there's extra at the end
        var count = 0
        for (cell in cellIterator()){
            count += 1
        }

        // check if everything has been removed
        if (privateSize == 0){
            // reset
            headCell = null
            tailCell = null
        }

        // cut off everything extra at the end if there's stuff there
        else if (privateSize < count){
            count = 0
            for (cell in cellIterator()){
                count += 1
                if (count == privateSize){
                    cell.next = null
                }
            }
        }

        return(this)
    }
}

/**
 * And this function builds a new LinkedList of the given type with
 * a vararg (variable argument) set of inputs.  You should
 * implement this first as all other tests will depend on this.
 */
fun <T> toLinkedList(vararg input:T) : LinkedList<T> {
    val retval = LinkedList<T>()
    for (item in input){
        retval.append(item)
    }
    return retval
}