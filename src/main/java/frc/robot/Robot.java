package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.buttons.POVButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  private final VictorSPX leftRear = new VictorSPX(1);
  private final VictorSPX leftFront = new VictorSPX(2);

  private final VictorSPX rightRear = new VictorSPX(3);
  private final VictorSPX rightFront = new VictorSPX(4);

  private final VictorSPX armLift = new VictorSPX(7);
  private final VictorSPX intake = new VictorSPX(0);

  private final VictorSPX hanger = new VictorSPX(6);
  private final VictorSPX tilter = new VictorSPX(5);

  private final Joystick js = new Joystick(0);
  private final Timer m_timer = new Timer();
  

  public static Double armspeed = 0.5;
  public static Double intakespeed = -0.3;
  public static Double hangspeed = 0.7;
  public static double tiltspeed = 0.5;


  
  Button D_Button = new JoystickButton(js, 1);
  Button X_Button = new JoystickButton(js, 2);
  Button O_Button = new JoystickButton(js, 3);
  Button T_Button = new JoystickButton(js, 4);
  Button L1_Button = new JoystickButton(js, 5);
  Button R1_Button = new JoystickButton(js, 6);
  Button L2_Button = new JoystickButton(js, 7);
  Button R2_Button = new JoystickButton(js, 8);
  Button Share_Button = new JoystickButton(js, 9);
  Button Option_Button = new JoystickButton(js, 10);
  Button L3_Button = new JoystickButton(js, 11);
  Button R3_Button = new JoystickButton(js, 12);
  Button PS_Button = new JoystickButton(js, 13);
  Button TouchPad = new JoystickButton(js, 14);

  POVButton DPad_Up = new POVButton(js, 0);
  POVButton DPad_Right = new POVButton(js, 90);
  POVButton DPad_Left = new POVButton(js, 270);

  protected void execute() {
    SmartDashboard.putNumber("Tilter Temp", tilter.getTemperature());
    SmartDashboard.putNumber("Arm Temp", armLift.getTemperature());
    SmartDashboard.putNumber("Intake Temp", intake.getTemperature());
    SmartDashboard.putNumber("Hanger Temp", hanger.getTemperature());
    SmartDashboard.putNumber("Drivetrain Avg Temp", (leftFront.getTemperature()+rightFront.getTemperature()+leftRear.getTemperature()+rightRear.getTemperature())/4);

    SmartDashboard.putNumber("Tilter Voltage", tilter.getBusVoltage());
    SmartDashboard.putNumber("Arm Voltage", armLift.getBusVoltage());
    SmartDashboard.putNumber("Intake Voltage", intake.getBusVoltage());
    SmartDashboard.putNumber("Hanger Voltage", hanger.getBusVoltage());

    SmartDashboard.putNumber("Tilter Motor Output Percentage", tilter.getMotorOutputPercent());
    SmartDashboard.putNumber("Arm Motor Output Percentage", armLift.getMotorOutputPercent());
    SmartDashboard.putNumber("Intake Motor Output Percentage", intake.getMotorOutputPercent());
    SmartDashboard.putNumber("Hanger Motor Output Percentage", hanger.getMotorOutputPercent());
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    // canMotor0.set(ControlMode.PercentOutput, 0);
  }

  /** This function is run once each time the robot ent ers autonomous mode. */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    /*
    // Drive for 2 seconds
    if (m_timer.get() < 2.0) {
      canMotor0.set(ControlMode.PercentOutput, 0.5); // drive forwards half speed
    } else {
      canMotor0.set(ControlMode.PercentOutput, 0); // stop robot
    }
    */
  }

  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {
      armLift.setNeutralMode(NeutralMode.Brake);
      hanger.setNeutralMode(NeutralMode.Brake);

  }

  /** This function is called periodically during teleoperated mode. */
  @Override
  public void teleopPeriodic() {

    double forward = js.getRawAxis(1);
		double turn = js.getRawAxis(2);		
		forward = Deadband(forward);
		turn = Deadband(turn);

		/* Arcade Drive using PercentOutput along with Arbitrary Feed Forward supplied by turn */
		leftFront.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn/1.8);
		leftRear.set(ControlMode.PercentOutput, forward, DemandType.ArbitraryFeedForward, +turn/1.8);

    rightFront.set(ControlMode.PercentOutput, -forward, DemandType.ArbitraryFeedForward, +turn/1.8);
		rightRear.set(ControlMode.PercentOutput, -forward, DemandType.ArbitraryFeedForward, +turn/1.8);

    //arm
    if(js.getRawButton(5)){
      armLift.set(ControlMode.PercentOutput, armspeed);
    }
    else if (js.getRawButton(7)){
      armLift.set(ControlMode.PercentOutput, -armspeed);
    }
    else{
      armLift.set(ControlMode.PercentOutput, 0);
    }

    //intake
    if(js.getRawButton(6)){
      intake.set(ControlMode.PercentOutput, intakespeed);
    }
    else if (js.getRawButton(8)){
      intake.set(ControlMode.PercentOutput, -intakespeed);
    }
    else{
      intake.set(ControlMode.PercentOutput, 0);
    }

    //hanger
    if(js.getRawButton(4)){
      hanger.set(ControlMode.PercentOutput, hangspeed);
    }
    else if (js.getRawButton(2)){
      hanger.set(ControlMode.PercentOutput, -hangspeed);
    }
    else{
      hanger.set(ControlMode.PercentOutput, 0);
    }

    //tilter
    if(js.getRawButton(1)){
      tilter.set(ControlMode.PercentOutput, tiltspeed);
    }
    else if (js.getRawButton(3)){
      tilter.set(ControlMode.PercentOutput, -tiltspeed);
    }
    else{
      tilter.set(ControlMode.PercentOutput, 0);
    }




  }
  
  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  double Deadband(double value) {
		/* Upper deadband */
		if (value >= +0.05) 
			return value;
		
		/* Lower deadband */
		if (value <= -0.05)
			return value;
		
		/* Outside deadband */
		return 0;
	}
  
}
