
/* 
 * I developed some code for recognize motion detections with JavaCV.
 * Actually, it works with an array of Rect, performing, every cicle, an
 * intersection test with area of difference with the rect of interests
 * (this array is callad "sa", stands for SizedArea). I hope could this
 * helps someone.
 * 
 * Feel free to ask about any question regarding the code above, cheers!
 *
 * Angelo Marchesin <marchesin.angelo@gmail.com>
 */

import java.io.File;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.opencv_video.BackgroundSubtractorMOG;
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class MOGMotionDetector {
    public static void main(String[] args) throws Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("E:/video.avi");
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        OpenCVFrameConverter.ToMat toMatconverter = new OpenCVFrameConverter.ToMat();
        
        grabber.start();
        opencv_video ov = new opencv_video();

        //IplImage frame = converter.convert(grabber.grab());
        IplImage image = null;
        IplImage prevImage = null;
        IplImage diff = null;
        File file = new File("E:/video.avi");
        FFmpegFrameGrabber g = new FFmpegFrameGrabber(file);
        g.start();
        Mat frame = null;
        try{
        	frame = toMatconverter.convert(grabber.grab());
        }catch(Exception e){
        	System.out.println(e);
        }

        CanvasFrame canvasFrame = new CanvasFrame("Some Title");
        canvasFrame.setCanvasSize(frame.arrayWidth(), frame.arrayDepth());
        

        CvMemStorage storage = CvMemStorage.create();
        BackgroundSubtractorMOG mog = new BackgroundSubtractorMOG();
       
        Mat fgmask = new Mat();
        
        int i=0;
        while (canvasFrame.isVisible() && (frame = toMatconverter.convert(grabber.grab())) != null) {
        	System.out.println(i);
        	i++;
            //cvClearMemStorage(storage);

//            cvSmooth(frame, frame, CV_GAUSSIAN, 9, 9, 2, 2);
            
            mog.apply(frame, fgmask);
            cvThreshold(fgmask, fgmask, 64, 255, CV_THRESH_BINARY);
            
        }
            g.stop();
        //grabber.stop();
        canvasFrame.dispose();
    }
}