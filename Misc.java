import java.awt.List;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileStore;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;

public class Misc {
	
	public static final int MINUTE = 180000;
	public static final int X = 400;
	public static final int Y = 400;
	////////////User Name//////////
	public static final String userName = System.getProperty("user.name");
	///////////Computer Name///////
	public static final String compName = getComputerName();
	///////////Serial Number///////
	public static final String serialNumber = getSerialNumber();
	///////////Disk Space//////////
	public static final String diskSpace = getDiskSpace();
	///////////Memory Status///////
	public static final String memory = getMemory();
	///////////File Roots//////////
	public static final String roots = getRoots();
	///////////Core Processors/////
	public static final String coreProcs = getCoreProcessors();
	//////////Java version/////////
	//public static double JAVA_VERSION = getVersion ();
	public static String JAVA_VERSION_STR = getVersionStr();
	
	
	public static void main(String[] args) throws Exception
    { 	
    	
		///////////Date and Time///////
    	//String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    	//String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);
    	//LocalDateTime timestamp = LocalDateTime.now();
		Robot robot = new Robot();
		String timestamp = getLocalDateTime();
    	
    	print("Starting...");
    	int i = 1;
    	while (true)
    	{
    		
    		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
    		robot.mouseMove(mousePosition.x, mousePosition.y);
    		
    		Thread.sleep(MINUTE);
    		
    		print("By: " + userName + " on computer: " + compName
    				+ ", date: " + timestamp + " with SN: " + serialNumber);
    		print("Disk Space: " + diskSpace);
    		print(coreProcs);
    		print(roots);
    		print(memory);
    		print("Java Version: " + JAVA_VERSION_STR);
    		if(i == 1) {
    			print("Moved " + i + " time ");
    		} else {
    			print("Moved " + i + " times ");
    		}
    		// The comment below actually prints!    
    		// \u000d System.out.println("\n"); 
    		
    		i++;
    	}
    }
	
	/**
	 * 
	 * @return java version
	 */
	static double getVersion () {
	    String version = System.getProperty("java.version");
	    int pos = version.indexOf('.');
	    pos = version.indexOf('.', pos+1);
	    return Double.parseDouble (version.substring (0, pos));
	}
	
	static String getVersionStr () {
		String version = Runtime.class.getPackage().getImplementationVersion();
		return version;
	}
	
	/**
	 * 
	 * @return Total number of processors or cores available to the JVM
	 */
	public static String getCoreProcessors() {
		/* Total number of processors or cores available to the JVM */
	    String coreProcs = "Available processors (cores): " + 
	        Runtime.getRuntime().availableProcessors();
		return coreProcs;
	}
	
	/**
	 * 
	 * @return list of all filesystem roots on this system
	 */
	public static String getRoots() {
		/* Get a list of all filesystem roots on this system */
	    File[] roots = File.listRoots();
	    
	    String rootsStr = "";
		/* For each filesystem root, print some info */
	    for (File root : roots) {
	      rootsStr += "File system root: " + root.getAbsolutePath();
	      //rootsStr += "File system: " + root.getPath();
	      //System.out.println("Total space (bytes): " + root.getTotalSpace());
	      //rootsStr += " Free space (bytes): " + root.getFreeSpace();
	      //rootsStr += " Usable space (bytes): " + root.getUsableSpace();
	    }
	    
	    return rootsStr;
	}
	
	/**
	 * 
	 * @return computer name
	 */
	public static String getComputerName() {
		
		String hostname = "Unknown";
		
		try
		{
		    InetAddress addr;
		    addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		}
		catch (UnknownHostException ex)
		{
		    System.out.println("Hostname can not be resolved");
		}
		
		return hostname;
	}
	
	/**
	 * 
	 * @return MST date/time
	 */
	public static String getLocalDateTime() {
		//24-hour format
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
		String timestamp = "no timestamp";
		
		Date date = new Date();
		df.setTimeZone( TimeZone.getTimeZone( "US/Mountain" ) );
		timestamp = df.format(date);
		
		return timestamp;
	}
	
	/**
	 * 
	 * @return serial number of computer
	 */
	public static String getSerialNumber(){
		// wmic command for diskdrive id: wmic DISKDRIVE GET SerialNumber
        // wmic command for cpu id : wmic cpu get ProcessorId
        Process process = null;
		try {
			process = Runtime.getRuntime().exec(new String[] { "wmic", "bios", "get", "serialnumber" });
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			process.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        @SuppressWarnings("resource")
		Scanner sc = new Scanner(process.getInputStream());
        @SuppressWarnings("unused")
		String property = sc.next();
        String serial = sc.next();
        return serial;
	}
	
	/**
	 * 
	 * @return disk space
	 */
	public static String getDiskSpace() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		String diskSpace = "NDP";
		for (Path root : FileSystems.getDefault().getRootDirectories()) {

		    System.out.print(root + ": ");
		    try {
		        FileStore store = Files.getFileStore(root);
		        diskSpace = "Available=" + nf.format(store.getUsableSpace())
		                            + " Total=" + nf.format(store.getTotalSpace());
		    } catch (IOException e) {
		        System.out.println("error querying space: " + e.toString());
		    }
		}
		
		return diskSpace;
	}
	
	/**
	 * 
	 * @return memory (RAM)
	 */
	public static String getMemory() {
		Runtime r=Runtime.getRuntime();  
		String memory = "Total Memory: "+r.totalMemory() +  
						" Free Memory: "+r.freeMemory();
		
		return memory;
	}
	
	/**
	 * Prints String line
	 * @param s
	 */
	public static void print(String s){
		   System.out.println(s);
	}
	
	/**
	 * Prints double line
	 * @param d
	 */
	public static void print(double d) {
		System.out.print(d);
	}

}