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
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
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

  private static final double rotDeadzone = 0.1;

  private static final double xDeadZone = 0.1;

  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  PhotonCamera camera = new PhotonCamera("Patribots4738");

  NetworkTable table = NetworkTableInstance.getDefault().getTable("photonvision");

  XboxController operator = new XboxController(0);

  
  JSON json = new JSON("data.json");

  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {

  }
  
  @Override
  public void teleopPeriodic() {

    // Use the A button to acrivate the allignment process
    if (operator.getAButton()){

      // Get the latest data from photonvision
      PhotonPipelineResult result = camera.getLatestResult();

      // Make sure that the camera has tags in view
      if (result.hasTargets()){

        PhotonTrackedTarget target = result.getBestTarget();

        double yaw = target.getYaw();
        double pitch = target.getPitch();
        Transform3d pose = target.getBestCameraToTarget();
        int tagID = target.getFiducialId();
        
        // Direction is Left: -1, Right: 1, Default: 0
        int direction = 0;

        if (yaw > rotDeadzone){
          // Rotate the robot in the negative yaw direction
        }
        
        else if (yaw < -rotDeadzone){
          // Rotate the robot in the positive yaw direction
        }

        /**
         * The operator has two buttons they can press, 
         * one to move to the position to the left of the tag or to the right.
         * 
         * The distances for these positions are set as an array tagPositions,
         * which follows format:
         * {1:[leftDist, rightDist], 2:[leftDist, rightDist], ...}
         */

        /**
         *  A visual representation of the apriltag positions
         *  / --------------------------------------------- \ 
         *  5                      |                        4
         *  |                      |                        |
         *  |                      |                        |
         *  6                      |                        3
         *  |                      |                        |
         *  7                      |                        2
         *  |                      |                        |
         *  8                      |                        1
         *  \ --------------------------------------------- /
         */

        else {

          // Get the direction the operator wants to move
          if (operator.getLeftBumper()){
            direction = -1;
          }
          else if (operator.getRightBumper()){
            direction = 1;
          }
          
            // There are different distances for each tag
            switch(tagID){
              case 1:
                if (direction == 1){
                  // Move the robot right x amount
                }
                else if (direction == -1){
                  // Move the robot left x amount
                }
                break;

              case 2:
                if (direction == 1){
                  // Move the robot right x amount
                }
                else if (direction == -1){
                  // Move the robot left x amount
                }
                break;
              
              case 3:
                if (direction == 1){
                  // Move the robot right x amount
                }
                else if (direction == -1){
                  // Move the robot left x amount
                }
                break;
              
              case 6:
                if (direction == 1){
                  // Move the robot right x amount
                }
                else if (direction == -1){
                  // Move the robot left x amount
                }
                break;

              case 7:
                if (direction == 1){
                  // Move the robot right x amount
                }
                else if (direction == -1){
                  // Move the robot left x amount
                }
                break;

              case 8:
                if (direction == 1){
                  // Move the robot right x amount
                }
                else if (direction == -1){
                  // Move the robot left x amount
                }
                break;
              
              // Makes the controller buzz if pointed at an invalid tag
              default:
                operator.setRumble(RumbleType.kBothRumble, 0.5);

            }
          }
        }
      }

      // Make the controller buzz if there are no targets in view
      else {
        operator.setRumble(RumbleType.kBothRumble, 0.5);
      }

    }



  
  


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
