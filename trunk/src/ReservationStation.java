
// 保存站
public class ReservationStation {
	ReservationStationItem rs[];
	
	public ReservationStation(){
		rs = new ReservationStationItem[Global.RSNum];
		for (int i = 0; i < Global.RSNum; i++){
			rs[i] = new ReservationStationItem ();
		}
	}
	
	public String getOp(int i){
		return rs[i].op;
	}
	
	public void setOp(int id, String op){
		rs[id].op = op;
	}
	
	public void setResult(int id, float f){
		rs[id].result = f;
	}
	
	public float getResult(int id){
		return rs[id].result;
	}

	public boolean isBusy(int id){
		return (rs[id].ctrl == Global.BUSY)? true : false;
	}
	
	public void setStation1(int id, int station){
		rs[id].station1 = station;
	}
	
	public void setStation2(int id, int station){
		rs[id].station2 = station;
	}
	
	public void setData1(int id, float data){
		rs[id].data1 = data;
	}
	
	public void setData2(int id, float data){
		rs[id].data2 = data;
	}
	
	public void setBusy(int id){
		rs[id].ctrl = Global.BUSY;
	}
	
	public void setIdle(int id){
		rs[id].ctrl = Global.IDLE;
	}
	
	public int getStation1(int id){
		return rs[id].station1;
	}
	
	public int getStation2(int id){
		return rs[id].station2;
	}
	
	public float getData1(int id){
		return rs[id].data1;
	}
	
	public float getData2(int id){
		return rs[id].data2;
	}
}

// 保存站的每一项
class ReservationStationItem {
	int station1;
	float data1;
	int station2;
	float data2;
	int ctrl;
	float result;
	String op;
	
	public ReservationStationItem(){
		station1 = -1;
		data1 = 0f;
		station2 = -1;
		data2 = 0f;
		ctrl = Global.IDLE;
		result = 0f;
	}
}