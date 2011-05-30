
// 浮点寄存器
public class FloatRegister {
	RegisterItem register[];
	
	public FloatRegister(){
		register = new RegisterItem[Global.RegisterNum];
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
}

class RegisterItem {
	int state;
	int station;
	float data;
	
	public RegisterItem(){
		state = Global.IDLE;
		station = 0;
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
}