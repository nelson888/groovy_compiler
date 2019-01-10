package com.tambapps.compiler

import com.tambapps.compiler.exception.LexicalException
import com.tambapps.compiler.exception.ParsingException
import com.tambapps.compiler.exception.SemanticException


/**
 * compile and execute code in a given folder
 * The folder has many source code and 3 directories:
 * parsing/, lexical/, semantic/, where all tests in each folder should raise an exception
 * (ParsingException, LexicalException, SemanticException)
 */
class ExecutionTest {

    //CONFIGURER MSM_PATH et TEST_FILES_FOLDER_PATH AVANT DE LANCER
    private static final String MSM_PATH = "/home/nelson/MSM/msm"
    private static final String TEST_FILES_FOLDER_PATH = "/home/nelson/compilerGroovyTests"
    private static final File TEST_FILES_FOLDER = new File(TEST_FILES_FOLDER_PATH)
    private static final File FILE = new File(TEST_FILES_FOLDER, "compiled.code")
    private static final Compiler COMPILER = new Compiler()

    static String executedCode(String input) {
        String code = COMPILER.compile(input)
        FILE.text = code
        String[] commands = [MSM_PATH, FILE.getPath()]
        Runtime rt = Runtime.getRuntime()
        Process proc = rt.exec(commands)
        return proc.getInputStream().text.trim()
    }

    static void process(File testFolder, def exceptionHandler) { //return true if right test, false otherwise
        if (!FILE.exists() && !FILE.createNewFile()) {
            throw new RuntimeException("Couldn't create test file")
        }

        int count = 0
        int success = 0
        int error = 0
        for (File file : testFolder.listFiles()) {
            if (file == FILE || file.isDirectory()) continue
            count++
            String text = file.text.trim()
            String expected = ""
            String output
            if (exceptionHandler == null) { //if no exception must be thrown, the result expected is in the last line of the file
                expected = text.substring(text.lastIndexOf("\n")).trim()
                text = text.substring(0, text.lastIndexOf("\n")).trim()
            }

            try {
                output = executedCode(text)
            } catch (Exception e) {
                if (!exceptionHandler || !exceptionHandler(e)) {
                    error++
                    println("Test $file.name failed with a ${e.getClass().simpleName} exception:")
                    println(e.message)
                }
                continue
            }
            if (exceptionHandler == null && expected != output) {
                error++
                println("Test $file.name failed:")
                println("\tExpected $expected but got $output")
                println()
            } else if (exceptionHandler) {
                error++
                println("No exception were thrown for test $file.name but one was expected")
                println()
            } else {
                success++
            }
        }
        println()

        println("$count tests were executed")
        println("$success tests were successful")
        println("$error tests failed")
        println()
        println()
    }

    public static void main(String[] args) {
        println("Starting tests")
        process(TEST_FILES_FOLDER, null)

        println("Starting parsing exception tests")
        File parserTests = new File(TEST_FILES_FOLDER, "parsing")
        process(parserTests, { e -> return e instanceof ParsingException })

        if (true) return
        println("lexical parsing exception tests")
        File lexicalTests = new File(TEST_FILES_FOLDER, "lexical")
        process(lexicalTests, { e -> return e instanceof LexicalException })

        println("semantic parsing exception tests")
        File semanticTests = new File(TEST_FILES_FOLDER, "semantic")
        process(semanticTests, { e -> return e instanceof SemanticException })
    }

}
