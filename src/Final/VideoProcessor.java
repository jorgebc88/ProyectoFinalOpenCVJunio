package Final;

import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JFrame;

import org.bytedeco.javacpp.opencv_highgui.CvCapture;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvScalar;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacpp.opencv_core.*;
import org.bytedeco.javacpp.opencv_imgproc.*;
import org.bytedeco.javacpp.opencv_video.*;
import org.bytedeco.javacpp.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;;

public class VideoProcessor implements Runnable{
	private CvCapture capture;
	private String displayInput = "Input";
	private String displayOutput = "Output";
	private FeatureTracker tracker;

	private Long delay = 0L;

	private boolean processFrames = true;

	private double frameRate = cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FPS);

	private CvSize frameSize = cvSize((int) (cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FRAME_WIDTH)),
			(int) (cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FRAME_HEIGHT)));

	int codec = (int) cvGetCaptureProperty(capture, opencv_highgui.CV_CAP_PROP_FOURCC);

	public VideoProcessor(CvCapture capture, FeatureTracker tracker) {
		// mandar excepcion si capture == null;
		this.capture = capture;
		this.tracker = tracker;
	}

	@Override
	public void run() {
		CanvasFrame canvas = new CanvasFrame("Proyecto", 1);
		canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		IplImage frame = null;
		IplImage output = null;
		CvMemStorage storage = CvMemStorage.create();
		Double frameRate = cvGetCaptureProperty(capture, CV_CAP_PROP_FPS);
		delay = Math.round(1000d / frameRate);
		while ((frame = readNextFrame()) != null) {
			cvClearMemStorage(storage);
			if (processFrames) {
				output = tracker.process(frame);
			} else {
				output = frame;
			}
			canvas.showImage(toBufferedImage(output));

			if (delay > 0) {
//				Thread.sleep(delay);
			}
			;
		}

	}

	private BufferedImage toBufferedImage(IplImage ipl) {
		OpenCVFrameConverter.ToIplImage openCVConverter = new OpenCVFrameConverter.ToIplImage();
		Java2DFrameConverter java2DConverter = new Java2DFrameConverter();
	    return java2DConverter.convert(openCVConverter.convert(ipl));
	}

	private IplImage readNextFrame() {
		if (cvGrabFrame(capture) != 0) {
			return cvRetrieveFrame(capture);
		} else {
			return null;
		}
	}
}
