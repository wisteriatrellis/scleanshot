package com.scleanshot

import org.scalatest.funsuite.AnyFunSuite


class FileNameMakerTest extends AnyFunSuite {
  val fileNameMaker = new {
    val fileMainName = "capture"
    val fnext = "png"
  } with FileNameMaker

  test("fileNumberOf") {
    assert(fileNameMaker.fileNumberOf("capture001.png") === Some(1))
    assert(fileNameMaker.fileNumberOf("capture020.png") === Some(20))
    assert(fileNameMaker.fileNumberOf("capture300.png") === Some(300))
    assert(fileNameMaker.fileNumberOf("capture400.svg") === None)
  }

  test("maxFileNumberIn") {
    val files1 = Array[String]("capture001.png", "capture002.png", "capture003.svg")
    assert(fileNameMaker.maxFileNumberIn(files1) === 2)
    val files2 = Array[String]("capture003.svg")
    assert(fileNameMaker.maxFileNumberIn(files2) === 0)
  }
}
