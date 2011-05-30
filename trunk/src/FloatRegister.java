
// 浮点寄存器
public class FloatRegister {
	RegisterItem register[];
	
	public FloatRegister(){
		register = new RegisterItem[Global.RegisterNum];
		for (int i = 0 ; i < Global.RegisterNum; i++){
			register[i] = new RegisterItem();
			register[i].write((float)i);
		}
		
	}
	
	
	public float read(int id){
		return register[id].read();
	}
	
	public void write(int id, float data){
		register[id].write(data);
	}
	
	public boolean isBusy(int id){
		return register[id].isBusy();
	}
	
	public void setBusy(int id){
		register[id].setBusy();
	}
	
	public void setIdle(int id){
		register[id].setIdle();
	}
	
	public void setStation(int id, int station){
		register[id].setStation(station);
	}
	
	public int getStation(int id){
		return register[id].getStation();
	}
}

class RegisterItem {
	int state;
	int station;
	float data;
	
	public RegisterItem(){
		state = Global.IDLE;
		station = -1;
		data = 0f;
	}
	
	public boolean isBusy(){
		return (state == Global.BUSY)? true : false;
	}
	
	public void setBusy(){
		state = Global.BUSY;
	}
	
	public void setIdle(){
		state = Global.IDLE;
	}
	
	public float read(){
		return data;
	}
	
	public void write(float data){
		this.data = data;
	}
	
	public void setStation(int s){
		station = s;
	}
	
	public int getStation(){
		return station;
	}
}