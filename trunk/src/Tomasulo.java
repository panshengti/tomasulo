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
	ArrayList<Float> station;
	ArrayList<InstructionItem> instList;
	
	public Tomasulo(){
		register = new FloatRegister();
		rs = new ReservationStation();
		mem = new Memory();
		adder = new Adder();
		mul = new Multiplier();
		station = new ArrayList<Float>();
		int i=0;
		for (i=0; i<Global.RegisterNum; i++){
			station.add(register.read(i));
		}
		for ( ; i<Global.RegisterNum + Global.RSNum; i++){
			station.add(rs.getResult(i));
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
	
	public void issue(){
		
	}
	
	public void execute(){
		
	}
	
	public void writeback(){
		
	}
	
	

}
