package edu.davis.cs.ecs36c.homework0

import java.io.File
import java.io.FileNotFoundException
import java.util.*
import kotlin.system.exitProcess


val defaultFile = "/usr/share/dict/words"

// A regular expression pattern that will match words:

// A useful trick: Given the regular expression object re,
// re.findAll returns a Collection (an iteratable structure)
// that contains the words, while re.split() returns an array
// of the strings that AREN'T matched.  So you can take an input
// line, create both the split and the collection, and iterate over
// the collection keeping track of the iteration count.
val splitString = "\\p{Alpha}+"

/**
 * This function should take a filename (either the default file or the
 * one specified on the command line.)  It should create a new MutableSet,
 * open the file, and load each line into the set.
 *
 * @param filename may not exist, and in that case the function should
 * throw a FileNotFound exception.
 */
fun loadFile(filename: String): Set<String>{
    val set = mutableSetOf<String>()
    // check if the file exists
    if (File(filename).exists()){
        // read through each dictionary line and add it to the set
        File(filename).forEachLine{set.add(it)}
        return set
    }
    // otherwise give an exception
    else{
        throw FileNotFoundException()
    }
}

/**
 * This function should check if a word is valid by checking the word,
 * the word in all lower case, and the word with all but the first character
 * converted in lower case with the first character unchanged.
 */
fun checkWord(word: String, dict: Set<String>) : Boolean{
    // check the word
    if (word in dict){
        return true
    }
    // check the word in lowercase
    if (word.lowercase() in dict){
        return true
    }
    // convert the word to all lowercase except the first letter and check it
    val tempword = word.replaceRange(1, word.length, word.drop(1).lowercase())
    if (tempword in dict) {
        return true
    }

    return false
}

/**
 * This function should take a set (returned from loadFile) and then
 * processes standard input one line at a time using readLine() until standard
 * input is closed
 *
 * Note: readLine() returns a String?: that is, a string or null, with null
 * when standard input is closed.  Under Unix or Windows in IntelliJ you can
 * close standard input in the console with Control-D, while on the mac it is
 * Command-D
 *
 * Once you have the line you should split it with a regular expression
 * into words and nonwords,
 */
fun processInput(dict: Set<String>){
    val re = Regex(splitString)
    // loop as long as standard input stream is open
    while (true){
        // read each line
        var line: String? = readLine()
        // exit if standard input is closed
        if (line == null){
            break
        }
        // form a sequence of words and a list of words and non-words
        var matches = re.findAll(line)
        var splits = re.split(line)

        // make a counter and go through every word and non-word
        var count = 0
        for (i in matches){
            // print out non-words
            print(splits[count])
            // check every word to see if it's legit
            if (!checkWord(i.value, dict)){
                // if not legit, add on " [sic]"
                print(i.value + " [sic]")
            }
            // if legit just add the original word
            else {
                print(i.value)
            }
            count += 1

            // println if at the end
            if(count == (splits.size - 1)){
                println(splits[count])
                break
            }

        }

        // if there's only numbers
        if (count == 0){
            println(splits[count])
        }

    }
}

/**
 * Your main function should accept an argument on the command line or
 * use the default filename if no argument is specified.  If the dictionary
 * fails to load with a FileNotFoundException it should use
 * kotlin.system.exitProcess with status code of 55
 */
fun main(args :Array<String>) {
    // Initialize a string
    val file : String
    // if there is no argument, assign the string to default
    if (args.isEmpty()){
        file = defaultFile
    }
    // otherwise assign it to the argument
    else{
        file = args[0]
    }
    // load the file with the loadFile function and try and catch
    try {
        val dictset = loadFile(file)
    }
    catch (exception: FileNotFoundException){
        // exit if file is not found
        kotlin.system.exitProcess(55)
    }
    val dictset = loadFile(file)

    // process the input with the processInput function
    processInput(dictset)

}