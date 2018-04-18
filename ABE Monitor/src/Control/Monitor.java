package Control;

public class Monitor implements Runnable{

	private static int ScanningTime = 40;
	

	public Monitor() {
	
	}

	private boolean ScannerRun()
	{
		return false;
	}
	

	@Override
	public void run() {
		while(true)
		{
			try{
			System.out.println("Running scanner...");
			ScannerRun();
			System.out.println("Scanner finished...");
			}catch(Exception e){System.out.println("ScannerRun error");System.out.println(e);}
		try {
			Thread.sleep(1000*60*ScanningTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

}
