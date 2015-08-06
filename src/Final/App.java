package Final;

import org.bytedeco.javacpp.opencv_highgui.CvCapture;

import static org.bytedeco.javacpp.opencv_highgui.*;

public class App {

	public static void main(String[] args) {
		CvCapture capture = cvCreateFileCapture("F:/Video MAte de Luna/100CANON/MVI_2558.MOV");
//		CvCapture capture = cvCreateFileCapture("C:/Users/jorgebc88/workspace123/javacv-examples-master/OpenCV2_Cookbook/data/bike.avi");

		// Feature tracker
		FeatureTracker tracker = new FeatureTracker(500, 0.01, 10);

		// Create video processor instance
		VideoProcessor processor = new VideoProcessor(capture, tracker);

		processor.run();

		// Close the video file
		cvReleaseCapture(capture);

		System.out.println("Done.");
	}

}
