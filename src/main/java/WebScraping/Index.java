package WebScraping;

import java.io.FileWriter;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Index {

	public static void main(String[] args) {
		try {
			homePage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void homePage() throws Exception {
		try (final WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
			webClient.getOptions().setCssEnabled(false);
			// webClient.getOptions().setJavaScriptEnabled(false);

			final HtmlPage page = webClient.getPage("https://www.foody.vn/ha-noi");
			webClient.waitForBackgroundJavaScript(3000);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
// Assert.assertEquals("HtmlUnit - Welcome to HtmlUnit", page.getTitleText());

			final String pageAsXml = page.asXml();
			// Assert.assertTrue(pageAsXml.contains("<body class=\"composite\">"));

			final String pageAsText = page.asText();
			// Assert.assertTrue(pageAsText.contains("Support for the HTTP and HTTPS
			// protocols"));

			// System.out.println(pageAsXml);

			final DomNodeList<DomNode> divs = page.querySelectorAll("ul > li > a.avatar");
			try {
				FileWriter fw = new FileWriter("test.txt");
				for (DomNode div : divs) {
					HtmlAnchor htmlAnchor = (HtmlAnchor) div;
					if (htmlAnchor.getAttribute("href") != null) {
						HtmlPage pages = htmlAnchor.click();
						final DomNode btnLoad_More = pages.querySelector("a.fd-btn-more");
						if (btnLoad_More != null) {
							((HtmlAnchor) btnLoad_More).click();
//							pages = ((HtmlAnchor) btnLoad_More).click();
						
							synchronized (pages) {
								pages.wait(3000); //wait
							}
//							System.out.println(pages.asText());
						}
						else System.out.println("not click!!");
						System.out.println(btnLoad_More);

						final DomNodeList<DomNode> cmts = pages.querySelectorAll("div.rd-des > span");
						final DomNodeList<DomNode> points = pages.querySelectorAll("div.review-points > span");

						fw.write(htmlAnchor.getAttribute("href") + "\n \n");
						for(int i=0; i < cmts.getLength(); i++) {
							fw.write(cmts.get(i).getTextContent() + "\n \n"+points.get(i).getTextContent()+"\n \n");
						}
//						for (DomNode cmt : cmts) {
//							fw.write(cmt.getTextContent() + "\n \n");
//						}
						fw.write("----------------------- \n");
					}
				}
				fw.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

}
