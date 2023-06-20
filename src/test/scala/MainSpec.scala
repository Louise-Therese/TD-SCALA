import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import scopt.OParser
import play.api.libs.json.{Json, JsArray}
import scalaj.http.{Http, HttpResponse}
import Main.parseJson

class MainSpec extends AnyFlatSpec with Matchers {
  "formatUrl" should "return the correct URL" in {
    val keyword = "Scala"
    val limit = 20
    val expectedUrl = "https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&sroffset=0&list=search&srsearch=Scala&srlimit=20"

    val actualUrl = Main.formatUrl(keyword, limit)

    actualUrl should be(expectedUrl)
  }

  "parseJson" should "return a sequence of WikiPage objects parsed from the raw JSON" in {
    val rawJson =
      """
        |{
        |  "query": {
        |    "search": [
        |      {
        |        "title": "Mon site 1",
        |        "wordcount": 100
        |      },
        |      {
        |        "title": "Mon site 2",
        |        "wordcount": 200
        |      }
        |    ]
        |  }
        |}
        |""".stripMargin

    val expectedWikiPages = Seq(
      WikiPage("Mon site 1", 100),
      WikiPage("Mon site 2", 200)
    )

    val parsedWikiPages = parseJson(rawJson)

    parsedWikiPages should be(expectedWikiPages)
  }

  "totalWords" should "return 0 for an empty list" in {
    val pages = Seq.empty[WikiPage]
    val expectedTotal = 0

    val actualTotal = Main.totalWords(pages)

    actualTotal should be(expectedTotal)
  }

  it should "return the correct total word count for a non-empty list" in {
    val pages = Seq(
      WikiPage("Page 1", 100),
      WikiPage("Page 2", 200),
      WikiPage("Page 3", 300)
    )
    val expectedTotal = 600

    val actualTotal = Main.totalWords(pages)

    actualTotal should be(expectedTotal)
  }

"parseArguments" should "return None for non-parsable arguments" in {
    val args = Array("--invalid-option")

    val result = Main.parseArguments(args)

    result should be(None)
  }

  it should "return Some(keyword) for arguments with keyword" in {
    val keyword = "Scala"
    val args = Array("--keyword", keyword)

    val result = Main.parseArguments(args)

    result should be(Some(keyword))
  }

  it should "return Some(keyword, limit) for arguments with keyword and limit" in {
    val keyword = "Scala"
    val limit = 10
    val args = Array("--keyword", keyword, "--limit", limit.toString)

    val result = Main.parseArguments(args)

    result should be(Some(keyword, limit))
  }


}








