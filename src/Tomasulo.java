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
	ArrayList<Float> stations;
	ArrayList<InstructionItem> instList;
	
	InstructionItem nextToExec = null;
	ArrayList<InstructionItem> execList;
	ArrayList<InstructionItem> wbList;
	
	static int clock;
	int pc;
	
	public Tomasulo(){
		register = new FloatRegister();
		rs = new ReservationStation();
		mem = new Memory();
		adder = new Adder();
		mul = new Multiplier();
		stations = new ArrayList<Float>();
		lsqueue = new LSQueue();
		execList = new ArrayList<InstructionItem>();	
		wbList = new ArrayList<InstructionItem>();
		clock = 0;
		pc = 0;
		
		int i=0;
		for (i=0; i<Global.RegisterNum; i++){
			stations.add(0f);
		}
		for (i=0; i<Global.LSQNum; i++){
			stations.add(0f);
		}
		for (i=0; i< Global.RSNum; i++){
			stations.add(0f);
		}
		
		instList = new ArrayList<InstructionItem>();
		
		BufferedReader br;
		try {
			br = new BufferedReader (new FileReader("instruction.txt"));
			String str = "";
			while (null != (str = br.readLine())){
				InstructionItem ii = new InstructionItem(str);
				instList.add(ii);
//				System.out.println(ii.name);
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
		if (clock <= 4){
			issue();
		}
			
		execute();
		writeback();
		
		if (nextToExec != null){
			execList.add(nextToExec);
		}
	}
	
	public void issue(){
		
		// decoder
		InstructionItem inst = instList.get(pc);
		nextToExec = null;
		String s[] = inst.name.split("\\s+");
		String op = s[0];
		int src1 = 0, src2 = 0, des = 0;
		if (op.equals("ADDD") || op.equals("SUBD")){
			
			inst.op = op.equals("ADDD")? Global.ADDD: Global.SUBD;
			des = Global.getInt(s[1]);
			src1 = Global.getInt(s[2]);
			src2 = Global.getInt(s[3]);
			inst.src1 = src1;
			inst.src2 = src2;
			inst.des = des;
			inst.time = 2;
			
			int station = schedule(Global.A);
			inst.result = station;
			
			register.setStation(des, station);
			rs.setBusy(Global.getID(station));
			
			int src1Station = register.getStation(src1);
			int src2Station = register.getStation(src2);
			if (src1Station == -1) {
				rs.setData1(src1, register.read(src1));
			} else {
				rs.setStation1(src1, src1Station);
			}
			if (src2Station == -1) {
				rs.setData1(src2, register.read(src2));
			} else {
				rs.setStation1(src2, src2Station);
			}
			
			pc ++;
			
		} else if (op.equals("MULD") || op.equals("DIVD")){
			inst.op = op.equals("MULD")? Global.MULD: Global.DIVD;
			inst.time = op.equals("MULD")? 10: 40;
			des = Global.getInt(s[1]);
			src1 = Global.getInt(s[2]);
			src2 = Global.getInt(s[3]);
			inst.src1 = src1;
			inst.src2 = src2;
			inst.des = des;
			
			int station = schedule(Global.M);
			
			if (station == -1){
				return;
			}		
			inst.result = station;
			
			register.setStation(des, station);
			rs.setBusy(Global.getID(station));
			
			int src1Station = register.getStation(src1);
			int src2Station = register.getStation(src2);
			if (src1Station == -1) {
				rs.setData1(src1, register.read(src1));
			} else {
				rs.setStation1(src1, src1Station);
			}
			if (src2Station == -1) {
				rs.setData1(src2, register.read(src2));
			} else {
				rs.setStation1(src2, src2Station);
			}
			
			pc++;
				
		} else if (op.equals("LD")) {
			inst.op = Global.LD;
			des = Global.getInt(s[1]);
			src1 = Integer.parseInt(s[2]);
			inst.time = 2;
			inst.src1 = src1;
			inst.des = des;
			
			int station = schedule(Global.L);
			
			// load_addr 是已有mem操作的 addr
			for (int j=Global._S1; j<=Global._S3; j++){
				if ( lsqueue.isBusy(j)){
					if (src1 == lsqueue.getAddr(j)){
						return;
					}
				}
			}
			
			inst.result = station;
			
			register.setStation(des, station);
			
			lsqueue.setBusy(Global.getID(station));
			lsqueue.setAddr(Global.getID(station), des);
			pc ++;
			
		} else if (op.equals("ST")) {
			inst.op = Global.ST;
			src1 = Global.getInt(s[1]);
			des = Integer.parseInt(s[2]);		
			inst.time = 2;
			inst.src1 = src1;
			inst.des = des;
			
			
			int station = schedule(Global.S);
			
			for (int j=Global._L1; j<=Global._L3; j++){
				if ( lsqueue.isBusy(j)){
					if (src1 == lsqueue.getAddr(j)){
						return;
					}
				}
			}
			
			inst.result = station;
			
			// waiting src's station if any
			if (register.getStation(src1) != -1){
				lsqueue.setStation(des, register.getStation(src1));
			}
			lsqueue.setBusy(Global.getID(station));
			lsqueue.setAddr(Global.getID(station), des);
			pc ++;
			
			
		} else {
			System.out.println("Error Instruction!");
		}
		
		nextToExec = inst;
		
		inst.issue = clock;
		System.out.println(clock +": " +inst.name);
		
		
	}
	
	public void execute(){
		for (InstructionItem inst: execList){
			switch (inst.op) {
			case Global.ADDD:
				if (register.getStation(inst.src1) == -1 && register.getStation(inst.src2) == -1){
					inst.time--;
					if (inst.time == 0){
						float result = register.read(inst.src1) + register.read(inst.src2);
						wbList.add(inst);						
						
						inst.result = result;
						execList.remove(inst);
					}
				} 
				break;
			case Global.SUBD:
				if (register.getStation(inst.src1) == -1 && register.getStation(inst.src2) == -1){
					inst.time--;
					if (inst.time == 0){
						float result = register.read(inst.src1) - register.read(inst.src2);
						wbList.add(inst);						
						
						inst.result = result;
						execList.remove(inst);
					}
				} 
				break;
			case Global.MULD:
				if (register.getStation(inst.src1) == -1 && register.getStation(inst.src2) == -1){
					inst.time--;
					if (inst.time == 0){
						float result = register.read(inst.src1) * register.read(inst.src2);
						wbList.add(inst);						
						
						inst.result = result;
						execList.remove(inst);
					}
				} 
				break;
			case Global.DIVD:
				if (register.getStation(inst.src1) == -1 && register.getStation(inst.src2) == -1){
					inst.time--;
					if (inst.time == 0){
						float result = register.read(inst.src1) / register.read(inst.src2);
						wbList.add(inst);						
						
						inst.result = result;
						execList.remove(inst);
					}
				} 
				break;
			case Global.LD:
				if (register.getStation(inst.des) == -1){
					inst.time--;
					if (inst.time == 0){
						float result = mem.load(inst.src1);
						wbList.add(inst);						
						
						inst.result = result;
						execList.remove(inst);
					}
				} 
				break;
			case Global.ST:
				if (register.getStation(inst.src1) == -1){
					inst.time--;
					if (inst.time == 0){
						float result = register.read(inst.src1);
						wbList.add(inst);						
						
						inst.result = result;
						execList.remove(inst);
					}
				} 
				break;

			default:
				break;
			}
		}
	}
	
	public void writeback(){
		
	}
	
	public int schedule(int type){
		int i = 0;
		switch (type){
		case Global.L:
			for (i=Global._L1; i<=Global._L3; i++){
				if ( !lsqueue.isBusy(i)){
					return i+8;
				}
			}
			break;
			
		case Global.S:
			for (i=Global._S1; i<=Global._S3; i++){
				if ( !lsqueue.isBusy(i)){
					return i+8;
				}
			}
			break;
			
		case Global.A:
			for (i=Global._A1; i<=Global._A3; i++){
				if ( !rs.isBusy(i)){
					return i+14;
				}
			}
			break;
			
		case Global.M:
			for (i=Global._M1; i<=Global._M2; i++){
				if ( !rs.isBusy(i)){
					return i+14;
				}
			}
			break;
		}
		
		return -1;	
	}
	
	public static void main(String []args){
		Tomasulo t = new Tomasulo();
		while (clock < 100){
			t.step();
		}
	}
	

}
