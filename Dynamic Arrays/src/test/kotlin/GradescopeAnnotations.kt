package edu.ucdavis.cs.ecs036c.testing

import org.junit.jupiter.api.TestReporter
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.io.File


/**
 * This Kotlin file contains a set of functions used to enable
 * test functions to work well in the autograding infrastructure
 * on gradescope.
 *
 * You do not need to modify this file, indeed you do not actually
 * even need to read this file, but think of it as a useful reference
 * in how you build more advanced testing infrastructure.
 */


/**
 * This list is used to generate the JSON
 * output for autograding.
 */
private var resultList: MutableList<GradescopeTestJson> = mutableListOf()

/**
 * This is a parent class for the testing infrastructure that will collect up the
 * test results and dump them to a JSON file
 */
open class GradescopeTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun setup() {
            println("Setup Run $resultList")
        }

        @AfterAll
        @JvmStatic
        fun teardown(){
            var max = 0
            for (item in resultList){
                max += item.max_score
            }
            val resultListEncoded = Json.encodeToString(resultList)
            println("Teardown Run $resultListEncoded")
            println("Maximum score $max")
            File("./build/reports/gradescope").mkdir()
            File("./build/reports/gradescope/testresults.json").writeText(resultListEncoded)
        }
    }

}

enum class Visibility {
    hidden, after_due_date, after_published, visible
}

@Target(AnnotationTarget.FUNCTION)
annotation class GradescopeAnnotation  (val name: String, val maxScore: Int,
    val visible : Visibility = Visibility.visible, val score: Int = 0) {
}

@Serializable
data class GradescopeTestJson (val name:String, val score: Int, val max_score: Int,
    val visibility : Visibility, val output:String) {
}

/**
 * This class is used to override testSuccessful/testFailed
 * so we can snag the results and the gradescope annotation
 * and toss it in the test list.
 */
class GradescopeTestWatcher : TestWatcher {
    override fun testSuccessful(context: ExtensionContext?) {
        val annotations = context!!.requiredTestMethod.annotations
        for (a in annotations){
            if (a is GradescopeAnnotation){
                val retval = GradescopeTestJson(a.name,
                    a.maxScore, a.maxScore, a.visible, "")
                resultList.add(retval)
            }
        }
        super.testSuccessful(context)
    }

    override fun testFailed(context: ExtensionContext?, cause: Throwable?) {
        val tags = context?.tags
        val method = context!!.requiredTestMethod
        val annotations = method.annotations
        println("Failed Context: $tags $method $annotations")
        for (a in annotations){
            if (a is GradescopeAnnotation) {
                val retval = GradescopeTestJson(
                    a.name,
                    0, a.maxScore, a.visible, "Exception registered: $cause"
                )
                resultList.add(retval)
            }
        }
        super.testFailed(context, cause)
    }
}