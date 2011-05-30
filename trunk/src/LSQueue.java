
public class LSQueue {
	QueueItem queue[];
	
	public LSQueue(){
		queue = new QueueItem[Global.LSQNum];
		for (int i = 0; i < Global.LSQNum; i++){
			queue[i] = new QueueItem();
		}
	}
		
	public boolean isBusy(int id){
		return (queue[id].state == Global.BUSY)? true : false;
	}
	
	public int getAddr(int id){
		return queue[id].addr;
	}
	
	public void setAddr(int id, int addr){
		queue[id].addr = addr;
	}
	
	public void setBusy(int id){
		queue[id].state = Global.BUSY;
	}
	
	public void setIdle(int id){
		queue[id].state = Global.IDLE;
	}
	
	public void setStation(int id, int s){
		queue[id].station = s;
	}
	
	public int getStation(int id){
		return queue[id].station;
	}
	
}

class QueueItem{
	int state = Global.IDLE;
	int station = -1;
	int addr = 0;
	float result = 0f;
	
	public QueueItem(){
		
	}
}
