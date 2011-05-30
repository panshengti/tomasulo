
// 保存站
public class ReservationStation {
	ReservationStationItem rs[];
	
	public ReservationStation(){
		rs = new ReservationStationItem[Global.RSNum];
	}
	
	
	
}

// 保存站的每一项
class ReservationStationItem {
	int station1;
	float data1;
	int station2;
	float data2;
	int ctrl;
	
	public ReservationStationItem(){
		station1 = 0;
		data1 = 0f;
		station2 = 0;
		data2 = 0f;
		ctrl = Global.WAIT;
	}
	
	
	
}