// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/*
__________         __         ._____.           __          
\______   \_____ _/  |________|__\_ |__   _____/  |_  ______
 |     ___/\__  \\   __\_  __ \  || __ \ /  _ \   __\/  ___/
 |    |     / __ \|  |  |  | \/  || \_\ (  <_> )  |  \___ \ 
 |____|    (____  /__|  |__|  |__||___  /\____/|__| /____  >
                \/                    \/                 \/ 
           ______________________    ______                 
          /  |  \______  \_____  \  /  __  \                
         /   |  |_  /    / _(__  <  >      <                
        /    ^   / /    / /       \/   --   \               
        \____   | /____/ /______  /\______  /               
             |__|               \/        \/                                                        
*/                                         


package frc.robot;

import java.util.List;

import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
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
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.*;




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

  private static final double rotDeadzone = 0.5;

  private static final double xDeadZone = 0.1;

  // private RobotContainer m_robotContainer;

  // PhotonCamera camera = new PhotonCamera("Patribots4738");

  NetworkTable table = NetworkTableInstance.getDefault().getTable("photonvision");

  XboxController operator = new XboxController(0);

  Vision vision = new Vision();

  
  // JSON json = new JSON("data.json");

  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    // m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {

  }
  
  @Override
  public void teleopPeriodic() {


    if (operator.getLeftBumper() && operator.getRightBumper()){

      operator.setRumble(RumbleType.kBothRumble, 0.1);

    }

    else if (operator.getLeftBumper()){

      operator.setRumble(RumbleType.kLeftRumble, 1);
      operator.setRumble(RumbleType.kRightRumble, 0);

    } else if (operator.getRightBumper()){

      operator.setRumble(RumbleType.kRightRumble, 1);
      operator.setRumble(RumbleType.kLeftRumble, 0);

    } else {
      
      operator.setRumble(RumbleType.kBothRumble, 0);
    }

    }



  
  


  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    

    // CommandScheduler.getInstance().cancelAll();

    // var result = camera.getLatestResult();


  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {
    if(operator.getAButton()) {

      operator.setRumble(RumbleType.kLeftRumble, 0);

      vision.pereodic();

      double x = -1;
      double y = -1;
      double roll = -1;
      double yaw = -1;

      if (vision.hasTargets()){
        x = vision.getX();
        y = vision.getY();
        roll = vision.getPitch();
        // Positive yaw and negative yaw are reversed (For whatever godsaken reason)
        yaw = vision.getYaw();

        System.out.println("X: " + String.format("%.3f", x) + 
                          " Y: " + String.format("%.3f", y) +
                          " Roll: " + String.format("%.3f", roll) +
                          " Yaw: " + String.format("%.3f", yaw));

        // Fix the yaw of the robot (Rotate left and right)
        if (yaw > rotDeadzone){

          System.out.println("Rotate right");

        } 

        else if (yaw < -rotDeadzone){

          System.out.println("Rotate left");

        } 
        else {

          System.out.println("Perfectly acceptable");

          if (operator.getLeftBumperPressed()){

            System.out.println("Moving to left position");
            System.out.println("--------------------------------------------------");
            

          }
          else if (operator.getRightBumperPressed()){

            System.out.println("Moving to right position");
            System.out.println("--------------------------------------------------");

          }


        }

      } else {

        operator.setRumble(RumbleType.kLeftRumble, 0.5);

      }
      
    } else {
      operator.setRumble(RumbleType.kBothRumble, 0);
    }
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
