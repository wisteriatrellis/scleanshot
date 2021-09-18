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
  override val filename: String = "capture",
  override val imageType: String = "png"
) extends JPanel
    with MouseListener with MouseMotionListener with ScleanshotIO {

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
      seqFileNamePath()
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
  def listFilesIn(dir: String): Unit = {
    val files = new File(dir).listFiles()
    files
      .map(_.getName())
      .foreach(println)
  }

  def fileNumberOf(filename: String): Int = filename match {
    case "capture001.png" => 1
    case "capture020.png" => 20
    case "capture300.png" => 300
  }
  //def seqFileNamePath(): File = {
  //  ???
  //}
}


trait ScleanshotIO {
  val filename: String
  val imageType: String

  def capture(
    rect: Rectangle
  ): BufferedImage = {
    val robot: Robot = new Robot()
    val screenShot: BufferedImage =
      robot.createScreenCapture(rect)
    screenShot
  }

  def seqFileNamePath(): File = {
    val filePath: File = new File(".", filename + ".png")
    listFilesIn(".")
    //???
    filePath
  }

  def listFilesIn(dir: String): Unit = {
    val files = new File(dir).listFiles()
    files
      .map(_.getName())
      .foreach(println)
  }

  def fileNumberOf(filename: String): Int = filename match {
    case "capture001.png" => 1
    case "capture020.png" => 20
    case "capture300.png" => 300
  }


  def save(
    screenShot: BufferedImage,
    filePath: File,
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

  }
}
