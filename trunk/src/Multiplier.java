
// 加法器
public class Multiplier {
	/*
	 * MULD : 10 cycles
	 * DIVD : 40 cycles
	 */
	int flag;
	int opType;
	float args1, args2;
	int state, finish;
	float result;
	public Multiplier(){
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
		}else if (flag == op){
			System.out.println("here!");
			if (opType == Global.MULD){
				result = args1 * args2;
			}else if (opType == Global.DIVD){
				result = args1 / args2;
			}
			flag = 0;
			finish = Global.FINISHED;
			state = Global.IDLE;
		}else{
			flag ++;
		}
		
	}
	
	/*for test*/
//	public static void main(String []args){
//		Multiplier a = new Multiplier();
//		float res = 0f;
//		int cnt = 0;
//		a.go(Global.MULD, 4, 5);
//		while (cnt ++ < 50){
//			if (a.isBusy() == Global.BUSY) {
//				/*still this instruction*/
//				a.go(Global.MULD, 4, 5);
//			}/*do nothing*/
//			else{
//				if (a.finish == Global.FINISHED){
//					System.out.println("get a result!");
//					res = a.getRes();
//					System.out.println(res);
//					/*next instruction*/
//					a.go(Global.DIVD, 1, 2);
//				}else{
//					/*next instruction*/
//					a.go(Global.DIVD, 1, 2);
//				}
//			}
//
//		}
//	}
}