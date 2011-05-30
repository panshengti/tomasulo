
// 存储器
public class Memory {
	
	float mem[];
	int state;
	
	public Memory(){
		mem = new float[4096];
		for (int i=0; i<4096; i++){
			mem[i] = i;
		}
		state = Global.IDLE;
	}
	
	public float load(int addr){
		return mem[addr];
	}
	
	public void store(int addr, float data){
		mem[addr] = data;
	}
	
	public void setBusy(){
		state = Global.BUSY;
	}
	
	public void setIdle(){
		state = Global.IDLE;
	}
	
	public boolean isBusy(){
		return (state == Global.BUSY)? true : false;
	}
}

