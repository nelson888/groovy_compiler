package com.tambapps.compiler


/**
 * EXECUTE DU CODE DANS LE FICHIER ET S'ASSURE QUE L'OUTPUT (LES PRINT) SONT BON
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

    static void main(String[] args) {
        if (!FILE.exists() && !FILE.createNewFile()) {
            throw new RuntimeException("Couldn't create test file")
        }
        def files = TEST_FILES_FOLDER.listFiles()
        println("${files.length - 1} tests will be executed")

        int success = 0
        int error = 0
        for (File file : files) {
            if (file == FILE) continue
            String text = file.text.trim()
            String expected = text.substring(text.lastIndexOf("\n")).trim()
            text = text.substring(0, text.lastIndexOf("\n")).trim()
            String output
            try {
                output = executedCode(text)
            } catch (Exception e) {
                error++
                println("Test of file $file.name failed with a $e.class.name exception:")
                println(e.message)
                continue
            }
            if (expected != output) {
                error++
                println("Test of file $file.name failed:")
                println("\tExpected $expected but got $output")
                println()
            } else {
                success++
            }
        }
        println()

        println("$success tests were successful")
        println("$error tests failed")
    }
}
