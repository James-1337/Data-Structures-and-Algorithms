package edu.ucdavis.cs.ecs36c

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import java.io.File
import java.util.ArrayDeque


/*
 * The "Six Degrees of Kevin Bacon" problem is a classic game for
 * movie bufs.  The goal is to take an arbitrary actor and determine
 * the minimum sequence of movies based on "who starred with whom" to
 * reach the actor Kevin Bacon.
 *
 * As an example, Keanu Reeves was in 47 Ronin with Niel Fingleton, while
 * Neil Fingleton was in X-Men: First Class with Kevin Bacon.
 *
 * This class is designed to implement a solution to the Kevin Bacon problem.
 * The constructor itself takes a filename which is a .csv file of movie
 * information: the first column is the title and the second and subsequent columns
 * are the actors.
 *
 * Then there is the "Baconator" function.  This function performs a breadth-first
 * traversal to find a minimum BaconPath.  This is returned as a list of
 * Actor/Title/Actor/Title/Actor, or an empty list if the name isn't in the database
 * or there is no Bacon Path.  There may be multiple possible minimum Bacon Paths for
 * any particular actor but you only need to return one.
 */
class Baconator (val filename: String){

    /*
     * Internally we store a key/value pair allowing us to go
     * from the name to the appropriate data structure for
     * the actor or movie.
     */
    val actors = mutableMapOf<String, Actor>()
    val movies = mutableMapOf<String, Movie>()

    /*
     * This is effectively a graph traversal problem, so we
     * need a class for nodes (actors) and edges (movies), and
     * we do it as an adjacency list for both for convenience*/

    data class Actor(val name: String){
        val movies = mutableSetOf<Movie>()
    }

    data class Movie(val title: String){
        val actors = mutableSetOf<Actor>()
    }

    /*
     * A useful little data class for the traversal.  You don't need to
     * use this but you might want to...
     */
    data class BaconLink(val actor1: Actor, val actor2: Actor, val movie: Movie)

    /*
     * Our constructor will load the specified CSV file
     */
    init {
        loadCSV()
    }

    /*
     * The heart of the 6-degrees of Kevin Bacon algorithm.  It should start
     * at Kevin Bacon and do a breadth-first search traversal until it finds
     * the target actor, and returns the BaconPath going the other way.
     * Alternatively, you could start at the targeted actor and go forward until
     * you reach Kevin Bacon: Either option is valid.
     *
     * Internally you will need a queue to implement the breadth first traversal,
     * a set to know if you have previously visited an actor, and some sort of
     * structure (most likely a map) to record each Actor->Movie->Actor link you
     * discover is possibly valid during the breadth first traversal.
     *
     * The returned data is a List of strings of the form Actor/Movie/Actor/Movie,
     * with the last actor being Kevin Bacon.
     *
     * Kevin Bacon should return a List of just "Kevin Bacon", and
     * if the name doesn't exist in the movie database OR there is no valid
     * baconpath for the name it should return an empty list.
     */
    fun getBaconpath(name: String) : List<String> {
        // load the data
        loadCSV()

        // make a queue for actors to go through
        val visit = ArrayDeque<Actor>()
        // make a set of actors already gone through
        var seen = mutableSetOf<Actor>()
        // make a set of key/value pairs of paths
        var paths = mutableMapOf<Actor, BaconLink>()
        // add Kevin Bacon to the queue
        visit.add(actors["Kevin Bacon"]!!)

        // if Kevin Bacon itself is the search
        if (name == "Kevin Bacon"){
            // make a list with Kevin Bacon
            val list = listOf("Kevin Bacon")
            // return the list with only kevin bacon
            return list
        }

        // while the dequeue has something in it
        while (visit.isNotEmpty()){
            // set a variable to be the current iteration
            var current = visit.removeFirst()

            // check if the name is actually in the actors map
            if (!actors.contains(name)){
                // return nothing if the name doesn't exist
                return listOf()
            }

            // check if the actor is found
            if (current.name == name){
                // make a list to be returned starting with the current actor
                val list = mutableListOf(current.name)
                // trace from the actor back to Kevin Bacon with the bacon links
                var at = current

                while (at.name != "Kevin Bacon"){
                    // get the movie and correlated actor from the bacon link
                    val baconMovie = paths[at]!!.movie.title
                    val actor1 = paths[at]!!.actor1.name

                    // add the actor and movie to the list
                    list.add(baconMovie)
                    list.add(actor1)

                    // set at to the previous actor
                    at = paths[at]!!.actor1
                }

                // return the list
                return list
            }

            // iterate through all movies associated with the current actor
            for (movie in current.movies){
                // nested loop to iterate through all actors associated with those movies
                for (actor in movie.actors){
                    // if the actors is not seen yet
                    if (actor !in seen){
                        // add the actor to seen
                        seen.add(actor)

                        // enqueue the actor into the visit queue
                        visit.add(actor)

                        // record a Bacon Link in the paths set
                        paths[actor] = BaconLink(current, actor, movie)
                    }
                }
            }


        }

        // return nothing otherwise
        return listOf()
    }

    /*
     * The function to load the CSV.  We use the Apache Commons CSV library (which
     * is under a freely permissive license and automagically downloaded by
     * the dependency reference in the build.gradle.kts file.
     *
     * The map executes the lambda for each line in the file, with the first entry
     * in the CSV Record being the title and the subsequent entries as names of
     * the actors in the movie.
     *
     * You can assume that the CSV file exists, is well-formed, and there are
     * no duplicate movie titles.
     */
    fun loadCSV() {
        CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.build().parse(File(filename).bufferedReader())
            .map {
                // call the load line function to load every line
                loadLineData(it)
            }
    }

    // load line function to load data from lines in CSV format
    fun loadLineData(data: CSVRecord){
        // store key/value pairs of the movie in the movies map
        movies[data[0]] = Movie(data[0])

        // iterate through every actor
        for (i in 1 until data.size()){
            // check if the data isn't already in actors
            if (data[i] !in actors) {
                // create the actor as an object
                actors[data[i]] = Actor(data[i])
            }
            // add the movie to the actor
            actors[data[i]]!!.movies.add(movies[data[0]]!!)
            // add the actor to the movie
            movies[data[0]]!!.actors.add(actors[data[i]]!!)
        }
    }
}