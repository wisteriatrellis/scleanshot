package com.scleanshot

import org.scalatest.funsuite.AnyFunSuite


class FileNameMakerTest extends AnyFunSuite {
  test("file number test") {
    val fileNameMaker = new Object with FileNameMaker
    assert(fileNameMaker.fileNumberOf("capture001.png") == 1)
    assert(fileNameMaker.fileNumberOf("capture020.png") == 20)
    assert(fileNameMaker.fileNumberOf("capture300.png") == 300)
  }
}
