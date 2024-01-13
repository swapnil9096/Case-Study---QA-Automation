package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.google.common.io.Files;

public class GettingUserInstructions {
	
	static WebDriver driver;
	static List<String> list = new ArrayList<>();
	static String excelfilepath = System.getProperty("user.dir") + "\\src\\test\\resources\\Instructions.xlsx";
	static String screenshotPath = System.getProperty("user.dir") + "\\src\\test\\resources\\Screenshots.png";
	
	
	public static void main(String[] args) throws IOException {
		
		//Opening the chrome browser
		driver = new ChromeDriver();
		
		//Using implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		//maximize the screen
		driver.manage().window().maximize();
		
		//Opening the website
		driver.get("https://www.google.com/maps/@28.6425088,77.217792,12z?entry=ttu");
		
		//Click on the direction button
		driver.findElement(By.xpath("//button[@id='hArJGc']")).click();
		
		//Entering your location
		driver.findElement(By.cssSelector("div[id='sb_ifc50'] input")).sendKeys("Pune");
		
		//Entering destination
		driver.findElement(By.cssSelector("div[id='sb_ifc51'] input")).sendKeys("91 Springboard, Vikhroli", Keys.ENTER);
		
		//Click on first route
		driver.findElement(By.xpath("//button[@class='TIQqpf fontTitleSmall XbJon Hk4XGb']")).click();
		
		//click on main points of driver instructions
		List<WebElement> mainPoints = driver.findElements(By.xpath("(//div[@class='Knz8tf'])"));
		
		//Clicking of every main point
		for(WebElement point : mainPoints)
		{
			point.click();
			
			//Getting the driver instructions
			List<WebElement> subPoints = driver.findElements(By.xpath("//div[@class='S0JAMb']"));
			
			for(WebElement subPoint : subPoints)
			{
				list.add(subPoint.getText());
			}
		}
		
		//Called method for inserting driver instructions into excel file.
		writeIntoExcelFile(excelfilepath,list);
		
		//Taking screenshot of the last page.
		takeScreenshot(screenshotPath);
		driver.quit();
	}
	
	
	//Creating method for getting screenshot
	public static void takeScreenshot(String filePath)
	{
		TakesScreenshot ts = (TakesScreenshot)driver;
		File srcFile = ts.getScreenshotAs(OutputType.FILE);
		File destFile = new File(filePath);
		
		try {
			Files.copy(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error in file copying...!!");
		}
	}
	
	
	
	//Creating method for inserting the driver instructions
		static void writeIntoExcelFile(String path, List<String> list)
		{	
			Workbook wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("Driver_Instructions");
			for(int i=0;i<list.size();i++)
			{
				Row row = sheet.createRow(i);
				Cell cell = row.createCell(0);
				cell.setCellValue(list.get(i));
			}
			try 
			{
				FileOutputStream fis = new FileOutputStream(path);
				wb.write(fis);
				wb.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				System.err.println("Error in file path...!!!");
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Error in file writing");
			}
		}
}














