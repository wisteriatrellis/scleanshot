package com.scleanshot

//import java.awt.Robot
//import java.awt.event.InputEvent
//import java.awt.Toolkit
//import java.a
import java.awt._
import java.io.File
import javax.imageio.ImageIO
import javax.swing._
import java.awt.image.BufferedImage
import java.awt.event.MouseListener
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseMotionListener
import java.awt.event.MouseAdapter



class MyPanel (
  size: Dimension,
  override val fileMainName: String = "capture",
  override val imageType: String = "png"
) extends JPanel
    with MouseListener with MouseMotionListener
    with ScleanshotIO with FileNameMaker {

  var beginX, beginY: Int = 0
  val selectedAreaColor = new Color(0.0f, 0.0f, 0.0f, 0.0f)
  val nonSelectedAreaColor = new Color(0.3f, 0.3f, 0.3f, 0.3f)

  addMouseListener(this)
  addMouseMotionListener(this)
  setBackground(nonSelectedAreaColor)
  setSize(size)
  
 
  def mouseEntered(e: MouseEvent): Unit = {}
  def mouseExited(e: MouseEvent): Unit = {}
  def mousePressed(e: MouseEvent): Unit = {
    val p = e.getPoint()
    beginX = p.x
    beginY = p.y
  }
  def mouseReleased(e: MouseEvent): Unit = {
    val p = e.getPoint()
    val g = this.getGraphics()
    clearAll(g)
    Thread.sleep(1000)
    save(
      capture(
        new Rectangle(
          beginX, beginY,
          p.x - beginX, p.y - beginY
        )
      ),
      seqFileNamePath(),
      imageType
    )
    System.exit(0)
  }
  def mouseClicked(e: MouseEvent): Unit = {}
  def mouseMoved(e: MouseEvent): Unit = {}
  def mouseDragged(e: MouseEvent): Unit = {
    val p = e.getPoint()
    val g = this.getGraphics()
    clearAll(g)
    drawNonSelectedArea(g)
    drawSelectedArea(g, p)
 }
  def clearAll(g: Graphics): Unit = {
    g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.0f))
    g.clearRect(
      0, 0,
      getWidth(), getHeight()
    )
  }
  def drawNonSelectedArea(g: Graphics): Unit = {
    g.setColor(nonSelectedAreaColor)
    g.fillRect(0, 0, getWidth(), getHeight())
  }

  def drawSelectedArea(g: Graphics, p: Point): Unit = {
    g.setColor(selectedAreaColor)
    g.clearRect(
      beginX, beginY,
      p.x - beginX, p.y - beginY
    )

    g.setColor(Color.white)
    g.drawRect(
      beginX, beginY,
      p.x - beginX, p.y - beginY
    )
  }
}


class MyWindow(
  size: Dimension
) extends JWindow {
  val backgroundColor = new Color(0.0f, 0.0f, 0.0f, 0.0f)
  setBackground(backgroundColor)
  setSize(size)
}


trait FileNameMaker {
  val fileMainName: String
  val imageType: String

  def listFilesIn(dir: String): Array[String] = 
    new File(dir)
      .listFiles()
      .map(_.getName())

  def fileNumberOf(filename: String): Int =
    removePreSuf(filename)
      .replaceAll("^0+(?!$)", "")
      .toInt
  
  // need error handling
  private def validPreSuf(filename: String): Boolean =
    filename.startsWith(fileMainName) && filename.endsWith("." + imageType)

  private def removePreSuf(filename: String): String =
    filename
      .replace(fileMainName, "")
      .replace("." + imageType, "")

  def maxFileNumberIn(files: Array[String]): Int =
    files
      .filter(validPreSuf)
      .map(removePreSuf)
      .map(fileNumberOf)
      .maxOption match {
        case Some(n) => n
        case None => 0
      }

  def seqFileNamePath(): File = {
    val filePath: File = new File(".", fileMainName + "." + imageType)
    //listFilesIn(".")
    //???
    filePath
  }
}


trait ScleanshotIO {
  def capture(
    rect: Rectangle
  ): BufferedImage = {
    val robot: Robot = new Robot()
    val screenShot: BufferedImage =
      robot.createScreenCapture(rect)
    screenShot
  }


  def save(
    screenShot: BufferedImage,
    filePath: File,
    imageType: String
  ): Unit = {
    ImageIO.write(screenShot, imageType, filePath)
  }
}



object Main {
  def main(args: Array[String]): Unit = {
    val screenSize: Dimension = Toolkit
      .getDefaultToolkit()
      .getScreenSize()
    val window = new MyWindow(screenSize)
    val panel = new MyPanel(screenSize, args(0))
    window.add(panel)
    window.setVisible(true)
    window.toFront()
  }
}
