package src.org.gosparx.team1126.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import edu.wpi.first.wpilibj.Timer;
import src.org.gosparx.team1126.subsytems.GenericSubsystem;

public class Logger extends Thread {

	private static Logger logger;
	
	private Timer timer;

	private String logName;
	private final String COMPRESSION_MODE = "TAR.GZ";
	private final String COUNTER_FILE = "counter.txt";
	private final String LOGS_DIRECTORY_LOCATION = "/home/lvuser/logs/"; //starting from the home directory
	private static boolean logReady;
	private static final boolean LOG_TO_CONSOLE = true;
	private static final boolean PERIODIC_LOG_TO_CONSOLE = true;
	private Vector<PeriodicLog> periodicLogs;

	private DecimalFormat df;
	private DecimalFormat df2;
	private Vector<String> dataToLog;
	private File logFile;

	public Logger() {
		periodicLogs = new Vector<PeriodicLog>();
		logReady = false;
		if(!makeLogsDir()) { 
			return;			  
		}					
		long counterNumber = readCounter();
		if(counterNumber == -1) {
			return;
		}
		compression();
		logName = LocalDateTime.now().getDayOfMonth() + "_" + LocalDateTime.now().getMonth() + "_" + LocalDateTime.now().getYear() + 
				"-" + LocalDateTime.now().getHour() + "_" + LocalDateTime.now().getMinute() + "_" + LocalDateTime.now().getSecond() + ".log";
		timer = new Timer();
		timer.start();
		try {
			logFile = new File(LOGS_DIRECTORY_LOCATION + counterNumber + "-" + logName);
			logFile.createNewFile();
			dataToLog = new Vector<String>();
			df = new DecimalFormat();
			df.setGroupingUsed(false);
			df.setMinimumFractionDigits(3);
			df.setMaximumFractionDigits(3);
			df.setMinimumIntegerDigits(2);
			df2 = new DecimalFormat();
			df2.setGroupingUsed(false);
			df2.setMinimumIntegerDigits(2);
			log("LOGGER", "INFO", "Log created at " + logName);
		} catch (Exception e) {
			return;
		}
		logReady = true;
	}
	
	public static Logger getInstance() {
		if(logger == null) {
			logger = new Logger();
			PrintStream os = new PrintStream(System.out) {
				@Override
				public void print(String str) {
					if(!LOG_TO_CONSOLE) {
						super.print(str);
					}
					logger.log("SYSOUT", str);
				}
			};
			System.setOut(os);
			logger.start();
		}
		return logger;
	}

	public enum Tag{
		INFO,			//Used for software-related logs, default
		ERROR,			//Used for errors
		CRITICAL,		//Used for Important problems that are not yet errors
		WARNING,		//Used for Lower level problems
		START,			//Used at the start of commands
		END,			//Used at the end of commands
		STATUS,			//Used for logging the state of hardware
		INTERRUPTED;	//Used when commands have been interrupted
	}

	public void log(String subsystem, String method, Tag type, String message) {
		String log = "[" + timerToHMS() + "][" + subsystem.toUpperCase() + "][" + method + "][" + type.toString().toUpperCase() + "] " + message;
		if(!logReady) {
			System.out.println(log);
			return;
		}
		if(LOG_TO_CONSOLE) {
			System.out.println(log);
		}
		dataToLog.add(log);
	}

	public void log(String subsystem, Tag type, String message) {
		String log = "[" + timerToHMS() + "][" + subsystem.toUpperCase() + "][" + type.toString().toUpperCase() + "] " + message;
		if(!logReady) {
			System.out.println(log);
			return;
		}
		if(LOG_TO_CONSOLE) {
			System.out.println(log);
		}
		dataToLog.add(log);
	}

	public void log(GenericSubsystem subsystem, String command, Tag type, String message) {
		String log = "[" + timerToHMS() + "][" + subsystem.getName().toUpperCase() + "][" + command + "][" + type.toString().toUpperCase() + "] " + message;
		if(!logReady) {
			System.out.println(log);
			return;
		}
		if(LOG_TO_CONSOLE) {
			System.out.println(log);
		}
		dataToLog.add(log);
	}

	public void log(GenericSubsystem subsystem, Tag type, String message) {
		String log = "[" + timerToHMS() + "][" + subsystem.getName().toUpperCase() + "][" + type.toString().toUpperCase() + "] " + message;
		if(!logReady) {
			System.out.println(log);
			return;
		}
		if(LOG_TO_CONSOLE) {
			System.out.println(log);
		}
		dataToLog.add(log);
	}

	public void log(String subsystem, String method, String message) {
		String log = "[" + timerToHMS() + "][" + subsystem.toUpperCase() + "][" + method + "][INFO] " + message;
		if(!logReady) {
			System.out.println(log);
			return;
		}
		if(LOG_TO_CONSOLE) {
			System.out.println(log);
		}
		dataToLog.add(log);
	}

	public void log(String subsystem, String message) {
		String log = "[" + timerToHMS() + "][" + subsystem.toUpperCase() + "][INFO] " + message;
		if(!logReady) {
			System.out.println(log);
			return;
		}
		if(LOG_TO_CONSOLE) {
			System.out.println(log);
		}
		dataToLog.add(log);
	}

	public void log(GenericSubsystem subsystem, String method, String message) {
		String log = "[" + timerToHMS() + "][" + subsystem.getName().toUpperCase() + "][" + method + "][INFO] " + message;
		if(!logReady) {
			System.out.println(log);
			return;
		}
		if(LOG_TO_CONSOLE) {
			System.out.println(log);
		}
		dataToLog.add(log);
	}

	public void log(GenericSubsystem subsystem, String message) {
		String log = "[" + timerToHMS() + "][" + subsystem.getName().toUpperCase() + "][INFO] " + message;
		if(!logReady) {
			System.out.println(log);
			return;
		}
		if(LOG_TO_CONSOLE) {
			System.out.println(log);
		}
		dataToLog.add(log);
	}

	public void logPeriodically(GenericSubsystem subsystem, Object caller, Method getter) {
		periodicLogs.add(new PeriodicLog(subsystem, Tag.INFO, getter, caller));
	}

	public void logPeriodically(GenericSubsystem subsystem, Tag tag, Object caller, Method getter) {
		periodicLogs.add(new PeriodicLog(subsystem, tag, getter, caller));
	}

	public void logPeriodically(GenericSubsystem subsystem, String method, Object caller, Method getter) {
		periodicLogs.add(new PeriodicLog(subsystem, method, Tag.INFO, getter, caller));
	}

	public void logPeriodically(GenericSubsystem subsystem, String method, Tag tag, Object caller, Method getter) {
		periodicLogs.add(new PeriodicLog(subsystem, method, tag, getter, caller));
	}

	public void removePeriodicLog(String messageOfLog) {
		for(int i = 0; i < periodicLogs.size(); i++) {
			String log = periodicLogs.get(i).getMessage();
			if(log.equals(messageOfLog)) {
				periodicLogs.remove(i);
			}
		}
	}

	public boolean periodicLogsContain(String messageOfLog) {
		for(int i = 0; i < periodicLogs.size(); i++) {
			String log = periodicLogs.get(i).getMessage();
			if(log.equals(messageOfLog)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		while(!isInterrupted()) {
			try {
				sleep(1000);
				if(logReady) {
					//for(PeriodicLog log: periodicLogs) {
				//		dataToLog.add(log.toString());
				//		if(PERIODIC_LOG_TO_CONSOLE) {
				//			System.out.println(log.toString());
				//		}
				//	}
					while(dataToLog.size() > 0) {
						Files.write(logFile.toPath(), (dataToLog.remove(0) + "\n").getBytes(), StandardOpenOption.APPEND, StandardOpenOption.DSYNC, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logReady = false;
			}

		}

	}

	private String timerToHMS() {
		double timeDouble = timer.get();
		return (df2.format((int) (timeDouble / 3600))) + ":" + df2.format(((int) ((timeDouble / 60) % 60))) + ":" + df.format(timeDouble % 60);
		//some accuracy is lost with the time % 60 because of doubles and math but thats okkkkkkkkkkkkkkkkk its efficient????
	}

	private boolean makeLogsDir() {
		File logsDirectory = new File(LOGS_DIRECTORY_LOCATION);
		logsDirectory.mkdirs();
		if(!logsDirectory.exists()) {
			return false;
		}
		return true;
	}

	private long readCounter() {//we probably don't need a long but w/e "just in case"
		try {
			File counter = new File(LOGS_DIRECTORY_LOCATION + COUNTER_FILE);
			if(counter.exists()) {
				Scanner sc = new Scanner(new FileReader(counter));
				long ln = sc.nextLong();
				sc.close();
				Files.write(counter.toPath(), ((ln + 1) + "").getBytes(), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.DSYNC, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
				return ln;
			} else {
				counter.createNewFile();
				Files.write(counter.toPath(), "2".getBytes());
				return 1;
			}
		} catch(Exception e) {
		}
		return -1;
	}

	private void compression() {
		File logsDirectory = new File(LOGS_DIRECTORY_LOCATION);
		File[] files = logsDirectory.listFiles();
		ArrayList<String> logs = new ArrayList<String>();
		for(File file: files) {
			if(file.getName().endsWith(".log")) {
				logs.add(file.getName().substring(0, file.getName().lastIndexOf('.')));
			}
		}
		if(COMPRESSION_MODE.equals("TAR.GZ")) {
			for(String str: logs) {
				File compressedFile = new File(LOGS_DIRECTORY_LOCATION + str + ".tar.gz");
				File originalFile = new File(LOGS_DIRECTORY_LOCATION + str + ".log");
				compressedFile.delete();
				try {
					Process ps = Runtime.getRuntime().exec("tar -zcf " + str + ".tar.gz " + str + ".log");
					ps.waitFor();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(compressedFile.exists()) {
					originalFile.delete();
				}
			}
		} else if(COMPRESSION_MODE.equals("ZIP")) {
			for(String str: logs) {
				File compressedFile = new File(LOGS_DIRECTORY_LOCATION + str + ".zip");
				File originalFile = new File(LOGS_DIRECTORY_LOCATION + str + ".log");
				compressedFile.delete();
				try {
					Process ps = Runtime.getRuntime().exec("zip " + str + ".zip " + str + ".log");
					ps.waitFor();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(compressedFile.exists()) {
					originalFile.delete();
				}
			}
		} else {
			System.err.println("[LOGGER][ERROR] Unsupported compression mode - " + COMPRESSION_MODE);
		}

	}

	public class PeriodicLog {
		private String subsystem;
		private String method;
		private String tag;
		private String message;
		private Method update;
		private Object caller;
		/**
		 * Creates a periodic log to display every so often in logs.
		 * @param message - the message of the log. This should contain, if its a STATUS tag, one or more portions of {NAME_OF_COMPONENT, COMPONENT_TYPE, ACTUAL_COMPONENT_OUTPUT} should be included;
		 * 		The name of the component should be a non-empty String, the component type if motor is "0", if solenoid "1", if encoder "2". The actual component output is the output of that component.
		 * 		Please be sure to actually include "{" and "}" on either end with no spaces inside. Include the commas as well.
		 * @param getter - the method called to update the logs information. Should have no parameters!!
		 * @param caller - Put the object that contains the getter method
		 */
		public PeriodicLog(GenericSubsystem subsystem, Tag tag, Method getter, Object caller) {
			this.subsystem = subsystem.toString();
			this.tag = tag.toString(); 
			update = getter;
			this.caller = caller;
		}

		/**
		 * Creates a periodic log to display every so often in logs.
		 * @param message - the message of the log; if(tag == Tag.STATUS), one or more portions of {NAME_OF_COMPONENT, COMPONENT_TYPE, ACTUAL_COMPONENT_OUTPUT} should be included;
		 * 		The name of the component should be a non-empty String, if(COMPONENT_TYPE.equals("0")) -> motor, if(COMPONENT_TYPE.equals("1")) -> solenoid, if(COMPONENT_TYPE.equals("2")) -> encoder. The actual component output is the output of that component.
		 * 		Please be sure to actually include "{" and "}" on either end with no spaces inside. Include the commas as well.
		 * @param getter - the method called to update the logs information. Should have no parameters!!
		 * @param caller - Put the object that contains the getter method
		 */
		public PeriodicLog(GenericSubsystem subsystem, String method, Tag tag, Method getter, Object caller) {
			this.method = method;
			this.subsystem = subsystem.getName(); 
			this.tag = tag.toString();
			update = getter;
			this.caller = caller;
		}

		/**
		 * Gets the subsystem of this class and the command(if it has one)
		 * @return
		 */
		public String getSubsystem() {
			return subsystem + (method == null ? "" : "][" + method);
		}

		public String getTags() {
			return tag;
		}

		public String getMessage() {
			return message;
		}

		private void update() {
			try {
				message = (String) update.invoke(caller, new Object[] {}); //Yes no warnings
			} catch (Exception ex) {
				ex.printStackTrace();
			}	
		}

		public String toString() {
			update();
			return "[" + timerToHMS() + "][" + getSubsystem() + "][" + getTags() + "]" + getMessage();
		}
	}
}
