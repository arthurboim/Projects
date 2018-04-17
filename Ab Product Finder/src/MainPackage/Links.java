package MainPackage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class Links {

	void get_links(String url) throws IOException {
		int flag = 0;
		String name = url;
		name = name.substring(url.indexOf(".com/") + 5, url.indexOf("/zgbs/"));
		System.out.println("File name = " + name + " created");
		String fileName = "C:\\Users\\Noname\\Desktop\\" + name + ".txt";
		FileWriter fw = new FileWriter(fileName, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		ArrayList<String> Items = new ArrayList<String>();
		System.setProperty("webdriver.chrome.driver", "C:\\ChromeDriverFolder\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver();
		String s = null;
		String s1 = null;
		List<WebElement> temp = null;
		ArrayList<String> list2 = new ArrayList<String>();
		Items.add(url);
		list2.add(url);
		for (int j = 0; j < Items.size(); j++) {
			System.out.println("Items.size() = " + Items.size() + " j = " + j);
			try {
				driver.get(Items.get(j).toString());
				// System.out.println(Items.get(j).toString());
			} catch (Exception e) {
				System.out.println("Can't get url");
			}
			temp = driver.findElements(By.tagName("a"));
			s = driver.getCurrentUrl();
			try {
				s = s.substring(s.indexOf("Best-Sellers"));
			} catch (Exception e) {
				s = s.substring(s.indexOf("best-sellers"));
			}
			s = s.substring(0, s.indexOf("/"));
			System.out.println(s);
			for (WebElement ele : temp) // https://www.amazon.com/Best-Sellers-
			{
				if (ele.getAttribute("href") != null && !(ele.getAttribute("href").contains("encoding=UTF8&pg"))
						&& !(ele.getAttribute("href").contains("/uedata/nvp/unsticky/"))
						&& (ele.getAttribute("href").contains("https://www.amazon.com/best-sellers-")
								|| ele.getAttribute("href").contains("https://www.amazon.com/Best-Sellers-"))) {
					if (!(ele.getAttribute("href").contains("#nav-top"))) {

						s1 = ele.getAttribute("href");
						s1 = s1.substring(0, s1.indexOf("/zgbs/"));
						for (String ele2 : list2) {
							if (ele2.contains(s1))
								flag = 1;
						}

						if (flag == 0) {
							list2.add(s1);
							Items.add(ele.getAttribute("href"));
							System.out.println(ele.getAttribute("href"));
							out.println(ele.getAttribute("href"));
						}
						flag = 0;
					}
				}
			}

			out.flush();
		}
		out.close();
		System.out.println("End");
		
	}

}
