package com.team3418.frc2016;

// import classes used in main robot program
import com.team3418.frc2016.Constants;
import com.team3418.frc2016.subsystems.Shooter;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.RobotDrive.MotorType;

/**
 * The main robot class, which instantiates all robot parts and helper classes.
 * After initializing all robot parts, the code sets up the autonomous.
 */
public class Robot extends IterativeRobot {
    // Subsystems
	Shooter mShooter = new Shooter();
	
    // Other parts of the robot
    ControlBoard mControls = ControlBoard.getInstance();
    RobotDrive mDrive = new RobotDrive(Constants.kLeftMotorPWMID, Constants.kRightMotorPWMID);
    
    
	double mNow;
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    
    
    private void stopAllSubsystems(){
    	mShooter.stop();
	}
    
    private void updateAllSubsystems() {
    	mShooter.updateSubsystemState();
    }
    
    
    @Override
    public void robotInit() {
    	//set initial wanted states for all subsystems

    	

    	stopAllSubsystems();
    	updateAllSubsystems();
    }
    
    @Override
    public void disabledInit() {
    	stopAllSubsystems();
    	updateAllSubsystems();
    }
    
    @Override
    public void autonomousInit() {
    	stopAllSubsystems();
    	updateAllSubsystems();
    }
    
    @Override
    public void teleopInit() {
    	//set subsystems to state wanted at beginning of teleop
    	stopAllSubsystems();
    	updateAllSubsystems();
    	}
    
    @Override
    public void disabledPeriodic() {
    	
    }
    
    @Override
    public void teleopPeriodic() {
    	//set states of subsystems depending on operator controls or the state of other subsystems
    	
    	mNow = Timer.getFPGATimestamp();
    	
    	
    	if(mControls.decreaseShooterSetpointButton()){
    		if (mShooter.getTargetRpm() > 0){
        		mShooter.setTargetRpm(mShooter.getTargetRpm() - 10);
    		}
    	} else if(mControls.increaseShooterSetpointButton()){
    		mShooter.setTargetRpm(mShooter.getTargetRpm() + 10);
    	}
    	
    	
    	if(mControls.spoolShooter()){
    		mShooter.setRpm();
    	} else {
    		mShooter.stop();
    	}
    	
    	
    	
    	
    	    	
    	// simple drive control
    	mDrive.tankDrive(mControls.getLeftThrottle(), mControls.getRightThrottle());



    	// update subsystem states
    	updateAllSubsystems();
    	//
    }
    
    @Override
    public void autonomousPeriodic() {
    	
    }
    
    /*
    private void updateDriverFeedback() {
    	
    }
    */
}
