
public class InstructionItem{
	int pc = 0;
	String name = "";
	int issue = 0;
	int execComp = 0;
	int writeback = 0;
	int time = 0;
	int op = 0;
	int des = 0;
	int src1 = 0;
	int src2 = 0;
	int station = 0;
	float result = 0f;
	
	public InstructionItem(String s){
		name = s;
	}
}
