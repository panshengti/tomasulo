import java.util.ArrayList;
import java.util.Queue;

import com.sun.jmx.remote.internal.ArrayQueue;


public class Global {
	public static final int ADDD = 1;
	public static final int SUBD = 2;
	public static final int MULD = 3;
	public static final int DIVD = 4;
	public static final int LD = 5;
	public static final int ST = 6;
	
	public static final int MAXINSTR = 20;
	
	public static final int ADDDTIME = 2;
	public static final int SUBDTIME = 2;
	public static final int MULDTIME = 2;
	public static final int DIVDTIME = 2;
	public static final int LDTIME = 2;
	public static final int STTIME = 2;
	
	
	public static int currenttime = 0;
	
	public static ArrayQueue<String> instructionList = new ArrayQueue<String>(MAXINSTR);
	
}
