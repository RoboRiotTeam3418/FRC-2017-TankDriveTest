
package com.team3418.frc2016.subsystems;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;
import com.team3418.frc2016.Constants;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter extends Subsystem {

	
	static Shooter mInstance = new Shooter();

    public static Shooter getInstance() {
        return mInstance;
    }    
    
    private CANTalon mShooterTalon = new CANTalon(0);
	
    
    public Shooter() {
    	//initialize shooter hardware settings
		System.out.println("Shooter Initialized");
		//leftshooter
						
		mShooterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		if (mShooterTalon.isSensorPresent(
                CANTalon.FeedbackDevice.CtreMagEncoder_Relative) != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
            DriverStation.reportError("Could not detect shooter encoder!", false);
		}
		
		mShooterTalon.enableBrakeMode(false);
		
		mShooterTalon.enableBrakeMode(false);
		
		mShooterTalon.reverseSensor(true);
		mShooterTalon.reverseOutput(false);
		
		mShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mShooterTalon.set(0);
		
		mShooterTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		mShooterTalon.configPeakOutputVoltage(+12.0f, 0f);
		
		mShooterTalon.setProfile(0);
		mShooterTalon.setPID(Constants.kFlywheelKp, Constants.kFlywheelKi, Constants.kFlywheelKd, Constants.kFlywheelKf,
                Constants.kFlywheelIZone, Constants.kFlywheelRampRate, 0);
		mShooterTalon.setAllowableClosedLoopErr(Constants.kFlywheelAllowableError);
		
		
		mTargetRpm = 2400;
		}
    
    public enum ShooterReadyState {
    	READY, NOT_READY
    }

    
    //
    private double mTargetRpm;
       
    public void setTargetRpm(double rpm){
    	mTargetRpm = rpm;
    }
    
    public double getTargetRpm(){
    	return mTargetRpm;
    }
    
    //
    
    //
    private ShooterReadyState mShooterReadyState;
    
    public ShooterReadyState getShooterState(){
    	return mShooterReadyState;
    }
    
    public ShooterReadyState getShooterReadyState(){
    	return mShooterReadyState;
    }
    //
    
    
    
	@Override
	public void updateSubsystemState() {
		
		//printShooterInfo();
		outputToSmartDashboard();
		
		if (isOnTarget()){
			mShooterReadyState = ShooterReadyState.READY;
		} else {
			mShooterReadyState = ShooterReadyState.NOT_READY;
		}
	}
	
	
	
	/*print shooter info to console
	private void printShooterInfo(){
		System.out.println(" out: "+getMotorOutput()+
				   " spd: "+mShooterTalon.getSpeed()+
		           " err: "+mShooterTalon.getClosedLoopError()+
		           " trgrpm: "+getTargetRpm()+
		           " trgspd: "+getTargetSpeed());
	}
	*/
	
	
	//get shooter speed info methods
	private double getRpm(){
		return mShooterTalon.getSpeed();
	}
	
	private double getSetpoint(){
		return mShooterTalon.getSetpoint();
	}
	//
	
	//set shooter speed methods
	public void setRpm(double rpm){
		mShooterTalon.changeControlMode(TalonControlMode.Speed);
		mShooterTalon.set(rpm);
	}
	
	public void setOpenLoop(double speed){
		mShooterTalon.changeControlMode(TalonControlMode.PercentVbus);
		mShooterTalon.set(speed);
	}
	
	public void stop(){
		setOpenLoop(0);
	}
	//
	
	//set shooter ready state
	private boolean isOnTarget(){
		return (mShooterTalon.getControlMode() == CANTalon.TalonControlMode.Speed
                && Math.abs(getRpm() - getSetpoint()) < Constants.kFlywheelOnTargetTolerance);
	}
	//
	
	

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Flywheel_rpm", getRpm());
        SmartDashboard.putNumber("Flywheel_Setpoint", mShooterTalon.getSetpoint());
        SmartDashboard.putBoolean("Flywheel_On_Target", isOnTarget());
        SmartDashboard.putNumber("Flywheel_Closed_Loop_error", mShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Flywheel_Closed_Loop_error_Number", mShooterTalon.getClosedLoopError());
        SmartDashboard.putNumber("Flywheel_Output_Current", mShooterTalon.getOutputCurrent());
        SmartDashboard.putNumber("Encoder_Velocity", mShooterTalon.getEncVelocity());
        SmartDashboard.putNumber("Closed_Loop_Ramp_Rate", mShooterTalon.getCloseLoopRampRate());
	}
    
    
    
    
}

