package io.crossroads.jni

import java.io.File
import java.io.InputStream
import java.io.OutputStream
import org.apache.commons.io._

object Library {

	def load(): Unit = {
		try {
			System.loadLibrary("sxs.so")
		} catch {
			case e: UnsatisfiedLinkError => {
				System.out.println("Error: " + e)
				loadExt
			}
		}
	}
	
	private def loadExt(): Unit = {
		val fileName = "/sxs.so"
		val in: InputStream = this.getClass().getResourceAsStream(fileName)
		var fileOut: File = new File(System.getProperty("java.io.tmpdir") + fileName)
		var out: OutputStream =  FileUtils.openOutputStream(fileOut)
		IOUtils.copy(in, out)
		in.close
		out.close
		try {
			System.load(fileOut.toString())
		} catch {
			case e: UnsatisfiedLinkError => System.out.println("Library is already loaded.")
		}
	}
}