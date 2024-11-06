
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "CurrentRobotCode")
public class CurrentRobotCode extends LinearOpMode {
    
    // Allows less abrupt starts and stops
    private static void logMotor (DcMotor motor, double motorPower) {

        // Doesn't move?
        // If it slows down it just stops
        // Acceleration?
        if (motorPower > 0) {
            motor.setPower(motorPower*motorPower); }
            
        else {
            motor.setPower(-1*motorPower*motorPower);}

    }

    // Declares Motors & Servos
    private DcMotor leftFront, rightFront, leftRear, rightRear, armElevation, armRotation, clawExtension;
    private Servo Servo_R, Servo_C;

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

        armElevation = hardwareMap.get(DcMotor.class, "ArmElevation");
        Servo_R = hardwareMap.get(Servo.class, "Servo_R"); 
        Servo_C = hardwareMap.get(Servo.class, "Servo_C");
        
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        // The encoder is probably the black motor of the motor at the back
        // that records how much it spins and stuff
        armElevation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        waitForStart();

        // This is the main part of the program
        while (opModeIsActive()) {

            // Deriving the power from the left and right paddles on the controller
            double x = -gamepad1.left_stick_x;
            double y = gamepad1.left_stick_y;
            double rotation = -gamepad1.right_stick_x;
            double leftRearPower = -x + y + rotation;
            double rightRearPower = -x - y + rotation;
            double leftFrontPower = x + y + rotation;
            double rightFrontPower = -x + y - rotation;

            // Sets three different speed options
            if (gamepad1.left_bumper)

                speed = 0.4;
            
            else if (gamepad1.right_bumper)

                speed = 0.8;
                
            else
                
                speed = 0.6;
            
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

        }
    }
}