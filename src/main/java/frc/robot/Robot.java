// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonTrackedTarget;
import org.photonvision.targeting.TargetCorner;

import com.fasterxml.jackson.databind.util.JSONPObject;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.Topic;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.utils.JSON;




/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final double TARGET_HEIGHT_METERS = 0;

  private static final double CAMERA_PITCH_RADIANS = 0;

  private static final double CAMERA_HEIGHT_METERS = 0;

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  PhotonCamera camera = new PhotonCamera("Patribots4738");

  NetworkTable table = NetworkTableInstance.getDefault().getTable("photonvision");

  XboxController operator = new XboxController(0);

  
  JSON json = new JSON("data.json");

  // SmartDashboard dashboard = new SmartDashboard();
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}



  
  //   PhotonTrackedTarget target = camera.getLatestResult().getBestTarget();
  // double yaw = target.getYaw();
  // double pitch = target.getPitch();
  // double area = target.getArea();
  // double skew = target.getSkew();
  // Transform2d pose = target.getCameraToTarget()


  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    

    CommandScheduler.getInstance().cancelAll();

    var result = camera.getLatestResult();


  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    if(operator.getAButton()) {

      var result = camera.getLatestResult();

      if(result.hasTargets()) {
        double x = result.getBestTarget().getBestCameraToTarget().getTranslation().getX();
        double y = result.getBestTarget().getBestCameraToTarget().getTranslation().getY();
        System.out.println("Target: " + result.getBestTarget().getBestCameraToTarget());
        SmartDashboard.putNumber("X",x);
        SmartDashboard.putNumber("Y",y);
        json.newLevel();
        json.put("X", Double.toString(x));
        json.put("Y", Double.toString(y));        
        json.put("Time", Double.toString(Timer.getFPGATimestamp()));
        json.closeLevel();
        
      }else{
        SmartDashboard.putNumber("X", -1);
        SmartDashboard.putNumber("Y", -1);
        json.newLevel();
        json.put("X", "-1");
        json.put("Y", "-1");
        json.closeLevel();
      }
      
    }
    if (operator.getBButtonPressed()){
      String data = json.getData();
      SmartDashboard.putString("data", data);
      System.out.println(data);
    }
    

    
    
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
