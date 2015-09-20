package aleatorio;


public class carRandom extends ObjectRandom implements Runnable{
	private String type = "Car";
	@Override
	public void run() {
		int millis;
		while(true){
			millis = (int) (Math.random() * 6000);
			try {
				Thread.sleep(millis);
				System.out.println("Car: " + millis);
				
				this.httpPost(type);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}		
	}


}
