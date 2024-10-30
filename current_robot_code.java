
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "CodeTest")
public class CodeTest extends LinearOpMode {

    // Experimental code, non-functional
    private static void rampMotor(DcMotor motor, double motorPower, double rampSpeed) {

        if (motorPower < 0.2 && motorPower > -0.2) {
            motor.setPower(0);
        }
        
        if ((motorPower > motor.getPower()) && (motorPower - motor.getPower() >= rampSpeed)) {
            motor.setPower(motor.getPower() + rampSpeed); 
        }

        if ((motorPower < motor.getPower()) && (motor.getPower() - motorPower >= rampSpeed)) {
            motor.setPower(motor.getPower() - rampSpeed);
        }

    }
    
    // Allows less abrupt starts and stops
    private static void logMotor (DcMotor motor, double motorPower) {

        if (motorPower < 0.2 && motorPower > -0.2) { 
            motor.setPower(0); }
        
        else if (motorPower > 0) {
            motor.setPower(motorPower*motorPower); }
            
        else {
            motor.setPower(-1*motorPower*motorPower);}

    }

    // Declares Motors & Servos
    private DcMotor leftFront, rightFront, leftRear, rightRear, armElevation, armRotation, clawExtension;
    //private DigitalChannel Ls_ArmRotation, Ls_ArmElevation, Ls_ArmExtension;
    private Servo Servo_R, Servo_C;

    private boolean lock = false;
    private double rampSpeed = 0.2;
    private double speed;
    private int clawState = 1;

    @Override
    public void runOpMode() throws InterruptedException {

        // Declares Variables for Motors & Sevos
    
        leftFront = hardwareMap.get(DcMotor.class, "LeftFront");
        rightFront = hardwareMap.get(DcMotor.class, "RightFront");
        rightRear = hardwareMap.get(DcMotor.class, "RightRear");
        leftRear = hardwareMap.get(DcMotor.class, "LeftRear");
        
        //Ls_ArmRotation = hardwareMap.get(DigitalChannel.class, "Ls_ArmRotation");
        //Ls_ArmElevation = hardwareMap.get(DigitalChannel.class, "Ls_ArmElevation");
        //Ls_ArmExtension = hardwareMap.get(DigitalChannel.class, "Ls_ArmExtension");
        //armRotation = hardwareMap.get(DcMotor.class, "ArmRotation");
        armElevation = hardwareMap.get(DcMotor.class, "ArmElevation");
        //clawExtension = hardwareMap.get(DcMotor.class, "ClawExtension");*/
        Servo_R = hardwareMap.get(Servo.class, "Servo_R"); 
        Servo_C = hardwareMap.get(Servo.class, "Servo_C");
        
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        
        armElevation.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        waitForStart();

        while (opModeIsActive()) {

            double x = gamepad1.left_stick_x;
            double y = -gamepad1.left_stick_y;
            double rotation = gamepad1.right_stick_x;
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
            
            leftRearPower *= speed;
            rightRearPower *= speed;
            leftFrontPower *= speed;
            rightFrontPower *= speed;
            
            // See method at top for this code

            logMotor(leftFront, leftFrontPower);
            logMotor(rightFront, rightFrontPower);
            logMotor(leftRear, leftRearPower);
            logMotor (rightRear, rightRearPower);
            
            // Sets drone launcher to on/off position

            if (gamepad2.left_trigger > 0.25)

                Servo_R.setPosition(1.0);

            else 

                Servo_R.setPosition(0.0);

            
            // Sets to a position until a new one is set
            
            if (gamepad2.b)
                
                clawState = 1;
                
            else if (gamepad2.y)
                
                clawState = 2;
                
            else if (gamepad2.a)
                
                clawState = 3;
                
            else if (gamepad2.x)
                
                clawState = 4;

            /*
             * Y - Single Pixel Open
             * B - Single Pixel Grab
             * X - Double Pixel Open
             * A - Double Pixel Grab
             */
            
            if (clawState == 1)

                Servo_C.setPosition(0.2);

            else if (clawState == 2)

                Servo_C.setPosition(0.35);

            else if (clawState == 3)

                Servo_C.setPosition(0.15);

            else
                
                Servo_C.setPosition(0.25);


            // Lock - Auto retracts the arm at a faster speed

            if (gamepad2.dpad_left)
                
                lock = true;
            
            else if (gamepad2.dpad_right)
                
                lock = false;

            // Sets the arm motor to retract/extend

            if (lock == true)
                
                armElevation.setPower(0.4);
            
            else if (gamepad2.dpad_down)

                armElevation.setPower(0.3);

            else if (gamepad2.dpad_up)

                armElevation.setPower(-0.3);
                
            else

                armElevation.setPower(0);


            telemetry.addData("Motor", armElevation.getCurrentPosition());
            telemetry.update();

        }
    }
}