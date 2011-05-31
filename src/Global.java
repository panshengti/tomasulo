
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
	public static final int RegisterNum = 8;
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
	
	// LSQ
	public static final int LSQNum = 6;
	public static final int _L1 = 0;
	public static final int _L2 = 1;
	public static final int _L3 = 2;
	public static final int _S1 = 3;
	public static final int _S2 = 4;
	public static final int _S3 = 5;
	
	
	
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
	
	
	public static final int IDLE = 0;
	public static final int BUSY = 1;
	
	public static final int UNFINISHED = 0;
	public static final int FINISHED = 1;
	
	//
	public static final int F = 0;
	public static final int L = 1;
	public static final int S = 2;
	public static final int A = 3;
	public static final int M = 4;
	
	
	public static int getID(int a){
		if (a > 13) a -= 14;
		else if (a > 7) a -= 8;
		
		return a;
	}
	public static int getInt(String s){
		s = s.substring(1);
		return Integer.parseInt(s);
	}
	public static String getStationName(int i){
		switch (i){
		case 0:
			return "F0";
		case 1:
			return "F1";
		case 2:
			return "F2";
		case 3:
			return "F3";
		case 4:
			return "F4";
		case 5:
			return "F5";
		case 6:
			return "F6";
		case 7:
			return "F7";
		case 8:
			return "Load1";
		case 9:
			return "Load2";
		case 10:
			return "Load3";
		case 11:
			return "Store1";
		case 12:
			return "Store2";
		case 13:
			return "Store3";
		case 14:
			return "Add1";
		case 15:
			return "Add2";
		case 16:
			return "Add3";
		case 17:
			return "Mul1";
		case 18:
			return "Mul2";
		}
		
		return null;
	}
}
