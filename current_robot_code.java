
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "RobotCode")
public class RobotCode extends LinearOpMode {
    
    private static void logMotor (DcMotor motor, double motorPower) {

        // Doesn't move?
        // If it slows down it just stops
        // Acceleration?
        if (motorPower > 0) {
            motor.setPower(motorPower*motorPower); 
            
        }
            
        else {
            motor.setPower(-1*motorPower*motorPower);
            
        }
            

    }
    

    // Declares Motors & Servos
    private DcMotor leftFront, leftRear;
    private DcMotor armElevationMotor, armRotationMotor; //, clawExtension;
    private DcMotor rightFront, rightRear;
    private Servo clawOpenerServo;

    private boolean lock = false;
    private double rampSpeed = 0.2;
    private double speed;
    // Maybe 1 open 0 closed?
    private int clawState = 1;
    
    @Override
    public void runOpMode() throws InterruptedException {

        // Declares Variables for Motors & Sevos
    
        leftFront = hardwareMap.get(DcMotor.class, "LeftFront");
        rightFront = hardwareMap.get(DcMotor.class, "RightFront");
        rightRear = hardwareMap.get(DcMotor.class, "RightRear");
        leftRear = hardwareMap.get(DcMotor.class, "LeftRear");
        armElevationMotor = hardwareMap.get(DcMotor.class, "ArmElevation");
        armRotationMotor = hardwareMap.get(DcMotor.class, "armRotation");
        clawOpenerServo = hardwareMap.get(Servo.class, "clawOpener");

       
        //Servo_R = hardwareMap.get(Servo.class, "Servo_R"); 
        //Servo_C = hardwareMap.get(Servo.class, "Servo_C");
    
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armElevationMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armRotationMotor.setZeroPowerBehavior (DcMotor.ZeroPowerBehavior.BRAKE);
        //clawOpenerServo.setZeroPowerBehavior (Servo.ZeroPowerBehavior.BRAKE);
        
        // The encoder is probably the black motor of the motor at the back
        // that records how much it spins and stuff
        //armElevation.setMode(DcMotor.RunMode. STOP_AND_RESET_ENCODER);


        waitForStart();

        // This is the main part of the program
        while (opModeIsActive()) {

            // Deriving the power from the left and right paddles on the controller
            double armRotationControl = gamepad2.left_stick_x; //maybe reverse
            double armElevationControl = gamepad2.right_stick_y; // maybe reverse
            double x = -gamepad1.left_stick_x;
            double y = gamepad1.left_stick_y;
            double s = -gamepad1.right_stick_x;
            double t = gamepad1.right_stick_y;
            boolean clawOpenerControl = gamepad2.left_bumper;
            double clawOpenerPower;
            
            if (gamepad2.left_bumper) {
                
                clawOpenerPower = 0.5;
            }
            
            else {
                clawOpenerPower = 0;
            }
            
            clawOpenerServo.setPosition(clawOpenerPower);
            
            if (x < 0) {
                x = x * x * -1;
            }
            else {
                x = x * x;
            }
            
            if (y < 0) {
                y = y * y * -1;
            }
            else {
                y = y * y;
            }
            
            if (t < 0) {
                t = t * t * -1;
            }
            else {
                t = t * t;
            }
            
            double leftRearPower =  x - s + t;
            double rightRearPower = -y + s - t;
            double leftFrontPower = -y - s + t;
            double rightFrontPower = x + s - t;
            double armElevationPower = armElevationControl;
            double armRotationPower = armRotationControl;
            
            
            //armRotation.setPower(0.5);
            // Sets three different speed options
            if (gamepad1.left_bumper) {
                

                speed = 0.5;
            }
            
            else if (gamepad1.right_bumper) {

                speed = 1.5;
            }
                
            else {
                
                speed = 1.0;
            }
            
            // Speed multiplier
            leftRearPower *= speed;
            rightRearPower *= speed;
            leftFrontPower *= speed;
            rightFrontPower *= speed;
            
            // See method at top for this code

            logMotor(leftFront, leftFrontPower);
            logMotor(rightFront, rightFrontPower);
            logMotor(leftRear, leftRearPower);
            logMotor (rightRear, rightRearPower);
            logMotor (armRotationMotor, armRotationPower);
            logMotor (armElevationMotor, armElevationPower);

        }
    }
}