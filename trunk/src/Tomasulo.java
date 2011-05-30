import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Tomasulo {
	
	FloatRegister register;
	ReservationStation rs;
	Memory mem;
	Adder adder;
	Multiplier mul;
	LSQueue lsqueue;
	ArrayList<Object> stations;
	ArrayList<InstructionItem> instList;
	int clock;
	int pc;
	
	public Tomasulo(){
		register = new FloatRegister();
		rs = new ReservationStation();
		mem = new Memory();
		adder = new Adder();
		mul = new Multiplier();
		stations = new ArrayList<Object>();
		lsqueue = new LSQueue();
		clock = 0;
		pc = 0;
		
		int i=0;
		for (i=0; i<Global.RegisterNum; i++){
			stations.add(register.register[i]);
		}
		for (i=0; i<Global.RegisterNum + Global.LSQNum; i++){
			stations.add(lsqueue.queue[i]);
		}
		for ( ; i<Global.RegisterNum + Global.LSQNum + Global.RSNum; i++){
			stations.add(rs.rs[i]);
		}
		
		instList = new ArrayList<InstructionItem>();
		
		BufferedReader br;
		try {
			br = new BufferedReader (new FileReader("instruction.txt"));
			String str = "";
			while (null != (str = br.readLine())){
				InstructionItem ii = new InstructionItem(str);
				instList.add(ii);
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clockInc(){
		
	}
	
	public void step(){
		clock ++;
		if (clock < instList.size()){
			issue();
		}
			
		execute();
		writeback();
	}
	
	public void issue(){
		
		// decoder
		InstructionItem inst = instList.get(pc);
		String s[] = inst.name.split("\\s+");
		String op = s[0];
		int src1 = 0, src2 = 0, des = 0;
		if (op.equals("ADDD")){
			des = Global.getInt(s[1]);
			src1 = Global.getInt(s[2]);
			src2 = Global.getInt(s[3]);
			inst.time = 2;
			
			int station = schedule(Global.A);
			register.setStation(des, station);
			
			pc ++;
			
		} else if (op.equals("SUBD")){
			des = Global.getInt(s[1]);
			src1 = Global.getInt(s[2]);
			src2 = Global.getInt(s[3]);
			inst.time = 2;
			
			int station = schedule(Global.A);
			register.setStation(des, station);
			
			pc++;
		} else if (op.equals("MULD")){
			des = Global.getInt(s[1]);
			src1 = Global.getInt(s[2]);
			src2 = Global.getInt(s[3]);
			inst.time = 10;
			
			int station = schedule(Global.M);
			if (station == -1){
				return;
			}
			register.setStation(des, station);
			pc++;
			
		} else if (op.equals("DIVD")){
			des = Global.getInt(s[1]);
			src1 = Global.getInt(s[2]);
			src2 = Global.getInt(s[3]);
			inst.time = 40;
			
			int station = schedule(Global.M);
			if (station == -1){
				return;
			}
			register.setStation(des, station);
			pc ++;
			
		} else if (op.equals("LD")) {
			des = Global.getInt(s[1]);
			src1 = Integer.parseInt(s[2]);
			inst.time = 2;
			
			int station = schedule(Global.L);
			
			// load_addr 是已有mem操作的 addr
			for (int j=Global.S1; j<=Global.S3; j++){
				if ( lsqueue.isBusy(j)){
					if (src1 == lsqueue.getAddr(j)){
						return;
					}
				}
			}
			
			register.setStation(des, station);
			
			pc ++;
			
		} else if (op.equals("ST")) {
			src1 = Global.getInt(s[1]);
			des = Integer.parseInt(s[2]);		
			inst.time = 2;
			
			int station = schedule(Global.S);
			
			for (int j=Global.L1; j<=Global.L3; j++){
				if ( lsqueue.isBusy(j)){
					if (src1 == lsqueue.getAddr(j)){
						return;
					}
				}
			}
			
			// waiting src's station if any
			if (register.getStation(src1) != -1){
				lsqueue.setStation(des, register.getStation(src1));
			}
			
			pc ++;
			
			
		} else {
			System.out.println("Error Instruction!");
		}
		
		inst.issue = clock;
		
		
		
	}
	
	public void execute(){
		
	}
	
	public void writeback(){
		
	}
	
	public int schedule(int type){
		int i = 0;
		switch (type){
		case Global.L:
			for (i=Global.L1; i<=Global.L3; i++){
				if ( !lsqueue.isBusy(i)){
					return i;
				}
			}
			break;
			
		case Global.S:
			for (i=Global.S1; i<=Global.S3; i++){
				if ( !lsqueue.isBusy(i)){
					return i;
				}
			}
			break;
			
		case Global.A:
			for (i=Global.A1; i<=Global.A3; i++){
				if ( !rs.isBusy(i)){
					return i;
				}
			}
			break;
			
		case Global.M:
			for (i=Global.M1; i<=Global.M2; i++){
				if ( !rs.isBusy(i)){
					return i;
				}
			}
			break;
		}
		
		return -1;	
	}
	

}
