import java.util.ArrayList;
import java.util.Queue;

import com.sun.jmx.remote.internal.ArrayQueue;


public class Global {
	public static final int ADDD = 1;
	public static final int SUBD = 2;
	public static final int MULD = 10; // for convenience
	public static final int DIVD = 40;
	public static final int LD = 5;
	public static final int ST = 6;
	
	public static final int MAXINSTR = 20;
	
	public static final int ADDDTIME = 2;
	public static final int SUBDTIME = 2;
	public static final int MULDTIME = 2;
	public static final int DIVDTIME = 2;
	public static final int LDTIME = 2;
	public static final int STTIME = 2;
	
	// 寄存器号
	public static final int RegisterNum = 14;
	public static final int F0 = 0;
	public static final int F1 = 1;
	public static final int F2 = 2;
	public static final int F3 = 3;
	public static final int F4 = 4;
	public static final int F5 = 5;
	public static final int F6 = 6;
	public static final int F7 = 7;
	public static final int L1 = 8;
	public static final int L2 = 9;
	public static final int L3 = 10;
	public static final int S1 = 11;
	public static final int S2 = 12;
	public static final int S3 = 13;
	
	
	public static final int A1 = 14;
	public static final int A2 = 15;
	public static final int A3 = 16;
	public static final int M1 = 17;
	public static final int M2 = 18;
	
	
	// 保留站号
	public static final int RSNum = 5;
	public static final int _A1 = 0;
	public static final int _A2 = 1;
	public static final int _A3 = 2;
	public static final int _M1 = 3;
	public static final int _M2 = 4;
	
	// 保留站控制号
	public static final int WAIT = 0;
	public static final int READY = 1;
	
	public static int currenttime = 0;
	
	public static ArrayQueue<String> instructionList = new ArrayQueue<String>(MAXINSTR);
	
	public static final int IDLE = 0;
	public static final int BUSY = 1;
	
	public static final int UNFINISHED = 0;
	public static final int FINISHED = 1;
}
