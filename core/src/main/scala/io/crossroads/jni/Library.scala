package io.crossroads.jni

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import org.apache.commons.io._

object Library {

	val fileName = "/XSLibrary.so"
		
	def load(): Unit = {
		val in: InputStream = this.getClass().getResourceAsStream(fileName)
        var fileOut: File = new File(System.getProperty("java.io.tmpdir") + fileName)
        var out: OutputStream =  FileUtils.openOutputStream(fileOut)
        IOUtils.copy(in, out)
        in.close
        out.close
        System.load(fileOut.toString())
	}
}