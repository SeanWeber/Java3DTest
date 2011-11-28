import java.awt.Color;

import javax.media.j3d.*;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.geometry.Sphere;


public class aSphere extends Shape3D {
	private RotationInterpolator rotator;
	private BranchGroup branchGroup;
	private String id;
	
	private static MouseTranslate myMouseTranslate;
	private TransformGroup tg;
	private Transform3D changeSize;
	private Transform3D resize;
	
	private double height = 10; 
	private double width = 10; 
	private double depth = 10;
	
	public double getHeight() {
		return height;
	}


	public void setHeight(double height) {
		this.height = height;
	}


	public double getWidth() {
		return width;
	}


	public void setWidth(double width) {
		this.width = width;
	}


	public double getDepth() {
		return depth;
	}


	public void setDepth(double depth) {
		this.depth = depth;
	}


	public Transform3D getResize() {
		return resize;
	}


	public void setResize(Transform3D resize) {
		this.resize = resize;
	}


	public Transform3D getChangeSize() {
		return changeSize;
	}


	public void setChangeSize(Transform3D changeSize) {
		this.changeSize = changeSize;
	}
	
	private float tx = 0.0f;
	private float ty = 0.0f;


	Color3f red = new Color3f (Color.RED);
	Color3f pink = new Color3f (Color.PINK);
	Color3f orange = new Color3f (Color.ORANGE);
	Color3f yellow = new Color3f (Color.YELLOW);
	Color3f green = new Color3f (Color.GREEN);
	Color3f blue = new Color3f (Color.BLUE);
	Color3f cyan = new Color3f (Color.CYAN);
	Color3f purple = new Color3f (Color.MAGENTA);
	Color3f black = new Color3f (Color.BLACK);
	Color3f white = new Color3f (Color.WHITE);
	Color3f gray = new Color3f (Color.GRAY);
	Color3f lightgray = new Color3f (Color.LIGHT_GRAY);
	
	//BOTTOM
	Point3f b_frontL = new Point3f(-1.0f,-1.0f, 1.0f);  //front left
	Point3f b_frontR = new Point3f( 1.0f,-1.0f, 1.0f);  //front right
	Point3f b_backR = new Point3f( 1.0f,-1.0f,-1.0f);  //back right
	Point3f b_backL = new Point3f(-1.0f,-1.0f,-1.0f);  //back left
		
	//TOP
	Point3f t_frontL = new Point3f(-1.0f, 1.0f, 1.0f);  //front left
	Point3f t_frontR = new Point3f( 1.0f, 1.0f, 1.0f);  //front right
	Point3f t_backR = new Point3f( 1.0f, 1.0f,-1.0f);  //back right
	Point3f t_backL = new Point3f(-1.0f, 1.0f,-1.0f);  //back left
		

    
    public aSphere() {
   		
    	Sphere sphere = new Sphere(1, 1, 35);
		
    	Appearance ap = new Appearance();
    	ColoringAttributes ca = new ColoringAttributes(blue, ColoringAttributes.NICEST); 
    	Material mat = new Material(black, black, blue, white, 30f);
    	ap.setColoringAttributes(ca);
    	ap.setMaterial(mat);
    	
    	this.setAppearance(ap);

		//set userData (id)
		int sphereCount = SwingTest.getSphereCount();
		this.setUserData( "Sphere".concat(Integer.toString(sphereCount)) );
		
		System.out.println("Created: " + getUserData());
		
		sphereCount++;
	 	SwingTest.setSphereCount(sphereCount);
	 	
	 	this.setGeometry(sphere.getShape().getGeometry());
	 	
	 	Transform3D defaultSize = new Transform3D();
	 	defaultSize.setScale(new Vector3d(1.0, 1.0, 1.0));
	 	setResize(defaultSize);
    }
    
    
    
    TransformGroup createRotator() {
    	Transform3D yAxis = new Transform3D();

		 /* axes of rotation */
	     yAxis.rotZ(Math.PI / 2.0);  	//X AXIS
	     //yAxis.rotY( Math.PI / 2.0 ); //Y AXIS
	     //yAxis.rotX(Math.PI / 2.0);   //Z AXIS
		    
		 TransformGroup spin = new TransformGroup(yAxis);
		 spin.setUserData("TG: SPIN");
		 
		 
		 spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		 

		 spin.addChild(this); //add rectPrism shape to the spin TG

		    
		 Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0,  4000, 0, 0, 0, 0, 0);
	     rotator = new RotationInterpolator(rotationAlpha, spin, yAxis, 0.0f, (float) Math.PI* GUI_3D.rotateSpeed );

		    
		 BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
		 rotator.setSchedulingBounds(bounds);
		 spin.addChild(rotator);  //add interpolator rotator to the spin TG
		 
		 
		 TransformGroup tg = new TransformGroup() ;
		 tg.setUserData("TG: TG".concat(Integer.toString(SwingTest.getRectPrismCount())));
		 tg.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE ) ;
		 setTg(tg);
		 
		 
		 tg.addChild(spin) ;
		 
		 TransformGroup mouseTG = new TransformGroup();	
		 mouseTG.setUserData("TG: MOUSETG");
		 myMouseTranslate = new MouseTranslate();
		 myMouseTranslate.setTransformGroup(tg);
		 myMouseTranslate.setSchedulingBounds(bounds);
		 mouseTG.addChild(myMouseTranslate);
		              
		        
		 tg.addChild(mouseTG);

		 return tg;
    }
	
	public TransformGroup getTg() {
		return tg;
	}


	public void setTg(TransformGroup tg) {
		this.tg = tg;
	}
	
	
	public float getTx() {
		return tx;
	}


	public void setTx(float tx) {
		this.tx = tx;
	}


	public float getTy() {
		return ty;
	}


	public void setTy(float ty) {
		this.ty = ty;
	}
}