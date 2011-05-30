import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


// 指令分析部件
public class InstructionAnalysisParts {
	BufferedReader br;
	public void readFile(){
	
		try {
			br = new BufferedReader (new FileReader("instruction.txt"));
			String str = "";
			while (null != (str = br.readLine())){
				Global.instructionList.add(str);
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
