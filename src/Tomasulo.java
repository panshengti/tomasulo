import java.util.ArrayList;


public class Tomasulo {
	
	FloatRegister register;
	ReservationStation rs;
	Memory mem;
	LSQueue lsqueue;
	ArrayList<Float> stations;
	ArrayList<InstructionItem> instList;
	
	InstructionItem nextToExec = null;
	ArrayList<InstructionItem> execList;
	ArrayList<InstructionItem> wbList;
	ArrayList<InstructionItem> nextWbList;
	
	int clock;
	int pc;
	
	public Tomasulo(){
		register = new FloatRegister();
		rs = new ReservationStation();
		mem = new Memory();
		stations = new ArrayList<Float>();
		lsqueue = new LSQueue();
		execList = new ArrayList<InstructionItem>();	
		wbList = new ArrayList<InstructionItem>();
		nextWbList = new ArrayList<InstructionItem>();
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
		
//		BufferedReader br;
//		try {
//			br = new BufferedReader (new FileReader("instruction.txt"));
//			String str = "";
//			while (null != (str = br.readLine())){
//				InstructionItem ii = new InstructionItem(str);
//				instList.add(ii);
////				//System.out.println(ii.name);
//			}
//		
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void printAll(){
		//System.out.println("-----------clock = "+clock+"--------------");
		//System.out.println("F1: "+ register.read(1));
		//System.out.println("F2: "+ register.read(2));
		//System.out.println("F3: "+ register.read(3));
		for (int i =0 ; i < instList.size();i ++){
			//System.out.println(instList.get(i).name);
			//System.out.println("issue :"+instList.get(i).issue);
			//System.out.println("exec :"+instList.get(i).execComp);
			//System.out.println("wb :"+instList.get(i).writeback);
		}
		for (int i=0; i<rs.rs.length; i++){
			//System.out.println("rs :"+i);
			//System.out.println("data1 : "+rs.getData1(i));
			//System.out.println("station1 : "+rs.getStation1(i));
			//System.out.println("data2 : "+rs.getData2(i));
			//System.out.println("station2 : "+rs.getStation2(i));
		}
		for (int i=0; i<register.register.length; i++){
			//System.out.println("REG :"+i);
			//System.out.println("REG: station : "+register.getStation(i));
		}
	}
	
	public void runAll(){
		//int cnt = 0;
		boolean end = false;
		while (!end){
			end = true;
			step();
			for (int i = 0; i < instList.size(); i++){
				if (instList.get(i).writeback == 0){
					end = false;
					break;
					
				}
					
			}
		}
		//t.printAll();
	}
	
	public void step(){
			clock ++;
			issue();
			//System.out.println("execlist.size():"+execList.size());
			execute();
			writeback();
		if (nextToExec != null){
			execList.add(nextToExec);
		} 
		if (nextWbList.size() != 0){
			for (InstructionItem inst: nextWbList){
				wbList.add(inst);
			}
			nextWbList.clear();
		}
	}
	
	public void issue(){
		
		if (pc >= instList.size()) {
			nextToExec = null;
			return;
		}
		// decoder
		////System.out.println("pc = "+pc);
		InstructionItem inst = instList.get(pc);
		nextToExec = null;
		String s[] = inst.name.split("\\s+");
		String op =s[0];
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
			inst.station = station;
			
			register.setStation(inst.des, inst.station);
			rs.setBusy(Global.getID(inst.station));
			rs.setOp(Global.getID(station), op);
			
			int src1Station = register.getStation(inst.src1);
			int src2Station = register.getStation(inst.src2);
			if (src1Station == -1 || src1Station == station) {
				register.setStation(inst.src1, -1);
				rs.setData1(Global.getID(station), register.read(inst.src1));
			} else {
				rs.setStation1(Global.getID(station), src1Station);
			}
			if (src2Station == -1  || src2Station == station) {
				register.setStation(inst.src2, -1);
				rs.setData2(Global.getID(station), register.read(inst.src2));
			} else {
				rs.setStation2(Global.getID(station), src2Station);
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
			inst.station = station;
			
			register.setStation(inst.des, inst.station);
			rs.setBusy(Global.getID(inst.station));
			rs.setOp(Global.getID(station), op);
			
			int src1Station = register.getStation(inst.src1);
			int src2Station = register.getStation(inst.src2);
			if (src1Station == -1 || src1Station == station) {
				register.setStation(inst.src1, -1);
				rs.setData1(Global.getID(station), register.read(inst.src1));
			} else {
				rs.setStation1(Global.getID(station), src1Station);
			}
			if (src2Station == -1  || src2Station == station) {
				register.setStation(inst.src2, -1);
				rs.setData2(Global.getID(station), register.read(inst.src2));
			} else {
				rs.setStation2(Global.getID(station), src2Station);
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
			
			inst.station = station;
			
			register.setStation(inst.des, station);
			
			lsqueue.setBusy(Global.getID(inst.station));
			lsqueue.setAddr(Global.getID(inst.station), inst.des);
			
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
			
			inst.station = station;
			
			// waiting src's station if any
			if (register.getStation(inst.src1) != -1){
				lsqueue.setStation(Global.getID(inst.station), register.getStation(inst.src1));
			}
			lsqueue.setBusy(Global.getID(inst.station));
			lsqueue.setAddr(Global.getID(inst.station), inst.des);
			
			pc ++;
			
			
		} else {
			////System.out.println("Error Instruction!");
		}
		
		nextToExec = inst;
		
		inst.issue = clock;
		
		//printAll();
		
	}
	
	public void execute(){ 
		if (execList.size() == 0) return;
		ArrayList<Integer> delList = new ArrayList<Integer>();
		for (int i = 0; i < execList.size(); i++){
			InstructionItem inst = execList.get(i);
			switch (inst.op) {
			case Global.ADDD:
				
				if (inst.time == 2){
					if ((rs.getStation1(Global.getID(inst.station)) == -1 || rs.getStation1(Global.getID(inst.station)) == inst.station) 
							&& (rs.getStation2(Global.getID(inst.station)) == -1 || rs.getStation2(Global.getID(inst.station)) == inst.station)){
						inst.time--;
					} 
					break;
				}
				inst.time --;
				if (inst.time == 0){
					float result = rs.getData1(Global.getID(inst.station)) + rs.getData2(Global.getID(inst.station));
					nextWbList.add(inst);						
						
					inst.result = result;
					inst.execComp = clock;
					//execList.remove(inst);
					delList.add(i);
						
				}
				
				break;
			case Global.SUBD:
				if (inst.time == 2){
					if ((rs.getStation1(Global.getID(inst.station)) == -1 || rs.getStation1(Global.getID(inst.station)) == inst.station) 
							&& (rs.getStation2(Global.getID(inst.station)) == -1 || rs.getStation2(Global.getID(inst.station)) == inst.station)){
						inst.time--;
					} 
					break;
				}
				inst.time --;
				if (inst.time == 0){
					float result = rs.getData1(Global.getID(inst.station)) - rs.getData2(Global.getID(inst.station));
					nextWbList.add(inst);						
						
					inst.result = result;
					inst.execComp = clock;
					//execList.remove(inst);
					delList.add(i);
						
				}
				break;
			case Global.MULD:
				if (inst.time == 10){
					if ((rs.getStation1(Global.getID(inst.station)) == -1 || rs.getStation1(Global.getID(inst.station)) == inst.station) 
							&& (rs.getStation2(Global.getID(inst.station)) == -1 || rs.getStation2(Global.getID(inst.station)) == inst.station)){
						inst.time--;
					} 
					break;
				}
				inst.time --;
				if (inst.time == 0){
					float result = rs.getData1(Global.getID(inst.station)) * rs.getData2(Global.getID(inst.station));
					nextWbList.add(inst);						
						
					inst.result = result;
					inst.execComp = clock;
					//execList.remove(inst);
					delList.add(i);
						
				}
				break;
			case Global.DIVD:
				if (inst.time == 40){
					if ((rs.getStation1(Global.getID(inst.station)) == -1 || rs.getStation1(Global.getID(inst.station)) == inst.station) 
							&& (rs.getStation2(Global.getID(inst.station)) == -1 || rs.getStation2(Global.getID(inst.station)) == inst.station)){
						inst.time--;
					} 
					break;
				}
				inst.time --;
				if (inst.time == 0){
					float result = rs.getData1(Global.getID(inst.station)) / rs.getData2(Global.getID(inst.station));
					nextWbList.add(inst);						
						
					inst.result = result;
					inst.execComp = clock;
					//execList.remove(inst);
					delList.add(i);
						
				}
				break;
			case Global.LD:
				////System.out.println("-----------------time: "+inst.time);
				if (inst.time == 2){
					inst.time--;						
					
					break;
				}
				inst.time --;
				if (inst.time == 0){
					float result = mem.load(inst.src1);
					nextWbList.add(inst);						
				
					inst.result = result;
					inst.execComp = clock;
					delList.add(i);
				}
				
				break;
			case Global.ST:
				if (inst.time == 2){
					if (lsqueue.getStation(Global.getID(inst.station)) == -1){
						inst.time--;
					}
					
					break;
				}
				inst.time --;
				if (inst.time == 0){
					float result = register.read(inst.src1);
					nextWbList.add(inst);						
					
					inst.result = result;
					inst.execComp = clock;
					delList.add(i);
				}
				
				break;

			default:
				break;
			}
		}
		
		for (int i = delList.size()-1; i >= 0; i--){
			execList.remove((int)delList.get(i));
		}
		
		//printAll();
		
	}
	
	public void writeback(){
		ArrayList<Integer> delList = new ArrayList<Integer>();
		for (int j = 0; j < wbList.size(); j++){
			InstructionItem inst = wbList.get(j);
			switch(inst.op){
			case Global.ADDD:
			case Global.SUBD:
			case Global.MULD:
			case Global.DIVD:
				rs.setIdle(Global.getID(inst.station));
				rs.setData1(Global.getID(inst.station), 0);
				rs.setData2(Global.getID(inst.station), 0);
				if (register.getStation(inst.des) == inst.station){
					register.setStation(inst.des, -1);
					register.write(inst.des, inst.result);
				}
				
				for (int i = 0; i < Global.RSNum; i++){
					if (rs.getStation1(i) == inst.station){
						rs.setStation1(i, -1);
						rs.setData1(i, inst.result);
					}
					if (rs.getStation2(i) == inst.station){
						rs.setStation2(i, -1);
						rs.setData2(i, inst.result);
					}
						
				}
				for (int i = 0; i < Global.LSQNum; i++){
					if (lsqueue.getStation(i) == inst.station){
						lsqueue.setStation(i, -1);
					}	
				}
				inst.writeback = clock;
				break;
				
			case Global.LD:
				lsqueue.setIdle(Global.getID(inst.station));
				lsqueue.setAddr(Global.getID(inst.station), -1);
				if (register.getStation(inst.des) == inst.station){
					register.setStation(inst.des, -1);
					register.write(inst.des, inst.result);
				}
				for (int i = 0; i < Global.RSNum; i++){
					if (rs.getStation1(i) == inst.station){
						rs.setStation1(i, -1);
						rs.setData1(i, inst.result);
					}
					if (rs.getStation2(i) == inst.station){
						rs.setStation2(i, -1);
						rs.setData2(i, inst.result);
					}	
				}
				for (int i = 0; i < Global.LSQNum; i++){
					if (lsqueue.getStation(i) == inst.station){
						lsqueue.setStation(i, -1);
					}	
				}
				inst.writeback = clock;
				
				break;
			case Global.ST:
				lsqueue.setAddr(Global.getID(inst.station), -1);
				mem.store(inst.des, inst.result);
				lsqueue.setIdle(Global.getID(inst.station));
				inst.writeback = clock;
				break;
			}
			//wbList.remove(inst);
			delList.add(j);
		}
		////System.out.println("wblist: "+wbList.size());
		////System.out.println("delist: "+delList.size());
		for (int i = delList.size()-1; i >= 0; i--){
			wbList.remove((int)delList.get(i));
		}
		
		printAll();
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
}
