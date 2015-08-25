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
import java.util.List;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.helper.opencv_core.AbstractCvScalar;
import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_features2d.KeyPoint;
import org.bytedeco.javacpp.opencv_features2d.SimpleBlobDetector;
import org.bytedeco.javacpp.opencv_legacy.CvBlob;
import org.bytedeco.javacpp.opencv_legacy.CvBlobDetector;
import org.bytedeco.javacpp.opencv_video.BackgroundSubtractorMOG;
import org.bytedeco.javacpp.opencv_video.BackgroundSubtractorMOG2;
import org.bytedeco.javacv.*;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class MotionDetector {
    public static void main(String[] args) throws Exception {
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber("F:/Video MAte de Luna/100CANON/MVI_2558.MOV");
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();

        grabber.start();
        opencv_video ov = new opencv_video();

        //IplImage frame = converter.convert(grabber.grab());
        IplImage image = null;
        IplImage prevImage = null;
        IplImage diff = null;
        IplImage output = null;
        File file = new File("E:/video.avi");
        FFmpegFrameGrabber g = new FFmpegFrameGrabber(file);
//        g.start();
        IplImage frame = null;
        try{
        	frame = converter.convert(grabber.grab());
        }catch(Exception e){
        	System.out.println(e);
        }

        CanvasFrame canvasFrame = new CanvasFrame("Some Title");
        canvasFrame.setCanvasSize(frame.width(), frame.height());
        CanvasFrame canvasFrame2 = new CanvasFrame("Some Title");
        canvasFrame2.setCanvasSize(frame.width(), frame.height());

        CvMemStorage storage = CvMemStorage.create();
        int i=0;
        BackgroundSubtractorMOG2 md2 = new BackgroundSubtractorMOG2();        
//        BackgroundSubtractorMOG md = new BackgroundSubtractorMOG();
        Mat mat = null;
        Mat outputMat = new Mat();

        CvScalar scalar = new CvScalar(0);
        CvScalar scalar2 = new CvScalar(2);
        
        KeyPoint keypoints = new KeyPoint();
        
        SimpleBlobDetector.Params params = new SimpleBlobDetector.Params();
        
        params.minThreshold(40);
        params.maxThreshold(60);
        params.thresholdStep(5);

        params.minArea(100); 
        params.minConvexity(0.3F);
        params.minInertiaRatio(0.01F);

        params.maxArea(8000);
        params.maxConvexity(10);

        params.filterByColor(false);
        params.filterByCircularity(false);

        
        SimpleBlobDetector simpleBlobDetector = new SimpleBlobDetector(params);
        simpleBlobDetector.create("SimpleBlob");
        while (canvasFrame.isVisible() && (frame = converter.convert(grabber.grab())) != null) {

            cvSmooth(frame, frame, CV_GAUSSIAN, 9, 0, 0, 0);
            
            if (image == null) {
                image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);                
                cvCvtColor(frame, image, CV_RGB2GRAY);
            } else {
            	prevImage = createImage(frame,prevImage);            	
            	cvCopy(image, prevImage, null);
            	image = createImage(frame,image);
                cvCvtColor(frame, image, CV_RGB2GRAY);
            }

            if (diff == null) {
                diff = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
            }

            if (prevImage != null) {
                // perform ABS difference
                cvAbsDiff(image, prevImage, diff);
            	
                // do some threshold for wipe away useless details
                cvThreshold(diff, diff, 64, 255, CV_THRESH_BINARY);

//                mat = converterToMat.convert(converter.convert(image));
//                md2.apply(mat, outputMat, 0.9);
                //CvBlobo bSeq a = new CvBlobSeq();
                //CvBlobDetector blob = new CvBlobDetector();
                //int i = blob.DetectNewBlob(frame, diff, a, blobSeq);
//                dilate(outputMat, outputMat, new Mat());
//                erode(outputMat, outputMat, new Mat());
                
//                canvasFrame.showImage(converter.convert(output));
               
                // recognize contours                
                CvBlob blob = new CvBlob();
                blob.;
                CvBlobDetector j;
                j.DetectNewBlob(image, diff, pNewBlobList, pOldBlobList)
            
                
//                CvSeq contour = new CvSeq(null);
//                CvMemStorage mem = CvMemStorage.create();
//
//                cvFindContours(diff, mem, contour, Loader.sizeof(CvContour.class), CV_RETR_EXTERNAL, CV_CHAIN_APPROX_NONE);
//                
//
////                IplImage output = cvCreateImage(cvGetSize(frame), frame.depth(), frame.nChannels());
//                
//                output = createImage(frame,output,frame.nChannels());                
//        		cvCopy(frame, output, null);
//        		cvSet(output, CV_RGB(255,255,255));
////                cvDrawContours(output, contour, scalar, scalar, 250);
//                CvRect boundbox;
//                CvSeq ptr = new CvSeq();
//
//                for (ptr = contour; ptr != null; ptr = ptr.h_next()) {
//                     boundbox = cvBoundingRect(ptr, 0);
//                     if(boundbox.height() > 10){
//                    	 cvRectangle( image, cvPoint( boundbox.x(), boundbox.y() ), cvPoint( boundbox.x() + boundbox.width(), boundbox.y() + boundbox.height()),cvScalar( 0, 255, 255, 0 ), 3, 0, 0 );
//                     }
//                }
                canvasFrame.showImage(converter.convert(diff));
                canvasFrame2.showImage(converter.convert(image));

            }
        }
        g.stop();
        canvasFrame.dispose();
    }
    
    
    
    
    protected static IplImage createImage(IplImage frame, IplImage image) {
    	if (image!=null) {
    		return image;
    	}
    	return image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, 1);
    }
    
    protected static IplImage createImage(IplImage frame, IplImage image, int nChannels) {
    	if (image != null) {
    		return image;
    	}
    	return image = IplImage.create(frame.width(), frame.height(), IPL_DEPTH_8U, nChannels);
    }
    
    private static void visualizeTrackedPoints(KeyPoint keyPoints,
			IplImage output) {
    	KeyPoint startPoint;
    	CvPoint cvPoint = new CvPoint();
		for (int i = 0; i < keyPoints.size(); i++) {
			startPoint = keyPoints.position(i);
			cvPoint.x((int)startPoint.pt().x());
			cvPoint.y((int)startPoint.pt().y());

			// Mark starting point with circle
			cvCircle(output, cvPoint, 3, AbstractCvScalar.WHITE, -1, CV_AA, 0);
		}

	}
    
}