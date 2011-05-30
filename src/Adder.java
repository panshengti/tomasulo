
// 加法器
public class Adder {
	/*
	 * ADDU : 2 cycles
	 * SUBD : 2 cycles
	 */
	int flag;
	int opType;
	float args1, args2;
	int state, finish;
	float result;
	public Adder(){
		opType = -1;
		args1 = 0;
		args2 = 0;
		state = Global.IDLE;
		result = 0;
		finish = Global.UNFINISHED;
	}
	 
	public int isBusy(){
		return state;
	}
	
	public int isFinished(){
		return finish;
	}
	
	public float getRes(){
		return result;
	}
	
	public void go(int op, float i, float j){
		System.out.println("flag == "+flag);
		if (flag == 0){
			flag = 1;
			opType = op;
			args1 = i;
			args2 = j;
			state = Global.BUSY;
			finish = Global.UNFINISHED;
			
		}else if (flag == 1){
			flag = 2;
		}else if (flag == 2){
			if (opType == Global.ADDD){
				result = args1 + args2;
			}else if (opType == Global.SUBD){
				result = args1 - args2;
			}
			flag = 0;
			finish = Global.FINISHED;
			state = Global.IDLE;
		}
		
	}
	
	/*for test*/
	public static void main(String []args){
		Adder a = new Adder();
		float res = 0f;
		int cnt = 0;
		a.go(Global.SUBD, 4, 5);
		while (cnt ++ < 6){
			if (a.isBusy() == Global.BUSY) {
				/*still this instruction*/
				a.go(Global.SUBD, 4, 5);
			}/*do nothing*/
			else{
				if (a.finish == Global.FINISHED){
					System.out.println("get a result!");
					res = a.getRes();
					System.out.println(res);
					/*next instruction*/
					a.go(Global.ADDD, 1, 2);
				}else{
					/*next instruction*/
					a.go(Global.ADDD, 1, 2);
				}
			}

		}
	}
}