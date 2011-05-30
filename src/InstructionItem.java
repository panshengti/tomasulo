
public class InstructionItem{
	int pc = 0;
	String name = "";
	int issue = 0;
	int execComp = 0;
	int writeback = 0;
	int time = 0;
	
	public InstructionItem(String s){
		name = s;
	}
}
