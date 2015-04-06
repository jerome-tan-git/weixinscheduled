import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

public class TestSend {
	public static void main(String[] args) {
		for(;;)
		{
			System.setProperty("phantomjs.binary.path", "C:\\phantomjs\\phantomjs.exe");
			WebDriver driver = new PhantomJSDriver();
			try
			{
//http://3g.mokxw.com/yk/www/mimi.php?hm=15097904000&c=57
		String name1 = UUID.randomUUID().toString();
		String name2 = UUID.randomUUID().toString();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(3000, TimeUnit.SECONDS);
//		driver.manage().window().setSize(new Dimension(800, 1000));
		driver.get("http://www.ddayh.com/dx/index.php?hm=15097904000&c=3");
		try {
			Thread.sleep(60000*5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			}
			catch(Exception e){}finally
			{
				driver.quit();
			}
		}
	}
}
