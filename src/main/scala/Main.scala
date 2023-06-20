import scopt.OParser
import play.api.libs.json.{Json, JsArray}
import scalaj.http.{Http, HttpResponse}

case class Config(limit: Int = 10, keyword: String = "")

case class WikiPage(title: String, words: Int)

object Main extends App {
  parseArguments(args) match {
    case Some(config) => run(config)
    case _            => println("Unable to parse arguments")
  }

  def parseArguments(args: Array[String]): Option[Config] = {
    val builder = OParser.builder[Config]
    val parser = {
      import builder._
      OParser.sequence(
        programName("WikiStats"),
        opt[Int]('l', "limit")
          .action((value, config) => config.copy(limit = value))
          .text("Limit the number of pages (default: 10)"),
        arg[String]("<keyword>")
          .required()
          .action((value, config) => config.copy(keyword = value))
          .text("Keyword to search")
      )
    }
    OParser.parse(parser, args, Config())
  }

  def formatUrl(keyword: String, limit: Int): String = {
    val encodedKeyword = java.net.URLEncoder.encode(keyword, "UTF-8")
    s"https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&sroffset=0&list=search&srsearch=$encodedKeyword&srlimit=$limit"
  }

  // GetPages
  def getPages(url: String): Either[Int, String] = {
    val response: HttpResponse[String] = Http(url).asString

    if (response.is2xx) {
      Right(response.body)
    } else {
      Left(response.code)
    }
  }

  def parseJson(rawJson: String): Seq[WikiPage] = {
    val json = Json.parse(rawJson)
    val searchResults = (json \ "query" \ "search").as[JsArray].value
    searchResults.map { result =>
      val title = (result \ "title").as[String]
      val words = (result \ "wordcount").as[Int]
      WikiPage(title, words)
    }
  }
  
  def totalWords(pages: Seq[WikiPage]): Int = {
    pages.foldLeft(0)((count, page) => count + page.words)
  }

  def run(config: Config): Unit = {
    val apiUrl = formatUrl(config.keyword, config.limit)
    val response = Http(apiUrl).asString
    val rawJson = response.body
    val wikiPages = parseJson(rawJson)
    val totalPages = wikiPages.length
    val totalWordsCount = totalWords(wikiPages)
    
    println(s"Number of pages found: $totalPages")
    println(s"Total words count: $totalWordsCount")
    
    wikiPages.foreach { page =>
      println(s"Title: ${page.title}, Words: ${page.words}")
    }
  }
}
