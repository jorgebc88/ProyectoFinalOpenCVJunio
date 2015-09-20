package aleatorio;

public class busRandom extends ObjectRandom implements Runnable{
	private String type = "Bus";
	@Override
	public void run() {
		int millis;
		while(true){
			millis = (int) (Math.random() * 6000);
			try {
				Thread.sleep(millis);
				System.out.println("Bus: " + millis);
				
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
