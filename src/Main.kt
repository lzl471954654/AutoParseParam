import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.system.exitProcess

lateinit var scanner:Scanner
lateinit var srcFile:File
fun main() {
    scanner = Scanner(System.`in`)
    println("Mode select ：\t1.Input\t2.File")
    val choice = scanner.next()
    val choiceNumber = Integer.parseInt(choice,10)
    if (choiceNumber == 1){
        generateFromInput()
    }else if (choiceNumber == 2){
        generateFromFile()
    }else{
        println("choice is not exists")
    }
}

fun generateFromFile(){
    println("Enter source file path ：")
    val path = scanner.nextLine()
    srcFile = File(path)
    if (!srcFile.exists() || srcFile.isDirectory ){
        println("File not exists")
        exitProcess(1)
    }
    val lines = srcFile.readLines()
    val pairSet = ArrayList<Pair<String,String>>()
    for (line in lines) {
        val temp = line.split(' ')
        if (temp.size < 2){
            println("param : $line is not valid")
            exitProcess(2)
        }
        val pair = Pair(temp[0],temp[1])
        pairSet.add(pair)
    }
    dealPairSet(pairSet)
}

fun generateFromInput(){
    println("Please enter param type and param name, just like : String key , enter 0 to end")
    srcFile = File.createTempFile("out","txt")
    val pairs = ArrayList<Pair<String,String>>()
    val reader = BufferedReader(InputStreamReader(System.`in`))
    while (true){
        val line = reader.readLine()
        if (line.startsWith("0"))
            break
        val temp = line.split(' ')
        if (temp.size < 2){
            println("param : $line is not valid")
            exitProcess(2)
        }
        val pair = Pair(temp[0],temp[1])
        pairs.add(pair)
    }
    dealPairSet(pairs)
}

fun dealPairSet(pairSet: ArrayList<Pair<String, String>>) {
    val targetFile = File( srcFile.parentFile.absolutePath + File.separator + "out.txt" )
    if (targetFile.exists()){
        targetFile.delete()
    }
    if (targetFile.createNewFile()){
        val writer = targetFile.bufferedWriter()
        for (pair in pairSet) {
            writer.write("private ${pair.first} ${pair.second};")
            writer.newLine()
        }
        writer.write("public void parseParam(Map<String,String> param){")
        writer.newLine()
        for (pair in pairSet) {
            writer.write("  if( param.containsKey(\"${pair.second}\") ){")
            writer.newLine()
            writer.write("    ${pair.second} = param.get(\"${pair.second}\");")
            writer.newLine()
            writer.write("  }")
            writer.newLine()
        }
        writer.write("}")
        writer.flush()
        writer.close()
    }else{
        println("Create out file failed!")
        exitProcess(3)
    }
}