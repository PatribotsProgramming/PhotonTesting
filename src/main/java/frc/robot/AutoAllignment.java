package frc.robot;


import java.util.HashMap;

import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.utils.JSON;

import frc.robot.subsystems.Vision;
import frc.robot.hardware.Swerve;

public class AutoAllignment {
    private static final double rotDeadzone = 0.1;

    private static final double xDeadZone = 0.1;

    private static final double allignmentSpeed = 0.05;

    private static final double allignmentRotSpeed = 0.05;

    Vision visionSubsystem = new Vision();

    XboxController operator;

    Swerve swerve;


    public AutoAllignment(XboxController operator, Swerve driveTrain){

        this.operator = operator;

        this.swerve = driveTrain;

    }


    public void pereodic(){

      // Use the A button to activate the allignment process
      if (operator.getAButton()){
          
          // Run the vision calculations and get the most visible tag
          visionSubsystem.pereodic();

          // Make sure that the camera has tags in view
          if (visionSubsystem.hasTargets()){

            // Get all the data we need for allignment
            double yaw = visionSubsystem.getYaw();
            double x = visionSubsystem.getX();
            int tagID = visionSubsystem.getTagID();

            // Direction is Left: -1, Right: 1, Default: 0
            int direction = 0;
    
            if (yaw > rotDeadzone){
              // Rotate the robot in the negative yaw direction
              swerve.drive(0, 0, -allignmentRotSpeed, true);
            }
            
            else if (yaw < -rotDeadzone){
              // Rotate the robot in the positive yaw direction
              swerve.drive(0, 0, allignmentRotSpeed, true);
            }
    
            /**
             * The operator has two buttons they can press, 
             * one moves the robot to the left if there's a valid position to the left,
             * and the other does the same for the right.
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

              // Check the horizontal allignment of the robot relative to the tag
              if (x > xDeadZone){
                // Move the robot left at x speed
                swerve.drive(-allignmentSpeed, 0, 0, true);
              }
              else if (x < -xDeadZone){
                // Move the robot right at x speed
                swerve.drive(allignmentSpeed, 0, 0, true);
              }
              else {
                // Stop the robot
                swerve.drive(0, 0, 0, true);
              }
    
              // The operator chooses whether to go to the cone position to the left or right of the tag
              if (operator.getLeftBumper()){
                direction = -1;
              }
              else if (operator.getRightBumper()){
                direction = 1;
              }

              // If the operator has chosen left, move the robot to the left cone position
              if (direction == -1){
                
                // Move the robot to the left cone position
                swerve.drive(0, 0, 0, true);

              }
            }
          }
    
          // Make the controller buzz if there are no targets in view
          else {
            operator.setRumble(RumbleType.kBothRumble, 0.5);
          }
      }
}
