import static org.bytedeco.javacpp.helper.opencv_imgproc.cvFindContours;
import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvAbsDiff;
import static org.bytedeco.javacpp.opencv_core.cvClearMemStorage;
import static org.bytedeco.javacpp.opencv_imgproc.CV_CHAIN_APPROX_SIMPLE;
import static org.bytedeco.javacpp.opencv_imgproc.CV_GAUSSIAN;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RETR_LIST;
import static org.bytedeco.javacpp.opencv_imgproc.CV_RGB2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvMinAreaRect2;
import static org.bytedeco.javacpp.opencv_imgproc.cvSmooth;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import java.io.File;
import java.math.MathContext;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_video;
import org.bytedeco.javacpp.opencv_core.CvBox2D;
import org.bytedeco.javacpp.opencv_core.CvContour;
import org.bytedeco.javacpp.opencv_core.CvMemStorage;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import org.bytedeco.javacpp.opencv_core.CvSize2D32f;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_legacy.CvBlobSeq;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

public class trackerIndependenceDay {
	public static void main(String[] args) throws Exception {
		trackerIndependenceDay tracker = new trackerIndependenceDay();
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("C:/video.avi");
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        grabber.start();
        opencv_video ov = new opencv_video();
        Mat mat;
        IplImage frame = converter.convert(grabber.grab());
        IplImage image = null;
        Mat prevPoints = null;
        Mat nextPoints = null;
        FFmpegFrameRecorder f = new FFmpegFrameRecorder(new File(""), 23);
        FFmpegFrameGrabber g = new FFmpegFrameGrabber("");

        CanvasFrame canvasFrame = new CanvasFrame("Some Title");
        canvasFrame.setCanvasSize(frame.width(), frame.height());

        CvMemStorage storage = CvMemStorage.create();
        CvBlobSeq blobSeq = new CvBlobSeq();
        while (canvasFrame.isVisible() && 
        		(frame = converter.convert(grabber.grab())) != null) {
            cvClearMemStorage(storage);
            
	        image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
	        cvCvtColor(frame, image, CV_RGB2GRAY);
            
	        if(tracker.addNewPoints()){
	        	
	        }
            
            canvasFrame.showImage(converter.convert(image));
        }
        grabber.stop();
        canvasFrame.dispose();
    }

	private boolean addNePoints = true;
	
	private boolean addNewPoints(){
		return this.addNePoints ;
	}
}
